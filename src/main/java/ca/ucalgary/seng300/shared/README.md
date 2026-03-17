# Shared Contracts Package

<!-- PLACEHOLDER -- to be expanded as shared interfaces are defined -->

## Purpose

This package is the **single source of truth for cross-team contracts**. All cross-team
dependencies flow through `shared/` — no team should directly import from another team's
implementation package.

## Sub-Packages

| Sub-Package | Contents | Status |
|-------------|----------|--------|
| `interfaces/` | Java interfaces: `PersistenceService`, `Game`, `MoveValidator`, `UserRepository`, etc. | Not started |
| `models/` | Shared domain objects: `Player`, `Game`, `GameDTO`, `Tag`, `LeaderboardEntry`, `LaunchConfigs` | Stubs created |

## Rules

1. **Platform Core defines** shared interfaces and models. Other teams consume them.
2. **No team imports from another team's implementation package.** For example, `client/`
   imports from `shared/interfaces/` but never from `core/identity/`.
3. **Changes to `shared/` require cross-team review.** Any merge request touching `shared/`
   needs 1 peer reviewer + 1 reviewer from each affected team.
4. **New interfaces go through a proposal process.** The proposing team opens a GitLab issue
   describing the interface, tags affected teams, and waits for consensus before creating
   the merge request.

## Shared Interfaces (Planned)

These correspond to the 7 shared interface areas from the Integration Release Plan:

1. Database / Persistence stubs
2. Identity contracts (authentication, session management)
3. Game Registry interface
4. Matchmaking interface
5. Turn Engine / Game State interface
6. Move Validation interface
7. Leaderboard / Scoring interface

## Naming Conventions

- Interface files: PascalCase, descriptive name (e.g., `PersistenceService.java`)
- Model files: PascalCase, singular noun (e.g., `Player.java`, `GameState.java`)
