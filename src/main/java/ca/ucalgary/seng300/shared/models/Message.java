package ca.ucalgary.seng300.shared.models;
import com.github.f4b6a3.uuid.UuidCreator;

public class Message {

    private final String id;
    private final String content;
    private final String sender;

    /**
     * Constructor to create new messages
     * @param content the content to add to the message
     * @param sender the name of the person sending them message
     */
    public Message(String content, String sender)
    {
        // time dated message id for easy chat logging
        this.id = UuidCreator.getTimeOrderedEpoch().toString();
        this.content = content;
        this.sender = sender;
    }

    /**
     * Constructor to create message objects from received strings
     * @param id the id of the message
     * @param content the content of the message
     * @param sender the sender of the message
     */
    public Message(String id, String content, String sender) {
        this.id = id;
        this.content = content;
        this.sender = sender;
    }

    /**
     * Gets the id of the message
     * @return a string containing the time-dated id of the message
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the content of the message
     * @return a string containing the message content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the sender of the message
     * @return a string containing name of sender
     */
    public String getSender() {
        return sender;
    }
}
