package com.distortionstack.snakeladder.ui;

import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.offline.OfflineGame;
import com.distortionstack.snakeladder.ui.offline.OfflineLobby;

public class DisplayController {
    private final MainFrame mainFrame;
    private final AssetManager assetManager;

    public DisplayController() {
        // 1. เตรียมทรัพยากร
        this.assetManager = new AssetManager();
        
        // 2. เตรียมหน้าต่างหลัก
        this.mainFrame = new MainFrame();
        
        // 3. เริ่มการแสดงหน้าแรก (เช่น เมนู)
        showMenu();
        
        // 4. แสดงหน้าต่างให้ผู้ใช้เห็น
        mainFrame.setVisible(true);
    }

    // ฟังก์ชันสำหรับสลับหน้าจอ (Scene Switching)
    public void showMenu() {
        MenuPanel menuPanel = new MenuPanel(assetManager, this);
        switchTo(menuPanel);
    }

    public void startOfflineGame() {
        OfflineGame gamePanel = new OfflineGame(assetManager, null, this);
        switchTo(gamePanel);
    }

    public void showOfflineLobbyPanel(){
        OfflineLobby lobbyPanel = new OfflineLobby(assetManager, this);
        switchTo(lobbyPanel);
    }

    public void quit(){
        System.exit(0);
    }

    // Helper method สำหรับล้างหน้าเก่าแล้วใส่หน้าใหม่
    private void switchTo(javax.swing.JPanel panel) {
        mainFrame.getContentPane().removeAll();
        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}