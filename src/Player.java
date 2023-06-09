import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CountDownLatch;

public class Player {
    private final Color playerColor;
    private Pawn[] pawns = new Pawn[4];
    private int firstField;
    private int lastField;
    private Statuses status;
    public final static int MAX_DICE_RESULT = 6;
    public final static int MAX_MOVE_COUNT = 3;
    public final static int PAWNS_AMOUNT = 4;
    private final static int AROUND_ROUTE_LENGTH = 56;
    private final static int PAWN_ROUTE = 61;
    private final PossibleColors playerColorName;

    Player(PossibleColors color, int newFirstField, int newLastField) {
        playerColorName = color;
        playerColor = new Color(color.getColorName());
        firstField = newFirstField;
        lastField = newLastField;
        status = Statuses.FREE;
        for (int i = 0; i < PAWNS_AMOUNT; i++) {
            switch (color) {
                case BLUE -> pawns[i] = new Pawn(new ImageIcon("./assets/Pawns/PawnBlue.png"));
                case GREEN -> pawns[i] = new Pawn(new ImageIcon("./assets/Pawns/PawnGreen.png"));
                case RED -> pawns[i] = new Pawn(new ImageIcon("./assets/Pawns/PawnRed.png"));
                case YELLOW -> pawns[i] = new Pawn(new ImageIcon("./assets/Pawns/PawnYellow.png"));
            }
        }
    }

    private int diceThrow() {
        int min = 1;
        int max = 6;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    private void movePawnToGame(Pawn pawn) {
        pawn.setStatusGame(PawnStatuses.IN_GAME);
    }

    private Pawn choosePawn(int diceResult) {
        final CountDownLatch latch = new CountDownLatch(1);
        final Pawn[] clickedPawn = new Pawn[1];
        boolean allPawnsInaccessible = true;

        for (Pawn pawn : pawns) {
            if (checkPawn(pawn, diceResult)) {
                allPawnsInaccessible = false;
                pawn.setBorder(BorderFactory.createLineBorder(Color.white, 2, true));
                pawn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        clickedPawn[0] = (Pawn) e.getSource();

                        latch.countDown(); // Zwalnianie CountDownLatch po kliknięciu
                    }
                });
                pawn.setListening(true);
            }
        }

        if (allPawnsInaccessible) {
            latch.countDown();
            return null;
        }

        try {
            latch.await(); // Oczekiwanie na kliknięcie
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Usuwanie listenerów
        for (Pawn pawn : pawns) {
            if (pawn.isListening()) {
                pawn.removeMouseListener(pawn.getMouseListeners()[0]);
                pawn.setBorder(null);
                pawn.setListening(false);
            }
        }

        return clickedPawn[0];
    }


    private boolean checkPawn(Pawn pawn, int diceResult) {
        if (diceResult == MAX_DICE_RESULT && pawn.getStatus() == PawnStatuses.IN_BASE) {
            return true;
        }
        return pawn.validateMove(diceResult) && pawn.getStatus() == PawnStatuses.IN_GAME;
    }

    private boolean leaveHomeCheck() {
        for (int i = 0; i < 4; i++) {
            if (pawns[i].getStatus() == PawnStatuses.IN_BASE) {
                return true;
            }
        }
        return false;
    }

    public boolean playerMove(Board board, int luckCounter) {
        boolean nextTurn = false;

        int diceResult = diceThrow();
        System.out.printf("%d %s\n", diceResult, this.getPlayerColorName());

        if (diceResult == MAX_DICE_RESULT) {
            if (luckCounter != MAX_MOVE_COUNT) {
                nextTurn = true;
            }
            Pawn chosenPawn = choosePawn(diceResult);
            if (chosenPawn == null) {
                return false;
            }
            if (chosenPawn.getStatus() == PawnStatuses.IN_BASE) {
                movePawnToGame(chosenPawn);
                for ( int i = 0 ; i < 4; i++) {
                    if ( chosenPawn.equals(pawns[i]) ) {
                        board.getStartBase().get(playerColorName).get(i).setOccupied(false);
                    }
                }
                board.setPawn(chosenPawn, firstField);
            } else {
                chosenPawn.move(diceResult);
                if (chosenPawn.getPosition() < AROUND_ROUTE_LENGTH)
                    board.setPawn(chosenPawn, (chosenPawn.getPosition() + firstField) % AROUND_ROUTE_LENGTH);
                else if (chosenPawn.getPosition() == PAWN_ROUTE)
                    board.setPawnEndBase(chosenPawn, getPlayerColorName());
                else
                    board.setPawnEndPath(chosenPawn, getPlayerColorName(), chosenPawn.getPosition() - AROUND_ROUTE_LENGTH);
            }
            return nextTurn;
        } else {
            Pawn chosenPawn = choosePawn(diceResult);
            if (chosenPawn == null) {
                return false;
            }
            chosenPawn.move(diceResult);
            if (chosenPawn.getPosition() < AROUND_ROUTE_LENGTH)
                board.setPawn(chosenPawn, (chosenPawn.getPosition() + firstField) % AROUND_ROUTE_LENGTH);
            else if (chosenPawn.getPosition() == PAWN_ROUTE - 1)
                board.setPawnEndBase(chosenPawn, getPlayerColorName());
            else
                board.setPawnEndPath(chosenPawn, getPlayerColorName(), chosenPawn.getPosition() - AROUND_ROUTE_LENGTH);
        }
        return false;
    }

    public void setStatusWinner() {
        status = Statuses.WINNER;
    }

    public int getFirstField() {
        return firstField;
    }

    public int getLastField() {
        return lastField;
    }

    public void setFirstField(int coords) {
        firstField = coords;
    }

    public void setLastField(int coords) {
        lastField = coords;
    }

    public Pawn[] getPawns() {
        return pawns;
    }

    public void setPawns(Pawn[] pawns) {
        this.pawns = pawns;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public PossibleColors getPlayerColorName() {
        return playerColorName;
    }

    public Statuses getStatus() {
        return status;
    }
}
