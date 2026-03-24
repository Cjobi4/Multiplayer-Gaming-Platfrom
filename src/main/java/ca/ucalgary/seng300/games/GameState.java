package main.java.ca.ucalgary.seng300.games;

/**
 * Game State Enumeration
 * <p>
 *     Enum containing all game states. See Game SM Diagram to understand why these states
 *     exist.
 * </p>
 * @author Jonathan Hooi
 * @email jonathan.hooi@ucalgary.ca
 * @version 1.0, 03/19/2026
 * @implNote Game State enums are sourced from the Game SM Diagram from Project Iteration 1
 */
public enum GameState {
    //TurnSelect constants
    TURN_DETERMINE_ACTIVE_PLAYER("TurnSelect; determining active player"),
    TURN_AWAITING_MOVE("TurnSelect; awaiting move from active player"),
    TURN_VALIDATING_MOVE("TurnSelect; validating move received from active player"),
    TURN_APPLY_MOVE("TurnSelect; applying move to game (state update)"),
    TURN_CHECK_END_CONDITIONS("TurnSelect; checking game end conditions"),
    GAME_TERMINATE("TurnSelect; Game termination"),

    //Player Game-Termination constants
    PLAYER_WIN("Player won terminated game"),
    PLAYER_DRAW("Players drew terminated game"),
    PLAYER_LOSE("Player lost terminated game"),

    //Game-Result constants
    GAME_REPORTING_RESULTS("Reporting results from terminated game"),

    //Player Game-Lobby constants
    LOBBY_CREATED("Lobby created for game"),
    LOBBY_AWAITING_PLAYERS("Lobby awaiting minimum amount of players to join"),
    LOBBY_AWAITING_START_SIGNAL("Awaiting player input to start game"),
    GAME_INITIALIZING("Game start signal received; initializing game setup");

    //Field variable to store description text of constants
    private final String description;

    //Constant description Constructor
    private GameState(String description) {
        this.description = description;
    }

    //Getter method for constant description
    public String getDescription() {
        return description;
    }

}
