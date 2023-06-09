import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Game extends JFrame {
    private int roundCounter = 1;
    private final Player[] players;
    private Player currentPlayer;
    private final Board board;
    private final JPanel infoPanel = new JPanel();
    private final JLabel textInfo = new JLabel();
    private final JLabel dicePlaceholder = new JLabel();
    private final ArrayList<PossibleColors> winnersTable = new ArrayList<>();
    private final static int AROUND_ROUTE_LENGTH = 56;
    private final static int MAP_WIDTH = 1200;
    private final static int MAP_HEIGHT = 800;
    private final static int DISTANCE_BETWEEN_PLAYERS = 14;
    public final static int FINAL_PATH = Pawn.PAWN_ROUTE - AROUND_ROUTE_LENGTH;
    public final static int PANEL_DIMENSIONS = 400;
    public final static int DICE_SIZE = 90;
    private static int PLAYER_NUMBER;

    private Dice dice;

    public Game(int playersNumber) throws HeadlessException {
        PLAYER_NUMBER = playersNumber;
        players = new Player[playersNumber];
        board = new Board();
        dice = new Dice();

        if (playersNumber >= 1) {
            players[0] = new Player(PossibleColors.BLUE, 0);
            for (Pawn pawn : players[0].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.BLUE);
            }
        }
        if (playersNumber >= 2) {
            players[1] = new Player(PossibleColors.RED, 2 * DISTANCE_BETWEEN_PLAYERS);
            for (Pawn pawn : players[1].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.RED);
            }
        }
        if (playersNumber >= 3) {
            players[2] = new Player(PossibleColors.GREEN, DISTANCE_BETWEEN_PLAYERS);
            for (Pawn pawn : players[2].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.GREEN);
            }
        }
        if (playersNumber == 4) {
            players[3] = new Player(PossibleColors.YELLOW, 3 * DISTANCE_BETWEEN_PLAYERS);
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
                setInformation(true);
                boolean nextMove = true;
                for (int i = 1; nextMove; i++) {
                    setInformation(true);
                    final CountDownLatch latch = new CountDownLatch(1);
                    dice.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            dice = (Dice) e.getSource();
                            dice.diceThrow();
                            latch.countDown(); // Zwalnianie CountDownLatch po kliknięciu
                        }
                    });
                    try {
                        latch.await(); // Oczekiwanie na kliknięcie
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dice.removeMouseListener(dice.getMouseListeners()[0]);
                    setInformation(false);
                    nextMove = player.playerMove(board, i, dice.getDiceResult());
                    checkBoard(player);
                    if (getWinnersTable().size() == PLAYER_NUMBER - 1) {
                        nextMove = false;
                    }
                    if (player.getStatus() == Statuses.WINNER) {
                        nextMove = false;
                    }
                }
            }
        }
        roundCounter++;
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
        switch (fieldType) {
            case FORWARD_1 -> player.animatedMove(1, pawn, board);
            case FORWARD_2 -> player.animatedMove(2, pawn, board);
            case FORWARD_3 -> player.animatedMove(3, pawn, board);
            case BACKWARD_1 -> player.animatedMove(-1, pawn, board);
            case BACKWARD_2 -> player.animatedMove(-2, pawn, board);
            case BACKWARD_3 -> player.animatedMove(-3, pawn, board);
            case TELEPORT -> pawn.setPosition(teleportPawn());
            case BLOCKING -> pawn.setStatusGame(PawnStatuses.IS_BLOCKED);
        }
        if (pawn.getPosition() < AROUND_ROUTE_LENGTH)
            board.setPawn(pawn, (pawn.getPosition() + player.getFirstField()) % AROUND_ROUTE_LENGTH);
        else if (pawn.getPosition() == Pawn.PAWN_ROUTE)
            board.setPawnEndBase(pawn, player.getPlayerColorName());
        else
            board.setPawnEndPath(pawn, player.getPlayerColorName(), pawn.getPosition() - AROUND_ROUTE_LENGTH);
        SpecialFieldTypes checkSF = board.getSpecialField((pawn.getPosition() + player.getFirstField()) % AROUND_ROUTE_LENGTH);
        if (checkSF == SpecialFieldTypes.BLOCKING && pawn.getStatus() != PawnStatuses.IS_BLOCKED)
            triggerSpecialFields(pawn, player, SpecialFieldTypes.BLOCKING);
        else if (checkSF == SpecialFieldTypes.TELEPORT) {
            triggerSpecialFields(pawn, player, SpecialFieldTypes.TELEPORT);
        }
    }

    private void checkCollisionPlayer(Player player1, Player player2) {
        if (player1.equals(player2)) {
            return;
        }
        for (Pawn pawn2 : player2.getPawns()) {
            if (pawn2.getStatus() == PawnStatuses.IN_GAME || pawn2.getStatus() == PawnStatuses.IS_BLOCKED) {
                for (Pawn pawn1 : player1.getPawns()) {
                    if (pawn1.getStatus() == PawnStatuses.IN_GAME || pawn1.getStatus() == PawnStatuses.IS_BLOCKED) {
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

    public static int startMenu() {
        Object[] options = {"2 graczy", "3 graczy", "4 graczy"};
        int check = JOptionPane.showOptionDialog(null, "Wybierz ilość graczy: ", "你好",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (check == 0) {
            return 2;
        } else if (check == 1) {
            return 3;
        } else {
            return 4;
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

    public void setInformation(boolean isThrow) {
        infoPanel.setBackground(currentPlayer.getPlayerColor());
        StringBuilder x = new StringBuilder("<html><pre>");
        if (isThrow) {
            x.append("Rzuć kostką\n");
        }
        x.append("Runda: ").append(roundCounter).append("\nKolej: ").append(currentPlayer.getPlayerColorName());
        if (getWinnersTable().size() > 0) {
            x.append("\n<ol>");
            for (PossibleColors color : winnersTable) {
                x.append("<li>").append(color).append("</li>");
            }
            x.append("</ol>");
        }
        x.append("</pre><html>");
        textInfo.setText(x.toString());
    }

    public void setDiceView(int diceResult) {
        dice.setIcon(Dice.diceViews[diceResult - 1]);
    }


    public void generatePopup() {
        StringBuilder x = new StringBuilder("Koniec gry\n");
        int i = 1;
        for (PossibleColors colors : winnersTable) {
            x.append(i);
            x.append(". ");
            x.append(colors);
            x.append("\n");
            i++;
        }
        Frame frame = new Frame();
        int input = JOptionPane.showConfirmDialog(frame,
                x.toString(), "Koniec gry", JOptionPane.DEFAULT_OPTION);
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

        dice.setForeground(Color.white);
        dice.setPreferredSize(new Dimension(DICE_SIZE, DICE_SIZE));
        dice.setBounds(170, 50, DICE_SIZE, DICE_SIZE);
        dicePlaceholder.add(dice);
        setDiceView(1);

        dicePlaceholder.setPreferredSize(new Dimension(PANEL_DIMENSIONS, PANEL_DIMENSIONS / 2));
        dicePlaceholder.setHorizontalAlignment(JLabel.CENTER);

        infoPanel.add(dicePlaceholder);
        infoPanel.add(textInfo, BorderLayout.CENTER);

        setInformation(false);
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