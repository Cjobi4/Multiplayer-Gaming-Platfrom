package ca.ucalgary.seng300.shared.models;

public class ActivePlayer {


    private static ActivePlayer instance;


    private String username;


    private ActivePlayer() {
        this.username = null;
    }


    public static ActivePlayer getInstance() {
        if (instance == null) {
            instance = new ActivePlayer();
        }
        return instance;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

}
