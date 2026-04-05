package com.distortionstack.snakeladder.ui.offline;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import java.util.List;

import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.domain.offline.OfflineGameLogicalManeger;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.include.config.GameUI;
import com.distortionstack.snakeladder.ui.DisplayController;
import com.distortionstack.snakeladder.ui.GamePanel;

public class OfflineGame extends GamePanel {
    OfflineGameLogicalManeger logical;

    public OfflineGame(AssetManager assetManager, OfflineGameLogicalManeger logical,DisplayController displayController) {
        super(assetManager,displayController);
        this.logical = logical;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<PlayerData> pArrayList = logical.getPlayerList();

        if (pArrayList == null || pArrayList.isEmpty())
            return;

        // 1. จัดกลุ่มผู้เล่นตามช่องที่ยืน (Map: Index -> List of Players)
        Map<Integer, List<PlayerData>> playersAtIndex = new HashMap<>();
        for (PlayerData p : pArrayList) {
            int idx = p.getgStatus().getVisibleIndex();
            playersAtIndex.computeIfAbsent(idx, k -> new ArrayList<>()).add(p);
        }

        // 2. วาดผู้เล่นทีละช่อง
        for (Integer index : playersAtIndex.keySet()) {
            List<PlayerData> playersInThisBlock = playersAtIndex.get(index);
            int count = playersInThisBlock.size();

            // เช็คว่า Index ถูกต้องและมีพิกัดจริง
            if (index < onePlayerPoint.length && onePlayerPoint[index] != null) {
                Point basePos = onePlayerPoint[index];

                // 3. วาดทุกคนในกลุ่มตามตำแหน่ง "หน้าลูกเต๋า"
                for (int i = 0; i < count; i++) {
                    ImageIcon skin = assetManager
                            .getGameAsset()
                            .getPlayerSkin(
                                    playersInThisBlock.get(i)
                                            .getSkincode());
                    if (skin != null) {
                        // คำนวณ Offset (ระยะเยื้องจากจุดกึ่งกลาง)
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

    // ฟังก์ชันช่วยคำนวณตำแหน่ง (Pattern หน้าลูกเต๋า)
    private Point getDiceOffset(int totalPlayers, int playerIndex) {
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
}
