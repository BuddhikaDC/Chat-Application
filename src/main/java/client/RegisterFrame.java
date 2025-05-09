package client;

import server.ChatService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;

public class RegisterFrame extends JFrame {
    private final JTextField emailField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField nicknameField;
    private final JTextField profilePictureField;

    public RegisterFrame() {
        setTitle("Register");

        // Initialize components
        emailField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        nicknameField = new JTextField(20);
        profilePictureField = new JTextField(20);
        JButton registerButton = new JButton("Register");
        JButton browseButton = new JButton("Browse");

        // Layout setup
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(6, 2, 5, 5));

        registerPanel.add(new JLabel("Email:"));
        registerPanel.add(emailField);
        registerPanel.add(new JLabel("Username:"));
        registerPanel.add(usernameField);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(passwordField);
        registerPanel.add(new JLabel("Nickname:"));
        registerPanel.add(nicknameField);
        registerPanel.add(new JLabel("Profile Picture:"));
        registerPanel.add(profilePictureField);
        registerPanel.add(browseButton);
        registerPanel.add(registerButton);

        setContentPane(registerPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setVisible(true);

        // Actions
        registerButton.addActionListener(this::registerAction);
        browseButton.addActionListener(this::browsePicture);
    }

    private void browsePicture(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            profilePictureField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void registerAction(ActionEvent e) {
        try {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String nickname = nicknameField.getText();
            String profilePicture = profilePictureField.getText();

            ChatService service = (ChatService) Naming.lookup("rmi://localhost:1099/ChatService");
            service.registerUser(email, username, password, nickname, profilePicture);

            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
            new LoginFrame();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registration failed!");
        }
    }
}
