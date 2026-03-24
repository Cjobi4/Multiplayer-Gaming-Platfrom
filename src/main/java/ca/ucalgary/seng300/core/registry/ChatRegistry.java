package ca.ucalgary.seng300.core.registry;

import ca.ucalgary.seng300.shared.models.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatRegistry {

    private static ChatRegistry instance;

    // the list of chats
    private List<Message> chats;

    // constructor
    private ChatRegistry()
    {
        this.chats = new ArrayList<>();
    }

    /**
     * Gets the current instance of the game registry
     * @return the game registry, if none exists one is made and returned.
     */
    public static ChatRegistry getInstance()
    {
        if (instance == null)
        {
            instance = new ChatRegistry();
        }

        return instance;
    }

    /**
     * Adds messages to the chat instance
     * @param m the message to add
     */
    public void addMessage(Message m)
    {
        chats.add(m);
    }

    /**
     * Lists all the chat messages in the instance
     * @return A list of all chat messages
     */
    public List<Message> ListAll() {return chats;}

    /**
     * Clears the chat history, since its only game-specific. should be called at the end of each game
     */
    public void clearChat()
    {
        this.chats.clear();
    }

}
