package com.distortionstack.snakeladder.ui.offline;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.DisplayController;
import com.distortionstack.snakeladder.ui.LobbyPanel;

public class OfflineLobby extends LobbyPanel {
    JButton startButton;

    public OfflineLobby(AssetManager assetManager,DisplayController displayController) {
        super(assetManager,displayController);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(90, 80, 50, 80));
        add(LobbyLabel, BorderLayout.NORTH);
        add(selectSkinPanel, BorderLayout.CENTER);

        operationPanel = new JPanel() {
            {
                setOpaque(false);
                setLayout(new FlowLayout());
                startButton = new JButton("start") {
                    {
                        setSize(200, 50);
                    }
                };
                add(startButton);
            }
        };
        add(operationPanel, BorderLayout.SOUTH);
    }

    public JButton getStartButton() {
        return startButton;
    }
}