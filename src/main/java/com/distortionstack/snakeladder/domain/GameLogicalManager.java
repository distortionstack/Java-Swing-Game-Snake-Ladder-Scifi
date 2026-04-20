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

    public static final int START_INDEX = 0;
    public static final int END_INDEX   = 100;

    private int currentTurnIndex = 0; // เริ่มที่ผู้เล่นคนแรก (index 0)
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
        status.setIndex(walkEndIndex);

        // เช็ค warp (งู/บันได)
        boolean warped    = applyWarpIfNeeded(status);
        int finalIndex    = status.getIndex();

        // เช็ค win
        boolean won       = checkAndMarkWin(status);

        // เปลี่ยนเทิร์นหลังสุด
        advanceTurn();

        System.out.println("Turn played: Player " + (currentTurnIndex + 1) + " rolled " + roll +
                           ", moved from " + startIndex + " to " + walkEndIndex +
                           (warped ? " and warped to " + finalIndex : "") +
                           (won ? " and won the game!" : ""));

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

    public void resetGame() {
        //เริ่่มเกมใหม่: รีเซ็ตสถานะผู้เล่นทุกคน, เริ่มเทิร์นที่ผู้เล่นคนแรก, รีเซ็ตค่า roll
        for (PlayerData p : playerList) {
            p.getgStatus().setIndex(START_INDEX);
            p.getgStatus().setWinner(false);
        }
        currentTurnIndex = 0;
        roll = 0;
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
     * คำนวณตำแหน่งหลังเดิน โดยไม่เกิน   
     */
    private int calcWalkEnd(int current, int dice) {
        int next = current + dice;
        return Math.min(next, GameLogicalManager.END_INDEX);
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
        if (status.getIndex() >= GameLogicalManager.END_INDEX) {
            status.setIndex(GameLogicalManager.END_INDEX);
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