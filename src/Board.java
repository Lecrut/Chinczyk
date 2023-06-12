import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board extends JPanel {
    private final Image image = new ImageIcon("./assets/Map.png").getImage();
    //mapa (bez domków) sklada sie z 56 pól, czyli 4 czesci po 14 pol
    private final HashMap<Integer, SpecialFieldTypes> specialFields = new HashMap<>();

    private final static int MAP_PARTS_NUMBER = 4;
    private final static int SPECIALFIELDS_PER_PART_NUMBER = 3;
    private final static int FIRST_COORDINATE_IN_PART = 1;
    private final static int LAST_COORDINATE_IN_PART = 14;
    private final static int PANEL_WIDTH = 790;
    private final static int PANEL_HEIGHT = 765;
    public final static int BEGIN_COORDINATE = 0;
    private static final int MAP_OFFSET = 50;
    private final ArrayList<Coordinate> fields = new ArrayList<>();
    private final HashMap<PossibleColors, ArrayList<Coordinate>> startBase = new HashMap<>();
    private final HashMap<PossibleColors, ArrayList<Coordinate>> endBase = new HashMap<>();
    private final HashMap<PossibleColors, ArrayList<Coordinate>> endPath = new HashMap<>();

    public Board() {
        setGuiParameters();
        // losowanie dla jednej z 4 czesci po trzy pola specjalne
        generateSpecialFields();
        generateArrayList();
        for (PossibleColors color : PossibleColors.values()) {
            switch (color) {
                case RED -> {
                    generateStartBaseCoordinates(687, 668, color);
                    generateEndBaseCoordinates(566, 549, color);
                    generatePathCoordinates(381, 667, false, -MAP_OFFSET, color);
                }
                case BLUE -> {
                    generateStartBaseCoordinates(20, 20, color);
                    generateEndBaseCoordinates(145, 140, color);
                    generatePathCoordinates(381, 70, false, MAP_OFFSET, color);
                }
                case GREEN -> {
                    generateStartBaseCoordinates(688, 20, color);
                    generateEndBaseCoordinates(567, 140, color);
                    generatePathCoordinates(688, 371, true, -MAP_OFFSET - 1, color);
                }
                case YELLOW -> {
                    generateStartBaseCoordinates(20, 668, color);
                    generateEndBaseCoordinates(145, 549, color);
                    generatePathCoordinates(73, 371, true, MAP_OFFSET + 1, color);
                }
            }
        }
    }

    public HashMap<PossibleColors, ArrayList<Coordinate>> getStartBase() {
        return startBase;
    }

    public Coordinate getField(int fieldNumber) {
        return fields.get(fieldNumber);
    }

    private void generateArrayList() {
        int beginX = 431;
        int beginY = 19;
        fields.add(new Coordinate(beginX, beginY, false));
        beginX += MAP_OFFSET;
        for (int i = 0; i < 6; i++) {
            fields.add(new Coordinate(beginX, beginY, false));
            beginY += MAP_OFFSET;
        }
        beginY -= MAP_OFFSET;
        for (int i = 0; i < 5; i++) {
            beginX += MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 4; i++) {
            beginY += MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginX -= MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        beginX -= 2;
        for (int i = 0; i < 5; i++) {
            beginY += MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 4; i++) {
            beginX -= MAP_OFFSET+1;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        beginX -= 4;
        for (int i = 0; i < 5; i++) {
            beginY -= MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginX -= MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        beginX -= 3;
        for (int i = 0; i < 4; i++) {
            beginY -= MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 5; i++) {
            beginX += MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        beginX += 2;
        for (int i = 0; i < 5; i++) {
            beginY -= MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
        for (int i = 0; i < 2; i++) {
            beginX += MAP_OFFSET;
            fields.add(new Coordinate(beginX, beginY, false));
        }
    }

    private void generateStartBaseCoordinates(int x, int y, PossibleColors color) {
        startBase.put(color, createArray(x, y));
    }

    private void generateEndBaseCoordinates(int x, int y, PossibleColors color) {
        endBase.put(color, createArray(x, y));
    }

    private ArrayList<Coordinate> createArray(int x, int y) {
        ArrayList<Coordinate> temp = new ArrayList<>();
        temp.add(new Coordinate(x, y, false));
        temp.add(new Coordinate(x, y + MAP_OFFSET, false));
        temp.add(new Coordinate(x + MAP_OFFSET, y, false));
        temp.add(new Coordinate(x + MAP_OFFSET, y + MAP_OFFSET, false));
        return temp;
    }

    private void generatePathCoordinates(int x, int y, boolean horizontally, int offset, PossibleColors color) {
        ArrayList<Coordinate> temp = new ArrayList<>();
        if (!horizontally) {
            for (int i = 0; i < Game.FINAL_PATH; i++) {
                temp.add(new Coordinate(x, y, false));
                y += offset;
            }
        } else {
            for (int i = 0; i < Game.FINAL_PATH; i++) {
                temp.add(new Coordinate(x, y, false));
                x += offset;
            }
        }
        endPath.put(color, temp);
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
        double scaleX = image.getWidth(null) / (double) getWidth();
        double scaleY = image.getHeight(null) / (double) getHeight();
        //Coordinate field = endPath.get(PossibleColors.RED).get(2);
        Coordinate field = fields.get(index);
        pawn.setBounds((int) (field.getX() * scaleX), (int) (field.getY() * scaleY), Pawn.PAWN_WIDTH, Pawn.PAWN_HEIGHT);
        this.add(pawn);
        this.repaint();
    }

    private void setPawnBase(Pawn pawn, PossibleColors color, HashMap<PossibleColors, ArrayList<Coordinate>> base) {
        double scaleX = image.getWidth(null) / (double) getWidth();
        double scaleY = image.getHeight(null) / (double) getHeight();
        for (Coordinate field : base.get(color)) {
            if (!field.isOccupied()) {
                pawn.setBounds((int) (field.getX() * scaleX), (int) (field.getY() * scaleY), Pawn.PAWN_WIDTH, Pawn.PAWN_HEIGHT);
                field.setOccupied(true);
                this.add(pawn);
                this.repaint();
                return;
            }
        }
    }

    public void setPawnEndPath(Pawn pawn, PossibleColors color, int index) {
        pawn.setStatusGame(PawnStatuses.IN_END_PATH);
        if (index < 0 || index > 3)
            return;
        double scaleX = image.getWidth(null) / (double) getWidth();
        double scaleY = image.getHeight(null) / (double) getHeight();
        Coordinate field = endPath.get(color).get(index);
        pawn.setBounds((int) (field.getX() * scaleX), (int) (field.getY() * scaleY), Pawn.PAWN_WIDTH, Pawn.PAWN_HEIGHT);
        this.add(pawn);
        this.repaint();
    }

    public void setPawnStartBase(Pawn pawn, PossibleColors color) {
        setPawnBase(pawn, color, startBase);
    }

    public void setPawnEndBase(Pawn pawn, PossibleColors color) {
        pawn.setStatusGame(PawnStatuses.IN_END);
        setPawnBase(pawn, color, endBase);
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

        g2D.drawImage(image, BEGIN_COORDINATE, BEGIN_COORDINATE, null);
        specialFields.forEach(
                (key, value) -> {
                    switch (value) {
                        case FORWARD_1 ->
                                g2D.drawImage(new ImageIcon("./assets/Fields/Forward_1.png").getImage(), fields.get(key).getX()-12, fields.get(key).getY()-15, null);
                        case FORWARD_2 ->
                                g2D.drawImage(new ImageIcon("./assets/Fields/Forward_2.png").getImage(), fields.get(key).getX()-12, fields.get(key).getY()-15, null);
                        case FORWARD_3 ->
                                g2D.drawImage(new ImageIcon("./assets/Fields/Forward_3.png").getImage(), fields.get(key).getX()-12, fields.get(key).getY()-15, null);
                        case BACKWARD_1 ->
                                g2D.drawImage(new ImageIcon("./assets/Fields/Backward_1.png").getImage(), fields.get(key).getX()-12, fields.get(key).getY()-15, null);
                        case BACKWARD_2 ->
                                g2D.drawImage(new ImageIcon("./assets/Fields/Backward_2.png").getImage(), fields.get(key).getX()-12, fields.get(key).getY()-15, null);
                        case BACKWARD_3 ->
                                g2D.drawImage(new ImageIcon("./assets/Fields/Backward_3.png").getImage(), fields.get(key).getX()-12, fields.get(key).getY()-15, null);
                        case TELEPORT ->
                                g2D.drawImage(new ImageIcon("./assets/Fields/TeleportField.png").getImage(), fields.get(key).getX()-12, fields.get(key).getY()-15, null);
                        case BLOCKING ->
                                g2D.drawImage(new ImageIcon("./assets/Fields/BlockingField.png").getImage(), fields.get(key).getX()-12, fields.get(key).getY()-15, null);
                    }
                }
        );
    }
}
