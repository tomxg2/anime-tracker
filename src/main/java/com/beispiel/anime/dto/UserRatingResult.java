package com.beispiel.anime.dto;

/** Ergebnis-DTO fuer "Durchschnittsbewertung pro Benutzer". */
public class UserRatingResult {

    private String user;
    private double averageRating;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
