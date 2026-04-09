# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/).

## [Unreleased]

### Changed

- **Platform Core:** Changed match date format saved to match record by TTT (#265).

### Fixed

- **Integration:** Fixed compilation errors from merge conflict resolution.

## [v0.3.0] - 2026-04-06

### Added

- **Platform Core (MR !61):** `Matchmaker` class for skill-based matchmaking with dynamic tolerance, match offer/accept/reject handling (`Matchmaker.java`). `Request` class for server-initiated client transmissions (`Request.java`). `ActivePlayer` singleton class for locally tracking the logged-in user's state (#268). Match notification handling in `Session` for queue kicks, accepts, and rejects (#265). Synchronous `addRequestAndWait()` method in `Session` for server-to-client request/response with `CompletableFuture` (#265). Game session creation path for direct challenges (#265).
- **Client/UI (MR !62):** Leaderboard rendered on main page with data fetched from server (#188). Matchmaking popup logic and button on main page (#267). Game thumbnail images and descriptions displayed on radio buttons (#270). Image assets added for game pages (#271). TTT chat fully implemented in `TTTgameController.java`. Login and register functions connected to front-end through `Network.java`. Bypass button on all pages for development.
- **Rules & Validation (MR !60):** Server-side `TicTacToeGameSession` orchestrating full game loop between two connected players. `ConnectFourGameSession` with board state updates and result recording. Database integration helpers for match result storage.

### Changed

- **Platform Core (MR !61):** Copied `TicTacToeGameSession` to `server/` module (original remains in `games/tictactoe/`). Capped number of `matchRecord` entries returned to 10 (#265). `Session.processRequest()` visibility changed from private for test access (#203). Handles uncaught session errors by logging out, leaving matchmaking queues, and shutting down the session. Secure client-server communication handling in `Network.java`, including authentication, matchmaking, game actions, chat, and data retrieval using encrypted requests over a background network thread.
- **Client/UI (MR !62):** Menu bar cleaned up and reorganized (#220). Leaderboard display updated on opponent select pages (#191). Aesthetic UI fixes across screens (#270).

### Removed

- **Platform Core (MR !61):** Removed redundant `setUncaughtExceptionHandler()` from `Session` (#265). Extracted `Request` class out of `Session.java` into standalone `Request.java`.

### Fixed

- **Platform Core:** Fixed client disconnect not being handled properly (#265). Fixed leaderboard data not being seeded into database on initialization (#265). Fixed account creation failing to create a corresponding leaderboard entry (#265). Socket timeout no longer resets on every request (#265).
- **Client/UI:** Chat registry now clears when user navigates back from game pages. Login and create account error fields display correctly. Create account validation working end-to-end.
- **Integration:** Fixed all compilation errors across client and server modules.

## [v0.2.0] - 2026-03-30

### Added

- **Quality & Testing (MR !40):** Unit tests for `GameRegistry` and `Network` in Core. `TicTacToeBoard` tests (4 tests) and basic network encryption tests (#91, #167).
- **Platform Core (MR !41):** Created SQL tables for leaderboard and match record data storage. Password length requirement validation in `registerAccount()`. Direct challenge player list retrieval and `getSession()` method (#186). Shutdown instructions added to `Session` thread. Username validation rejecting `` ` `` and `^` characters (#186). `getGameType()` method added to leaderboard models (#166).
- **Client/UI (MR !38):** Team logo and project name on welcome page. Matchmaking button on main page (mirrors "Choose Opponent", full implementation deferred). Split "Choose Opponent" and game pages into dedicated FXML + controller files per game type (Connect 4 and Tic-Tac-Toe) to reduce merge conflicts. Search bar functionality with game filtering on main page (#192). Chat integration for Connect 4 game page (#199). Tic-Tac-Toe game grid UI with clickable buttons and animation (#197). Leaderboard section structure on main page (#188). Player list connected to opponent select screen (#210).
- **Rules & Validation (MR !30):** Created Connect Four game with `ConnectFourBoard` (6x7, gravity-based placement) and `ConnectFourGame` (win/draw detection, move validation).

### Changed

- **Platform Core (MR !41):** `processRequest()` updated to support concurrent client requests with a narrowed synchronized block to minimize blocking. Client UI controllers connected to game registry. Login and register account handling now notifies client of operation outcome. Removed userids; usernames now required unique (#49). Removed rank and matchID from models (#49). Password salts adjusted to be unique per username.
- **Client/UI (MR !38):** Login and Create Account pages visually updated with consistent styling. `PlayerRegistry` functions completed for online player tracking (#208).

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

- **Platform Core (MR !25):** `GameDTO.java` deleted (replaced by direct `Game` getters).

### Fixed

- **Integration:** Fixed `Network.getGames()` calling `Game` constructor with wrong argument types. Fixed incorrect `main.java.ca.ucalgary.seng300.games` package declarations in 6 game files. Removed duplicate `LeaderboardEntry` stub from `shared/models/`.
