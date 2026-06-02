package com.beispiel.anime.repository;

import com.beispiel.anime.dto.GenreCountResult;
import com.beispiel.anime.dto.StudioCountResult;
import com.beispiel.anime.model.Anime;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Repository fuer die anime-Collection.
 *
 * MongoRepository<Anime, String> liefert bereits CRUD-Methoden
 * (findAll, findById, save, deleteById, ...).
 *
 * Zusaetzlich definieren wir:
 *  - eigene Abfragen mit @Query
 *  - Aggregations-Pipelines mit @Aggregation
 */
public interface AnimeRepository extends MongoRepository<Anime, String> {

    // ----------------------------------------------------------------
    //  EIGENE QUERIES (@Query)
    // ----------------------------------------------------------------

    /**
     * Findet alle Anime, die ein bestimmtes Genre enthalten.
     * ?0 ist der erste Methodenparameter. Da 'genres' ein Array ist,
     * matcht MongoDB automatisch, wenn der Wert im Array vorkommt.
     */
    @Query("{ 'genres': ?0 }")
    List<Anime> findByGenre(String genre);

    /**
     * Findet alle Anime mit mindestens so vielen Episoden wie angegeben.
     */
    @Query("{ 'episodes': { '$gte': ?0 } }")
    List<Anime> findByMinimumEpisodes(int minEpisodes);

    // ----------------------------------------------------------------
    //  AGGREGATIONEN (@Aggregation)
    // ----------------------------------------------------------------

    /**
     * AGGREGATION 1 - Anzahl Anime pro Studio.
     * Pipeline: $group nach Studio + Zaehler, dann $project fuer saubere
     * Feldnamen und $sort absteigend.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$studio', 'count': { '$sum': 1 } } }",
            "{ '$project': { '_id': 0, 'studio': '$_id', 'count': 1 } }",
            "{ '$sort': { 'count': -1 } }"
    })
    List<StudioCountResult> countAnimePerStudio();

    /**
     * AGGREGATION 2 - Anzahl Anime pro Genre.
     * $unwind zerlegt das genres-Array, sodass jeder Genre-Eintrag eine
     * eigene Zeile wird; danach $group nach Genre.
     */
    @Aggregation(pipeline = {
            "{ '$unwind': '$genres' }",
            "{ '$group': { '_id': '$genres', 'count': { '$sum': 1 } } }",
            "{ '$project': { '_id': 0, 'genre': '$_id', 'count': 1 } }",
            "{ '$sort': { 'count': -1 } }"
    })
    List<GenreCountResult> countAnimePerGenre();
}
