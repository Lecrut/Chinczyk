public class Coordinate {
    private final int x;
    private final int y;
    private boolean free;

    public Coordinate(int x, int y, boolean flag) {
        this.x = x;
        this.y = y;
        this.free = flag;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
