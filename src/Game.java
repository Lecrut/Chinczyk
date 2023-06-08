import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends JFrame  {
    private final int roundCounter = 0;
    private final Player[] players;
    private Player currentPlayer;
    private final Board board;
    private final JPanel infoPanel = new JPanel();
    private final JLabel textInfo = new JLabel();
    private final JLabel dicePlaceholder = new JLabel();
    private final ImageIcon[] diceViews = new ImageIcon[6];
    private final JButton diceView = new JButton();

    private final ArrayList<PossibleColors> winnersTable = new ArrayList<>();

    private final static int AROUND_ROUTE_LENGTH = 56;
    private final static int PAWN_ROUTE = 61;
    private final static int MAP_WIDTH = 1200;
    private final static int MAP_HEIGHT = 800;
    private final static int DISTANCE_BETWEEN_PLAYERS = 14;
    public final static int FINAL_PATH = PAWN_ROUTE - AROUND_ROUTE_LENGTH - 1;
    public final static int PANEL_DIMENSTIONS = 400;

    public Game(int playersNumber) throws HeadlessException {
        players = new Player[playersNumber];
        board = new Board();

        //TODO tymczasowy pionek
        board.setPawn(new Pawn(new ImageIcon("./assets/Pawns/PawnBlue.png")), 0);
        if (playersNumber >= 1) {
            players[0] = new Player(PossibleColors.GREEN, 0, 4 * DISTANCE_BETWEEN_PLAYERS);
            for (Pawn pawn : players[0].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.GREEN);
            }
        }
        if (playersNumber >= 2) {
            players[1] = new Player(PossibleColors.BLUE, 2 * DISTANCE_BETWEEN_PLAYERS, (2 * DISTANCE_BETWEEN_PLAYERS) - 1);
            for (Pawn pawn : players[1].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.BLUE);
            }
        }
        if (playersNumber >= 3) {
            players[2] = new Player(PossibleColors.RED, DISTANCE_BETWEEN_PLAYERS, DISTANCE_BETWEEN_PLAYERS - 1);
            for (Pawn pawn : players[2].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.RED);
            }
        }
        if (playersNumber == 4) {
            players[3] = new Player(PossibleColors.YELLOW, 3 * DISTANCE_BETWEEN_PLAYERS, (3 * DISTANCE_BETWEEN_PLAYERS) - 1);
            for (Pawn pawn : players[3].getPawns()) {
                board.setPawnStartBase(pawn, PossibleColors.YELLOW);
            }
        }
        currentPlayer = players[1];

        setFrameParameters();
    }

    public void round() {
        for (Player player : players) {
            player.playerMove();
            checkBoard(player);
        }
    }

    private void checkSpecialFields(Player player) {
        for (Pawn pawn : player.getPawns()) {
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
            int x = (int) (Math.random() * AROUND_ROUTE_LENGTH);
            if (board.getSpecialField(x) == null) {
                return x;
            }
        }
    }

    private void triggerSpecialFields(Pawn pawn, SpecialFieldTypes fieldType) {
        if (fieldType == null)
            return;
        switch (fieldType) {
            case FORWARD_1:
                pawn.move(1);
            case FORWARD_2:
                pawn.move(2);
            case FORWARD_3:
                pawn.move(3);
            case BACKWARD_1:
                pawn.move(-1);
            case BACKWARD_2:
                pawn.move(-2);
            case BACKWARD_3:
                pawn.move(-3);
            case TELEPORT:
                pawn.setPosition(teleportPawn());
        }
    }

    private void checkCollisionPlayer(Player player1, Player player2) {
        if (player1.equals(player2)) {
            return;
        }
        for (Pawn pawn2 : player2.getPawns()) {
            if (pawn2.getStatus() == PawnStatuses.IN_GAME) {
                for (Pawn pawn1 : player1.getPawns()) {
                    if (pawn1.getStatus() == PawnStatuses.IN_GAME) {
                        if ((pawn1.getPosition() + player1.getFirstField()) % PAWN_ROUTE == (pawn2.getPosition() + player2.getFirstField()) % PAWN_ROUTE) {
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
            for (Pawn pawn : player.getPawns()) {
                if (pawn.getPosition() == PAWN_ROUTE) {
                    pawn.setStatusGame(PawnStatuses.IN_END);
                    board.setPawnEndBase(pawn, player.getPlayerColorName());
                }
            }
            if (checkWinnigPlayer(player)) {
                winnersTable.add(player.getPlayerColorName());
                player.setStatusWinner();
            }
        }
    }

    public boolean checkWinnigPlayer(Player player) {
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

    public void setInformation(Player pl) {
        if (checkWinners()) {
            String x = "<html><pre>ROUND: " + roundCounter + "\nTURN: " + pl.getPlayerColorName() + "\n<ol>";
            for (PossibleColors color : winnersTable) {
                x += "<li>" + color + "</li>";
            }
            x += "</ol></pre><html>";
            textInfo.setText(x);
        } else {
            textInfo.setText("<html><pre>ROUND: " + roundCounter + "\nTURN: " + pl.getPlayerColorName() + "</pre><html>");
        }

    }

    public void settingDiceView (int diceResult){
        diceView.setIcon(diceViews[diceResult-1]);
    }

    public void setDiceViews (){
        diceViews[0] = new ImageIcon("./assets/Dice/DiceImage1.png");
        diceViews[1] = new ImageIcon("./assets/Dice/DiceImage2.png");
        diceViews[2] = new ImageIcon("./assets/Dice/DiceImage3.png");
        diceViews[3] = new ImageIcon("./assets/Dice/DiceImage4.png");
        diceViews[4] = new ImageIcon("./assets/Dice/DiceImage5.png");
        diceViews[5] = new ImageIcon("./assets/Dice/DiceImage6.png");

        diceView.setForeground(Color.white);
        diceView.setPreferredSize(new Dimension(80,80));
        diceView.setBounds(170,50,80,80);
        dicePlaceholder.add(diceView);

        settingDiceView(5); // widok poczatkowy przed pierwszym rzutem
    }

    private void setFrameParameters() {
        infoPanel.setBounds(800, 0, PANEL_DIMENSTIONS, PANEL_DIMENSTIONS*2);
        infoPanel.setBackground(currentPlayer.getPlayerColor());

        textInfo.setPreferredSize(new Dimension(PANEL_DIMENSTIONS,PANEL_DIMENSTIONS));
        textInfo.setBackground(new Color(255, 255, 255));
        textInfo.setForeground(new Color(255, 255, 255));
        textInfo.setHorizontalAlignment(JLabel.CENTER);
        textInfo.setFont(new Font("Arial", Font.BOLD, 40));
        textInfo.setBounds(0,0,50,50);

        dicePlaceholder.setPreferredSize(new Dimension(PANEL_DIMENSTIONS,PANEL_DIMENSTIONS/2));
        dicePlaceholder.setHorizontalAlignment(JLabel.CENTER);

        infoPanel.add(dicePlaceholder);
        infoPanel.add(textInfo, BorderLayout.CENTER);

        setInformation(currentPlayer);
        setDiceViews();

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