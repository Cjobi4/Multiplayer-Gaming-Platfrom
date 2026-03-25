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

    private final String gameId;
    private final String gameUrl;
    private final Boolean openInNewTab;
    private final Boolean fullscreen;

    public LaunchConfigs(String gameId, String gameUrl, String openInNewTab, String fullscreen)
    {
        this.gameId = gameId;
        this.gameUrl = gameUrl;
        this.openInNewTab = openInNewTab.equalsIgnoreCase("true");
        this.fullscreen = fullscreen.equalsIgnoreCase("true");
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public Boolean getFullscreen() {
        return fullscreen;
    }

    public Boolean getOpenInNewTab() {
        return openInNewTab;
    }

    public String getGameId() {
        return gameId;
    }
}
