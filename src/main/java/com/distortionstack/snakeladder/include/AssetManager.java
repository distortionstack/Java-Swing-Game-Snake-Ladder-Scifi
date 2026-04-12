package com.distortionstack.snakeladder.include;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.ImageIcon;
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
        loadManifest("game.xml", gameAsset::put);
        loadManifest("menu.xml", menuAsset::put);
    }

    private void loadManifest(String xmlFileName, AssetReceiver target) {
        try {
            InputStream is = null;

            // ท่าไม้ตาย: อ่านไฟล์ตรงๆ จากโฟลเดอร์ src เลย ไม่ง้อ IDE!
            File directFile = new File("src/main/resources/manifests/" + xmlFileName);
            if (directFile.exists()) {
                is = new FileInputStream(directFile);
                System.out.println("[AssetManager] ✅ load XML bypass IDE found: " + directFile.getAbsolutePath());
            } else {
                // เผื่อฟลุคอยู่ใน Classpath
                is = getClass().getClassLoader().getResourceAsStream("manifests/" + xmlFileName);
            }

            if (is == null) {
                System.err.println("[AssetManager] ❌ didn't find XML: " + xmlFileName);
                return;
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
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
            target.receive(fullKey, loadImage(el.getAttribute("file")));
        } else if (tagName.equals("group")) {
            NodeList children = el.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    parseElement((Element) children.item(i), ns, key + ".", target);
                }
            }
        }
    }

    private ImageIcon loadImage(String fileName) {
        // รายชื่อโฟลเดอร์ที่เราจะควานหา
        String[] subDirs = {"", "dice/", "arrow/", "animation/"};

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

    // ──────────────────────────────────────────────
    //  Static Helpers
    // ──────────────────────────────────────────────
    public static ImageIcon scaleImage(ImageIcon src, int w, int h) {
        if (src == null) return null;
        Image scaled = src.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public GameAsset getGameAsset() { return gameAsset; }
    public MenuAsset getMenuAsset() { return menuAsset; }

    @FunctionalInterface
    public interface AssetReceiver {
        void receive(String key, ImageIcon icon);
    }
}