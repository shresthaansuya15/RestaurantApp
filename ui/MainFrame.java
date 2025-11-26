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
        
        // Cuisine buttons with images 
        String[] cuisines = {"Indian", "Chinese", "Italian", "Mexican", "American", "Japanese", "Vietnamese", "French", "Mediterranean", "Vegetarian", "Asian"}; 
        int x = 50, y = 100; 
        for (String cuisine : cuisines) 
        { 
            // Load image for the cuisine 
            ImageIcon icon = null; 
            try 
            { 
                icon = new ImageIcon(getClass().getResource("/resources/cuisine_icons/" + cuisine.toLowerCase() + ".png")); 
                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); 
                icon = new ImageIcon(img); 
            } 
            catch (Exception ex) 
            { 
                System.out.println("Image not found for " + cuisine); 
            } 
            
            // Create button 
            JButton btn = new JButton(cuisine, icon); 
            btn.setHorizontalTextPosition(SwingConstants.CENTER); 
            btn.setVerticalTextPosition(SwingConstants.BOTTOM); 
            btn.setBounds(x, y, 120, 80); 
            btn.setBackground(new Color(255, 182, 193, 200)); 
            btn.setForeground(Color.BLACK); 
            btn.setFocusPainted(false); 
            btn.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

            // Hover zoom effect
            final Rectangle originalBounds = btn.getBounds();
            final int targetWidth = (int)(originalBounds.width * 1.2);
            final int targetHeight = (int)(originalBounds.height * 1.2);

            Timer growTimer = new Timer(10, null);
            Timer shrinkTimer = new Timer(10, null);

            btn.addMouseListener(new MouseAdapter() 
            { 
                @Override
                public void mouseEntered(MouseEvent e) 
                {
                    shrinkTimer.stop();
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btn.setBackground(new Color(255, 105, 180)); // darker pink
                    btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

                    growTimer.addActionListener(ev -> {
                        Rectangle b = btn.getBounds();
                        if (b.width < targetWidth) {
                            int newWidth = Math.min(b.width + 2, targetWidth);
                            int newHeight = Math.min(b.height + 2, targetHeight);
                            int newX = b.x - 1; // center
                            int newY = b.y - 1;
                            btn.setBounds(newX, newY, newWidth, newHeight);
                        } else {
                            growTimer.stop();
                        }
                    });
                    growTimer.start();
                }

                @Override
                public void mouseExited(MouseEvent e) 
                {
                    growTimer.stop();
                    btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    btn.setBackground(new Color(255, 182, 193, 200));
                    btn.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

                    shrinkTimer.addActionListener(ev -> {
                        Rectangle b = btn.getBounds();
                        if (b.width > originalBounds.width) {
                            int newWidth = Math.max(b.width - 2, originalBounds.width);
                            int newHeight = Math.max(b.height - 2, originalBounds.height);
                            int newX = b.x + 1;
                            int newY = b.y + 1;
                            btn.setBounds(newX, newY, newWidth, newHeight);
                        } else {
                            shrinkTimer.stop();
                        }
                    });
                    shrinkTimer.start();
                }
            });

            // Click event
            btn.addActionListener(e -> showRestaurants(cuisine)); 
            bgLabel.add(btn); 
            x += 140; 
            if (x > 700) 
            { 
                x = 50; 
                y += 100; 
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