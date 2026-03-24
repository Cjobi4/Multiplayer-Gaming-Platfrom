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
        // 1. Arrange
        Game game1 = new Game();
        String expectedID = game1.setId("game1");

        // 2. Act
        gameRegistry.register(game1);
        List<Game> games = gameRegistry.ListAll();

        // 3. AssertEquals
        int expectedSize = 1;
        int actualSize = games.size();
        String actualID = games.get(0).getId();

        assertEquals(expectedSize, actualSize, "Expected List<Game> games size is: " +expectedSize + ", and was: " + actualSize);
        assertEquals(expectedID, actualID, "Expected ID is: " + expectedID + ", but was: " + actualID);
    }

    @Test
    void testFindByIdReturnsCorrectGameID() {

        // 1. arrange
        Game game1 = new Game();
        String expectedID = game1.setId("game1");
        gameRegistry.register(game1);

        // 2. act
        Game found = gameRegistry.findById("game1");
        String actualId = found.getId();

        // 3. assertions
        assertEquals(expectedID, actualId, "Expected ID for the game is:" + expectedID + " and found: " + actualId);

    }

    @Test
    void testFindByIdWhenGameDNE() {
        // 1+ 2. arrange + act
        Game found = gameRegistry.findById("does_not_exist");

        // 3. assertion
        assertNull(found, "No game should have been found.");
    }

    @Test
    void testAddMultipleGameReturnsCorrectRegistrySize() {
        // 1. Arrange
        Game game1 = new Game();
        game1.setId("game1");

        Game game2 = new Game();
        game2.setId("game2");

        Game game3 = new Game();
        game3.setId("game3");

        // 2. Act
        gameRegistry.register(game1);
        gameRegistry.register(game2);
        gameRegistry.register(game3);

        int expectedSize = 3;

        // 3. AssertEquals
        assertEquals(expectedSize, gameRegistry.ListAll().size(), "Game Registry size should be equal to expected size of: " + expectedSize);
    }

    @Test
    void testUnregisterRemovesSingleGame() {
        // 1. Arrange
        Game game1 = new Game();
        game1.setId("game1");
        gameRegistry.register(game1);

        // 2. Act
        gameRegistry.unregister("game1");

        // 3. assertion
        assertTrue(gameRegistry.ListAll().isEmpty(), "Game registry should be empty.");
    }

    @Test
    void testUnregisterRemovesMultipleGames() {
        // 1. Arrange
        Game game1 = new Game();
        game1.setId("game1");
        gameRegistry.register(game1);

        Game game2 = new Game();
        game2.setId("game2");
        gameRegistry.register(game2);

        Game game3 = new Game();
        game3.setId("game3");
        gameRegistry.register(game3);

        // 2. Act
        gameRegistry.unregister("game2");
        gameRegistry.unregister("game3");
        int expectedSize = 1;

        // 3. AssertEquals
        assertEquals(expectedSize, gameRegistry.ListAll().size(), "Game registry size should be equal to " + expectedSize + " after removal of 2/3 games.");
    }

    @Test
    void testUnregisterFailsToChangeRegistrySizeWhenGameDNE() {
        // 1. arrange
        Game game1 = new Game();
        game1.setId("game1");
        gameRegistry.register(game1);

        // 2. act
        gameRegistry.unregister("game2");
        int expectedSize = 1;

        // 3. assertion
        assertEquals(expectedSize, gameRegistry.ListAll().size(), "Game registry size should be equal to expected size of: " + expectedSize);
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
        Game game1 = new Game();
        String game1Expected = game1.setId("game1");
        gameRegistry.register(game1);

        Game game2 = new Game();
        String game2Expected = game2.setId("game2");
        gameRegistry.register(game2);

        Game game3 = new Game();
        String game3Expected = game3.setId("game3");
        gameRegistry.register(game3);

        // 2. act
        List<Game> games = gameRegistry.ListAll();
        int expectedSize = 3;

        // 3. assertions
        assertEquals(expectedSize, games.size(), "Game Registry size should be equal to expected size of: " + expectedSize);
        assertEquals(game1Expected, games.get(0).getId(), "Expected id of index 0 game is 'game1'");
        assertEquals(game2Expected, games.get(1).getId(), "Expected id of index 1 game is 'game2'");
        assertEquals(game3Expected, games.get(2).getId(), "Expected id of index 2 game is 'game3'");
    }
}