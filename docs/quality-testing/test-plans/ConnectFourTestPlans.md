# Connect Four Testing Documentation
Contains two primary files that need to be tested:
- ConnectFourBoard.java
- ConnectFourGame.java

## Testing Strategy
These two class functions are made independent of one another, thus testing each function is the strategy of the testing

## Main Findings

### ConnectFourBoard.java
- This class does the following: create a 6x7 board, checks when the board is full, drops pieces to the board, and prints the board
- Tested that the board is correctly created and initialized
- Tested that the piece is correctly dropped in the board, and checked edge cases
- Checked that the board is declared as full when it is, not full when it isn't, and not full when only partially full
- Tested that the board is correctly printed
- No errors found after tests were conducted

### ConnectFourGame.java
- The class does the following: initializes the game, makes the move, switches player turn, checks if a win has occurred
- Want to test each method separately where possible and test in combinations
- The following methods work correctly: makeMove(), switchPlayer(), checkWin(), ConnectFourGame() is implicitly tested
- The following methods work incorrectly: NA
- All logic is working as it should and no false negatives/positives were made/found

## General Conclusion
- All methods within both classes are correctly working
- No bugs were found
