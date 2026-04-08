package ca.ucalgary.seng300;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class Database
{
    //database variables
    private static final String url = "jdbc:sqlite:database.db";
    private static Connection conn;
    private static Statement stmt;

    private static Hashtable<String, Session> loggedInUsers = new Hashtable<>();

    //matchmaking threads
    private static Matchmaker tttMatchmaker;
    private static Matchmaker c4Matchmaker;

    /**
     * establishes a connection with the database.db file that contains all the information. If database.db doesn't
     * already exist, a new database.db file will be created. If a connection cannot be established, shut down the
     * server (left as a stub for now).
     */
    private static void connect()
    {
        try
        {
            //connect to the database
            conn = DriverManager.getConnection(url);

            //set this up for later use
            stmt = conn.createStatement();
        } catch (SQLException e)
        {
            //if unable to connect to database, should just shut down the server
            System.out.println(e.getMessage());
            System.out.println("shut down.");
            System.exit(0);
        }
    }

    /**
     * Fully prepares the database for use, should be called at program launch. Sets up a connection with the database
     * using the connect() function, and then seeds database.db with necessary tables and entries. If this fails at any
     * point, the server should shut down
     */
    public static void databaseInitial()
    {
        try
        {
            //connect to the database
            connect(); //need to add a check to shut down if this fails

            //create a tables for data (if they doesn't already exist)
            stmt.execute("CREATE TABLE IF NOT EXISTS userLoginInfo ("
                    + "username TEXT PRIMARY KEY,"
                    + "password TEXT NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS gameInfo ("
                    + "gameid INTEGER PRIMARY KEY,"
                    + "gameData MEMO NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS leaderboard ("
                    + "username TEXT PRIMARY KEY,"
                    + "tttWins INTEGER NOT NULL,"
                    + "tttMatchesPlayed INTEGER NOT NULL,"
                    + "c4Wins INTEGER NOT NULL,"
                    + "c4MatchesPlayed INTEGER NOT NULL,"
                    + "totalWins INTEGER NOT NULL,"
                    + "totalMatchesPlayed INTEGER NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS matchRecord ("
                    + "gameType TEXT PRIMARY KEY,"
                    + "p1Username TEXT NOT NULL,"
                    + "p2Username TEXT NOT NULL,"
                    + "winnerName TEXT NOT NULL,"
                    + "date TEXT NOT NULL);");


            //check if the table(s) are empty (i.e. freshly created)
            ResultSet rs = stmt.executeQuery(  "SELECT COUNT(*) FROM userLoginInfo;");

            rs.next();

            //if they are, add in a default test user account
            if (rs.getInt(1) == 0)
            {
                //create the hashed password
                String hashedPassword = hash("password", "admin");

                //must use prepared statement and not statement bc special characters in hash will disrupt command
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO userLoginInfo(username, password) VALUES(?, ?)");
                pstmt.setString(1, "admin");
                pstmt.setString(2, hashedPassword);
                pstmt.executeUpdate();

                //repeat for second test account
                hashedPassword = hash("password", "test");
                pstmt = conn.prepareStatement("INSERT INTO userLoginInfo(username, password) VALUES(?, ?)");
                pstmt.setString(1, "test");
                pstmt.setString(2, hashedPassword);
                pstmt.executeUpdate();

                //add in a sample gameInfo
                stmt.executeUpdate("INSERT INTO gameInfo(gameid, gameData) VALUES(0, \"Connect Four^Description1^Multiplayer`PINK`Turn Based`PINK^TTT^YES\")");
                stmt.executeUpdate("INSERT INTO gameInfo(gameid, gameData) VALUES(1, \"Tic Tac Toe^Description1^Multiplayer`PINK`Turn Based`PINK^C4^YES\")");
                stmt.executeUpdate("INSERT INTO leaderboard(username, tttWins, c4Wins, tttMatchesPlayed, c4MatchesPlayed, totalWins, totalMatchesPlayed) VALUES(\"admin\", 999, 999, 999, 999, 999, 999)");
                stmt.executeUpdate("INSERT INTO leaderboard(username, tttWins, c4Wins, tttMatchesPlayed, c4MatchesPlayed, totalWins, totalMatchesPlayed) VALUES(\"test\", 0, 0, 0, 0, 0, 0)");
                stmt.executeUpdate("INSERT INTO matchRecord(gameType, p1Username, p2Username, winnerName, date) VALUES(\"gameType\", \"admin\", \"test\", \"Tic-Tac-Toe\", \"date\")");
            }
        } catch (SQLException e)
        {
            //also need to add in a check to shut down server here if something goes wrong
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize and start the Matchmaker threads responsible for matchmaking.
     */
    public static void launchMatchmaking()
    {
        //create the Matchmaking threads
        tttMatchmaker = new Matchmaker("ttt");
        c4Matchmaker = new Matchmaker("c4");

        tttMatchmaker.start();
        c4Matchmaker.start();
    }

    /**
     * Getter for the Tic-tac-toe Matchmaker thread object, tttMatchmaker.
     * @return tttMatchermaker
     */
    public static Matchmaker getTttMatchmaker()
    {
        return tttMatchmaker;
    }

    /**
     * Getter for the Connect-4 Matchmaker thread object, c4Matchmaker.
     * @return c4Matchermaker
     */
    public static Matchmaker getC4Matchmaker()
    {
        return c4Matchmaker;
    }

    /**
     * Given a plaintext string, hashes it with SHA-256 to get a hexadecimal string
     * @param plaintext The string to be hashed
     * @param salt The salt to be added to the plaintext before hashing.
     * @return The hashed version of the string, in hexadecimal. Returns empty string instead if SHA-256 couldn't be found.
     */
    private static String hash(String plaintext, String salt)
    {
        try
        {
            //add the salt to the start of the plaintext before hashing
            plaintext = salt + plaintext;

            //use java's built in MessageDigest class for SHA-256 hash function
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));

            //now must convert hash into string for storage
            //use StringBuilder instead of string for repeated concatenation
            StringBuilder hashString = new StringBuilder();

            //go through each byte in the hash...
            for (byte b: hash)
            {
                //and convert it into a hexadecimal string
                hashString.append(String.format("%02x", b));
            }

            return hashString.toString();
        } catch (NoSuchAlgorithmException e) //if cannot find SHA-256 algorithm, return blank string
        {
            return "";
        }
    }

    /**
     * Given a username and password, creates a new account with those credentials. No login attempt is additionally
     * required, the user is logged in as the account is created.
     * @param username The username for the new account in String format. Must be unique.
     * @param password The password for the new account in String format. Should be not be hashed. MUST be between 6-18
     *                 characters long
     * @param session The Session thread object responsible for this user
     * @return If the account is created successfully, 1 is returned. If the account creation fails because the username
     * has already been taken, return 0. Otherwise, if the account fails return -1.
     */
    public static int createAccount(String username, String password, Session session)
    {
        try
        {
            //check if the username is unique
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM userLoginInfo WHERE (username = ?) LIMIT 1;");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            System.out.println("name is unique");

            //if it wasn't notify user
            if (rs.getInt(1) != 0)
            {
                return 0;
            }

            //if the username was unique...
            //hash the password first
            String hashedPassword = hash(password, username);

            //then add it to the database
            pstmt = conn.prepareStatement("INSERT INTO userLoginInfo(username, password) VALUES(?, ?);");
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();

            //add a leaderboard entry for the user
            //then add it to the database
            pstmt = conn.prepareStatement("INSERT INTO leaderboard(username, tttWins, c4Wins, tttMatchesPlayed, c4MatchesPlayed, totalWins, totalMatchesPlayed) VALUES(?, 0, 0, 0, 0, 0, 0)");
            pstmt.setString(1, username);
            pstmt.executeUpdate();

            System.out.println("added");

            //update list of logged-in users
            loggedInUsers.put(username, session);

            return 1;
        } catch (Exception e) //if something goes wrong, assume invalid input and reject acount creation
        {
            System.out.println(e);
            return -1;
        }
    }

    /**
     * Given a username and password, checks to see if a set of login credentials should be accepted. If a match is
     * found, the user is added to the HashTable loggedInUsers.
     * @param username The username to be tested in String format
     * @param password The password to be tested in String format. Should be not be hashed.
     * @param session The Session thread object responsible for this user
     * @return If a match was found, 1 is returned. Otherwise, if no match is found or the query
     * fails, -1 is returned instead.
     */
    public static int checkLoginCredentials(String username, String password, Session session)
    {
        try
        {
            //make sure someone else hasn't already signed in to the same account yet
            if (loggedInUsers.contains(username))
            {
                return -1;
            }

            //hash the password first
            String hashedPassword = hash(password, username);

            //then check for matches in the database
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM userLoginInfo WHERE (username = ?) AND (password = ?) LIMIT 1;");
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();

            //check results of query, if at a match was found allow access
            if (rs.next())
            {
                //add it to the list of logged-in users
                loggedInUsers.put(username, session);
                System.out.println("Username logged in: " + username);

                return 1;
            }else   //if no match is found, notify of failure
            {
                return -1;
            }
        } catch (Exception e) //if something goes wrong, assume invalid input and reject login
        {
            return -1;
        }
    }

    /**
     * Given a username and a game, returns the user's win rate.
     * @param username The user whose win rate is to be checked.
     * @param game The name of the game for the user's win rate. "ttt" for Tic-tac-toe, and "c4" for Connect-4.
     * @return The win rate of the user, or -1 if it couldn't be retrieved.
     */
    public static int getWinRate(String username, String game)
    {
        try
        {
            //collect the user's leaderboard entry
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM leaderboard WHERE (username = ?);");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            //move the pointer up
            rs.next();

            //return the winrate
            if (rs.getInt(game + "MatchesPlayed") == 0)
            {
                return -1;
            }

            return rs.getInt(game + "Wins") / rs.getInt(game + "MatchesPlayed") * 100;
        } catch (SQLException e)
        {
            return -1;
        }
    }

    /**
     * Logs out a user.
     * @param username The username of the user to be logged out
     */
    public static void logOut(String username)
    {
        //remove the user from the list of logged-in users
        loggedInUsers.remove(username);
    }

    /**
     * Gets the game info for the client from the Database.db tables.
     * @return A ResultSet containing all the game infos. If something goes wrong with the query, return null instead.
     */
    public static ResultSet getAllGames()
    {
        try
        {
            //collect all the info on the games
            return stmt.executeQuery("SELECT * FROM gameInfo;");
        } catch (SQLException e)
        {
            return null;
        }
    }

    /**
     * Gets all the information in the leaderboard from the Database.db tables.
     * @return A ResultSet containing all the leaderboard data. If something goes wrong with the query, return null
     * instead.
     */
    public static ResultSet getAllLeaderboardEntries(String game)
    {
        try
        {
            //collect all the leaderboard entries
            return stmt.executeQuery("SELECT * FROM leaderboard ORDER BY " + game + "Wins DESC;");
        } catch (SQLException e)
        {
            System.out.println("Leaderboard query failed");
            return null;
        }
    }

    /**
     * Gets all match records from the specified user in Database.db tables.
     * @return A ResultSet containing the match records. If something goes wrong with the query, return null instead.
     */
    public static ResultSet getMatchRecord(String username)
    {
        try
        {
            //collect all the leaderboard entries with matching usernames
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM matchRecord WHERE p1Username = ? OR p2Username = ? LIMIT 10;");
            pstmt.setString(1, username);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs;
        } catch (SQLException e)
        {
            return null;
        }
    }

    /**
     * Returns a list of logged-in users, formatted for transmission.
     * @return A list of logged-in users' usernames, with ^ separating each user.
     */
    public static String getLoggedInUsers()
    {
        StringBuilder sbuild = new StringBuilder();

        //get a list of all the keys
        Enumeration<String> keys = loggedInUsers.keys();
        String key;

        //go through all the logged-in users
        while (keys.hasMoreElements())
        {
            key = keys.nextElement();

            //add the user's username
            sbuild.append(key);
            sbuild.append("^");
        }

        //remove the trailing ^ symbol
        sbuild.deleteCharAt(sbuild.length());

        return sbuild.toString();
    }

    /**
     * Given a username, returns the associated Session object
     * @param username
     * @return
     */
    public static Session getSession(String username)
    {
        return loggedInUsers.get(username);
    }

    /**
     * Given the results of a match, update the leaderboard entries and add it to the list of match records.
     * @param playerOne The username of the first player from the game in String format.
     * @param playerTwo The username of the second player from the game in String format.
     * @param winner The username of the player that won in String format.
     * @param date The date the match occurred in String format.
     * @param game The game type that was played in String format. "ttt" for Tic-tac-toe, "c4" for Connect 4.
     */
    public static synchronized void addMatchResult(String playerOne, String playerTwo, String winner, String date, String game)
    {
        //if there was no winner don't count the match
        if (winner != null)
        {
            try
            {
                //add it to the match record
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO matchRecord(gameType, p1Username, p2Username, winnerName, date) VALUES(?, ?, ?, ?, ?)");
                pstmt.setString(1, game);
                pstmt.setString(2, playerOne);
                pstmt.setString(3, playerTwo);
                pstmt.setString(4, winner);
                pstmt.setString(5, date);
                pstmt.execute();

                String loser;

                //find the username of the loser
                if (winner.equals(playerOne))
                {
                    loser = playerTwo;
                }else
                {
                    loser = playerOne;
                }

                //update the leaderboard entries
                stmt.execute("UPDATE leaderboard SET " + game + "Wins = " + game + "Wins + 1, " +
                        game + "MatchesPlayed = " + game + "MatchesPlayed + 1 WHERE username = " + winner + ";");
                stmt.execute("UPDATE leaderboard SET " + game + "Wins = " + game + "Wins - 1, " +
                        game + "MatchesPlayed = " + game + "MatchesPlayed + 1 WHERE username = " + loser + ";");
            } catch (Exception e)   //if the match result couldn't be saved...
            {
                System.out.println("Match results not saved.");
                throw new RuntimeException(e);
            }
        }
    }
}
