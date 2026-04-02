package ca.ucalgary.seng300.core.identity.client;

import ca.ucalgary.seng300.core.registry.ChatRegistry;
import ca.ucalgary.seng300.core.registry.GameRegistry;
import ca.ucalgary.seng300.core.registry.PlayerRegistry;
import ca.ucalgary.seng300.rules.leaderboard.GameType;
import ca.ucalgary.seng300.rules.leaderboard.LeaderboardEntry;
import ca.ucalgary.seng300.rules.leaderboard.MatchRecord;
import ca.ucalgary.seng300.shared.models.Game;
import ca.ucalgary.seng300.shared.models.Message;
import ca.ucalgary.seng300.shared.models.Player;
import ca.ucalgary.seng300.shared.models.Tag;

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
import java.util.Arrays;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;

public class Network extends Thread {
    private static byte[] sharedKey = null;
    private static SecretKey AESKey;
    private static SecureRandom sRan;
    private LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

    // private static final String serverIP ="10.2.1.179";
    // private static final int serverPort = 501;
    private Socket socket;
    private String clientID = null;
    private static Network instance;

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
    public static final byte SEND_MOVE_TTT = 12;

    // to be added/modified later
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

        // CREATE ACCOUNT
        boolean success = (Boolean) Network.getInstance().queueRequest(Network.CREATE_ACCOUNT, new String[]{"username", "password"}).get();

        // LOGIN
        boolean loggedIn = (Boolean) Network.getInstance().queueRequest(Network.LOGIN, new String[]{"username", "password"}).get();

        // LOGOUT
        boolean loggedOut = (Boolean) Network.getInstance().queueRequest(Network.LOGOUT, null).get();

        // GET GAME LIST — no return value, just populates GameRegistry
        Network.getInstance().queueRequest(Network.GET_GAME_LIST, null).get();
        // access games after via GameRegistry.getInstance().ListAll();

        // GET LEADERBOARD takes parameter -> (0 = TTT, 1 = C4, 2 = COMBINED)
        List<LeaderboardEntry> leaderboard = (List<LeaderboardEntry>) Network.getInstance().queueRequest(Network.GET_LEADERBOARD, new String[]{"0"}).get();

        // GET ONLINE PLAYERS — no return value, populates PlayerRegistry
        Network.getInstance().queueRequest(Network.GET_ONLINE_PLAYERS, null).get();

        // SEND TTT MOVE
        Network.getInstance().queueRequest(Network.SEND_MOVE_TTT, new String[]{board.toString()});

        // RECEIVE TTT MOVE

        // GET MATCH RECORDS
        List<MatchRecord> records = (List<MatchRecord>) Network.getInstance().queueRequest(Network.GET_MATCH_RECORD, new String[]{"username"}).get();

        // JOINING QUEUE
        boolean joinedTTT = (Boolean) Network.getInstance().queueRequest(Network.JOIN_TTT_QUEUE, null).get();
        boolean joinedC4  = (Boolean) Network.getInstance().queueRequest(Network.JOIN_C4_QUEUE, null).get();

        // LEAVING QUEUE
        boolean leftTTT = (Boolean) Network.getInstance().queueRequest(Network.LEAVE_TTT_QUEUE, null).get();
        boolean leftC4  = (Boolean) Network.getInstance().queueRequest(Network.LEAVE_C4_QUEUE, null).get();
    */

    /** Constructor
     *
     * @throws Exception
     */
    public Network(String ip, int port) throws Exception {
        socket = new Socket(ip, port);
        establishHandshake();
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

            socket.setSoTimeout(3000);

            while (true) {
                try {

                    int descriptionByte = socket.getInputStream().read();
                    // chats are the only unprompted requests currently...add matchmaking later
                    if (descriptionByte == receive_chat) {
                        receiveMessage();
                    }
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
        String[] parameters = req.getParameters();
        switch (req.getType()) {

            case CREATE_ACCOUNT:
                req.future.complete(registerAccount(parameters[0], parameters[1]));
                break;

            case LOGIN:
                req.future.complete(login(parameters[0], parameters[1]));
                break;

            case LOGOUT:
                req.future.complete(logout());
                break;

            case GET_GAME_LIST:
                getGames();

                req.future.complete(null);
                break;

            case GET_LEADERBOARD:
                req.future.complete(getLeaderboard(Integer.parseInt(parameters[0])));
                break;

            case GET_MATCH_RECORD:
                req.future.complete(getMatchRecords(parameters[0]));
                break;

            case JOIN_TTT_QUEUE:
                req.future.complete(joinQueue(GameType.TICTACTOE));
                break;

            case LEAVE_TTT_QUEUE:
                req.future.complete(leaveQueue(GameType.TICTACTOE));
                break;

            case JOIN_C4_QUEUE:
                req.future.complete(joinQueue(GameType.CONNECT4));
                break;

            case LEAVE_C4_QUEUE:
                req.future.complete(leaveQueue(GameType.CONNECT4));
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

        }

    }

    // LOGIN

    /** sends login request to the server
     *  returns True if successful login
     *
     * @param username
     * @param pwd
     * @return
     * @throws Exception
     */
    public boolean login(String username, String pwd) throws Exception {

        // send description byte
        socket.getOutputStream().write(LOGIN);

        // send parameters
        sendRequestParameter(username);
        sendRequestParameter(pwd);

        // interpret whether the login attempt was successful or not
        return readResponseString().equals("1");
    }

    public boolean logout() throws Exception {
        socket.getOutputStream().write(LOGOUT);

        return readResponseString().equals("1");
    }

    public boolean registerAccount(String username, String password) throws Exception {

        // checking if password meets minimum length requirements
        if (password.length() < 6 || password.length() > 18) {
            return false;
        }

        // send description byte
        socket.getOutputStream().write(CREATE_ACCOUNT);

        // send parameters
        sendRequestParameter(username);
        sendRequestParameter(password);

        // interpret whether registration was successful or not
        return readResponseString().equals("1");
    }

    // GAMES

    /** Sends game_list description byte to server
     *
     * @throws Exception
     */
    public void requestGamesList() throws Exception {
        socket.getOutputStream().write(GET_GAME_LIST);
    }

    /** Parsing of game data to be used to construct game objects
     *
     * Strings received in the format:
     * GameID^Title^Description^Tag1`Color1`Tag2`Color2`...
     * Server sends 1 after all games, or 0 if server error occurs
     *
     * @throws Exception
     */
    public void getGames() throws Exception {

        // send game list request
        requestGamesList();

        // reading two responses (one for each game) and storing in an array of strings
        String[] responses = {
                readResponseString(), readResponseString()
        };

        // either 0 or 1 sent after transmission
        String terminator = readResponseString();

        if (terminator.equals("0")) {
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

            // building tag objects
            List<Tag> tags = new ArrayList<>();

            for (int i = 0; i < tagComponents.length - 1; i += 2) {
                tags.add(new Tag(tagComponents[i], tagComponents[i+1]));
            }

            GameRegistry.getInstance().register(new Game(id, title, description, tags));
        }
    }

    /** Request for joining the queue
     *
     * @param game pass in game type
     * @return
     * @throws Exception
     */
    public boolean joinQueue(GameType game) throws Exception {
        if (game == GameType.TICTACTOE) {
            socket.getOutputStream().write(JOIN_TTT_QUEUE);
        }
        else if (game == GameType.CONNECT4) {
            socket.getOutputStream().write(JOIN_C4_QUEUE);
        }

        return readResponseString().equals("1");
    }

    /** Request for leaving queue
     *
     * @param game pass in game type
     * @return
     * @throws Exception
     */
    public boolean leaveQueue(GameType game) throws Exception {
        if (game == GameType.TICTACTOE) {
            socket.getOutputStream().write(LEAVE_TTT_QUEUE);
        }
        else if (game == GameType.CONNECT4) {
            socket.getOutputStream().write(LEAVE_C4_QUEUE);
        }

        return readResponseString().equals("1");
    }

    // TODO: Update with PlayerRegistry...
    public void getOnlinePlayers() throws Exception {

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

    /** gets leaderboard from database and stores as a nested list (List<List<LeaderboardEntry>>)
     * .0 can be used to access ttt, 1 for c4, 2 for combined
     *
     * @return
     * @throws Exception
     */
    public List<LeaderboardEntry> getLeaderboard(int leaderboardType) throws Exception {

        //send description byte
        socket.getOutputStream().write(GET_LEADERBOARD);
        sendRequestParameter(String.valueOf(leaderboardType));

        List<LeaderboardEntry> entries = new ArrayList<>();

        String response = "";
        boolean receiving = true;

        while (receiving) {

            // receive username as response, or "0"/"1" if end of entries
            response = readResponseString();

            if (response.equals("0") || response.equals("1")) {
                receiving = false;
            }

            else {
                String username = response;

                // data sent as: playerid^wins^matches
                String[] parts = readResponseString().split("\\^");

                int wins = Integer.parseInt(parts[0]);
                int matches = Integer.parseInt(parts[1]);

                // parse string and add to individual lists
                entries.add(new LeaderboardEntry(username, wins, matches));
            }
        }
        // error from server side
        if (response.equals("0")) {
            return null;
        }
        return entries;
    }

    // MATCH RECORD

    /** Request to get the game records of a user
     *
     * @param username
     * @return
     * @throws Exception
     */
    public List<MatchRecord> getMatchRecords(String username) throws Exception {

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
                String[] parts = response.split("\\^");

                int playerOneID = Integer.parseInt((parts[0]));
                int playerTwoID = Integer.parseInt((parts[1]));
                GameType gameType;

                if (parts[2].equals("TICTACTOE")) {
                    gameType = GameType.TICTACTOE;
                } else {
                    gameType = GameType.CONNECT4;
                }

                int winnerID = Integer.parseInt(parts[3]);
                String date = parts[4];

                records.add(new MatchRecord(String.valueOf(playerOneID), String.valueOf(playerTwoID), gameType, String.valueOf(winnerID), date));
            }
        }
        if (response.equals("0")) {
            return null;
        }

        return records;
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

    /** If we receive 6 as a description byte, this method is ran
     *
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