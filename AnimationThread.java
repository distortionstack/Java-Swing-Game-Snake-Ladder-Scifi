import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class AnimationThread extends Thread{
    PlayerData playerData;
    GamePanel gamePanel;
    GameOfflineLogicalProcessor offlineLogical;
    boolean running = true;
    long sleepDuration = 200;
    int currentVisual;
    int targetIndex;
    AnimationThread(PlayerData playerData , GamePanel gamePanel){
        this.playerData = playerData;
        this.gamePanel = gamePanel;
    }
}


class OfflineAnimationThread extends AnimationThread{
    OfflineAnimationThread(PlayerData playerData , GamePanel gamePanel , GameOfflineLogicalProcessor offlineLogical){
        super(playerData, gamePanel);
        this.offlineLogical = offlineLogical;
        start();
    }
    @Override
    public void run() {
        GameStatus status = playerData.getgStatus();
        currentVisual = status.getVisibleIndex();
        targetIndex = status.getIndex();
        
        gamePanel.BlockDiceButton(); // ล็อกปุ่ม

        while (running) {
            // 1. ถ้าเดินมาถึงช่องเป้าหมาย (ตามลูกเต๋า) แล้ว
            if (currentVisual == targetIndex) { 
                
                // 2. เช็คดูซิว่า ตรงนี้มี งู หรือ บันได ไหม?
                boolean needWarp = offlineLogical.CheckLadderAndCSnakes();
                
                if (needWarp) {
                    // ถ้าเจอ: ให้หน่วงเวลา + วาร์ป
                    try {
                        // A. หน่วงเวลา 0.8 วินาที ให้คนเห็นว่าตกช่องนี้
                       
                        // B. วาร์ป! (ดึงค่า Index ใหม่ที่เปลี่ยนแล้วมาแสดงเลย)
                        targetIndex = status.getIndex(); // ค่านี้ถูกแก้ใน CheckLadder... แล้ว
                        JFrame animatFrame = gamePanel.getAnimateUFO(targetIndex,currentVisual);
                        animatFrame.setVisible(true);
                        sleep(2000); 
                        animatFrame.dispose();
                        currentVisual = targetIndex;     // วาร์ปตัวแปร Visual
                        
                        status.setVisibleIndex(currentVisual); // อัปเดตจอทันที
                        gamePanel.repaint();
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 3. จบเทิร์น (ไม่ว่าจะวาร์ปหรือไม่วาร์ป ก็จบเทิร์นตรงนี้)
                System.out.println("Turn Finished.");
                gamePanel.UnBlockDiceButton();
                offlineLogical.NextTurn(); 
                return; // ออกจาก Thread
            }
            try {
                sleep(sleepDuration);
                // ขยับ 1 ช่อง
                currentVisual++;
                status.setVisibleIndex(currentVisual);
                gamePanel.repaint();
            } catch (InterruptedException e) {
                System.out.println("AnimationThread Eror: " + e.getMessage());
            }
        }
    }
}
