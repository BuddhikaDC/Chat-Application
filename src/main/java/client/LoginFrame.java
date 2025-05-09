package client;

import server.ChatService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel loginPanel;

    public LoginFrame() {
        setTitle("Login");

        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        setContentPane(loginPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setVisible(true);

        loginButton.addActionListener(this::loginAction);
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });
    }


    private void loginAction(ActionEvent e) {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {
            ChatService service = (ChatService) Naming.lookup("rmi://localhost:1099/ChatService");
            boolean isAdmin = service.login(email, password);

            if (isAdmin) {
                JOptionPane.showMessageDialog(this, "Admin Login successful!");
                dispose();
                new AdminPanel();
            } else {
                String nickname = service.getNicknameByEmail(email);
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + nickname);
                dispose();

                ChatClient chatClient = new ChatClient(email, nickname);
                chatClient.startClient();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login failed!");
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
