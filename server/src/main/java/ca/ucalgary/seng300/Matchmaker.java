package ca.ucalgary.seng300;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Matchmaker extends Thread
{
    //matchmaking queue
    private LinkedBlockingQueue<Session> mQueue = new LinkedBlockingQueue<>();
    private String gameName;

    //constructor
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

        /// VARIABLES FOR MATCHMAKING ///
        Session prioPlayer;      //the player a match is currently being looked for
        int prioPWinRate;

        Session potentialPartner;
        LinkedList<Session> checkedPlayers = new LinkedList<>();    //players who have been checked at least once already

        boolean matchFound;
        long queueStartTime;
        long queueTime;
        final double acceptableSkillDiff = 10;

        //keep checking to see if a player is waiting in the queue
        while (true)
        {
            try
            {
                //take the player that's been waiting for a match the longest
                if (!checkedPlayers.isEmpty())
                {
                    prioPlayer = checkedPlayers.getFirst();
                    checkedPlayers.removeFirst();
                } else
                {
                    prioPlayer = mQueue.take();
                }

                //get the player's winrate
                prioPWinRate = Database.getWinRate(prioPlayer.getUserID(), gameName);

                //start the timer
                matchFound = false;
                queueStartTime = System.currentTimeMillis();
                queueTime = 0;

                //keep looping until a match is found for the player
                while (true)
                {
                    //check how long the player has been waiting in the queue (use to adjust acceptable skill difference)
                    queueTime = System.currentTimeMillis() - queueStartTime;

                    //first go through all the check players (who have been waiting the longest)...
                    for (int i = 0; i < checkedPlayers.size(); i++)
                    {
                        //if the player's win rate was unable to be retrieved or the difference in skill is acceptable...
                        if (prioPWinRate == -1 ||
                                Math.abs(prioPWinRate - checkedPlayers.get(i).getWinRate()) <= acceptableSkillDiff + (10 * (double)queueTime / 10000))
                        {
                            //then match them together
                            /// match

                            checkedPlayers.remove(i);
                            matchFound = true;
                            break;
                        }

                        //otherwise check the next player in checkedPlayers
                    }

                    //once all the players in checkedPlayers have been checked, and still no match is found, start going through the queue...
                    while (!mQueue.isEmpty() && !matchFound)
                    {
                        //take the player from the queue
                        potentialPartner = mQueue.take();

                        //and compare their win rates
                        if (prioPWinRate == -1 ||
                                Math.abs(prioPWinRate - potentialPartner.getWinRate()) <= acceptableSkillDiff + (10 * (double)queueTime / 10000))
                        {
                            //if the difference is acceptable, match the two

                            matchFound = true;
                            break;
                        }else   //if the difference is too great, add them to the list of checkedPlayers
                        {
                            checkedPlayers.add(potentialPartner);
                        }
                    }

                    /*//start going through the previously checked players
                    if (!checkedPlayers.isEmpty())
                    {
                        //check the next player that was waiting the longest
                        potentialPartner = checkedPlayers.getFirst();
                    }else   //if no previously checked players, go through the queue for players
                    {
                        //check the next player that joins the queue
                        potentialPartner = mQueue.take();
                        checkedPlayers.add(potentialPartner);
                    }

                    //check how long the player has been waiting in the queue (use to adjust acceptable skill difference)
                    queueTime = System.currentTimeMillis() - queueStartTime;

                    //check if the two players are a good match
                    if (prioPWinRate == -1)     //if the player's winrate was unable to be retrieved...
                    {
                        //then match them with the first available player
                        /// match

                        checkedPlayers.removeLast();
                        continue;
                    }else if (Math.abs(prioPWinRate - potentialPartner.getWinrate()) <= acceptableSkillDiff + (10 * (double)queueTime / 10000))    //if they are a good match...
                    {
                        //match them in a game then remove them from the queue

                        checkedPlayers.remove(potentialPartner);
                        break;      //repeat the process with the next player looking for a match
                    }

                    //if the difference in skill level was too high, repeat and check the next player*/
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
