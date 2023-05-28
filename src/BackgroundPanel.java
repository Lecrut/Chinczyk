import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private final Image image;
    public BackgroundPanel() {
        image = new ImageIcon("assets/map.png").getImage();
        this.setOpaque(true);
        this.setBackground(Color.BLUE);
        this.setBounds(0, 0, 785, 765);
        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        double scaleX = (double) getWidth() / image.getWidth(null);
        double scaleY = (double) getHeight() / image.getHeight(null);
        g2D.scale(scaleX, scaleY);

        g2D.drawImage(image, 0, 0, null);
    }
}

