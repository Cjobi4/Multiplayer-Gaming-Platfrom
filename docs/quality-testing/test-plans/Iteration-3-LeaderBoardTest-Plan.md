# LeaderBoard Testing Documentation
Contains four files that need to be tested
- LeaderBoard.java
- LeaderboardEntry.java
- MatchRecord.java
- UserRecord.java
Note: After discussion with platform-core, no testing for LeaderboardDatabase.java was needed as it was not going to be used

## Testing Strategy
- Started by testing from the least complex classes to the most complex class
  - this was based on the fact that LeaderBoard.java uses the logic of LeaderboardEntry.java, MatchRecord.java, and UserRecord.java
  - Since that was the case, we wanted to ensure that those three classes were correctly working before testing LeaderBoard.java
- Created a network stub for ease of testing 

## Main Findings

### UserRecord.java
- The class does the following: create a user record and has getters for tic-tac-toes wins, connect 4 wins, tic-tac-toe matches, connect 4 matches, total wins, and total matches
- Testing for this class included initializing one user record and ensuring that all getters return the correct values
- No errors found after conducting tests for this class

### MatchRecord.java
- The class does the following: creates a Match Record -- which contains information of the users in the match, the game type, the winner, and creates the date when called
- Testing for the date is difficult as it includes the hours, thus a dummy date was needed to assertFalse for when the date was checked
- No errors found after conducting tests for this class

### LeaderboardEntry.java
- This class does the following: create a leaderboard entry with username, the number of wins, and the number of matches that the user participated in. Additionally, it has getters for all three of those inputs
- Getters and setters were tested
- No errors found after conducting tests for this class

### LeaderBoard.java
- This class does the following: gets the leaderboard for a specific game type, gets the user record, and gets the user match record
- In order to create a controlled environment for testing, a stub network was created -- named as LeaderBoardNetworkStub.java
- Small bug was found, notified the author and fixed it
  - Lines 26, 31, and 36 were missing .get(), thus the correct values were not being stored in the specific leaderboard entries
- No other problems were found after testing


## General Conclusion
- All methods within both classes are correctly working
- One bug found in leaderboard, promptly corrected