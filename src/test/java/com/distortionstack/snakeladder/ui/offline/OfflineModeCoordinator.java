package com.distortionstack.snakeladder.ui.offline;

import javax.swing.JButton;

import com.distortionstack.snakeladder.domain.offline.OfflineGameLogicalManeger;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.DisplayController;

public class OfflineModeCoordinator {
    OfflineGameLogicalManeger offlineLogic;
    OfflineLobbyPanel offlineLobbyPanel;
    OfflineGamePanel offlineGamePanel;
    DisplayController displayController;
    public static boolean condition = false;

    public OfflineModeCoordinator(OfflineGameLogicalManeger offlineLogic, AssetManager assetManager,DisplayController displayController) {
        this.offlineLogic = offlineLogic;

        offlineLobbyPanel = new OfflineLobbyPanel(assetManager,displayController);
        offlineGamePanel = new OfflineGamePanel(assetManager, this.offlineLogic ,displayController);

        this.displayController = displayController;

        bindEvents();
    }

    private void bindEvents() {
        // Lobby
        offlineLobbyPanel.getStartButton().addActionListener(e -> onStartGameClicked());
        for (JButton button : offlineLobbyPanel.getSelectSkinButton()) {
            button.addActionListener(e -> setOnSkinSelected(button));
        }
        
        // Game
        offlineGamePanel.addDiceButtonListener(e -> onDiceRolled());
    }

    private void onStartGameClicked(){
        if(!offlineLogic.getPlayerList().isEmpty()){
            displayController.startOfflineGame();
        }else{
            offlineLobbyPanel.EmptyPlayerAlert();
        }
    }

    private void setOnSkinSelected(JButton button){
            offlineLogic.addPlayer(button.getText());
            System.out.println(offlineLogic.getPlayerList().getLast().getSkincode());
            offlineLobbyPanel.handleSkinSelect(button);
    }

    private void onDiceRolled() {
    // 1. เตรียมความพร้อม
    offlineGamePanel.BlockDiceButton();
    
    
    // 2. ทอยใน Logic ครั้งเดียวเพื่อเอา "ผลลัพธ์ที่จะจบ"
    offlineLogic.diceRoll(); 
    int finalResult = offlineLogic.getDiceRollValue(); 
    
    // 3. เริ่ม Animation โดยส่งค่า finalResult เข้าไปรอไว้
    // ในระหว่างที่หมุน DiceAnimation จะสุ่มเลขหลอก (1-6) โชว์ไปเรื่อยๆ เอง
    offlineGamePanel.getAssetManager().getGameAsset().playDiceRollSound();
        offlineGamePanel.getAssetManager().getGameAsset().playDiceRollingSound();
        // อัปเดตตำแหน่งในกระดาน
        offlineLogic.updateIndex(); 
        
        // สั่งเดินตัวละคร
        executeWalkingSequence();
}

    private void executeWalkingSequence() {
            new OfflineAnimationThread(
                offlineLogic.getCurrentPlayer(),
                offlineGamePanel,
                offlineLogic
            );
            if (condition) {
                gameOverSequence();
            }
    }

    private void gameOverSequence() {
        offlineGamePanel.getAssetManager().getGameAsset().playGameFinishedSound(); // เล่นเสียงจบเกม
    }

    public OfflineGamePanel getofflineGamePanel() {
        return offlineGamePanel;
    }

    public OfflineLobbyPanel getOfflineLobby() {
        return offlineLobbyPanel;
    }

}
