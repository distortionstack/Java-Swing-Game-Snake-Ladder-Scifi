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
    protected JLabel[] upJLabels   = new JLabel[GameLogical.LADDERS_UP.length];
    protected JLabel[] downJLabels = new JLabel[GameLogical.SNAKES_DOWN.length];

    public GamePanel(AssetManager assetManager,DisplayController displayController) {
        this.assetManager = assetManager;
        bgImageIcon = assetManager.getGameAsset().getGameBackGround();

        diceButton = new JButton() {{
            setBounds(new Rectangle(GameUI.DICE_BUTTON_POINT, GameUI.DICE_BUTTON_DIMENSION));
            setIcon(assetManager.getGameAsset().getDiceButtonUnBlock());
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }};

        for (int i = 0; i < upJLabels.length; i++)
            upJLabels[i] = new JLabel(assetManager.getGameAsset().getArrow_up());

        for (int i = 0; i < downJLabels.length; i++)
            downJLabels[i] = new JLabel(assetManager.getGameAsset().getArrow_down());

        setLayout(null);
        setPreferredSize(DisplayUI.WINDOW_SIZE);
        PositionSetUP();
        add(diceButton);
    }

    public void addDiceButtonListener(ActionListener listener) {
        diceButton.addActionListener(listener);
    }

    public void PositionSetUP() {
        onePlayerPoint[0] = GameUI.START_POINT_ONE_PLAYER;
        onePlayerPoint[1] = new Point(420, 570);

        int cellWidth  = 56;
        int cellHeight = 58;
        int Tunner     = 14;

        for (int i = 2; i < onePlayerPoint.length; i++) {
            int row = (i - 1) / 10;
            int col = (i - 1) % 10;
            if (row % 2 == 0) {
                onePlayerPoint[i] = new Point(
                    onePlayerPoint[1].x + col * cellWidth,
                    onePlayerPoint[1].y - row * cellHeight
                );
            } else {
                onePlayerPoint[i] = new Point(
                    onePlayerPoint[1].x + (9 - col) * cellWidth,
                    onePlayerPoint[1].y - row * cellHeight
                );
            }
        }

        // ── Ladder Up ──
        ImageIcon arrowUp = assetManager.getGameAsset().getArrow_up();
        for (int i = 0; i < upJLabels.length; i++) {
            int tileIndex = GameLogical.LADDERS_UP[i][0];
            if (tileIndex < onePlayerPoint.length && onePlayerPoint[tileIndex] != null) {
                Point p = onePlayerPoint[tileIndex];
                upJLabels[i].setBounds(
                    p.x - Tunner,
                    p.y - Tunner - (tileIndex / 12 - 1),
                    GameUI.LADDER_AND_SNAKES_CELL_SIZE.width,
                    GameUI.LADDER_AND_SNAKES_CELL_SIZE.height
                );
                upJLabels[i].setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2));
                add(upJLabels[i]);
            }
            if (arrowUp != null)
                upJLabels[i].setIcon(AssetManager.scaleImage(arrowUp,
                    GameUI.LADDER_AND_SNAKES_CELL_SIZE.width,
                    GameUI.LADDER_AND_SNAKES_CELL_SIZE.height));
        }

        // ── Snake Down ──
        ImageIcon arrowDown = assetManager.getGameAsset().getArrow_down();
        for (int i = 0; i < downJLabels.length; i++) {
            int tileIndex = GameLogical.SNAKES_DOWN[i][0];
            if (tileIndex < onePlayerPoint.length && onePlayerPoint[tileIndex] != null) {
                Point p = onePlayerPoint[tileIndex];
                downJLabels[i].setBounds(
                    p.x - Tunner,
                    p.y - Tunner - (tileIndex / 12 - 1),
                    GameUI.LADDER_AND_SNAKES_CELL_SIZE.width,
                    GameUI.LADDER_AND_SNAKES_CELL_SIZE.height
                );
                downJLabels[i].setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLUE, 2));
                add(downJLabels[i]);
            }
            if (arrowDown != null)
                downJLabels[i].setIcon(AssetManager.scaleImage(arrowDown,
                    GameUI.LADDER_AND_SNAKES_CELL_SIZE.width,
                    GameUI.LADDER_AND_SNAKES_CELL_SIZE.height));
        }

        System.out.println("=== position check ===");
        for (int i = 1; i <= 10; i++)
            System.out.println("ช่อง " + i + ": " + onePlayerPoint[i]);
        System.out.println("ช่อง 100: " + onePlayerPoint[100]);

        repaint();
        revalidate();
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
            g.drawImage(bgImageIcon.getImage(), 0, 0, this);

        if (!debugmode) return;

        g.setColor(Color.RED);
        for (int i = 0; i < onePlayerPoint.length; i++) {
            if (onePlayerPoint[i] == null) continue;
            if (i == 1)
                g.drawImage(assetManager.getGameAsset().getArrow_down().getImage(),
                    onePlayerPoint[i].x, onePlayerPoint[i].y, 20, 20, this);
            g.fillOval(onePlayerPoint[i].x, onePlayerPoint[i].y, 5, 5);
            g.drawString(String.valueOf(i), onePlayerPoint[i].x, onePlayerPoint[i].y - 5);
        }
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

        return new JFrame() {{
            add(new JLabel(icon));
            setSize(icon.getIconWidth(), icon.getIconHeight());
            setLocationRelativeTo(null);
            setUndecorated(true);
            setAlwaysOnTop(true);
        }};
    }
}