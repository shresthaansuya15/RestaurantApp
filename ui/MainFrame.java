import dataaccess.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

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

        restaurantDAO = new RestaurantDAO();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 182, 193));

        JLabel bgLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/main_bg.png")));
        bgLabel.setBounds(0, 0, 800, 600);
        panel.add(bgLabel);

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

            JButton btn = new JButton(cuisine, icon);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setBounds(x, y, 120, 80);
            btn.setBackground(new Color(255, 182, 193, 200));
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

            // Hover effect
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
                    btn.setBackground(new Color(255, 105, 180));
                    btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                    growTimer.addActionListener(ev -> 
                    {
                        Rectangle b = btn.getBounds();
                        if (b.width < targetWidth) 
                        {
                            int newWidth = Math.min(b.width + 2, targetWidth);
                            int newHeight = Math.min(b.height + 2, targetHeight);
                            int newX = b.x - 1;
                            int newY = b.y - 1;
                            btn.setBounds(newX, newY, newWidth, newHeight);
                        } 
                        else 
                        {
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
                    shrinkTimer.addActionListener(ev -> 
                    {
                        Rectangle b = btn.getBounds();
                        if (b.width > originalBounds.width) 
                        {
                            int newWidth = Math.max(b.width - 2, originalBounds.width);
                            int newHeight = Math.max(b.height - 2, originalBounds.height);
                            int newX = b.x + 1;
                            int newY = b.y + 1;
                            btn.setBounds(newX, newY, newWidth, newHeight);
                        } 
                        else 
                        {
                            shrinkTimer.stop();
                        }
                    });
                    shrinkTimer.start();
                }
            });

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

    private void showRestaurants(String cuisine) 
    {
        java.util.List<Restaurant> originalList = restaurantDAO.getRestaurantsByCuisine(cuisine);
        @SuppressWarnings("unchecked")
        final java.util.List<Restaurant>[] currentList = new java.util.List[]{new ArrayList<>(originalList)};

        JFrame frame = new JFrame(cuisine + " Restaurants");
        frame.setSize(650, 600);
        frame.setLocationRelativeTo(this);

        JLabel bgLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/main_bg.png"))); 
        bgLabel.setLayout(new BorderLayout()); 
        frame.setContentPane(bgLabel); 

        JPanel mainPanel = new JPanel(new BorderLayout()); 
        mainPanel.setOpaque(false);

        // Top bar
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBackground(new Color(255, 160, 180, 200));
        topBar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(100, 30));
        backBtn.addActionListener(e -> frame.dispose());
        topBar.add(backBtn);
        topBar.add(Box.createHorizontalStrut(10));

        JTextField searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(150, 30));
        topBar.add(searchField);
        topBar.add(Box.createHorizontalStrut(10));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(Color.WHITE);
        searchBtn.setPreferredSize(new Dimension(80, 30));
        topBar.add(searchBtn);
        topBar.add(Box.createHorizontalStrut(10));

        JButton sortHighBtn = new JButton("Sort: High Rating");
        sortHighBtn.setBackground(Color.WHITE);
        sortHighBtn.setPreferredSize(new Dimension(130, 30));
        topBar.add(sortHighBtn);
        topBar.add(Box.createHorizontalStrut(10));

        JButton sortLowBtn = new JButton("Sort: Low Rating");
        sortLowBtn.setBackground(Color.WHITE);
        sortLowBtn.setPreferredSize(new Dimension(130, 30));
        topBar.add(sortLowBtn);
        topBar.add(Box.createHorizontalStrut(5));

        JButton sortAlphaBtn = new JButton("Sort: A-Z");
        sortAlphaBtn.setBackground(Color.WHITE);
        sortAlphaBtn.setPreferredSize(new Dimension(100, 30));
        topBar.add(sortAlphaBtn);

        mainPanel.add(topBar, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(listPanel); 
        scrollPane.getViewport().setOpaque(false); 
        scrollPane.setOpaque(false); 
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        Runnable loadList = () -> 
        {
            listPanel.removeAll();
            for (Restaurant r : currentList[0]) 
            {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                card.setBackground(new Color(255, 255, 255, 200));
                card.setMaximumSize(new Dimension(600, 130));

                // Left thumbnail
                try 
                {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/resources/restaurant_images/" + r.getId() + ".jpg"));
                    Image img = icon.getImage();
                    int newWidth = 120;
                    int newHeight = (img.getHeight(null) * newWidth) / img.getWidth(null);
                    Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
                    card.add(imgLabel, BorderLayout.WEST);
                }  
                catch (Exception ex) 
                {
                    System.out.println("Image not found for " + r.getName());
                }

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setOpaque(false);
                infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                JLabel name = new JLabel(r.getName());
                name.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
                JLabel ratingLabel = new JLabel("Rating: " + r.getRating() + " ⭐");

                String priceText;
                switch (r.getPriceRange()) 
                {
                    case "$": priceText = "Inexpensive"; break;
                    case "$$": priceText = "Moderate"; break;
                    case "$$$": priceText = "Expensive"; break;
                    case "$$$$": priceText = "Luxury"; break;
                    default: priceText = "Not specified";
                }
                JLabel priceLabel = new JLabel("Price: " + priceText);

                JButton detailBtn = new JButton("View Details");
                detailBtn.setBackground(new Color(255, 182, 193));

                // Detail page
                detailBtn.addActionListener(ev -> 
                {
                    JFrame detailFrame = new JFrame(r.getName() + " Details");
                    detailFrame.setSize(700, 500);
                    detailFrame.setLocationRelativeTo(frame);

                    JLabel detailBg = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/main_bg.png")));
                    detailBg.setLayout(new BorderLayout());
                    detailFrame.setContentPane(detailBg);

                    JPanel content = new JPanel(new BorderLayout(20, 0));
                    content.setOpaque(false);
                    content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    // Left info
                    JPanel infoDetail = new JPanel();
                    infoDetail.setLayout(new BoxLayout(infoDetail, BoxLayout.Y_AXIS));
                    infoDetail.setOpaque(false);

                    JLabel nameDetail = new JLabel(r.getName());
                    nameDetail.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
                    JLabel addressDetail = new JLabel("Address: " + r.getAddress());
                    JLabel ratingDetail2 = new JLabel("Rating: " + r.getRating() + " ⭐");

                    String priceDetail;
                    switch (r.getPriceRange()) 
                    {
                        case "$": priceDetail = "Inexpensive"; break;
                        case "$$": priceDetail = "Moderate"; break;
                        case "$$$": priceDetail = "Expensive"; break;
                        case "$$$$": priceDetail = "Luxury"; break;
                        default: priceDetail = "Not specified";
                    }

                    JLabel descriptionLabel = new JLabel("<html><body style='width: 250px;'>Cuisines: " + String.join(", ", r.getCuisines()) + 
                                                     "<br>Dining Type: " + r.getDiningType() + 
                                                     "<br>Price Range: " + priceDetail + 
                                                     "<br>Hours: " + r.getHours() + 
                                                     "<br>Phone: " + r.getPhone() + 
                                                     "<br>Email: " + r.getEmail() + "</body></html>");

                    infoDetail.add(nameDetail);
                    infoDetail.add(Box.createVerticalStrut(10));
                    infoDetail.add(addressDetail);
                    infoDetail.add(Box.createVerticalStrut(5));
                    infoDetail.add(ratingDetail2);
                    infoDetail.add(Box.createVerticalStrut(5));
                    infoDetail.add(descriptionLabel);

                    content.add(infoDetail, BorderLayout.WEST);

                    // Right image
                    try 
                    {
                        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/restaurant_images/" + r.getId() + ".jpg"));
                        Image img = icon.getImage();
                        int newWidth = 250;
                        int newHeight = (img.getHeight(null) * newWidth) / img.getWidth(null);
                        Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
                        content.add(imgLabel, BorderLayout.EAST);
                    } 
                    catch (Exception ex) 
                    {
                        System.out.println("Image not found for " + r.getName());
                    }

                    detailBg.add(content, BorderLayout.CENTER);
                    detailFrame.setVisible(true);
                });

                infoPanel.add(name);
                infoPanel.add(ratingLabel);
                infoPanel.add(priceLabel);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(detailBtn);

                card.add(infoPanel, BorderLayout.CENTER);

                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(10));
            }
            listPanel.revalidate();
            listPanel.repaint();
        };

        loadList.run();

        searchBtn.addActionListener(e -> 
        {
            String keyword = searchField.getText().trim().toLowerCase();
            if (keyword.isEmpty()) 
            {
                currentList[0] = new ArrayList<>(originalList);
            } 
            else 
            {
                java.util.List<Restaurant> filtered = new ArrayList<>();
                for (Restaurant r : originalList) 
                {
                    if (r.getName().toLowerCase().contains(keyword)) 
                    {
                        filtered.add(r);
                    }
                }
                currentList[0] = filtered;
            }
            loadList.run();
        });

        sortHighBtn.addActionListener(e -> 
        {
            currentList[0].sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
            loadList.run();
        });

        sortLowBtn.addActionListener(e -> 
        {
            currentList[0].sort(Comparator.comparingDouble(Restaurant::getRating));
            loadList.run();
        });

        sortAlphaBtn.addActionListener(e -> 
        {
            currentList[0].sort(Comparator.comparing(Restaurant::getName, String.CASE_INSENSITIVE_ORDER));
            loadList.run();
        });

        bgLabel.add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new MainFrame("User"));
    }
}
