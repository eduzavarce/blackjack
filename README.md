# Blackjack API

A reactive REST API for a Blackjack game built with Spring Boot, following Domain-Driven Design principles and an event-driven architecture.

## Requirements

- Java 21
- MySQL database
- MongoDB database
- Docker (optional, for running with containers)

## Installation and Setup

### Environment Variables

Create a `.env` file in the project root with the following variables:

```
R2DBC_URL=r2dbc:mysql://localhost:3306/blackjack
R2DBC_USER=root
R2DBC_PASSWORD=secret

MONGODB_PASSWORD=secret
MONGODB_PORT=27017
MONGODB_HOST=localhost
MONGODB_DATABASE=blackjack
MONGODB_USER=root
```

### Building and Running the Project

The project uses Make to simplify common development tasks. Here are the basic commands to get started:

```bash
# Build the project
make build

# Start the application
make start
```

For a complete list of available commands, see the [Make Commands](#make-commands) section below.

### Docker (Optional)

A Dockerfile is provided to containerize the application:

```bash
# Build the Docker image
docker build -t blackjack-api .

# Run the container
docker run -p 8080:8080 --env-file .env blackjack-api
```

## API Endpoints

### User Management

- **Create User**
  - `PUT /api/v1/users/{userId}`
  - Request Body: `{ "name": "John Doe", "email": "john.doe@example.com" }`
  - Creates a new user with the specified ID, name, and email

- **Change User Name**
  - `PATCH /api/v1/users/{userId}/name`
  - Request Body: `{ "name": "John Smith" }`
  - Updates the name of an existing user

### Game Play

- **Create Play**
  - `PUT /api/v1/play/{playId}`
  - Request Body: `{ "userId": "user-uuid", "betAmount": 50.0 }`
  - Creates a new blackjack play with the specified ID, user ID, and bet amount

- **Get Play**
  - `GET /api/v1/play/{playId}`
  - Retrieves a play by its unique identifier

- **Perform Play**
  - `POST /api/v1/play/{playId}`
  - Executes a blackjack play (draws cards) for the specified play ID

### Ranking

- **Get Scoreboard**
  - `GET /api/v1/ranking?limit=10`
  - Retrieves a list of player rankings sorted by earnings

## Postman Configuration

A Postman collection is included in the project at `src/main/resources/postman_collection.json`. To use it:

1. Import the collection into Postman
2. Set the `baseUrl` variable to your server address (default: `http://localhost:8080`)
3. The collection includes scripts to automatically generate UUIDs for users and plays
4. Follow the request sequence:
   - Create User → Create Play → Perform Play → Get Scoreboard

## Technologies

- **Spring Boot 3.5.0**: Application framework
- **Spring WebFlux**: Reactive web framework
- **Spring Data R2DBC**: Reactive relational database connectivity
- **Spring Data MongoDB Reactive**: Reactive MongoDB support
- **Spring Validation**: Input validation
- **Spring dotenv**: Environment variable management
- **SpringDoc OpenAPI**: API documentation
- **Testcontainers**: Integration testing with Docker containers
- **JUnit 5**: Testing framework
- **Gradle**: Build tool
- **Java 21**: Programming language

## Architecture

The application follows Domain-Driven Design (DDD) principles with a hexagonal architecture:

### Bounded Contexts

- **Auth**: User management and authentication
- **Accounts**: User account and balance management
- **Game**: Game play, deck management, and game rules
- **Ranking**: Player rankings and statistics

### Layers in Each Context

- **Domain**: Core business logic, entities, and value objects
- **Application**: Use cases and application services
- **Infrastructure**: Controllers, repositories, and external services

### Event-Driven Communication

The application uses an event-driven architecture for communication between bounded contexts:

1. Domain events are created by aggregates
2. Events are published through an event bus
3. Event subscribers in other contexts react to these events

## Workflow Example: Creating and Performing a Play

This example demonstrates the event propagation in the system:

1. **Create User**:
   - A `PUT` request to `/api/v1/users/{userId}` creates a new user
   - The `User` aggregate records a `UserRegistered` event
   - Event subscribers in other contexts react:
     - `CreateAccountOnUserRegistered` creates a user account with initial funds
     - `CreateGameUserOnUserRegistered` creates a game user profile

2. **Create Play**:
   - A `PUT` request to `/api/v1/play/{playId}` creates a new play
   - The `Play` aggregate records a `PlayCreated` event
   - `UpdateAccountOnPlayCreated` deducts the bet amount from the user's account

3. **Perform Play**:
   - A `POST` request to `/api/v1/play/{playId}` performs the play
   - Cards are drawn for the player and dealer
   - The winner is determined based on blackjack rules
   - The `Play` aggregate records a `PlayCompleted` event
   - Event subscribers react:
     - `UpdateAccountOnPlayCompleted` increases the user's funds if they won
     - `UpdateRankingOnPlayCompleted` updates the player's ranking

This workflow demonstrates how different bounded contexts communicate through events without direct coupling, following DDD principles.

## Make Commands

The project includes a Makefile that provides several useful commands to simplify development tasks:

| Command | Description |
|---------|-------------|
| `make all` | Default target that cleans, installs dependencies, and runs tests |
| `make clean` | Cleans the project |
| `make install` | Installs dependencies (builds without running tests) |
| `make test` | Runs tests |
| `make lint` | Checks code style using Spotless |
| `make format` | Applies code formatting using Spotless |
| `make start` | Starts the server |
| `make compile` | Compiles the project |
| `make build` | Builds the project (compiles, tests, and packages) |
| `make help` | Displays help information about available commands |

You can run `make help` to see this list of commands directly in your terminal.

## API Documentation

The API documentation is available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
