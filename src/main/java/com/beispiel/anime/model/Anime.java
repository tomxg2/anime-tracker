package com.beispiel.anime.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Document-Klasse 1: Ein Anime.
 *
 * ORM-Annotationen:
 *  @Document(collection = "anime") -> Diese Klasse wird in der MongoDB-Collection
 *                                     "anime" als BSON-Dokument gespeichert.
 *  @Id                             -> Markiert das Primaerschluessel-Feld. MongoDB
 *                                     vergibt automatisch eine ObjectId, die hier als
 *                                     String abgebildet wird.
 *
 * Beziehung (EMBEDDED): Die Liste 'seasons' wird direkt IM Anime-Dokument
 * eingebettet (kein eigenes Dokument, keine eigene Collection).
 */
@Document(collection = "anime")
public class Anime {

    @Id
    private String id;

    private String title;
    private String studio;

    /** Genres als einfaches String-Array, z.B. ["Mecha", "Drama"]. */
    private List<String> genres = new ArrayList<>();

    /** Gesamtanzahl Episoden ueber alle Staffeln. */
    private int episodes;

    /** EMBEDDED: Staffeln werden direkt im Anime-Dokument gespeichert. */
    private List<Season> seasons = new ArrayList<>();

    public Anime() {
        // Leerer Konstruktor wird von Spring Data fuer das Mapping benoetigt.
    }

    public Anime(String title, String studio, List<String> genres, int episodes, List<Season> seasons) {
        this.title = title;
        this.studio = studio;
        this.genres = genres;
        this.episodes = episodes;
        this.seasons = seasons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }
}
