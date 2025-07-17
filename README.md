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

The Blackjack API provides the following RESTful endpoints for managing users, game plays, and rankings. All endpoints return JSON responses and accept JSON request bodies where applicable.

### Error Handling

The API uses a consistent error response format across all endpoints:

```json
{
  "message": "Error message describing what went wrong"
}
```

For validation errors, a more detailed response is provided:

```json
{
  "errors": [
    {
      "field": "name",
      "message": "Name cannot be empty"
    },
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

Common HTTP status codes:

- 200 OK: Request succeeded
- 201 Created: Resource created successfully
- 400 Bad Request: Invalid request parameters or body
- 404 Not Found: Resource not found
- 409 Conflict: Resource already exists
- 422 Unprocessable Entity: Validation errors
- 500 Internal Server Error: Server-side error

### User Management

- **Create User**
  - `PUT /api/v1/users/{id}`
  - Description: Creates a new user with the specified ID, name, and email
  - Request Body:
    ```json
    {
      "name": "John Doe",
      "email": "john.doe@example.com"
    }
    ```
  - Response Status:
    - 201: User created successfully
    - 400: Invalid request body or validation error
    - 409: User already exists
    - 500: Internal server error

- **Change User Name**
  - `PATCH /api/v1/users/{id}`
  - Description: Updates the name of an existing user
  - Request Body:
    ```json
    {
      "name": "John Smith"
    }
    ```
  - Response Status:
    - 200: User name updated successfully
    - 400: Invalid user ID format or invalid request body
    - 404: User not found
    - 500: Internal server error

### Game Play

- **Create Play**
  - `PUT /api/v1/play/{id}`
  - Description: Creates a new blackjack play with the specified ID, user ID, and bet amount
  - Request Body:
    ```json
    {
      "userId": "user-uuid",
      "betAmount": 50.0
    }
    ```
  - Response Status:
    - 201: Play created successfully
    - 400: Invalid request body or validation error
    - 409: Play already exists
    - 500: Internal server error

- **Get Play**
  - `GET /api/v1/play/{id}`
  - Description: Retrieves a play by its unique identifier
  - Response Body (200 OK):
    ```json
    {
      "id": "play-uuid",
      "userId": "user-uuid",
      "betAmount": 50.0,
      "status": "IN_PROGRESS",
      "playerCards": [
        {"suit": "HEARTS", "rank": "ACE"},
        {"suit": "DIAMONDS", "rank": "KING"}
      ],
      "dealerCards": [
        {"suit": "CLUBS", "rank": "SEVEN"}
      ],
      "playerScore": 21,
      "dealerScore": 7,
      "result": null
    }
    ```
  - Response Status:
    - 200: Play found (returns PlayDto)
    - 400: Invalid play ID format
    - 404: Play not found
    - 500: Internal server error

- **Perform Play (Hit)**
  - `POST /api/v1/play/{id}`
  - Description: Executes a blackjack play (draws cards) for the specified play ID
  - Response Body (200 OK):
    ```json
    {
      "id": "play-uuid",
      "userId": "user-uuid",
      "betAmount": 50.0,
      "status": "IN_PROGRESS",
      "playerCards": [
        {"suit": "HEARTS", "rank": "ACE"},
        {"suit": "DIAMONDS", "rank": "KING"},
        {"suit": "SPADES", "rank": "FIVE"}
      ],
      "dealerCards": [
        {"suit": "CLUBS", "rank": "SEVEN"}
      ],
      "playerScore": 16,
      "dealerScore": 7,
      "result": null
    }
    ```
  - Response Status:
    - 200: Play performed successfully (returns PlayDto)
    - 400: Invalid play ID format
    - 404: Play not found
    - 409: Play is already completed
    - 500: Internal server error

- **Perform Stand**
  - `PATCH /api/v1/play/{id}`
  - Description: Player stands, dealer draws until reaching at least 17 points, and the game result is determined
  - Response Body (200 OK):
    ```json
    {
      "id": "play-uuid",
      "userId": "user-uuid",
      "betAmount": 50.0,
      "status": "COMPLETED",
      "playerCards": [
        {"suit": "HEARTS", "rank": "ACE"},
        {"suit": "DIAMONDS", "rank": "KING"}
      ],
      "dealerCards": [
        {"suit": "CLUBS", "rank": "SEVEN"},
        {"suit": "HEARTS", "rank": "NINE"}
      ],
      "playerScore": 21,
      "dealerScore": 16,
      "result": "WIN"
    }
    ```
  - Response Status:
    - 200: Stand performed successfully (returns PlayDto)
    - 400: Invalid play ID format
    - 404: Play not found
    - 409: Play is already completed
    - 500: Internal server error

### Ranking

- **Get Scoreboard**
  - `GET /api/v1/ranking?limit=10`
  - Description: Retrieves a list of player rankings sorted by earnings
  - Query Parameters:
    - limit (optional, default=10): Maximum number of rankings to return
  - Response Body (200 OK):
    ```json
    [
      {
        "userId": "user-uuid-1",
        "userName": "John Doe",
        "earnings": 500.0,
        "wins": 10,
        "losses": 5
      },
      {
        "userId": "user-uuid-2",
        "userName": "Jane Smith",
        "earnings": 300.0,
        "wins": 7,
        "losses": 4
      }
    ]
    ```
  - Response Status:
    - 200: Rankings retrieved successfully (returns array of RankingDto)
    - 500: Internal server error

## Postman Configuration

Postman collection and environment files are available in the resources folder:
- `src/main/resources/Blackjack API.postman_collection.json`
- `src/main/resources/blackjack.postman_environment.json`

To use them:

1. Import both the collection and environment into Postman
2. Select the imported environment in Postman
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

The API is documented using OpenAPI (Swagger) specifications. You can access the interactive documentation at:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
  - Interactive documentation that allows you to try out the API endpoints directly from your browser

- **OpenAPI JSON**: `http://localhost:8080/api-docs`
  - Raw OpenAPI specification in JSON format, which can be imported into tools like Postman

The Swagger UI provides detailed information about all endpoints, including:
- Request parameters and bodies
- Response formats and status codes
- Example requests and responses
- Schema definitions for all data models
