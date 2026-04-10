package com.distortionstack.snakeladder.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.distortionstack.snakeladder.domain.PlayerData;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.include.config.DisplayUI;
import com.distortionstack.snakeladder.include.config.GameLogical;
import com.distortionstack.snakeladder.include.config.GameUI;

public class GamePanel extends JPanel {

    public final int GAP = 10;
    protected boolean debugmode = false;
    protected AssetManager assetManager;

    protected JButton diceButton;
    protected ImageIcon bgImageIcon;
    protected Point[] onePlayerPoint = new Point[GameLogical.INDEX_AMOUNT];
    protected JLabel[] upJLabels = new JLabel[GameLogical.LADDERS_UP.length];
    protected JLabel[] downJLabels = new JLabel[GameLogical.SNAKES_DOWN.length];
    protected JLabel diceAnimLabel;
    protected DiceRollAnimation diceRollAnimation;

    public GamePanel(AssetManager assetManager, DisplayController displayController) {
        this.assetManager = assetManager;
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
        diceAnimLabel = new JLabel();
        diceAnimLabel.setBounds(0, 0, 390, DisplayUI.WINDOW_SIZE.height);
        diceAnimLabel.setHorizontalAlignment(JLabel.CENTER);
        diceAnimLabel.setVerticalAlignment(JLabel.CENTER);
        diceAnimLabel.setVisible(false);
        diceRollAnimation = new DiceRollAnimation(
    assetManager.getGameAsset(),
    this,
    DisplayUI.WINDOW_SIZE.width,
    DisplayUI.WINDOW_SIZE.height
);
        add(diceAnimLabel);
    }

    public void addDiceButtonListener(ActionListener listener) {
        diceButton.addActionListener(listener);
    }

    public void positionSetUp() {
        onePlayerPoint[0] = GameUI.START_POINT_ONE_PLAYER;
        onePlayerPoint[1] = new Point(420, 570);

        int cellWidth = 56;
        int cellHeight = 58;
        int Tunner = 14;

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

    public void drawPlayer(Graphics g, ArrayList<PlayerData> playerList) {
        if (playerList == null || playerList.isEmpty()) {
            System.out.println("Player list is null or empty");
            return;
        }
        for (PlayerData playerData : playerList) {
            int index = playerData.getgStatus().getVisibleIndex();
            if (index < 0 || index >= onePlayerPoint.length) {
                System.out.println("Invalid index: " + index);
                continue;
            }
            ImageIcon skin = assetManager.getGameAsset().getPlayerSkin(playerData.getSkincode());
            if (skin == null || skin.getImage() == null) {
                System.out.println("Skin not found for: " + playerData.getSkincode());
                continue;
            }
            Point position = onePlayerPoint[index];
            g.drawImage(skin.getImage(), position.x, position.y, 13, 19, this);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImageIcon != null)
            g.drawImage(bgImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);

        if (!debugmode)
            return;

        g.setColor(Color.RED);
        for (int i = 0; i < onePlayerPoint.length; i++) {
            if (onePlayerPoint[i] == null)
                continue;
            if (i == 1)
                g.drawImage(assetManager.getGameAsset().getArrow_down().getImage(),
                        onePlayerPoint[i].x, onePlayerPoint[i].y, 20, 20, this);
            g.fillOval(onePlayerPoint[i].x, onePlayerPoint[i].y, 5, 5);
            g.drawString(String.valueOf(i), onePlayerPoint[i].x, onePlayerPoint[i].y - 5);
        }
    }

    /** เรียกก่อนเริ่มเดิน — animation จบแล้วค่อย callback */
    public void playDiceAnimation(int result, Runnable onFinish) {
    diceButton.setEnabled(false);
    diceRollAnimation.play(result, () -> {
        diceButton.setEnabled(true);
        if (onFinish != null) onFinish.run();
    });
}

    public void BlockDiceButton() {
        diceButton.setEnabled(false);
        diceButton.setIcon(assetManager.getGameAsset().getDiceButtonBlcoked());
    }

    public void UnBlockDiceButton() {
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
}