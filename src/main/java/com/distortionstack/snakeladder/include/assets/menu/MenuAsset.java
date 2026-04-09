package com.distortionstack.snakeladder.include.assets.menu;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class MenuAsset {
    private final Map<String, ImageIcon> map = new HashMap<>();

    public void put(String key, ImageIcon icon) { map.put(key, icon); }
    public ImageIcon get(String key)            { return map.get(key); }
    public boolean contains(String key)         { return map.containsKey(key); }

    // เพิ่มเมธอดนี้เข้าไปครับ
    public ImageIcon getMenuBackground() {
        // "menu.background" คือชื่อที่เกิดจาก namespace + key ใน XML
        return map.get("menu.background");
    }
}