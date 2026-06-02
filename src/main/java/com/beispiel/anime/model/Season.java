package com.beispiel.anime.model;

/**
 * EMBEDDED-Objekt: Eine Staffel eines Anime.
 *
 * Wichtig: KEIN @Document und KEIN @Id. Season ist kein eigenstaendiges
 * Dokument, sondern wird als Teil des Anime-Dokuments eingebettet
 * (siehe Anime.seasons). In MongoDB landet das als verschachteltes
 * Sub-Dokument im "seasons"-Array.
 */
public class Season {

    private int seasonNumber;
    private int episodeCount;
    private int year;

    public Season() {
    }

    public Season(int seasonNumber, int episodeCount, int year) {
        this.seasonNumber = seasonNumber;
        this.episodeCount = episodeCount;
        this.year = year;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
