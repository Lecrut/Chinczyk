import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    private final ArrayList<Coordinate> fields = new ArrayList<>();

    public Board() {
        setGuiParameters();
        // losowanie dla jednej z 4 czesci po trzy pola specjalne
        generateSpecialFields();
        generateArrayList();
    }

    public Coordinate getField(int fieldNumber) {
        return fields.get(fieldNumber);
    }

    private void generateArrayList() {
        int beginX = 431;
        int beginY = 19;
        int offset = 50;
        fields.add(new Coordinate(beginX, beginY, false));
        beginX += offset;
        for (int i = 0; i < 6; i++) {
            fields.add(new Coordinate(beginX, beginY, false));
            beginY += offset;
        }
        beginY -= offset;
        for (int i = 0; i < 5; i++) {
            beginX += offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 4; i++) {
            beginY += offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginX -= offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginY += offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 4; i++) {
            beginX -= offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginY -= offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginX -= offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 4; i++) {
            beginY -= offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginX += offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginY -= offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 2; i++) {
            beginX += offset;
            fields.add(new Coordinate(beginX, beginY, false));
        }
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
        Coordinate field = getField(index);
        double scaleX =  image.getWidth(null) / (double) getWidth();
        double scaleY = image.getHeight(null) / (double) getHeight();
        pawn.setBounds((int)(field.getX()*scaleX), (int)(field.getY()*scaleY), Pawn.PAWN_WIDTH, Pawn.PAWN_HEIGHT);
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
