package com.distortionstack.snakeladder.ui.offline;

import javax.swing.JFrame;

import com.distortionstack.snakeladder.domain.GameStatus;
import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.domain.offline.OfflineGameLogicalManeger;
import com.distortionstack.snakeladder.ui.AnimationThread;

class OfflineAnimationThread extends AnimationThread{
    OfflineGameLogicalManeger gameLogical;
    OfflineAnimationThread(PlayerData playerData , OfflineGamePanel offlineGamePanel , OfflineGameLogicalManeger gameLogical){
        super(playerData, offlineGamePanel);
        this.gameLogical = gameLogical;
        start();
    }
    @Override
    public void run() {
        GameStatus status = playerData.getgStatus();
        currentVisual = status.getVisibleIndex();
        targetIndex = status.getIndex();
        
        gamePanel.BlockDiceButton(); // ล็อกปุ่ม

        while (running) {
            // 1. ถ้าเดินมาถึงช่องเป้าหมาย (ตามลูกเต๋า) แล้ว
            if (currentVisual == targetIndex) { 
                
                // 2. เช็คดูซิว่า ตรงนี้มี งู หรือ บันได ไหม?
                boolean needWarp = gameLogical.CheckLadderAndCSnakes();
                
                if (needWarp) {
                    // ถ้าเจอ: ให้หน่วงเวลา + วาร์ป
                    try {
                        // A. หน่วงเวลา 0.8 วินาที ให้คนเห็นว่าตกช่องนี้
                       
                        // B. วาร์ป! (ดึงค่า Index ใหม่ที่เปลี่ยนแล้วมาแสดงเลย)
                        targetIndex = status.getIndex(); // ค่านี้ถูกแก้ใน CheckLadder... แล้ว
                        JFrame animatFrame = gamePanel.getAnimateUFO(targetIndex,currentVisual);
                        animatFrame.setVisible(true);
                        sleep(2000); 
                        animatFrame.dispose();
                        currentVisual = targetIndex;     // วาร์ปตัวแปร Visual
                        
                        status.setVisibleIndex(currentVisual); // อัปเดตจอทันที
                        gamePanel.repaint();
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 3. จบเทิร์น (ไม่ว่าจะวาร์ปหรือไม่วาร์ป ก็จบเทิร์นตรงนี้)
                System.out.println("Turn Finished.");
                gamePanel.UnBlockDiceButton();
                gameLogical.NextTurn(); 
                return; // ออกจาก Thread
            }
            try {
                sleep(sleepDuration);
                // ขยับ 1 ช่อง
                currentVisual++;
                status.setVisibleIndex(currentVisual);
                gamePanel.repaint();
            } catch (InterruptedException e) {
                System.out.println("AnimationThread Eror: " + e.getMessage());
            }
        }
    }
}
