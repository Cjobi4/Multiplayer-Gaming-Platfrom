package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.shared.models.Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameRegistryTest {

    private GameRegistry gameRegistry;
    private static Game game;

    @BeforeEach
    void setUp() {
        gameRegistry = GameRegistry.getInstance();
        gameRegistry.ListAll().clear();
    }

    @Test
    void testRegisterGameAddsSingleGame() {
        /*
        // 1. Arrange
        Game game1 = new Game();
        should be able to set.id("game1");

        // 2. Act
        gameRegistry.register(game1);
        List<Game> games = gameRegistry.ListAll();

        // 3. AssertEquals
        int expectedSize = 1;
        int actualSize = games.size();
        String expectedID = "game1";
        String actualID = games.get(0).getId();

        assertEquals(expectedSize, actualSize, "Expected List<Game> games size is: " +expectedSize + ", and was: " + actualSize);
        assertEquals(expectedID, expectedSize, "Expected ID is: " + expectedID + ", but was: " + actualID);
         */
    }

    @Test
    void testFindByIdReturnsCorrectGameID() {
        /*
        // 1. arrange
        Game game = new Game("game1");
        should be able to set.id("game1");
        gameRegistry.register(game);

        // 2. act
        Game found = gameRegistry.findById("game1");
        String id = found.getId();
        String expected = "game1";

        // 3. assertions
        assertEquals(expected, id, "Expected ID for the game is:" + expected + " and found: " + id);

         */
    }

    @Test
    void testFindByIdWhenGameDNE() {
        /*
        // 1+ 2. arrange + act
        Game found = gameRegistry.findById("does_not_exist");

        // 3. assertion
        assertNull(found, "No game should have been found.");

         */
    }

    @Test
    void testAddMultipleGameReturnsCorrectRegistrySize() {
        // 1. Arrange
        /*
        Game game1 = new Game();
        should be able to set.id("game1");
        Game game2 = new Game();
        should be able to set.id("game2");
        Game game3 = new Game();
        should be able to set.id("game3");
         */

        // 2. Act
        /*
        gameRegistry.register(game1);
        gameRegistry.register(game2);
        gameRegistry.register(game3);

        int expectedSize = 3;
         */

        // 3. AssertEquals
        /*
        assertEquals(expectedSize, gameRegistry.ListAll().size(), "Game Registry size should be equal to expected size of: " + expectedSize);
         */
    }

    @Test
    void testUnregisterRemovesSingleGame() {
        // 1. Arrange
        /*
        Game game = new Game();
        should be able to set.id("game1");
        gameRegistry.register(game);
         */

        // 2. Act
        /*
        gameRegistry.unregister("game1");
         */

        // 3. assertion
        /*
        assertTrue(gameRegistry.ListAll().isEmpty(), "Game registry should be empty.");
         */
    }

    @Test
    void testUnregisterRemovesMultipleGames() {
        // 1. Arrange
        /*
        Game game1 = new Game("game1");
        should be able to set.id("game1");
        gameRegistry.register(game1);

        Game game2 = new Game("game2");
        should be able to set.id("game2");
        gameRegistry.register(game2);

        Game game3 = new Game("game3");
        should be able to set.id("game3");
        gameRegistry.register(game3);

         */

        // 2. Act
        /*
        gameRegistry.unregister("game2");
        gameRegistry.unregister("game3");
        int expectedSize = 1;

         */

        // 3. AssertEquals
        /*
        assertEquals(expectedSize, gameRegistry.ListAll().size(), "Game registry size should be equal to " + expectedSize + " after removal of 2/3 games.");
         */
    }

    @Test
    void testUnregisterFailsToChangeRegistrySizeWhenGameDNE() {
        // 1. arrange
        /*
        Game game = new Game();
        should be able to set.id("game1");
        gameRegistry.register(game);

         */

        // 2. act
        /*
        gameRegistry.unregister("game2");
        int expectedSize = 1;

         */

        // 3. assertion
        /*
        assertEquals(expectedSize, gameRegistry.ListAll().size(),  "Game registry size should be equal to expected size of: " + expectedSize);

         */
    }

    @Test
    void testGetInstanceCorrectly() {
        // 1. Arrange + Act
        GameRegistry instance = GameRegistry.getInstance();

        // 2. AssertEquals
        assertNotNull(instance, "Game Registry instance should not be null");
    }

    @Test
    void testListAllReturnsAllGamesByIndex() {
        // 1. arrange

        /*
        Game game1 = new Game();
        should be able to set.id("game1");
        gameRegistry.register(game1);

        Game game2 = new Game("game2");
        should be able to set.id("game2");
        gameRegistry.register(game2);

        Game game3 = new Game("game3");
        should be able to set.id("game3");
        gameRegistry.register(game3);
        */

        // 2. act
        /*
        List<Game> games = gameRegistry.ListAll();
        int expectedSize = 3;
        */

        // 3. assertions
        /* assertEquals(expectedSize, games.size(), "Game Registry size should be equal to expected size of: " + expectedSize);
        assertEquals("game1", games.get(0).getId(), "Expected id of index 0 game is 'game1'");
        assertEquals("game2", games.get(1).getId(), "Expected id of index 1 game is 'game2'");
        assertEquals("game3", games.get(2).getId(), "Expected id of index 2 game is 'game3'");
         */
    }
}