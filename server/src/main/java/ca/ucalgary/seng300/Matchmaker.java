package ca.ucalgary.seng300;

import ca.ucalgary.seng300.games.TTTServerSession;
import ca.ucalgary.seng300.games.ConnectFourGameSession;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Matchmaker extends Thread
{
    private LinkedBlockingQueue<Session> mQueue = new LinkedBlockingQueue<>();      //matchmaking queue
    private LinkedBlockingQueue<Session> quittersQueue = new LinkedBlockingQueue<>();       //list of players trying to leave
    private String gameName;
    private boolean m = false;


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

                System.out.println("Prio player is: " + prioPlayer.getUsername());

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
                        System.out.println(checkedPlayers.get(i).getUsername() + " being checked.");
                        //if the player's win rate was unable to be retrieved or the difference in skill is acceptable...
                        if (prioPWinRate == -1 ||
                                Math.abs(prioPWinRate - Database.getWinRate(checkedPlayers.get(i).getUsername(), gameName)) <= acceptableSkillDiff + (100 * (double)queueTime / 10000))
                        {
                            System.out.println("matched.");
                            //then match them together
                            result = createMatch(prioPlayer, checkedPlayers.get(i));
                            System.out.println("result is: " + result);

                            //if both players declined...
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
                            } else if (result == 3)     //if both players accepted...
                            {
                                //remove them both from the queue
                                leaveMQueue(checkedPlayers.get(i));
                                matchFound = true;

                                //notify of outcome
                                prioPlayer.addRequest(14, null);
                                checkedPlayers.get(i).addRequest(14, null);

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
                            System.out.println("matched.");
                            //then match them together
                            result = createMatch(prioPlayer, potentialPartner);
                            System.out.println("result is: " + result);

                            //if both players declined...
                            if (result == 0)
                            {
                                //remove them both from the queue
                                leaveMQueue(potentialPartner);
                                matchFound = true;

                                //notify of removal from queue
                                prioPlayer.addRequest(13, null);
                                potentialPartner.addRequest(13, null);
                                break;
                            } else if (result == 1)     //if only the prioPlayer declined
                            {
                                //stop looking for matches and move onto the next prioPlayer to be matched
                                matchFound = true;

                                //notify of outcome
                                prioPlayer.addRequest(13, null);
                                potentialPartner.addRequest(15, null);
                                break;
                            } else if (result == 2)     //if only the possible opponent declined
                            {
                                //remove the possible opponent
                                leaveMQueue(potentialPartner);

                                //notify of outcome
                                prioPlayer.addRequest(15, null);
                                potentialPartner.addRequest(13, null);
                            } else if (result == 3)     //if both players accepted...
                            {
                                //remove them both from the queue
                                leaveMQueue(potentialPartner);
                                matchFound = true;

                                //notify of outcome
                                prioPlayer.addRequest(14, null);
                                potentialPartner.addRequest(14, null);

                                break;
                            }else   //if the match creation failed, treat it like the difference in skill was too great and do nothing
                            {
                                System.out.println(potentialPartner.getUsername() + " added to checked players.");
                                checkedPlayers.add(potentialPartner);
                            }
                        }else   //if the difference is too great, add them to the list of checkedPlayers
                        {
                            System.out.println(potentialPartner.getUsername() + " added to checked players.");
                            checkedPlayers.add(potentialPartner);
                        }
                    }
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
            System.out.println(sesh.getUsername() + " joined the queue");
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
    public int createMatch(Session p1, Session p2)
    {
        try
        {
            //let the players know a match has been found
            Request req1 = new Request(12, new String[]{p2.getUsername()});
            Request req2 = new Request(12, new String[]{p1.getUsername()});

            System.out.println("Req1 is: " + req1);

            p1.addRequest2(req1);
            p2.addRequest2(req2);

            System.out.println("requests added to players");

            //see if they accepted it
            String p1Accept = req1.getResult();
            String p2Accept = req2.getResult();

            System.out.println("p1 said: " + p1Accept);
            System.out.println("p2 said: " + p2Accept);

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

            //if both players accepted, create a new match
            if (gameName.equals("ttt")) {
                TTTServerSession gameSession = new TTTServerSession(p1, p2);
                gameSession.start();
            } else if (gameName.equals("c4")) {
                ConnectFourGameSession gameSession = new ConnectFourGameSession(p1, p2);
                gameSession.start();
            }

            //let server know game created
            return 3;
        } catch (Exception e)   //if something went wrong, notify Matchmaker
        {
            System.out.println("Create match exception: " + e);
            return 4;
        }
    }
}
