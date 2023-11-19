package game.service;

import game.entities.move.Move;
import game.entities.player.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @Test
    void readFilesReadsPlayerFiles() {
        String match_data_string = "test_resources/test_match_data.txt";
        String player_data_string = "test_resources/test_player_data.txt";
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);

        assertFalse(service.getPlayers().values().isEmpty());
    }

    @Test
    void readFilesReadsMatchFiles() {
        String match_data_string = "test_resources/test_match_data.txt";
        String player_data_string = "test_resources/test_player_data.txt";
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);

        assertFalse(service.getMatches().values().isEmpty());
    }

    @Test
    void readFilesIgnoresIncorrectLinesForPlayerData() {
        String match_data_string = "test_resources/test_match_data.txt";
        String player_data_string = "test_resources/test_player_data.txt";
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);

        assertEquals(2 ,service.getPlayers().values().size());
    }

    @Test
    void readFilesIgnoresIncorrectLinesForMatchData() {
        String match_data_string = "test_resources/test_match_data.txt";
        String player_data_string = "test_resources/test_player_data.txt";
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);

        assertEquals(3 ,service.getMatches().values().size());
    }

    @Test
    void readFilesUpdatesCreatesCorrectPlayerEntities() {
        String match_data_string = "test_resources/test_match_data.txt";
        String player_data_string = "test_resources/test_player_data.txt";
        String string = "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4";
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        Player player = service.getPlayers().get(string);

        assertEquals(string, player.getId());
    }

    @Test
    void readFilesUpdatesCreatesCorrectMoveEntities() {
        String match_data_string = "test_resources/test_match_data.txt";
        String player_data_string = "test_resources/test_player_data.txt";
        String[] values = {"DEPOSIT","","4000",""};
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4");
        Move move = player.getMoves().get(0);

        assertEquals(values[0], move.getOperation());
        assertEquals(values[1], move.getMatchID());
        assertEquals(Integer.parseInt(values[2]), move.getCoinNumber());
        assertEquals(values[3], move.getBetOnSide());
    }

    @Test
    void readFilesUpdatesPlayerMoves() {
        String match_data_string = "test_resources/test_match_data.txt";
        String player_data_string = "test_resources/test_player_data.txt";
        String[] values = {"DEPOSIT","","4000",""};
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4");

        assertFalse(player.getMoves().isEmpty());
    }

    @Test
    void makePlayerMovesResetsServiceBalanceWhenPlayerMadeIllegalMove() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data2.txt";
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();

        assertEquals(0, service.getBalance());
    }

    @Test
    void makePlayerMovesPlayerWrongMoveNotNullDoesNotAddAdditionalWrongMoveAndResetsBalance() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data3.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4");

        assertEquals("" ,player.getWrongMove().getMatchID());
        assertEquals("DEPOSIT" ,player.getWrongMove().getOperation());
    }

    @Test
    void makePlayerMovesOperationNotValidUpdatesPlayerIllegalMove() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data2.txt";
        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4");

        assertNotNull(player.getWrongMove());
    }

    @Test
    void makePlayerMovesDepositAddsIllegalMoveIfCoinsZeroOrLess() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data3.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4");

        assertEquals("" ,player.getWrongMove().getMatchID());
        assertEquals("DEPOSIT" ,player.getWrongMove().getOperation());
    }

    @Test
    void makePlayerMovesDepositAddsMoneyToPlayer() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data2.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12ff");

        assertEquals(4000, player.getBalance());
    }

    @Test
    void makePlayerMovesWithdrawBalanceNotEnoughAddsWrongMove() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data4.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe");

        assertNotNull(player.getWrongMove());
    }

    @Test
    void makePlayerMovesWithdrawCoinsZeroOrLessAddsWrongMove() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data4.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe");

        assertNotNull(player.getWrongMove());
    }

    @Test
    void makePlayerMovesBetPlayerNotEnoughBalanceAddsWrongMove() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe");

        assertNotNull(player.getWrongMove());
    }

    @Test
    void makePlayerMovesBetPlayerAlreadyBetOnGame() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data8.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa");

        assertNotNull(player.getWrongMove());
    }


    @Test
    void makePlayerMovesBetZeroOrLessSetsWrongMove() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12ff");

        assertNotNull(player.getWrongMove());
    }

    @Test
    void makePlayerMovesBetNoMatchIDFoundSetsWrongMove() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fc");

        assertNotNull(player.getWrongMove());
    }

    @Test
    void makePlayerMovesBetNoBetOnSideSetsWrongMove() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fc");

        assertNotNull(player.getWrongMove());
    }

    @Test
    void makePlayerMovesBetRemovesPlayerBalance() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fb");

        assertEquals(2000, player.getBalance());
    }

    @Test
    void makePlayerMovesBetAddsServiceBalance() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();

        assertEquals(-2000, service.getBalance());
    }

    @Test
    void makePlayerMovesBetAddsPlayerGames() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fb");

        assertEquals(1, player.getGamesPlayed());
    }

    @Test
    void makePlayerMovesBetAddsPlayerGamesWon() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa");

        assertEquals(1, player.getGamesWon());
    }

    @Test
    void makePlayerMovesBetWinRemovesServiceBalance() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();

        assertEquals(-2000, service.getBalance());
    }

    @Test
    void makePlayerMovesBetWinningAddsPlayerBalanceCorrectAmount() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa");

        assertEquals(8000, player.getBalance());
    }

    @Test
    void makePlayerMovesBetDrawReturnsMoneyToPlayer() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data5.txt";

        Service service = new Service();

        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        Player player = service.getPlayers().get("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fd");

        assertEquals(4000, player.getBalance());
    }

    @Test
    void writeResultsWritesCorrectResultLegalPlayers() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data6.txt";
        List<String> result = new ArrayList<>();

        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa 4000 1.00");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe 8000 1.00");
        result.add("");
        result.add("");
        result.add("");
        result.add("-8000");
        Service service = new Service();
        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        service.writeResults();

        try {
            // Read all lines from the file into a List<String>
            List<String> lines = Files.readAllLines(Paths.get("src/game/result.txt"));
            assertEquals(result, lines);
        } catch (IOException e) {
            System.err.println("unable to read result.txt");
        }
    }

    @Test
    void writeResultsWritesCorrectResultIllegalPlayers() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data7.txt";
        List<String> result = new ArrayList<>();

        result.add("");
        result.add("");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa BET a3815c17-9def-4034-a21e-65369f6d4a56 2000 B");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe DEPOSIT null 0 null");
        result.add("");
        result.add("0");
        Service service = new Service();
        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        service.writeResults();

        try {
            // Read all lines from the file into a List<String>
            List<String> lines = Files.readAllLines(Paths.get("src/game/result.txt"));
            assertEquals(result, lines);
        } catch (IOException e) {
            System.err.println("unable to read result.txt");
        }
    }

    @Test
    void writeResultsWritesCorrectServiceBalance() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data7.txt";
        List<String> result = new ArrayList<>();

        result.add("");
        result.add("");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa BET a3815c17-9def-4034-a21e-65369f6d4a56 4000 B");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe DEPOSIT null 0 null");
        result.add("");
        result.add("0");
        Service service = new Service();
        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        service.writeResults();

        try {
            // Read all lines from the file into a List<String>
            List<String> lines = Files.readAllLines(Paths.get("src/game/result.txt"));
            assertEquals("0", lines.get(lines.size() - 1));
        } catch (IOException e) {
            System.err.println("unable to read result.txt");
        }
    }

    @Test
    void writeResultsSortsByID() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data7.txt";
        List<String> result = new ArrayList<>();

        result.add("");
        result.add("");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa BET a3815c17-9def-4034-a21e-65369f6d4a56 2000 B");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe DEPOSIT null 0 null");
        result.add("");
        result.add("0");
        Service service = new Service();
        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        service.writeResults();

        try {
            // Read all lines from the file into a List<String>
            List<String> lines = Files.readAllLines(Paths.get("src/game/result.txt"));
            assertEquals(result, lines);
        } catch (IOException e) {
            System.err.println("unable to read result.txt");
        }
    }

    @Test
    void writeResultsIfLegalPlayerResultsEmptyAddEmptyLine() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data7.txt";
        List<String> result = new ArrayList<>();

        result.add("");
        result.add("");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa BET a3815c17-9def-4034-a21e-65369f6d4a56 2000 B");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe DEPOSIT null 0 null");
        result.add("");
        result.add("0");
        Service service = new Service();
        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        service.writeResults();

        try {
            // Read all lines from the file into a List<String>
            List<String> lines = Files.readAllLines(Paths.get("src/game/result.txt"));
            assertEquals(result, lines);
        } catch (IOException e) {
            System.err.println("unable to read result.txt");
        }
    }

    @Test
    void writeResultsIfIllegalPlayerResultsEmptyAddEmptyLine() {
        String match_data_string = "test_resources/test_match_data2.txt";
        String player_data_string = "test_resources/test_player_data6.txt";
        List<String> result = new ArrayList<>();
        Service service = new Service();

        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fa 4000 1.00");
        result.add("163f23ed-e9a9-4e54-a5b1-4e1fc86f12fe 8000 1.00");
        result.add("");
        result.add("");
        result.add("");
        result.add("-8000");
        service.readFiles(player_data_string, match_data_string);
        service.makePlayerMoves();
        service.writeResults();

        try {
            // Read all lines from the file into a List<String>
            List<String> lines = Files.readAllLines(Paths.get("src/game/result.txt"));
            assertEquals(result, lines);
        } catch (IOException e) {
            System.err.println("unable to read result.txt");
        }
    }
}