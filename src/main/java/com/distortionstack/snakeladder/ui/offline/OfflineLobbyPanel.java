package com.distortionstack.snakeladder.ui.offline;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.DisplayController;
import com.distortionstack.snakeladder.ui.LobbyPanel;

public class OfflineLobbyPanel extends LobbyPanel {
    JButton startButton;

    public OfflineLobbyPanel(AssetManager assetManager, DisplayController displayController) {
        super(assetManager, displayController);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(90, 80, 50, 80));
        add(LobbyLabel, BorderLayout.NORTH);
        add(selectSkinPanel, BorderLayout.CENTER);

        operationPanel = new JPanel() {
            {
                setOpaque(false);
                setLayout(new FlowLayout());
                startButton = new JButton("start");
                startButton.setPreferredSize(new Dimension(200, 50));
                add(startButton);
            }
        };
        add(operationPanel, BorderLayout.SOUTH);
    }

    public void handleSkinSelect(JButton button) {
            button.setEnabled(false);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public void showEmptyPlayerAlert() {
        JOptionPane.showMessageDialog( null, "You must choose at least one character","Warning" , JOptionPane.WARNING_MESSAGE);
    }
}
