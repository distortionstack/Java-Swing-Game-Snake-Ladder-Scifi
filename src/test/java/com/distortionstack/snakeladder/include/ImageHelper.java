package com.distortionstack.snakeladder.include;

import javax.swing.ImageIcon;
import java.awt.Image;

public class ImageHelper {
    // ──────────────────────────────────────────────
    //  Static Helpers
    // ──────────────────────────────────────────────
    public static ImageIcon scaleImage(ImageIcon src, int w, int h) {
        if (src == null) return null;
        Image scaled = src.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // อย่าลืมเพิ่มเมธอด cropImage ไว้ข้างล่างด้วย (ถ้ายังไม่มี)
    public static ImageIcon cropImage(ImageIcon src, int x, int y, int w, int h) {
        if (src == null) return null;
        try {
            java.awt.image.BufferedImage buf = new java.awt.image.BufferedImage(
                src.getIconWidth(), src.getIconHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB
            );
            java.awt.Graphics g = buf.getGraphics();
            g.drawImage(src.getImage(), 0, 0, null);
            g.dispose();
            return new ImageIcon(buf.getSubimage(x, y, w, h));
        } catch (Exception e) {
            System.err.println("❌ Crop รูปพลาด: " + e.getMessage());
            return null;
        }
    }
}