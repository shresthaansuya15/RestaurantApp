import dataaccess.FoodItemDAO;
import model.FoodItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuFrame extends JFrame 
{
    public MenuFrame(String restaurantId, String restaurantName, String username) 
    {
        setTitle("Menu - " + restaurantName);
        setSize(420, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(255, 182, 193)); // pastel pink

        // Background image (optional – remove if you don’t want image)
        JLabel bg = new JLabel(new ImageIcon(
                getClass().getResource("/resources/backgrounds/main_bg.png")));
        bg.setBounds(0, 0, 420, 550);
        mainPanel.add(bg);

        // Title
        JLabel title = new JLabel("Menu – " + restaurantName, SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        title.setBounds(30, 20, 360, 30);
        bg.add(title);

        // Scrollable menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(255, 240, 245)); // light blush

        FoodItemDAO dao = new FoodItemDAO();
        List<FoodItem> menu = dao.getFoodItemsByRestaurant(restaurantId);

        for (FoodItem item : menu) 
        {
            JPanel row = new JPanel(new BorderLayout());
            row.setMaximumSize(new Dimension(350, 45));
            row.setBackground(Color.WHITE);
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.PINK),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));

            JLabel name = new JLabel(item.getName());
            name.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel price = new JLabel("$" + item.getPrice());
            price.setFont(new Font("Arial", Font.PLAIN, 13));

            row.add(name, BorderLayout.WEST);
            row.add(price, BorderLayout.EAST);

            menuPanel.add(row);
            menuPanel.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBounds(30, 70, 360, 420);
        scroll.setBorder(null);

        bg.add(scroll);
        add(mainPanel);
        setVisible(true);
    }
}
