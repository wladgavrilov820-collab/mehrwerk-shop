# MEHRWERK Shop Backend

Spring Boot application for the MEHRWERK Backend Coding Challenge.

## Requirements

- Java 21
- Docker Desktop
- Maven (Maven Wrapper included)

## Environment Variables

Configure the following environment variables before starting the application:

| Variable | Description |
|---|---|
| `DB_USERNAME` | SQL Server username |
| `DB_PASSWORD` | SQL Server password |
| `MEHRWERK_API_KEY` | MEHRWERK API key |
| `MEHRWERK_CLIENT_ID` | OAuth client ID |
| `MEHRWERK_CLIENT_SECRET` | OAuth client secret |

Credentials are not included in the repository.

Make sure the database credentials match the configuration in `compose.yaml` and `application.yaml`.

## Running the Application

### 1. Clone the repository

```bash
git clone https://github.com/wladgavrilov820-collab/mehrwerk-shop.git
cd mehrwerk-shop
```

### 2. Configure the environment

Set the required environment variables listed above.

The database password must also be available to Docker Compose if referenced in `compose.yaml`.

### 3. Start SQL Server

Make sure Docker Desktop is running.

```bash
docker compose up -d
```

Wait until SQL Server is ready to accept connections.

### 4. Start the application

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

On startup, Flyway applies the database migrations and the application imports shops from the MEHRWERK API.

## REST API

Retrieve shops:

```http
GET http://localhost:8080/api/shops
```

Retrieve shops with pagination and sorting:

```http
GET http://localhost:8080/api/shops?page=0&size=10&sort=id,asc
```

Query parameters:

| Parameter | Description |
|---|---|
| `page` | Zero-based page number |
| `size` | Number of shops per page |
| `sort` | Sorting field and direction |

## Running Tests

Make sure Docker Desktop is running. Integration tests use Testcontainers with Microsoft SQL Server.

Windows:

```powershell
.\mvnw.cmd clean verify
```

Linux/macOS:

```bash
./mvnw clean verify
```

## Notes

- The application requires access to the MEHRWERK test API for the initial import.
- OAuth access tokens are managed automatically.
- Shop data is cached for 10 minutes.
- The cache is invalidated after shop updates.
- Do not commit passwords, API keys, or other credentials to version control.
