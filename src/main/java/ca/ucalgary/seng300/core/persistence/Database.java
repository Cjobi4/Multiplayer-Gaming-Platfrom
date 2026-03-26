package ca.ucalgary.seng300.core.persistence;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Database
{
    private static final String url = "jdbc:sqlite:database.db";

    private static Connection conn;
    private static Statement stmt;
    private static String salt = "salt";

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

            //create a table to hold user login info (if it doesn't already exist)
            stmt.execute("CREATE TABLE IF NOT EXISTS userLoginInfo ("
                    + "username TEXT PRIMARY KEY,"
                    + "password TEXT NOT NULL);");


            //check if the table(s) are empty (i.e. freshly created)
            ResultSet rs = stmt.executeQuery(  "SELECT COUNT(*) FROM userLoginInfo;");

            rs.next();

            //if they are, add in a default test user account
            if (rs.getInt(1) == 0)
            {
                //create the hashed password
                String hashedPassword = hash("password");
                stmt.execute("INSERT INTO userLoginInfo(username, password) VALUES (user, " + hashedPassword + ")");
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
}
