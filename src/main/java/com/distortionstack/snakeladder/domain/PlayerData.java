package com.distortionstack.snakeladder.domain;
import java.io.Serializable;


public class PlayerData {
    private static final long serialVersionUID = 10L;

    String playerId;
    String skinCode = "Invalid";

    GameStatus gameStatus;
    LobbyStatus lobbyStatus;

    PlayerData(){
        gameStatus = new GameStatus();
        lobbyStatus = new LobbyStatus();
    }

    public void setSkinCode(String skinCode) {
        this.skinCode = skinCode;
    }

    public LobbyStatus getLobbyStatus() {
        return lobbyStatus;
    }
    public GameStatus getGameStatus() {
        return gameStatus;
    }
    public String getSkinCode() {
        return skinCode;
    }

}


