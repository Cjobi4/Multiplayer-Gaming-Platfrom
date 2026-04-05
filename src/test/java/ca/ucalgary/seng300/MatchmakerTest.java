package ca.ucalgary.seng300;  // matchmaker constructor is package specific, thus not in core

import ca.ucalgary.seng300.core.StubSocket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchmakerTest {

    private static Matchmaker matchmaker;
    private static Session session;

    @BeforeAll
    static void setUp(){
        matchmaker = new Matchmaker("ttt");
        session = new Session(new StubSocket(new byte[0]));
    }

    @Test
    void run() {
    }

    @Test
    void testJoinMQueue () {
        boolean result = matchmaker.joinMQueue(session);
        assertTrue(result, "Boolean value of joinMQueue should be true when joined successfully.");
    }

    @Test
    void testLeaveMQueue() {
        boolean result = matchmaker.leaveMQueue(session);
        assertTrue(result, "Boolean value of leaveMQueue should be true when left successfully.");
    }

    @Test
    void testJoinAndLeaveMQueueSuccessfully() {
        boolean join = matchmaker.joinMQueue(session);
        boolean leave = matchmaker.leaveMQueue(session);

        assertTrue(join, "Boolean value of joinMQueue should be true when joined successfully.");
        assertTrue(leave, "Boolean value of leaveMQueue should be true when left successfully.");
    }

    @Test
    void testMultipleSessionJoinAndLeaveMQueueSuccessfully() {
        Session session2 = new Session(new StubSocket(new byte[0]));

        boolean join1 = matchmaker.joinMQueue(session);
        boolean join2 = matchmaker.joinMQueue(session2);

        boolean leave1 = matchmaker.leaveMQueue(session);
        boolean leave2 = matchmaker.leaveMQueue(session2);

        assertTrue(join1, "Boolean value of joinMQueue should be true when joined successfully.");
        assertTrue(leave1, "Boolean value of leaveMQueue should be true when left successfully.");
        assertTrue(join2, "Boolean value of joinMQueue should be true when joined successfully.");
        assertTrue(leave2, "Boolean value of leaveMQueue should be true when left successfully.");
    }
}