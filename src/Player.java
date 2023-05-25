import java.util.Random;

public class Player {

    private Colour colour;
    public Pawn[] pawns = new Pawn[4];
    private int firstField;
    private int lastField;
    private Statuses status = Statuses.FREE;

    private int luckCounter = 0;

    private int diceThrow(){
        int min = 1;
        int max = 6;
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }

    private void movePawnToGame(Pawn pawn){
        pawn.setStatusGame(PawnStatuses.IN_GAME);
    }

    private Pawn choosePawn(){

        //TODO: TU TRZEBA DODAC WYBOR PIONKA ZA POMOCĄ GUI, SPRAWDZENIE CZY DANY PIONEK MOZE BYC RUSZONY
        return pawns[0];
    }

    private boolean leaveHomeCheck(){
        for(int i = 0; i < 4; i++){
            if(pawns[i].getStatus() == PawnStatuses.IN_BASE){
                return true;
            }
        }
        return false;
    }
    public void playerMove(){
        boolean nextTurn = false;

        int diceResult = diceThrow();

        if(diceResult == 6){
            luckCounter++;
            if(luckCounter != 3) {
                nextTurn = true;
            }

            if(nextTurn){
                if(leaveHomeCheck()){
                    movePawnToGame(choosePawn());
                }
                playerMove();
            }
            luckCounter = 0;
        }
        else {
            Pawn chosenPawn = choosePawn();
            chosenPawn.move(diceResult);
        }
    }

    public Colour getColour() {
        return colour;
    }
    public int getFirstField() {
        return firstField;
    }
    public int getLastField(){
        return lastField;
    }

    public void setFirstField(int coords){
        firstField = coords;
    }
    public void setLastField(int coords){
        lastField = coords;
    }

    public void setColour(Colour newColor) {
        colour = newColor;
    }


}
