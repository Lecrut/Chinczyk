public class Pawn {
    public int position = 0;
    public PawnStatuses status = PawnStatuses.IN_BASE;
    public void move(int x) {
        if ( x == 6  && status == PawnStatuses.IN_BASE ) {
            position += x;
            status = PawnStatuses.IN_GAME;
        }
        else if ( position + x == 61 ) {
            position = 61;
            status = PawnStatuses.IN_END;
        }
        else if ( status != PawnStatuses.IN_BASE ) {
            position += x;
        }
    }
}
