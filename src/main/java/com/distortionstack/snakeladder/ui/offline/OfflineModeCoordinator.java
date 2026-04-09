package com.distortionstack.snakeladder.ui.offline;

import javax.swing.JButton;

import com.distortionstack.snakeladder.domain.offline.OfflineGameLogicalManeger;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.DisplayController;

public class OfflineModeCoordinator{
    OfflineGameLogicalManeger offlineLogic;
    OfflineLobbyPanel offlineLobbyPanel;
    OfflineGamePanel offlineGamePanel;
    DisplayController displayController;

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
        // ทำ Logic ให้เสร็จ
        offlineLogic.diceRoll();
        offlineLogic.updateIndex();
        
        // แสดงผล
        System.out.println("Target: " + offlineLogic.getCurrentPlayerIndex());
        
        // สั่ง Animation (อาจจะแยกเป็นเมธอดเดินตัวละครโดยเฉพาะ)
        executeWalkingSequence();
    }

    private void executeWalkingSequence() {

        new OfflineAnimationThread(
            offlineLogic.getCurrentPlayer(),
            offlineGamePanel,
            offlineLogic
        );
    }

    public OfflineGamePanel getofflineGamePanel() {
        return offlineGamePanel;
    }

    public OfflineLobbyPanel getOfflineLobby() {
        return offlineLobbyPanel;
    }

}
