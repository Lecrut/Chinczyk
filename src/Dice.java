import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dice implements ActionListener {

    public static final ImageIcon[] diceViews = new ImageIcon[6];

    private static int diceResult;

    private static boolean drawFlag;

    Dice() {
        configureDiceViews();
        Game.diceView.addActionListener(this);
        setDrawFlag(true);
        System.out.println(drawFlag);
    }

    public static int getDiceResult() {
        return diceResult;
    }

    public static void setDrawFlag(boolean drawFlag) {
        System.out.println("zmiana na " + drawFlag + "\n");
        Dice.drawFlag = drawFlag;
    }

    public void configureDiceViews() {
        diceViews[0] = new ImageIcon("./assets/Dice/DiceImage1.png");
        diceViews[1] = new ImageIcon("./assets/Dice/DiceImage2.png");
        diceViews[2] = new ImageIcon("./assets/Dice/DiceImage3.png");
        diceViews[3] = new ImageIcon("./assets/Dice/DiceImage4.png");
        diceViews[4] = new ImageIcon("./assets/Dice/DiceImage5.png");
        diceViews[5] = new ImageIcon("./assets/Dice/DiceImage6.png");
    }

    public static int diceThrow() {
        int min = 1;
        int max = 6;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public void setDiceView(int diceResult) {
        Game.diceView.setIcon(diceViews[diceResult-1]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Game.diceView) {
            if(drawFlag){
                diceResult = diceThrow();
                setDiceView(diceResult);
                Dice.setDrawFlag(false);
            }
        }
    }
}
