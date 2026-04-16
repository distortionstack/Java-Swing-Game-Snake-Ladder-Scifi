package com.distortionstack.snakeladder.domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;

import com.distortionstack.snakeladder.include.config.GameLogical;

public abstract class GameLogicalManeger {
    // const

    private Point[] IndexLocation = new Point[101];
    // var

    // ArrayList
    private ArrayList<PlayerData> playerList = new ArrayList<>();

    private int playerAmount = 0;
    private int CurrentTrunIndex = 0;
    private int roll = 0;

    // method
    public void diceRoll() {
        roll = new Random().nextInt(6) + 1;
    }

    public void updateIndex() {
        GameStatus gameStatus = playerList.get(CurrentTrunIndex).getgStatus();
        if (gameStatus.getIndex() >= 100 || -gameStatus.getIndex() + getDiceRollValue() >= 100) {
            System.out.println("You Win");
            gameStatus.setIndex(100);
            return;
        }
        gameStatus.setVisibleIndex(gameStatus.getIndex());
        gameStatus.setIndex(gameStatus.getIndex() + getDiceRollValue());
    }

    public void NextTurn() {
        CurrentTrunIndex++;
        if (CurrentTrunIndex >= playerList.size()) {
            CurrentTrunIndex = 0;
        }
    }

    private boolean checkAndApplyWarp(int[][] warpMap, String warpType) {
        GameStatus gameStatus = playerList.get(CurrentTrunIndex).getgStatus();

        for (int[] warp : warpMap) {
            if (gameStatus.getIndex() == warp[0]) {
                int newIndex = warp[1];
                gameStatus.setIndex(newIndex);
                System.out.println("Found " + warpType + "! Will Warp to " + newIndex);
                return true;
            }
        }
        return false;
    }

    public boolean CheckLadderAndSnakes() {
        if (playerList == null || playerList.isEmpty())
            return false;

        // Check ladders first
        if (checkAndApplyWarp(GameLogical.LADDERS_UP, "Ladder")) {
            return true;
        }

        // Then check snakes
        return checkAndApplyWarp(GameLogical.SNAKES_DOWN, "Snake");
    }

    // -getter
    public int getDiceRollValue() {
        if (roll == 0) {
            diceRoll();
        }
        return roll;
    }

    public PlayerData getCurrentPlayer() {
        return playerList.get(CurrentTrunIndex);
    }

    public int getCurrentPlayerIndex() {
        return getCurrentPlayer().getgStatus().getIndex();
    }

    

    public void addPlayer(String skinCode) {
        PlayerData player = new PlayerData();
        player.setSkincode(skinCode);
        // --------- [เพิ่มโค้ดทดสอบตรงนี้] ---------
        player.getgStatus().setIndex(98);
        player.getgStatus().setVisibleIndex(98);
        // ----------------------------------------
        playerList.add(player);
    }

    public ArrayList<PlayerData> getPlayerList() {
        return playerList;
    }

    public boolean isGameOver() {
        for (PlayerData player : playerList) {
            if (player.getgStatus().isWinner()) {
                return true;
            }
        }
        return false;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }

    public Point[] getIndexLocation() {
        return IndexLocation;
    }
}