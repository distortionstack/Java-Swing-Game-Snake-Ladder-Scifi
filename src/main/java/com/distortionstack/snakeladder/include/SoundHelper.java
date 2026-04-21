package com.distortionstack.snakeladder.include;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class SoundHelper {
    public static void playSound(String soundFilePath) {
        if (soundFilePath == null || soundFilePath.isEmpty()) return;

        try {
            AudioInputStream audioInputStream;
            if (isUrlPath(soundFilePath)) {
                audioInputStream = AudioSystem.getAudioInputStream(new URL(soundFilePath));
            } else {
                File soundFile = new File(soundFilePath);
                if (!soundFile.exists()) {
                    System.err.println("❌ ไม่พบไฟล์เสียงที่: " + soundFilePath);
                    return;
                }
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            }
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            
            // เล่นเสร็จแล้วคืนทรัพยากรด้วย (สำคัญมาก ไม่งั้นรันไปนานๆ เสียงจะใบ้)
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (UnsupportedAudioFileException e) {
            System.err.println("❌ ไฟล์เสียงไม่รองรับ (ต้องเป็น .wav เท่านั้น): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ เกิดข้อผิดพลาดในการเล่นเสียง: " + e.getMessage());
        }
    }

    private static boolean isUrlPath(String path) {
        return path.startsWith("file:")
                || path.startsWith("jar:")
                || path.startsWith("http:")
                || path.startsWith("https:");
    }
}
