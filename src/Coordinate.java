public class Coordinate {
    private final int x;
    private final int y;
    private boolean occupied;

    public Coordinate(int x, int y, boolean flag) {
        this.x = x;
        this.y = y;
        this.occupied = flag;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean free) {
        this.occupied = free;
    }
}
