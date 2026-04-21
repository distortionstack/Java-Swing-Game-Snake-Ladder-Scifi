package com.distortionstack.snakeladder.ui.offline;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.domain.TurnResult;
import com.distortionstack.snakeladder.domain.offline.OfflineGameLogicalManager;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.DisplayController;

public class OfflineModeCoordinator {

    private final OfflineGameLogicalManager offlineLogic;
    private final OfflineLobbyPanel         offlineLobbyPanel;
    private final OfflineGamePanel          offlineGamePanel;
    private final DisplayController         displayController;
    private volatile boolean animationCompletedFlag;
    private volatile boolean gameOverFlag;
    private volatile String winnerSkinCode;

    public OfflineModeCoordinator(OfflineGameLogicalManager offlineLogic,
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

        offlineGamePanel.addHomeButtonListener(e -> onGameExit());
    }

    // ─────────────────────────────────────────────
    //  Event handlers
    // ─────────────────────────────────────────────

    private void onStartGameClicked() {
        if (!offlineLogic.getPlayerList().isEmpty()) {
            offlineGamePanel.syncPlayerPositions();
            displayController.startOfflineGame();
        } else {
            offlineLobbyPanel.showEmptyPlayerAlert();
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
        animationCompletedFlag = false;
        gameOverFlag = false;
        winnerSkinCode = null;

        new OfflineAnimationThread(player, offlineGamePanel, result, this::onAnimationCompleted);
    }

    private void onAnimationCompleted(OfflineAnimationThread.AnimationCompletion completion) {
        gameOverFlag = completion.isWin();
        winnerSkinCode = completion.getWinnerSkinCode();
        animationCompletedFlag = true;

        // ให้ฝั่ง UI เป็นคนอ่าน flag และตัดสินใจขั้นถัดไป
        SwingUtilities.invokeLater(this::processAnimationCompletion);
    }

    private void processAnimationCompletion() {
        if (!animationCompletedFlag) {
            return;
        }

        animationCompletedFlag = false;
        if (gameOverFlag) {
            onGameOver();
        } else {
            offlineGamePanel.unblockDiceButton();
        }
    }

    private void onGameOver() {
        offlineGamePanel.getAssetManager().getGameAsset().playGameFinishedSound();
        if (winnerSkinCode != null) {
            displayController.showFinishScreen(winnerSkinCode);
        }
    }

    private void onGameExit() {
        int result = offlineGamePanel.showGameExitAlert();
        if (JOptionPane.OK_OPTION == result) {
            offlineLogic.exitGame();
            displayController.showMenu();
        }
    }

    // ─────────────────────────────────────────────
    //  Getters
    // ─────────────────────────────────────────────

    public OfflineGamePanel  getOfflineGamePanel() { return offlineGamePanel;  }
    public OfflineLobbyPanel getOfflineLobby()     { return offlineLobbyPanel; }
}
