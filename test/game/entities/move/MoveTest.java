package game.entities.move;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @org.junit.jupiter.api.Test
    void operationIsValidCorrect() {
        Move move = new Move("BET", "matchid", "20", "A");

        assertTrue(move.operationIsValid());
    }

    @org.junit.jupiter.api.Test
    void isValidBetOperationNoBetOnSide() {
        Move move = new Move("BET", "matchid", "20", "");

        assertFalse(move.operationIsValid());
    }

    @org.junit.jupiter.api.Test
    void isValidBetOperationNoMatchID() {
        Move move = new Move("BET", "", "20", "A");

        assertFalse(move.operationIsValid());
    }

    @org.junit.jupiter.api.Test
    void isValidDepositOperationNoBetOnSide() {
        Move move = new Move("DEPOSIT", "matchid", "20", "");

        assertFalse(move.operationIsValid());
    }

    @org.junit.jupiter.api.Test
    void isValidDepositOperationNoMatchID() {
        Move move = new Move("DEPOSIT", "", "20", "A");

        assertFalse(move.operationIsValid());
    }

    @org.junit.jupiter.api.Test
    void isValidDepositOperationNoMatchIDNoBetOnSide() {
        Move move = new Move("DEPOSIT", "", "20", "");

        assertTrue(move.operationIsValid());
    }

    @org.junit.jupiter.api.Test
    void isValidWithdrawOperationNoBetOnSide() {
        Move move = new Move("WITHDRAW", "matchid", "20", "");

        assertFalse(move.operationIsValid());
    }

    @org.junit.jupiter.api.Test
    void isValidWithdrawOperationNoMatchID() {
        Move move = new Move("WITHDRAW", "", "20", "A");

        assertFalse(move.operationIsValid());
    }

    @org.junit.jupiter.api.Test
    void isValidDepositOperationNoMatchIDNoBetOnSideNoBetOnSide() {
        Move move = new Move("WITHDRAW", "", "20", "");

        assertTrue(move.operationIsValid());
    }
}