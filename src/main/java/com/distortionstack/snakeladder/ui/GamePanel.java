package com.distortionstack.snakeladder.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.include.assets.game.GameAsset;
import com.distortionstack.snakeladder.include.config.DisplayUI;
import com.distortionstack.snakeladder.include.config.GameLogical;
import com.distortionstack.snakeladder.include.config.GameUI;

public class GamePanel extends JPanel {

    public final int GAP = 10;
    protected boolean debugmode = false;
    protected AssetManager assetManager;
    protected DiceAnimation diceRollAnimation; // เปลี่ยนชื่อคลาสให้ตรงกัน

    protected JButton diceButton;
    protected ImageIcon bgImageIcon;
    protected Point[] onePlayerPoint = new Point[GameLogical.INDEX_AMOUNT];
    protected JLabel[] upJLabels = new JLabel[GameLogical.LADDERS_UP.length];
    protected JLabel[] downJLabels = new JLabel[GameLogical.SNAKES_DOWN.length];
    protected JLabel diceAnimLabel;
    protected DiceLabel diceLabel;
    protected DisplayController displayController;

    protected JPanel overlay; // panel ที่ครอบ DiceDisplay

    public GamePanel(AssetManager assetManager, DisplayController displayController) {
        this.assetManager = assetManager;
        this.displayController = displayController; // เก็บค่าไว้ที่นี่
        bgImageIcon = assetManager.getGameAsset().getGameBackGround();

        diceButton = new JButton() {
            {
                setBounds(new Rectangle(GameUI.DICE_BUTTON_POINT, GameUI.DICE_BUTTON_DIMENSION));
                setIcon(assetManager.getGameAsset().getDiceButtonUnBlock());
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
            }
        };

        for (int i = 0; i < upJLabels.length; i++)
            upJLabels[i] = new JLabel(assetManager.getGameAsset().getArrow_up());

        for (int i = 0; i < downJLabels.length; i++)
            downJLabels[i] = new JLabel(assetManager.getGameAsset().getArrow_down());

        setLayout(null);
        setPreferredSize(DisplayUI.WINDOW_SIZE);
        positionSetUp();
        add(diceButton);

        // Dice animation overlay
        diceRollAnimation = new DiceAnimation(this);
        diceAnimLabel = new JLabel();
        diceAnimLabel.setBounds(0, 0, 390, DisplayUI.WINDOW_SIZE.height);
        diceAnimLabel.setHorizontalAlignment(JLabel.CENTER);
        diceAnimLabel.setVerticalAlignment(JLabel.CENTER);
        diceAnimLabel.setVisible(false);
        setOndiceRoll(
                assetManager.getGameAsset(),
                this,
                DisplayUI.WINDOW_SIZE.width,
                DisplayUI.WINDOW_SIZE.height);
        add(diceAnimLabel);
    }

    public void addDiceButtonListener(ActionListener listener) {
        diceButton.addActionListener(listener);
    }

    public void onDiceRoll() {
        overlay.setVisible(true);
        showFace(1); // เริ่มต้นโชว์หน้า 1
    }

    public void setOndiceRoll(GameAsset gameAsset, JPanel parentPanel, int panelW, int panelH) {
        // สร้าง Object DiceLabel ก่อน
        this.diceLabel = new DiceLabel();

        // กำหนดค่าให้ตัวแปร overlay ของคลาส (ห้ามใส่ JPanel นำหน้า)
        this.overlay = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        this.overlay.setOpaque(false);
        this.overlay.setBounds(0, 0, panelW, panelH);
        this.overlay.setVisible(false);

        int cx = (panelW - GameUI.DiCE_PANEL_SIZE) / 2;
        int cy = (panelH - GameUI.DiCE_PANEL_SIZE) / 2;
        this.diceLabel.setBounds(cx, cy, GameUI.DiCE_PANEL_SIZE, GameUI.DiCE_PANEL_SIZE);

        this.overlay.add(this.diceLabel);
        parentPanel.add(this.overlay);
        parentPanel.setComponentZOrder(this.overlay, 0);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void positionSetUp() {
        onePlayerPoint[0] = GameUI.START_POINT_ONE_PLAYER;
        onePlayerPoint[1] = new Point(420, 570);

        int cellWidth = GameUI.CELL_WIDTH_PX;
        int cellHeight = GameUI.CELL_HEIGHT_PX;
        int Tunner = GameUI.CELL_OFFSET_ADJUSTMENT_X;

        for (int i = 2; i < onePlayerPoint.length; i++) {
            int row = (i - 1) / 10;
            int col = (i - 1) % 10;
            if (row % 2 == 0) {
                onePlayerPoint[i] = new Point(
                        onePlayerPoint[1].x + col * cellWidth,
                        onePlayerPoint[1].y - row * cellHeight);
            } else {
                onePlayerPoint[i] = new Point(
                        onePlayerPoint[1].x + (9 - col) * cellWidth,
                        onePlayerPoint[1].y - row * cellHeight);
            }
        }

        // ── Ladder Up ──
        ImageIcon arrowUp = assetManager.getGameAsset().getArrow_up();
        for (int i = 0; i < upJLabels.length; i++) {
            int tileIndex = GameLogical.LADDERS_UP[i][0];
            if (tileIndex < onePlayerPoint.length && onePlayerPoint[tileIndex] != null) {
                Point p = onePlayerPoint[tileIndex];

                if (upJLabels[i] == null) {
                    upJLabels[i] = new JLabel();
                }

                if (arrowUp != null) {
                    // ใส่รูปที่ย่อมาแล้วลงไปตรงๆ ได้เลย
                    upJLabels[i].setIcon(arrowUp);

                    // ดึงขนาดความกว้าง/สูง จากตัวไฟล์รูปจริงๆ
                    int imgW = arrowUp.getIconWidth();
                    int imgH = arrowUp.getIconHeight();

                    int cellW = GameUI.LADDER_AND_SNAKES_CELL_SIZE.width;
                    int cellH = GameUI.LADDER_AND_SNAKES_CELL_SIZE.height;

                    // คำนวณตำแหน่งให้ภาพอยู่กึ่งกลางช่องพอดี
                    int offsetX = (cellW - imgW) / 2;
                    int offsetY = (cellH - imgH) / 2;
                    int baseX = p.x - Tunner;
                    int baseY = p.y - Tunner - (tileIndex / 12 - 1);

                    upJLabels[i].setBounds(baseX + offsetX, baseY + offsetY, imgW, imgH);
                }

                add(upJLabels[i]);
            }
        }

        // ── Snake Down ──
        ImageIcon arrowDown = assetManager.getGameAsset().getArrow_down();
        for (int i = 0; i < downJLabels.length; i++) {
            int tileIndex = GameLogical.SNAKES_DOWN[i][0];
            if (tileIndex < onePlayerPoint.length && onePlayerPoint[tileIndex] != null) {
                Point p = onePlayerPoint[tileIndex];

                if (downJLabels[i] == null) {
                    downJLabels[i] = new JLabel();
                }

                if (arrowDown != null) {
                    // ใส่รูปที่ย่อมาแล้วลงไปตรงๆ ได้เลย
                    downJLabels[i].setIcon(arrowDown);

                    // ดึงขนาดความกว้าง/สูง จากตัวไฟล์รูปจริงๆ
                    int imgW = arrowDown.getIconWidth();
                    int imgH = arrowDown.getIconHeight();

                    int cellW = GameUI.LADDER_AND_SNAKES_CELL_SIZE.width;
                    int cellH = GameUI.LADDER_AND_SNAKES_CELL_SIZE.height;

                    // คำนวณตำแหน่งให้ภาพอยู่กึ่งกลางช่องพอดี
                    int offsetX = (cellW - imgW) / 2;
                    int offsetY = (cellH - imgH) / 2;
                    int baseX = p.x - Tunner;
                    int baseY = p.y - Tunner - (tileIndex / 12 - 1);

                    downJLabels[i].setBounds(baseX + offsetX, baseY + offsetY, imgW, imgH);
                }

                add(downJLabels[i]);
            }
        }

        System.out.println("=== position check ===");
        for (int i = 1; i <= 10; i++) {
            System.out.println("ช่อง " + i + ": " + onePlayerPoint[i]);
            System.out.println("ช่อง 100: " + onePlayerPoint[100]);

            repaint();
            revalidate();
        }
    }

    // ── ดึงไฟล์รูปแยก 1-6 มา Scale โดยตรง ไม่ต้อง Crop ──
    public void showFace(int face) {
        diceLabel.setIcon(assetManager.getGameAsset().getDice(face));
    }

    public void drawPlayer(Graphics g, ArrayList<PlayerData> playerList) {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. วาด Background (ดึง Image ออกมาวาดเลย ไม่ต้องสั่ง getWidth/getHeight
        // ใหม่ทุกครั้งถ้าไม่จำเป็น)
        if (bgImageIcon != null) {
            g.drawImage(bgImageIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
        }

        // 2. เรียกเมธอดวาดตัวละคร (ต้องส่ง List ของ Player เข้ามาด้วย)
        // สมมติว่าคุณเก็บ playerList ไว้ใน DisplayController หรือคลาสที่ควบคุม Logic
        // drawPlayer(g, displayController.getPlayers());

        if (!debugmode)
            return;

        // ส่วนของ Debug Mode
        g.setColor(Color.RED);
        for (int i = 0; i < onePlayerPoint.length; i++) {
            if (onePlayerPoint[i] == null)
                continue;
            g.fillOval(onePlayerPoint[i].x, onePlayerPoint[i].y, 5, 5);
            g.drawString(String.valueOf(i), onePlayerPoint[i].x, onePlayerPoint[i].y - 5);
        }
    }

    /** เรียกก่อนเริ่มเดิน — animation จบแล้วค่อย callback */
    public void playDiceAnimation(int result, Runnable onFinish) {
        diceRollAnimation.play(result, () -> {
            if (onFinish != null)
                onFinish.run();
        });
    }

    public void blockDiceButton() {
        diceButton.setEnabled(false);
        diceButton.setIcon(assetManager.getGameAsset().getDiceButtonBlcoked());
    }

    public void unblockDiceButton() {
        diceButton.setEnabled(true);
        diceButton.setIcon(assetManager.getGameAsset().getDiceButtonUnBlock());
    }

    public JButton getDiceButton() {
        return diceButton;
    }

    public JFrame getAnimateUFO(int newPos, int oldPos) {
        ImageIcon icon = newPos < oldPos
                ? assetManager.getGameAsset().getUfoDown()
                : assetManager.getGameAsset().getUfoUp();

        return new JFrame() {
            {
                add(new JLabel(icon));
                setSize(icon.getIconWidth(), icon.getIconHeight());
                setLocationRelativeTo(null);
                setUndecorated(true);
                setAlwaysOnTop(true);
            }
        };
    }

    public JPanel getOverlay() {
        return overlay;
    }

    public DisplayController getDisplayController() {
        return displayController;
    }

    public void drawPlayer(Graphics g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'drawPlayer'");
    }
}

// ── Custom component วาดกล่อง + เงา ──
class DiceLabel extends JLabel {
    private ImageIcon currentIcon;

    DiceLabel() {
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