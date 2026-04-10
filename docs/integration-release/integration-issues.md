# Integration Issues

This document tracks issues discovered during cross-team integration on `staging-branch`.

## Issue Log

| Issue # | Date | Description | Affected Teams | Status | Resolution |
|---|---|---|---|---|---|
| I-001 | 2026-03-25 | `Network.getGames()` called `Game` constructor with wrong argument types after `Game.java` was rewritten | Platform Core, Client/UI | Resolved | Fixed constructor call to match updated `Game` class signature |
| I-002 | 2026-03-25 | Incorrect `main.java.ca.ucalgary.seng300.games` package declarations in 6 game files | Rules & Validation | Resolved | Corrected package declarations to `ca.ucalgary.seng300.games` |
| I-003 | 2026-03-25 | Duplicate `LeaderboardEntry` stub in `shared/models/` conflicted with `rules/leaderboard/` version | Platform Core, Rules & Validation | Resolved | Removed duplicate stub from `shared/models/` |
| I-004 | 2026-04-06 | Compilation errors from merge conflicts between Client/UI staging and Platform Core staging | Client/UI, Platform Core | Resolved | Fixed conflicting imports and method signatures |
| I-005 | 2026-04-07 | Compilation errors during merge of Client/UI staging into staging-branch | Client/UI, Integration & Release | Resolved | Fixed broken imports after merge conflict resolution |
| I-006 | 2026-04-08 | Client disconnect not handled properly in server `Session`, leaving stale queue entries | Platform Core | Resolved | Added cleanup for disconnected clients (logout, leave queues, shutdown session) |
| I-007 | 2026-04-08 | Leaderboard data not seeded into database on initialization | Platform Core, Rules & Validation | Resolved | Added leaderboard seed data to `databaseInitial()` |
| I-008 | 2026-04-08 | Account creation did not create a corresponding leaderboard entry | Platform Core, Rules & Validation | Resolved | Added leaderboard entry creation during account registration |
| I-009 | 2026-04-09 | Chat messages interfered with turn-based moves during gameplay | Platform Core, Client/UI | Resolved | Separated chat message handling from turn-based move processing (#265) |
| I-010 | 2026-04-09 | Game state sent to clients twice after match ends | Platform Core | Resolved | Fixed duplicate game state transmission in server sessions (#265) |
| I-011 | 2026-04-09 | Connect Four diagonal and horizontal win checking broken after server integration | Rules & Validation, Client/UI | Resolved | Fixed win condition logic in Connect Four game classes |
| I-012 | 2026-04-09 | Obsolete test cases failing after source code changes from cross-team integration | Quality & Testing | Resolved | Commented out obsolete tests and updated remaining tests to match current API |
| I-013 | 2026-04-09 | `ConnectFourGameSession` sending incorrect request byte constants | Platform Core | Resolved | Adjusted request numbers in `ConnectFourGameSession` (#265) |

## Status Legend

- **Open** -- Issue identified, not yet resolved.
- **In Progress** -- Actively being worked on.
- **Resolved** -- Fix applied and verified.
- **Won't Fix** -- Documented but intentionally not addressed.
