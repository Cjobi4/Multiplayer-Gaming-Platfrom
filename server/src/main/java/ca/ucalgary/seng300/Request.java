package ca.ucalgary.seng300;

import java.util.concurrent.CompletableFuture;

/**
 * Class for sending and receiving requests to the client FROM THE SERVER (server initiated transmissions)
 */
public class Request
{
    private CompletableFuture<String> future;
    private int type;
    private String[] parameters;

    //constructor
    public Request(int t, String[] p)
    {
        type = t;
        parameters = p;
    }

    //type getter
    public int getType()
    {
        return type;
    }

    //parameter getter
    public String[] getParameters()
    {
        return parameters;
    }

    //returns the result of the future (must handle exception)
    public String getResult() throws Exception
    {
        return future.get();
    }

    /**
     * Set the result of the future
     * @param res The result to be set
     */
    public void setFuture(String res)
    {
        future.complete(res);
    }
}
