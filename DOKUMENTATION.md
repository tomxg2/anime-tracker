# Technische Dokumentation — Anime Watchlist Tracker

Modul NoSQL / MongoDB · Abschlussprojekt · Spring Boot 3 + Spring Data MongoDB

---

## 1. Themenbeschreibung

Die Applikation verwaltet eine **Anime-Watchlist**. Benutzer können Anime
katalogisieren und pro Anime einen persönlichen Eintrag mit Bewertung, Status
und Fortschritt anlegen. Über Aggregationen werden daraus Statistiken berechnet
(z. B. beliebteste Genres, fleissigste Benutzer, Top-Anime).

---

## 2. Datenbankdesign

### 2.1 Collections

| Collection | Beschreibung |
|------------|--------------|
| `anime` | Katalog aller Anime |
| `watch_entries` | Persönliche Watchlist-Einträge pro Benutzer |

### 2.2 Dokument `anime`

```json
{
  "_id": "ObjectId",
  "title": "Neon Genesis Evangelion",
  "studio": "Gainax",
  "genres": ["Mecha", "Psychological", "Drama"],
  "episodes": 26,
  "seasons": [
    { "seasonNumber": 1, "episodeCount": 26, "year": 1995 }
  ]
}
```

### 2.3 Dokument `watch_entries`

```json
{
  "_id": "ObjectId",
  "animeId": "<_id aus anime>",
  "user": "tom",
  "rating": 10,
  "episodesWatched": 26,
  "status": "COMPLETED"
}
```

### 2.4 Beziehungen

Das Projekt zeigt **beide** in MongoDB üblichen Beziehungsarten:

- **Embedded (eingebettet):** Die `seasons` werden direkt im Anime-Dokument
  gespeichert. Staffeln existieren nie ohne ihren Anime und werden immer
  zusammen mit ihm geladen — Einbettung ist hier die natürliche Wahl.
- **Referenziert:** Ein `watch_entries`-Dokument speichert nur die `animeId`
  als Referenz auf das zugehörige `anime`-Dokument (1:N — ein Anime kann in
  vielen Einträgen vorkommen). So vermeiden wir Datenredundanz: Anime-Stammdaten
  liegen nur einmal vor und werden bei Bedarf per `$lookup` verknüpft.

---

## 3. ORM-Mapping (Spring Data MongoDB)

| Annotation | Verwendung |
|------------|-----------|
| `@Document(collection = "...")` | Markiert `Anime` und `WatchEntry` als persistente Dokumente |
| `@Id` | Primärschlüssel (MongoDB-`ObjectId`, als `String` abgebildet) |
| (keine Annotation) | `Season` ist ein eingebettetes POJO ohne eigene Collection |
| `enum WatchStatus` | Wird automatisch als String gespeichert |

Repositories erben von `MongoRepository<T, String>` und erhalten dadurch alle
CRUD-Methoden geschenkt (`findAll`, `findById`, `save`, `deleteById` …).

---

## 4. Eigene Queries (`@Query`)

| Repository | Methode | Filter |
|------------|---------|--------|
| `AnimeRepository` | `findByGenre` | `{ 'genres': ?0 }` |
| `AnimeRepository` | `findByMinimumEpisodes` | `{ 'episodes': { '$gte': ?0 } }` |
| `WatchEntryRepository` | `findByUser` | `{ 'user': ?0 }` |
| `WatchEntryRepository` | `findByStatusAndMinRating` | `{ 'status': ?0, 'rating': { '$gte': ?1 } }` |

`?0`, `?1` sind die Methodenparameter in Reihenfolge.

---

## 5. Aggregationen (`@Aggregation`)

Insgesamt **7 Pipelines** (Anforderung: mind. 5). Zwei davon verwenden einen
Join über `$lookup`.

| # | Methode | Collection | Zweck | Stages |
|---|---------|-----------|-------|--------|
| 1 | `countAnimePerStudio` | anime | Anzahl Anime pro Studio | `$group`, `$project`, `$sort` |
| 2 | `countAnimePerGenre` | anime | Anzahl Anime pro Genre | `$unwind`, `$group`, `$project`, `$sort` |
| 3 | `totalEpisodesPerUser` | watch_entries | Summe gesehener Episoden pro User | `$group ($sum)`, `$project`, `$sort` |
| 4 | `countPerStatus` | watch_entries | Verteilung der Status | `$group`, `$project`, `$sort` |
| 5 | `averageRatingPerUser` | watch_entries | Ø-Bewertung pro User | `$group ($avg)`, `$round`, `$sort` |
| 6 | `findTopRatedAnime` | watch_entries → anime | Top 5 Anime nach Ø-Bewertung | `$group`, `$sort`, `$limit`, **`$lookup`**, `$unwind`, `$project` |
| 7 | `averageRatingPerGenre` | watch_entries → anime | Ø-Bewertung pro Genre | **`$lookup`**, `$unwind`×2, `$group`, `$project`, `$sort` |

### 5.1 Begründung der Aggregations-Wahl

Die Aggregationen beantworten echte Fragen der Anwendung:
„Welche Genres kommen am besten an?“, „Wer hat am meisten geschaut?“,
„Welche Anime sind die beliebtesten?“. Sie decken bewusst eine breite Palette
von Operatoren ab: Gruppierung (`$group`), Summe (`$sum`), Durchschnitt
(`$avg`), Array-Zerlegung (`$unwind`), Sortierung/Begrenzung (`$sort`/`$limit`)
und Joins (`$lookup`).

### 5.2 Detail: Join über `$lookup`

Da `watch_entries.animeId` als **String** gespeichert wird, die `_id` der
`anime`-Collection aber eine **ObjectId** ist, muss im `$lookup` konvertiert
werden:

```javascript
{ '$lookup': {
    'from': 'anime',
    'let': { 'aid': { '$toObjectId': '$animeId' } },
    'pipeline': [
      { '$match': { '$expr': { '$eq': [ '$_id', '$$aid' ] } } }
    ],
    'as': 'anime'
}}
```

Anschliessend macht `$unwind` aus dem (immer einelementigen) `anime`-Array ein
einzelnes Objekt, dessen Felder im `$project` flach ins Ergebnis-DTO übernommen
werden.

---

## 6. Architektur / Schichten

```
Controller  →  Service  →  Repository  →  MongoDB
 (REST)        (Logik)      (Daten)
```

- **Controller** nehmen HTTP-Requests entgegen und geben JSON zurück.
- **Service** kapselt die Businesslogik (z. B. Update-Logik, ID-Handling).
- **Repository** spricht über Spring Data mit MongoDB.

Diese Trennung macht den Code übersichtlich und testbar.

---

## 7. Vorgehen

1. Thema gewählt und Datenmodell entworfen (Embedded vs. Referenziert).
2. Projekt mit Spring Initializr / Maven aufgesetzt, MongoDB lokal verbunden.
3. Document-Klassen, Repositories und Services implementiert.
4. REST-Endpoints ergänzt und mit curl/Postman getestet.
5. Aggregations-Pipelines entwickelt und gegen die Testdaten geprüft.
6. `DataSeeder` für reproduzierbare Demo-Daten ergänzt.
7. Dokumentation geschrieben und Demo vorbereitet.

---

## 8. Mögliche Erweiterungen

- Validierung der Eingaben (Rating 1–10) mit Bean Validation.
- Authentifizierung pro Benutzer.
- Frontend (z. B. React) zur Visualisierung der Statistiken.
