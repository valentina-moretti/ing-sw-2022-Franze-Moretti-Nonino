package it.polimi.ingsw.client.View.GUI;

import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.client.Controller.Console;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.common.gamePojo.*;
import it.polimi.ingsw.common.messages.*;

import java.util.function.Consumer;

/** the methods of this class are called by Console class when is needed that a player do an action, as for CLI
 * theese methods are blockers and Console waits as long as the player makes the correct movement
 */
public class GUIView implements View {

    private Consumer<Boolean> nameCompleteObserver;

    private Consumer<Boolean> noEnoughCoinsObserver;

    private Consumer<Boolean> invalidChoiseObserver;

    private Consumer<Boolean> correctMoveObserver;

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
            System.out.println("Closed while choosing game mode");
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
            System.out.println("Closed while choosing nickname");
        }

    }

    /**
     * when the client controller calls chooseAssistantCard, the console's attribute "assistantCardPlayed" is
     * set with the value chosen in GameHandlerScene.
     * The semaphore is 0, so that the thread waits in chooseAssistantCard until someone calls
     * semaphore.release. This is done by a JavaFX thread at the end of setAssistantCardChosen1/2.../10()
     * (in  the class GameHandlerScene)
     */
    @Override
    public void chooseAssistantCard() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            System.out.println("Closed while choosing assistant card");
        }
    }

    /**
     * when the client controller calls moveStudent, the console's "pawnColour" and "pawnWhere" is
     * set with the value chosen in GameHandlerScene.
     * The semaphore is 0, so that the thread waits in moveStudent until someone calls
     * semaphore.release. This is done by a JavaFX thread at the end of setStudentOnGameBoard() and
     * setStudentOnIsland()
     * (in the class GameHandlerScene)
     */
    @Override
    public void moveStudent() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            System.out.println("Closed while moving student");
        }
    }

    /**
     * when the client controller calls placeMotherNature, the console's attribute "stepsMotherNature" is
     * set with the value chosen in GameHandlerScene.
     * The semaphore is 0, so that the thread waits in placeMotherNature until someone calls
     * semaphore.release. This is done by a JavaFX thread at the end of setMotherNatureToIsalnd()
     * (in  the class GameHandlerScene)
     */
    @Override
    public void placeMotherNature() {
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().changeCursor(1));
        GuiController.getInstance().runMethod();
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            System.out.println("Closed while placing mother nature");
        }
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().changeCursor(0));
        GuiController.getInstance().runMethod();
    }




    /**
     * when the client controller calls chooseCloud, the console's attribute "cloudChosen" is
     * set with the value chosen in GameHandlerScene.
     * The semaphore is 0, so that the thread waits in chooseCloud until someone calls
     * semaphore.release. This is done by a JavaFX thread at the end of setCloudChosen1/2/3()
     * (in  the class GameHandlerScene)
     */
    @Override
    public synchronized void chooseCloud() {
        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            System.out.println("Closed while choosing cloud");
        }
    }



    /**
     * shows the messages received by the server according to its time
     * @param message
     */
    @Override
    public synchronized void showMessage(Message message) {
        if(message==null) return;

        if(message.getMessageType().equals(TypeOfMessage.Ack)){
            AckMessage ackMessage = (AckMessage) message;
            showAck(ackMessage);
            if(correctMoveObserver!=null){
                correctMoveObserver.accept(true);
                correctMoveObserver=null;
            }
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
                    nameCompleteObserver=null;
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
                if(invalidChoiseObserver!=null){
                    invalidChoiseObserver.accept(false);
                    invalidChoiseObserver=null;
                }
                break;
            case AlreadyPlayed:
                System.out.println("Another player has already played this card.\n");
                break;
            case TurnError:
                System.out.println("You cannot play. Wait for your turn.\n");
                break;
            case NoMoney:
                if(noEnoughCoinsObserver!=null){
                    noEnoughCoinsObserver.accept(false);
                    noEnoughCoinsObserver=null;
                }
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
    }

    @Override
    public synchronized void showAck(AckMessage ackMessage) {

        switch(ackMessage.getTypeOfAck()) {
            case CorrectConnection:
                //name ok -> change view
                GuiController.getInstance().setRunnable(()->GuiController.getInstance().switchWaitScene());
                GuiController.getInstance().runMethod();
                break;
            case CompleteLobby:
                GuiController.getInstance().setRunnable(()->GuiController.getInstance().switchGameScene());
                GuiController.getInstance().runMethod();
                break;
            default:
                System.out.println("Unknown ack message");
                break;
        }
    }

    @Override
    public synchronized void showUpdate(UpdateMessage updateMessage) {
        GameStatePojo gameStatePojo = updateMessage.getGameState();
        if(gameStatePojo.isGameOver()){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().switchGameOverScene());
            GuiController.getInstance().runMethod();
        }
        else {
            showGameState(gameStatePojo);
        }

    }

    @Override
    public synchronized void showAsync(AsyncMessage asyncMessage) {
    }

    @Override
    public synchronized void showMove(GameMessage gameMessage) {

    }

    @Override
    public synchronized void showPing(PingMessage pingMessage) {
    }

    @Override
    public synchronized void showGameState(GameStatePojo gameStatePojo) {
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().showGameUpdate());
        GuiController.getInstance().runMethod();
    }
    synchronized public void setNameCompleteObserver(Consumer<Boolean> nameCompleteObserver) {
        this.nameCompleteObserver = nameCompleteObserver;
    }

    public void setNoEnoughCoinsObserver(Consumer<Boolean> noEnoughCoinsObserver) {
        this.noEnoughCoinsObserver = noEnoughCoinsObserver;
    }

    public void setInvalidChoiseObserver(Consumer<Boolean> invalidChoiseObserver) {
        this.invalidChoiseObserver = invalidChoiseObserver;
    }

    public void setCorrectMoveObserver(Consumer<Boolean> correctMoveObserver) {
        this.correctMoveObserver = correctMoveObserver;
    }


    //METHODS FOR COMPLEX MODE:

    /**
     * Calls runLater thanks to GuiController.getInstance().runMethod();
     * Change cursor if mother nature round.
     */
    @Override
    public void askForCharacter() {
        if(ClientController.getInstance().getGameStatePojo().isExpert()==true) {
            //change cursor if mother nature round
            if(ClientController.getInstance().getConsole().getCurrActionBookMark() == Console.ActionBookMark.placeMotherNature){
                GuiController.getInstance().setRunnable(()->GuiController.getInstance().changeCursor(1));
                GuiController.getInstance().runMethod();
            }
            try {
                ClientController.getSemaphore().acquire();
            } catch (InterruptedException e) {
            }

            GuiController.getInstance().setRunnable(()->GuiController.getInstance().changeCursor(0));
            GuiController.getInstance().runMethod();
        }
    }

    /**
     * method used by card 1
     */
    @Override
    public synchronized void moveStudentToIsland(){
        GameHandlerScene.setCharacterCardToUse(true); // block all simple mode methods
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard1());
        GuiController.getInstance().runMethod();
        GameHandlerScene.setMoveStudentCard(true);

        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GameHandlerScene.setCharacterCardToUse(false);
        GameHandlerScene.setMoveStudentCard(false);
    }

    /**
     * method used by card 3 and card 5.
     * According to the card's id, it calls the correct method activeGuiCard() in GuiController-class.
     * To enables click on island for card's effect on the GUI, calls setChooseIsland(true).
     * At the end go back to initial conditions setting to false the GameHandlerScene's attributes that
     * are used to enables additional clicks on gui during character-card's effects.
     */
    @Override
    public synchronized void chooseIsland(){
        GameHandlerScene.setCharacterCardToUse(true);

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==3){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard3());
            GuiController.getInstance().runMethod();
            GameHandlerScene.setChooseIsland(true); // enables click on island for card's effect
        }

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==5){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard5());
            GuiController.getInstance().runMethod();
            GameHandlerScene.setChooseIsland(true);
        }

        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //back to initial conditions
        GameHandlerScene.setCharacterCardToUse(false);
        GameHandlerScene.setChooseIsland(false);

        GuiController.getInstance().setRunnable(()->GuiController.getInstance().changeCursor(0));
        GuiController.getInstance().runMethod();

    }

    /** this method is used by card 7 and 10 to make the player choose the number of pawns he wants to move.
     * According to the card's id, it calls the correct method activeGuiCard() in GuiController-class.
     * The choice is made throught an alert-panel. */
    @Override
    public synchronized void chooseNumOfMove() {
        GameHandlerScene.setCharacterCardToUse(true);

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==7){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard7_10(7));
            GuiController.getInstance().runMethod();

        }
        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==10){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard7_10(10));
            GuiController.getInstance().runMethod();
        }

        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GameHandlerScene.setCharacterCardToUse(false);

    }
    /**called when a card is used, shows and alert*/
    @Override
    public void showEffect(int num) {
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().showBanner(num));
        GuiController.getInstance().runMethod();
    }
    /**enables the movement of coins*/
    @Override
    public void canMoveCoin(boolean b) {
        GameHandlerScene.canMoveCoin(b);
    }

    /**when a player has used a character card then he must do the next move in game and can't ask again to use a
     * character card
     * @param b
     */
    @Override
    public void skipAskForCharacterGUI(boolean b) {
        GameHandlerScene.skipAskForForCharacter(b);
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().changeCursor(0));
        GuiController.getInstance().runMethod();
    }

    /**
     * this method is used by card 7, 9, 10, 11 and 12.
     * The player chooses the pawn on the card.
     * According to the card's id, it calls the correct method activeGuiCard() in GuiController-class.
     * To enables click GUI for card's effect on the GUI, calls setPawnColourBoolean(true), setClickStudentEntrance(true)
     * setClickDining(true).
     * At the end go back to initial conditions setting to false the GameHandlerScene's attributes that
     * are used to enables additional clicks on gui during character-card's effects.
     */
    @Override
    public synchronized void chooseColour() {
        GameHandlerScene.setCharacterCardToUse(true);

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==9) {
            GuiController.getInstance().setRunnable(() -> GuiController.getInstance().activeGuiCard9_12(9));
            GuiController.getInstance().runMethod();
        }

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==11){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard11());
            GuiController.getInstance().runMethod();
            GameHandlerScene.setPawnColourBoolean(true);
        }

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==12){
        GuiController.getInstance().setRunnable(()->GuiController.getInstance().activeGuiCard9_12(12));
        GuiController.getInstance().runMethod();
        }

        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==10){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().addClickCard10());
            GuiController.getInstance().runMethod();
            if(GameHandlerScene.isSecondPart()==false) {
                GameHandlerScene.setClickStudentEntrance(true);
            }else{
                GameHandlerScene.setClickDining(true);
            }
        }
        if(ClientController.getInstance().getGameStatePojo().getActiveEffect().getCharacterId()==7){
            GuiController.getInstance().setRunnable(()->GuiController.getInstance().addClickCard7());
            GuiController.getInstance().runMethod();
            if(GameHandlerScene.isSecondPart()==false) {
                GameHandlerScene.setClickStudentEntrance(true);
            }else{
                GameHandlerScene.setPawnColourBoolean(true);
            }
        }

        try {
            ClientController.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //bloccare tutte le condizioni e tornare alla condizione di partenza perch?? effetto terminato
        GameHandlerScene.setCharacterCardToUse(false);
        GameHandlerScene.setPawnColourBoolean(false);

    }






}