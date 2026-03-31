package ca.ucalgary.seng300.client.components;

public class leaderBoardRows {
    private final int rank;
    private final String playerName;
    private final int wins;
    private final int matches;

    public leaderBoardRows(int rank, String playerName, int wins, int matches) {
        this.rank = rank;
        this.playerName = playerName;
        this.wins = wins;
        this.matches = matches;
    }

    public int getRank() {
        return rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getWins() {
        return wins;
    }

    public int getMatches() {
        return matches;
    }
}
