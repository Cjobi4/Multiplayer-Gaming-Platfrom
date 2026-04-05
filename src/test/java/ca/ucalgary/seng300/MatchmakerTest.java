package ca.ucalgary.seng300;  // matchmaker is package specifc, thus not in core

import ca.ucalgary.seng300.core.StubSocket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.ucalgary.seng300.Matchmaker;
import ca.ucalgary.seng300.Session;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchmakerTest {

    private static Matchmaker matchmaker;
    private static Session session;
    String gameName = "ttt";  // doing this so that i can test run()

    @BeforeAll
    static void setUp(){
        matchmaker = new Matchmaker("ttt");
        session = new Session(new StubSocket(new byte[0]));
    }

    @Test
    void run() {
    }

    @Test
    void testJoinMQueue () throws Exception {

    }

    @Test
    void testLeaveMQueue() {
    }
}