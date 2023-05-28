import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private final BackgroundPanel backgroundPanel = new BackgroundPanel();
    public Game() throws HeadlessException {
        setFrameParameters();
    }

    private void setFrameParameters() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 800);
        this.setLayout(null);
        this.setVisible(true);
        this.setTitle("Gra chińczyk");
        this.add(backgroundPanel);
        this.repaint();
    }
}
