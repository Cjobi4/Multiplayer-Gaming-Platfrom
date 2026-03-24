package ca.ucalgary.seng300.shared.models;

/**
 * Data Transfer Object for transferring game information between layers.
 *
 * <p>This DTO provides a serialization-safe representation of {@link Game}
 * for use in client-server communication and UI rendering.</p>
 *
 * <p>TODO: Define fields mirroring Game and implement serialization support.</p>
 */
public class GameDTO {

    // make private add get / set

    public String id;
    public String title;
    public String gameUrl;
}
