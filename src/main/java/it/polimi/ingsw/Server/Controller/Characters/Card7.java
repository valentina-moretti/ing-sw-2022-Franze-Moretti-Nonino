package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.PawnsMap;

public class Card7 extends CharacterEffect{

    private final GameController gameController;
    private PawnsMap pawns;

    public Card7(GameController gameController){
        this.gameController = gameController;
        pawns = new PawnsMap();
    }

    public void initializeCard() {
        pawns.add(gameController.getGame().getStudentsBag().removeRandomly(6));
    }

    public void doEffect(){

        PawnsMap pawnsChosen = new PawnsMap();
        PawnsMap pawnsToRemove = new PawnsMap();
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        System.out.println("The students on the card are:\n" + pawns);

        System.out.println("You can choose up to 3 Students from this card and replace them with the same amount of " +
                "Students form your entrance ( colour pawn '-1' to end):\n");
        boolean valid;
        int chosenPawn; // index of ColourPawn enumeration
        int chosenIsland; // index island
        int count = 0;
        boolean end = false;
        do{
            valid=false;
            chosenPawn = messageHandler.getValueCLI("choose one color pawn from this card: ",gameController.getCurrentPlayer());
            if (chosenPawn == -1){
                valid=true;
                end = true;
            }
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn && pawns.get(p)>=1 ){
                    valid=true;
                    count++;
                    if (count == 3){end = true;}
                    pawns.remove(ColourPawn.values()[chosenPawn]);
                    pawnsChosen.add(ColourPawn.values()[chosenPawn]);
                }
            }
        }while(!valid || (valid && end == false));

        valid = true;
        while(!valid || count > 0){
            chosenPawn = messageHandler.getValueCLI("choose one color pawn from your entrance: ",gameController.getCurrentPlayer());
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn && gameController.getCurrentPlayer().getSchoolBoard().getEntrance().get(p)>=1 ){
                    count--;
                    gameController.getCurrentPlayer().getSchoolBoard().getEntrance().remove(ColourPawn.values()[chosenPawn]);
                    pawnsToRemove.add(ColourPawn.values()[chosenPawn]);
                }else{
                    valid = false;
                }
            }
        }

        /*now pawnsChosen contains the pawns chosen from the card and pawnsToRemove contains the pawns form the entrance*/
        gameController.getCurrentPlayer().getSchoolBoard().getEntrance().add(pawnsChosen);
        pawns.add(pawnsToRemove);

        System.out.println("Your current entrance is: " + gameController.getCurrentPlayer().getSchoolBoard().getEntrance());





    }
}
