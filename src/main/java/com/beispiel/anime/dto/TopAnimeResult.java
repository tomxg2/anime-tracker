package com.beispiel.anime.dto;

/**
 * Ergebnis-DTO fuer "Top bewertete Anime".
 * Enthaelt den Titel aus der anime-Collection, der per $lookup dazugeholt wird.
 */
public class TopAnimeResult {

    private String animeId;
    private String title;
    private double averageRating;
    private long ratingCount;

    public String getAnimeId() {
        return animeId;
    }

    public void setAnimeId(String animeId) {
        this.animeId = animeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(long ratingCount) {
        this.ratingCount = ratingCount;
    }
}
