package com.beispiel.anime.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Document-Klasse 2: Ein Watchlist-Eintrag eines Benutzers fuer einen Anime.
 *
 * Beziehung (REFERENZIERT): Statt den ganzen Anime einzubetten, speichern wir
 * hier nur dessen ID im Feld 'animeId'. Das entspricht einer 1:N-Beziehung
 * (ein Anime kann in vielen WatchEntries vorkommen). Die Verknuepfung wird in
 * den Aggregationen ueber $lookup wieder aufgeloest.
 */
@Document(collection = "watch_entries")
public class WatchEntry {

    @Id
    private String id;

    /** REFERENZ auf Anime.id (Fremdschluessel-Idee in MongoDB). */
    private String animeId;

    private String user;

    /** Bewertung von 1 bis 10. */
    private int rating;

    /** Wie viele Episoden der User bereits gesehen hat. */
    private int episodesWatched;

    private WatchStatus status;

    public WatchEntry() {
    }

    public WatchEntry(String animeId, String user, int rating, int episodesWatched, WatchStatus status) {
        this.animeId = animeId;
        this.user = user;
        this.rating = rating;
        this.episodesWatched = episodesWatched;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnimeId() {
        return animeId;
    }

    public void setAnimeId(String animeId) {
        this.animeId = animeId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getEpisodesWatched() {
        return episodesWatched;
    }

    public void setEpisodesWatched(int episodesWatched) {
        this.episodesWatched = episodesWatched;
    }

    public WatchStatus getStatus() {
        return status;
    }

    public void setStatus(WatchStatus status) {
        this.status = status;
    }
}
