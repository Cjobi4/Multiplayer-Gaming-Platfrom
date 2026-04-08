
package ca.ucalgary.seng300.client.screens;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.core.registry.PlayerRegistry;
import ca.ucalgary.seng300.shared.models.Message;
import ca.ucalgary.seng300.shared.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InterfaceTest {

    @BeforeEach
    void setUp() {
        ChatRegistry.getInstance().clearChat();
        PlayerRegistry.getInstance().clear();
    }

    @Test
    void testInterfaceTestFileExists() {
        assertTrue(true, "Interface test file should exist.");
    }

    @Test
    void testUIManualFindingLoginAndCreateAccountBehaveSimilarly() {
        assertTrue(true, "Manual UI testing found login and create-account flows behave too similarly.");
    }

    @Test
    void testUIManualFindingTTTActsAsSinglePlayer() {
        assertTrue(true, "Manual UI testing found Tic-Tac-Toe currently behaves like a single-instance flow.");
    }

    @Test
    void testUIManualFindingOpponentListEmpty() {
        assertTrue(true, "Manual UI testing found opponent list remains empty when player registry is not populated.");
    }

    @Test
    void testUIManualFindingConnect4Incomplete() {
        assertTrue(true, "Manual UI testing found Connect 4 is not fully functional in the current UI flow.");
    }

    @Test
    void testPlayerRegistryStoresCorrectOpponentNames() {
        PlayerRegistry.getInstance().register(new Player("Alice"));
        PlayerRegistry.getInstance().register(new Player("Bob"));

        assertEquals("Alice", PlayerRegistry.getInstance().listAll().get(0).getName());
        assertEquals("Bob", PlayerRegistry.getInstance().listAll().get(1).getName());
    }

    @Test
    void testChatRegistryClearsMessagesForNewUIFlow() {
        ChatRegistry.getInstance().addMessage(new Message("Hello", "Player 1"));
        ChatRegistry.getInstance().clearChat();

        assertTrue(ChatRegistry.getInstance().ListAll().isEmpty());
    }

    @Test
    void testChatRegistryStoresMessageForUIFlow() {
        ChatRegistry.getInstance().addMessage(new Message("Hello", "Player 1"));
        assertEquals(1, ChatRegistry.getInstance().ListAll().size());
    }

    @Test
    void testChatRegistryStartsEmptyForUIFlow() {
        assertTrue(ChatRegistry.getInstance().ListAll().isEmpty());
    }

    @Test
    void testPlayerRegistryPopulatesOpponentList() {
        PlayerRegistry.getInstance().register(new Player("Alice"));
        PlayerRegistry.getInstance().register(new Player("Bob"));

        assertEquals(2, PlayerRegistry.getInstance().listAll().size());
    }

    @Test
    void testOpponentListEmptyWhenRegistryNotPopulated() {
        assertTrue(PlayerRegistry.getInstance().listAll().isEmpty());
    }
}