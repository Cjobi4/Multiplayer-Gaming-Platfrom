package ca.ucalgary.seng300.core.turnengine;

/**
 * Manages the state and turn progression of an active game session.
 *
 * <p>Handles the lifecycle of a single game: player joins, turn order,
 * move submission (delegating validation to the Rules &amp; Validation
 * subsystem), game state transitions, and end-of-game resolution.</p>
 *
 * <p>TODO: Implement session lifecycle (start, submitMove, endTurn,
 * endGame) and integrate with MoveValidator from shared interfaces.</p>
 */
public class GameSession {
    private String sessionId;
    private String playerId;
    private String gameId;
    private String startedAt;
    private int score;

    public void end()
    {
        // TODO: Close session
    }

    public void submitScore(int s)
    {
        this.score = s;
        // TODO: Call leaderboard to submit scores
    }

    public int getDuration()
    {
        // TODO: Track total time on session
        return 0;
    }
}
