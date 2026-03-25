package ca.ucalgary.seng300.shared.models;

import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game available on the platform.
 *
 * <p>This is a shared model class used across all sub-teams. Any changes
 * require cross-team review per the Integration Release Plan.</p>
 */
public class Game {
    private String id;
    private String title;
    private String description;
    private String gameUrl;

    private List<Tag> tags = new ArrayList<>();
    private LaunchConfigs launchConfigs;
    private LeaderBoard leaderBoard;

    public Game (String id, String title, String description, String gameUrl, String tags, String launchConfigs, String leaderboard)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.gameUrl = gameUrl;

        // TODO: Finish constructor for game
    }

    public String getId()
    {
        return id;
    }

    public void launch()
    {
        // TODO: Implementation for launching game here
    }

    public GameDTO getMetaData()
    {
        GameDTO dto = new GameDTO();
        dto.id = this.id;
        dto.title = this.title;
        dto.gameUrl = this.gameUrl;
        return dto;
    }

    public String getUrl() {
        return gameUrl;
    }
}
