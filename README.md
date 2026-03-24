# Zarządzanie Zadaniami (TODO) – REST API

Prosta aplikacja backendowa w Javie (Spring Boot) udostępniająca REST API do zarządzania zadaniami (TODO).

## Technologie

- Java 21
- Spring Boot 4.0.4
- Spring Data JPA
- H2 (baza danych in-memory)
- Junit + Mockito
- Lombok
- Jakarta Validation
- Springdoc OpenAPI (Swagger UI)
- Maven
- Docker

## Uruchomienie

### Wymagania

- Java 21+
- Maven 3.9+ (lub użyj dołączonego Maven Wrapper)

### Lokalnie (Maven Wrapper)

##### Windows
```cmd
mvnw.cmd spring-boot:run
```
##### Linux
```bash
./mvnw spring-boot:run
```

### Maven

```bash
mvn clean install
mvn spring-boot:run
```

Aplikacja uruchomi się na `http://localhost:8080`.

### Docker

```bash
docker build -t zarzadzanie-zadaniami .
docker run -p 8080:8080 zarzadzanie-zadaniami
```

## Dokumentacja API (Swagger UI)

Po uruchomieniu aplikacji otwórz przeglądarkę:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Konsola H2

Dostępna pod adresem: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

- **JDBC URL:** `jdbc:h2:mem:tododb`
- **Username:** `sa`
- **Password:** *(puste)*

## Testy

```bash
./mvnw clean test
```

Projekt zawiera:

- **Testy jednostkowe** – warstwa serwisowa (`TaskServiceTest`) z użyciem Mockito
- **Testy integracyjne** – kontroler REST (`TaskControllerIntegrationTest`) z MockMvc i H2

## Walidacja danych wejściowych

- `title` – wymagany, niepusty, maksymalnie 255 znaków
- `description` – opcjonalny, maksymalnie 1000 znaków
- `status` – wymagany przy aktualizacji (`NEW`, `IN_PROGRESS`, `DONE`)

Błędy walidacji zwracane są jako odpowiedź 400 z listą błędów pól.

## Obsługa błędów

| Kod | Sytuacja                          |
|-----|-----------------------------------|
| 400 | Błąd walidacji danych wejściowych |
| 404 | Zadanie o podanym ID nie istnieje |

## Endpointy

| Metoda   | Endpoint                 | Opis                         | Kody odpowiedzi |
|----------|--------------------------|------------------------------|-----------------|
| `POST`   | `/api/tasks`             | Tworzenie nowego zadania     | 201, 400        |
| `GET`    | `/api/tasks`             | Lista wszystkich zadań       | 200             |
| `GET`    | `/api/tasks/{id}`        | Pobranie zadania po ID       | 200, 404        |
| `PUT`    | `/api/tasks/{id}`        | Aktualizacja zadania         | 200, 400, 404   |
| `PATCH`  | `/api/tasks/{id}/status` | Aktualizacja statusu zadania | 200, 400, 404   |
| `DELETE` | `/api/tasks/{id}`        | Usunięcie zadania            | 204, 404        |

## Model zadania

```json
{
  "id": 1,
  "title": "Przykładowe zadanie",
  "description": "Opis zadania (opcjonalny)",
  "status": "NEW",
  "createdAt": "2026-03-24T02:45:00"
}
```

### Statusy zadań

| Status        | Opis                 |
|---------------|----------------------|
| `NEW`         | Nowe zadanie         |
| `IN_PROGRESS` | W trakcie realizacji |
| `DONE`        | Ukończone            |