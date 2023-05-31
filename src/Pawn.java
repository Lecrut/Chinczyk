import javax.swing.*;
import java.awt.*;

public class Pawn extends JLabel {
    private final ImageIcon icon;
    private int position;
    private PawnStatuses status;

    public final static int PAWN_WIDTH = 150;
    public final static int PAWN_HEIGHT = 150;

    public Pawn(ImageIcon icon) {
        this.icon = icon;
        setGuiParameters();
        position = 0;
        status = PawnStatuses.IN_BASE;
    }

    public void move(int x) {
        if ( validateMove(x))
            position += x;
    }

    private void setGuiParameters() {
        setOpaque(false);
    }
    private boolean validateMove(int x) {
        return position + x >= 0 && position + x <= 61;
    }
    public void setStatusGame (PawnStatuses pawnStatus) {
        status = pawnStatus;
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
