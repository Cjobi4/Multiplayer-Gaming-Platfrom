package ca.ucalgary.seng300.core;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.shared.models.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the class of Chat Registry
 */
public class ChatRegistryTest {
    private ChatRegistry chatRegistry;
    private static Message[] messages;

    /**
     * Before every single test we want to have a chat registry instance
     * Before every single test, we want to clear the chat registry
     * This is done as these are expected to be refreshed for each game
     */
    @BeforeEach
    public void setup(){
        chatRegistry = ChatRegistry.getInstance();
        chatRegistry.clearChat();
    }

    /**
     * Testing the ChatRegistry instance
     * The instance should never be null
     * Expected output: an instance of ChatRegistry
     */
    @Test
    void testChatRegistryInstanceSuccessfullyCreated() {
        ChatRegistry instance = ChatRegistry.getInstance();

        assertNotNull(instance, "Chat Registry instance should not be null");
    }

    /**
     * This tests the addMessage method
     * Input: content (which what will be in the chat) and the sender name
     * Expected output: the same content and sender from the input
     * Actual Input: the values stored for content and sender in the chat registry
     */
    @Test
    void testAddMessage(){
        String sender1 = "sender1";
        String content1 = "player to player communication";

        Message message_sent = new Message(content1, sender1);
        chatRegistry.addMessage(message_sent);
        String expectedMessage = content1;
        String expectedSender = sender1;
        String actualMessage = chatRegistry.ListAll().get(0).getContent();
        String actualSender = chatRegistry.ListAll().get(0).getSender();

        assertEquals(expectedMessage, actualMessage, "The expected message was: " + expectedMessage + " and the actual message was: " + actualMessage);
        assertEquals(expectedSender, actualSender, "The expectedSender was: " + expectedSender + " and the actual sender was: " + actualSender);
    }

    /**
     * Testing the second Message constructor (for receiving)
     *
     * Input: content and sender information in the outgoing Message constructor
     * Expected output: same content and sender from the input
     * Actual Output: the content and sender information stored in the chat registry
     * </b> after being processed by the receiving Message constructor
     */
    @Test
    void testAddMessageReturnsCorrectMessageForReceiver() {
        String sender1 = "sender1";
        String content1 = "player to player communication";

        Message messageSent = new Message(content1, sender1);
        chatRegistry.addMessage(messageSent);
        String expectedMessageContent = content1;
        String expectedSender = sender1;

        String messageId = chatRegistry.ListAll().get(0).getId();
        String actualSender1 = chatRegistry.ListAll().get(0).getSender();
        String actualContent1 = chatRegistry.ListAll().get(0).getContent();

        Message messageReceived = new Message(messageId, actualContent1, actualSender1);
        chatRegistry.addMessage(messageReceived);
        String actualMessageReceived = chatRegistry.ListAll().get(1).getContent();
        String actualMessageReceivedSender = chatRegistry.ListAll().get(1).getSender();

        assertEquals(expectedMessageContent, actualMessageReceived, "The expected message was: " + expectedMessageContent + " and the actual message was: " + actualMessageReceived);
        assertEquals(expectedSender, actualMessageReceivedSender, "The expectedSender was: " + expectedSender + " and the actual sender was: " + actualMessageReceivedSender);
    }
}