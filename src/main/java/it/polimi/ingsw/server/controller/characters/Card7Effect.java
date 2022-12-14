package it.polimi.ingsw.server.controller.characters;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameController;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.*;

public class Card7Effect extends CharacterEffect{


    /**
     * Places 6 students on this card randomly
     * @param gameController
     * @param characterState
     */
    public Card7Effect(GameController gameController, CharacterState characterState) {
        super(gameController, characterState);
        for(int i=0; i<6; i++){
            ((CharacterStateStudent)(this.characterState)).addStudent(gameController.getGame().getStudentsBag()
                    .removeRandomly());
        }
    }

    /**
     * Handles the choices of the players (util they are valid choices) about:
     * -> how many students to move (max 3)
     * -> the student on the card
     * -> the student in his entrance
     * and swaps the pawns
     */
    public void doEffect(){

        ErrorMessage errorGameMessage;
        GameMessage gameMessage;
        Message receivedMessage;

        String currPlayer= gameController.getCurrentPlayer().getNickname();

        MessageHandler messageHandler = this.gameController.getMessageHandler();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);

        boolean valid;
        int pawnCard;
        int pawnBoard;
        int count = 0; // number of movements made
        boolean end = false;

        // player chooses how many students to move
        do {
            valid=true;
            receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.NumOfMove);
            gameMessage = (GameMessage) receivedMessage;
            count = gameMessage.getValue();
            if (count<0 || count > 3) {  // max 3 movements
                valid = false;
                errorGameMessage = new ErrorMessage(TypeOfError.InvalidChoice); // index colour invalid
                playerManager.sendMessage(errorGameMessage);
            }
        }while(!valid);
        AckMessage ackMessage = new AckMessage(TypeOfAck.CorrectMove);
        playerManager.sendMessage(ackMessage);

        // now the player chooses the students
        for(int i=0; i<count; i++){

            // first chooses the student in his entrance
            do {
                valid = true;
                receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
                gameMessage = (GameMessage) receivedMessage;
                pawnBoard = gameMessage.getValue();

                valid=false;
                for (ColourPawn p : ColourPawn.values()) {
                    if (p.getIndexColour() == pawnBoard && gameController.getCurrentPlayer().getSchoolBoard().getEntrance().get(p)>=1)
                        valid = true;
                }

                if(valid==false){
                    errorGameMessage = new ErrorMessage(TypeOfError.InvalidChoice); // index colour invalid
                    playerManager.sendMessage(errorGameMessage);
                }
            }while(!valid);
            ackMessage = new AckMessage(TypeOfAck.CorrectMove);
            playerManager.sendMessage(ackMessage);

            //the player chooses the student on the board
            do {
                valid = true;
                receivedMessage = playerManager.readMessage(TypeOfMessage.Game, TypeOfMove.StudentColour);
                gameMessage = (GameMessage) receivedMessage;
                pawnCard = gameMessage.getValue();

                valid=false;
                for (ColourPawn p : ColourPawn.values()) {
                    if (p.getIndexColour() == pawnCard && ((CharacterStateStudent) (this.characterState)).getAllStudents().get(p) >= 1)
                        valid = true;
                }

                if(valid==false){
                    errorGameMessage = new ErrorMessage(TypeOfError.InvalidChoice); // index colour invalid
                    playerManager.sendMessage(errorGameMessage);
                }
            }while(!valid);
            ackMessage = new AckMessage(TypeOfAck.CorrectMove);
            playerManager.sendMessage(ackMessage);




            //swap
            //pawnCard ?? quello preso dalla carta personaggio
            //pawnBoard ?? quello preso dalla entrance
            ((CharacterStateStudent) (this.characterState)).removeStudent(ColourPawn.values()[pawnCard]);
            ((CharacterStateStudent) (this.characterState)).addStudent(ColourPawn.values()[pawnBoard]);
            gameController.getCurrentPlayer().getSchoolBoard().getEntrance().add(ColourPawn.values()[pawnCard]);
            gameController.getCurrentPlayer().getSchoolBoard().getEntrance().remove(ColourPawn.values()[pawnBoard]);
            gameController.update(); //otherwise he can't see how new pawns are placed
            ackMessage = new AckMessage(TypeOfAck.CorrectMove);
            playerManager.sendMessage(ackMessage);
        }

    }

    @Override
    public Player effectInfluence(Island island) {
        return null;
    }
}
