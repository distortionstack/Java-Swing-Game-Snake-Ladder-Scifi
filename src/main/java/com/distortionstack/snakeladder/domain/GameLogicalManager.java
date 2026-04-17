package com.distortionstack.snakeladder.domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.distortionstack.snakeladder.include.config.GameLogical;

/**
 * ผู้รับผิดชอบ Logic ทั้งหมดของเกม
 *
 * กฎสำคัญ:
 *   - ทุก state ของเกมเปลี่ยนได้เฉพาะในคลาสนี้เท่านั้น
 *   - ฝั่ง UI/Coordinator อ่านผลผ่าน TurnResult และ getter เท่านั้น
 *   - ไม่มี method ใดที่ต้องเรียกจากภายนอกหลาย ๆ ครั้งแทน playTurn()
 */
public abstract class GameLogicalManager {

    private final Point[] indexLocation = new Point[101];
    private final ArrayList<PlayerData> playerList = new ArrayList<>();

    private int currentTurnIndex = 0;
    private int roll = 0;

    // ─────────────────────────────────────────────
    //  Public API  (ฝั่ง Coordinator เรียกได้)
    // ─────────────────────────────────────────────

    /**
     * เล่นหนึ่งเทิร์นครบวงจร แล้ว return ผลให้ UI เอาไปแสดง
     *
     * ลำดับ: ทอย → บันทึก startIndex → เดิน → เช็ค warp → เช็ค win → เปลี่ยนเทิร์น
     */
    public TurnResult playTurn() {
        rollDice();

        PlayerData player  = getCurrentPlayer();
        GameStatus status  = player.getgStatus();

        int startIndex     = status.getIndex();
        int walkEndIndex   = calcWalkEnd(startIndex, roll);

        // อัปเดต index หลังเดิน
        status.setVisibleIndex(startIndex);
        status.setIndex(walkEndIndex);

        // เช็ค warp (งู/บันได)
        boolean warped    = applyWarpIfNeeded(status);
        int finalIndex    = status.getIndex();

        // เช็ค win
        boolean won       = checkAndMarkWin(status);

        // เปลี่ยนเทิร์นหลังสุด
        advanceTurn();

        return new TurnResult(roll, startIndex, walkEndIndex, finalIndex, warped, won);
    }

    public void addPlayer(String skinCode) {
        PlayerData player = new PlayerData();
        player.setSkincode(skinCode);
        playerList.add(player);
    }

    public boolean isGameOver() {
        for (PlayerData p : playerList) {
            if (p.getgStatus().isWinner()) return true;
        }
        return false;
    }

    // ─────────────────────────────────────────────
    //  Getters
    // ─────────────────────────────────────────────

    public PlayerData getCurrentPlayer() {
        return playerList.get(currentTurnIndex);
    }

    public ArrayList<PlayerData> getPlayerList() { return playerList; }
    public Point[]               getIndexLocation() { return indexLocation; }
    public int                   getDiceRollValue()  { return roll; }

    // ─────────────────────────────────────────────
    //  Private helpers
    // ─────────────────────────────────────────────

    private void rollDice() {
        roll = new Random().nextInt(6) + 1;
    }

    /**
     * คำนวณตำแหน่งหลังเดิน โดยไม่เกิน 100
     */
    private int calcWalkEnd(int current, int dice) {
        int next = current + dice;
        return Math.min(next, 100);
    }

    /**
     * ตรวจสอบและ apply warp (งู/บันได)
     * @return true ถ้าโดน warp
     */
    private boolean applyWarpIfNeeded(GameStatus status) {
        if (applyWarp(status, GameLogical.LADDERS_UP,   "Ladder")) return true;
        if (applyWarp(status, GameLogical.SNAKES_DOWN,  "Snake"))  return true;
        return false;
    }

    private boolean applyWarp(GameStatus status, int[][] warpMap, String label) {
        for (int[] warp : warpMap) {
            if (status.getIndex() == warp[0]) {
                System.out.println("Found " + label + "! Warp → " + warp[1]);
                status.setIndex(warp[1]);
                return true;
            }
        }
        return false;
    }

    /**
     * เช็ควิน และ mark ถ้าชนะ
     * @return true ถ้าชนะ
     */
    private boolean checkAndMarkWin(GameStatus status) {
        if (status.getIndex() >= 100) {
            status.setIndex(100);
            status.setWinner(true);
            System.out.println("Player wins!");
            return true;
        }
        return false;
    }

    private void advanceTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % playerList.size();
    }
}