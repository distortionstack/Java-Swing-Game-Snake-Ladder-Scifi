package com.distortionstack.snakeladder.include.assets.menu;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

import com.distortionstack.snakeladder.include.AssetReceiver;

public class MenuAsset implements AssetReceiver {
    private final Map<String, ImageIcon> mapImage = new HashMap<>();
    private final Map<String, String> mapSound = new HashMap<>();
    public void put(String key, ImageIcon icon) { mapImage.put(key, icon); }
    public ImageIcon get(String key)            { return mapImage.get(key); }
    public boolean contains(String key)         { return mapImage.containsKey(key); }


    @Override
    public void receiveImage(String key, ImageIcon icon) {
        mapImage.put(key, icon);
    }
    @Override
    public void receiveSound(String key, String soundPath) {
        mapSound.put(key, soundPath); 
    }

    // เพิ่มเมธอดนี้เข้าไปครับ
    public ImageIcon getMenuBackground() {
        // "menu.background" คือชื่อที่เกิดจาก namespace + key ใน XML
        return mapImage.get("menu.background");
    }
}