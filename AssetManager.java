import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class AssetManager {
    static String slash = File.separator;
    static String UserDir = System.getProperty("user.dir");

    MenuAsset menuAsset;
    GameAsset gameAsset;

    AssetManager(){
        gameAsset = new GameAsset();
        menuAsset = new MenuAsset();
    }

    public static String getSlash() {
        return slash;
    }
    public static String getUserDir() {
        return UserDir;
    }

    public GameAsset getGameAsset() {
        return gameAsset;
    }
    public MenuAsset getMenuAsset() {
        return menuAsset;
    }

    public static ImageIcon cropImage(ImageIcon sourceIcon, int x, int y, int width, int height) {
        if (sourceIcon == null) return null;

        // 1. ดึง Image ออกมาจาก ImageIcon
        Image img = sourceIcon.getImage();

        // 2. สร้าง BufferedImage (กระดาษวาดรูปเปล่าๆ) ขนาดเท่ารูปเดิม
        BufferedImage bufferedImage = new BufferedImage(
            img.getWidth(null), 
            img.getHeight(null),
            BufferedImage.TYPE_INT_ARGB // ใช้ ARGB เพื่อรองรับพื้นหลังใส (Transparency)
        );

        // 3. วาดรูปเดิมลงไปใน BufferedImage
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose(); // วาดเสร็จแล้วปล่อยแปรง

        // 4. สั่งตัด (Crop) เฉพาะส่วนที่ต้องการ
        // ระวัง! อย่าใส่ x, y, width, height เกินขอบรูป ไม่งั้น error
        BufferedImage croppedImage = bufferedImage.getSubimage(x, y, width, height);

        // 5. ห่อกลับเป็น ImageIcon แล้วส่งคืน
        return new ImageIcon(croppedImage);
    }
    public ImageIcon scaleImage(ImageIcon sourceIcon, int width, int height) {
        try {
            Image sourceImage = sourceIcon.getImage();
            Image scaledImage = sourceImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            ImageIcon scaledGifIcon = new ImageIcon(scaledImage);
            return scaledGifIcon;
        } catch (Exception e) {
            System.out.println("explo pic eror");
            return null;
            // TODO: handle exception
        }
    }
}

class MenuAsset{

}

class GameAsset {
    //
    private static String slash = AssetManager.getSlash();
    private static String UserDir = AssetManager.getUserDir();
    //String Path
    int skinamount;
    //Asset File Object
    ImageIcon arrow_up;
    ImageIcon arrow_down;
    ImageIcon [] playerSkin;
    ImageIcon Gamebackground;
    ImageIcon Menubackground;
    ImageIcon BasediceButtonIcon;
    ImageIcon diceButtonUnBlock;
    ImageIcon diceButtonBlcoked;
    ImageIcon ufoDown;
    ImageIcon ufoUp;
    GameAsset(){
        skinamount = GameLogical.SKINCODE_ARRAY.length;
        playerSkin = new ImageIcon[skinamount];
        
        Menubackground = ImageIconLoader("bg_image.png");
        Gamebackground = ImageIconLoader("newmap.png");

        arrow_up = ImageIconLoader( "up.gif");
        arrow_down = ImageIconLoader( "down.gif");

        ufoUp = ImageIconLoader("UFO_UP(resize).gif");
        ufoDown = ImageIconLoader("UFO_DOWN(resize).gif");

        if(arrow_down != null){
            System.out.println("por tai" + arrow_down.getIconWidth());
        }

        BasediceButtonIcon = ImageIconLoader("but_dice.png");

        for (int i = 0; i < skinamount; i++) {
            playerSkin[i] = ImageIconLoader("player_" + GameLogical.SKINCODE_ARRAY[i] + ".png"); 
        }


        diceButtonUnBlock = AssetManager.cropImage(BasediceButtonIcon, 0, 0, 84, 87);
        diceButtonBlcoked = AssetManager.cropImage(BasediceButtonIcon, 84, 0, 84, 87);
    }
    private ImageIcon ImageIconLoader(String FileName){
        try {
            String pathLoad = UserDir + slash +"assets" + slash + FileName;
            System.out.println(pathLoad);
            File file = new File(pathLoad);
            if (!file.exists()) {
                System.out.println("File not found: " + pathLoad);
                return null;
            }
            ImageIcon icon = new ImageIcon(pathLoad);     
            return icon;
        } catch (Exception e) {
            System.out.println( FileName + " : " + e.getMessage());
            return null;
        }
    }
    public ImageIcon getArrow_down() {
        return arrow_down;
    }
    public ImageIcon getArrow_up() {
        return arrow_up;
    }
    public ImageIcon getPlayerSkin(String skin) {
        if(Arrays.asList(GameLogical.SKINCODE_ARRAY).indexOf(skin) == -1){
            System.out.println("Can't Find The Skin");
        }
        return playerSkin[Arrays.asList(GameLogical.SKINCODE_ARRAY).indexOf(skin)];
    }
    public ImageIcon getGameBackGround() {
        return Gamebackground;
    }
    public ImageIcon getMenubackground() {
        return Menubackground;
    }
    public ImageIcon getDiceButtonBlcoked() {
        return diceButtonBlcoked;
    }
    public ImageIcon getDiceButtonUnBlock() {
        return diceButtonUnBlock;
    }
    
}
