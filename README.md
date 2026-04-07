# Online Multiplayer Game Platform

Group 8 Online Multiplayer Game Platform

University of Calgary

SENG 300 Tutorial T08

Winter 2026

A turn-based online multiplayer game platform (Tic-Tac-Toe and Connect Four) built with JavaFX and a socket-based server.

## Project Status

**Please read the [CURRENT_STATE.md](CURRENT_STATE.md) file**. All team members should reference this document to understand what is done, what is in progress, and what remains.

## Prerequisites

### Client

- **JDK 25**
- **Git**

### Server

- **JDK 25**
- **Git**
- **Admin access may be required for firewall setup**

### Cross-Platform Note

Commands below use `./mvnw` (Linux, macOS, Git Bash on Windows).

On **Windows CMD or PowerShell**, use `mvnw.cmd` instead (e.g., `mvnw.cmd clean compile`).

## Client Build and Run

Compile first to check for errors, then run tests to verify correctness, then launch the application.

### Build Instructions

```bash
./mvnw clean compile
```

### Test Instructions

```bash
./mvnw clean test
```

### Run Instructions

```bash
./mvnw clean javafx:run
```

## Server Build and Run

Server commands can be run from either the project root (using `-f server/pom.xml`) or from the `server/` directory.

### Build Instructions

From project root:

```bash
./mvnw -f server/pom.xml clean compile
```

Or from the `server/` directory:

```bash
cd server
../mvnw clean compile
```

To package the server JAR:

```bash
./mvnw -f server/pom.xml clean package
```

### Test Instructions

```bash
./mvnw -f server/pom.xml clean test
```

### Run Instructions

```bash
./mvnw -f server/pom.xml exec:java
```

Or from the `server/` directory:

```bash
cd server
../mvnw exec:java
```

**Note:** The server cannot run directly from IntelliJ IDE due to firewall restrictions. Running from terminal/command line with admin privileges is required.

## Firewall Configuration

Before running the server for the first time, allow TCP traffic on port **14001**.

### Windows

1. Open **Windows Defender Firewall** → **Advanced Settings**
2. Create **Inbound Rule**:
   - Type: Port
   - Protocol: TCP, Port: 14001
   - Action: Allow the connection
   - Profile: All
   - Name: "SENG300 Server Port"
3. Create matching Outbound Rule
4. Enable both rules when server is running and disable rules when server is not running

### macOS

macOS does not block outbound connections by default. If using the built-in firewall, allow incoming connections for Java in **System Settings > Network > Firewall**.

### Linux

```bash
sudo ufw allow 14001/tcp
```

## Server Configuration

The server uses:

- **Port:** 14001 (configured in `server/src/.../Network.java`)
- **Database:** SQLite (bundled with Maven)

## Network Notes

- The server listens for TCP connections on port `14001`.
- Clients connect using the server machine's internal IP address and port.
- Under the current test setup, client and server must be on the same network.
- If the server IP address changes, the client configuration may need to be updated.

## Project Structure

```
project-root/
├── docs/                              # Design documents and diagrams
│   ├── architecture/                  # System-wide architecture
│   ├── platform-core/                 # Platform Core designs
│   │   ├── identity/                  # Authentication and session
│   │   ├── game-registry/             # Game registry design
│   │   ├── rooms-and-matchmaking/     # Matchmaking design
│   │   ├── turn-engine/               # Turn engine design
│   │   └── persistence/               # Database and server design
│   ├── client-ui/                     # Client/UI designs
│   │   ├── screens/                   # Screen-level designs
│   │   └── game-rendering/            # Game rendering designs
│   ├── rules-validation/              # Rules & Validation designs
│   │   ├── leaderboard/               # Leaderboard design
│   │   └── move-validation/           # Move validation design
│   ├── quality-testing/               # Test plans
│   └── integration-release/           # I&R process docs
├── server/                            # Server module (separate Maven project)
│   ├── pom.xml
│   └── src/
│       ├── main/java/.../
│       │   ├── Database.java          # SQLite persistence and user management
│       │   ├── Matchmaker.java        # Skill-based matchmaking queue
│       │   ├── Network.java           # TCP server socket listener with encryption
│       │   ├── Request.java           # Async request DTO with CompletableFuture
│       │   ├── ServerMain.java        # Server entry point
│       │   ├── Session.java           # Client session handler (16 request types)
│       │   └── Games/                 # Server-side game sessions
│       │       ├── GameState.java
│       │       ├── TicTacToeGameSession.java
│       │       ├── tictactoe/         # Server TTT board and game logic
│       │       └── connectfour/       # Server C4 board, game, and session
│       └── test/java/.../             # Server unit tests (Matchmaker)
├── src/
│   ├── main/
│   │   ├── java/ca/ucalgary/seng300/
│   │   │   ├── app/                   # Application entry point (MainApp)
│   │   │   ├── shared/                # Cross-team contracts and models
│   │   │   │   └── models/            # Game, Tag, Player, Message, ActivePlayer
│   │   │   ├── core/
│   │   │   │   ├── identity/          # Client-side auth and networking
│   │   │   │   ├── registry/          # Game, Chat, and Player registries
│   │   │   │   ├── matchmaking/       # Matchmaking (stub)
│   │   │   │   ├── rooms/             # Room management (stub)
│   │   │   │   └── turnengine/        # Game session management
│   │   │   ├── rules/
│   │   │   │   ├── leaderboard/       # Scoring models, queries, and database
│   │   │   │   └── validation/        # Move validation pipeline (stub)
│   │   │   ├── client/
│   │   │   │   ├── screens/           # FXML controllers (9 screens)
│   │   │   │   ├── components/        # Leaderboard row model and mock data
│   │   │   │   └── rendering/         # Board rendering (stub)
│   │   │   └── games/
│   │   │       ├── tictactoe/         # Tic-Tac-Toe board and game logic
│   │   │       └── connectfour/       # Connect Four board and game logic
│   │   └── resources/
│   │       ├── css/                   # Stylesheets
│   │       ├── fxml/                  # Screen layouts (9 FXML files)
│   │       └── images/               # Static assets
│   └── test/                          # JUnit 5 test suite
│       └── java/.../
│           ├── client/                # UI controller tests
│           ├── core/                  # Registry and network tests
│           ├── games/                 # Game logic tests (TTT and C4)
│           ├── rules/                 # Leaderboard tests
│           └── integration/           # Integration tests (stub)
├── scripts/                           # Utility scripts (placeholder)
├── .gitlab-ci.yml                     # CI/CD pipeline (build + test, client and server)
├── .gitlab/                           # GitLab merge request templates
├── mvnw / mvnw.cmd                    # Maven wrapper (Unix / Windows)
├── pom.xml                            # Client Maven build config
├── CHANGELOG.md                       # Version history
├── CURRENT_STATE.md                   # Feature status and requirements checklist
└── team.md                            # Sub-team roster
```

## Key Documents

- [Current State](CURRENT_STATE.md)
- [Team Roster](team.md)
- [Changelog](CHANGELOG.md)
- [Documentation Directory](docs/README.md)

## Sub-Team Responsibilities

See [team.md](team.md) for the full sub-team roster, leads, and members.

| Sub-Team              | Package                | Responsibilities                                                                        |
| --------------------- | ---------------------- | --------------------------------------------------------------------------------------- |
| Platform Core         | `core/`, `server/`     | Identity management, game registry, rooms and matchmaking, turn engine, and persistence |
| Client/UI             | `client/`              | Login/signup, lobby, game info screen, leaderboard dashboard, and admin console         |
| Rules & Validation    | `rules/`, `games/`     | Move validation pipeline and leaderboard scoring                                        |
| Quality & Testing     | `test/`                | End-to-end test planning                                                                |
| Integration & Release | Root files, `scripts/` | Branching/merging workflow, peer review process, and versioning management              |

---

_Last updated: 2026-04-06_
