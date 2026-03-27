package ca.ucalgary.seng300;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Hashtable;

public class Database
{
    private static final String url = "jdbc:sqlite:database.db";

    private static Connection conn;
    private static Statement stmt;
    private static final String salt = "salt";
    private static Hashtable<Integer, Thread> loggedInUsers = new Hashtable<>();
    private static int nextAvailID;     //the next available userID to be assigned

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
                    + "userid INTEGER PRIMARY KEY,"
                    + "username TEXT NOT NULL,"
                    + "password TEXT NOT NULL);");

            /*stmt.execute("CREATE TABLE IF NOT EXISTS gameInfo ("
                    + "gameid INTEGER PRIMARY KEY,"
                    + "title TEXT NOT NULL,"
                    + "description MEMO NOT NULL"
                    + "label TEXT NOT NULL,"
                    + "color TEXT NOT NULL"
                    + "gameURL TEXT NOT NULL,"
                    + "fullscreen YES/NO NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS games ("
                    + "gameid INTEGER PRIMARY KEY,"
                    + "title TEXT NOT NULL,"
                    + "description MEMO NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS tags ("
                    + "gameid INTEGER PRIMARY KEY,"
                    + "label TEXT NOT NULL,"
                    + "color TEXT NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS launchConfigs ("
                    + "gameid INTEGER PRIMARY KEY,"
                    + "gameURL TEXT NOT NULL,"
                    + "fullscreen YES/NO NOT NULL);");*/

            stmt.execute("CREATE TABLE IF NOT EXISTS gameInfo ("
                            + "gameid INTEGER PRIMARY KEY,"
                            + "gameData MEMO NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS leaderboard ("
                    + "userid INTEGER PRIMARY KEY,"
                    + "username TEXT NOT NULL,"
                    + "tttWins INTEGER NOT NULL,"
                    + "c4Wins INTEGER NOT NULL,"
                    + "tttMatchesPlayed INTEGER NOT NULL,"
                    + "c4MatchesPlayed INTEGER NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS matchRecord ("
                    + "gameid INTEGER PRIMARY KEY,"
                    + "p1Userid INTEGER NOT NULL,"
                    + "p2Userid INTEGER NOT NULL,"
                    + "gametype TEXT NOT NULL,"
                    + "winnerID INTEGER NOT NULL,"
                    + "date TEXT NOT NULL);");


            //check if the table(s) are empty (i.e. freshly created)
            ResultSet rs = stmt.executeQuery(  "SELECT COUNT(*) FROM userLoginInfo;");

            rs.next();

            //if they are, add in a default test user account
            if (rs.getInt(1) == 0)
            {
                //create the hashed password
                String hashedPassword = hash("password");

                //must use prepared statement and not statement bc special characters in hash will disrupt command
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO userLoginInfo(userid, username, password) VALUES(0, ?, ?)");
                pstmt.setString(1, "admin");
                pstmt.setString(2, hashedPassword);
                pstmt.executeUpdate();

                //update the next available userid
                nextAvailID = 1;

                //add in a sample gameInfo
                stmt.execute("INSERT INTO gameInfo(gameid, gameData) VALUES(0, Connect Four^Description1^Multiplayer`Turn Based^PINK^gameURL^YES)");
                stmt.execute("INSERT INTO gameInfo(gameid, gameData) VALUES(0, Tic Tac Toe^Description2^Multiplayer`Turn Based^PINK^gameURL^YES)");
                stmt.execute("INSERT INTO leaderboard(userid, username, tttWins, c4Wins, tttMatchesPlayed, c4MatchesPlayed) VALUES(0, admin, 999, 999, 999, 999)");
                stmt.execute("INSERT INTO matchRecord(gameid, p1Userid, p2Userid, gametype, winnerID, date) VALUES(gameid, 0, p2Userid, Tic-Tac-Toe, 0, date)");
            }else   //if the table is not empty...
            {
                //set the next available userid
                nextAvailID = rs.getInt(1);
            }

        } catch (SQLException e)
        {
            //also need to add in a check to shut down server here if something goes wrong
            throw new RuntimeException(e);
        }
    }

    /**
     * Given a plaintext string, hashes it with SHA-256 to get a hexadecimal string
     * @param plaintext The string to be hashed
     * @return The hashed version of the string, in hexidecimal. Returns empty string instead if SHA-256 couldn't be found.
     */
    private static String hash(String plaintext)
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
     * @param username The username for the new account in String format
     * @param password The password for the new account in String format. Should be not be hashed. MUST be between 6-18
     *                 characters long
     * @param session The Session thread object responsible for this user
     * @return If the account is created successfully, the newly assigned userid is returned. Otherwise, if the account
     * creation fails, -1 is returned.
     */
    public static int createAccount(String username, String password, Thread session)
    {
        try
        {
            //hash the password first
            String hashedPassword = hash(password);

            //then add it to the database
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO userLoginInfo(userid, username, password) VALUES(?, ?, ?)");
            pstmt.setString(1, Integer.toString(nextAvailID));
            pstmt.setString(2, username);
            pstmt.setString(3, hashedPassword);
            pstmt.executeUpdate();

            //update list of logged-in users
            loggedInUsers.put(nextAvailID, session);
            nextAvailID++;

            return nextAvailID - 1;
        } catch (Exception e) //if something goes wrong, assume invalid input and reject acount creation
        {
            return -1;
        }
    }

    /**
     * Given a username and password, checks to see if a set of login credentials should be accepted. If a match is
     * found, the user is added to the HashTable loggedInUsers and the userid is returned.
     * @param username The username to be tested in String format
     * @param password The password to be tested in String format. Should be not be hashed.
     * @param session The Session thread object responsible for this user
     * @return If a match was found, the corresponding userid is returned. Otherwise, if no match is found or the query
     * fails, -1 is returned instead.
     */
    public static int checkLoginCredentials(String username, String password, Thread session)
    {
        try
        {
            //hash the password first
            String hashedPassword = hash(password);

            //then check for matches in the database
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM userLoginInfo WHERE (username = ?) AND (password = ?) LIMIT 1;");
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();

            //check results of query, if at a match was found allow access
            if (rs.next())
            {
                //add it to the list of logged-in users
                loggedInUsers.put(rs.getInt("userid"), session);

                return rs.getInt("userid");
            }else
            {
                return -1;
            }
        } catch (Exception e) //if something goes wrong, assume invalid input and reject login
        {
            return -1;
        }
    }

    /**
     * Logs out a user.
     * @param userID The userID of the user to be logged out
     */
    public static void logOut(int userID)
    {
        //remove the user from the list of logged-in users
        loggedInUsers.remove(userID);
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM games;");

            return rs;
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
    public static ResultSet getAllLeaderboardEntries()
    {
        try
        {
            //collect all the leaderboard entries
            ResultSet rs = stmt.executeQuery("SELECT * FROM leaderboard;");

            return rs;
        } catch (SQLException e)
        {
            return null;
        }
    }

    /**
     * Gets all match records from the specified userid in Database.db tables.
     * @return A ResultSet containing the match records. If something goes wrong with the query, return null instead.
     */
    public static ResultSet getMatchRecord(int userid)
    {
        try
        {
            //collect all the leaderboard entries with matching userids
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM matchRecord WHERE userid = ?;");
            pstmt.setString(1, String.valueOf(userid));
            ResultSet rs = pstmt.executeQuery();

            return rs;
        } catch (SQLException e)
        {
            return null;
        }
    }
}
