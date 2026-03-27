package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.shared.models.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChatRegistryTest {
    private ChatRegistry chatRegistry;
    private static Message[] messages;

    @BeforeEach
    public void setup(){
        chatRegistry = ChatRegistry.getInstance();
        chatRegistry.ListAll().clear();
    }
}