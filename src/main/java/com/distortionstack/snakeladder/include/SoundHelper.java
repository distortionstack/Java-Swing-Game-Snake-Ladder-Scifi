package com.distortionstack.snakeladder.include;

public class SoundHelper {
    public static void playSound(String soundFilePath) {
        try {
            javax.sound.sampled.AudioInputStream audioInputStream = javax.sound.sampled.AudioSystem.getAudioInputStream(
                new java.io.File(soundFilePath)
            );
            javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("❌ can't play sound: " + e.getMessage());
        }
    }
}
