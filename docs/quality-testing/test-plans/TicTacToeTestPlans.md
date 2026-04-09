# Tic Tac Toe Testing Documentation
Contains two primary files that need to be tested:
-TicTacToeBoard.java
-TicTacToeGame.java

## Testing Strategy

Because these two classes are built to function essentially independently, it is possible to simply test individual functions rather than force an integrated test with the rest of the classes.

## Main Findings

### TicTacToeBoard.java
-Rather simple class, simply creates a blank 3x3 board
-Main functionality to test is the ability to create a board from a string, as the base initialization is effectively trivial
-Code functions properly, no errors found and all functionality works as intended
-Edge cases have been tested, such as nearly or completely full boards

### TicTacToeGame.java
-Slightly more complex class, primarily because it has inherently more functions than TicTacToeBoard.java. 
-Main functionality to test is the makeMove() function, because it is what triggers the rest of the functions
-Class also relies rather heavily on GameState toggles, which as a lesser note do not have an explicit setter
-Logic and functionality for makeMove() is working as intended, it correctly updates the Board and changes GameState
-Win detection is also working as intended, no false positives/negatives could be found

## General Conclusion
The TicTacToe Game has no observable errors, and should be ready for implementation with the rest of the project. 