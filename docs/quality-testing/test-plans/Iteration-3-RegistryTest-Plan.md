# Registry Testing Documentation
The registry included three classes: GameRegistry.java, ChatRegsitry.java, and PlayerRegistry.java

## Testing Strategy
- All classes function separately from each other 
- None of them are involved in any way, but all belong to the registry, thus all are tested separately.

## Main Findings

### GameRegistry.java
- This class does the following: creates a game registry, gets the instance of the game registry, registers games, finds the game by id, finds the game by name, and lists all games in the registry
- Tested all methods, including cases for when searching for something that does not exist in the registry
- No problems were found after testing

### ChatRegistry.java
- This class does the following: creates a chat registry, gets instance of the registry, adds a message, creates a list off all messages in the registry, and clears the chat history
- All methods have been tested
- No problems were found after testing

### PlayerRegistry.java
- The class does the following: creates a player registry, gets instance of the registry, registers and unregisters a player, finds player by name, creates a list of all players, and clears the players from the registry
- All methods have been tested, including cases for when player does not exist/is not registered
- No problems were found after testing

## General Conclusion
- All methods in all classes were working
- No bugs found
