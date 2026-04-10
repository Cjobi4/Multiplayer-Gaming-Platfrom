# Move Validation

**Owning Team:** Rules & Validation

Move validation pipeline designs: server-side authoritative validation, per-game rule enforcement, and illegal move rejection with error states.

## Contents

No standalone design artifacts. Move validation logic was implemented directly in the game classes (`TicTacToeGame`, `ConnectFourGame`) rather than as a separate pipeline. Validation includes bounds checking, occupied cell rejection (Tic-Tac-Toe), and full column rejection (Connect Four).

## Naming Conventions

- Diagrams: `<topic>-<diagram-type>-diagram.<ext>`
- Descriptions: `<topic>-<document-type>.<ext>`
