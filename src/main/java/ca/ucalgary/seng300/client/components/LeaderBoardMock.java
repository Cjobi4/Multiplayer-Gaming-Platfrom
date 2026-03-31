package ca.ucalgary.seng300.client.components;
import ca.ucalgary.seng300.client.components.LeaderBoardRows;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardMock {

    public static List<LeaderBoardRows> getCombinedLeaderboard(){
        List<LeaderBoardRows> rows = new ArrayList<>();

        rows.add(new LeaderBoardRows(1, "Cj", 80, 130));
        rows.add(new LeaderBoardRows(2, "Rebecca", 71, 125));
        rows.add(new LeaderBoardRows(3, "Anaya", 69, 100));
        rows.add(new LeaderBoardRows(4, "Owen", 63, 140));
        rows.add(new LeaderBoardRows(5, "Wekai", 55, 98));
        rows.add(new LeaderBoardRows(6, "Sanmeet", 50, 100));
        rows.add(new LeaderBoardRows(7, "Sajan", 15, 200));

        return rows;
    }

}
