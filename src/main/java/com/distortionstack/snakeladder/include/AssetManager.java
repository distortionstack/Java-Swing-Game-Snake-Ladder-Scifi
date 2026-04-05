package com.distortionstack.snakeladder.include;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
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
        //loadManifest("menu.xml", menuAsset::put);
    }

    // ──────────────────────────────────────────────
    //  โหลด XML
    // ──────────────────────────────────────────────

    private void loadManifest(String xmlFileName, AssetReceiver target) {
        try {
            URL url = getClass().getResource("manifests/" + xmlFileName);
            if (url == null) {
                System.err.println("[AssetManager] manifest not found: " + xmlFileName);
                return;
            }
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url.toString());
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            String namespace = root.getAttribute("namespace");

            parseElement(root, namespace, target);
            System.out.println("[AssetManager] " + xmlFileName + " done.");
        } catch (Exception e) {
            System.err.println("[AssetManager] Failed: " + xmlFileName + " → " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────
    //  Recursive parser
    // ──────────────────────────────────────────────

    private void parseElement(Element element, String currentPath, AssetReceiver target) {
        NodeList children = element.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            Element child  = (Element) node;
            String tag     = child.getTagName();
            String key     = child.getAttribute("key");
            String newPath = currentPath.isEmpty() ? key : currentPath + "." + key;

            switch (tag) {
                case "group":
                    parseElement(child, newPath, target);
                    break;

                case "image": {
                    ImageIcon icon = loadImageIcon(child.getAttribute("file"));
                    // scale ถ้ามี attribute
                    icon = applyScale(icon, child);
                    target.put(newPath, icon);
                    System.out.println("  Loaded : " + newPath);
                    break;
                }

                case "crop": {
                    ImageIcon full = loadImageIcon(child.getAttribute("file"));
                    int x = Integer.parseInt(child.getAttribute("x"));
                    int y = Integer.parseInt(child.getAttribute("y"));
                    int w = Integer.parseInt(child.getAttribute("width"));
                    int h = Integer.parseInt(child.getAttribute("height"));
                    ImageIcon icon = cropImage(full, x, y, w, h);
                    // scale หลัง crop ถ้ามี
                    icon = applyScale(icon, child);
                    target.put(newPath, icon);
                    System.out.println("  Cropped: " + newPath);
                    break;
                }
            }
        }
    }

    // ──────────────────────────────────────────────
    //  Helpers
    // ──────────────────────────────────────────────

    private ImageIcon loadImageIcon(String fileName) {
        InputStream is = getClass().getResourceAsStream("resouces/" + fileName);
        if (is == null) {
            System.err.println("  Missing: resouces/" + fileName);
            return null;
        }
        try {
            return new ImageIcon(ImageIO.read(is));
        } catch (IOException e) {
            System.err.println("  Failed to read: " + fileName);
            return null;
        }
    }

    // อ่าน scale_w / scale_h จาก element ถ้าไม่มีคืน icon เดิม
    private ImageIcon applyScale(ImageIcon icon, Element el) {
        String sw = el.getAttribute("scale_w");
        String sh = el.getAttribute("scale_h");
        if (sw.isEmpty() || sh.isEmpty()) return icon;
        return scaleImage(icon, Integer.parseInt(sw), Integer.parseInt(sh));
    }

    public static ImageIcon scaleImage(ImageIcon src, int w, int h) {
        if (src == null) return null;
        Image scaled = src.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static ImageIcon cropImage(ImageIcon src, int x, int y, int w, int h) {
        if (src == null) return null;
        Image img = src.getImage();
        BufferedImage buf = new BufferedImage(
            src.getIconWidth(), src.getIconHeight(), BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = buf.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        int finalW = Math.min(w, buf.getWidth()  - x);
        int finalH = Math.min(h, buf.getHeight() - y);
        return new ImageIcon(buf.getSubimage(x, y, finalW, finalH));
    }

    // ──────────────────────────────────────────────
    //  Getters
    // ──────────────────────────────────────────────

    public GameAsset getGameAsset() { return gameAsset; }
    public MenuAsset getMenuAsset() { return menuAsset; }

    @FunctionalInterface
    private interface AssetReceiver {
        void put(String key, ImageIcon icon);
    }
}