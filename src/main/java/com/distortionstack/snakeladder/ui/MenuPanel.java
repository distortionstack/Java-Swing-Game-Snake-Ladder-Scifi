package com.distortionstack.snakeladder.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

import javax.swing.BorderFactory;

import com.distortionstack.snakeladder.include.AssetManager;

public class MenuPanel extends JPanel {
    ButtonAction[] menuButton;
    JPanel menuBar;

    JLabel titleLabel;
    JPanel contentPanel;
    AssetManager assetManager;
    DisplayController displayController;

    public MenuPanel(AssetManager assetManager,DisplayController displayController) {
        this.assetManager = assetManager;
        menuBar = new JPanel();
        this.displayController = displayController;
            
        menuButton = new ButtonAction[] {
            new ButtonAction("Offline Mode", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayController.startOfflineGame();
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
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contentPanel = new JPanel(new BorderLayout(0, 20));
            contentPanel.add(titleLabel, BorderLayout.NORTH);
            contentPanel.add(menuBar, BorderLayout.CENTER);  
        

        setLayout(new GridLayout(1, 3)); 
        setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0));

        add(new JPanel());   
        add(contentPanel);   
        add(new JPanel());     
    }

    public ButtonAction[] getMenuButton() {
        return menuButton;
    }
}

class SettingPanel extends JPanel {

}