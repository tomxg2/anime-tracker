# Anime Watchlist Tracker

Abschlussprojekt **NoSQL / MongoDB** — Spring Boot 3 Applikation zur Verwaltung
einer Anime-Watchlist mit Bewertungen und Statistik-Aggregationen.

---

## 1. Voraussetzungen

| Tool | Version |
|------|---------|
| Java JDK | 17 oder neuer |
| Maven | 3.6+ (oder die IDE-integrierte Variante) |
| MongoDB | lokal laufend auf `localhost:8080` |

MongoDB starten (Beispiel):

```bash
# Linux/macOS (Service)
sudo systemctl start mongod
# oder direkt
mongod --dbpath /pfad/zu/deinen/daten
```

Die Datenbank `animedb` wird automatisch beim ersten Schreibzugriff erstellt —
nichts manuell anlegen.

---

## 2. Starten

```bash
# im Projektordner (dort wo die pom.xml liegt)
mvn spring-boot:run
```

Alternativ als JAR:

```bash
mvn clean package
java -jar target/anime-tracker-1.0.0.jar
```

Beim ersten Start lädt der `DataSeeder` automatisch Testdaten
(6 Anime, 10 Watchlist-Einträge). In der Konsole erscheint:

```
>> Testdaten geladen: 6 Anime, 10 Watchlist-Eintraege.
```

Die App läuft anschliessend auf <http://localhost:8080>.

---

## 3. REST-Endpoints

### Anime (`/api/anime`)

| Methode | Pfad | Beschreibung |
|---------|------|--------------|
| GET | `/api/anime` | Alle Anime |
| GET | `/api/anime/{id}` | Ein Anime per ID |
| POST | `/api/anime` | Neuen Anime anlegen |
| PUT | `/api/anime/{id}` | Anime aktualisieren |
| DELETE | `/api/anime/{id}` | Anime löschen |
| GET | `/api/anime/genre/{genre}` | Filter per Genre (`@Query`) |
| GET | `/api/anime/stats/per-studio` | Aggregation: Anzahl pro Studio |
| GET | `/api/anime/stats/per-genre` | Aggregation: Anzahl pro Genre |

### Watchlist-Einträge (`/api/entries`)

| Methode | Pfad | Beschreibung |
|---------|------|--------------|
| GET | `/api/entries` | Alle Einträge |
| GET | `/api/entries/{id}` | Ein Eintrag per ID |
| POST | `/api/entries` | Neuen Eintrag anlegen |
| PUT | `/api/entries/{id}` | Eintrag aktualisieren |
| DELETE | `/api/entries/{id}` | Eintrag löschen |
| GET | `/api/entries/user/{user}` | Filter per Benutzer (`@Query`) |
| GET | `/api/entries/stats/episodes-per-user` | Aggregation: Episoden pro Benutzer |
| GET | `/api/entries/stats/per-status` | Aggregation: Anzahl pro Status |
| GET | `/api/entries/stats/avg-rating-per-user` | Aggregation: Ø-Bewertung pro Benutzer |
| GET | `/api/entries/stats/top-rated` | Aggregation mit `$lookup`: Top 5 Anime |
| GET | `/api/entries/stats/avg-rating-per-genre` | Aggregation mit `$lookup`: Ø-Bewertung pro Genre |

---

## 4. Beispiel-Aufrufe (curl)

```bash
# Alle Anime
curl http://localhost:8080/api/anime

# Neuen Anime anlegen
curl -X POST http://localhost:8080/api/anime \
  -H "Content-Type: application/json" \
  -d '{
        "title": "Spy x Family",
        "studio": "Wit Studio",
        "genres": ["Action", "Comedy"],
        "episodes": 25,
        "seasons": [{"seasonNumber": 1, "episodeCount": 25, "year": 2022}]
      }'

# Anime nach Genre filtern
curl http://localhost:8080/api/anime/genre/Drama

# Aggregation: Durchschnittsbewertung pro Genre
curl http://localhost:8080/api/entries/stats/avg-rating-per-genre
```

---

## 5. Projektstruktur

```
src/main/java/com/beispiel/anime/
├── AnimeTrackerApplication.java   # Einstiegspunkt
├── model/                         # @Document-Klassen + Enum + Embedded Season
├── repository/                    # MongoRepository + @Query + @Aggregation
├── service/                       # Businesslogik
├── controller/                    # REST-Endpoints
├── dto/                           # Ergebnis-Objekte der Aggregationen
└── config/                        # DataSeeder (Testdaten)
```

Details zum Datenbankdesign und zu den Aggregationen: siehe `DOKUMENTATION.md`.
