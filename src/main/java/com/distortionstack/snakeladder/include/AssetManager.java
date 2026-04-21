package com.distortionstack.snakeladder.include;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.distortionstack.snakeladder.include.assets.game.GameAsset;
import com.distortionstack.snakeladder.include.assets.menu.MenuAsset;

public class AssetManager {

    private final GameAsset gameAsset = new GameAsset();
    private final MenuAsset menuAsset = new MenuAsset();

    public AssetManager() {
        loadManifest("game.xml", gameAsset);
        loadManifest("menu.xml", menuAsset);
    }

    private void loadManifest(String xmlFileName, AssetReceiver target) {
        try {
            InputStream stream = null;

            // ท่าไม้ตาย: อ่านไฟล์ตรงๆ จากโฟลเดอร์ src เลย ไม่ง้อ IDE!
            File directFile = new File("src/main/resources/manifests/" + xmlFileName);
            if (directFile.exists()) {
                stream = new FileInputStream(directFile);
                System.out.println("[AssetManager] ✅ load XML bypass IDE found: " + directFile.getAbsolutePath());
            } else {
                // เผื่อฟลุคอยู่ใน Classpath
                stream = getClass().getClassLoader().getResourceAsStream("manifests/" + xmlFileName);
            }

            if (stream == null) {
                System.err.println("[AssetManager] ❌ didn't find XML: " + xmlFileName);
                return;
            }

            try (InputStream is = stream) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();

            String namespace = doc.getDocumentElement().getAttribute("namespace");
            NodeList nList = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    parseElement((Element) node, namespace, "", target);
                }
            }
            }
        } catch (Exception e) {
            System.err.println("[AssetManager] ❌ Error loading manifest: " + xmlFileName);
            e.printStackTrace();
        }
    }

    private void parseElement(Element el, String ns, String prefix, AssetReceiver target) {
    String tagName = el.getTagName();
    String key = prefix + el.getAttribute("key");
    String fullKey = ns + "." + key;

    if (tagName.equals("image")) {
        target.receiveImage(fullKey, loadImage(el.getAttribute("file")));
    } 
    else if (tagName.equals("crop")) {
        ImageIcon sheet = loadImage(el.getAttribute("file"));
        if (sheet != null) {
            int x = Integer.parseInt(el.getAttribute("x"));
            int y = Integer.parseInt(el.getAttribute("y"));
            int w = Integer.parseInt(el.getAttribute("width"));
            int h = Integer.parseInt(el.getAttribute("height"));
            target.receiveImage(fullKey, ImageHelper.cropImage(sheet, x, y, w, h));
        }
    }else if (tagName.equals("scaled")) {
        ImageIcon img = loadImage(el.getAttribute("file"));
        if (img != null) {
            int w = Integer.parseInt(el.getAttribute("width"));
            int h = Integer.parseInt(el.getAttribute("height"));
            target.receiveImage(fullKey, ImageHelper.scaleImage(img, w, h));
            System.out.println("[AssetManager] ✅ Register Scaled Image: " + fullKey);
        }else {
            System.err.println("[AssetManager] ❌ couldn't load image for scaling: " + el.getAttribute("file"));
        }
    }
    else if (tagName.equals("sound")) {
        String fileName = el.getAttribute("file");
        URL soundUrl = getClass().getClassLoader().getResource("assets/sounds/" + fileName);
        if (soundUrl != null) {
            target.receiveSound(fullKey, soundUrl.toExternalForm());
            System.out.println("[AssetManager] ✅ Register Sound: " + fullKey);
            return;
        }

        File sFile = new File("src/main/resources/assets/sounds/" + fileName);
        String absolutePath = sFile.getAbsolutePath();
        target.receiveSound(fullKey, absolutePath);
        
        if (sFile.exists()) {
            System.out.println("[AssetManager] ✅ Register Sound: " + fullKey);
        } else {
            System.err.println("[AssetManager] ❌ File not found: " + fileName);
        }
    } // <--- ต้องปิดปีกกาตรงนี้ให้สนิท!
    else if (tagName.equals("group")) {
        NodeList children = el.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                // ส่ง key ปัจจุบันไปเป็น prefix เพื่อต่อจุด (.) ให้ลูกๆ
                parseElement((Element) children.item(i), ns, key + ".", target);
            }
        }
    }
}

    private ImageIcon loadImage(String fileName) {
        // รายชื่อโฟลเดอร์ที่เราจะควานหา
        String[] subDirs = { "", "dice/", "arrow/", "animation/" };

        // 1. ลองหาแบบไฟล์ตรงๆ (File I/O)
        for (String sub : subDirs) {
            File imgFile = new File("src/main/resources/assets/" + sub + fileName);
            if (imgFile.exists()) {
                return new ImageIcon(imgFile.getAbsolutePath());
            }
        }

        // 2. ถ้าแบบไฟล์ไม่เจอ ลองหาผ่าน Classpath
        for (String sub : subDirs) {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("assets/" + sub + fileName);
            if (imgUrl != null) {
                return new ImageIcon(imgUrl);
            }
        }

        System.err.println("[AssetManager] ❌ didn't load image: " + fileName);
        return null;
    }

    public GameAsset getGameAsset() {
        return gameAsset;
    }

    public MenuAsset getMenuAsset() {
        return menuAsset;
    }
}
