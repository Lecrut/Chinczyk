public class Game {

    public Player[] players;

    public Board board ;
    private final int playerNumber;

    public Game(int x) {
        playerNumber = x;
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

    private void checkSpecialFields(Player player) {
        for (int i = 0 ; i < 4 ; i++ ) {
            if ( player.pawns[i].getStatus() == PawnStatuses.IN_GAME) {
                SpecialFieldTypes specialFieldType = board.getSpecialField((player.pawns[i].getPosition() + player.getFirstField())%61);
                triggerSpecialFields(player.pawns[i], specialFieldType);
                for ( int j = 0 ; j < playerNumber ; j++ ) {
                    checkCollisionPlayer(player, players[j]);
                }
            }
        }
    }

    private int teleportPawn() {
        while (true) {
            int x = (int) (Math.random() * 56);
            if ( board.getSpecialField(x) == null ) {
                return x;
            }
        }
    }
    private void triggerSpecialFields ( Pawn pawn , SpecialFieldTypes fieldType ) {
        if ( fieldType == null)
            return;
        switch (fieldType) {
            case FORWARD_1 -> pawn.move(1);
            case FORWARD_2 -> pawn.move(2);
            case FORWARD_3 -> pawn.move(3);
            case BACKWARD_1 -> pawn.move(-1);
            case BACKWARD_2 -> pawn.move(-2);
            case BACKWARD_3 -> pawn.move(-3);
            case TELEPORT -> pawn.setPosition(teleportPawn());
        }
    }

    private void checkCollisionPlayer(Player player1, Player player2) {
        if ( player1.equals(player2) ) {
            return;
        }
        for ( int j = 0 ; j < 4 ; j++) {
            if ( player2.pawns[j].getStatus() == PawnStatuses.IN_GAME ) {
                for ( int k = 0 ; k < 4 ; k ++) {
                    if ( player1.pawns[k].getStatus() == PawnStatuses.IN_GAME) {
                        if ( (player1.pawns[k].getPosition() + player1.getFirstField())%61 == (player2.pawns[j].getPosition() + player2.getFirstField())%61) {
                            player2.pawns[j].setStatusGame(PawnStatuses.IN_BASE);
                            player2.pawns[j].setPosition(0);
                        }
                    }
                }
            }
        }
    }



    public void checkBoard (Player player) {
        for ( int i = 0 ; i < playerNumber ; i++) {
            checkCollisionPlayer(player, players[i]);
        }
        checkSpecialFields(player);
    }
}
