package game;

import game.service.Service;

public class MainController {

    public static void main(String[] args) {
        Service service = new Service();
        service.readFiles("resources/player_data.txt", "resources/match_data.txt");
        service.makePlayerMoves();
        service.writeResults();
    }
}
