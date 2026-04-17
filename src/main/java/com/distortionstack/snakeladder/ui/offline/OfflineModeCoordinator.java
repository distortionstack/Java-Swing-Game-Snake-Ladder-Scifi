package com.distortionstack.snakeladder.ui.offline;

import javax.swing.JButton;

import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.domain.TurnResult;
import com.distortionstack.snakeladder.domain.offline.OfflineGameLogicalManeger;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.DisplayController;

public class OfflineModeCoordinator {

    private final OfflineGameLogicalManeger offlineLogic;
    private final OfflineLobbyPanel         offlineLobbyPanel;
    private final OfflineGamePanel          offlineGamePanel;
    private final DisplayController         displayController;

    public OfflineModeCoordinator(OfflineGameLogicalManeger offlineLogic,
                                  AssetManager assetManager,
                                  DisplayController displayController) {
        this.offlineLogic      = offlineLogic;
        this.displayController = displayController;

        offlineLobbyPanel = new OfflineLobbyPanel(assetManager, displayController);
        offlineGamePanel  = new OfflineGamePanel(assetManager, offlineLogic, displayController);

        bindEvents();
    }

    // ─────────────────────────────────────────────
    //  Event binding
    // ─────────────────────────────────────────────

    private void bindEvents() {
        offlineLobbyPanel.getStartButton().addActionListener(e -> onStartGameClicked());
        for (JButton button : offlineLobbyPanel.getSelectSkinButton()) {
            button.addActionListener(e -> onSkinSelected(button));
        }
        offlineGamePanel.addDiceButtonListener(e -> onDiceRolled());
    }

    // ─────────────────────────────────────────────
    //  Event handlers
    // ─────────────────────────────────────────────

    private void onStartGameClicked() {
        if (!offlineLogic.getPlayerList().isEmpty()) {
            displayController.startOfflineGame();
        } else {
            offlineLobbyPanel.EmptyPlayerAlert();
        }
    }

    private void onSkinSelected(JButton button) {
        offlineLogic.addPlayer(button.getText());
        offlineLobbyPanel.handleSkinSelect(button);
    }

    private void onDiceRolled() {
        offlineGamePanel.blockDiceButton();

        // ดึงผู้เล่น **ก่อน** playTurn() เพราะ playTurn() จะ advanceTurn() ไปแล้ว
        PlayerData currentPlayer = offlineLogic.getCurrentPlayer();

        // Logic ทำงานครั้งเดียว — Coordinator แค่รับผลลัพธ์
        TurnResult result = offlineLogic.playTurn();

        playSoundsForTurn();

        startAnimationSequence(currentPlayer, result);
    }

    // ─────────────────────────────────────────────
    //  Private helpers
    // ─────────────────────────────────────────────

    private void playSoundsForTurn() {
        offlineGamePanel.getAssetManager().getGameAsset().playDiceRollSound();
        offlineGamePanel.getAssetManager().getGameAsset().playDiceRollingSound();
    }

    /**
     * สร้าง OfflineAnimationThread โดยส่ง onFinished callback เข้า constructor
     *
     * ❌ ไม่ต้องเรียก .start() เอง
     *    เพราะ constructor ส่ง this::start ให้ playDiceAnimation เป็น callback อยู่แล้ว
     *    animation ลูกเต๋าจบ → start() ถูกเรียกอัตโนมัติ
     */
    private void startAnimationSequence(PlayerData player, TurnResult result) {
        Runnable onFinished = result.isWin()
                ? this::onGameOver
                : offlineGamePanel::unblockDiceButton;

        new OfflineAnimationThread(player, offlineGamePanel, result, onFinished);
    }

    private void onGameOver() {
        offlineGamePanel.getAssetManager().getGameAsset().playGameFinishedSound();
        // TODO: displayController.showFinishScreen(...)
    }

    // ─────────────────────────────────────────────
    //  Getters
    // ─────────────────────────────────────────────

    public OfflineGamePanel  getOfflineGamePanel() { return offlineGamePanel;  }
    public OfflineLobbyPanel getOfflineLobby()     { return offlineLobbyPanel; }
}