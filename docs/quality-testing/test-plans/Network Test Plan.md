# Network System Testing Documentation
Contains the primary file tested for core client-server logic:
- NetworkTest.java

## Testing Strategy
The network system is tested using an **Isolated Unit Testing** approach. Tested whatever was possible to test without a live connection to a server, with tests utilize a custom `StubSocket` class. This stub allows us to fake an output from the server, letting us test the code that would run after probing the server.

## Main Findings

### NetworkTest.java
- **Encryption Symmetry:** Successfully verifies that payload encryption and decryption algorithms are perfectly symmetrical, ensuring data is not corrupted during the scrambling process.
- **Response Handling:** Confirms the client accurately translates varied raw byte responses from the server (e.g., parsing a `1` as a successful login, a `0` as a taken username, and a `2` as invalid credentials).
- **Data Unpacking:** Verifies the logic for parsing complex, multi-variable requests (Game Lists, Leaderboards, Match Records). When fed properly formatted mock byte strings, the client successfully reconstructs the data into the correct Java objects.
- **Requirement Supported:** Secure Client-Server Communication and Data Serialization.

## General Conclusion
The core networking pipeline is fully functional. The isolated logic handles data securely, encrypts payloads correctly, and interprets theoretical server responses exactly as intended without throwing casting or parsing exceptions.