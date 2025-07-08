# Makefile for Blackjack API

# Default target
.PHONY: all
all: clean install test

# Clean the project
.PHONY: clean
clean:
	./gradlew clean

# Install dependencies
.PHONY: install
install:
	./gradlew build -x test

# Run tests
.PHONY: test
test:
	./gradlew test

# Run linting (code style checks)
.PHONY: lint
lint:
	./gradlew spotlessCheck

# Apply code formatting
.PHONY: format
format:
	./gradlew spotlessApply

# Start the server
.PHONY: start
start:
	./gradlew bootRun

# Compile the project
.PHONY: compile
compile:
	./gradlew compileJava

# Build the project (compile, test, and package)
.PHONY: build
build:
	./gradlew build

# Help command
.PHONY: help
help:
	@echo "Blackjack API Makefile"
	@echo ""
	@echo "Usage:"
	@echo "  make [target]"
	@echo ""
	@echo "Targets:"
	@echo "  all      - Clean, install dependencies, and run tests (default)"
	@echo "  clean    - Clean the project"
	@echo "  install  - Install dependencies"
	@echo "  test     - Run tests"
	@echo "  lint     - Check code style"
	@echo "  format   - Apply code formatting"
	@echo "  start    - Start the server"
	@echo "  compile  - Compile the project"
	@echo "  build    - Build the project (compile, test, and package)"
	@echo "  help     - Display this help message"