# Current State

<!-- PLACEHOLDER -- to be updated each iteration -->

This document tracks the implementation status of each subsystem.

## Feature Status Matrix

| Subsystem | Component | Status | Notes |
|---|---|---|---|
| Identity Management | `core/identity/` | In Progress | Client-side network requests for login, registration, chat; session management stubbed |
| Game Registry | `core/registry/` | In Progress | Game catalog with register/find/list; chat registry added |
| Rooms & Matchmaking | `core/rooms/`, `core/matchmaking/` | Not Started | Room lifecycle, lobby, matchmaking |
| Turn Engine | `core/turnengine/` | Stubbed | Turn progression, game state management |
| Persistence | `server/` | In Progress | SQLite database with user login, game info tables; server session handling with login, registration, chat, game list requests |
| Screens | `client/screens/` | In Progress | Welcome, Login, Create Account, Main, Opponent Select, Game, Game Over screens with navigation and CSS theming |
| UI Components | `client/components/` | Not Started | Reusable UI components |
| Game Rendering | `client/rendering/` | Not Started | Board rendering for sample games |
| Move Validation | `rules/validation/` | Not Started | Server-side move validation pipeline |
| Leaderboard | `rules/leaderboard/` | In Progress | Leaderboard scoring, match records, per-game leaderboard database |
| Tic-Tac-Toe | `games/tictactoe/` | In Progress | Board initialization, move placement with validation, win condition checking, database serialization |
| Connect Four | `games/connectfour/` | Not Started | Sample game implementation |
| Game Engine | `games/` | In Progress | GameEngine interface, GameController, GameState enum, supporting classes (Move, Player, GeneralStats) |
| Shared Models | `shared/models/` | In Progress | Game, Tag, LaunchConfigs with constructors and getters; Message model; GameDTO and shared Player removed |
| Integration Tests | `test/.../integration/` | Not Started | End-to-end integration tests |

## Legend

- **Not Started** -- No implementation work has begun.
- **In Progress** -- Active development underway.
- **Stubbed** -- Placeholder code exists but is not functional.
- **Complete** -- Feature is implemented and tested.
