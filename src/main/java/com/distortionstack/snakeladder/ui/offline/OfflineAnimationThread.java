package com.distortionstack.snakeladder.ui.offline;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.domain.TurnResult;
import com.distortionstack.snakeladder.include.config.ThreadConfig;
import com.distortionstack.snakeladder.ui.AnimationThread;

/**
 * รัน animation หนึ่งเทิร์น
 *
 * รู้แค่ TurnResult — ไม่ต้องถือ reference ของ GameLogicalManager อีกต่อไป
 * ลำดับ: เดินทีละช่อง → (ถ้า warp: เล่น warp animation) → แจ้ง onFinished
 */
class OfflineAnimationThread extends AnimationThread {

    private final Runnable      onFinished; // callback เมื่อ animation จบ
    private OfflineGamePanel gamePanel;

    OfflineAnimationThread(PlayerData playerData,
                           OfflineGamePanel offlineGamePanel,
                           TurnResult turnResult,
                           Runnable onFinished) {
        super(playerData, offlineGamePanel, turnResult);
        this.onFinished = onFinished;
        gamePanel = offlineGamePanel;

        // เริ่มเล่น dice animation ก่อน แล้วค่อย start() thread เดิน
        offlineGamePanel.playDiceAnimation(turnResult.getDiceValue(), this::start);
    }

    // ─────────────────────────────────────────────
    //  Main animation loop
    // ─────────────────────────────────────────────

    @Override
    public void run() {
        currentVisual = turnResult.getStartIndex();    // จุดออกเดิน
        int walkTarget = turnResult.getWalkEndIndex(); // จุดหลังเดิน (ก่อน warp)

        try {
            walkToTarget(walkTarget);

            if (turnResult.isWarp()) {
                playWarpAnimation(walkTarget, turnResult.getFinalIndex());
                currentVisual = turnResult.getFinalIndex();
                gamePanel.setVisiblePlayerIndex(playerData, currentVisual);
            }

            repaintBoard();

            if (turnResult.isWin()) {
                handleWin();
            } else {
                handleTurnEnd();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("OfflineAnimationThread interrupted: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  Steps
    // ─────────────────────────────────────────────

    /**
     * เดินทีละช่องจนถึง target พร้อมเล่นเสียงและ repaint ทุก frame
     */
    private void walkToTarget(int target) throws InterruptedException {
        while (currentVisual < target) {
            sleep(ThreadConfig.ANIMATION_FRAME_DELAY_MS);
            gamePanel.getAssetManager().getGameAsset().playPlayerMovedSound();
            advanceOneStep();
            repaintBoard();
        }
    }

    /**
     * เล่น warp animation (UFO/งู/บันได) จาก warpFrom → warpTo
     */
    private void playWarpAnimation(int warpFrom, int warpTo) throws InterruptedException {
        JFrame warpFrame = gamePanel.getAnimateUFO(warpTo, warpFrom);
        gamePanel.getAssetManager().getGameAsset().playWarpSound();
        warpFrame.setVisible(true);
        sleep(ThreadConfig.SNAKE_WARP_ANIMATION_DURATION_MS);
        warpFrame.dispose();
    }

    private void handleWin() {
        System.out.println("🎉 Game Finished!");
        gamePanel.getAssetManager().getGameAsset().playGameFinishedSound();
        SwingUtilities.invokeLater(() ->
            gamePanel.getDisplayController().showFinishScreen(playerData.getSkincode())
        );
        // ไม่ปลดล็อคปุ่ม เพราะเกมจบแล้ว
        onFinished.run();
    }

    private void handleTurnEnd() {
        System.out.println("Turn Finished.");
        onFinished.run(); // Coordinator จะปลดล็อกปุ่มให้
    }

    // ─────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────

    private synchronized void advanceOneStep() {
        currentVisual++;
        gamePanel.setVisiblePlayerIndex(playerData , currentVisual);
    }

    private void repaintBoard() {
        SwingUtilities.invokeLater(gamePanel::repaint);
    }

    public int getCurrentVisual(){
        return currentVisual;
    }
}