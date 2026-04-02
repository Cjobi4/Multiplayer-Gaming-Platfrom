package ca.ucalgary.seng300.core.registry;

import ca.ucalgary.seng300.shared.models.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerRegistry {

    private static PlayerRegistry instance;

    private final List<Player> players;

    private PlayerRegistry()
    {
        this.players = new ArrayList<>();
    }

    public static PlayerRegistry getInstance()
    {
        if (instance == null)
        {
            instance = new PlayerRegistry();
        }

        return instance;
    }

    public Player findById(String id)
    {
        for (Player player : players)
        {
            if (player.getId().equals(id))
            {
                return player;
            }
        }

        return null;
    }

    public Player findByName(String name)
    {
        for (Player player : players)
        {
            if (player.getName().equals(name))
            {
                return player;
            }
        }

        return null;
    }

    public List<Player> listAll()
    {
        return players;
    }

}
