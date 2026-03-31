package ca.ucalgary.seng300.shared.models;

public class Player {

    private final String id;
    private final String name;


    public Player(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }


}
