package com.distortionstack.snakeladder.ui;

import com.distortionstack.snakeladder.include.config.ThreadConfig;
import javax.swing.*;

public class DiceAnimation {
    private Timer spinTimer;
    private int spinCounter;
    private int targetFace; 
    private Runnable onFinish;
    
    // เปลี่ยนจาก Coordinator เป็น GamePanel
    private GamePanel gamePanel; 

    public DiceAnimation(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void play(int result, Runnable onFinish) {
        if (spinTimer != null) {
            spinTimer.stop();
            spinTimer = null; // มั่นใจว่าตัวเก่าหลุดจากหน่วยความจำ
        }

        this.targetFace = result;
        this.onFinish = onFinish;
        this.spinCounter = 0;

        // เรียกผ่าน gamePanel
        gamePanel.getOverlay().setVisible(true);
        gamePanel.showFace(1); 
        
        spinTimer = new Timer(ThreadConfig.SPIN_DELAY_MS, e -> tick());
        spinTimer.start();
    }

    private void tick() {
        spinCounter++;

        if (spinCounter > ThreadConfig.SPIN_COUNT - 6) {
            spinTimer.setDelay(
                    ThreadConfig.SPIN_DELAY_MS + (spinCounter - (ThreadConfig.SPIN_COUNT - 6)) * 40);
        }

        if (spinCounter >= ThreadConfig.SPIN_COUNT) {
            spinTimer.stop();
            gamePanel.showFace(targetFace);
            Timer hold = new Timer(ThreadConfig.HOLD_MS, e -> {
                gamePanel.getOverlay().setVisible(false);
                if (onFinish != null)
                    onFinish.run();
            });
            hold.setRepeats(false);
            hold.start();
            return;
        }

        int randomFace = (int) (Math.random() * 6) + 1;
        gamePanel.showFace(randomFace);
    }
}