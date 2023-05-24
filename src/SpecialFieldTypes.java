public enum SpecialFieldTypes {
    FORWARD_1(0),
    FORWARD_2(1),
    FORWARD_3(2),
    BACKWARD_1(3),
    BACKWARD_2(4),
    BACKWARD_3(5),
    TELEPORT(6);

    private SpecialFieldTypes(int value) {
    }

    private static final SpecialFieldTypes[] vals = values();

    public SpecialFieldTypes next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
