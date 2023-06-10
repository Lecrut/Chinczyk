public enum PossibleColors {
    GREEN(0x00b000),
    RED(0xff5f24),
    YELLOW(0xcccc02),
    BLUE(0x84a5ff);

    private final int colorName;

    PossibleColors(int colorName) {
        this.colorName = colorName;
    }

    public int getColorName() {
        return colorName;
    }
}

