package com.distortionstack.snakeladder.ui;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ButtonAction {
    private ActionListener listener;
    private JButton button;

    public ButtonAction(String buttonName , ActionListener listener){
        this.listener = listener;
        button = new JButton();

        button.setText(buttonName);
        button.addActionListener(listener);
    }

    public ActionListener getListener() {
        return listener;
    }
    public JButton getButton() {
        return button;
    }
}
