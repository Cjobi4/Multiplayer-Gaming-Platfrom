package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.rules.leaderboard.LeaderBoard;
import ca.ucalgary.seng300.shared.models.Game;

import ca.ucalgary.seng300.shared.models.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test class for the Game Registry Class
 */
public class GameRegistryTest {

    private GameRegistry gameRegistry;

    /**
     * This uses a single instance of the game registry
     * Clear the game registry before each test
     * Cleared registry ensures that there are no conflicts
     */
    @BeforeEach
    void setUp() {
        gameRegistry = GameRegistry.getInstance();
        gameRegistry.ListAll().clear();
    }

    /**
     * This test tests to see if a single game can correctly be added to the game registry
     * Input: adding game1 with id of "game1" into the registry
     * Expected Output 1: Game registry should have a size of 1
     * Expected Output 2: ID of the game in the game registry should be "game1"
     * Actual Outputs: Actual size of game registry and ID of the game in the registry
     */
    @Test
    void testRegisterGameAddsSingleGame() {
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));
        Game game1 = new Game("game1", "CONNECT4", "some_description", tags);
        String expectedID = "game1";

        gameRegistry.register(game1);
        List<Game> games = gameRegistry.ListAll();

        int expectedSize = 1;
        int actualSize = games.size();
        String actualID = games.get(0).getId();

        assertEquals(expectedSize, actualSize, "Expected List<Game> games size is: " +expectedSize + ", and was: " + actualSize);
        assertEquals(expectedID, actualID, "Expected ID is: " + expectedID + ", but was: " + actualID);
    }

    /**
     * Testing the method findById()
     * Input: ID of game1, which is set as "game1"
     * Expected Output: game1
     * Actual Output: the game found using the ID "game1"
     */
    @Test
    void testFindByIdReturnsCorrectGameID() {
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));
        Game game1 = new Game("game1", "CONNECT4", "some_description", tags);
        gameRegistry.register(game1);
        Game expectedGame = game1;

        // this (.findById()) is the method that we are testing
        Game actualGame = gameRegistry.findById("game1");

        assertEquals(expectedGame, actualGame, "Expected ID for the game is:" + expectedGame + " and found: " + actualGame);
    }

    /**
     * Testing the method findById() when the ID is not related to a game in the registry
     * Input: ID set as "does_not_exist"
     * Expected output: null
     */
    @Test
    void testFindByIdWhenGameDNE() {
        Game found = gameRegistry.findById("does_not_exist");

        assertNull(found, "No game should have been found.");
    }

    /**
     * This test tests to see if multiple games can correctly be added to the game registry
     * Input: game1, game2, game3
     * Expected Output: size of 3
     * Actual Output: size of actual game registry
     */
    @Test
    void testAddMultipleGameReturnsCorrectRegistrySize() {
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));

        Game game1 = new Game("game1", "CONNECT4", "some_description", tags);
        Game game2 = new Game("game2", "TICTACTOE", "some_description", tags);
        Game game3 = new Game("game3", "UNKNOWN", "some_description", tags);

        gameRegistry.register(game1);
        gameRegistry.register(game2);
        gameRegistry.register(game3);

        int expectedSize = 3;

        assertEquals(expectedSize, gameRegistry.ListAll().size(), "Game Registry size should be equal to expected size of: " + expectedSize);
    }

    /**
     * Testing the getInstance() method
     * Expected Output: an existing instance, should never be null even if one didn't exist before
     */
    @Test
    void testGetInstanceCorrectly() {
        GameRegistry instance = GameRegistry.getInstance();

        assertNotNull(instance, "Game Registry instance should not be null");
    }

    /**
     * Testing if all games are correctly added into the game registry
     * Inputs: game1, game2, game3
     * Expected Outputs: IDs of "game1", "game2", "game3"
     * Actual Outputs: the IDs of the games stored in game registry
     */
    @Test
    void testListAllReturnsAllGamesByIndex() {
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));

        Game game1 = new Game("game1", "CONNECT4", "some_description", tags);
        gameRegistry.register(game1);
        String game1Expected = game1.getId();

        Game game2 = new Game("game2", "TICTACTOE", "some_description", tags);
        gameRegistry.register(game2);
        String game2Expected = game2.getId();

        Game game3 = new Game("game3", "UNKNOWN", "some_description", tags);
        gameRegistry.register(game3);
        String game3Expected = game3.getId();

        List<Game> games = gameRegistry.ListAll();
        int expectedSize = 3;

        assertEquals(expectedSize, games.size(), "Game Registry size should be equal to expected size of: " + expectedSize);
        assertEquals(game1Expected, games.get(0).getId(), "Expected id of index 0 game is 'game1'");
        assertEquals(game2Expected, games.get(1).getId(), "Expected id of index 1 game is 'game2'");
        assertEquals(game3Expected, games.get(2).getId(), "Expected id of index 2 game is 'game3'");
    }

    /**
     * This test tests the getTag() method from the Game Class
     * Input: Creating a Tag list
     * Expected Outputs: label --> "two-player", color --> "RED"
     */
    @Test
    void testGetTagReturnsCorrectTagValues() {
        // initializing the tags and leaderboard
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));

        // create game and add it to the registry
        Game game1 = new Game("game1", "CONNECT4", "some_description", tags);
        gameRegistry.register(game1);

        String expectedLabel = "two-player";
        String expectedColor = "RED";

        Game gameFound = gameRegistry.findById("game1");
        List<Tag> actualTag = gameFound.getTags();
        String actualLabel = actualTag.get(0).getLabel();
        String actualColor = actualTag.get(0).getColor();

        assertEquals(expectedLabel, actualLabel, "Label should be equal to expected label, which is: " + expectedLabel);
        assertEquals(expectedColor, actualColor, "Color should be equal to expected color, which is: " + expectedColor);
    }

    /**
     * This test tests that the description is stored properly
     * Testing the method getDescription from Game Clas
     * Input: store description in the creation of new game
     * Expected Output: input of description
     * Actual Output: the value stored in description
     */
    @Test
    void testGetDescriptionReturnsCorrectGameDescription() {
        // initialization of Tags and Leaderboard
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));

        // create game and add it to the registry
        Game game1 = new Game("game1", "CONNECT4", "some_description", tags);
        gameRegistry.register(game1);

        String expectedDescription = game1.getDescription();

        Game gameFound = gameRegistry.findById("game1");
        String actualDescription = gameFound.getDescription();

        assertEquals(expectedDescription, actualDescription, "Description should be equal to expected description which is: " + expectedDescription);
    }
}