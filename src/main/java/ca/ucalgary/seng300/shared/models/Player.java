package ca.ucalgary.seng300.shared.models;

import ca.ucalgary.seng300.core.turnengine.GameSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player on the platform.
 *
 * <p>Contains identity information (username, display name) and session
 * state. Used across all sub-teams as the shared player representation.</p>
 *
 * <p>TODO: Define fields (username, displayName, sessionId, status) and
 * implement identity contract.</p>
 */
public class Player {
    private String id;
    private String title;
    private String description;
    private String gameUrl;

    public void startSession(Game g) {
        // TODO: Implementation to initialize a GameSession
    }

    public List<GameSession> getHistory() {
        // TODO: Implementation for checking history
        return new ArrayList<>();
    }

    public int getHighScore(String id, int score) {
        // TODO: Implementation for checking high score
        return 0;
    }
}
