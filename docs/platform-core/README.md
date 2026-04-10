# Platform Core Documentation

<!-- PLACEHOLDER -- to be expanded as designs evolve -->

**Owning Team:** Platform Core

Design artifacts for all Platform Core subsystems.

## Sub-Directories

| Directory | Subsystem | Status |
|---|---|---|
| `identity/` | Identity management and authentication | Complete |
| `game-registry/` | Game registry data layer | Complete |
| `rooms-and-matchmaking/` | Room lifecycle, lobby, matchmaking | Complete |
| `turn-engine/` | Turn progression and game state management | Complete |
| `persistence/` | Database design, server/network architecture | Complete |

## Top-Level Artifacts

| File | Description |
|---|---|
| `platform-use-case-diagram.svg` | Platform-level use case diagram |
| `platform-use-case-descriptions.md` | Use case descriptions for the platform |
| `platform-core-presentation.pdf` | Platform Core sub-team presentation |

## Cross-References

- Architecture overview: [docs/architecture/](../architecture/README.md)
- Shared interfaces consumed by Platform Core: `src/main/java/.../shared/interfaces/`
