import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Board extends JPanel {
    private final Image image = new ImageIcon("../assets/Map.png").getImage();
    //mapa (bez domków) sklada sie z 56 pól, czyli 4 czesci po 14 pol
    private final HashMap<Integer, SpecialFieldTypes> specialFields = new HashMap<>();

    private final static int MAP_PARTS_NUMBER = 4;
    private final static int SPECIALFIELDS_PER_PART_NUMBER = 3;
    private final static int FIRST_COORDINATE_IN_PART = 1;
    private final static int LAST_COORDINATE_IN_PART = 14;
    private final static int PANEL_WIDTH = 790;
    private final static int PANEL_HEIGHT = 765;
    public final static int BEGIN_COORDINATE = 0;

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
        SpecialFieldTypes specialField = SpecialFieldTypes.values()[random.nextInt(SpecialFieldTypes.SPECIAL_FIELDS_COUNT)];
        int lowerLimit = FIRST_COORDINATE_IN_PART, upperLimit = LAST_COORDINATE_IN_PART;
        for (int i = 0; i < MAP_PARTS_NUMBER; i++) {
            for (int j = 0; j < SPECIALFIELDS_PER_PART_NUMBER; j++) {
                int nextRand = random.nextInt(upperLimit - lowerLimit) + lowerLimit;
                while (specialFields.containsKey(nextRand)) {
                    nextRand = random.nextInt(upperLimit - lowerLimit) + lowerLimit;
                }
                specialFields.put(nextRand, specialField);
                specialField = specialField.next();
            }
            lowerLimit += LAST_COORDINATE_IN_PART;
            upperLimit += LAST_COORDINATE_IN_PART;
        }
    }

    public void setPawn(Pawn pawn, int index) {
        //TODO do zmiany, wykorzystanie index do pobrania odpowiednich koordynat z array listy w tej klasie
        pawn.setBounds(110, 100, Pawn.PAWN_WIDTH, Pawn.PAWN_HEIGHT);
        this.add(pawn);
        this.repaint();
    }

    private void setGuiParameters() {
        this.setOpaque(true);
        this.setBounds(BEGIN_COORDINATE, BEGIN_COORDINATE, PANEL_WIDTH, PANEL_HEIGHT);
        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        double scaleX = (double) getWidth() / image.getWidth(null);
        double scaleY = (double) getHeight() / image.getHeight(null);
        g2D.scale(scaleX, scaleY);

        g2D.drawImage(image, BEGIN_COORDINATE, BEGIN_COORDINATE, null);
    }
}
