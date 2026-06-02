package com.beispiel.anime.dto;

/** Ergebnis-DTO fuer "Total gesehene Episoden pro Benutzer". */
public class UserEpisodesResult {

    private String user;
    private int totalEpisodes;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }
}
