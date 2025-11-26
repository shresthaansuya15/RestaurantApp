import dataaccess.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame 
{
    private JTextField userText;
    private JPasswordField passwordText;

    public LoginFrame() 
    {
        setTitle("Restaurant Advisor");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with pink background
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 182, 193)); // pastel pink

        // Background image
        JLabel bgLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/backgrounds/login_bg.png")));
        bgLabel.setBounds(0, 0, 400, 500);
        panel.add(bgLabel);

        // Title
        JLabel title = new JLabel("Good food deserves good reviews", SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        title.setForeground(Color.BLACK);
        title.setBounds(50, 30, 300, 30); // width can be adjusted
        bgLabel.add(title);

        // Restaurant icon under title, bigger and centered
        int iconSize = 120;
        JLabel restaurantIcon = new JLabel(scaleIcon("/resources/icons/restaurant.png", iconSize, iconSize));
        int iconX = (400 - iconSize) / 2; // center in 400px wide frame
        restaurantIcon.setBounds(iconX, 70, iconSize, iconSize);
        bgLabel.add(restaurantIcon);

        // Username label and field (shifted down)
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 230, 100, 25);
        bgLabel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(150, 230, 165, 25);
        bgLabel.add(userText);

        // Username icon
        JLabel userIcon = new JLabel(scaleIcon("/resources/icons/user_login.png", 32, 32));
        userIcon.setBounds(320, 230, 32, 32);
        bgLabel.add(userIcon);

        // Password label and field (shifted down)
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 280, 100, 25);
        bgLabel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(150, 280, 165, 25);
        bgLabel.add(passwordText);

        // Password icon
        JLabel passIcon = new JLabel(scaleIcon("/resources/icons/login_password.png", 32, 32));
        passIcon.setBounds(320, 280, 32, 32);
        bgLabel.add(passIcon);

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 325, 100, 30);
        loginButton.setBackground(Color.PINK);
        bgLabel.add(loginButton);

        JButton registerButton = new JButton("New User? Create Account");
        registerButton.setBounds(100, 365, 200, 30);
        registerButton.setBackground(Color.PINK);
        bgLabel.add(registerButton);

        JButton forgotButton = new JButton("Forgot Password");
        forgotButton.setBounds(120, 405, 150, 30);
        forgotButton.setBackground(Color.PINK);
        bgLabel.add(forgotButton);

        // Login action
        loginButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String username = userText.getText();
                String password = String.valueOf(passwordText.getPassword());

                UserDAO userDAO = new UserDAO();
                if(userDAO.validateUser(username, password))
                {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    new MainFrame(username); // open main screen
                    dispose();
                } 
                else 
                {
                JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    // Utility method to scale icons
    private ImageIcon scaleIcon(String path, int width, int height) 
    {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static void main(String[] args) 
    {
        new LoginFrame();
    }
}
