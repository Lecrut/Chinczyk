import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Game extends JFrame {
    private final int roundCounter = 0;
    private final Player[] players;
    private Player currentPlayer;
    private final Board board;
    private final JPanel infoPanel = new JPanel();
    private final JLabel textInfo = new JLabel();
    private final JLabel dicePlaceholder = new JLabel();
//    private final ImageIcon[] diceViews = new ImageIcon[6];
    public static final JButton diceView = new JButton();

    private final ArrayList<PossibleColors> winnersTable = new ArrayList<>();

    private final static int AROUND_ROUTE_LENGTH = 56;
    private final static int MAP_WIDTH = 1200;
    private final static int MAP_HEIGHT = 800;
    private final static int DISTANCE_BETWEEN_PLAYERS = 14;
    public final static int FINAL_PATH = Pawn.PAWN_ROUTE - AROUND_ROUTE_LENGTH;
    public final static int PANEL_DIMENSIONS = 400;
    public final static int DICE_SIZE = 90;

    public Dice kostka;

    public Game(int playersNumber) throws HeadlessException {
        players = new Player[playersNumber];
        board = new Board();
        kostka = new Dice();

        if (playersNumber >= 1) {
            players[0] = new Player(PossibleColors.BLUE, 0, 4 * DISTANCE_BETWEEN_PLAYERS - 1);
            for (Pawn pawn : players[0].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.BLUE);
            }
        }
        if (playersNumber >= 2) {
            players[1] = new Player(PossibleColors.RED, 2 * DISTANCE_BETWEEN_PLAYERS, (2 * DISTANCE_BETWEEN_PLAYERS) - 1);
            for (Pawn pawn : players[1].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.RED);
            }
        }
        if (playersNumber >= 3) {
            players[2] = new Player(PossibleColors.GREEN, DISTANCE_BETWEEN_PLAYERS, DISTANCE_BETWEEN_PLAYERS - 1);
            for (Pawn pawn : players[2].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.GREEN);
            }
        }
        if (playersNumber == 4) {
            players[3] = new Player(PossibleColors.YELLOW, 3 * DISTANCE_BETWEEN_PLAYERS, (3 * DISTANCE_BETWEEN_PLAYERS) - 1);
            for (Pawn pawn : players[3].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.YELLOW);
            }
        }
        currentPlayer = players[0];

        setFrameParameters();
    }

    public void round() {
        for (Player player : players) {
            if (player.getStatus() == Statuses.FREE) {
                currentPlayer = player;
                infoPanel.setBackground(currentPlayer.getPlayerColor());
                boolean nextMove = true;
                for (int i = 1; nextMove; i++) {
                    final CountDownLatch latch = new CountDownLatch(1);
                    kostka.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            kostka = (Dice) e.getSource();
                            kostka.diceThrow();
                            latch.countDown(); // Zwalnianie CountDownLatch po kliknięciu
                        }
                    });
                    try {
                        latch.await(); // Oczekiwanie na kliknięcie
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    kostka.removeMouseListener(kostka.getMouseListeners()[0]);
                    nextMove = player.playerMove(board, i, kostka.getDiceResult());
                    checkBoard(player);
                    if (getWinnersTable().size() == 3) {
                        nextMove = false;
                    }
                    if (player.getStatus() == Statuses.WINNER) {
                        nextMove = false;
                    }
                }
            }
        }
    }

    public ArrayList<PossibleColors> getWinnersTable() {
        return winnersTable;
    }

    private void checkSpecialFields(Player player) {
        for (Pawn pawn : player.getPawns()) {
            if (pawn.getStatus() == PawnStatuses.IN_GAME) {
                SpecialFieldTypes specialFieldType = board.getSpecialField((pawn.getPosition() + player.getFirstField()) % AROUND_ROUTE_LENGTH);
                triggerSpecialFields(pawn, player, specialFieldType);
                for (Player player1 : players) {
                    checkCollisionPlayer(player, player1);
                }
            }
        }
    }

    private int teleportPawn() {
        while (true) {
            int x = (int) (Math.random() * AROUND_ROUTE_LENGTH);
            if (board.getSpecialField(x) == null) {
                return x;
            }
        }
    }

    private void triggerSpecialFields(Pawn pawn, Player player, SpecialFieldTypes fieldType) {
        if (fieldType == null)
            return;
        System.out.println("\npole specjalne " + fieldType.name() + "\n");
        switch (fieldType) {
            case FORWARD_1 -> pawn.move(1);
            case FORWARD_2 -> pawn.move(2);
            case FORWARD_3 -> pawn.move(3);
            case BACKWARD_1 -> pawn.move(-1);
            case BACKWARD_2 -> pawn.move(-2);
            case BACKWARD_3 -> pawn.move(-3);
            case TELEPORT -> pawn.setPosition(teleportPawn());
        }
        if (pawn.getPosition() < AROUND_ROUTE_LENGTH)
            board.setPawn(pawn, (pawn.getPosition() + player.getFirstField()) % AROUND_ROUTE_LENGTH);
        else if (pawn.getPosition() == Pawn.PAWN_ROUTE)
            board.setPawnEndBase(pawn, player.getPlayerColorName());
        else
            board.setPawnEndPath(pawn, player.getPlayerColorName(), pawn.getPosition() - AROUND_ROUTE_LENGTH);
    }

    private void checkCollisionPlayer(Player player1, Player player2) {
        if (player1.equals(player2)) {
            return;
        }
        for (Pawn pawn2 : player2.getPawns()) {
            if (pawn2.getStatus() == PawnStatuses.IN_GAME) {
                for (Pawn pawn1 : player1.getPawns()) {
                    if (pawn1.getStatus() == PawnStatuses.IN_GAME) {
                        if ((pawn1.getPosition() + player1.getFirstField()) % AROUND_ROUTE_LENGTH == (pawn2.getPosition() + player2.getFirstField()) % AROUND_ROUTE_LENGTH) {
                            pawn2.setStatusGame(PawnStatuses.IN_BASE);
                            pawn2.setPosition(0);
                            board.setPawnStartBase(pawn2, player2.getPlayerColorName());
                        }
                    }
                }
            }
        }
    }

    private void checkWinningPawns() {
        for (Player player : players) {
            if (checkWinningPlayer(player)) {
                winnersTable.add(player.getPlayerColorName());
                player.setStatusWinner();
            }
        }
    }

    public boolean checkWinningPlayer(Player player) {
        if (player.getStatus() == Statuses.WINNER)
            return false;
        for (Pawn pawn : player.getPawns()) {
            if (pawn.getStatus() != PawnStatuses.IN_END) {
                return false;
            }
        }
        return true;
    }

    public void checkBoard(Player player) {
        for (Player player1 : players) {
            checkCollisionPlayer(player, player1);
        }
        checkWinningPawns();
        checkSpecialFields(player);
    }

    public boolean checkWinners() {
        for (Player player : players) {
            if (player.getStatus() == Statuses.WINNER) {
                return true;
            }
        }
        return false;
    }

    public void setInformation() {
        if (checkWinners()) {
            String x = "<html><pre>ROUND: " + roundCounter + "\nTURN: " + currentPlayer.getPlayerColorName() + "\n<ol>";
            for (PossibleColors color : winnersTable) {
                x += "<li>" + color + "</li>";
            }
            x += "</ol></pre><html>";
            textInfo.setText(x);
        } else {
            textInfo.setText("<html><pre>ROUND: " + roundCounter + "\nTURN: " + currentPlayer.getPlayerColorName() + "</pre><html>");
        }

    }

    public void setDiceView(int diceResult) {
        kostka.setIcon(Dice.diceViews[diceResult-1]);
    }


    public void generatePopup() {
        String x = "Koniec gry\n";
        int i = 1;
        for (PossibleColors colors : winnersTable) {
            x += String.valueOf(i);
            x += ". ";
            x += colors;
            x += "\n";
            i++;
        }
        Frame frame = new Frame();
        int input = JOptionPane.showConfirmDialog(frame,
                x, "Koniec gry", JOptionPane.DEFAULT_OPTION);
        if (input == 0) {
            System.exit(0);
        }
    }

    private void setFrameParameters() {
        infoPanel.setBounds(800, 0, PANEL_DIMENSIONS, PANEL_DIMENSIONS * 2);
        infoPanel.setBackground(currentPlayer.getPlayerColor());

        textInfo.setPreferredSize(new Dimension(PANEL_DIMENSIONS, PANEL_DIMENSIONS));
        textInfo.setBackground(new Color(255, 255, 255));
        textInfo.setForeground(new Color(255, 255, 255));
        textInfo.setHorizontalAlignment(JLabel.CENTER);
        textInfo.setFont(new Font("Arial", Font.BOLD, 40));
        textInfo.setBounds(0, 0, 50, 50);

        kostka.setForeground(Color.white);
        kostka.setPreferredSize(new Dimension(DICE_SIZE, DICE_SIZE));
        kostka.setBounds(170, 50, DICE_SIZE, DICE_SIZE);
        dicePlaceholder.add(kostka);
        setDiceView(1);

        dicePlaceholder.setPreferredSize(new Dimension(PANEL_DIMENSIONS, PANEL_DIMENSIONS / 2));
        dicePlaceholder.setHorizontalAlignment(JLabel.CENTER);

        infoPanel.add(dicePlaceholder);
        infoPanel.add(textInfo, BorderLayout.CENTER);

        setInformation();
        setDiceView(1);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(MAP_WIDTH, MAP_HEIGHT);
        this.setLayout(null);
        this.setVisible(true);
        this.setTitle("Gra chińczyk");

        this.add(infoPanel, BorderLayout.WEST);
        this.add(board);
        this.repaint();
    }
}