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

> All server commands must be run from the `server/` directory. Start with `cd server` from the project root. When finished, return to the project root with `cd ..`.

### Build Instructions

```bash
cd server
../mvnw clean compile
```

To build the executable JAR:

```bash
cd server
../mvnw clean package
```

### Test Instructions

```bash
cd server
../mvnw clean test
```

### Run Instructions

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
├── docs/                          # Design documents and diagrams
│   ├── architecture/              # System-wide architecture
│   ├── platform-core/             # Platform Core designs
│   ├── client-ui/                 # Client/UI designs
│   ├── rules-validation/          # Rules & Validation designs
│   ├── quality-testing/           # Test plans
│   └── integration-release/       # I&R process docs
├── server/                        # Server module (separate Maven project)
│   ├── pom.xml
│   └── src/main/java/.../
│       ├── Database.java          # SQLite persistence and user management
│       ├── Matchmaker.java        # Skill-based matchmaking queue
│       ├── Network.java           # TCP server socket listener
│       ├── ServerMain.java        # Server entry point
│       └── Session.java           # Client session handler with encryption
├── src/
│   ├── main/
│   │   ├── java/ca/ucalgary/seng300/
│   │   │   ├── app/               # Application entry point (MainApp)
│   │   │   ├── shared/            # Cross-team contracts and models
│   │   │   ├── core/
│   │   │   │   ├── identity/      # Client-side auth and networking
│   │   │   │   ├── registry/      # Game, Chat, and Player registries
│   │   │   │   ├── matchmaking/
│   │   │   │   ├── rooms/
│   │   │   │   └── turnengine/    # Game session management
│   │   │   ├── rules/
│   │   │   │   ├── leaderboard/   # Scoring models and database
│   │   │   │   └── validation/
│   │   │   ├── client/
│   │   │   │   ├── screens/       # FXML controllers
│   │   │   │   ├── components/
│   │   │   │   └── rendering/
│   │   │   └── games/
│   │   │       ├── tictactoe/     # Tic-Tac-Toe board and game logic
│   │   │       └── connectfour/   # Connect Four board and game logic
│   │   └── resources/
│   │       ├── css/               # Stylesheets
│   │       ├── fxml/              # Screen layouts
│   │       └── images/            # Static assets
│   └── test/                      # JUnit test suite
├── scripts/                       # Utility scripts
├── .gitlab-ci.yml                 # CI/CD pipeline configuration
├── .gitlab/                       # GitLab merge request templates
├── mvnw / mvnw.cmd                # Maven wrapper (Unix / Windows)
├── pom.xml                        # Client Maven build config
├── CHANGELOG.md                   # Version history
├── CURRENT_STATE.md               # Feature status matrix
└── team.md                        # Sub-team roster
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

_Last updated: 2026-04-03_
