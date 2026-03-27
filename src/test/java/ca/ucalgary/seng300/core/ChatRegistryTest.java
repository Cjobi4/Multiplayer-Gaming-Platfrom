package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.shared.models.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatRegistryTest {
    private ChatRegistry chatRegistry;
    private static Message[] messages;

    @BeforeEach
    public void setup(){
        chatRegistry = ChatRegistry.getInstance();
        chatRegistry.clearChat();
    }

    @Test
    void testAddMessage(){
        String sender1 = "sender1";
        String content1 = "player to player communication";

        Message message_sent = new Message(content1, sender1);
        chatRegistry.addMessage(message_sent);
        Message expectedMessage = message_sent;
        Message actualMessage = chatRegistry.ListAll().get(0);

        assertEquals(expectedMessage, actualMessage, "The expected message was: " + expectedMessage + " and the actual message was: " + actualMessage);
    }

    @Test
    void testAddMessageReturnsCorrectMessageForReciever() {
        String sender1 = "sender1";
        String content1 = "player to player communication";

        Message message_sent = new Message(content1, sender1);
        chatRegistry.addMessage(message_sent);
        Message expectedMessage = message_sent;

        String messageId = chatRegistry.ListAll().get(0).getId();
        String actualSender1 = chatRegistry.ListAll().get(0).getSender();
        String actualContent1 = chatRegistry.ListAll().get(0).getContent();

        Message messageReceived = new Message(messageId, actualSender1, actualContent1);
        chatRegistry.addMessage(messageReceived);

        assertEquals(expectedMessage, messageReceived, "The expected message was: " + expectedMessage + " and the actual message was: " + messageReceived);
    }
}