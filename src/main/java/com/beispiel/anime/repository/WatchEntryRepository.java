package com.beispiel.anime.repository;

import com.beispiel.anime.dto.GenreRatingResult;
import com.beispiel.anime.dto.StatusCountResult;
import com.beispiel.anime.dto.TopAnimeResult;
import com.beispiel.anime.dto.UserEpisodesResult;
import com.beispiel.anime.dto.UserRatingResult;
import com.beispiel.anime.model.WatchEntry;
import com.beispiel.anime.model.WatchStatus;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Repository fuer die watch_entries-Collection.
 * Enthaelt die "interessanten" Aggregationen, inkl. zwei Pipelines mit
 * $lookup (Join auf die anime-Collection).
 */
public interface WatchEntryRepository extends MongoRepository<WatchEntry, String> {

    // ----------------------------------------------------------------
    //  EIGENE QUERIES (@Query)
    // ----------------------------------------------------------------

    /** Alle Eintraege eines bestimmten Benutzers. */
    @Query("{ 'user': ?0 }")
    List<WatchEntry> findByUser(String user);

    /** Alle Eintraege mit einem bestimmten Status und Mindestbewertung. */
    @Query("{ 'status': ?0, 'rating': { '$gte': ?1 } }")
    List<WatchEntry> findByStatusAndMinRating(WatchStatus status, int minRating);

    // ----------------------------------------------------------------
    //  AGGREGATIONEN (@Aggregation)
    // ----------------------------------------------------------------

    /**
     * AGGREGATION 3 - Total gesehene Episoden pro Benutzer.
     * $sum summiert episodesWatched je Benutzer.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$user', 'totalEpisodes': { '$sum': '$episodesWatched' } } }",
            "{ '$project': { '_id': 0, 'user': '$_id', 'totalEpisodes': 1 } }",
            "{ '$sort': { 'totalEpisodes': -1 } }"
    })
    List<UserEpisodesResult> totalEpisodesPerUser();

    /**
     * AGGREGATION 4 - Anzahl Eintraege pro Status (Watchlist-Verteilung).
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$status', 'count': { '$sum': 1 } } }",
            "{ '$project': { '_id': 0, 'status': '$_id', 'count': 1 } }",
            "{ '$sort': { 'count': -1 } }"
    })
    List<StatusCountResult> countPerStatus();

    /**
     * AGGREGATION 5 - Durchschnittsbewertung pro Benutzer.
     * $avg bildet den Mittelwert, $round rundet auf 2 Nachkommastellen.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$user', 'averageRating': { '$avg': '$rating' } } }",
            "{ '$project': { '_id': 0, 'user': '$_id', 'averageRating': { '$round': [ '$averageRating', 2 ] } } }",
            "{ '$sort': { 'averageRating': -1 } }"
    })
    List<UserRatingResult> averageRatingPerUser();

    /**
     * AGGREGATION 6 - Top bewertete Anime (mit JOIN).
     * Ablauf:
     *  1. $group nach animeId -> Durchschnittsbewertung + Anzahl Bewertungen
     *  2. $sort/$limit -> nur die 5 besten
     *  3. $lookup -> Titel aus der anime-Collection dazuholen.
     *     animeId ist ein String, die _id in anime ist eine ObjectId,
     *     deshalb wird mit $toObjectId konvertiert.
     *  4. $unwind + $project -> flaches Ergebnis fuer das DTO.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$animeId', 'averageRating': { '$avg': '$rating' }, 'ratingCount': { '$sum': 1 } } }",
            "{ '$sort': { 'averageRating': -1 } }",
            "{ '$limit': 5 }",
            "{ '$lookup': { 'from': 'anime', 'let': { 'aid': { '$toObjectId': '$_id' } }, 'pipeline': [ { '$match': { '$expr': { '$eq': [ '$_id', '$$aid' ] } } } ], 'as': 'anime' } }",
            "{ '$unwind': '$anime' }",
            "{ '$project': { '_id': 0, 'animeId': '$_id', 'title': '$anime.title', 'averageRating': { '$round': [ '$averageRating', 2 ] }, 'ratingCount': 1 } }"
    })
    List<TopAnimeResult> findTopRatedAnime();

    /**
     * AGGREGATION 7 - Durchschnittsbewertung pro Genre (mit JOIN).
     * Verbindet jeden WatchEntry mit seinem Anime, zerlegt dessen Genres
     * und bildet den Durchschnitt der Bewertungen je Genre.
     */
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'anime', 'let': { 'aid': { '$toObjectId': '$animeId' } }, 'pipeline': [ { '$match': { '$expr': { '$eq': [ '$_id', '$$aid' ] } } } ], 'as': 'anime' } }",
            "{ '$unwind': '$anime' }",
            "{ '$unwind': '$anime.genres' }",
            "{ '$group': { '_id': '$anime.genres', 'averageRating': { '$avg': '$rating' }, 'entryCount': { '$sum': 1 } } }",
            "{ '$project': { '_id': 0, 'genre': '$_id', 'averageRating': { '$round': [ '$averageRating', 2 ] }, 'entryCount': 1 } }",
            "{ '$sort': { 'averageRating': -1 } }"
    })
    List<GenreRatingResult> averageRatingPerGenre();
}
