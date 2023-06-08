public class Main {
    public static void main(String[] args) {
        Game game = new Game(4);
        while (game.getWinnersTable().size() < 3) { //TODO: do zmiany warunek
            game.round();
        }
    }
}