import javax.swing.JFrame;

public class Main {
    public static GuiDisplay guiDisplay;
    
    public static void main(String[] args) {
        AssetManager assetManager = new AssetManager(); 
        guiDisplay = new GuiDisplay(assetManager){{
            setVisible(true);
        }};
    }

    public static GuiDisplay getGuiDisplay() {
        return guiDisplay;
    }
}

class GuiDisplay extends JFrame {
    MenuPanel menuPanel;
    GuiDisplay(AssetManager assetManager){
        setSize(DisplayUI.WINDOW_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        
        menuPanel = new MenuPanel(assetManager);
        add(menuPanel);

        menuPanel.addMenuActionListener("Offline Mode", e -> {
            remove(menuPanel);
            add(new OffineModeManagement(assetManager).getComponetManagement().getOfflineLobby());
            repaint();
            revalidate();
        });

        menuPanel.addMenuActionListener("JOIN",e -> {
            
        });
        menuPanel.addMenuActionListener("HOST",e -> {
            
        });
        menuPanel.addMenuActionListener("Setting",e -> {
            
        });
        menuPanel.addMenuActionListener("Quit",e -> {
            
        });
    }
}


