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

class GameStatus implements Serializable {
    private static final long SerialVersionUID = 10l;
    int index;
    int visibleIndex;
    boolean isWinner;


    public void setIndex(int index) {
        this.index = index;
    }

    public void setVisibleIndex(int visibleIndex) {
        this.visibleIndex = visibleIndex;
    }

    public int getIndex() {
        return index;
    }

    public int getVisibleIndex() {
        return visibleIndex;
    }
}

class LobbyStatus implements Serializable {
    private static final long SerialVersionUID = 10l;
    boolean isReady;

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean isReady() {
        return isReady;
    }
}