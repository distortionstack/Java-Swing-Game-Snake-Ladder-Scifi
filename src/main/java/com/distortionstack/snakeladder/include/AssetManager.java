package com.distortionstack.snakeladder.include;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NodeList;

import com.distortionstack.snakeladder.include.asset.game.GameAsset;

public class AssetManager {
    static String slash = File.separator;
    private MenuAsset menuAsset;
    private GameAsset gameAsset;

    public AssetManager() {
        gameAsset = new GameAsset();
        menuAsset = new MenuAsset();
    }

    public static String getSlash() {
        return slash;
    }

    public GameAsset getGameAsset() {
        return gameAsset;
    }

    public MenuAsset getMenuAsset() {
        return menuAsset;
    }

    public static ImageIcon cropImage(ImageIcon sourceIcon, int x, int y, int width, int height) {
        if (sourceIcon == null)
            return null;

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
        }
    }

    // 1. วิธีดึง Path ของโฟลเดอร์ (เพื่อเอาไปแสดงผลหรือเช็คตำแหน่ง)
    public void printAssetPath() {
        URL resource = getClass().getClassLoader().getResource("resouces");
        if (resource != null) {
            try {
                // ใส่ try-catch ครอบไว้ตามที่ Java บังคับ
                String folderPath = Paths.get(resource.toURI()).toString();
                System.out.println("Full Path ของโฟลเดอร์: " + folderPath);
            } catch (URISyntaxException e) {
                System.out.println("Path มีปัญหา: " + e.getMessage());
            }
        } else {
            System.out.println("หาโฟลเดอร์ไม่เจอ!");
        }
    }

    // 2. วิธีโหลดไฟล์ในโฟลเดอร์นั้นมาใช้งาน (แนะนำวิธีนี้ที่สุด)
    public static ImageIcon getPathAndImageIconLoader(String fileName) {
        // อ้างอิง Path ต่อจาก Classpath ได้เลย
        String path = "resouces" + slash + fileName;
       URL fileURL = AssetManager.class.getClassLoader().getResource(path);
        if (fileURL != null) {
            return new ImageIcon(fileURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void parseGameAssets(InputStream xmlStream) {
    try {
        // 1. เตรียม Parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlStream);
        doc.getDocumentElement().normalize();

        // --- เริ่มอ่านข้อมูล ---

        // อ่าน Background
        String bg = doc.getElementsByTagName("background").item(0).getTextContent();
        generalAssets.put(AssetType.BACKGROUND, getPathAndImageIconLoader(bg));

        // อ่าน Players (เก็บเข้า Map<String, ImageIcon>)
        NodeList playerNodes = doc.getElementsByTagName("player");
        for (int i = 0; i < playerNodes.getLength(); i++) {
            Element el = (Element) playerNodes.item(i);
            String color = el.getAttribute("color"); // "blue", "red", ...
            String fileName = el.getTextContent();
            playerSkins.put(color, getPathAndImageIconLoader(fileName));
        }

        // อ่าน Dice List (เก็บเข้า List<ImageIcon>)
        NodeList diceNodes = doc.getElementsByTagName("dice");
        List<ImageIcon> diceImages = new ArrayList<>();
        for (int i = 0; i < diceNodes.getLength(); i++) {
            diceImages.add(getPathAndImageIconLoader(diceNodes.item(i).getTextContent()));
        }
        // คุณอาจจะต้องสร้าง Map อีกตัวเพื่อเก็บ List นี้ หรือเก็บลง Object ทั่วไป
        
        // อ่าน Dice Button (ตัวอย่างการอ่านแบบเจาะจง)
        Element diceBtnNormal = (Element) doc.getElementsByTagName("normal").item(0);
        if (diceBtnNormal != null) {
            ImageIcon fullImg = getPathAndImageIconLoader(diceBtnNormal.getTextContent());
            // ถ้า type="crop" ก็สั่ง Crop ต่อได้เลย
            if ("crop".equals(diceBtnNormal.getAttribute("type"))) {
                // สมมติใช้ logic เดิมของคุณ
                diceButtonUnBlock = AssetManager.cropImage(fullImg, 0, 0, 84, 87);
            }
        }

        System.out.println("XML Parsing Completed!");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}

class MenuAsset {

}
