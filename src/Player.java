import javax.swing.*;
import java.awt.*;

public class Player {
    private final Color playerColor;
    private Pawn[] pawns = new Pawn[4];
    private int firstField;
    private int lastField;
    private Statuses status;
    public final static int MAX_DICE_RESULT = 6;
    public final static int MAX_MOVE_COUNT = 3;
    public final static int PAWNS_AMOUNT = 4;
    private int luckCounter = 0;
    private final PossibleColors playerColorName;

    Player(PossibleColors color, int newFirstField, int newLastField) {
        playerColorName = color;
        playerColor = new Color(color.getColorName());
        firstField = newFirstField;
        lastField = newLastField;
        status = Statuses.FREE;
        for (int i = 0; i < PAWNS_AMOUNT; i++) {
            //TODO zmienic na odpowiedni kolor ikony
            pawns[i] = new Pawn(new ImageIcon("./assets/pawn.png"));
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

    private Pawn choosePawn() {

        //TODO: TU TRZEBA DODAC WYBOR PIONKA ZA POMOCĄ GUI, SPRAWDZENIE CZY DANY PIONEK MOZE BYC RUSZONY
        return pawns[0];
    }

    private boolean leaveHomeCheck() {
        for (int i = 0; i < 4; i++) {
            if (pawns[i].getStatus() == PawnStatuses.IN_BASE) {
                return true;
            }
        }
        return false;
    }

    public void playerMove() {
        boolean nextTurn = false;

        int diceResult = diceThrow();

        if (diceResult == MAX_DICE_RESULT) {
            luckCounter++;
            if (luckCounter != MAX_MOVE_COUNT) {
                nextTurn = true;
            }

            if (nextTurn) {
                if (leaveHomeCheck()) {
                    movePawnToGame(choosePawn());
                }
                playerMove();
            }
            luckCounter = 0;
        } else {
            Pawn chosenPawn = choosePawn();
            chosenPawn.move(diceResult);
        }
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
}
