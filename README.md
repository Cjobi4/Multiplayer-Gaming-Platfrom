# Online Multiplayer Game Platform

A turn-based online multiplayer game platform built with JavaFX for SENG 300.

## Prerequisites

### Client
- **JDK 25**
- **Git**

### Server
- **JDK 25**
- **Git**
- **SQLite**
- **SQLite JDBC driver**
- **Admin access may be required for firewall setup**

## Client Build and Run

### Build Instructions

```bash
./mvnw clean compile
```

### Run Instructions

```bash
./mvnw javafx:run
```

### Test Instructions

```bash
./mvnw test
```

## Server Build and Run

### Build Instructions

Navigate to the server directory and compile:
```bash
../mvnw clean compile
```

To build the executable JAR:
```bash
../mvnw clean package
```

### Run Instructions

```bash
../mvnw exec:java
```

**Note:** The server cannot run directly from IntelliJ IDE due to firewall restrictions. Running from terminal/command line with admin privileges is required.

### Test Instructions
```bash
cd server
../mvnw test
```

### Firewall Configuration for Windows

Before running the server for the first time:

1. Open **Windows Defender Firewall** → **Advanced Settings**
2. Create **Inbound Rule**:
    - Type: Port
    - Protocol: TCP, Port: 14001
    - Action: Allow the connection
    - Profile: All
    - Name: "SENG300 Server Port"
3. Create matching **Outbound Rule**
4. Enable both rules when server is running
5. **Disable rules when server is not running** (security best practice)


### Server Configuration

The server uses:
- **Port:** 14001 (configurable in `Database.java`)
- **Database:** SQLite


## Network Notes

- The server listens for TCP connections on port `14001`.
- Clients connect using the server machine’s internal IP address and port.
- Under the current test setup, client and server must be on the same network.
- If the server IP address changes, the client configuration may need to be updated.


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
