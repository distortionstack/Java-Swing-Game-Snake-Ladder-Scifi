package com.distortionstack.snakeladder.ui.offline;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
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

    static final class AnimationCompletion {
        private final boolean win;
        private final String winnerSkinCode;

        AnimationCompletion(boolean win, String winnerSkinCode) {
            this.win = win;
            this.winnerSkinCode = winnerSkinCode;
        }

        boolean isWin() {
            return win;
        }

        String getWinnerSkinCode() {
            return winnerSkinCode;
        }
    }

    private final Consumer<AnimationCompletion> onFinished; // callback เมื่อ animation จบ
    private final OfflineGamePanel gamePanel;

    OfflineAnimationThread(PlayerData playerData,
                           OfflineGamePanel offlineGamePanel,
                           TurnResult turnResult,
                           Consumer<AnimationCompletion> onFinished) {
        super(playerData, offlineGamePanel, turnResult);
        this.onFinished = onFinished;
        this.gamePanel = offlineGamePanel;

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
            notifyCompletion(turnResult.isWin());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("OfflineAnimationThread interrupted: " + e.getMessage());
            notifyCompletion(false);
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
        gamePanel.getAssetManager().getGameAsset().playWarpSound();
        runOnEdtAndWait(() -> gamePanel.showWarpAnimationIcon(warpTo, warpFrom));
        sleep(ThreadConfig.SNAKE_WARP_ANIMATION_DURATION_MS);
        runOnEdtAndWait(gamePanel::hideWarpAnimationIcon);
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

    private void notifyCompletion(boolean isWin) {
        if (onFinished == null) {
            return;
        }
        onFinished.accept(new AnimationCompletion(isWin, isWin ? playerData.getSkinCode() : null));
    }

    private void runOnEdtAndWait(Runnable action) throws InterruptedException {
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to run warp animation action on EDT", e);
        }
    }

    public int getCurrentVisual(){
        return currentVisual;
    }
}
