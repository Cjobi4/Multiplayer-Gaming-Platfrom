package ca.ucalgary.seng300;

import ca.ucalgary.seng300.games.TTTServerSession;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Matchmaker extends Thread
{
    private LinkedBlockingQueue<Session> mQueue = new LinkedBlockingQueue<>();      //matchmaking queue
    private LinkedBlockingQueue<Session> quittersQueue = new LinkedBlockingQueue<>();       //list of players trying to leave
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

        Session quitter;

        boolean matchFound;
        long queueStartTime;
        long queueTime;
        final double acceptableSkillDiff = 10;
        int result;

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

                //get the player's win rate
                prioPWinRate = Database.getWinRate(prioPlayer.getUsername(), gameName);

                //start the timer
                matchFound = false;
                queueStartTime = System.currentTimeMillis();
                queueTime = 0;

                //keep looping until a match is found for the player
                while (!matchFound)
                {
                    //before each round of checks, first make sure that any quitters are removed
                    if (!quittersQueue.isEmpty())
                    {
                        quitter = quittersQueue.take();

                        //if the prio player was the quitter...
                        if (quitter.equals(prioPlayer))
                        {
                            //restart the process with a new prio player
                            System.out.println("Prio player removed.");     //for debug
                            break;
                        }else
                        {
                            //try to remove them from the list of checkPlayers first
                            if (!checkedPlayers.remove(quitter))
                            {
                                //if they weren't in there, remove them from the queue
                                mQueue.remove(quitter);
                            }
                        }
                    }

                    //check how long the player has been waiting in the queue (use to adjust acceptable skill difference)
                    queueTime = System.currentTimeMillis() - queueStartTime;

                    //first go through all the checked players (who have been waiting the longest)...
                    for (int i = 0; i < checkedPlayers.size(); i++)
                    {
                        //if the player's win rate was unable to be retrieved or the difference in skill is acceptable...
                        if (prioPWinRate == -1 ||
                                Math.abs(prioPWinRate - Database.getWinRate(checkedPlayers.get(i).getUsername(), gameName)) <= acceptableSkillDiff + (10 * (double)queueTime / 10000))
                        {
                            //then match them together
                            result = createMatch(prioPlayer, checkedPlayers.get(i));

                            //if both players declined or both accepted...
                            if (result == 0)
                            {
                                //remove them both from the queue
                                leaveMQueue(checkedPlayers.get(i));
                                matchFound = true;

                                //notify of removal from queue
                                prioPlayer.addRequest(13, null);
                                checkedPlayers.get(i).addRequest(13, null);
                                break;
                            } else if (result == 1)     //if only the prioPlayer declined
                            {
                                //stop looking for matches and move onto the next prioPlayer to be matched
                                matchFound = true;

                                //notify of outcome
                                prioPlayer.addRequest(13, null);
                                checkedPlayers.get(i).addRequest(15, null);
                                break;
                            } else if (result == 2)     //if only the possible opponent declined
                            {
                                //remove the possible opponent
                                leaveMQueue(checkedPlayers.get(i));

                                //notify of outcome
                                prioPlayer.addRequest(15, null);
                                checkedPlayers.get(i).addRequest(13, null);
                            } else if (result == 3)
                            {
                                //remove them both from the queue
                                leaveMQueue(checkedPlayers.get(i));
                                matchFound = true;

                                //notify of outcome
                                prioPlayer.addRequest(14, null);
                                checkedPlayers.get(i).addRequest(14, null);


                            //

                                break;
                            }
                            //if the match creation failed, treat it like the difference in skill was too great and do nothing
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
                                Math.abs(prioPWinRate - Database.getWinRate(potentialPartner.getUsername(), gameName)) <= acceptableSkillDiff + (10 * (double)queueTime / 10000))
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
        //try to add the Session to the matchmaking queue
        try
        {
            mQueue.put(sesh);
            return true;
        }catch (Exception e) //if it fails let the Session know the queue wasn't joined
        {
            return false;
        }
    }

    /**
     * Removes a Session from the matchmaking queue.
     * @param sesh The Session to be removed.
     * @return True if the Session was removed successfully, false if it failed.
     */
    public boolean leaveMQueue(Session sesh)
    {
        //try to add the Session to the quitter's queue
        try
        {
            quittersQueue.put(sesh);
            return true;
        }catch (Exception e) //if it fails let the Session know the quitter's queue wasn't joined
        {
            return false;
        }
    }

    /**
     * Given two users with acceptable differences in win rate, notify them of the match and see if they accept. If they
     * do, create a new game for them. Otherwise, notify the matchmaker.
     * @param p1 The first Session object (user) to be matched.
     * @param p2 The second Session object (user) to be matched.
     * @return Returns 0 if both players decline, 1 if only p1 declines, 2 if only p2 declines, 3 if it succeeds, and 4
     * if an exception is thrown.
     */
    private int createMatch(Session p1, Session p2)
    {
        try
        {
            //let the players know a match has been found
            Request req1 = new Request(12, new String[]{p2.getUsername()});
            Request req2 = new Request(12, new String[]{p1.getUsername()});

            p1.addRequest(req1);
            p2.addRequest(req2);

            //see if they accepted it
            String p1Accept = req1.getResult();
            String p2Accept = req2.getResult();

            //if anyone rejected it, let the server know who
            if (p1Accept.equals("0") && p2Accept.equals("0"))
            {
                return 0;
            }else if (p1Accept.equals("0"))
            {
                return 1;
            }else if (p2Accept.equals("0"))
            {
                return 2;
            }

            //if no one rejected it (both players accepted), create a new match for them
            /// make game
            TTTServerSession gameSession = new TTTServerSession(p1, p2);
            gameSession.start();



            return 3;
        } catch (Exception e)   //if something went wrong, notify Matchmaker
        {
            return 4;
        }
    }
}
