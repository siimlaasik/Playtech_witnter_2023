package game.entities.player;

import game.entities.move.Move;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.math.*;

public class Player {
    private int balance = 0;
    private final String id;
    private int gamesPlayed;
    private int gamesWon;
    private final List<Move> moves = new ArrayList<>();
    private Move illegalMove;

    public Player( String id) {
        this.id = id;
    }

    public void addBalance(int amount) {
        balance += amount;
    }

    public void removeBalance(int amount) {
        balance -= amount;
    }

    public void setWrongMove(Move move) {
        if (illegalMove == null) {
            illegalMove = move;
        }
    }

    public Move getWrongMove() {
        return illegalMove;
    }

    public void addGame() {
        gamesPlayed ++;
    }


    public void addGamesWon() {
        gamesWon++;
    }

    public String createPlayerString() {
        if (illegalMove == null) {
            return id + " " + balance + " " + getWinRate();
        } else if (illegalMove.getOperation().equals("WITHDRAW") || illegalMove.getOperation().equals("DEPOSIT")) {
            return id + " " + illegalMove.getOperation() + " " + null + " "
                    + illegalMove.getCoinNumber() + " " + null;
        } else {
            return id + " " + illegalMove.getOperation() + " " + illegalMove.getMatchID() + " "
                    + illegalMove.getCoinNumber() + " " + illegalMove.getBetOnSide();
        }
    }

    private BigDecimal getWinRate() {
        if (gamesPlayed == 0) {
            return BigDecimal.ZERO;
        }
        return  new BigDecimal(gamesWon).divide(new BigDecimal(gamesPlayed), 2, RoundingMode.HALF_UP);
    }

    public UUID getUUID() {
        return UUID.fromString(id);
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public List<Move> getMoves() {
        return moves;
    }
}
