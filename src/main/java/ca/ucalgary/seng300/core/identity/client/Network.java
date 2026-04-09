package ca.ucalgary.seng300.core.identity.client;

import ca.ucalgary.seng300.client.screens.C4opponentSelectController;
import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.core.registry.PlayerRegistry;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.rules.leaderboard.MatchRecord;
import ca.ucalgary.seng300.shared.models.*;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import java.util.Optional;

public class Network extends Thread {
    private static byte[] sharedKey = null;
    private static SecretKey AESKey;
    private static SecureRandom sRan;
    private LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();
    public CompletableFuture<String[]> incomingChallenge = new CompletableFuture<>();

    // private static final String serverIP ="10.2.1.179";
    // private static final int serverPort = 501;
    private Socket socket;
    private String clientID = null;
    private static Network instance;
    private ChallengeListener challengeListener;

    public static final byte PING = 0;
    public static final byte CREATE_ACCOUNT = 1;
    public static final byte LOGIN = 2;
    public static final byte LOGOUT = 3;
    public static final byte GET_GAME_LIST = 4;
    public static final byte GET_LEADERBOARD = 5;
    public static final byte GET_MATCH_RECORD = 6;
    public static final byte JOIN_TTT_QUEUE = 7;
    public static final byte LEAVE_TTT_QUEUE = 8;
    public static final byte JOIN_C4_QUEUE = 9;
    public static final byte LEAVE_C4_QUEUE = 10;
    public static final byte GET_ONLINE_PLAYERS = 11;
    public static final byte MATCH_FOUND = 12;
    public static final byte KICKED_FROM_QUEUE = 13;
    public static final byte MATCH_ACCEPTED = 14;
    public static final byte MATCH_REJECTED = 15;
    //??

    public static final byte RESPOND_QUEUE = 35;
    public static final byte SEND_CHALLENGE = 16;
    public static final byte RECEIVE_CHALLENGE = 17;

    // to be added/modified later
    public static final byte SEND_MOVE_TTT = 125;
    public static final byte send_chat = 126;
    public static final byte receive_chat = 127;

    /*
        HOW TO USE THE NETWORK CLASS

        1. The first screen must initialize the network with the server IP and port:
           Network.getInstance("192.168.1.1", 14001);

        2. All subsequent requests are made through queueRequest().
           queueRequest() takes the type of request as a parameter (see list above), as well as the parameters themselves as a String[]
           Never call network methods directly or create a new Network object.

        3. Results must be cast to the expected return type, use .get() to block until a request is fulfilled

        Here are some examples on how you can request from the server:

        -- CREATE ACCOUNT --

                Returns: 0 = username taken, 1 = success, 2 = invalid input, 3 = other failure
                int result = (Integer) Network.getInstance().queueRequest(Network.CREATE_ACCOUNT, new String[]{"username", "password"}).get();

        -- LOGIN --

                Returns: 1 = success, 0 = failure
                int result = (Integer) Network.getInstance().queueRequest(Network.LOGIN, new String[]{"username", "password"}).get();

        -- LOGOUT --

                Network.getInstance().queueRequest(Network.LOGOUT, null);

        -- GET GAME LIST - has no return value, just populates GameRegistry --

                Network.getInstance().queueRequest(Network.GET_GAME_LIST, null).get();

           access games after via GameRegistry.getInstance().ListAll();

        -- GET LEADERBOARD takes parameter -> ("ttt" for TTT leaderboard, "c4" for C4 leaderboard, or "total" for COMBINED leaderboard) --

         Tic-Tac-Toe:
                Network.getInstance().queueRequest(Network.GET_LEADERBOARD, new String[]{"ttt"});

         Connect 4:
                Network.getInstance().queueRequest(Network.GET_LEADERBOARD, new String[]{"c4"});

         Combined:
                Network.getInstance().queueRequest(Network.GET_LEADERBOARD, new String[]{"total"});

        -- GET ONLINE PLAYERS — no return value, populates PlayerRegistry --

                Network.getInstance().queueRequest(Network.GET_ONLINE_PLAYERS, null).get();

        -- SEND TTT MOVE --

                Network.getInstance().queueRequest(Network.SEND_MOVE_TTT, new String[]{board.toString()});

        * RECEIVE TTT MOVE

        -- GET MATCH RECORDS --

                List<MatchRecord> records = (List<MatchRecord>) Network.getInstance().queueRequest(Network.GET_MATCH_RECORD, new String[]{"username"}).get();

        -- JOINING QUEUE --

          Join TTT Queue:
                boolean joinedTTT = (Boolean) Network.getInstance().queueRequest(Network.JOIN_TTT_QUEUE, null).get();

          Join C4 Queue:
                boolean joinedC4  = (Boolean) Network.getInstance().queueRequest(Network.JOIN_C4_QUEUE, null).get();

        -- RESPONDING TO QUEUE --

          Accept Queue
                int result = (Integer) Network.getInstance().queueRequest(Network.RESPOND_QUEUE, new String[]{"1"}).get();

          Decline Queue
                int result = (Integer) Network.getInstance().queueRequest(Network.RESPOND_QUEUE, new String[]{"0"}).get();

           -- CHALLENGE PLAYER --

           pass in the username and gametype as a string
           int result = (Integer) Network.getInstance().queueRequest(Network.SEND_CHALLENGE, new String[]{"username", "gameType"}).get();

    */

    /** Constructor
     *
     * @throws Exception
     */
    public Network(String ip, int port)
    {
        try
        {
            socket = new Socket(ip, port);
            establishHandshake();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /** Constructor for testing
     * Testing shouldn't require a direct connection
     * @param stubSocket fakes an output from the server
     */
    public Network(Socket stubSocket){
        this.socket = stubSocket;
    }

    /** only called once when user enters the ip and port
     *
     * @param ip
     * @param port
     * @return
     * @throws Exception
     */
    public static Network getInstance(String ip, int port) throws Exception {
        if (instance == null) {
            instance = new Network(ip, port);
            System.out.print("Created new network obj");
            instance.start();
        }
        return instance;
    }

    /** called every other time for method calls
     *
     * @return
     * @throws Exception
     */
    public static Network getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("Network not initialized. Call getInstance(ip, port)");
        }

        return instance;
    }

    /** Internal class to handle requests being processed
     * Created by queueRequest() and processed by run()
     * future holds the result
     */
    class Request {
        CompletableFuture<Object> future = new CompletableFuture<>();
        private int type;
        private String[] parameters;

        Request(int type, String[] parameters) {
            this.type = type;
            this.parameters = parameters;
        }

        public int getType() {
            return type;
        }

        public String[] getParameters() {
            return parameters;
        }
    }

    /**
     *  Runs in background on a separate thread, listens for server-initiated transmissions such as incoming chat messages
     *  Processes client-initiated requests from the request queue
     *  Checks queue every 5 seconds if no server transmission is received
     */
    @Override
    public void run() {
        try {

            while (true) {
                try {
                    socket.setSoTimeout(3000);

                    int descriptionByte = socket.getInputStream().read();
                    // chats are the only unprompted requests currently...add matchmaking later
                    if (descriptionByte == receive_chat) {
                        receiveMessage();
                    }
                    else if (descriptionByte == RECEIVE_CHALLENGE) {
                        receiveChallenge();
                    }
//                    // TODO REMOVE THIS AFTER SERVER SIDE TURNS IMPLEMENTED
//                    else if (descriptionByte == 19) {
//                        System.out.println("Server is waiting for a move... Auto-skipping to unblock server!");
//                        sendRequestParameter("dummy_local_move");
//                    }
                }

                catch (SocketTimeoutException e) {

                    if (!requestQueue.isEmpty()) {
                        Request req = requestQueue.take();
                        processRequest(req);
                    }
                }
            }

        }

        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** To be called whenever a request is made
     *
     * @param type type of request
     * @param parameters parameters for request
     * @return
     * @throws Exception
     */
    public CompletableFuture<Object> queueRequest(int type, String[] parameters) throws Exception {
        Request req = new Request(type, parameters);
        requestQueue.put(req);

        return req.future;
    }


    /**
     * Takes a request and processes it depending on what it is
     * Uses future to process the result so caller can retrieve it
     *
     * Only called from run()
     *
     * @param req
     * @throws Exception
     */
    private void processRequest(Request req) throws Exception {
        socketRequestTimeout();
        String[] parameters = req.getParameters();
        switch (req.getType()) {

            case CREATE_ACCOUNT:
                req.future.complete(registerAccount(parameters[0], parameters[1]));
                break;

            case LOGIN:
                req.future.complete(login(parameters[0], parameters[1]));
                break;

            case LOGOUT:
                logout();
                req.future.complete(null);
                break;

            case GET_GAME_LIST:
                getGames();

                req.future.complete(null);
                break;

            case GET_LEADERBOARD:
                req.future.complete(getLeaderboard(parameters[0]));
                System.out.print("getLeaderboard returrned");
                break;

            case GET_MATCH_RECORD:
                req.future.complete(getMatchRecords(parameters[0]));
                break;

            case JOIN_TTT_QUEUE:
                req.future.complete(joinQueue(GameType.TICTACTOE));
                break;

            /*case LEAVE_TTT_QUEUE:
                req.future.complete(leaveQueue(GameType.TICTACTOE));
                break;*/

            case JOIN_C4_QUEUE:
                req.future.complete(joinQueue(GameType.CONNECT4));
                break;

            /*case LEAVE_C4_QUEUE:
                req.future.complete(leaveQueue(GameType.CONNECT4));
                break;*/

            case RESPOND_QUEUE:
                boolean accept = parameters[0].equals("1");
                req.future.complete(respondQueue(accept));
                break;

            case GET_ONLINE_PLAYERS:
                getOnlinePlayers();
                req.future.complete(null);
                break;

            case SEND_MOVE_TTT:
                sendMoveTTT(parameters[0]);
                req.future.complete(null);
                break;

            case send_chat:
                sendMessage(parameters[0], parameters[1], parameters[2]);
                req.future.complete(null);
                break;

            case SEND_CHALLENGE:
                req.future.complete(sendChallenge(parameters[0], parameters[1]));
                System.out.println("send challenge");
                break;

            case RECEIVE_CHALLENGE:
                boolean acceptChallenge = parameters[0].equals("1");

                if (acceptChallenge) {
                    socket.getOutputStream().write(MATCH_ACCEPTED); // Sends 14
                } else {
                    socket.getOutputStream().write(MATCH_REJECTED); // Sends 15
                }

                // Wait for case 17 to echo the byte back so we know the GameSession is created
                int finalResult = socket.getInputStream().read();
                req.future.complete(finalResult);
                break;


        }

    }

    // LOGIN

    /**
     *  Request for logging in
     *
     *  If registration was successful, return 1
     *  If registration fails due (invalid input or credentials) return 0
     *
     * @param username
     * @param pwd
     * @return
     * @throws Exception
     */
    public int login(String username, String pwd) throws Exception {
        // giving 30s for response
        socketRequestTimeout();
        try {
            // send description byte
            socket.getOutputStream().write(LOGIN);

            // send parameters
            sendRequestParameter(username);
            sendRequestParameter(pwd);

            // interpret whether the login attempt was successful or not
            int response = socket.getInputStream().read();
            if (response == 1) {
                ActivePlayer.getInstance().setUsername(username);
            }
            return response;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Tells server to log out, no return value
     *
     * @throws Exception
     */
    public void logout() throws Exception {
        socket.getOutputStream().write(LOGOUT);
    }

    /**
     * Request for registering an account
     *
     * If username is already taken, return 0
     * If registration was successful, return 1
     * If registration fails due to incorrect credentials (invalid pass length or username contains ` or ^) return 2
     * If other (server failure) return 3
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public int registerAccount(String username, String password) throws Exception {
        socketRequestTimeout();
        try {
            // send description byte
            socket.getOutputStream().write(CREATE_ACCOUNT);

            // send parameters
            sendRequestParameter(username);
            sendRequestParameter(password);

            // interpret whether registration was successful or not
            int response = socket.getInputStream().read();
            if (response == 1) {
                ActivePlayer.getInstance().setUsername(username);
            }

            return response;
        } catch (Exception e) {
            return -1;
        }
    }

    // GAMES

    /** Parsing of game data to be used to construct game objects
     *
     * Strings received in the format:
     * GameID^Title^Description^Tag1`Color1`Tag2`Color2`...
     * Server sends 1 after all games, or 0 if server error occurs
     *
     * @throws Exception
     */
    public void getGames() throws Exception {
        socketRequestTimeout();
        try {
            // send description byte
            socket.getOutputStream().write(GET_GAME_LIST);

            // reading two responses (one for each game) and storing in an array of strings
            String[] responses = {
                    readResponseString(), readResponseString()
            };

            // either 0 or 1 sent after transmission
            int terminator = socket.getInputStream().read();

            if (terminator == 0) {
                // server error
                return;
            }

            // iterating through each array entry (corresponding to a different game) and splitting by ^
            for (String gameInfo : responses) {

                String[] gameFields = gameInfo.split("\\^");

                // parsing the string
                String id = gameFields[0];
                String title = gameFields[1];
                String description = gameFields[2];
                String[] tagComponents = gameFields[3].split("`");
                String fxmlPath = gameFields[4];

                // building tag objects
                List<Tag> tags = new ArrayList<>();

                for (int i = 0; i < tagComponents.length - 1; i += 2) {
                    tags.add(new Tag(tagComponents[i], tagComponents[i + 1]));
                }

                GameRegistry.getInstance().register(new Game(id, title, description, tags, fxmlPath));
            }
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Request for joining the queue
     * Returns the username of the opponent if queue is successful
     *
     * @param game pass in game type
     * @return
     * @throws Exception
     */
    public String joinQueue(GameType game) throws Exception {
        if (game == GameType.TICTACTOE) {
            socket.getOutputStream().write(JOIN_TTT_QUEUE);
        }
        else if (game == GameType.CONNECT4) {
            socket.getOutputStream().write(JOIN_C4_QUEUE);
        }

        // send 1 if queue is joined, 0 if not
        int joinedQueue = socket.getInputStream().read();
        if (joinedQueue == 0) {
            return null;
        }

        // wait 90s to receive a match
        socket.setSoTimeout(90000);
        try {
            // receive 12 if match found
            int desc = socket.getInputStream().read();
            if (desc == MATCH_FOUND) {
                // received receive username
                return readResponseString();
            }
        } catch (SocketTimeoutException e) {
            // 90 sec passed, no queue pop. tell server to leave queue
            leaveQueue(game);
            return null;
        }
        return null;
    }

    /**
     * Request for leaving queue
     * @param game
     * @throws Exception
     */
    public int leaveQueue(GameType game) throws Exception {
        socketRequestTimeout();
        try {
            if (game == GameType.TICTACTOE) {
                socket.getOutputStream().write(LEAVE_TTT_QUEUE);
            } else if (game == GameType.CONNECT4) {
                socket.getOutputStream().write(LEAVE_C4_QUEUE);
            }
            // 1 if left successfully, otherwise 0
            int leaveSuccessful = socket.getInputStream().read();
            return leaveSuccessful;
        } catch (Exception e){
            return -1;
        }
    }

    /**
     * To respond to a queue pop
     * Pass in true to accept, false to decline
     * Returns
     *      13 = kicked from queue (player declined queue)
     *      14 = match accepted
     *      15 = match rejected (opponent declined queue)
     *      -1 default
     *
     * @param response
     * @return
     * @throws Exception
     */
    public int respondQueue(boolean response) throws Exception {
        socketRequestTimeout();
        try {
            if (response) {
                // tell server match accepted
                socket.getOutputStream().write(1);

                // read server response
                int desc = socket.getInputStream().read();
                System.out.print("Server returned: " + desc);

                // other player accepts, return 14
                if (desc == MATCH_ACCEPTED) {
                    return MATCH_ACCEPTED;
                }
                // other player declined match 15
                else if (desc == MATCH_REJECTED) {
                    return MATCH_REJECTED;
                }
            } else {
                socket.getOutputStream().write(0);
                // receive 13 if player declines and is removed from queue
                int exitQueue = socket.getInputStream().read();

                if (exitQueue == KICKED_FROM_QUEUE) {
                    return KICKED_FROM_QUEUE;
                }
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }


    public int sendChallenge(String username, String gameType) throws Exception {
        socketRequestTimeout();
        try {
            socket.getOutputStream().write(SEND_CHALLENGE);

            sendRequestParameter(username);
            sendRequestParameter(gameType);

            return socket.getInputStream().read();
        } catch (Exception e) {
            return -1;
        }
    }

    public interface ChallengeListener {
        void onChallengeReceived(String challengerName, String gameType);
    }


    public void setChallengeListener(ChallengeListener listener) {
        this.challengeListener = listener;
    }

    public void receiveChallenge() throws Exception {
        System.out.println("User Receiving Challenge...");


        String challengerName = readResponseString();
        String gameType = readResponseString();


        if (challengeListener != null) {
            challengeListener.onChallengeReceived(challengerName, gameType);
        } else {

            System.err.println("No UI is listening for challenges! Auto-declining.");
            queueRequest(RECEIVE_CHALLENGE, new String[]{"0"});
        }
    }

    public void getOnlinePlayers() throws Exception {
        socketRequestTimeout();
        try {
            // send description byte
            socket.getOutputStream().write(GET_ONLINE_PLAYERS);

            String response = readResponseString();

            if (response.equals("0")) {
                return;
            }

            PlayerRegistry.getInstance().clear();

            String[] activePlayers = response.split("\\^");

            for (String player : activePlayers) {
                PlayerRegistry.getInstance().register(new Player(player));
            }
        } catch (Exception e) {
            return;
        }
    }

    public void sendMoveTTT(String boardState) throws Exception {
        // send description byte
        socket.getOutputStream().write(SEND_MOVE_TTT);

        // send board
        sendRequestParameter(boardState);
    }

    public String receiveMoveTTT() throws Exception {

        return null;
    }

    // LEADERBOARD

    /** gets leaderboard from database and stores as list (List<LeaderboardEntry>)
     *  parameters: pass in "ttt", "c4", or "total" for their corresponding leaderboards
     *  total is the combined w/l across both games
     *
     * @return
     * @throws Exception
     */
    public List<LeaderboardEntry> getLeaderboard(String leaderboardType) throws Exception {
        socketRequestTimeout();
        try {
            //send description byte
            socket.getOutputStream().write(GET_LEADERBOARD);

            sendRequestParameter(leaderboardType);

            List<LeaderboardEntry> entries = new ArrayList<>();

            String response = "";
            boolean receiving = true;

            while (receiving) {

                // receive username as first response, or "0"/"1" if end of entries
                System.out.println("listening for response");
                response = readResponseString();
                System.out.print("Reponse: " + response);

                if (response.equals("0") || response.equals("1")) {
                    receiving = false;
                } else {
                    String username = response;

                    System.out.println("username: " + username);
                    // data sent as: wins^matches
                    String[] parts = readResponseString().split("\\^");

                    int wins = Integer.parseInt(parts[0]);
                    int matches = Integer.parseInt(parts[1]);

                    System.out.println("Wins: " + wins + " matches: " + matches);
                    // parse string and add to individual lists
                    entries.add(new LeaderboardEntry(username, wins, matches));
                    System.out.print("reached");
                    System.out.println(entries);
                }
            }

            // error from server side
            if (response.equals("0")) {
                return null;
            }
            System.out.println("entry returned");
            return entries;
        } catch (Exception e) {
            System.out.println("exceptioned: " + e);
            return null;
        }
    }

    // MATCH RECORD

    /** Request to get the game records of a user
     *  Pass in username and all game records including the player will be returned in a List<MatchRecord>
     *
     * @param username
     * @return
     * @throws Exception
     */
    public List<MatchRecord> getMatchRecords(String username) throws Exception {
        socketRequestTimeout();
        try {
            //send description byte
            socket.getOutputStream().write(GET_MATCH_RECORD);

            // send username
            sendRequestParameter(username);

            List<MatchRecord> records = new ArrayList<>();
            String response = "";
            boolean receiving = true;

            while (receiving) {

                response = readResponseString();

                if (response.equals("0") || response.equals("1")) {
                    receiving = false;
                } else {

                    // data received as gametype^p1Username^p2Username^winnerName^date

                    String[] parts = response.split("\\^");

                    GameType gameType;

                    if (parts[0].equals("ttt")) {
                        gameType = GameType.TICTACTOE;
                    } else {
                        gameType = GameType.CONNECT4;
                    }

                    String playerOne = parts[1];
                    String playerTwo = parts[2];
                    String winner = parts[3];
                    String date = parts[4];

                    records.add(new MatchRecord(playerOne, playerTwo, gameType, winner, date));
                }
            }
            if (response.equals("0")) {
                return null;
            }
            return records;
        } catch (Exception e) {
            return null;
        }
    }

    // CHAT

    /** Method for sending chats
     *
     * @param id
     * @param content
     * @param sender
     * @throws Exception
     */
    public void sendMessage(String id, String content, String sender) throws Exception {

        // send description byte
        socket.getOutputStream().write(send_chat);

        // send request parameters as byte[]
        sendRequestParameter(id);
        sendRequestParameter(content);
        sendRequestParameter(sender);

        // update local directory
        ChatRegistry.getInstance().addMessage(new Message(id, content, sender));
    }

    /**
     * Chat message received
     * @throws Exception
     */
    public void receiveMessage() throws Exception {

        // interpret each response sent in sequence by server
        String id = readResponseString();
        String content = readResponseString();
        String sender = readResponseString();

        // update the local chat
        ChatRegistry.getInstance().addMessage(new Message(id, content, sender));
    }



    // HELPERS

    /** Method for sending encrypted parameters for request
     *
     * @param value
     * @throws Exception
     */
    private void sendRequestParameter(String value) throws Exception {

        // encrypting parameter
        byte[] encryptedParam = encrypt(value);

        // allocating space for total length of message
        byte[] message = ByteBuffer.allocate(4 + encryptedParam.length)
                // write the encrypted parameter (nonce + ciphertext) length as first 4 bytes
                .putInt(encryptedParam.length)
                // append the encrypted parameter
                .put(encryptedParam)
                .array();

        // send complete message to server
        socket.getOutputStream().write(message);
    }

    private byte[] readResponse() throws Exception {
        // getting first 4 bytes representing message length
        int length = ByteBuffer.wrap(socket.getInputStream().readNBytes(4)).getInt();

        // reading the rest of the message of length ^
        return socket.getInputStream().readNBytes(length);
    }

    private String readResponseString() throws Exception {
        // return string response received
        return decrypt(readResponse());
    }

    private void socketRequestTimeout() throws Exception {
        // 30 seconds for requests
        socket.setSoTimeout(30000);
    }

    private void socketListenTimeout() throws Exception {
        // 3 seconds for passive listening
        socket.setSoTimeout(3000);
    }

    // CRYPTO

    /** Complete handshake sequence to take place at beginning of program
     *
     * @throws Exception
     */
    private void establishHandshake() throws Exception {

        // check for existing clientID, sending clientID or send "new" if no clientID exists
        String send;
        send = Objects.requireNonNullElse(clientID, "new");

        // send the clientID or "new" to the server
        byte[] idBytes = send.getBytes(StandardCharsets.UTF_8);
        socket.getOutputStream().write(ByteBuffer.allocate(4).putInt(idBytes.length).array());
        socket.getOutputStream().write(idBytes);

        // read response from server
        int response = socket.getInputStream().read();

        // 0 = no clientID, perform DH key exchange
        // 1 = has clientID, set key

        if (response == 0) {

            // read server's public key
            int keyLength = ByteBuffer.wrap(socket.getInputStream().readNBytes(4)).getInt();
            byte[] serverPubKey = socket.getInputStream().readNBytes(keyLength);

            // generate shared key
            byte[] clientPubKey = generateSharedKey(serverPubKey);

            // send public key to the server, server uses to generate same shared key
            socket.getOutputStream().write(ByteBuffer.allocate(4).putInt(clientPubKey.length).array());
            socket.getOutputStream().write(clientPubKey);

            // derive AES key
            generateAESKey();

            // save clientID from server
            clientID = decrypt(readResponse());
        }

        // only matters if clientID persistent when restarting program?
        else if (response == 1) {
            // server has clientID and AES key, just initialize client side with existing key
            generateAESKey();
        }
    }

    /** Method for creating the shared secret/key
     *
     * @param serverPubKeyBytes
     * @return
     * @throws Exception
     */
    public static byte[] generateSharedKey(byte[] serverPubKeyBytes) throws Exception {

        // build public key from serverPubKeyBytes
        KeyFactory clientKeyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(serverPubKeyBytes);
        PublicKey serverPublicKey = clientKeyFactory.generatePublic(x509KeySpec);

        // ensuring same dh parameters are used on client and server side (g and m)
        DHParameterSpec dhServerParameterSpec = ((DHPublicKey) serverPublicKey).getParams();

        // making the public/private key pair using server parameters
        KeyPairGenerator clientKeyPairGen = KeyPairGenerator.getInstance("DH");
        clientKeyPairGen.initialize(dhServerParameterSpec);
        KeyPair clientKeyPair = clientKeyPairGen.generateKeyPair();

        // creating key agreement object
        KeyAgreement clientKeyAgreement = KeyAgreement.getInstance("DH");
        clientKeyAgreement.init(clientKeyPair.getPrivate());

        // combining server's key with client's, generating shared key
        clientKeyAgreement.doPhase(serverPublicKey, true);
        sharedKey = clientKeyAgreement.generateSecret();

        return clientKeyPair.getPublic().getEncoded();
    }


    /**
     * Initializes encryption with a dummy key for unit testing.
     * This bypasses the Diffie-Hellman key exchange that requires a live server.
     * @throws Exception if key generation fails.
     */
    public static void setupTestEncryption() throws Exception {
        sharedKey = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        generateAESKey();
    }

    /** Turning the shared key into a usable AES encryption key
     *
     * @throws Exception
     */
    private static void generateAESKey() throws Exception {

        // using shared key to generate hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(sharedKey);

        // use first 16 bytes of hash to make key
        byte[] shortHash = new byte[16];
        System.arraycopy(hash, 0, shortHash, 0, 16);
        AESKey = new SecretKeySpec(shortHash, "AES");

        // create secure rng
        sRan = new SecureRandom();
    }

    /** encryption method
     *  takes some string and returns the encrypted byte array
     *
     * @param plainText
     * @return
     */
    public static byte[] encrypt(String plainText) throws Exception {

        // makes 12 byte long nonce (random value) as per NIST standards
        // important a new one is generated for each encryption
        byte[] nonce = new byte[12];
        sRan.nextBytes(nonce);
        GCMParameterSpec gcmParamSpec = new GCMParameterSpec(128, nonce);

        // creating cipher and setting up encryption algorithm
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, AESKey, gcmParamSpec);

        // encrypting text
        byte[] cipherTextTag = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // combining encrypted text with nonce, copying to beginning of message
        byte[] message = new byte[nonce.length + cipherTextTag.length];
        System.arraycopy(nonce, 0, message, 0, 12);
        System.arraycopy(cipherTextTag, 0, message, 12, cipherTextTag.length);

        return message;
    }

    /** decryption method
     *  takes an encrypted byte array and turns it into a string
     *
     * @param cipherText
     * @return
     */
    public static String decrypt(byte[] cipherText) throws Exception {

        // extracting nonce from cipher text (first 12 bytes)
        byte[] nonce = new byte[12];
        System.arraycopy(cipherText, 0, nonce, 0, 12);

        // copying everything after nonce (encrypted data)
        byte[] cipherTextMessage = new byte[cipherText.length - 12];
        System.arraycopy(cipherText, 12, cipherTextMessage, 0, cipherText.length - 12);

        // recreating gcm parameters, ensuring decryption uses 'same settings' as encryption
        GCMParameterSpec gcmParamSpec = new GCMParameterSpec(128, nonce);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // setting up for decyption
        cipher.init(Cipher.DECRYPT_MODE, AESKey, gcmParamSpec);

        // decrypting
        byte[] plainText = cipher.doFinal(cipherTextMessage);

        return new String(plainText, StandardCharsets.UTF_8);
    }

}