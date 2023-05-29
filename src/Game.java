import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private final Board board = new Board();
    public Game() throws HeadlessException {
        setFrameParameters();
        //TODO tymczasowy pionek
        board.setPawn(new Pawn(new ImageIcon("assets/pawn.png")), 0);
    }

    private void setFrameParameters() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 800);
        this.setLayout(null);
        this.setVisible(true);
        this.setTitle("Gra chińczyk");
        this.add(board);
        this.repaint();
    }
}
