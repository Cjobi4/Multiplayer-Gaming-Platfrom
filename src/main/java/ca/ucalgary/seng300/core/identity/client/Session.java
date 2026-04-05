package ca.ucalgary.seng300.core.identity.client;

import com.sun.net.httpserver.Request;

public abstract class Session {

    private int currentUserID;
    private String username;
    private boolean isLoggedIn;

    public void startSession(int id, String username) {

    }

    public void endSession() {

    }

    public String getUsername() {
        return null;
    }

    public int getUserID() {
        return 0;
    }

    public abstract void addRequest(Request req);

    // overload used in Matchmaker class
    public abstract void addRequest(int code, Object obj);
}
