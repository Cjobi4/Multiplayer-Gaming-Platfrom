package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.shared.models.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChatRegistryTest {
    private ChatRegistry chatRegistry;
    private static Message[] messages;

    @BeforeEach
    public void setup(){
        chatRegistry = ChatRegistry.getInstance();
        chatRegistry.clearChat();
    }

    @Test
    void testChatRegistryInstanceSuccessfullyCreated() {
        ChatRegistry instance = ChatRegistry.getInstance();

        assertNotNull(instance, "Chat Registry instance should not be null");

    }

    @Test
    void testAddMessage(){
        String sender1 = "sender1";
        String content1 = "player to player communication";

        Message message_sent = new Message(content1, sender1);
        chatRegistry.addMessage(message_sent);
        String expectedMessage = content1;
        String actualMessage = chatRegistry.ListAll().get(0).getContent();

        assertEquals(expectedMessage, actualMessage, "The expected message was: " + expectedMessage + " and the actual message was: " + actualMessage);
    }

    @Test
    void testAddMessageReturnsCorrectMessageForReciever() {
        String sender1 = "sender1";
        String content1 = "player to player communication";

        Message message_sent = new Message(content1, sender1);
        chatRegistry.addMessage(message_sent);
        String expectedMessage = content1;

        String messageId = chatRegistry.ListAll().get(0).getId();
        String actualSender1 = chatRegistry.ListAll().get(0).getSender();
        String actualContent1 = chatRegistry.ListAll().get(0).getContent();

        Message messageReceived = new Message(messageId, actualContent1, actualSender1);
        chatRegistry.addMessage(messageReceived);
        String actualMessageReceived = chatRegistry.ListAll().get(1).getContent();

        assertEquals(expectedMessage, actualMessageReceived, "The expected message was: " + expectedMessage + " and the actual message was: " + actualMessageReceived);
    }
}