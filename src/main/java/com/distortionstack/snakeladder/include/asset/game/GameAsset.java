package com.distortionstack.snakeladder.include.asset.game;

import java.io.File;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.include.config.GameLogical;


public class GameAsset {
    // เก็บ Asset ทั่วไป
    private Map<GameAssetKey, ImageIcon> generalAssets = new EnumMap<>(GameAssetKey.class);
    // เก็บสกินผู้เล่น โดยใช้สีเป็น Key เช่น "blue", "red"
    private Map<String, ImageIcon> playerSkins = new HashMap<>();

    public GameAsset() {
        // โหลด Asset ทั่วไป
        generalAssets.put(GameAssetKey.BG_MENU, AssetManager.getPathAndImageIconLoader("bg_image.png"));
        generalAssets.put(GameAssetKey.BG_GAME, AssetManager.getPathAndImageIconLoader("newmap.png"));
        generalAssets.put(GameAssetKey.ARROW_UP, AssetManager.getPathAndImageIconLoader("up.gif"));
        generalAssets.put(GameAssetKey.ARROW_DOWN, AssetManager.getPathAndImageIconLoader("down.gif"));
        generalAssets.put(GameAssetKey.UFO_UP, AssetManager.getPathAndImageIconLoader("UFO_UP(resize).gif"));
        generalAssets.put(GameAssetKey.UFO_DOWN, AssetManager.getPathAndImageIconLoader("UFO_DOWN(resize).gif"));

        // โหลดปุ่มเต๋าและ Crop
        ImageIcon baseDice = AssetManager.getPathAndImageIconLoader("but_dice.png");
        generalAssets.put(GameAssetKey.DICE_BTN_NORMAL, AssetManager.cropImage(baseDice, 0, 0, 84, 87));
        generalAssets.put(GameAssetKey.DICE_BTN_DISABLED, AssetManager.cropImage(baseDice, 84, 0, 84, 87));

        // โหลดสกินผู้เล่น
        for (String color : GameLogical.SKINCODE_ARRAY) {
            playerSkins.put(color, AssetManager.getPathAndImageIconLoader("player_" + color + ".png"));
        }
    }

    // วิธีดึงใช้แค่ 2 Method พอ
    public ImageIcon get(GameAssetKey key) {
        return generalAssets.get(key);
    }

    public ImageIcon getPlayerSkin(String color) {
        return playerSkins.getOrDefault(color, null);
    }
}
    

