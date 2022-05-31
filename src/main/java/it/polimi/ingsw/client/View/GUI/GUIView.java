package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.CharacterCardsConsole;
import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.Console;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;

import it.polimi.ingsw.server.controller.logic.GameMode;
import it.polimi.ingsw.server.model.AssistantCard;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;


public class GUIView implements View {

    private Consumer<Boolean> nameCompleteObserver;
    private boolean isAssistantCardChosen;
    private boolean isCloudChosen;
    private int cloudChosen;


    private int assistantCardChosen; // to remove

    /**
     * when the client controller calls chooseGameMode, the client controller's attribute "gameMode" is
     * set with the value chosen in ChooseGameModeScene.
     * The semaphore is initialized with 0, so that the thread waits in chooseGameMode until someone calls
     * semaphore.release. This is done by a JavaFX thread at the end of inputChoice1/2/3/4()
     * (in  the class ChooseGameModeScene)
     */
    @Override
    public synchronized void chooseGameMode() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * when the client controller calls beginReadUsername, the client controller's attribute "nickname" is
     * set with the value chosen in ChooseNameScene.
     * The semaphore is 0, so that the thread waits in beginReadUsername until someone calls
     * semaphore.release. This is done by a JavaFX thread at the end of nameCheck()
     * (in  the class ChooseNameScene)
     */
    @Override
    public void beginReadUsername() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    synchronized public void setNameCompleteObserver(Consumer<Boolean> nameCompleteObserver)
    {
        this.nameCompleteObserver = nameCompleteObserver;
    }

    @FXML
    private ImageView AssistantCard1;

    @FXML
    private ImageView AssistantCard2;

    @FXML
    private ImageView AssistantCard3;

    @FXML
    private ImageView AssistantCard4;

    @FXML
    private ImageView AssistantCard5;

    @FXML
    private ImageView AssistantCard6;

    @FXML
    private ImageView AssistantCard7;

    @FXML
    private ImageView AssistantCard8;

    @FXML
    private ImageView AssistantCard9;

    @FXML
    private ImageView AssistantCard10;

    @FXML
    void setAssistantCardChosen1(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==1){
                valid=true;
                this.assistantCardChosen=1;
            }
        }
    }

    @FXML
    void setAssistantCardChosen2(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==2){
                valid=true;
                this.assistantCardChosen=2;
            }
        }
    }

    @FXML
    void setAssistantCardChosen3(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==3){
                valid=true;
                this.assistantCardChosen=3;
            }
        }
    }
    @FXML
    void setAssistantCardChosen4(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==4){
                valid=true;
                this.assistantCardChosen=4;
            }
        }
    }

    @FXML
    void setAssistantCardChosen5(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==5){
                valid=true;
                this.assistantCardChosen=5;
            }
        }
    }
    @FXML
    void setAssistantCardChosen6(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==6){
                valid=true;
                this.assistantCardChosen=6;
            }
        }
    }

    @FXML
    void setAssistantCardChosen7(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==7){
                valid=true;
                this.assistantCardChosen=7;
            }
        }
    }
    @FXML
    void setAssistantCardChosen8(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==8){
                valid=true;
                this.assistantCardChosen=8;
            }
        }
    }

    @FXML
    void setAssistantCardChosen9(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==9){
                valid=true;
                this.assistantCardChosen=9;
            }
        }
    }

    @FXML
    void setAssistantCardChosen10(MouseEvent event) {

        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        Set<AssistantCardPojo> assistantCardPojos = clientController.getGameStatePojo().getCurrentPlayer().getDeck();
        for (AssistantCardPojo a : assistantCardPojos) {
            if(a.getTurnOrder()==10){
                valid=true;
                this.assistantCardChosen=10;
            }
        }
    }





    @Override
    public void chooseAssistantCard() {

        ClientController clientController = ClientController.getInstance();
        String resultString;
        int result = 0;
        boolean valid = true;
        System.out.println("-----------------------------------------------------------------------------------------------------------------\n");

        System.out.println("\033[01m"+"PIANIFICATION PHASE"+"\033[0m" + "\nYou need to choose your Assistant Card. Here is your deck.\n");

        clientController.getConsole().setAssistantCardPlayed(this.assistantCardChosen);

    }

    @Override
    public void askForCharacter() {

    }

    @Override
    public void moveStudent() {

    }

    @Override
    public void placeMotherNature() {

    }

    @FXML
    private ImageView Cloud1;

    @FXML
    private ImageView Cloud2;

    @FXML
    private ImageView Cloud3;

    @FXML
    private Button blueOnCloud1;

    @FXML
    private Button blueOnCloud2;

    @FXML
    private Button blueOnCloud3;

    @FXML
    private Button greenOnCloud1;

    @FXML
    private Button greenOnCloud2;

    @FXML
    private Button greenOnCloud3;

    @FXML
    private Button redOnCloud1;

    @FXML
    private Button redOnCloud2;

    @FXML
    private Button redOnCloud3;

    @FXML
    private Button yellowOnCloud1;

    @FXML
    private Button yellowOnCloud2;

    @FXML
    private Button yellowOnCloud3;



    @FXML
    void setCloudChosen1(MouseEvent event) {
        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        CloudPojo cloud= ClientController.getInstance().getGameStatePojo().getClouds().get(0);
        if(cloud.getStudents().getPawns().get(ColourPawn.Green)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Red)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Yellow)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Blue)==0 ||
                isAssistantCardChosen==false){
            return;
        }
        else{
            isAssistantCardChosen=false;
            redOnCloud1.setId("0");
            yellowOnCloud1.setId("0");
            greenOnCloud1.setId("0");
            blueOnCloud1.setId("0");
            isCloudChosen = true;
            cloudChosen=0;
        }

    }

    @FXML
    void setCloudChosen2(MouseEvent event) {
        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        CloudPojo cloud= ClientController.getInstance().getGameStatePojo().getClouds().get(1);
        if(cloud.getStudents().getPawns().get(ColourPawn.Green)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Red)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Yellow)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Blue)==0 ||
                isAssistantCardChosen==false){
            return;
        }
        else{
            isAssistantCardChosen=false;
            redOnCloud1.setId("0");
            yellowOnCloud2.setId("0");
            greenOnCloud2.setId("0");
            blueOnCloud2.setId("0");
            isCloudChosen=true;
            cloudChosen=1;
        }

    }


    @FXML
    void setCloudChosen3(MouseEvent event) {
        ClientController clientController= ClientController.getInstance();
        boolean valid=false;
        CloudPojo cloud= ClientController.getInstance().getGameStatePojo().getClouds().get(2);
        if(cloud.getStudents().getPawns().get(ColourPawn.Green)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Red)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Yellow)==0 &&
                cloud.getStudents().getPawns().get(ColourPawn.Blue)==0 ||
                isAssistantCardChosen==false || isCloudChosen==true){
            return;
        }
        else{
            redOnCloud1.setId("0");
            yellowOnCloud1.setId("0");
            greenOnCloud1.setId("0");
            blueOnCloud1.setId("0");
            isCloudChosen=true;
            cloudChosen=2;
        }

    }


    @Override
    public synchronized void chooseCloud() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        Console console = clientController.getConsole();
        console.setCloudChosen(this.cloudChosen);
    }


    @Override

    public synchronized void showMessage(Message message) {
        if(message==null) return;

        if(message.getMessageType().equals(TypeOfMessage.Ack)){
            AckMessage ackMessage = (AckMessage) message;
            showAck(ackMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Update)){
            UpdateMessage updateMessage = (UpdateMessage) message;
            showUpdate(updateMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Game)){
            GameMessage gameMessage = (GameMessage) message;
            showMove(gameMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Error)){
            ErrorMessage errorMessage = (ErrorMessage) message;
            showError(errorMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Ping)){
            PingMessage pingMessage = (PingMessage) message;
            showPing(pingMessage);
        }
        if(message.getMessageType().equals(TypeOfMessage.Async)){
            AsyncMessage asyncMessage = (AsyncMessage) message;

            showAsync(asyncMessage);
        }
    }

    @Override
    public synchronized void showError(ErrorMessage errorMessage) {
        switch(errorMessage.getTypeOfError()) {
            case UsedName:
                //notify name already in use
                if(nameCompleteObserver!=null){
                    // accept calls the consumer in nameCompleteObserver with argument false
                    //so a message of allert will be shown
                    nameCompleteObserver.accept(false);
                }
                break;
            case UnmatchedMessages:
                System.out.println("Unexpected message received from server.\n");
                if(ClientController.getInstance().isDisconnected()==false){
                    ClientController.getInstance().setDisconnected();
                }
                break;
            case FullDiningRoom:
                System.out.println("The diningroom is full. Place the student in another place.\n");
                break;
            case InvalidChoice:
                System.out.println("The choice you made is invalid.\n");
                break;
            case AlreadyPlayed:
                System.out.println("Another player has already played this card.\n");
                break;
            case TurnError:
                System.out.println("You cannot play. Wait for your turn.\n");
                break;
            case NoMoney:
                System.out.println("You don't have enough money.\n");
                break;
            case FailedConnection:
                System.out.println("Failed connection to the server.\n");
                break;
            default:
                System.out.println("Unknown error message.\n");
        }
    }

    @Override
    public synchronized void showConnection(ConnectionMessage connectionMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringConnection = jsonConverter.fromMessageToJson(connectionMessage);
        System.out.println(connectionMessage);
    }

    @Override
    public synchronized void showAck(AckMessage ackMessage) {

        switch(ackMessage.getTypeOfAck()) {
            case CorrectConnection:
                //name ok -> change view
                GuiController.getInstance().setRunnable(()->GuiController.getInstance().switchWaitScene());
                GuiController.getInstance().change();
                break;
            case CompleteLobby:
                GuiController.getInstance().setRunnable(()->GuiController.getInstance().switchGameScene());
                GuiController.getInstance().change();
                break;
            default:
                System.out.println("Unknown ack message");
                break;
        }
    }

    @Override
    public synchronized void showUpdate(UpdateMessage updateMessage) {
        GameStatePojo gameStatePojo = updateMessage.getGameState();
        showGameState(gameStatePojo);
    }

    @Override
    public synchronized void showAsync(AsyncMessage asyncMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringAsync = jsonConverter.fromMessageToJson(asyncMessage);
        System.out.println(stringAsync);
    }

    @Override
    public synchronized void showMove(GameMessage gameMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringMove = jsonConverter.fromMessageToJson(gameMessage);
        System.out.println(stringMove);
    }

    @Override
    public synchronized void showPing(PingMessage pingMessage) {
        JsonConverter jsonConverter = new JsonConverter();
        String stringPing = jsonConverter.fromMessageToJson(pingMessage);
        System.out.println(stringPing);
    }

    @Override
    public synchronized void showGameState(GameStatePojo gameStatePojo) {
        ClientController clientController = ClientController.getInstance();
        PlayerPojo me = null;
        for (PlayerPojo p : clientController.getGameStatePojo().getPlayers()){
            if (p.getNickname().equals(ClientController.getInstance().getNickname())){
                me = p;
            }
        }

        System.out.println("\n----------------------------------------------------GAME STATE----------------------------------------------------\n");
        // PIANIFICATION
        if(clientController.getGameStatePojo().getCurrentPhase().equals(Phase.PIANIFICATION)){

            Set<AssistantCardPojo> assistantCardPojos= me.getDeck();
            for(AssistantCardPojo a: assistantCardPojos) {

                if (a.getTurnOrder()== 1) {
                    AssistantCard1.setImage(AssistantCard1.getImage());
                }
                if (a.getTurnOrder()== 2) {
                    AssistantCard2.setImage(AssistantCard2.getImage());
                }
                if (a.getTurnOrder()== 3) {
                    AssistantCard3.setImage(AssistantCard3.getImage());
                }
                if (a.getTurnOrder()== 4) {
                    AssistantCard4.setImage(AssistantCard4.getImage());
                }
                if (a.getTurnOrder()== 5) {
                    AssistantCard5.setImage(AssistantCard5.getImage());
                }
                if (a.getTurnOrder()== 6) {
                    AssistantCard7.setImage(AssistantCard6.getImage());
                }
                if (a.getTurnOrder()== 7) {
                    AssistantCard8.setImage(AssistantCard7.getImage());
                }
                if (a.getTurnOrder()== 8) {
                    AssistantCard9.setImage(AssistantCard8.getImage());
                }
                if (a.getTurnOrder()== 9) {
                    AssistantCard10.setImage(AssistantCard9.getImage());
                }
                if (a.getTurnOrder()== 10) {
                    AssistantCard10.setImage(AssistantCard10.getImage());
                }
            }




        }



        List<CloudPojo> cloudPojos=clientController.getGameStatePojo().getClouds();
        for(CloudPojo c: cloudPojos){
            if(c.getCloudId()==0){
                redOnCloud1.setId(c.getStudents().getPawns().get(ColourPawn.Red).toString());
                yellowOnCloud1.setId(c.getStudents().getPawns().get(ColourPawn.Yellow).toString());
                blueOnCloud1.setId(c.getStudents().getPawns().get(ColourPawn.Blue).toString());
                greenOnCloud1.setId(c.getStudents().getPawns().get(ColourPawn.Green).toString());
            }
            if(c.getCloudId()==1){
                redOnCloud2.setId(c.getStudents().getPawns().get(ColourPawn.Red).toString());
                yellowOnCloud2.setId(c.getStudents().getPawns().get(ColourPawn.Yellow).toString());
                blueOnCloud2.setId(c.getStudents().getPawns().get(ColourPawn.Blue).toString());
                greenOnCloud2.setId(c.getStudents().getPawns().get(ColourPawn.Green).toString());
            }
            if(c.getCloudId()==2){
                redOnCloud3.setId(c.getStudents().getPawns().get(ColourPawn.Red).toString());
                yellowOnCloud3.setId(c.getStudents().getPawns().get(ColourPawn.Yellow).toString());
                blueOnCloud3.setId(c.getStudents().getPawns().get(ColourPawn.Blue).toString());
                greenOnCloud3.setId(c.getStudents().getPawns().get(ColourPawn.Green).toString());
            }
        }


    }

    //METHODS FOR COMPLEX MODE:
    @Override
    public synchronized void moveStudentToIsland(){
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        CharacterCardsConsole characterCardsConsole = clientController.getCharacterCardsConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        valid = false;
        do {
            resultString = null;
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice.");
                System.out.print("\nChoose a colour (insert the numerical index): ");
                valid = false;
            }
        }while(valid == false);

        characterCardsConsole.setPawnColour(result);

        System.out.println("\n - Insert the island index to place it on an island");
        System.out.print("\nInsert your choice: ");

        valid = false;
        do {
            resultString = null;
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice.");
                System.out.print("\nInsert your choice (numerical index): ");
                valid = false;
            }
        }while(valid == false);
        characterCardsConsole.setPawnWhere(result);
    }

    @Override
    public synchronized void chooseIsland(){
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        CharacterCardsConsole characterCardsConsole = clientController.getCharacterCardsConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        int cardID = gameStatePojo.getActiveEffect().getCharacterId(); // can be 3 or 5

        System.out.println("\n"+"\033[01m"+"CARD"+cardID+" EFFECT"+"\033[0m");
        System.out.print("Insert island index: ");

        valid = false;
        do {
            resultString = null;
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice. You must choose a number.");
                System.out.print("Insert island index: ");
                valid = false;
            }
        }while(valid == false);
        characterCardsConsole.setPawnWhere(result);
    }

    @Override
    public synchronized void chooseColour() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        CharacterCardsConsole characterCardsConsole = clientController.getCharacterCardsConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        int cardID = gameStatePojo.getActiveEffect().getCharacterId();
        if(cardID!=7 && cardID!=10) {
            System.out.println("\n" + "\033[01m" + "CARD" + cardID + " EFFECT" + "\033[0m");
        }
        System.out.println("These are the colours you can choose from");
        for (ColourPawn c : ColourPawn.values()){
            System.out.println(c.getIndexColour() + " - " + c.toString() + ".");
        }
        System.out.print("Insert colour: ");
        valid = false;
        do {
            resultString = null;
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice. You must choose a number.");
                System.out.print("Insert colour: ");
            }
        }while(valid == false);
        characterCardsConsole.setPawnColour(result);
    }

    /** this method is used by card 7 and 10 to make the player choose the number of pawns he wants to move */
    @Override
    public synchronized void chooseNumOfMove() {
        ClientController clientController = ClientController.getInstance();
        GameStatePojo gameStatePojo = clientController.getGameStatePojo();
        CharacterCardsConsole characterCardsConsole = clientController.getCharacterCardsConsole();
        String resultString;
        Integer result = null;
        boolean valid;

        int cardID = gameStatePojo.getActiveEffect().getCharacterId(); // can be 7 or 10

        System.out.println("\n"+"\033[01m"+"CARD"+cardID+" EFFECT"+"\033[0m");
        System.out.print("Insert the number of students you want to move: ");

        valid = false;
        do {
            resultString = null;
            try{
                result = Integer.parseInt(resultString);
                valid = true;
            } catch(NumberFormatException e){
                System.out.println("Invalid choice. You must choose a number.");
                System.out.print("Insert number of students you want to move: ");
            }
        }while(valid == false);
        characterCardsConsole.setPawnsToMove(result);
        if(cardID==7){
            System.out.println("\nFirst choose the student on the card then the student in your entrance");
        }else if(cardID==10){
            System.out.println("\nFirst choose the student in your entrance then the student in your dining room");
        }
    }





}