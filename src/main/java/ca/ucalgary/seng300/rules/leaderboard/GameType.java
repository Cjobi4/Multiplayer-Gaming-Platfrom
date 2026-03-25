package ca.ucalgary.seng300.rules.leaderboard;


public enum GameType {
    TICTACTOE ("Tic-Tac-Toe"), CONNECT4 ("Connect 4");
    private final String name;

    GameType (String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
