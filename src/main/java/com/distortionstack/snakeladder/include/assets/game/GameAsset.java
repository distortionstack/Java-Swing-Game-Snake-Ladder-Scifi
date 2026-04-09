package com.distortionstack.snakeladder.include.assets.game;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class GameAsset {
    private final Map<String, ImageIcon> map = new HashMap<>();

    public void put(String key, ImageIcon icon) { map.put(key, icon); }
    public ImageIcon get(String key)            { return map.get(key); }
    public boolean contains(String key)         { return map.containsKey(key); }

    // ── Background ──
    public ImageIcon getGameBackGround()    { return map.get("game.background"); }
    public ImageIcon getMenubackground()    { return map.get("menu.background"); }

    // ── Dice faces (1-6) ──
    public ImageIcon getDice(int face)      { return map.get("game.dice." + face); }

    // ── Player skins ──
    public ImageIcon getPlayerSkin(String color) { return map.get("game.player." + color); }

    // ── UFO animation ──
    public ImageIcon getUfoUp()             { return map.get("game.ufo.up"); }
    public ImageIcon getUfoDown()           { return map.get("game.ufo.down"); }

    // ── Arrows ──
    public ImageIcon getArrow_up()          { return map.get("game.arrow.up"); }
    public ImageIcon getArrow_down()        { return map.get("game.arrow.down"); }

    // ── Dice button ──
    public ImageIcon getDiceButtonUnBlock() { return map.get("game.dice_button.normal"); }
    public ImageIcon getDiceButtonBlcoked() { return map.get("game.dice_button.disabled"); }
}