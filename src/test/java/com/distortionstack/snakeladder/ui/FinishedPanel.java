package com.distortionstack.snakeladder.ui;

import java.awt.*;
import javax.swing.*;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.include.config.DisplayUI;

public class FinishedPanel extends JPanel {
    private final DisplayController displayController;
    private final AssetManager assetManager;

    public FinishedPanel(AssetManager assetManager, DisplayController displayController, String winnerName) {
        this.displayController = displayController;
        this.assetManager = assetManager;

        setLayout(new GridBagLayout());
        setPreferredSize(DisplayUI.WINDOW_SIZE);
        setBackground(new Color(0, 0, 0, 180)); // พื้นหลังมืดโปร่งแสง

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // ข้อความ "Game Over" หรือ "Winner"
        JLabel titleLabel = new JLabel("🎉 GAME FINISHED 🎉");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // แสดงชื่อผู้ชนะ
        JLabel winnerLabel = new JLabel("Winner: " + winnerName);
        winnerLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        winnerLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(winnerLabel, gbc);

        // ปุ่มกลับหน้าหลัก
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> displayController.showMenu());
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(backButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // วาดพื้นหลังเดิมของเกมแบบจางๆ (ถ้าต้องการ)
        if (assetManager.getGameAsset().getGameBackGround() != null) {
            g.drawImage(assetManager.getGameAsset().getGameBackGround().getImage(), 
                        0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 150)); // ทับด้วยสีดำจางๆ
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}