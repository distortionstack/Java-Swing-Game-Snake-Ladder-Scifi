package com.distortionstack.snakeladder.ui;

import com.distortionstack.snakeladder.include.assets.game.GameAsset;
import javax.swing.*;
import java.awt.*;

public class DiceRollAnimation {

    // timing
    private static final int SPIN_DELAY_MS = 80;
    private static final int SPIN_COUNT = 20;
    private static final int HOLD_MS = 2000;

    // display size
    private static final int DISPLAY_SIZE = 180; // ลูกเต๋าสี่เหลี่ยมจตุรัส

    private final GameAsset gameAsset;
    private final JPanel overlay; // panel ที่ครอบ DiceDisplay
    private final DiceDisplay diceDisplay;

    private Timer spinTimer;
    private int spinCounter;
    private int targetFace; // 1-6
    private Runnable onFinish;

    public DiceRollAnimation(GameAsset gameAsset, JPanel parentPanel, int panelW, int panelH) {
        this.gameAsset = gameAsset;

        // --- overlay ทึบกึ่งกลาง ---
        overlay = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        overlay.setOpaque(false);
        overlay.setBounds(0, 0, panelW, panelH);
        overlay.setVisible(false);

        // --- กล่องแสดงลูกเต๋า ---
        diceDisplay = new DiceDisplay();
        int cx = (panelW - DISPLAY_SIZE) / 2;
        int cy = (panelH - DISPLAY_SIZE) / 2;
        diceDisplay.setBounds(cx, cy, DISPLAY_SIZE, DISPLAY_SIZE);

        overlay.add(diceDisplay);
        parentPanel.add(overlay);
        parentPanel.setComponentZOrder(overlay, 0); // ขึ้นมาบนสุด
    }

    // ── เรียกตอนกดทอย ──
    public void play(int result, Runnable onFinish) {
        if (spinTimer != null && spinTimer.isRunning())
            spinTimer.stop();

        this.targetFace = result;
        this.onFinish = onFinish;
        this.spinCounter = 0;

        overlay.setVisible(true);
        showFace(1); // เริ่มต้นโชว์หน้า 1

        spinTimer = new Timer(SPIN_DELAY_MS, e -> tick());
        spinTimer.start();
    }

    private void tick() {
        spinCounter++;

        // slow down ช่วงท้าย
        if (spinCounter > SPIN_COUNT - 6) {
            ((Timer) spinTimer).setDelay(
                    SPIN_DELAY_MS + (spinCounter - (SPIN_COUNT - 6)) * 40);
        }

        if (spinCounter >= SPIN_COUNT) {
            spinTimer.stop();
            showFace(targetFace);
            Timer hold = new Timer(HOLD_MS, e -> {
                overlay.setVisible(false);
                if (onFinish != null)
                    onFinish.run();
            });
            hold.setRepeats(false);
            hold.start();
            return;
        }

        // สุ่มโชว์หน้า 1-6 ไปเรื่อยๆ
        int randomFace = (int) (Math.random() * 6) + 1;
        showFace(randomFace);
    }

    // ── ดึงไฟล์รูปแยก 1-6 มา Scale โดยตรง ไม่ต้อง Crop ──
    private void showFace(int face) {
        diceDisplay.setIcon(gameAsset.getDice(face));
    }

    // ── Custom component วาดกล่อง + เงา ──
    private static class DiceDisplay extends JLabel {
        private ImageIcon currentIcon;

        DiceDisplay() {
            setOpaque(false);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        void setIcon(ImageIcon icon) {
            this.currentIcon = icon;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int arc = 24;

            // เงา
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(6, 6, w - 6, h - 6, arc, arc);

            // พื้นหลังกล่อง
            g2.setColor(new Color(20, 20, 30, 220));
            g2.fillRoundRect(0, 0, w - 6, h - 6, arc, arc);

            // border เรืองแสง
            g2.setStroke(new BasicStroke(2.5f));
            g2.setColor(new Color(255, 120, 30, 200));
            g2.drawRoundRect(0, 0, w - 7, h - 7, arc, arc);

            // วาดภาพลูกเต๋า
            if (currentIcon != null) {
                int pad = 12;
                g2.drawImage(currentIcon.getImage(), pad, pad, w - pad * 2 - 6, h - pad * 2 - 6, this);
            }
            g2.dispose();
        }
    }
}