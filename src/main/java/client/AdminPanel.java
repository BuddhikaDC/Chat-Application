package client;

import server.ChatService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;

public class AdminPanel extends JFrame {

    public AdminPanel() {
        setTitle("Admin Panel");

        // Initialize components
        JButton startChatButton = new JButton("Start Chat");
        JButton subscribeUserButton = new JButton("Subscribe User");
        JButton unsubscribeUserButton = new JButton("Unsubscribe User");
        JButton removeUserButton = new JButton("Remove User");

        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(4, 1, 10, 10));

        adminPanel.add(startChatButton);
        adminPanel.add(subscribeUserButton);
        adminPanel.add(unsubscribeUserButton);
        adminPanel.add(removeUserButton);

        setContentPane(adminPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);

        // Add button listeners
        startChatButton.addActionListener(this::startChat);
        subscribeUserButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Subscribe User logic here!"));
        unsubscribeUserButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Unsubscribe User logic here!"));
        removeUserButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Remove User logic here!"));
    }

    private void startChat(ActionEvent e) {
        try {
            ChatService service = (ChatService) Naming.lookup("rmi://localhost:1099/ChatService");
            service.startChat();
            JOptionPane.showMessageDialog(this, "Chat started!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to start chat!");
        }
    }
}
