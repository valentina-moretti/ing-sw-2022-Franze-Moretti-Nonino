package it.polimi.ingsw.server.Controller.Messages;

import it.polimi.ingsw.server.Controller.GameController;
import it.polimi.ingsw.server.Controller.Lobby;
import it.polimi.ingsw.server.Controller.Messages.ClientMessage;
import it.polimi.ingsw.server.Controller.Messages.JsonConverter;
import it.polimi.ingsw.server.Controller.Messages.Message;
import it.polimi.ingsw.server.Model.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MessageHandler {
    Scanner scanner = new Scanner(System.in);
    public int value;
    private int index =0;
    private int[] buffer = {1,2,3}; // inserire valori da provare con getValueTest()
    private ClientMessage lastMessage;
    private int portNumber;
    private Map<String,BufferedReader> bufferedReaderIn;
    private Map<String,BufferedWriter> bufferedReaderOut;
    private ServerSocket serverSocket;
    private JsonConverter jsonConverter;


    public MessageHandler(Lobby lobby){
        try {
            serverSocket=new ServerSocket();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        bufferedReaderIn= new HashMap<>();
        for(String s: lobby.getUsersReadyToPlay().keySet()){
            try{
                Socket clientSocket = lobby.getUsersReadyToPlay().get(s);
                BufferedReader in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                bufferedReaderIn.put(s, in);
                BufferedWriter out= new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                bufferedReaderOut.put(s, out);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        jsonConverter = new JsonConverter();
    }


    //restituisce il valore inserito dal giocatore player
    //in qualche modo chiedo il valore al giocatore e lo restituisco
    public int getValue(Player player) {
        return value;
    }

    public ServerSocket getSocket(){
        return serverSocket;
    }



    /* chiede valore in generale a quel message handler,
     non sappiamo ancora se 1 per ogni player o cosa....
    legge valore da std input, non funziona con i test
     */
    public int getValueCLI(String type, Player player){
        int a=0;
        boolean valid;
        System.out.print(player.getNickname() + " - " + type);
        /*
        do {
            valid=false;
            if(scanner.hasNextInt()) {
                a = scanner.nextInt();
                valid = true;
            }else{
                scanner.next();
            }
        }while(!valid);
         */
        Random rand = new Random();
        a = rand.nextInt(12)-1;
        return a;
    }

    /* buffer di valori memorizzati, utile per test
    * ogni volta che leggi un valore passa a quello successivo */
    public int getValueTest(){
        index++;
        return buffer[index-1];
    }

    public void setWinner(Player winner){
        System.out.println(winner);
    }

    /** parses from message-type to json string, then sends to the player the message, reads the answer from the player,
     * parses from json string to message-type
     * @param gameController
     * @param messageToSend
     * @return answer-message to the game controller. The game controller has to verify the validity of the answer.
     * @throws IOException
     */
    public Message communicationWithClient(GameController gameController, ClientMessage messageToSend) throws IOException {
       String stringToSend = jsonConverter.fromMessageToJson(messageToSend);
       String nickname= messageToSend.getNickname();
       bufferedReaderOut.get(nickname).write(stringToSend);
       bufferedReaderOut.get(nickname).flush();
       String receivedString= bufferedReaderIn.get(nickname).readLine();
       Message receivedMessage= jsonConverter.fromJsonToMessage(receivedString);
       return receivedMessage;
    }


    public void setLastMessage(ClientMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ClientMessage getLastMessage() {
        return lastMessage;
    }

    public Map<String, BufferedReader> getBufferedReaderIn() {
        return bufferedReaderIn;
    }

    public Map<String, BufferedWriter> getBufferedReaderOut() {
        return bufferedReaderOut;
    }
}