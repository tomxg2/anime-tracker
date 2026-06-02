package com.beispiel.anime.config;

import com.beispiel.anime.model.Anime;
import com.beispiel.anime.model.Season;
import com.beispiel.anime.model.WatchEntry;
import com.beispiel.anime.model.WatchStatus;
import com.beispiel.anime.repository.AnimeRepository;
import com.beispiel.anime.repository.WatchEntryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Befuellt die Datenbank beim Start mit Testdaten - aber nur, wenn die
 * Collections noch leer sind. So funktioniert die Demo sofort, ohne dass
 * man manuell Daten anlegen muss.
 *
 * CommandLineRunner.run(...) wird von Spring Boot nach dem Start aufgerufen.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final AnimeRepository animeRepository;
    private final WatchEntryRepository watchEntryRepository;

    public DataSeeder(AnimeRepository animeRepository, WatchEntryRepository watchEntryRepository) {
        this.animeRepository = animeRepository;
        this.watchEntryRepository = watchEntryRepository;
    }

    @Override
    public void run(String... args) {
        if (animeRepository.count() > 0) {
            return; // Daten bereits vorhanden -> nichts tun.
        }

        // --- Anime anlegen (IDs werden von MongoDB vergeben) ---
        Anime evangelion = animeRepository.save(new Anime(
                "Neon Genesis Evangelion", "Gainax",
                List.of("Mecha", "Psychological", "Drama"), 26,
                List.of(new Season(1, 26, 1995))));

        Anime fmab = animeRepository.save(new Anime(
                "Fullmetal Alchemist: Brotherhood", "Bones",
                List.of("Action", "Adventure", "Fantasy"), 64,
                List.of(new Season(1, 64, 2009))));

        Anime steinsGate = animeRepository.save(new Anime(
                "Steins;Gate", "White Fox",
                List.of("Sci-Fi", "Thriller", "Drama"), 24,
                List.of(new Season(1, 24, 2011))));

        Anime aot = animeRepository.save(new Anime(
                "Attack on Titan", "Wit Studio",
                List.of("Action", "Drama", "Fantasy"), 87,
                List.of(new Season(1, 25, 2013), new Season(2, 12, 2017), new Season(3, 22, 2018))));

        Anime bebop = animeRepository.save(new Anime(
                "Cowboy Bebop", "Sunrise",
                List.of("Action", "Sci-Fi", "Drama"), 26,
                List.of(new Season(1, 26, 1998))));

        Anime deathNote = animeRepository.save(new Anime(
                "Death Note", "Madhouse",
                List.of("Thriller", "Psychological", "Drama"), 37,
                List.of(new Season(1, 37, 2006))));

        // --- Watchlist-Eintraege anlegen (referenzieren Anime per ID) ---
        watchEntryRepository.saveAll(List.of(
                new WatchEntry(evangelion.getId(), "tom", 10, 26, WatchStatus.COMPLETED),
                new WatchEntry(steinsGate.getId(), "tom", 9, 24, WatchStatus.COMPLETED),
                new WatchEntry(aot.getId(), "tom", 8, 60, WatchStatus.WATCHING),
                new WatchEntry(bebop.getId(), "tom", 7, 0, WatchStatus.PLAN_TO_WATCH),
                new WatchEntry(fmab.getId(), "marco", 10, 64, WatchStatus.COMPLETED),
                new WatchEntry(deathNote.getId(), "marco", 7, 37, WatchStatus.COMPLETED),
                new WatchEntry(bebop.getId(), "marco", 9, 26, WatchStatus.COMPLETED),
                new WatchEntry(evangelion.getId(), "luigi", 9, 26, WatchStatus.COMPLETED),
                new WatchEntry(aot.getId(), "luigi", 8, 87, WatchStatus.COMPLETED),
                new WatchEntry(steinsGate.getId(), "luigi", 8, 10, WatchStatus.DROPPED)
        ));

        System.out.println(">> Testdaten geladen: "
                + animeRepository.count() + " Anime, "
                + watchEntryRepository.count() + " Watchlist-Eintraege.");
    }
}
