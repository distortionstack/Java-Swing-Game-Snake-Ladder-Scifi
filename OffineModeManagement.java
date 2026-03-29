import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.List;

public class OffineModeManagement{
    GameOfflineLogicalProcessor logicalProcess;
    GameOfflineComponetManagement componentManage;
    OffineModeManagement(AssetManager assetManager){
        logicalProcess = new GameOfflineLogicalProcessor();
        componentManage = new GameOfflineComponetManagement(this,assetManager);
    }
    //getter
    public GameOfflineComponetManagement getComponetManagement() {
        return componentManage;
    }
    public GameOfflineLogicalProcessor getLogicalProcess() {
        return logicalProcess;
    }
}

class GameOfflineLogicalProcessor extends GameLogicalProsessor{

    public GameOfflineLogicalProcessor() {
    }

}

class GameOfflineComponetManagement extends ComponentManagement {
    OffineModeManagement offline;
    OfflineLobby offlineLobby;
    OfflineGame offlineGame;

    GameOfflineComponetManagement(OffineModeManagement offline,AssetManager assetManager) {
        this.offline = offline;

        offlineLobby = new OfflineLobby(assetManager);  
        LobbyListenerSetUp();

        offlineGame = new OfflineGame(assetManager,offline.getLogicalProcess());
        GameListenerSetUp();
    }

    public void GameListenerSetUp(){
        offlineGame.addDiceButtonListener(e -> {
        offline.getLogicalProcess().diceRoll();
        offline.getLogicalProcess().updateIndex(); 
        
        System.out.println("Target Index is: " + offline.getLogicalProcess().getCurrentPlayer().getgStatus().getIndex());

        new OfflineAnimationThread(
            offline.getLogicalProcess().getCurrentPlayer(), 
            offlineGame,
            offline.getLogicalProcess()
        );
        });
    }

    public void LobbyListenerSetUp(){
        for (JButton button : offlineLobby.getSelectSkinButton()) {
            button.addActionListener(e -> {
                button.setEnabled(false);
                offline.getLogicalProcess().addPlayer(button);
                System.out.println(offline.getLogicalProcess().playerList.getLast().Skincode);
            });
        }
        offlineLobby.getStartButton().addActionListener(e -> {
            Main.getGuiDisplay().remove(offlineLobby);
            Main.getGuiDisplay().add(offlineGame);
            Main.getGuiDisplay().repaint();
            Main.getGuiDisplay().revalidate();
        });
    }

    public OfflineGame getOfflineGame() {
        return offlineGame;
    }

    public OfflineLobby getOfflineLobby() {
        return offlineLobby;
    }
}

class OfflineLobby extends LobbyPanel {
    JButton startButton;
    
    OfflineLobby(AssetManager assetManager){
        super(assetManager);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(90,80,50,80));
        add(LobbyLabel,BorderLayout.NORTH);
        add(selectSkinPanel,BorderLayout.CENTER);

        operationPanel = new JPanel(){{
            setOpaque(false);
            setLayout(new FlowLayout());
            startButton = new JButton("start"){{
                setSize(200,50);
            }};
            add(startButton);
        }};
        add(operationPanel,BorderLayout.SOUTH);
    }
    public JButton getStartButton() {
        return startButton;
    }
}

class OfflineGame extends GamePanel {
    GameLogicalProsessor logical;
    OfflineGame(AssetManager assetManager,GameLogicalProsessor logical){
        super(assetManager);
        this.logical = logical;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        ArrayList<PlayerData> pArrayList = logical.getPlayerList();
        
        if(pArrayList == null || pArrayList.isEmpty()) return;

        // 1. จัดกลุ่มผู้เล่นตามช่องที่ยืน (Map: Index -> List of Players)
        Map<Integer, List<PlayerData>> playersAtIndex = new HashMap<>();
        for (PlayerData p : pArrayList) {
            int idx = p.getgStatus().getVisibleIndex();
            playersAtIndex.computeIfAbsent(idx, k -> new ArrayList<>()).add(p);
        }

        // 2. วาดผู้เล่นทีละช่อง
        for (Integer index : playersAtIndex.keySet()) {
            List<PlayerData> playersInThisBlock = playersAtIndex.get(index);
            int count = playersInThisBlock.size();

            // เช็คว่า Index ถูกต้องและมีพิกัดจริง
            if (index < onePlayerPoint.length && onePlayerPoint[index] != null) {
                Point basePos = onePlayerPoint[index];

                // 3. วาดทุกคนในกลุ่มตามตำแหน่ง "หน้าลูกเต๋า"
                for (int i = 0; i < count; i++) {
                    PlayerData p = playersInThisBlock.get(i);
                    ImageIcon skin = assetManager.getGameAsset().getPlayerSkin(p.getSkincode());
                    
                    if (skin != null) {
                        // คำนวณ Offset (ระยะเยื้องจากจุดกึ่งกลาง)
                        Point offset = getDiceOffset(count, i);
                        
                        g.drawImage(skin.getImage(), 
                                    basePos.x + offset.x, 
                                    basePos.y + offset.y, 
                                    GameUI.PLAYER_SIZE.width, GameUI.PLAYER_SIZE.height, this);
                    }
                }
            }
        }
    }

    // ฟังก์ชันช่วยคำนวณตำแหน่ง (Pattern หน้าลูกเต๋า)
    private Point getDiceOffset(int totalPlayers, int playerIndex) {
        // ถ้าคนเยอะเกิน 6 คน ให้เรียงแบบหน้า 6 ไปเรื่อยๆ หรือซ้อนกัน
        if (totalPlayers > 6) totalPlayers = 6; 
        
        switch (totalPlayers) {
            case 1: // จุดตรงกลาง
                return new Point(0, 0);
            case 2: // เฉียงซ้ายบน - ขวาล่าง
                if (playerIndex == 0) return new Point(-GAP, -GAP);
                return new Point(GAP, GAP);
            case 3: // เฉียง 3 จุด
                if (playerIndex == 0) return new Point(-GAP, -GAP);
                if (playerIndex == 1) return new Point(0, 0);
                return new Point(GAP, GAP);
            case 4: // 4 มุม
                if (playerIndex == 0) return new Point(-GAP, -GAP); // ซ้ายบน
                if (playerIndex == 1) return new Point(GAP, -GAP);  // ขวาบน
                if (playerIndex == 2) return new Point(-GAP, GAP);  // ซ้ายล่าง
                return new Point(GAP, GAP);                         // ขวาล่าง
            case 5: // 4 มุม + ตรงกลาง
                if (playerIndex == 0) return new Point(-GAP, -GAP);
                if (playerIndex == 1) return new Point(GAP, -GAP);
                if (playerIndex == 2) return new Point(0, 0);       // กลาง
                if (playerIndex == 3) return new Point(-GAP, GAP);
                return new Point(GAP, GAP);
            case 6: // 2 แถว แถวละ 3 (แนวตั้ง)
            default:
                if (playerIndex == 0) return new Point(-GAP, -GAP);
                if (playerIndex == 1) return new Point(GAP, -GAP);
                if (playerIndex == 2) return new Point(-GAP, 0);
                if (playerIndex == 3) return new Point(GAP, 0);
                if (playerIndex == 4) return new Point(-GAP, GAP);
                return new Point(GAP, GAP);
        }
    }
}
