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
    private final String id;
    private final String title;
    private final String description;

    private List<Tag> tags = new ArrayList<>();

    /**
     * Constructor for creating a new game object. Tags, LaunchConfigs, and leaderboard object must be created first
     * @param id the game id
     * @param title the title of the game
     * @param description the game description
     * @param tags the tags for the game
     */
    public Game (String id, String title, String description, List<Tag> tags)
    {
        this.id = id;
        this.title = title;
        this.description = description;

        // These are not string objects, but must be pulled from database.
        // Must be made using respective classes constructor before passing into here
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
