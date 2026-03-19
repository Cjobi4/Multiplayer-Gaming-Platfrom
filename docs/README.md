# Documentation Directory

<!-- PLACEHOLDER -- to be expanded as documentation grows -->

This directory contains all non-code project artifacts: design documents, diagrams, planning docs, and presentations. It is organized by component to mirror the source package structure.

## Directory Layout

| Directory | Owning Team | Contents |
|---|---|---|
| `architecture/` | Integration & Release / Platform Core | System-wide architecture diagrams and decision records |
| `platform-core/` | Platform Core | Designs for identity, game registry, rooms, matchmaking, turn engine, persistence |
| `client-ui/` | Client/UI | UI/UX designs, screen flows, wireframes, game rendering conventions |
| `rules-validation/` | Rules & Validation | Move validation pipeline and leaderboard subsystem designs |
| `quality-testing/` | Quality & Testing | Test plans, test case specifications, coverage reports |
| `integration-release/` | Integration & Release | Process docs, integration issue logs, presentations |

## File Naming Conventions

- **Directories:** lowercase, hyphenated (e.g., `rooms-and-matchmaking/`)
- **Diagrams (PNG/SVG):** kebab-case with diagram type suffix (e.g., `login-sequence-diagram.png`)
- **Documents (PDF):** kebab-case with component prefix (e.g., `database-use-case-descriptions.pdf`)
- **Drawio source files:** kebab-case matching the rendered PNG (e.g., `leaderboard-use-case.drawio`)
- **Markdown docs:** kebab-case (e.g., `integration-release-plan.md`)
- **README files:** Always `README.md` (uppercase)

## Adding a New Document

1. Determine the owning component directory.
2. Name the file using the conventions above.
3. Place the file in the appropriate sub-directory.
4. Update the component's `README.md` to list the new file.
