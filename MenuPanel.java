import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;

public class MenuPanel extends JPanel {
    String[] TextOnButton = {"Offline Mode", "Setting", "Quit"};
    JButton[] MenuButton = new JButton[TextOnButton.length];
    JPanel menuBar;
    
    JLabel titleLabel;
    JPanel contentPanel;
    AssetManager assetManager; 

    MenuPanel(AssetManager assetManager) {
        this.assetManager = assetManager;

        for (int i = 0; i < TextOnButton.length; i++) {
            MenuButton[i] = new JButton(TextOnButton[i]);
        }

        menuBar = new JPanel() {{
            setLayout(new GridLayout(TextOnButton.length, 1, 0, 10)); 
            for (int i = 0; i < TextOnButton.length; i++) {
                add(MenuButton[i]);
            }
        }};

        titleLabel = new JLabel("My Snake Game"){{
            setFont(new Font("Arial", Font.BOLD, 36)); 
            setHorizontalAlignment(SwingConstants.CENTER);
        }};

        contentPanel = new JPanel(new BorderLayout(0, 20)){{
            add(titleLabel, BorderLayout.NORTH);
            add(menuBar, BorderLayout.CENTER);  
        }}; 
        

        setLayout(new GridLayout(1, 3)); 
        setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0));

        add(new JPanel());   
        add(contentPanel);   
        add(new JPanel());     
    }

    public void addMenuActionListener(String MenuName, ActionListener listener) {
        for (int i = 0; i < MenuButton.length; i++) {
            if (MenuButton[i].getText().equals(MenuName)) {
                MenuButton[i].addActionListener(listener);
                break;
            }
        }
    }

    public JButton[] getMenuButton() {
        return MenuButton;
    }
}


class SettingPanel extends JPanel {

}