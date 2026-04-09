package com.distortionstack.snakeladder.ui;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.distortionstack.snakeladder.include.config.DisplayUI;

public class MainFrame extends JFrame {
    MenuPanel menuPanel;
    public MainFrame(){
        setSize(DisplayUI.WINDOW_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
    }

    public void showPanel(JPanel panel) {
        getContentPane().removeAll();
        add(panel);
        revalidate();
        repaint();
    }
}

