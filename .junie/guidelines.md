# Blackjack API Development Guidelines

This document provides guidelines and information for developers working on the Blackjack API project.

## Build and Configuration

### Prerequisites
- Java 21
- Gradle
- Docker (for running tests with Testcontainers)

### Environment Variables
The application requires the following environment variables to be set:

**R2DBC (MySQL) Configuration:**
- `R2DBC_URL`: The R2DBC URL for MySQL connection
- `R2DBC_USER`: MySQL username
- `R2DBC_PASSWORD`: MySQL password

**MongoDB Configuration:**
- `MONGODB_HOST`: MongoDB host
- `MONGODB_PORT`: MongoDB port
- `MONGODB_DATABASE`: MongoDB database name
- `MONGODB_USER`: MongoDB username
- `MONGODB_PASSWORD`: MongoDB password

You can use a `.env` file in the project root for local development (using the spring-dotenv dependency).

### Building the Project
```bash
./gradlew build
```

### Running the Application
```bash
./gradlew bootRun
```

## Testing

### Test Structure
The project uses JUnit 5 for testing. Tests are organized by domain:
- Unit tests for domain entities and services
- Integration tests for controllers and repositories

### Test Containers
Integration tests use Testcontainers to spin up Docker containers for MongoDB. The configuration is in `TestcontainersConfiguration.java`.

Example of importing the test containers configuration:
```java
@Import(TestcontainersConfiguration.class)
@SpringBootTest(
        classes = Blackjack2Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class YourIntegrationTest {
    // Test code
}
```

### Running Tests
```bash
# Run all tests
./gradlew test

# Run a specific test
./gradlew test --tests "dev.eduzavarce.blackjack_api.play.PlayTest"
```

### Example Test
Here's an example of a unit test for a domain entity:

```java
@Test
void testCreatePlay() {
    // Arrange
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = 100.0;

    // Act
    Play play = Play.create(playId, userId, betAmount);
    
    // Assert
    PlayDto primitives = play.toPrimitives();
    assertEquals(playId, primitives.id());
    assertEquals(userId, primitives.userId());
    assertEquals(betAmount, primitives.betAmount());
    assertEquals(PlayStatus.IN_PROGRESS, primitives.status());
    
    // Verify that a domain event was recorded
    List<DomainEvent> events = play.pullDomainEvents();
    assertEquals(1, events.size());
    
    DomainEvent event = events.get(0);
    assertTrue(event instanceof PlayCreated);
    assertEquals(playId, event.aggregateId());
    assertEquals("play.created", event.eventName());
}
```

## Domain-Driven Design (DDD) Structure

### Project Structure
The project follows a DDD structure with bounded contexts:

```
src/main/java/dev/eduzavarce/blackjack_api/contexts/
├── accounts/           # Account management context
├── auth/               # Authentication context
├── game/               # Game context (plays, decks, etc.)
└── shared/             # Shared kernel
```

Each bounded context is further divided into:
- `domain/`: Domain entities, value objects, and repositories
- `application/`: Application services and use cases
- `infrastructure/`: Controllers, database entities, and external services

### Aggregates and Entities
- Aggregates extend the `AggregateRoot` class
- Entities implement the `Entity` interface
- Value objects extend appropriate base classes (`StringValueObject`, `IntValueObject`, etc.)

Example of an aggregate:
```java
public class Play extends AggregateRoot {
    private final PlayId id;
    private final PlayStatus status;
    private final UserId userId;
    private final double betAmount;
    private final Deck deck;

    // Constructor and methods
}
```

### Domain Events
The project uses an event-driven architecture:
- Domain events extend the `DomainEvent` class
- Event subscribers are annotated with `@DomainEventSubscriber`
- Events are published through the `EventBus`

Example of a domain event:
```java
public class PlayCreated extends DomainEvent {
    private final String eventName;
    private final PlayDto body;

    // Constructor and methods
}
```

Example of an event subscriber:
```java
@Service
@DomainEventSubscriber({UserRegistered.class})
public class CreateAccountOnUserRegistered {
    public void on(UserRegistered event) {
        // Handle the event
    }
}
```

## Code Style and Conventions

The project uses Google Java Format for code formatting, configured with Spotless:

```gradle
spotless {
    java {
        target 'src/**/*.java'
        googleJavaFormat('1.22.0')
        importOrder('\\#', 'java', '', 'dev.eduzavarce')
        removeUnusedImports()
        endWithNewline()
        formatAnnotations()
    }
}
```

To format the code:
```bash
./gradlew spotlessApply
```

To check if the code is formatted correctly:
```bash
./gradlew spotlessCheck
```