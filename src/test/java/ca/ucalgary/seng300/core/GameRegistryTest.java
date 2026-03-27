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
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));
        LeaderBoard leaderBoard = null;
        Game game1 = new Game("game1", "CONNECT4", "some_description", tags, leaderBoard);
        String expectedID = "game1";

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
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));
        LeaderBoard leaderBoard = null;
        Game game1 = new Game("game1", "CONNECT4", "some_description", tags, leaderBoard);
        gameRegistry.register(game1);
        String expectedID = "game1";

        // 2. act
        Game foundGame = gameRegistry.findById("game1");
        String actualId = foundGame.getId();

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
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));
        LeaderBoard leaderBoard = null;

        Game game1 = new Game("game1", "CONNECT4", "some_description", tags, leaderBoard);
        Game game2 = new Game("game2", "TICTACTOE", "some_description", tags, leaderBoard);
        Game game3 = new Game("game3", "UNKNOWN", "some_description", tags, leaderBoard);

        // 2. Act
        gameRegistry.register(game1);
        gameRegistry.register(game2);
        gameRegistry.register(game3);

        int expectedSize = 3;

        // 3. AssertEquals
        assertEquals(expectedSize, gameRegistry.ListAll().size(), "Game Registry size should be equal to expected size of: " + expectedSize);
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
        List<Tag> tags = Collections.singletonList(new Tag("two-player", "RED"));
        LeaderBoard leaderBoard = null;

        Game game1 = new Game("game1", "CONNECT4", "some_description", tags, leaderBoard);
        gameRegistry.register(game1);
        String game1Expected = game1.getId();

        Game game2 = new Game("game2", "TICTACTOE", "some_description", tags, leaderBoard);
        gameRegistry.register(game2);
        String game2Expected = game2.getId();

        Game game3 = new Game("game3", "UNKNOWN", "some_description", tags, leaderBoard);
        gameRegistry.register(game3);
        String game3Expected = game3.getId();

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