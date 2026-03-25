package ca.ucalgary.seng300.core.registry;

import ca.ucalgary.seng300.shared.models.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the catalog of available games on the platform.
 * Responsible for registering, unregistering, and listing games.
 * Implements the game registry interface from the shared contracts layer.
 * The data layer is owned by Platform Core; the UI flow is owned by
 * Client/UI in the screens package.
 */
public class GameRegistry {

    // the game registry
    private static GameRegistry instance;

    // the list of games
    private List<Game> games;

    // constructor
    private GameRegistry()
    {
        this.games = new ArrayList<>();
    }

    /**
     * Gets the current instance of the game registry
     * @return the game registry, if none exists one is made and returned.
     */
    public static GameRegistry getInstance()
    {
        if (instance == null)
        {
            instance = new GameRegistry();
        }

        return instance;
    }

    /**
     * used to register games into the game registry
     * @param g the game object you want to register
     */
    public void register(Game g)
    {
        games.add(g);
    }


    /**
     * Searches the list of games by id, if it exists returns that game
     * @param id the string id of the game
     * @return the game object if found, otherwise null
     */
    public Game findById(String id)
    {
        for (Game g : games)
        {
            if (g.getId().equals(id))
            {
                return g;
            }
        }

        return null;
    }

    /**
     * Simple function to return a list of all games currently in the game registry
     * @return List of all games
     */
    public List<Game> ListAll()
    {
        return games;
    }
}
