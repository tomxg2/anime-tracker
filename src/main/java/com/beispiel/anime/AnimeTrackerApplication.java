package com.beispiel.anime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Einstiegspunkt der Applikation.
 *
 * @SpringBootApplication aktiviert:
 *  - Auto-Configuration (z.B. MongoDB-Verbindung aus application.properties)
 *  - Component-Scan ab diesem Package abwaerts (findet Controller, Service, Repository)
 */
@SpringBootApplication
public class AnimeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimeTrackerApplication.class, args);
    }
}
