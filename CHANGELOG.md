# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/).

## [Unreleased]

## [v0.1.0] - 2026-03-25

### Added
- **Platform Core (MR !25):** Server session handling (`Session.java`) with login, registration, chat, and game list request processing. Client-side `Network` rewritten as threaded socket client with login, chat, and game list methods. `ChatRegistry` and `Message` model for in-game chat. `GameType` enum, `LeaderboardDatabase`, and `MatchRecord` for leaderboard persistence. `uuid-creator` dependency added to root `pom.xml`; `sqlite-jdbc` added to `server/pom.xml`.
- **Rules & Validation (MR !27):** `TicTacToeBoard` with 3x3 board, cell validation, and database serialization (`toString`/`fromString`). `TicTacToeGame` with move placement, validation, and win condition checking. `GameEngine` interface, `GameController`, `GameState` enum, and supporting classes (`Move`, `Player`, `GeneralStats`).
- **Client/UI (MR !24):** Create Account and Game Over screens with controllers and FXML. Back buttons on all screens. Log Out button on main page. CSS theme (`style.css`) applied across all pages. Menu bar with Change Log, Info, and Admin Control sections. Search bar on main page.

### Changed
- **Platform Core (MR !25):** `Game.java` rewritten with parameterized constructor and `final` fields. `Tag.java` rewritten with constructor, `colour` renamed to `color`, `id` field removed. `LaunchConfigs.java` rewritten with constructor. `GameRegistry.unregister()` method removed. `Database.java` and `Network.java` moved from `core/persistence/` to `server/` module. `LeaderboardEntry` updated with `wins`/`matches` fields replacing `score`.
- **Client/UI (MR !24):** `MainApp.java` window title changed from "Welcome Page" to "Welcome!". All FXML layouts updated with new color scheme and Dubai font family.
- **Integration:** Enabled `--enable-native-access=javafx.graphics` in `pom.xml` to suppress JDK 25 warnings.

### Removed
- **Platform Core (MR !25):** `GameDTO.java` and `shared/models/Player.java` deleted (replaced by direct `Game` getters and `games/Player.java` respectively).

### Fixed
- **Integration:** Fixed `Network.getGames()` calling `Game` constructor with wrong argument types. Fixed incorrect `main.java.ca.ucalgary.seng300.games` package declarations in 6 game files. Removed duplicate `LeaderboardEntry` stub from `shared/models/`.


## [v0.2.0] - 2026-03-30

### Added
- **Quality & Testing (MR !40):** Created partially testing fuctions for `GameRegistry`, `Network` in Core and run successfully. 
- **Platform Core (MR !41)**: Created SQL tables for leaderboard and match record data storage. Password length requirement validation in `registerAccount()`
- **Client-UI (MR !38)**: Team logo and project name on welcome page; matchmaking button (currently mirrors "Choose Opponent", full implementation next week); split "Choose Opponent" and game pages into dedicated FXML + controller files per game type (Connect 4 and Tic Tac Toe) to reduce future merge conflicts
- **Rules & Validation(MR !30)**: Created connectFour game 

### Changed
- **Platform Core(MR !41)**: `processRequest()` updated to support concurrent client requests with a narrowed synchronized block to minimize blocking; client UI controllers connected to game registry; login and register account handling now notifies client of operation outcome
- **Client UI(MR !38)**: Login and Create Account pages are now visually 

## [v0.3.0] - 2026-04-06

## Added 
- **Client UI(MR !62)**: TTT chat is fully implemented in `TTTgameController.java`, login and register function also connected to front-end through `Network.java`, and bypass button on all pages needed for development. Synchronous addRequestAndWait() method in `Session` for server-to-client request/response with `CompletableFuture`. Game session creation path for direct challenges.
- **Platform Core (MR !61)**: Matches two players with similar win rates, creates a game if both accept, otherwise reports who declined or if an error occurred through `Matchmaker.java`. Class for sending and receiving requests to the client FROM THE SERVER (server initiated transmissions) through `Request.java` (implemented class)
- **Rules & Validation(MR !60)**: Server-side `TicTacToeGameSession` orchestrating full game loop between two connected players. `ConnectFourGameSession` with board state updates and result recording. Database integration helpers for match result storage.

## Changed 
- **Platform Core (MR !61)**: Handles uncaught session errors by logging out, leaving matchmaking queues, and shutting down the session in `Session.java`. Handles secure client-server communication, including authentication, matchmaking, game actions, chat, and data retrieval using encrypted requests over a background network thread in `Network.java`
- **Client/UI (MR !62)**: Menu bar cleaned up and reorganized. Leaderboard display updated on opponent select pages. Aesthetic UI fixes across screens.

## Removed
- **Platform Core (MR !61)**: Separate class Request out of `Session.java` to new `Request.java` file