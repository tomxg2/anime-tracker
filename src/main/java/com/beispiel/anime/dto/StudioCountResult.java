package com.beispiel.anime.dto;

/**
 * Ergebnis-DTO fuer "Anzahl Anime pro Studio".
 * Die Feldnamen muessen exakt zu den per $project erzeugten Feldern passen,
 * damit Spring Data das Aggregations-Ergebnis automatisch mappen kann.
 */
public class StudioCountResult {

    private String studio;
    private long count;

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
