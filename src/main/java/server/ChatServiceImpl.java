package server;

import client.ChatClient;
import dao.ChatDao;
import dao.UserDao;
import model.Chat;
import model.User;
import observer.ChatObserver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {


    private final List<ChatClient> clients = new ArrayList<>();
    private final List<ChatObserver> observers = new ArrayList<>();
    private final List<String> chatLog = new ArrayList<>();
    private final List<String> activeUsers = new ArrayList<>();
    private LocalDateTime chatStartTime;
    private final Set<String> subscribedUsers = new HashSet<>();




    protected ChatServiceImpl() throws RemoteException {
        super();
    }

    public void registerUser(String email, String username, String password, String nickname, String profilePicture) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setProfilePicture(profilePicture);
        user.setRole("user");

        new UserDao().saveUser(user);
        System.out.println("User registered: " + username);
    }



    @Override
    public void startChat() {
        chatStartTime = LocalDateTime.now();
        String startMessage = "Chat started at: " + chatStartTime;
        chatLog.add(startMessage);
        try {
            notifyObservers(startMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(startMessage);
    }


    @Override
    public void sendMessage(String nickname, String message) {
        String formattedMessage = nickname + ": " + message;
        chatLog.add(formattedMessage);
        try {
            notifyObservers(formattedMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void leaveChat(String nickname) {
        String leaveMessage = nickname + " left: " + LocalDateTime.now();
        chatLog.add(leaveMessage);
        try {
            notifyObservers(leaveMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        activeUsers.remove(nickname);

        if (activeUsers.isEmpty()) {
            endChat();
        }
    }
    private void endChat() {
        String endMessage = "Chat stopped at: " + LocalDateTime.now();
        chatLog.add(endMessage);
        try {
            notifyObservers(endMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        saveChatToFile();
    }

    private void saveChatToFile() {
        try {
            String fileName = "chat_" + System.currentTimeMillis() + ".txt";
            Path filePath = Paths.get(fileName);
            Files.write(filePath, chatLog, StandardCharsets.UTF_8);
            System.out.println("Chat saved to " + filePath.toAbsolutePath());

            // Save to DB
            Chat chat = new Chat();
            chat.setStartTime(chatStartTime);
            chat.setEndTime(LocalDateTime.now());
            chat.setFilePath(filePath.toString());
            new ChatDao().saveChat(chat);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean login(String email, String password){
        User user =new UserDao().getUserByEmail(email);
        String userpwd = user.getPassword();

        if (password.equals(userpwd)) {
            if (user.getRole().equals("admin")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void registerObserver(ChatObserver observer) {
        observers.add(observer);
        try {
            notifyObservers("A new user has joined the chat.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void removeObserver(ChatObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void subscribeUserToChat(String email) throws RemoteException {
        subscribedUsers.add(email);
    }

    @Override
    public void unsubscribeUserFromChat(String email) throws RemoteException {
        subscribedUsers.remove(email);
    }


    private void notifyObservers(String message) throws RemoteException {
        for (ChatObserver observer : observers) {
            observer.update(message);
        }
    }


    public  String getNicknameByEmail(String email) {
        String nickName = new UserDao().getNickNameByEmail(email);
        return nickName;
    }


}
