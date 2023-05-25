public class Game {

    public Player[] players;

    public Board board ;

    public Game(int x) {
        players = new Player[x];
        board = new Board();
        if ( x >= 1 )
            players[0] = new Player(Colour.GREEN, 0, 56);
        if ( x >= 2 )
            players[1] = new Player(Colour.BLUE, 28, 27);
        if ( x >= 3)
            players[2] = new Player(Colour.RED, 14, 13);
        if ( x == 4)
            players[3] = new Player(Colour.YELLOW, 42, 41);
    }

    public void round() {

    }

    public void checkBoard () {

    }
}
