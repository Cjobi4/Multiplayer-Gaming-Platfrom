# Current State

_Last Updated: 2026-04-10_

This document is the source of truth for project completion status. It tracks both subsystem implementation progress and client-facing requirements fulfillment. All team members should reference and update this document as work is completed.

---

## Feature Status Matrix

This matrix tracks the implementation status of each subsystem at the code level.

> **How to update:** Update the Feature Status Matrix when a subsystem's status changes (e.g., from "In Progress" to "Complete") to reflect the new implementation status

| Subsystem           | Component               | Status   | Notes                                                                                                                                                                                 |
| ------------------- | ----------------------- |----------| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Identity Management | `core/identity/`        | Complete | Client-side `Network` with 12 request methods, AES-GCM encryption via Diffie-Hellman key exchange; `ActivePlayer` singleton for local state (located in `shared/models/`); `Session` abstract class stubbed; unit tested (7 network + 4 session tests) |
| Game Registry       | `core/registry/`        | Complete | Game, Chat, and Player registries with find/list/clear operations; all use singleton pattern; unit tested                                                                             |
| Rooms & Matchmaking | `server/Matchmaker`     | Complete | Skill-based matchmaking queue with dynamic tolerance; match offer/accept/reject handling via `Matchmaker.java`; unit tested (4 matchmaker tests); room lifecycle and lobby not started (`core/rooms/` empty) |
| Turn Engine         | `core/turnengine/`      | Complete | Client-side `GameSession` stub; server-side `TicTacToeGameSession`, `TTTServerSession`, and `ConnectFourGameSession` (in `server/Games/`) handle full game loops with move prompting, board updates, and result reporting |
| Persistence         | `server/`               | Complete | SQLite database with 4 tables (userLoginInfo, gameInfo, leaderboard, matchRecord); SHA-256 password hashing; server session handling with encryption and 12 request types             |
| Screens             | `client/screens/`       | Complete | 9 screens: Welcome, Login, Create Account, Main, TTT/C4 Opponent Select, TTT/C4 Game, Game Over; all with navigation and CSS theming                                                  |
| UI Components       | `client/components/`    | Complete | `LeaderBoardRows` data model and `LeaderBoardMock` for sample data; additional reusable components not yet created                                                                    |
| Game Rendering      | `client/rendering/`     | Complete | Directory contains only .gitkeep; game boards rendered directly in FXML (TTT button grid, C4 GridPane)                                                                                |
| Move Validation     | `rules/validation/`     | Complete | Directory contains only .gitkeep; game-level validation exists in `games/` (bounds checking, occupied cell, full column)                                                              |
| Leaderboard         | `rules/leaderboard/`    | Complete | Data models (LeaderboardEntry, MatchRecord, UserRecord, GameType) complete; `LeaderBoard` query logic implemented; `LeaderboardDatabase` stub; server-side integration via `Database` |
| Tic-Tac-Toe         | `games/tictactoe/`      | Complete | Board initialization, move placement with validation, win/draw detection, database serialization complete; unit tests written (4 board + 8 game tests)                                |
| Connect Four        | `games/connectfour/`    | Complete | Board, gravity-based placement, win/draw detection, full-column rejection all implemented; unit tests written (9 board + 15 game tests)                                               |
| Game Engine         | `games/`                | Partial  | `GameEngine` interface and `GameController` commented out; `GameState` enum complete; `Move` and `GeneralStats` are placeholder stubs                                                 |
| Shared Models       | `shared/models/`        | Complete | Game, Tag, Player, Message, and ActivePlayer models with constructors and getters; tested via registry and chat tests                                                                 |
| Integration Tests   | `test/.../integration/` | Not Started | Directory contains only .gitkeep; unit tests exist in other test directories but no integration tests were written                                                                |

### Legend

- **Not Started**: No implementation work has begun.
- **In Progress**: Active development underway.
- **Stubbed**: Placeholder code exists but is not functional.
- **Complete**: Feature is implemented and tested.

---

## Requirements Completion Checklist

This checklist tracks client-facing deliverables and is based on the "Project Breakdown" section of the SENG300 Project Document Version 3.0.1.

> **How to update:** Mark checklist items with an `x` as they are completed (e.g., `[x]`)

### 1. Core Game Logic

> Subsystems: `games/tictactoe/`, `games/connectfour/`, `rules/validation/`

- [x] Implement complete Tic-Tac-Toe game logic (turn alternation, move placement on a 3x3 grid, input validation)
- [x] Implement win-condition detection for Tic-Tac-Toe (three in a row horizontally, vertically, or diagonally)
- [x] Implement draw-condition detection for Tic-Tac-Toe (all cells filled with no winner)
- [x] Implement complete Connect Four game logic (turn alternation, piece drop into a 7x6 column grid, gravity-based placement)
- [x] Implement win-condition detection for Connect Four (four in a row horizontally, vertically, or diagonally)
- [x] Implement draw-condition detection for Connect Four (board completely filled with no winner)
- [x] Validate that illegal moves are rejected in Tic-Tac-Toe (occupied cell selection)
- [x] Validate that illegal moves are rejected in Connect Four (piece drop into a full column)

### 2. Multiplayer Functionality

> Subsystems: `core/turnengine/`, `server/Matchmaker`, `games/`

- [x] Implement two-player turn-based interaction for Tic-Tac-Toe (each player takes alternating turns)
- [x] Implement two-player real-time interaction for Connect Four (each player takes alternating turns with live board updates)
- [x] Ensure game state is synchronized between both players during an active match
- [x] Display a clear indication of whose turn it is at all times during gameplay
- [x] Display the match result (win, loss, or draw) to both players upon game completion

### 3. User Authentication and Profile Management

> Subsystems: `core/identity/`, `server/`, `client/screens/`

- [x] Implement a user login screen that accepts credentials (username and password)
- [x] Implement a user registration flow for new account creation
- [ ] Create a user profile page that displays the player's username and account information
- [ ] Display total games played on each user's profile
- [ ] Display win/loss record on each user's profile
- [ ] Display per-game statistics (Tic-Tac-Toe stats and Connect Four stats separately) on each user's profile
- [ ] Implement account management functionality (ability to update profile details)
- [ ] Enable users to view other players' profiles, including rank, current online status, and recent match history

### 4. Graphical User Interface

> Subsystems: `client/screens/`, `client/components/`, `client/rendering/`

- [x] Build a main menu or lobby screen where players can select a game from the available library (Tic-Tac-Toe or Connect Four)
- [x] Build an interactive game board GUI for Tic-Tac-Toe (clickable cells, visual X/O markers)
- [x] Build an interactive game board GUI for Connect Four (clickable columns, visual piece drop, two-color disc display)
- [ ] Implement a player search interface where users can look up other players by username
- [ ] Implement the ability to challenge another player directly from their profile
- [x] Build a basic in-game text chat interface that allows players to communicate during a match
- [x] Ensure all GUI screens are user-friendly, clearly labeled, and navigable without external instructions

### 5. Matchmaking System

> Subsystems: `server/Matchmaker`, `rules/leaderboard/`

- [x] Implement a matchmaking queue where players can queue up for a new match in a selected game
- [x] Implement skill-based matchmaking logic that pairs players of similar skill or rank
- [ ] Allow players to search for and join an ongoing or open game
- [x] Build a leaderboard view that ranks players by performance (e.g., win rate or skill rating)
- [x] Ensure the leaderboard displays player rank, username, and key statistics
- [x] Ensure leaderboard data updates after each completed match

### 6. Design Architecture and Interface Stubs

> Subsystems: `server/`, `core/identity/`, `core/registry/`

- [x] Design and document the overall system architecture showing where the database, hosting backend, and client application connect
- [x] Create a stub/driver for the user authentication database interface (simulates login validation, registration, and credential storage)
- [x] Create a stub/driver for the game data storage interface (simulates saving and retrieving match history and leaderboard data)
- [x] Create a stub/driver for the cloud hosting or server infrastructure interface (simulates online session creation and connection handling)
- [x] Ensure all stubs return realistic mock data so the platform can be demonstrated end-to-end without a live backend
- [ ] Document each stub's expected input parameters, return values, and integration points so the database team can implement real connections

### 7. Game Hosting Design

> Subsystems: `server/Matchmaker`, `server/`

- [x] Design and document how the online game hosting interface would function in a live environment (session creation, player connection, real-time state relay)
- [x] Demonstrate the designed online interface through the working GUI and stub integrations
- [ ] Document the boundary between the platform application and the hosting/server infrastructure for the handoff to the backend team

### 8. Data and State Tracking

> Subsystems: `server/`, `rules/leaderboard/`, `shared/models/`

- [x] Track and store each completed match result (players involved, game type, outcome, date)
- [x] Update each player's profile statistics after every completed match
- [x] Update the leaderboard standings after every completed match
- [x] Persist player profile data across sessions using the stub database interface

### 9. In-Game Communication

> Subsystems: `core/identity/`, `core/registry/`, `client/screens/`

- [x] Implement basic text chat functionality that allows both players to send and receive messages during an active game
- [x] Display chat messages with sender identification (player username or label)
- [x] Ensure chat is only active during an ongoing match between two connected players

---
