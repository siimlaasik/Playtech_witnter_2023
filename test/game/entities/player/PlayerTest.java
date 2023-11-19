package game.entities.player;

import game.entities.move.Move;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void addBalanceIncreasesBalanceAmount() {
        Player player = new Player("abcd");

        player.addBalance(100);

        assertEquals(100, player.getBalance());
    }


    @Test
    void removeBalanceRemovesBalance() {
        Player player = new Player("abcd");

        player.addBalance(100);
        player.removeBalance(50);

        assertEquals(50, player.getBalance());
    }

    @Test
    void setWrongMoveDoesNotUpdateIllegalMoveWhenThereAlreadyIsIllegalMove() {
        Player player = new Player("abcd");
        Move move = new Move("BET", "matchid", "20", "A");
        Move move2 = new Move("BET", "matchid2", "10", "B");

        player.setWrongMove(move);
        player.setWrongMove(move2);


        assertEquals(move, player.getWrongMove());
    }

    @Test
    void addGameAddsGameAmountCorrectly() {
        Player player = new Player("abcd");

        player.addGame();
        player.addGame();

        assertEquals(2, player.getGamesPlayed());
    }

    @Test
    void addGamesWonAddsWonGamesCorrectly() {
        Player player = new Player("abcd");

        player.addGamesWon();
        player.addGamesWon();

        assertEquals(2, player.getGamesWon());
    }

    @Test
    void getUUIDReturnsTypeUUIDInsteadOfString() {
        Player player = new Player("4925ac98-833b-454b-9342-13ed3dfd3ccf");

       assertInstanceOf(UUID.class, player.getUUID() );
    }

    @Test
    void addMoveAddsMovesToList() {
        Player player = new Player("4925ac98-833b-454b-9342-13ed3dfd3ccf");
        Move move = new Move("BET", "matchid", "20", "A");
        Move move2 = new Move("BET", "matchid2", "10", "B");

        player.addMove(move);
        player.addMove(move2);

        assertEquals(2, player.getMoves().size());
    }

    @Test
    void createPlayerStringIllegalPlayerReturnsCorrectString() {
        Player player = new Player("4925ac98-833b-454b-9342-13ed3dfd3ccf");
        Move move = new Move("BET", "matchid2", "10", "B");
        String string = "4925ac98-833b-454b-9342-13ed3dfd3ccf BET matchid2 10 B";

        player.setWrongMove(move);

        assertEquals(string, player.createPlayerString());
    }

    @Test
    void createPlayerStringIllegalPlayerWithdrawWrongMoveReturnsCorrectString() {
        Player player = new Player("4925ac98-833b-454b-9342-13ed3dfd3ccf");
        Move move = new Move("WITHDRAW", "", "10", "");
        String string = "4925ac98-833b-454b-9342-13ed3dfd3ccf WITHDRAW null 10 null";

        player.setWrongMove(move);

        assertEquals(string, player.createPlayerString());
    }

    @Test
    void createPlayerStringLegalPlayerReturnsCorrectString() {
        Player player = new Player("4925ac98-833b-454b-9342-13ed3dfd3ccf");
        String correctResult = "4925ac98-833b-454b-9342-13ed3dfd3ccf 50 0.50";

        player.setBalance(50);
        player.setGamesPlayed(50);
        player.setGamesWon(25);

        System.out.println(player.createPlayerString());
        assertEquals(correctResult, player.createPlayerString());
    }

    @Test
    void getWinRateReturnsCorrectWinRate() {
        Player player = new Player("123");

        player.setBalance(50);
        player.setGamesPlayed(50);
        player.setGamesWon(25);
        System.out.println(player.createPlayerString());
        String string = player.createPlayerString().substring(7, 11);
        assertEquals("0.50", string);
    }
}