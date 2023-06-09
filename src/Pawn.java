import javax.swing.*;
import java.awt.*;

public class Pawn extends JLabel {
    private final ImageIcon icon;
    private int position;
    private PawnStatuses status;
    private boolean listening = false;
    public final static int PAWN_ROUTE = 60;
    private final static int PAWN_START = 0;
    public final static int PAWN_WIDTH = 30;
    public final static int PAWN_HEIGHT = 30;

    public Pawn(ImageIcon icon) {
        this.icon = icon;
        setGuiParameters();
        position = PAWN_START;
        status = PawnStatuses.IN_BASE;
    }

    public void move(int boardFieldsToMove) {
        if (validateMove(boardFieldsToMove))
            position += boardFieldsToMove;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    private void setGuiParameters() {
        setOpaque(false);
    }

    public boolean validateMove(int boardFieldsToMove) {
        return position + boardFieldsToMove >= PAWN_START && position + boardFieldsToMove <= PAWN_ROUTE;
    }

    public void setStatusGame(PawnStatuses pawnStatus) {
        this.status = pawnStatus;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public PawnStatuses getStatus() {
        return status;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(icon.getImage(), Board.BEGIN_COORDINATE, Board.BEGIN_COORDINATE, getWidth(), getHeight(), null);
    }
}
