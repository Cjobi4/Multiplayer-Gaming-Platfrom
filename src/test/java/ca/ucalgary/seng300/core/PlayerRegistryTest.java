package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.core.registry.PlayerRegistry;
import ca.ucalgary.seng300.shared.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testPlayerRegistryFindByIdReturnsPlayer() {

    }


    @Test
    void testPlayerRegistryFindByIdReturnsNull() {

    }


    @Test
    void testPlayerRegistryFindByNameReturnsPlayer() {

    }


    @Test
    void testPlayerRegistryFindByNameReturnsNull() {

    }


    @Test
    void testPlayerRegistryListAllReturnsPlayers() {

    }


    @Test
    void testPlayerRegistryListAllReturnsNull() {

    }
}
