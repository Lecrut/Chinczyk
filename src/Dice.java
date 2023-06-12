import javax.swing.*;
import java.awt.*;

public class Dice extends JLabel {

    public static final ImageIcon[] diceViews = new ImageIcon[6];

    private int diceResult;

    private ImageIcon currentImage = new ImageIcon();

    Dice() {
        configureDiceViews();
    }

    public int getDiceResult() {
        return diceResult;
    }

    public void configureDiceViews() {
        diceViews[0] = new ImageIcon("./assets/Dice/DiceImage1.png");
        diceViews[1] = new ImageIcon("./assets/Dice/DiceImage2.png");
        diceViews[2] = new ImageIcon("./assets/Dice/DiceImage3.png");
        diceViews[3] = new ImageIcon("./assets/Dice/DiceImage4.png");
        diceViews[4] = new ImageIcon("./assets/Dice/DiceImage5.png");
        diceViews[5] = new ImageIcon("./assets/Dice/DiceImage6.png");
    }

    public void diceThrow() {
        int min = 1;
        int max = 6;
        diceResult = (int) Math.floor(Math.random() * (max - min + 1) + min);
        currentImage = diceViews[diceResult - 1];

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(currentImage.getImage(), Board.BEGIN_COORDINATE, Board.BEGIN_COORDINATE, getWidth(), getHeight(), null);
    }
}
