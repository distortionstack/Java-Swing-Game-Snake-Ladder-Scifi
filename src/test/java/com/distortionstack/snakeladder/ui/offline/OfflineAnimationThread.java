package com.distortionstack.snakeladder.ui.offline;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.distortionstack.snakeladder.domain.GameStatus;
import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.domain.offline.OfflineGameLogicalManeger;
import com.distortionstack.snakeladder.include.config.ThreadConfig;
import com.distortionstack.snakeladder.ui.AnimationThread;

class OfflineAnimationThread extends AnimationThread {
    OfflineGameLogicalManeger gameLogical;
    GameStatus status;

    OfflineAnimationThread(PlayerData playerData, OfflineGamePanel offlineGamePanel,
            OfflineGameLogicalManeger gameLogical) {
        super(playerData, offlineGamePanel);
        this.gameLogical = gameLogical;

        // ดึง result จาก status ที่ logic คำนวณไว้แล้ว
        int diceResult = gameLogical.getDiceRollValue(); // หรือ method ที่ logic มี
        offlineGamePanel.playDiceAnimation(diceResult, this::start);
    }

    @Override
    public void run() {
        status = playerData.getgStatus();
        currentVisual = status.getVisibleIndex();
        targetIndex = status.getIndex();

        gamePanel.BlockDiceButton(); // ล็อกปุ่ม

        while (running) {
            // 1. ถ้าเดินมาถึงช่องเป้าหมาย (ตามลูกเต๋า) แล้ว
            if (currentVisual == targetIndex) {

                // 2. เช็คดูซิว่า ตรงนี้มี งู หรือ บันได ไหม?
                boolean needWarp = gameLogical.CheckLadderAndSnakes();

                if (needWarp) {
                    // ถ้าเจอ: ให้หน่วงเวลา + วาร์ป
                    try {
                        // B. วาร์ป! (ดึงค่า Index ใหม่ที่เปลี่ยนแล้วมาแสดงเลย)
                        targetIndex = status.getIndex(); // ค่านี้ถูกแก้ใน CheckLadder... แล้ว
                        JFrame animatFrame = gamePanel.getAnimateUFO(targetIndex, currentVisual);
                        gamePanel.getAssetManager().getGameAsset().playWarpSound(); // เล่นเสียงวาร์ป
                        animatFrame.setVisible(true);
                        sleep(ThreadConfig.SNAKE_WARP_ANIMATION_DURATION_MS);
                        animatFrame.dispose();
                        currentVisual = targetIndex; // วาร์ปตัวแปร Visual

                        status.setVisibleIndex(currentVisual); // อัปเดตจอทันที
                        gamePanel.repaint();

                    } catch (InterruptedException e) {
                        System.out.println("Warp Animation Interrupted: " + e.getMessage());
                        e.printStackTrace();
                        Thread.currentThread().interrupt(); // restore interrupt status
                    }
                }

                if (currentVisual >= 100) {
                    System.out.println("🎉 Game Finished!");
                    
                    // เล่นเสียงจบเกม (ใช้ Key: game.status.finish ตาม XML ล่าสุด)
                    gamePanel.getAssetManager().getGameAsset().playGameFinishedSound();
                    
                    // ปลดบล็อกปุ่มเพื่อให้ไปจัดการหน้าจอจบเกมต่อได้ (หรือจะปล่อยล็อกไว้ก็ได้)
                    gamePanel.UnBlockDiceButton();

                    // 3. สลับหน้าจอไปที่ FinishPanel (ใช้ InvokeLater เพื่อให้ UI อัปเดตถูกต้อง)
                        SwingUtilities.invokeLater(() -> {
                            gamePanel.getDisplayController().showFinishScreen(playerData.getSkincode());
                        });
                    
                    return; // 🛑 จบ Thread ตรงนี้เลย ไม่ต้องไปเรียก NextTurn
                }

                // 3. จบเทิร์น (ไม่ว่าจะวาร์ปหรือไม่วาร์ป ก็จบเทิร์นตรงนี้)
                System.out.println("Turn Finished.");
                gamePanel.UnBlockDiceButton();
                gameLogical.NextTurn();
                return; // ออกจาก Thread
            }
            try {
                gamePanel.getAssetManager().getGameAsset().playPlayerMovedSound(); // เล่นเสียงเดิน
                sleep(ThreadConfig.ANIMATION_FRAME_DELAY_MS);
                // ขยับ 1 ช่องทีละน้อยๆ จนกว่าจะถึงเป้าหมาย
                updateGameStatus(currentVisual + 1);
                SwingUtilities.invokeLater(() -> gamePanel.repaint());
            } catch (InterruptedException e) {
                System.out.println("AnimationThread Eror: " + e.getMessage());
            }
        }
    }

    private synchronized void updateGameStatus(int newVisualIndex) {
        currentVisual = newVisualIndex;
        status.setVisibleIndex(currentVisual);
    }
}
