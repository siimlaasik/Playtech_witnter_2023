package game.service;

import game.entities.match.Match;
import game.entities.move.Move;
import game.entities.player.Player;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service {
     private long balance = 0L;
     private final Map<String, Player> players = new HashMap<>();
     private final Map<String, Match> matches = new HashMap<>();

    public void readFiles(String playerDataFile, String matchDataFile) {
        readFile(matchDataFile, 1);
        readFile(playerDataFile, 0);
    }

    private  void readFile(String dataFile, Integer fileType) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String action;
            while ((action = br.readLine()) != null) {
                if (!verifyLine(action, fileType)) {
                    continue;
                }
                updateList(action, fileType);
            }
        } catch (IOException e) {
            System.out.println("Reading file failed");
        }
    }

    private boolean verifyLine(String action, Integer fileType) {
        String regex = "";
        if (fileType == 0) {
            regex = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12},[A-Z]+,([a-f0-9]" +
                    "{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})?,[0-9]+,[A|B]?";
        } else if (fileType == 1) {
            regex = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12},[0-9].[0.-9].,[0-9]" +
                    ".[0.-9].,(A|B|DRAW)";
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(action);
        return matcher.matches();
    }

    private void updateList(String action, Integer fileType) {
        String[] values = action.split(",");
        Move move;
        if (fileType == 0) {
            if (values.length == 5) {
                move = new Move(values[1], values[2], values[3], values[4]);
            } else {
                move = new Move(values[1], values[2], values[3]);
            }
            if (!players.containsKey(values[0])) {
                Player player = new Player(values[0]);
                players.put(values[0], player);
            }
                players.get(values[0]).addMove(move);
        } else if (fileType == 1) {
            Match match = new Match(values[0], values[1], values[2], values[3]);
            if (!matches.containsKey(values[0])) {
                matches.put(values[0], match);
            }
        }
    }

    public void makePlayerMoves() {
        for (Player player: players.values()) {
            long casinoStartingBalance = balance;
            for (Move move : player.getMoves()) {
                if (move.operationIsValid() && player.getWrongMove() == null && player.hasNotBetOnMatchYet(move)) {
                    switch (move.getOperation()) {
                        case "DEPOSIT":
                            deposit(player, move);
                            break;
                        case "WITHDRAW":
                            withdraw(player, move);
                            break;
                        case "BET":
                            bet(player, move);
                            break;
                    }
                } else {
                    player.setWrongMove(move);
                    balance = casinoStartingBalance;
                    break;
                }
                if (player.getWrongMove() != null) {
                    balance = casinoStartingBalance;
                }
            }
        }
    }

    private void deposit(Player player, Move move) {
        if (move.getCoinNumber() > 0) {
            player.addBalance(move.getCoinNumber());
        } else {
            player.setWrongMove(move);
        }
    }

    private void withdraw(Player player, Move move) {
        if (player.getBalance() >= move.getCoinNumber() && move.getCoinNumber() > 0) {
            player.removeBalance(move.getCoinNumber());
        } else {
            player.setWrongMove(move);
        }
    }

    private void bet(Player player, Move move) {
        if (player.getBalance() >= move.getCoinNumber() && move.getCoinNumber() > 0 &&
                matches.containsKey(move.getMatchID()) && move.getBetOnSide() != null) {
            player.addGame();
            String result = matches.get(move.getMatchID()).getResult();
            BigDecimal returnRate = BigDecimal.valueOf(1);
            if (result.equals(move.getBetOnSide())) {
                if (move.getBetOnSide().equals("A")) {
                    returnRate = matches.get(move.getMatchID()).getASideReturnRate();
                } else if (move.getBetOnSide().equals("B")) {
                    returnRate = matches.get(move.getMatchID()).getBSideReturnRate();
                }
                int returnedMoney = returnRate.multiply(new BigDecimal(move.getCoinNumber())).intValue();
                balance -= returnedMoney;
                player.addBalance(returnedMoney);
                player.addGamesWon();
            } else if (!result.equals("DRAW")){
                player.removeBalance(move.getCoinNumber());
                balance += move.getCoinNumber();
            }
        } else {
            player.setWrongMove(move);
        }
    }

    public void writeResults() {
        String results = createResultString();
        String filePath = "src/game/result.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the content to the file
                writer.write(results);
        } catch (IOException e) {
            System.out.println("Writing failed");
        }
    }

    private String createResultString() {
        List<Player> playerList = players.values().stream()
                .sorted(Comparator.comparing(Player::getUUID)).toList();
        StringBuilder resultStringBuilder = new StringBuilder();
        StringBuilder illegitimateStringBuilder = new StringBuilder();

        for (Player player : playerList) {
            String playerString = player.createPlayerString();
            if (player.getWrongMove() == null) {
                resultStringBuilder.append(playerString).append("\n");
            } else {
                illegitimateStringBuilder.append(playerString).append("\n");
            }
        }
        if (resultStringBuilder.isEmpty()) {
            resultStringBuilder.append("\n");
        }
        resultStringBuilder.append("\n");
        if (illegitimateStringBuilder.isEmpty()) {
            resultStringBuilder.append("\n");
        }
        resultStringBuilder.append(illegitimateStringBuilder);
        resultStringBuilder.append("\n");
        resultStringBuilder.append(balance);
        return resultStringBuilder.toString();
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Map<String, Match> getMatches() {
        return matches;
    }
}
