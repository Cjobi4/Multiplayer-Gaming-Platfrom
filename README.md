# Online Multiplayer Game Platform

A turn-based online multiplayer game platform built with JavaFX for SENG 300.

## Prerequisites

- **JDK 25**
- **Git**

## Build Instructions

```bash
./mvnw clean compile
```

## Run Instructions

```bash
./mvnw javafx:run
```

## Test Instructions

```bash
./mvnw test
```

## Project Structure

```
project-root/
├── docs/                  # Design documents and diagrams
│   ├── architecture/      # System-wide architecture
│   ├── platform-core/     # Platform Core designs
│   ├── client-ui/         # Client/UI designs
│   ├── rules-validation/  # Rules & Validation designs
│   ├── quality-testing/   # Test plans
│   └── integration-release/  # I&R process docs
├── src/
│   ├── main/
│   │   ├── java/ca/ucalgary/seng300/
│   │   │   ├── app/       # Application entry point
│   │   │   ├── shared/    # Cross-team contracts
│   │   │   ├── core/      # Platform Core
│   │   │   ├── rules/     # Rules & Validation
│   │   │   ├── client/    # Client/UI
│   │   │   └── games/     # Sample game implementations
│   │   └── resources/     # FXML, CSS, images
│   └── test/              # JUnit test suite
├── scripts/               # Utility scripts
├── CHANGELOG.md           # Version history
├── CURRENT_STATE.md       # Feature status matrix
└── team.md                # Sub-team roster
```

## Key Documents

- [Team Roster](team.md)
- [Current State](CURRENT_STATE.md)
- [Changelog](CHANGELOG.md)
- [Documentation Guide](docs/README.md)

## Sub-Team Responsibilities

| Sub-Team | Package | Responsibilities |
|---|---|---|
| Platform Core | `core/` | Identity, game registry, rooms, matchmaking, turn engine, persistence |
| Client/UI | `client/` | JavaFX screens, shared UI components, game rendering |
| Rules & Validation | `rules/` | Move validation pipeline, leaderboard scoring |
| Quality & Testing | `test/.../integration/` | Integration tests, test plans, demo verification |
| Integration & Release | Root files, `scripts/` | Build config, branching workflow, release process |
