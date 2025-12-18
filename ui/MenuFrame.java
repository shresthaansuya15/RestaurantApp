import dataaccess.FoodItemDAO;
import model.FoodItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuFrame extends JFrame {

    public MenuFrame(String restaurantId, String restaurantName, String address,
                     String diningType, String hours, String username) {

        setTitle("Menu - " + restaurantName);
        setSize(420, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(255, 182, 193)); // pastel pink

        // Background image
        JLabel bg = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/main_bg.png")));
        bg.setBounds(0, 0, 420, 550);
        mainPanel.add(bg);

        // Title
        JLabel title = new JLabel("Menu – " + restaurantName, SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        title.setBounds(30, 10, 360, 30);
        bg.add(title);

        // Top panel for restaurant details
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBounds(10, 50, 400, 140);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: Restaurant Image
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/restaurant_images/" + restaurantId + ".jpg"));
            Image img = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            topPanel.add(imgLabel, BorderLayout.WEST);
        } catch (Exception ex) {
            System.out.println("Image not found for restaurant " + restaurantId);
        }

        // Right: Restaurant info
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel nameLabel = new JLabel(restaurantName);
        nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        JLabel addressLabel = new JLabel("Address: " + address);
        JLabel diningLabel = new JLabel("Dining: " + diningType);
        JLabel hoursLabel = new JLabel("Hours: " + hours);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(addressLabel);
        infoPanel.add(diningLabel);
        infoPanel.add(hoursLabel);

        topPanel.add(infoPanel, BorderLayout.CENTER);
        bg.add(topPanel);

        // Scrollable menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(255, 240, 245)); // light blush
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        FoodItemDAO dao = new FoodItemDAO();
        List<FoodItem> menu = dao.getFoodItemsByRestaurant(restaurantId);

        for (FoodItem item : menu) {
            JPanel row = new JPanel(new BorderLayout());
            row.setMaximumSize(new Dimension(360, 50));
            row.setBackground(Color.WHITE);
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.PINK, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));

            JLabel name = new JLabel(item.getName());
            name.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel price = new JLabel("$" + item.getPrice());
            price.setFont(new Font("Arial", Font.PLAIN, 13));

            row.add(name, BorderLayout.WEST);
            row.add(price, BorderLayout.EAST);

            menuPanel.add(row);
            menuPanel.add(Box.createVerticalStrut(5));
        }

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBounds(10, 200, 400, 340);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        bg.add(scroll);
        add(mainPanel);
        setVisible(true);
    }
}
