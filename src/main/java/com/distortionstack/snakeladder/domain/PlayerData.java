package com.distortionstack.snakeladder.domain;
import java.io.Serializable;


public class PlayerData {
    private static final long SerialVersionUID = 10l;

    String PlayerID;
    String Skincode = "Invalid";

    GameStatus gStatus;
    LobbyStatus lobbyStatus;

    PlayerData(){
        gStatus = new GameStatus();
        lobbyStatus = new LobbyStatus();
    }

    public void setSkincode(String skincode) {
        Skincode = skincode;
    }

    public LobbyStatus getLobbyStatus() {
        return lobbyStatus;
    }
    public GameStatus getgStatus() {
        return gStatus;
    }
    public String getSkincode() {
        return Skincode;
    }

}


