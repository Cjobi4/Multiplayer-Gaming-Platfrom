package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.core.registry.PlayerRegistry;
import ca.ucalgary.seng300.shared.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PlayerRegistryTest {
    private PlayerRegistry registry;

    @BeforeEach
    public void setUp(){
        registry = PlayerRegistry.getInstance();
        registry.listAll().clear();
    }


    @Test
    void testPlayerRegistryInstanceReturnsNotNull() {
        PlayerRegistry instance = PlayerRegistry.getInstance();

        assertNotNull(instance, "Player registry should not be null.");
    }


    @Test
    void testPlayerRegistryFindByNameReturnsPlayer() {
        Player testPlayer = new Player("jane doe");
        registry.register(testPlayer);
        Player expectedPlayer = testPlayer;

        Player actualPlayerFound = registry.findByName(testPlayer.getName());

        assertEquals(expectedPlayer, actualPlayerFound, "Player found by findByName method should be: " + expectedPlayer + ", actual player found is: " + actualPlayerFound);
    }


    @Test
    void testPlayerRegistryFindByNameReturnsNull() {
        // making sure that the registry isn't empty
        Player testPlayer = new Player("jane doe");
        registry.register(testPlayer);

        Player expectedPlayer = null;
        Player actualPlayerFound = registry.findByName("john doe"); // this is a name that is not registered in the game registry

        assertEquals(expectedPlayer, actualPlayerFound, "Player found by findByName method should be null.");
    }


    @Test
    void testPlayerRegistryFindByIdReturnsNullWhenPlayerExistsButNotRegistered() {
        Player testPlayer = new Player("jane doe");
        registry.register(testPlayer);
        Player testPlayer2 = new Player("john doe"); // this player will not be registered into the registry

        Player expectedPlayer = null;
        Player actualPlayerFound = registry.findByName("john doe"); // this is a name that is not registered in the game registry

        assertEquals(expectedPlayer, actualPlayerFound, "Player found by findByName method should be null.");
    }


    @Test
    void testPlayerRegistryListAllReturnsPlayers() {
        Player testPlayer = new Player("jane doe");
        registry.register(testPlayer);
        Player testPlayer2 = new Player("john doe");
        registry.register(testPlayer2);
        Player testPlayer3 = new Player("john suh");
        registry.register(testPlayer3);

        List<Player> expectedPlayers = Arrays.asList(testPlayer, testPlayer2, testPlayer3);
        List<Player> actualPlayers = registry.listAll();

        assertEquals(expectedPlayers, actualPlayers, "Player list should be equal.");
    }


    @Test
    void testPlayerRegistryListAllReturnsNull() {
        // these are players that aren't added to the registry, thus the registry should be empty
        Player testPlayer = new Player("jane doe");
        Player testPlayer2 = new Player("john doe");
        Player testPlayer3 = new Player("john suh");

        List<Player> expectedPlayers = new ArrayList<>(); // empty
        List<Player> actualPlayers = registry.listAll();

        assertEquals(expectedPlayers, actualPlayers, "Player list should be empty.");
    }
}
