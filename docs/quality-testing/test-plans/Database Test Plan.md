# Database Systems Testing Documentation
Contains the architectural justification for database testing scope:
- DatabaseTest.java (Deprecated/Placeholder)

## Testing Strategy & Architectural Justification
Initially, `DatabaseTest.java` was created to test basic database retrieval operations, such as fetching the leaderboard or game list. However, upon review of the source code, these tests were deemed unnecessary as they would overlap almost entirely with NetworkTest.java and SessionTest.java.

1. **Coverage via Integration (SessionTest):** The `SessionTest.java` class already acts as a live, end-to-end test of the database. When `SessionTest` successfully registers a user or fetches a leaderboard, it implicitly proves that the database's `INSERT` and `SELECT` queries are functioning perfectly.
2. **Coverage via Serialization (NetworkTest):** The `NetworkTest.java` suite proves that the client can correctly parse and map the data exactly as the database formats it.

## Main Findings
- Standalone database tests are unneeded because their intended functionality—verifying that data can be saved and retrieved safely—is completely absorbed by the `SessionTest` and `NetworkTest` suites.
- **Requirement Supported:** Data Persistence (verified through Session Integration).
