package com.beispiel.anime.model;

/**
 * Status eines Eintrags auf der Watchlist.
 * Wird in MongoDB automatisch als String gespeichert (z.B. "COMPLETED").
 */
public enum WatchStatus {
    PLAN_TO_WATCH,
    WATCHING,
    COMPLETED,
    DROPPED
}
