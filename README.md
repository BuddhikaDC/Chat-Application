# Chat-Application
 Project Overview
This project is a multiuser chat application developed for the ICT3122 – Advanced Programming Practicum course at the University of Ruhuna. The application facilitates real-time messaging between users and includes administrative controls for managing chats and users.

 Features

# User Features
- Create account with:
  - Email, Username, Password, Nickname, Profile Picture
- Update profile (except email)
- Self-subscribe and unsubscribe to/from available chats
- Join and leave chats with automatic system notifications
- View other users’ nicknames and profile pictures during chat
- Send "Bye" to leave a chat

# Admin Features
- Create and manage chats
- Subscribe/unsubscribe users to chats
- Remove users from the system

# Chat Functionality
- Only subscribed and logged-in users can participate
- Displays:
  - Chat started time
  - User joined/left messages
  - Chat ended time
- Automatically ends when the last user leaves
- Chat logs saved as `.txt` files and recorded in MySQL

 #Technical Requirements

- GUI: Java Swing
- Design Pattern: Observer (used for subscribe/unsubscribe)
- Multithreading and RMI: To support chat sessions
- Backend: MySQL (via Hibernate ORM)
- Constraints:
  - Only one chat session active at a time
  - A user can only be in one chat at any time

 #Technologies Used
- Java SE
- Java RMI
- Java Swing
- Hibernate ORM
- MySQL
- JDBC
- Maven (for project management)

 #Setup Instructions

1. Database Setup:
   - Import the provided MySQL schema.
   - Update `hibernate.cfg.xml` with your DB credentials.

2. Build the Project:
   - Use `mvn clean install` or compile via IDE (e.g., IntelliJ or Eclipse).

3. Run the Application:
   - Start the RMI server.
   - Launch the GUI client application.

