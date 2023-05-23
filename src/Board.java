import java.util.HashMap;
import java.util.Random;

public class Board {
    //mapa (bez domków) sklada sie z 56 pól, czyli 4 czesci po 14 pol

    private final HashMap<Integer, SpecialFieldTypes> specialFields = new HashMap<>();
    public Board() {
        Random random = new Random();
        SpecialFieldTypes specialField = SpecialFieldTypes.values()[random.nextInt(7)];
        int lowerLimit = 1, upperLimit = 14;

        // losowanie dla jednej z 4 czesci po trzy pola specjalne
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
    public SpecialFieldTypes getSpecialField(int coordinate) {
        return specialFields.get(coordinate);
    }
}
