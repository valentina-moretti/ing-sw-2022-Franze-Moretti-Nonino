package it.polimi.ingsw.Server.Controller.Characters;

import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.Network.MessageHandler;
import it.polimi.ingsw.Server.Controller.Network.Messages.ServerMessage;
import it.polimi.ingsw.Server.Controller.Network.Messages.StudentChoice;
import it.polimi.ingsw.Server.Controller.Network.Messages.TypeOfMessage;
import it.polimi.ingsw.Server.Model.ColourPawn;
import it.polimi.ingsw.Server.Model.Player;

public class Card12 extends CharacterEffect{

    private GameController gameController;
    public Card12(GameController gameController){
        this.gameController=gameController;
    }


    public void doEffect(){
        boolean valid;
        ColourPawn colourPawn;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int chosenPawn; // index of ColourPawn enumeration
        do{
            valid=false;
            ServerMessage messageToSend= new ServerMessage(gameController.getCurrentPlayer().getNickname(), TypeOfMessage.StudentChoice);
            StudentChoice receivedMessage = (StudentChoice) messageHandler.communicationWithClient(gameController, messageToSend);
            chosenPawn =receivedMessage.getColourPawn().getIndexColour();
            //chosenPawn = messageHandler.getValueCLI("choose one color pawn: ",gameController.getCurrentPlayer());
            for(ColourPawn p : ColourPawn.values()){
                if(p.getIndexColour()==chosenPawn){
                    valid=true;

                }
            }

        }while(!valid);

        colourPawn=ColourPawn.values()[chosenPawn];

        for(Player p: gameController.getGame().getPlayers()){
            if(p.getSchoolBoard().getDiningRoom().get(colourPawn)>=3){
                p.getSchoolBoard().getDiningRoom().remove(colourPawn,3);
                gameController.getGame().getStudentsBag().add(colourPawn,3);
            }
            else{
                for(ColourPawn c: ColourPawn.values()){
                    int num=p.getSchoolBoard().getDiningRoom().get(c);
                    p.getSchoolBoard().getDiningRoom().remove(colourPawn, num);
                    gameController.getGame().getStudentsBag().add(colourPawn,num);
                }
            }
        }
    }
}
