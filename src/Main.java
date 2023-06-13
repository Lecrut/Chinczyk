public class Main {
    static int PLAYERS_NUMBER;
    public static void main(String[] args) {
        PLAYERS_NUMBER = Game.startMenu();
        Game game = new Game(PLAYERS_NUMBER);

        Music.playMusic();
        while (game.getWinnersTable().size() < PLAYERS_NUMBER - 1) {
            game.round();
        }
        game.generatePopup();
    }
}