public class Main {
    private static final int numberOfPlayers = 4;

    public static void main(String[] args) {
        Game game = new Game(numberOfPlayers);
        while (game.getWinnersTable().size() < numberOfPlayers - 1) {
            game.round();
        }
        game.generatePopup();
    }
}