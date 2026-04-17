package com.distortionstack.snakeladder.ui;

import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.domain.TurnResult;

/**
 * AnimationThread รับ TurnResult ที่ Logic คำนวณไว้แล้ว
 * ไม่จำเป็นต้องถือ reference ของ GameLogicalManager อีกต่อไป
 *
 * Subclass override run() แล้วอ่าน turnResult เอาไปใช้เล่น animation ได้เลย
 */
public abstract class AnimationThread extends Thread {

    protected final PlayerData playerData;
    protected final GamePanel  gamePanel;
    protected final TurnResult turnResult;  // ผลของเทิร์นนี้ — อ่านอย่างเดียว

    protected boolean running      = true;
    protected int     currentVisual;        // index ที่กำลังแสดงอยู่ระหว่าง animation

    public AnimationThread(PlayerData playerData, GamePanel gamePanel, TurnResult turnResult) {
        this.playerData = playerData;
        this.gamePanel  = gamePanel;
        this.turnResult = turnResult;
        this.currentVisual = turnResult.getStartIndex(); // เริ่มต้นที่จุดออกเดินทาง
    }
}