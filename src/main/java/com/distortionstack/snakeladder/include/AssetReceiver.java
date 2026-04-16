package com.distortionstack.snakeladder.include;

import javax.swing.ImageIcon;

public interface AssetReceiver {
    void receiveImage(String key, ImageIcon icon);
    void receiveSound(String key, String soundPath);
}