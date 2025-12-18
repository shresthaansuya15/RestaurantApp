import model.User;
import dataaccess.UserDAO;

import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JFrame {

    private final UserDAO userDAO;
    @SuppressWarnings("unused")
    private final String username;

    public ProfileFrame(String username) {
        this.username = username;
        this.userDAO = new UserDAO();

        setTitle("User Profile");
        setSize(500, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== Background =====
        JLabel bg;
        try {
            bg = new JLabel(new ImageIcon(getClass()
                    .getResource("/resources/backgrounds/main_bg.png")));
        } catch (Exception e) {
            bg = new JLabel();
            bg.setOpaque(true);
            bg.setBackground(new Color(255, 182, 193));
        }
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        // ===== Main Panel =====
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== Title =====
        JLabel title = new JLabel("👋 Hi " + username + "!");
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        title.setForeground(new Color(139, 0, 139));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));

        // ===== Load User =====
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found!");
            dispose();
            return;
        }

        // ===== Info Panel =====
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setOpaque(false);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");
        JLabel lblFullName = new JLabel("Full Name:");
        JLabel lblEmail = new JLabel("Email:");

        JTextField txtUsername = new JTextField(user.getUsername());
        txtUsername.setEditable(false);

        // 🔐 PASSWORD FIELD (CORRECT)
        JPasswordField txtPassword = new JPasswordField(user.getPassword());
        txtPassword.setEchoChar('•');
        txtPassword.setEditable(false);

        JTextField txtFullName = new JTextField(user.getFullName());
        txtFullName.setEditable(false);

        JTextField txtEmail = new JTextField(user.getEmail());
        txtEmail.setEditable(false);

        Color pink = new Color(255, 182, 193);
        txtUsername.setBackground(pink);
        txtPassword.setBackground(pink);
        txtFullName.setBackground(pink);
        txtEmail.setBackground(pink);

        infoPanel.add(lblUsername);
        infoPanel.add(txtUsername);
        infoPanel.add(lblPassword);
        infoPanel.add(txtPassword);
        infoPanel.add(lblFullName);
        infoPanel.add(txtFullName);
        infoPanel.add(lblEmail);
        infoPanel.add(txtEmail);

        mainPanel.add(infoPanel);

        // ===== Show / Hide Password Toggle =====
        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setOpaque(false);
        showPassword.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        showPassword.addActionListener(e -> {
            txtPassword.setEchoChar(
                showPassword.isSelected() ? (char) 0 : '•'
            );
        });

        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(showPassword);
        mainPanel.add(Box.createVerticalStrut(20));

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);

        JButton btnEdit = new JButton("Edit Profile");
        JButton btnSave = new JButton("Save Profile");
        btnSave.setEnabled(false);

        btnEdit.setBackground(pink);
        btnSave.setBackground(pink);

        btnEdit.addActionListener(e -> {
            txtPassword.setEditable(true);
            txtFullName.setEditable(true);
            txtEmail.setEditable(true);
            btnSave.setEnabled(true);
            JOptionPane.showMessageDialog(this,
                    "You can now edit your profile.");
        });

        btnSave.addActionListener(e -> {
            user.setPassword(new String(txtPassword.getPassword()));
            user.setFullName(txtFullName.getText());
            user.setEmail(txtEmail.getText());

            boolean updated = userDAO.updateUser(user);
            if (updated) {
                JOptionPane.showMessageDialog(this,
                        "Profile updated successfully!");
                txtPassword.setEditable(false);
                txtFullName.setEditable(false);
                txtEmail.setEditable(false);
                btnSave.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update profile.");
            }
        });

        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSave);

        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }

    // ===== Optional test =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProfileFrame("User"));
    }
}
