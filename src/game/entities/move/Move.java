package game.entities.move;

public class Move {
    private final String operation;
    private final String matchID;
    private final int coinNumber;
    private final String betOnSide;

    public Move(String operation, String matchID, String coinNumber, String betOnSide) {
        this.operation = operation;
        this.matchID = matchID;
        this.coinNumber = Integer.parseInt(coinNumber);
        this.betOnSide = betOnSide;
    }

    public Move(String operation, String matchID, String coinNumber) {
        this.operation = operation;
        this.matchID = matchID;
        this.coinNumber = Integer.parseInt(coinNumber);
        betOnSide = "";
    }

    public boolean operationIsValid() {
        return isValidBetOperation() || isValidDepositOrWithdrawOperation();
    }

    private boolean isValidBetOperation() {
        return !betOnSide.isEmpty() && !matchID.isEmpty() && operation.equals("BET");
    }

    private boolean isValidDepositOrWithdrawOperation() {
        return betOnSide.isEmpty() && matchID.isEmpty() && (operation.equals("DEPOSIT") || operation.equals("WITHDRAW"));
    }

    public String getOperation() {
        return operation;
    }

    public String getMatchID() {
        return matchID;
    }

    public int getCoinNumber() {
        return coinNumber;
    }

    public String getBetOnSide() {
        return betOnSide;
    }
}
