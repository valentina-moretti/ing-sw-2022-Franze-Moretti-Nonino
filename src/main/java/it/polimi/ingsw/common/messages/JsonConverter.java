package it.polimi.ingsw.common.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.model.CharacterState;

public class  JsonConverter {
    private static GsonBuilder builder = new GsonBuilder();
    private static Gson gson;

    static{
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public static Message fromJsonToMessage(String jsonString){
        Message message = gson.fromJson(jsonString, Message.class);

        if(message.getMessageType() == TypeOfMessage.Connection){
            ConnectionMessage messageReal=  gson.fromJson(jsonString, ConnectionMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Ack){
            AckMessage messageReal=  gson.fromJson(jsonString, AckMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Update){
            UpdateMessage messageReal=  gson.fromJson(jsonString, UpdateMessage.class);
            for (int i = 0; i < 3; i++){
                if (messageReal.getGameState().getCharacters().get(i).getCharacterId() == 1 ||
                        messageReal.getGameState().getCharacters().get(i).getCharacterId() == 7 ||
                        messageReal.getGameState().getCharacters().get(i).getCharacterId() == 11){
                //In realtà le characterPojos all'interno del GameStatePojo non sono veramente characterPojos,
                    // ma CharacterStudentPojo --> DA RISOLVERE
                }
                if (messageReal.getGameState().getCharacters().get(i).getCharacterId() == 5){
                //In realtà le characterPojos all'interno del GameStatePojo non sono veramente characterPojos,
                    // ma CharacterNoEntryPojo --> DA RISOLVERE
                }
            }
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Game){
            GameMessage messageReal1=  gson.fromJson(jsonString, GameMessage.class);
            if (messageReal1.getTypeOfMove() == TypeOfMove.PawnMovement){
                PawnMovementMessage messageReal2 = gson.fromJson(jsonString, PawnMovementMessage.class);
                return messageReal2;
            }
            return messageReal1;
        }

        if(message.getMessageType() == TypeOfMessage.Error){
            ErrorMessage messageReal=  gson.fromJson(jsonString, ErrorMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Ping){
            PingMessage messageReal=  gson.fromJson(jsonString, PingMessage.class);
            return messageReal;
        }

        if(message.getMessageType() == TypeOfMessage.Async){
            AsyncMessage messageReal=  gson.fromJson(jsonString, AsyncMessage.class);
            return messageReal;
        }

        return message;
    }


    public static String fromMessageToJson(Message message){
        String jsonString=gson.toJson(message) + "\nEOF\n";
        return(jsonString);
    }

}
