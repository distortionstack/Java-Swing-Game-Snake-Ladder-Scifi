package com.distortionstack.snakeladder.ui.offline;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import java.util.List;

import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.domain.offline.OfflineGameLogicalManager;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.include.config.GameLogical;
import com.distortionstack.snakeladder.include.config.GameUI;
import com.distortionstack.snakeladder.ui.DisplayController;
import com.distortionstack.snakeladder.ui.GamePanel;

public class OfflineGamePanel extends GamePanel {
    OfflineGameLogicalManager logical;
    // ใน OfflineGamePanel ก็ประกาศแค่:
    private List<PlayerViewState> playerViewStates = new ArrayList<>();

    public OfflineGamePanel(AssetManager assetManager, OfflineGameLogicalManager logical,DisplayController displayController) {
        super(assetManager,displayController);
        this.logical = logical;
        
        // ทำการ Sync ค่าจาก Model มายัง View
        syncPlayerPositions();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPlayer(g);
    }

    @Override
    public void drawPlayer(Graphics g) {   
         if(playerViewStates.isEmpty()) return;

        // 1. จัดกลุ่มผู้เล่นตามช่องที่ยืน "บนหน้าจอ" (visualIndex)
        Map<Integer, List<PlayerData>> playersAtIndex = new HashMap<>();
        for (PlayerViewState state : playerViewStates) {
            playersAtIndex.computeIfAbsent(state.visualIndex, k -> new ArrayList<>()).add(state.player);
        }

        // 2. วาดผู้เล่นทีละช่อง
        for (Map.Entry<Integer, List<PlayerData>> entry : playersAtIndex.entrySet()) {
            int index = entry.getKey();
            List<PlayerData> playersInThisBlock = entry.getValue();
            int count = playersInThisBlock.size();

            // เช็คว่า Index ถูกต้องและมีพิกัดจริง
            if (index >= 0 && index < onePlayerPoint.length && onePlayerPoint[index] != null) {
                Point basePos = onePlayerPoint[index];

                // 3. วาดทุกคนในกลุ่มตามตำแหน่ง "หน้าลูกเต๋า"
                for (int i = 0; i < count; i++) {
                    ImageIcon skin = assetManager.getGameAsset().getPlayerSkin(playersInThisBlock.get(i).getSkinCode());
                    
                    if (skin != null) {
                        Point offset = getDiceOffset(count, i);
                        g.drawImage(skin.getImage(),
                                basePos.x + offset.x,
                                basePos.y + offset.y,
                                GameUI.PLAYER_SIZE.width, GameUI.PLAYER_SIZE.height, this);
                    }
                }
            }
        }
    }

    public void setVisiblePlayerIndex(PlayerData player , int index) {
        for (PlayerViewState state : playerViewStates) {
            if (state.player.equals(player)) {
                state.visualIndex = index;
                break;
            }
        }
    }

    public void syncPlayerPositions() {
        playerViewStates.clear();
        if (logical != null && logical.getPlayerList() != null) {
            for (PlayerData p : logical.getPlayerList()) {
                PlayerViewState state = new PlayerViewState();
                state.player = p;
                state.visualIndex = GameLogical.START_INDEX; // ดึงค่าเริ่มต้น
                playerViewStates.add(state);
            }
        }
    }

    public void addDiceButtonListener(ActionListener listener){
        diceButton.addActionListener(listener);
    }

    // ฟังก์ชันช่วยคำนวณตำแหน่ง (Pattern หน้าลูกเต๋า)
    private Point getDiceOffset(int totalPlayers, int playerIndex) {
        if (totalPlayers <= 0 || playerIndex < 0 || playerIndex >= totalPlayers) {
            return new Point(0, 0);  // default safe value
        }
        // ถ้าคนเยอะเกิน 6 คน ให้เรียงแบบหน้า 6 ไปเรื่อยๆ หรือซ้อนกัน
        if (totalPlayers > 6)
            totalPlayers = 6;

        switch (totalPlayers) {
            case 1: // จุดตรงกลาง
                return new Point(0, 0);
            case 2: // เฉียงซ้ายบน - ขวาล่าง
                if (playerIndex == 0)
                    return new Point(-GAP, -GAP);
                return new Point(GAP, GAP);
            case 3: // เฉียง 3 จุด
                if (playerIndex == 0)
                    return new Point(-GAP, -GAP);
                if (playerIndex == 1)
                    return new Point(0, 0);
                return new Point(GAP, GAP);
            case 4: // 4 มุม
                if (playerIndex == 0)
                    return new Point(-GAP, -GAP); // ซ้ายบน
                if (playerIndex == 1)
                    return new Point(GAP, -GAP); // ขวาบน
                if (playerIndex == 2)
                    return new Point(-GAP, GAP); // ซ้ายล่าง
                return new Point(GAP, GAP); // ขวาล่าง
            case 5: // 4 มุม + ตรงกลาง
                if (playerIndex == 0)
                    return new Point(-GAP, -GAP);
                if (playerIndex == 1)
                    return new Point(GAP, -GAP);
                if (playerIndex == 2)
                    return new Point(0, 0); // กลาง
                if (playerIndex == 3)
                    return new Point(-GAP, GAP);
                return new Point(GAP, GAP);
            case 6: // 2 แถว แถวละ 3 (แนวตั้ง)
            default:
                if (playerIndex == 0)
                    return new Point(-GAP, -GAP);
                if (playerIndex == 1)
                    return new Point(GAP, -GAP);
                if (playerIndex == 2)
                    return new Point(-GAP, 0);
                if (playerIndex == 3)
                    return new Point(GAP, 0);
                if (playerIndex == 4)
                    return new Point(-GAP, GAP);
                return new Point(GAP, GAP);
        }
    }

    private class PlayerViewState {
        PlayerData player;
        int visualIndex;
        
        // เอาไว้เก็บพิกัด หรือข้อมูลอนิเมชันเพิ่มในอนาคตได้ตรงนี้
    }

}
