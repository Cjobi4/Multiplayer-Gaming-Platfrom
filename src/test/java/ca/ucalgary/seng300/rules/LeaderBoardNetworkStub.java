package ca.ucalgary.seng300.rules;

import ca.ucalgary.seng300.core.identity.client.Network;

import java.util.concurrent.CompletableFuture;

public class LeaderBoardNetworkStub extends Network {
    private Object response;

    public LeaderBoardNetworkStub() {
        super("localHost", 0);  // just a dummy host, don't want to open a socket
    }

    public void setResponse(Object response){
        this.response = response;
    }

    @Override
    public CompletableFuture<Object> queueRequest(int type, String[] parameters) throws Exception {
        return CompletableFuture.completedFuture(response);
    }

}
