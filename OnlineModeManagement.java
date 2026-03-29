public class OnlineModeManagement {
    
}

class GameServer extends GameLogicalProsessor{
    GameServer(){

    }
}

class GameClient extends ComponentManagement {
    OnlineModeManagement online;
    GameClient(OnlineModeManagement online) {
        this.online = online;
    }
}
