import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Board extends JPanel {
    private final Image image = new ImageIcon("assets/Map.png").getImage();
    //mapa (bez domków) sklada sie z 56 pól, czyli 4 czesci po 14 pol
    private final HashMap<Integer, SpecialFieldTypes> specialFields = new HashMap<>();

    public Board() {
        setGuiParameters();
        // losowanie dla jednej z 4 czesci po trzy pola specjalne
        generateSpecialFields();
    }

    public SpecialFieldTypes getSpecialField(int coordinate) {
        return specialFields.get(coordinate);
    }

    private void generateSpecialFields() {
        Random random = new Random();
        SpecialFieldTypes specialField = SpecialFieldTypes.values()[random.nextInt(7)];
        int lowerLimit = 1, upperLimit = 14;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int nextRand = random.nextInt(upperLimit - lowerLimit) + lowerLimit;
                while (specialFields.containsKey(nextRand)) {
                    nextRand = random.nextInt(upperLimit - lowerLimit) + lowerLimit;
                }
                specialFields.put(nextRand, specialField);
                specialField = specialField.next();
            }
            lowerLimit += 14;
            upperLimit += 14;
        }
    }

    public void setPawn(Pawn pawn, int index) {
        //TODO do zmiany, wykorzystanie index do pobrania odpowiednich koordynat z tablicy w tej klasie
        pawn.setBounds(110, 100, 150, 150);
        pawn.setOpaque(false);
        this.add(pawn);
        this.repaint();
    }

    private void setGuiParameters() {
        this.setOpaque(true);
        this.setBounds(0, 0, 790, 765);
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
