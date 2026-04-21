package com.distortionstack.snakeladder.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;

import com.distortionstack.snakeladder.include.AssetManager;

public class MenuPanel extends JPanel {
    ButtonAction[] menuButton;
    JPanel menuBar;

    JLabel titleLabel;
    JPanel contentPanel;
    AssetManager assetManager;
    DisplayController displayController;

    public MenuPanel(AssetManager assetManager, DisplayController displayController) {
        this.assetManager = assetManager;
        menuBar = new JPanel();
        menuBar.setOpaque(false);
        this.displayController = displayController;

        menuButton = new ButtonAction[] {
                new ButtonAction("Offline Mode", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        displayController.startOfflineMode();
                    }
                }),
                new ButtonAction("Quit", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        displayController.quit();
                    }
                })
        };

        for (int i = 0; i < menuButton.length; i++) {
            menuBar.add(menuButton[i].getButton());
        }

        menuBar.setLayout(new GridLayout(menuButton.length, 1, 0, 10));
        titleLabel = new JLabel("My Snake Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setOpaque(false); //

        contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false); // <--- เพิ่มบรรทัดนี้ลงไป
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(menuBar, BorderLayout.CENTER);

        setLayout(new GridLayout(1, 3));
        setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0)); 

        add(new JPanel() {
            {
                setOpaque(false);
            }
        });
        add(contentPanel);
        add(new JPanel() {
            {
                setOpaque(false);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(assetManager.getMenuAsset().getMenuBackground().getImage(), 
                    0, 0, getWidth(), getHeight(), this);
    }

    public ButtonAction[] getMenuButton() {
        return menuButton;
    }
}

class SettingPanel extends JPanel {

}