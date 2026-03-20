# Current State

<!-- PLACEHOLDER -- to be updated each iteration -->

This document tracks the implementation status of each subsystem.

## Feature Status Matrix

| Subsystem | Component | Status | Notes |
|---|---|---|---|
| Identity Management | `core/identity/` | Not Started | Authentication, session management, user profiles |
| Game Registry | `core/registry/` | Stubbed | Game catalog management |
| Rooms & Matchmaking | `core/rooms/`, `core/matchmaking/` | Not Started | Room lifecycle, lobby, matchmaking |
| Turn Engine | `core/turnengine/` | Stubbed | Turn progression, game state management |
| Persistence | `core/persistence/` | Not Started | SQLite database wrapper |
| Screens | `client/screens/` | Not Started | JavaFX controllers for each screen |
| UI Components | `client/components/` | Not Started | Reusable UI components |
| Game Rendering | `client/rendering/` | Not Started | Board rendering for sample games |
| Move Validation | `rules/validation/` | Not Started | Server-side move validation pipeline |
| Leaderboard | `rules/leaderboard/` | Stubbed | Leaderboard scoring and queries |
| Tic-Tac-Toe | `games/tictactoe/` | Not Started | Sample game implementation |
| Connect Four | `games/connectfour/` | Not Started | Sample game implementation |
| Shared Interfaces | `shared/interfaces/` | Not Started | Cross-team contracts |
| Shared Models | `shared/models/` | Stubbed | Domain objects |
| Integration Tests | `test/.../integration/` | Not Started | End-to-end integration tests |

## Legend

- **Not Started** -- No implementation work has begun.
- **In Progress** -- Active development underway.
- **Stubbed** -- Placeholder code exists but is not functional.
- **Complete** -- Feature is implemented and tested.
