package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.PlayerRegistry;
import ca.ucalgary.seng300.shared.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test class focuses on testing the methods in the PlayerRegistry class
 */
public class PlayerRegistryTest {
    private PlayerRegistry registry;

    /**
     * setUp calls for the player registry and clears it each time for clear testing
     */
    @BeforeEach
    public void setUp(){
        registry = PlayerRegistry.getInstance();
        registry.listAll().clear();
    }

    /**
     * Testing the getInstance method
     * Input: N/A
     * Expected Output: an existing instance, should never be null even if one didn't exist before
     */
    @Test
    void testPlayerRegistryInstanceReturnsNotNull() {
        PlayerRegistry instance = PlayerRegistry.getInstance();

        assertNotNull(instance, "Player registry should not be null.");
    }

    /**
     * Testing the findByName method, and implicitly testing the getName method
     * Input: "jane doe"
     * Expected Output: testPlayer
     * Actual Output: player found by the player name
     */
    @Test
    void testPlayerRegistryFindByNameReturnsPlayer() {
        Player testPlayer = new Player("jane doe");
        registry.register(testPlayer);
        Player expectedPlayer = testPlayer;

        Player actualPlayerFound = registry.findByName(testPlayer.getName()); // doing it this way to also test if getName works

        assertEquals(expectedPlayer, actualPlayerFound, "Player found by findByName method should be: " + expectedPlayer + ", actual player found is: " + actualPlayerFound);
    }

    /**
     * Testing the findByName method when the player doesn't exist
     * Input: "jane doe"
     * Expected Output: null
     * Actual Output: the value of what has been found based on the given name
     */
    @Test
    void testPlayerRegistryFindByNameReturnsNull() {
        // making sure that the registry isn't empty
        Player testPlayer = new Player("jane doe");
        registry.register(testPlayer);

        Player expectedPlayer = null;
        Player actualPlayerFound = registry.findByName("john doe"); // this is a name that is not registered in the game registry

        assertEquals(expectedPlayer, actualPlayerFound, "Player found by findByName method should be null.");
    }


    /**
     * Testing the findByName method when the player exists but isn't registered
     * Input: "john doe"
     * Expected Output: null
     * Actual Output: the value of what has been found based on the given name
     */
    @Test
    void testPlayerRegistryFindByIdReturnsNullWhenPlayerExistsButNotRegistered() {
        Player testPlayer = new Player("jane doe");
        registry.register(testPlayer);
        Player testPlayer2 = new Player("john doe"); // this player will not be registered into the registry

        Player expectedPlayer = null;
        Player actualPlayerFound = registry.findByName("john doe"); // this is a name that is not registered in the game registry

        assertEquals(expectedPlayer, actualPlayerFound, "Player found by findByName method should be null.");
    }


    /**
     * Testing the listAll method
     * Inputs: testPlayer, testPlayer2, and testPlayer3
     * Expected Output: testPlayer, testPlayer2, and testPlayer3
     * Actual Output: the players stored in the player registry
     */
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

    /**
     * Testing the listAll method when players haven't been registered
     * Inputs: testPlayer, testPlayer2, and testPlayer3
     * Expected Output: empty reistry
     * Actual Output: the players stored in the player registry
     */
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

    /**
     * Testing the unregister method
     * Input: testPlayer
     * Expected Output: removal of testPlayer from the registry
     * Actual Output: the players in the registry
     */
    @Test
    void testUnregisterPlayerSuccessfully() {
        // set the registry to three players
        Player testPlayer = new Player("jane doe");
        registry.register(testPlayer);
        Player testPlayer2 = new Player("mark lee");
        registry.register(testPlayer2);
        Player testPlayer3 = new Player("john suh");
        registry.register(testPlayer3);

        registry.unregister(testPlayer); // removing the first player
        List<Player> expectedPlayers = Arrays.asList(testPlayer2, testPlayer3);
        List<Player> actualPlayers = registry.listAll();

        assertEquals(expectedPlayers, actualPlayers, "Player list should be equal.");
    }
}
