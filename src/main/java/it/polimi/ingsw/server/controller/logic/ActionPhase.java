package it.polimi.ingsw.server.controller.logic;

import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.ColourTower;
import it.polimi.ingsw.server.controller.characters.Card5;
import it.polimi.ingsw.server.controller.characters.CharacterEffect;
import it.polimi.ingsw.server.controller.characters.CharacterEffectInfluence;
import it.polimi.ingsw.server.controller.network.MessageHandler;
import it.polimi.ingsw.common.messages.*;
import it.polimi.ingsw.server.controller.network.PlayerManager;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Character;

import java.util.*;
import java.util.stream.Collectors;

//DA TOGLIERE TUTTI GLI STRING MESSAGES TO CLIENT
public class ActionPhase extends GamePhase {
    private final GameController gameController;

    private HashMap<Player, Integer> maximumMovements;
    private List<Player> turnOrder;
    private ActionResult actionResult;

    public ActionPhase(GameController gameController) {
        this.gameController = gameController;
        maximumMovements = null;
        turnOrder = null;
        actionResult = new ActionResult();
    }

    public ActionResult handle(List<Player> turnOrder, HashMap<Player, Integer> maximumMovements,
                               boolean isLastRoundFinishedStudentsBag) {

        MessageHandler messageHandler = this.gameController.getMessageHandler();
        this.maximumMovements = maximumMovements;
        this.turnOrder = turnOrder;
        boolean isEnded = false;
        actionResult.setFirstPianificationPlayer(turnOrder.get(0));


        for (Player p : turnOrder) {
            if (!(actionResult.isFinishedTowers() || actionResult.isThreeOrLessIslands())){

                gameController.setCurrentPlayer(p);



                messageHandler.stringMessageToAllClients("\nMOTHERNATURE: Island number " + gameController.getGame().findMotherNature()+ "\nISLANDS:\\n\" + gameController.getGame().islandsToString()");

                gameController.update();
                askforCharacter();
                gameController.update();
                moveStudents();
                gameController.update();

                askforCharacter();
                gameController.update();
                if (checkEnd() == true){return actionResult;}

                messageHandler.stringMessageToAllClients("\nMOTHERNATURE: Island number " + gameController.getGame().findMotherNature()+" (maximumMovements for mothernature: " + maximumMovements.get(gameController.getCurrentPlayer()) + ")\n");

                Island whereMotherNature = moveMotherNature(p);

                messageHandler.stringMessageToAllClients("\nMOTHERNATURE: moved to Island number " + gameController.getGame().findMotherNature() + gameController.getGame().findMotherNature()+" (maximumMovements for mothernature: " + maximumMovements.get(gameController.getCurrentPlayer()) + ")\n");

                //System.out.println("\nMOTHERNATURE: moved to Island number " + gameController.getGame().findMotherNature());

                Player moreInfluentPlayer = calcultateInfluence(whereMotherNature);
                if(whereMotherNature.getNumNoEntryTile()>0){
                    whereMotherNature.setNumNoEntryTile(whereMotherNature.getNumNoEntryTile()-1);
                    for(Character c : gameController.getGame().getCharacters())
                        if(c.getCharacterId()==5) {
                            ((Card5)gameController.getCharacterEffects().get(c)).addNoEntryTile();
                        }
                }

                gameController.update();

                if (moreInfluentPlayer == null){
                    messageHandler.stringMessageToAllClients("MOREINFLUENTPLAYER: none");
                    //System.out.println("MOREINFLUENTPLAYER: none");
                } else {
                    messageHandler.stringMessageToAllClients("MOREINFLUENTPLAYER: "+ moreInfluentPlayer.toString());
                    //System.out.println("MOREINFLUENTPLAYER: "+ moreInfluentPlayer.toString());
                }

                if (moreInfluentPlayer != null){
                    isEnded = placeTowerOfPlayer(moreInfluentPlayer, whereMotherNature);
                    gameController.update();
                    if (isEnded) {
                        actionResult.setFinishedTowers(true);

                        return actionResult;
                    }

                    boolean union = verifyUnion();

                    gameController.update();

                    int numIslands= this.gameController.getGame().getIslands().size();

                    if(numIslands<4){
                        actionResult.setThreeOrLessIslands(true);
                        messageHandler.stringMessageToAllClients("There are 3 or less islands");

                        return actionResult;
                    }

                }
            }

            askforCharacter();
            gameController.update();
            if (checkEnd() == true){return actionResult;}


            /*in this round players choose the cloud only if in the pianification phase i had enough
            studentsPawns in the bag to fill ALL the clouds*/

            //if (!isLastRoundFinishedStudentsBag) {
            //    System.out.println("\nCLOUDS:\n" + gameController.getGame().cloudsToString());
            //    chooseCloud();
            //}

            gameController.update();

            askforCharacter();
            gameController.update();
            if (checkEnd() == true){return actionResult;}

        }

        /*reset characterEffects activated*/
        gameController.getGame().setActiveEffect(null);

        return actionResult;
    }


    /** if where==-1 moves student from the schoolboard entrance to the diningroom
     * else moves student from the schoolboard entrance to the island of index=where
     * @param colour
     * @param where
     */
    protected void moveSingleStudent(ColourPawn colour, Integer where){
        int coinsToAdd;
        PawnsMap student= new PawnsMap();
        student.add(colour);

        if (where == -1) {
            coinsToAdd = gameController.getCurrentPlayer().getSchoolBoard().fromEntranceToDiningRoom(student, gameController.getGame());
            gameController.getCurrentPlayer().addCoins(gameController.isExpert() ? coinsToAdd : 0);
        } else {
            gameController.getCurrentPlayer().getSchoolBoard().getEntrance().remove(colour);
            gameController.getGame().getIslands().get(where).addStudents(student);
        }

    }


    protected void moveStudents(){
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager= messageHandler.getPlayerManager(currPlayer);
        Message receivedMessage;
        Message errorGameMessage;
        boolean valid=true;
        int indexColour;
        GameMessage gameMessage;
        int where = 0;   // -1 refer for diningRoom, index of island for island

        int studentsToMove = 0;
        if (gameController.getGame().getPlayers().size() == 2){studentsToMove = 3;}
        if (gameController.getGame().getPlayers().size() == 3){studentsToMove = 4;}

        for(int i=0; i<studentsToMove; i++){
            // to user: choose your i+1 movement of 3
            do{
                valid = true;
                // to user: choose one color pawn

                gameMessage = playerManager.readMessage(TypeOfMessage.StudentColour);

                indexColour= gameMessage.getValue();
                if(indexColour<=-1 || indexColour>=5){
                    valid=false;
                    // to user: index not valid
                    playerManager.stringMessageToClient("indexColour not valid.");
                    //System.out.println("indexColour not valid.");
                }
                if(valid){
                    if (gameController.getCurrentPlayer().getSchoolBoard()
                            .getEntrance().get(ColourPawn.get(indexColour)) <= 0){
                        valid = false;
                        //to user: change color pawn to move, you don't have that color
                        errorGameMessage=new Message(TypeOfMessage.Error);
                        playerManager.sendMessage(errorGameMessage);
                        //System.out.println("You don't have that colour.");
                    }
                }

                // to user: choose position

                if(valid){
                    gameMessage = playerManager.readMessage(TypeOfMessage.StudentPosition);
                    where = gameMessage.getValue();
                    if(where!= -1 && (where <0 || where > gameController.getGame().getIslands().size()-1 )) {
                        valid = false;
                        //to user: position not valid
                        errorGameMessage=new Message(TypeOfMessage.Error);
                        playerManager.sendMessage(errorGameMessage);
                    }
                }
                if(valid && gameController.getCurrentPlayer().getSchoolBoard().getDiningRoom().
                        get(ColourPawn.get(indexColour))>=10) {
                    valid = false;
                    // to user: your school board in that row of your dining room is full
                    Message ErrorMessage = new Message(TypeOfMessage.Error);
                    playerManager.sendMessage(ErrorMessage);
                }
            }while(!valid);

            // to user: ok
            this.moveSingleStudent(ColourPawn.get(indexColour), where );

        }
    }

    protected Island moveMotherNature(Player currentPlayer){
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        Message receivedMessage;
        GameMessage gameMessage;
        Message errorGameMessage;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        PlayerManager playerManager=messageHandler.getPlayerManagerMap().get(currPlayer);
        Island ris = null;
        int played;

        do {
            gameMessage = playerManager.readMessage(TypeOfMessage.MoveMotherNature);
            played = gameMessage.getValue();
            if (played < 1 || played > maximumMovements.get(currentPlayer)){
                errorGameMessage=new Message(TypeOfMessage.Error);
                playerManager.sendMessage(errorGameMessage);
            }
        }
        while(played < 1 || played > maximumMovements.get(currentPlayer));


        List<Island> islandList = this.gameController.getGame().getIslands();

        boolean flag = false; //used to find the current position of motherNature
        for(int i = 0; i < islandList.size() && flag == false; i++){
            if (islandList.get(i).getHasMotherNature() == true){
                flag = true;
                islandList.get(i).setHasMotherNature(false);
                islandList.get((i+played) % islandList.size()).setHasMotherNature(true);

                ris = islandList.get((i+played) % islandList.size());
            }
        }
        return ris;
    }


    /**if some particualr characters are active it's not called the usual method: island.getInfluence() but
     * it's called the getInfluence() method of that character: this method returns the more influence player
     * according to the new effect (e.g. towers are not counted)
     */
    public Player calcultateInfluence(Island island){
        if(island.getNumNoEntryTile() >0){
            return null;
        }
        /* if some particualr characters are active it's not called the usual method: island.getInfluence() but
        * it's called the getInfluence() method of that character: this method returns the more influence player
        * according to the new effect (e.g. towers are not counted)
         */
        Player moreInfluentPlayer;
        if(gameController.getGame().getActiveEffect()!=null) {
            int characterId=gameController.getGame().getActiveEffect().getCharacterId();
            if(characterId==2 || characterId==6 || characterId==8 || characterId==9){
                CharacterEffectInfluence character= (CharacterEffectInfluence) gameController.getCharacterEffects().get(gameController.getGame().getActiveEffect());
                moreInfluentPlayer = character.effectInfluence(island);
                return moreInfluentPlayer;
            }
        }
        // if none of previous character effects is active, influence is as usual... :
        moreInfluentPlayer = island.getInfluence(gameController.getGame());
        return moreInfluentPlayer;
    }

    /**places the tower of the player on the island. Returns true if one player has finished his towers*/
    public boolean placeTowerOfPlayer(Player moreInfluentPlayer, Island island){
        ColourTower color=moreInfluentPlayer.getColourTower();
        if(island.getTowerCount()==0){
            island.addTower(1);
            moreInfluentPlayer.getSchoolBoard().removeTower();
            island.setTowerColor(color);
            if (moreInfluentPlayer.getSchoolBoard().getSpareTowers() == 0) {return true;}
        } else {
            if (!island.getTowerColour().equals(color)) {

                /*adding the towers on the schoolboard of the player who has lost the island control*/
                Player oldOwner = null;
                for (Player player : turnOrder) {
                    if (player.getColourTower() == island.getTowerColour()) {
                        oldOwner = player;
                    }
                }
                oldOwner.getSchoolBoard().addTower(island.getTowerCount());

                /*removing the towers on the schoolboard of the player who won the island control*/
                int numLeftTowers = moreInfluentPlayer.getSchoolBoard().getSpareTowers();
                moreInfluentPlayer.getSchoolBoard().removeTower(island.getTowerCount());
                island.setTowerColor(color);
                if (numLeftTowers < island.getTowerCount()) {return true;}
            }
        }
        return false;
    }

    /**returns true if there was a union, false otherwise*/
    public boolean verifyUnion() {
        List<Island> islandList = this.gameController.getGame().getIslands();
        List<Integer> currColour;
        HashMap<ColourTower, List<Integer>> colourMap = new HashMap<ColourTower, List<Integer>>();

        for(ColourTower c : ColourTower.values()){
            currColour = new ArrayList<>();
            colourMap.put(c, currColour);
        }
        for(int i = 0; i < islandList.size(); i++){
            if (islandList.get(i).getTowerCount() > 0){
                colourMap.get(islandList.get(i).getTowerColour()).add(i);
            }
        }

        /*at this point colourMap is a map where the key is a ColourTower and the value contained is a List of Integer
        corrisponding at the index of the isalnds where the tower has that colour*/

        boolean flag = false; //true when islands of the same colour are adjacent
        Set<Integer> toUnify = new HashSet<>();//set of the islands index to unify

        for(ColourTower c : ColourTower.values()) {
            currColour = colourMap.get(c);

            for(Integer i : currColour){
                for(Integer j : currColour){
                    if (j.equals((i+1) % islandList.size())){
                        toUnify.add(i);
                        toUnify.add(j);
                        flag = true;
                    }
                }
            }
        }

        List<Island> ris = new ArrayList<>(); //list of the islands to unify
        if (flag == true){
            for(Integer i : toUnify){
                ris.add(islandList.get(i));
            }
        }

        if (ris.size() == 0) {return false;}

        this.gameController.getGame().unifyIslands(ris);

        return true;
    }

    protected void chooseCloud(){
        GameMessage gameMessage;
        Message errorGameMessage;
        boolean valid;
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        String currPlayer=gameController.getCurrentPlayer().getNickname();
        PlayerManager playerManager = messageHandler.getPlayerManager(currPlayer);
        int indexCloud;
        // if there is only one cloud left, it goes directly in player's schoolBoard
        List<Cloud> cloudNotEmpty;
        cloudNotEmpty = (gameController.getGame().getClouds()).stream()
                .filter(x -> ! x.getStudents().isEmpty()).collect(Collectors.toList());
        if(  cloudNotEmpty.size() == 1 ){
            gameController.getCurrentPlayer().getSchoolBoard().insertCloud(cloudNotEmpty.get(0));

            playerManager.stringMessageToClient("There is only one Cloud left. You have received students form Cloud number " +
                    gameController.getGame().getClouds().indexOf(cloudNotEmpty.get(0)));

            return;
        }
        // to user: choose one cloud
        // print possibile cloud with values
        do{
            valid = true;
            gameMessage = playerManager.readMessage(TypeOfMessage.CloudChoice);
            indexCloud= gameMessage.getValue();
            if(indexCloud<0 || indexCloud > gameController.getGame().getPlayers().size()-1){
                // to user: index not valid
                errorGameMessage=new Message(TypeOfMessage.Error);
                playerManager.sendMessage(errorGameMessage);
                valid = false;
            }
            if(valid){
                if(gameController.getGame().getClouds().get(indexCloud).getStudents().isEmpty()){
                    // to user: empty cloud, rechoose
                    errorGameMessage=new Message(TypeOfMessage.Error);
                    playerManager.sendMessage(errorGameMessage);
                    valid=false;
                }
            }
        }while(!valid);
        // to user: ok

        gameController.getCurrentPlayer().getSchoolBoard().insertCloud(gameController.getGame().getClouds().get(indexCloud));
    }

    // fine metodi di gioco

    /** this method does nothing if game is in simple mode because no player has more than 0 coins
     * otherwise it ask the player for character card he wants to use between that he can afford */
    protected void askforCharacter(){
        GameMessage gameMessage;
        Message errorGameMessage;
        String currPlayer= gameController.getCurrentPlayer().getNickname();
        MessageHandler messageHandler = this.gameController.getMessageHandler();
        int cardNumber;
        PlayerManager playerManager=messageHandler.getPlayerManagerMap().get(currPlayer);

        if(playerManager.isCharacterReceived()==false){
            return;
        }

        List<Character> usable = new ArrayList<>();
        for(Character character: gameController.getGame().getCharacters()) {
            if (gameController.getCurrentPlayer().getCoins() >= character.getCost() &&
                    gameController.getGame().getActiveEffect() == null) {
                usable.add(character);
            }
        }

        if(!usable.isEmpty()){
            // to user: you can play one of theese cards.. select the number of card you want to
            // use to play the card now, any other key to skip

            gameMessage = playerManager.readMessage(TypeOfMessage.CharacterCard);

            cardNumber= gameMessage.getValue();
            if(cardNumber >= 0 && cardNumber<=usable.size()-1) {
                gameController.getGame().setActiveEffect(usable.get(cardNumber));
                gameController.getCurrentPlayer().removeCoins(usable.get(cardNumber).getCost());
                gameController.getGame().addCoins(usable.get(cardNumber).getCost());

                for (Character cr : gameController.getGame().getCharacters())
                    if (cr.getCharacterId() == usable.get(cardNumber).getCharacterId()) {
                        cr.use();
                    }

                CharacterEffect currentCharacterEffect = gameController.getCharacterEffects().get(usable.get(cardNumber));
                currentCharacterEffect.doEffect();
            }else{
                //this character card is not valid
                errorGameMessage=new Message(TypeOfMessage.Error);
                playerManager.sendMessage(errorGameMessage);
            }
        }else{
            errorGameMessage=new Message(TypeOfMessage.Error);
            playerManager.sendMessage(errorGameMessage);
        }
        playerManager.setCharacterReceived(false);

    }

    public void setCurrPlayer(Player currPlayer) {
        this.gameController.setCurrentPlayer(currPlayer);
    }

    public void setMaximumMovements(HashMap<Player, Integer> maximumMovements) {
        this.maximumMovements = maximumMovements;
    }

    public void setTurnOrder(List<Player> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    /**returns true if a player has finished his towers / there are less than 4 islands*/
    private boolean checkEnd(){
        if (actionResult.isFinishedTowers() == true) return true;
        if (actionResult.isThreeOrLessIslands() == true) return true;
        return false;
    }

    @Override
    public String toString(){
        return "ActionPhase";
    }
}