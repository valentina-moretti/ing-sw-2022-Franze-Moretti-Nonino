package it.polimi.ingsw.common.gamePojo;

import java.util.List;

public class GameStatePojo {

    private Integer gameId;
    private PlayerPojo currentPlayerPojo;
    private CharacterPojo activeEffect;

    private int coinSupply;
    private List<IslandPojo> islandPojos;
    private PawnsMapPojo studentsBag;
    private List<CloudPojo> cloudPojos;
    private PawnsMapPojo professorsLeft;
    private List<CharacterPojo> characterPojos;

    private List<PlayerPojo> playerPojos;



    private Phase currentPhase;
    private boolean gameOver;
    private String winner;
    private boolean expert;

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }



    public PlayerPojo getCurrentPlayer() {
        return currentPlayerPojo;
    }

    public void setCurrentPlayer(PlayerPojo currentPlayerPojo) {
        this.currentPlayerPojo = currentPlayerPojo;
    }

    public CharacterPojo getActiveEffect() {
        return activeEffect;
    }

    public void setActiveEffect(CharacterPojo activeEffect) {
        this.activeEffect = activeEffect;
    }

    public int getCoinSupply() {
        return coinSupply;
    }

    public void setCoinSupply(int coinSupply) {
        this.coinSupply = coinSupply;
    }

    public List<IslandPojo> getIslands() {
        return islandPojos;
    }

    public void setIslands(List<IslandPojo> islandPojos) {
        this.islandPojos = islandPojos;
    }

    public PawnsMapPojo getStudentsBag() {
        return studentsBag;
    }

    public void setStudentsBag(PawnsMapPojo studentsBag) {
        this.studentsBag = studentsBag;
    }

    public List<CloudPojo> getClouds() {
        return cloudPojos;
    }

    public void setClouds(List<CloudPojo> cloudPojos) {
        this.cloudPojos = cloudPojos;
    }

    public PawnsMapPojo getProfessorsLeft() {
        return professorsLeft;
    }

    public void setProfessorsLeft(PawnsMapPojo professorsLeft) {
        this.professorsLeft = professorsLeft;
    }

    public List<CharacterPojo> getCharacters() {
        return characterPojos;
    }

    public void setCharacters(List<CharacterPojo> characterPojos) {
        this.characterPojos = characterPojos;
    }

    public List<PlayerPojo> getPlayers() {
        return playerPojos;
    }

    public void setPlayers(List<PlayerPojo> playerPojos) {
        this.playerPojos = playerPojos;
    }

    public void show(){

    }
}
