package ca.ucalgary.seng300;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Matchmaker extends Thread
{
    //matchmaking queue
    private LinkedBlockingQueue<Session> mQueue = new LinkedBlockingQueue<>();
    private String gameName;

    private Session prioPlayer;      //the player a match is currently being looked for
    private int prioPWinRate;
    private Session potentialPartner;
    private LinkedList<Session> checkedPlayers = new LinkedList<>();
    private long queueTime;

    //empty constructor
    Matchmaker(String g)
    {
        gameName = g;
    }

    @Override
    public void run()
    {
        //first make sure the matchmaker object was made with a valid game name
        if (!gameName.equals("ttt") && !gameName.equals("c4"))
        {
            /// ////DO SOMETHING HERE///////
            Thread.currentThread().interrupt();
        }

        //keep checking to see if a player is waiting in the queue
        while (true)
        {
            try
            {
                //take the player that's been waiting for a match the longest
                if (checkedPlayers.isEmpty())
                {
                    prioPlayer = mQueue.take();
                } else
                {
                    prioPlayer = checkedPlayers.getFirst();
                }

                //get the player's winrate
                prioPWinRate = Database.getWinRate(prioPlayer.getUserID(), gameName);

                //start the timer
                queueTime = System.currentTimeMillis();

                //start going through the previously checked players
                if (!checkedPlayers.isEmpty())
                {
                    //check the next player that was waiting the longest
                    potentialPartner = mQueue.take();
                }else   //if no previously checked players, go through the queue for players
                {
                    //check the next player that joins the queue
                    potentialPartner = mQueue.take();
                    checkedPlayers.add(potentialPartner);
                }

                //check if the two players are a good match
                if (prioPWinRate == -1)     //if the player's winrate was unable to be retrieved...
                {
                    //then match them with the first available player
                    /// match

                    checkedPlayers.removeLast();
                    continue;
                }



            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Adds a Session to the matchmaking queue for a game.
     * @param sesh The Session trying to join queue.
     * @return True if the queue was joined successfully, false if it failed.
     */
    public boolean joinMQueue(Session sesh)
    {
        //try to add the Session to the queue
        try
        {
            mQueue.put(sesh);
            return true;
        }catch (Exception e) //if it fails let the Session know the queue wasn't joined
        {
            return false;
        }
    }
}
