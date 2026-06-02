package com.beispiel.anime.dto;

/**
 * Ergebnis-DTO fuer "Durchschnittsbewertung pro Genre".
 * Verbindet WatchEntry.rating mit Anime.genres ueber $lookup.
 */
public class GenreRatingResult {

    private String genre;
    private double averageRating;
    private long entryCount;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(long entryCount) {
        this.entryCount = entryCount;
    }
}
