package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.client.View.CLIView;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.controller.logic.GamePhase;

import java.io.IOException;

public class Console {

    private enum ActionBookMark{moveStudents, placeMotherNature, chooseCloud}

    private Phase currentPhase = null;
    private ActionBookMark currActionBookMark = ActionBookMark.moveStudents;

    private int assistantCardPlayed = -1;
    private Integer characterPlayed = null;
    private Integer studentToMove = null;
    private Integer pawnColour = null;
    private Integer pawnWhere = null;
    private Integer stepsMotherNature = null;
    private Integer cloudChosen = null;
    private CharacterCardsConsole characterCardsConsole = ClientController.getInstance().getCharacterCardsConsole(); //only methods

    public void play(){
        currentPhase = ClientController.getInstance().getGameStatePojo().getCurrentPhase();

        if(ClientController.getInstance().isDisconnected()) return;
        switch (currentPhase){
            case PIANIFICATION:
                playPianification();
                break;
            case ACTION:
                playAction();
                break;
        }
    }

    private void playPianification(){
        ClientController clientController = ClientController.getInstance();
        NetworkHandler networkHandler = clientController.getNetworkHandler();

        Message receivedMessage;
        GameMessage gameMessage;
        boolean moveAccepted = false;

        do{
            clientController.view.chooseAssistantCard();
            gameMessage = new GameMessage(TypeOfMove.AssistantCard, assistantCardPlayed);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();

            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    moveAccepted = true;
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                    clientController.view.showMessage(receivedMessage);
                    ErrorMessage errorMessage=(ErrorMessage)receivedMessage;
                    if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice) ||
                            errorMessage.getTypeOfError().equals(TypeOfError.AlreadyPlayed))){
                            break;
                    }
            }
        }while(moveAccepted==false);
    }

    private void playAction(){
        View view = ClientController.getInstance().view;
        GameStatePojo gameStatePojo = ClientController.getInstance().getGameStatePojo();

        int studentsToMoveTotal;
        if (gameStatePojo.getPlayers().size() == 2){
            studentsToMoveTotal = 3;
        }else{
            studentsToMoveTotal = 4;
        }

        switch (currActionBookMark){
            case moveStudents:
                studentToMove = studentsToMoveTotal;
                for(int i = 0; i < studentsToMoveTotal; i++){
                    askForCharacter();
                    studentToMove--;
                    moveStudent();
                }
                askForCharacter();
                break;
            case placeMotherNature:
                placeMotherNature();
                askForCharacter();
                currActionBookMark = ActionBookMark.chooseCloud;
                break;
            case chooseCloud:
                System.out.println("FLAG CHOOSECLOUD - CONSOLE");
                chooseCloud();
                currActionBookMark = ActionBookMark.moveStudents;
                break;
        }

    }

    public void setAssistantCardPlayed(int assistantCardPlayed) {
        this.assistantCardPlayed = assistantCardPlayed;
    }

    public void setCharacterPlayed(Integer characterPlayed) {
        this.characterPlayed = characterPlayed;
    }

    public void moveStudent(){
        ClientController clientController = ClientController.getInstance();
        NetworkHandler networkHandler = clientController.getNetworkHandler();
        View view = clientController.view;
        Message receivedMessage = null;

        boolean valid = false;
        do{
            view.moveStudent();
            GameMessage gameMessage = new PawnMovementMessage(pawnColour, pawnWhere);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!clientController.isDisconnected()){
                receivedMessage = networkHandler.getReceivedMessage();
            }

            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    valid = true;
                    if (studentToMove == 0){
                        currActionBookMark = ActionBookMark.placeMotherNature;
                    }
                    receivedMessage = networkHandler.getReceivedMessage();
                    if (receivedMessage.getMessageType() == TypeOfMessage.Update){
                        UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                        ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
                        view.showMessage(receivedMessage);
                    }else{
                        //messaggio imprevisto
                        view.showMessage(receivedMessage);
                    }
                }else{
                    //messaggio imprevisto
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice) || errorMessage.getTypeOfError().equals(TypeOfError.FullDiningRoom))){
                    break;
                }
            }
        }while(valid == false);
    }

    public void askForCharacter(){

        View view = ClientController.getInstance().view;
        NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();
        Message receivedMessage;

        boolean valid = false;
        do{
            if (ClientController.getInstance().getGameStatePojo().isExpert()){
                view.askForCharacter();
            }
            GameMessage gameMessage = new GameMessage(TypeOfMove.CharacterCard, characterPlayed);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();
            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    valid = true;
                    //se ho giocato una carta mi arriva anche un update. Qui lo gestisco
                    if (characterPlayed != null){
                        receivedMessage = networkHandler.getReceivedMessage();
                        if (receivedMessage.getMessageType() == TypeOfMessage.Update){
                            UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
                            ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
                            view.showMessage(receivedMessage);
                            //handling character card effect
                            int currentCharacterID = updateMessage.getGameState().getActiveEffect().getCharacterId();
                            characterCardsConsole.playEffect(currentCharacterID);
                        }else{
                            //messaggio imprevisto
                            view.showMessage(receivedMessage);
                        }
                    }
                }else{
                    //messaggio imprevisto
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice) || (errorMessage.getTypeOfError().equals(TypeOfError.NoMoney)))){
                    break;
                }
            }
            characterPlayed = null;
        }while(valid == false);
    }

    public void placeMotherNature(){
        View view = ClientController.getInstance().view;
        NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();
        Message receivedMessage;

        boolean valid = false;
        do{
            view.placeMotherNature();
            GameMessage gameMessage = new GameMessage(TypeOfMove.MoveMotherNature, stepsMotherNature);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(ClientController.getInstance().isDisconnected()) return;
            receivedMessage = networkHandler.getReceivedMessage();

            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    valid = true;
                }else{
                    //messaggio imprevisto
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))) {
                    break;
                }
            }
            stepsMotherNature = null;
        }while(valid == false);

        receivedMessage = networkHandler.getReceivedMessage();
        if (receivedMessage.getMessageType() == TypeOfMessage.Update){
            UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
            ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
            view.showMessage(receivedMessage);
        }else{
            //messaggio imprevisto
            view.showMessage(receivedMessage);
        }
    }

    public void chooseCloud(){
        View view = ClientController.getInstance().view;
        NetworkHandler networkHandler = ClientController.getInstance().getNetworkHandler();
        Message receivedMessage;

        boolean valid = false;
        do{
            view.chooseCloud();
            GameMessage gameMessage = new GameMessage(TypeOfMove.CloudChoice, cloudChosen);

            try {
                if(ClientController.getInstance().isDisconnected()) return;
                networkHandler.sendToServer(gameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            receivedMessage = networkHandler.getReceivedMessage();

            if (receivedMessage.getMessageType().equals(TypeOfMessage.Ack)){
                AckMessage ackMessage = (AckMessage) receivedMessage;
                if (ackMessage.getTypeOfAck().equals(TypeOfAck.CorrectMove)){
                    valid = true;
                }else{
                    //messaggio imprevisto
                    view.showMessage(receivedMessage);
                }
            }else if(receivedMessage.getMessageType().equals(TypeOfMessage.Error)){
                view.showMessage(receivedMessage);
                ErrorMessage errorMessage = (ErrorMessage)receivedMessage;
                if(!(errorMessage.getTypeOfError().equals(TypeOfError.InvalidChoice))) {
                    break;
                }
            }
            cloudChosen = null;
        }while(valid == false);


        receivedMessage = networkHandler.getReceivedMessage();
        if (receivedMessage.getMessageType() == TypeOfMessage.Update){
            UpdateMessage updateMessage = (UpdateMessage) receivedMessage;
            ClientController.getInstance().setGameStatePojo(updateMessage.getGameState());
            view.showMessage(receivedMessage);
        }else{
            //messaggio imprevisto
            view.showMessage(receivedMessage);
        }
    }

    public void setPawnColour(Integer pawnColour) {
        this.pawnColour = pawnColour;
    }

    public void setPawnWhere(Integer pawnWhere) {
        this.pawnWhere = pawnWhere;
    }

    public void setStepsMotherNature(Integer stepsMotherNature) {
        this.stepsMotherNature = stepsMotherNature;
    }

    public void setCloudChosen(Integer cloudChosen) {
        this.cloudChosen = cloudChosen;
    }
}
