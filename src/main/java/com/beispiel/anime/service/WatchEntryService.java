package com.beispiel.anime.service;

import com.beispiel.anime.dto.GenreRatingResult;
import com.beispiel.anime.dto.StatusCountResult;
import com.beispiel.anime.dto.TopAnimeResult;
import com.beispiel.anime.dto.UserEpisodesResult;
import com.beispiel.anime.dto.UserRatingResult;
import com.beispiel.anime.model.WatchEntry;
import com.beispiel.anime.model.WatchStatus;
import com.beispiel.anime.repository.WatchEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service-Schicht fuer Watchlist-Eintraege.
 */
@Service
public class WatchEntryService {

    private final WatchEntryRepository watchEntryRepository;

    public WatchEntryService(WatchEntryRepository watchEntryRepository) {
        this.watchEntryRepository = watchEntryRepository;
    }

    public List<WatchEntry> findAll() {
        return watchEntryRepository.findAll();
    }

    public Optional<WatchEntry> findById(String id) {
        return watchEntryRepository.findById(id);
    }

    public WatchEntry create(WatchEntry entry) {
        entry.setId(null);
        return watchEntryRepository.save(entry);
    }

    public Optional<WatchEntry> update(String id, WatchEntry updated) {
        return watchEntryRepository.findById(id).map(existing -> {
            existing.setAnimeId(updated.getAnimeId());
            existing.setUser(updated.getUser());
            existing.setRating(updated.getRating());
            existing.setEpisodesWatched(updated.getEpisodesWatched());
            existing.setStatus(updated.getStatus());
            return watchEntryRepository.save(existing);
        });
    }

    public boolean delete(String id) {
        if (watchEntryRepository.existsById(id)) {
            watchEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<WatchEntry> findByUser(String user) {
        return watchEntryRepository.findByUser(user);
    }

    public List<WatchEntry> findByStatusAndMinRating(WatchStatus status, int minRating) {
        return watchEntryRepository.findByStatusAndMinRating(status, minRating);
    }

    // --- Aggregationen durchreichen ---

    public List<UserEpisodesResult> episodesPerUser() {
        return watchEntryRepository.totalEpisodesPerUser();
    }

    public List<StatusCountResult> countPerStatus() {
        return watchEntryRepository.countPerStatus();
    }

    public List<UserRatingResult> averageRatingPerUser() {
        return watchEntryRepository.averageRatingPerUser();
    }

    public List<TopAnimeResult> topRatedAnime() {
        return watchEntryRepository.findTopRatedAnime();
    }

    public List<GenreRatingResult> averageRatingPerGenre() {
        return watchEntryRepository.averageRatingPerGenre();
    }
}
