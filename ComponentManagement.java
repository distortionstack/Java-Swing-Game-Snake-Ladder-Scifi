import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class ComponentManagement {

}

abstract class GamePanel extends JPanel {
    // กำหนดระยะห่างของ "จุด" บนลูกเต๋า (ปรับเลขนี้ถ้าอยากให้ห่างกันมากขึ้น/น้อยลง)
    final int GAP = 10;
    boolean debugmode = false;
    AssetManager assetManager;
    // - component
    JButton diceButton;
    ImageIcon bgImageIcon;
    Point [] onePlayerPoint = new Point[GameLogical.INDEX_AMOUNT];
    JLabel [] upJLabels = new JLabel[GameLogical.LADDERS_UP.length];
    JLabel [] downJLabels = new JLabel[GameLogical.SNAKES_DOWN.length];
    
    GamePanel(AssetManager assetManager){
        //assign
        this.assetManager = assetManager;
        bgImageIcon = assetManager.getGameAsset().getGameBackGround();
        diceButton = new JButton(){{
            setBounds(new Rectangle(GameUI.DICE_BUTTON_POINT,GameUI.DICE_BUTTON_DIMENSION));
            setIcon(assetManager.getGameAsset().getDiceButtonUnBlock());
            setContentAreaFilled(false); // ปิดการระบายสีพื้นหลังปุ่ม
            setBorderPainted(false);     // ปิดขอบปุ่ม
            setFocusPainted(false);      // ปิดเส้นประเวลาคลิก
        }};

        for (int i = 0; i < upJLabels.length; i++) {
            upJLabels[i] = new JLabel(assetManager.getGameAsset().getArrow_up());
        }

        for (int i = 0; i < downJLabels.length; i++) {
            downJLabels[i] = new JLabel(assetManager.getGameAsset().getArrow_down());
        }

        //config this class
        setLayout(null);
        setPreferredSize(DisplayUI.WINDOW_SIZE);

        //config other
        PositionSetUP();
        add(diceButton);
    }
    //method
    // -setter
    public void addDiceButtonListener(ActionListener listener){
        this.diceButton.addActionListener(listener);
    }

    public void PositionSetUP(){
        onePlayerPoint[0] = GameUI.START_POINT_ONE_PLAYER;
        // กำหนดจุดเริ่มต้นช่องที่ 1 (มุมล่างซ้าย)
        onePlayerPoint[1] = new Point(420, 570); // ปรับตำแหน่งช่อง 1
        int cellWidth = 56;
        int cellHeight = 58;
        int Tunner = 14;
        for (int i = 2; i < onePlayerPoint.length; i++) {
            int row = (i - 1) / 10;          
            int col = (i - 1) % 10;          
            
            if (row % 2 == 0) {
                // แถวคี่: ซ้ายไปขวา
                onePlayerPoint[i] = new Point(
                    onePlayerPoint[1].x + col * cellWidth,
                    onePlayerPoint[1].y - row * cellHeight
                );
            } else {
                // แถวคู่: ขวาไปซ้าย  
                onePlayerPoint[i] = new Point(
                    onePlayerPoint[1].x + (9 - col) * cellWidth,
                    onePlayerPoint[1].y - row * cellHeight
                );
            }
        }

        // ตั้งค่าป้ายลูกศรขึ้น (Ladder Up)
        for (int i = 0; i < upJLabels.length; i++) {
            int tileIndex = GameLogical.LADDERS_UP[i][0]; // ดูว่าบันไดเริ่มช่องไหน
            if (tileIndex < onePlayerPoint.length && onePlayerPoint[tileIndex] != null) {
                Point p = onePlayerPoint[tileIndex];          
                upJLabels[i].setBounds(p.x - Tunner, p.y - Tunner - (tileIndex/12 - 1), GameUI.LADDER_AND_SNAKES_CELL_SIZE.width, GameUI.LADDER_AND_SNAKES_CELL_SIZE.height);
                upJLabels[i].setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2));
                add(upJLabels[i]);
            }

            // ใส่รูป + ย่อ (ใช้ scaleImage ที่มีอยู่แล้ว)
            ImageIcon originalUp = assetManager.getGameAsset().getArrow_up();
            if (originalUp != null) {
                upJLabels[i].setIcon(assetManager.scaleImage(originalUp, GameUI.LADDER_AND_SNAKES_CELL_SIZE.width, GameUI.LADDER_AND_SNAKES_CELL_SIZE.height));
            }
        }
        // ตั้งค่าป้ายลูกศรลง (Snake Down)
        for (int i = 0; i < downJLabels.length; i++) {
            int tileIndex = GameLogical.SNAKES_DOWN[i][0]; // ดูว่างูเริ่มช่องไหน
           
            if (tileIndex < onePlayerPoint.length && onePlayerPoint[tileIndex] != null) {
                Point p = onePlayerPoint[tileIndex];
                downJLabels[i].setBounds(p.x - Tunner, p.y - Tunner - (tileIndex/12 - 1), GameUI.LADDER_AND_SNAKES_CELL_SIZE.width, GameUI.LADDER_AND_SNAKES_CELL_SIZE.height);
                downJLabels[i].setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLUE, 2));
                add(downJLabels[i]);
            }

            // ใส่รูป + ย่อ (ใช้ scaleImage ที่มีอยู่แล้ว)
            ImageIcon originalUp = assetManager.getGameAsset().getArrow_down();
            if (originalUp != null) {
                downJLabels[i].setIcon(assetManager.scaleImage(originalUp, GameUI.LADDER_AND_SNAKES_CELL_SIZE.width, GameUI.LADDER_AND_SNAKES_CELL_SIZE.height));
            }

            ImageIcon testIcon = assetManager.getGameAsset().getArrow_up();
            System.out.println("Icon width: " + testIcon.getIconWidth());
            System.out.println("Icon height: " + testIcon.getIconHeight());
            System.out.println("Image: " + testIcon.getImage());

        }
        
        // ตรวจสอบตำแหน่ง
        System.out.println("=== position check ===");
        for (int i = 1; i <= 10; i++) {
            System.out.println("ช่อง " + i + ": " + onePlayerPoint[i]);
        }
        System.out.println("ช่อง 100: " + onePlayerPoint[100]);
        repaint();
        revalidate();
    }

    public void drawPlayer(Graphics g , ArrayList <PlayerData> playerList){
        if(playerList == null || playerList.isEmpty()){
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
            System.out.println("Drawing " + playerData.getSkincode() + " at index " + index + 
                            " position (" + position.x + "," + position.y + ")");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(bgImageIcon != null) {
            g.drawImage(bgImageIcon.getImage(), 0, 0, this);
        }
        
        g.setColor(Color.RED);
        for (int i = 0; i < onePlayerPoint.length; i++) {
            if (onePlayerPoint[i] != null) {
                // ดีบัก: วาดตำแหน่งทั้งหมด
                if(!debugmode){return;}
                if(i == 1){g.drawImage(assetManager.getGameAsset().getArrow_down().getImage(), onePlayerPoint[i].x, onePlayerPoint[i].y,20,20, this);}
                g.fillOval(onePlayerPoint[i].x, onePlayerPoint[i].y, 5, 5);
                g.drawString(String.valueOf(i), onePlayerPoint[i].x, onePlayerPoint[i].y - 5);
            }
        }
        
    }

    public void BlockDiceButton(){
        diceButton.setEnabled(false);
        diceButton.setIcon(assetManager.getGameAsset().getDiceButtonBlcoked());
    }

    public void UnBlockDiceButton(){
        diceButton.setEnabled(true);
        diceButton.setIcon(assetManager.getGameAsset().getDiceButtonUnBlock());
    }

    public JButton getDiceButton() {
        return diceButton;
    }

    public JFrame getAnimateUFO(int New, int Old){
        return new JFrame(){{
                        add(new JLabel(){{
                            if(New < Old){
                             setIcon(assetManager.getGameAsset().ufoDown);
                            }else{
                             setIcon(assetManager.getGameAsset().ufoUp);
                            }
                        }});
                        if(New < Old){
                             setSize(assetManager.getGameAsset().ufoDown.getIconWidth(),assetManager.getGameAsset().ufoDown.getIconHeight());
                        }else{
                             setSize(assetManager.getGameAsset().ufoUp.getIconWidth(),assetManager.getGameAsset().ufoUp.getIconHeight());
                        }
                        setLocationRelativeTo(null);
                        setUndecorated(true);
                        setAlwaysOnTop(true);
                }};
    }
}

abstract class LobbyPanel extends JPanel {
    JButton [] selectSkinButton = new JButton[GameLogical.SKINCODE_ARRAY_LENGTH];
    JPanel selectSkinPanel;
    JLabel LobbyLabel;
    JPanel operationPanel;
    AssetManager assetManager;
    LobbyPanel(AssetManager assetManager){
        this.assetManager = assetManager;
        
        for (int i = 0; i < selectSkinButton.length; i++) {
            selectSkinButton[i] = new JButton();
            selectSkinButton[i].setText(GameLogical.SKINCODE_ARRAY[i]);
        }

        //set up
        setForeground(Color.WHITE);

        selectSkinPanel = new JPanel(){{
            setOpaque(false);
            setLayout(new GridLayout(2, 3,40,40));
            for (int i = 0; i < selectSkinButton.length; i++) {
                add(selectSkinButton[i]);
            }
        }};

        LobbyLabel = new JLabel(){{
            setFont(new Font("Tohama",Font.BOLD,50));
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
            setText("Select Charector Room");
            setBackground(Color.RED);
            setForeground(Color.WHITE);
            
        }};
    }
    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        g.drawImage(assetManager.getGameAsset().getMenubackground().getImage(), 0, 0, this);
    }
    public JButton[] getSelectSkinButton() {
        return selectSkinButton;
    }
}

