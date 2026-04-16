package com.distortionstack.snakeladder.domain.online;
import com.distortionstack.snakeladder.domain.GameLogicalManeger;

public class OnlineModeManagement {
    
}

class GameServer extends GameLogicalManeger{
    GameServer(){

    }
}

class GameClient {
    OnlineModeManagement online;
    GameClient(OnlineModeManagement online) {
        this.online = online;
    }
}
