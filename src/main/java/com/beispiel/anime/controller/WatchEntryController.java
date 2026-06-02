package com.beispiel.anime.controller;

import com.beispiel.anime.dto.GenreRatingResult;
import com.beispiel.anime.dto.StatusCountResult;
import com.beispiel.anime.dto.TopAnimeResult;
import com.beispiel.anime.dto.UserEpisodesResult;
import com.beispiel.anime.dto.UserRatingResult;
import com.beispiel.anime.model.WatchEntry;
import com.beispiel.anime.service.WatchEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-Endpoints rund um Watchlist-Eintraege.
 * Basis-Pfad: /api/entries
 */
@RestController
@RequestMapping("/api/entries")
public class WatchEntryController {

    private final WatchEntryService watchEntryService;

    public WatchEntryController(WatchEntryService watchEntryService) {
        this.watchEntryService = watchEntryService;
    }

    /** GET /api/entries - alle Eintraege. */
    @GetMapping
    public List<WatchEntry> getAll() {
        return watchEntryService.findAll();
    }

    /** GET /api/entries/{id} - ein Eintrag per ID. */
    @GetMapping("/{id}")
    public ResponseEntity<WatchEntry> getById(@PathVariable String id) {
        return watchEntryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/entries - neuen Eintrag anlegen. */
    @PostMapping
    public ResponseEntity<WatchEntry> create(@RequestBody WatchEntry entry) {
        WatchEntry saved = watchEntryService.create(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** PUT /api/entries/{id} - Eintrag aktualisieren. */
    @PutMapping("/{id}")
    public ResponseEntity<WatchEntry> update(@PathVariable String id, @RequestBody WatchEntry entry) {
        return watchEntryService.update(id, entry)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/entries/{id} - Eintrag loeschen. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return watchEntryService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /** GET /api/entries/user/{user} - Filter ueber @Query-Methode. */
    @GetMapping("/user/{user}")
    public List<WatchEntry> getByUser(@PathVariable String user) {
        return watchEntryService.findByUser(user);
    }

    // ---------------- Aggregationen ----------------

    /** GET /api/entries/stats/episodes-per-user */
    @GetMapping("/stats/episodes-per-user")
    public List<UserEpisodesResult> episodesPerUser() {
        return watchEntryService.episodesPerUser();
    }

    /** GET /api/entries/stats/per-status */
    @GetMapping("/stats/per-status")
    public List<StatusCountResult> countPerStatus() {
        return watchEntryService.countPerStatus();
    }

    /** GET /api/entries/stats/avg-rating-per-user */
    @GetMapping("/stats/avg-rating-per-user")
    public List<UserRatingResult> averageRatingPerUser() {
        return watchEntryService.averageRatingPerUser();
    }

    /** GET /api/entries/stats/top-rated (Aggregation mit $lookup) */
    @GetMapping("/stats/top-rated")
    public List<TopAnimeResult> topRatedAnime() {
        return watchEntryService.topRatedAnime();
    }

    /** GET /api/entries/stats/avg-rating-per-genre (Aggregation mit $lookup) */
    @GetMapping("/stats/avg-rating-per-genre")
    public List<GenreRatingResult> averageRatingPerGenre() {
        return watchEntryService.averageRatingPerGenre();
    }
}
