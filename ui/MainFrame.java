import dataaccess.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainFrame extends JFrame 
{
    private RestaurantDAO restaurantDAO;

    public MainFrame(String username) 
    {
        setTitle("Restaurant Advisor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        restaurantDAO = new RestaurantDAO(); // handles reading restaurants.txt

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 182, 193)); // pastel pink

        // Background image
        JLabel bgLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/main_bg.png")));
        bgLabel.setBounds(0, 0, 800, 600);
        panel.add(bgLabel);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Hi, " + username + "! What are you craving today?");
        welcomeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBounds(50, 30, 700, 40);
        bgLabel.add(welcomeLabel);

        // Cuisine buttons
        String[] cuisines = {"Indian", "Chinese", "Italian", "Mexican", "American", "Japanese", "Vietnamese", "French", "Mediterranean", "Vegetarian", "Asian"};
        int x = 50, y = 100;
        for (String cuisine : cuisines) 
        {
            JButton btn = new JButton(cuisine);
            btn.setBounds(x, y, 120, 40);
            btn.setBackground(Color.PINK);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            bgLabel.add(btn);

            // Click event to filter restaurants
            btn.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showRestaurants(cuisine);
                }
            });

            x += 140;
            if (x > 700) 
            {
                x = 50;
                y += 60;
            }
        }

        add(panel);
        setVisible(true);
    }

    // Show restaurants of selected cuisine in a new scrollable frame
    private void showRestaurants(String cuisine) 
    {
        List<Restaurant> list = restaurantDAO.getRestaurantsByCuisine(cuisine);

        JFrame frame = new JFrame(cuisine + " Restaurants");
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (Restaurant r : list) 
        {
            JPanel rPanel = new JPanel();
            rPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            rPanel.setBackground(new Color(255, 192, 203)); // lighter pink

            JLabel nameLabel = new JLabel(r.getName() + " (" + r.getDiningType() + ")");
            JLabel addressLabel = new JLabel("Address: " + r.getAddress());

            rPanel.add(nameLabel);
            rPanel.add(addressLabel);

            JButton detailsBtn = new JButton("View Details");
            rPanel.add(detailsBtn);

            panel.add(rPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public static void main(String[] args) 
    {
        new MainFrame("Ansuya");
    }
}

