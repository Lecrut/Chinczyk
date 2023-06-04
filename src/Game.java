import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private int numberOfPlayers = 0;
    private final Player[] players;
    private final Board board;
    private final JPanel infoPanel = new JPanel();
    JButton diceRoll = new JButton("Kostka");
    JTextField [] table;
    public Game(int playersNumber) throws HeadlessException {
        players = new Player[playersNumber];
        board = new Board();
        setFrameParameters();
        numberOfPlayers = playersNumber;

        //TODO tymczasowy pionek
        board.setPawn(new Pawn(new ImageIcon("assets/pawn.png")), 0);

        if (playersNumber >= 1)
            players[0] = new Player(Colour.GREEN, 0, 56);
        if (playersNumber >= 2)
            players[1] = new Player(Colour.BLUE, 28, 27);
        if (playersNumber >= 3)
            players[2] = new Player(Colour.RED, 14, 13);
        if (playersNumber == 4)
            players[3] = new Player(Colour.YELLOW, 42, 41);
    }

    public void round() {
        for (Player player : players) {
            player.playerMove();
            checkBoard(player);
        }
    }

    private void checkSpecialFields(Player player) {
        for (Pawn pawn : player.pawns) {
            if (pawn.getStatus() == PawnStatuses.IN_GAME) {
                SpecialFieldTypes specialFieldType = board.getSpecialField((pawn.getPosition() + player.getFirstField()) % 61);
                triggerSpecialFields(pawn, specialFieldType);
                for (Player player1 : players) {
                    checkCollisionPlayer(player, player1);
                }
            }
        }
    }

    private int teleportPawn() {
        while (true) {
            int x = (int) (Math.random() * 56);
            if (board.getSpecialField(x) == null) {
                return x;
            }
        }
    }

    private void triggerSpecialFields(Pawn pawn, SpecialFieldTypes fieldType) {
        if (fieldType == null)
            return;
        switch (fieldType) {
            case FORWARD_1: pawn.move(1);
            case FORWARD_2: pawn.move(2);
            case FORWARD_3: pawn.move(3);
            case BACKWARD_1: pawn.move(-1);
            case BACKWARD_2: pawn.move(-2);
            case BACKWARD_3: pawn.move(-3);
            case TELEPORT: pawn.setPosition(teleportPawn());
        }
    }

    private void checkCollisionPlayer(Player player1, Player player2) {
        if (player1.equals(player2)) {
            return;
        }
        for (Pawn pawn2 : player2.pawns) {
            if (pawn2.getStatus() == PawnStatuses.IN_GAME) {
                for (Pawn pawn1 : player1.pawns) {
                    if (pawn1.getStatus() == PawnStatuses.IN_GAME) {
                        if ((pawn1.getPosition() + player1.getFirstField()) % 61 == (pawn2.getPosition() + player2.getFirstField()) % 61) {
                            pawn2.setStatusGame(PawnStatuses.IN_BASE);
                            pawn2.setPosition(0);
                        }
                    }
                }
            }
        }
    }

    private void checkWinningPawns() {
        for (Player player : players) {
            for (Pawn pawn : player.pawns) {
                if (pawn.getPosition() == 61) {
                    pawn.setStatusGame(PawnStatuses.IN_END);
                }
            }
        }
    }

    public void checkBoard(Player player) {
        for (Player player1 : players) {
            checkCollisionPlayer(player, player1);
        }
        checkWinningPawns();
        checkSpecialFields(player);
    }

    private void setFrameParameters() {
        infoPanel.setBounds(800,0,400,800);
        infoPanel.setBackground(new Color(0xA65A1D));

        diceRoll.setBackground(Color.white);
        diceRoll.setFont(new Font("Arial",Font.BOLD,10));
        diceRoll.setBounds(40,40,40,40);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1200, 800);
        this.setLayout(null);
        this.setVisible(true);
        this.setTitle("Gra chińczyk");

        infoPanel.add(diceRoll);
        this.add(infoPanel,BorderLayout.WEST);
        this.add(board);
        this.repaint();
    }
}
