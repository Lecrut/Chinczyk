public class Pawn {
    private int position;
    private PawnStatuses status;

    public Pawn() {
        position = 0;
        status = PawnStatuses.IN_BASE;
    }

    public void move(int x) {
        if ( validateMove(x))
            position += x;
    }

    private boolean validateMove(int x) {
        return position + x >= 0 && position + x <= 61;
    }
    public void setStatusGame (PawnStatuses pawnStatus) {
        status = pawnStatus;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    public PawnStatuses getStatus() {
        return status;
    }
}
