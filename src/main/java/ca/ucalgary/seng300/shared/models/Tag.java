package ca.ucalgary.seng300.shared.models;

/**
 * Represents a tag used to categorize games in the registry.
 *
 * <p>Tags enable filtering and searching games by category (e.g., "strategy",
 * "two-player", "quick-play"). Used by the game registry and Client/UI
 * browse-and-play workflows.</p>
 */
public class Tag {

    private final String label;
    private final String color;

    public Tag(String label, String color)
    {
        this.label = label;
        this.color = color;
    }

    public String getLabel()
    {
        return label;
    }

    public String getColor()
    {
        return color;
    }

    /**
     * Function to check if this tag matches a searched tag
     * @param g the search
     * @return true if matches search
     */
    public Boolean matches(String g)
    {
        return label != null && label.equalsIgnoreCase(g);
    }
}
