package com.distortionstack.snakeladder.include.assets.game;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

import com.distortionstack.snakeladder.include.AssetReceiver;
import com.distortionstack.snakeladder.include.ImageHelper;
import com.distortionstack.snakeladder.include.SoundHelper;

public class GameAsset implements AssetReceiver {
    private final Map<String, ImageIcon> mapImage = new HashMap<>();
    private final Map<String, String> mapSound = new HashMap<>();
    public void put(String key, ImageIcon icon) { mapImage.put(key, icon); }
    public ImageIcon get(String key)            { return mapImage.get(key); }
    public boolean contains(String key)         { return mapImage.containsKey(key); }

    // ── Background ──
    public ImageIcon getGameBackGround()    { return mapImage.get("game.background"); }
    public ImageIcon getMenubackground()    { return mapImage.get("menu.background"); }

    // ── Dice faces (1-6) ──
    public ImageIcon getDice(int face)      { return ImageHelper.scaleImage(mapImage.get("game.dice." + face), 180, 180); }

    // ── Player skins ──
    public ImageIcon getPlayerSkin(String color) { return mapImage.get("game.player." + color); }

    // ── UFO animation ──
    public ImageIcon getUfoUp()             { return mapImage.get("game.ufo.up"); }
    public ImageIcon getUfoDown()           { return mapImage.get("game.ufo.down"); }

    // ── Arrows ──
    public ImageIcon getArrow_up()          { return mapImage.get("game.arrow.up"); }
    public ImageIcon getArrow_down()        { return mapImage.get("game.arrow.down"); }

    // ── Dice button ──
    public ImageIcon getDiceButtonUnBlock() { return mapImage.get("game.dice_button.normal"); }
    public ImageIcon getDiceButtonBlcoked() { return mapImage.get("game.dice_button.disabled"); }

    // ── Dice roll animation frames (1-6) ──
    public ImageIcon getDiceSheet(int face) { return mapImage.get("game.dice." + face); }

    public void playPlayerMovedSound() {
        SoundHelper.playSound(mapSound.get("game.player_move.move" + ((int)(Math.random() * 6) + 1)));
    }
    @Override
    public void receiveImage(String key, ImageIcon icon) {
        mapImage.put(key, icon);
    }
    @Override
    public void receiveSound(String key, String soundPath) {
        mapSound.put(key, soundPath); 
    }
}