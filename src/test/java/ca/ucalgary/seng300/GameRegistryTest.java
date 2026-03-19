package ca.ucalgary.seng300;

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
        gameRegistry.listAll().clear();
    }

    @Test
    void testRegisterGameAddsSingleGame() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }

    @Test
    void testFindByIdReturnsCorrectGameID() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }

    @Test
    void testFindByIdWhenGameDNE() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }

    @Test
    void testAddMultipleGameReturnsCorrectRegistrySize() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }

    @Test
    void testUnregisterRemovesSingleGame() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }

    @Test
    void testUnregisterRemovesMultipleGames() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }

    @Test
    void testUnregisterFailsToChangeRegistrySizeWhenGameDNE() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }

    @Test
    void testGetInstanceCorrectly() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }

    @Test
    void testListAllReturnsAllGamesByIndex() {
        // 1. Arrange

        // 2. Act

        // 3. Assertions
    }
}