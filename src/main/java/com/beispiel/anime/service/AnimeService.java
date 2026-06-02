package com.beispiel.anime.service;

import com.beispiel.anime.dto.GenreCountResult;
import com.beispiel.anime.dto.StudioCountResult;
import com.beispiel.anime.model.Anime;
import com.beispiel.anime.repository.AnimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service-Schicht fuer Anime: kapselt die Businesslogik und ruft das
 * Repository auf. Controller sprechen nur mit dem Service, nicht direkt
 * mit dem Repository (saubere Schichtentrennung).
 */
@Service
public class AnimeService {

    private final AnimeRepository animeRepository;

    // Konstruktor-Injection: Spring uebergibt das Repository automatisch.
    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public List<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Optional<Anime> findById(String id) {
        return animeRepository.findById(id);
    }

    public Anime create(Anime anime) {
        // Bei einem neuen Anime soll keine bestehende ID ueberschrieben werden.
        anime.setId(null);
        return animeRepository.save(anime);
    }

    public Optional<Anime> update(String id, Anime updated) {
        return animeRepository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setStudio(updated.getStudio());
            existing.setGenres(updated.getGenres());
            existing.setEpisodes(updated.getEpisodes());
            existing.setSeasons(updated.getSeasons());
            return animeRepository.save(existing);
        });
    }

    public boolean delete(String id) {
        if (animeRepository.existsById(id)) {
            animeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Anime> findByGenre(String genre) {
        return animeRepository.findByGenre(genre);
    }

    // --- Aggregationen durchreichen ---

    public List<StudioCountResult> countPerStudio() {
        return animeRepository.countAnimePerStudio();
    }

    public List<GenreCountResult> countPerGenre() {
        return animeRepository.countAnimePerGenre();
    }
}
