package com.distortionstack.snakeladder.domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;

import com.distortionstack.snakeladder.include.config.GameLogical;

public abstract class GameLogicalManeger {
    //const
    
    private Point[] IndexLocation = new Point[101];
    //var

    //ArrayList 
    private ArrayList <PlayerData> playerList = new ArrayList<>();
    
    private int playerAmount = 0;
    private int CurrentTrunIndex = 0;
    private int roll = 0;

    //method
    public void diceRoll(){
        roll = new Random().nextInt(6)+1;
    }

    public void updateIndex(){
        GameStatus gameStatus = playerList.get(CurrentTrunIndex).getgStatus();
        if(gameStatus.getIndex() >= 100 || - gameStatus.getIndex() + getDiceRollValue() >= 100){
            System.out.println("You Win");
            gameStatus.setIndex(100);
            return;
        }
        gameStatus.setVisibleIndex(gameStatus.getIndex());
        gameStatus.setIndex(gameStatus.getIndex() + getDiceRollValue());
    }

    public void NextTurn(){
        CurrentTrunIndex++;
        if(CurrentTrunIndex >= playerList.size()){
            CurrentTrunIndex = 0;
        }
    }

   // 2. แก้ CheckLadderAndCSnakes ให้คืนค่า boolean (True = เจอ, False = ไม่เจอ)
    public boolean CheckLadderAndCSnakes(){
        if (playerList == null || playerList.isEmpty()) return false;

        GameStatus gameStatus = playerList.get(CurrentTrunIndex).getgStatus();
        boolean found = false;

        // เช็คบันได (Ladders)
        for (int[] jumpindex : GameLogical.LADDERS_UP) {
            if(gameStatus.getIndex() == jumpindex[0]){
                gameStatus.setIndex(jumpindex[1]); // อัปเดตเป้าหมายใหม่
                found = true;
                System.out.println("Found Ladder! Will Warp to " + jumpindex[1]);
                break;
            }
        }
        
        // เช็คงู (Snakes) - ถ้ายังไม่เจอบันไดค่อยเช็ค
        if(!found) { 
            for (int[] jumpindex : GameLogical.SNAKES_DOWN) {
                if(gameStatus.getIndex() == jumpindex[0]){
                    gameStatus.setIndex(jumpindex[1]); // อัปเดตเป้าหมายใหม่
                    found = true;
                    System.out.println("Found Snake! Will Warp to " + jumpindex[1]);
                    break;
                }
            }
        }
        
        return found; // ส่งผลลัพธ์กลับไปบอก Thread
    }

    //-getter
    public int getDiceRollValue(){
        if(roll == 0){
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

    public void addPlayer(JButton button){
        PlayerData player = new PlayerData();
        player.setSkincode(button.getText());
        playerList.add(player);
    }
    
    public ArrayList<PlayerData> getPlayerList() {
        return playerList;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }

    public Point[] getIndexLocation() {
        return IndexLocation;
    }
}