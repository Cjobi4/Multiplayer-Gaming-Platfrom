package ca.ucalgary.seng300.shared.models;

/**
 * Configuration parameters for launching a game session.
 *
 * <p>Encapsulates settings such as player count, time limits, and
 * game-specific options needed to start a new game room.</p>
 *
 * <p>TODO: Define configuration fields and validation logic.</p>
 */
public class LaunchConfigs {

    private String gameId;
    private String gameUrl;
    private Boolean openInNewTab;
    private Boolean fullscreen;

    public String getUrl() {
        return gameUrl;
    }

    public Boolean validate() {
        return gameUrl != null && !gameUrl.isEmpty();
    }
}
