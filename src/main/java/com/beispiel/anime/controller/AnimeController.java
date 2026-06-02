package com.beispiel.anime.controller;

import com.beispiel.anime.dto.GenreCountResult;
import com.beispiel.anime.dto.StudioCountResult;
import com.beispiel.anime.model.Anime;
import com.beispiel.anime.service.AnimeService;
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
 * REST-Endpoints rund um Anime.
 * Basis-Pfad: /api/anime
 */
@RestController
@RequestMapping("/api/anime")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    /** GET /api/anime - alle Anime. */
    @GetMapping
    public List<Anime> getAll() {
        return animeService.findAll();
    }

    /** GET /api/anime/{id} - ein Anime per ID (404 falls nicht vorhanden). */
    @GetMapping("/{id}")
    public ResponseEntity<Anime> getById(@PathVariable String id) {
        return animeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/anime - neuen Anime anlegen. */
    @PostMapping
    public ResponseEntity<Anime> create(@RequestBody Anime anime) {
        Anime saved = animeService.create(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** PUT /api/anime/{id} - bestehenden Anime aktualisieren. */
    @PutMapping("/{id}")
    public ResponseEntity<Anime> update(@PathVariable String id, @RequestBody Anime anime) {
        return animeService.update(id, anime)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/anime/{id} - Anime loeschen. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return animeService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /** GET /api/anime/genre/{genre} - Filter ueber @Query-Methode. */
    @GetMapping("/genre/{genre}")
    public List<Anime> getByGenre(@PathVariable String genre) {
        return animeService.findByGenre(genre);
    }

    /** GET /api/anime/min-episodes/{min} - Filter ueber @Query-Methode. */
    @GetMapping("/min-episodes/{min}")
    public List<Anime> getByMinEpisodes(@PathVariable int min) {
        return animeService.findByMinimumEpisodes(min);
    }

    /** GET /api/anime/stats/per-studio - Aggregation: Anzahl pro Studio. */
    @GetMapping("/stats/per-studio")
    public List<StudioCountResult> countPerStudio() {
        return animeService.countPerStudio();
    }

    /** GET /api/anime/stats/per-genre - Aggregation: Anzahl pro Genre. */
    @GetMapping("/stats/per-genre")
    public List<GenreCountResult> countPerGenre() {
        return animeService.countPerGenre();
    }
}
