package com.distortionstack.snakeladder.ui;
import com.distortionstack.snakeladder.domain.GameLogicalManeger;
import com.distortionstack.snakeladder.domain.PlayerData;

public abstract class AnimationThread extends Thread{
    protected PlayerData playerData;
    protected GamePanel gamePanel;
    protected GameLogicalManeger gamelogical;
    protected boolean running = true;
    protected int currentVisual;
    protected int targetIndex;
    public AnimationThread(PlayerData playerData , GamePanel gamePanel){
        this.playerData = playerData;
        this.gamePanel = gamePanel;
    }
}

