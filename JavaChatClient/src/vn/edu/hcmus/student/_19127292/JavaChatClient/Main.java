package vn.edu.hcmus.student._19127292.JavaChatClient;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.net.*;
import java.io.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.border.EmptyBorder;

/**a
 * vn.edu.hcmus.student._19127292.JavaChatClient
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 16-Dec-21 - 23:26
 * Description: Main Class
 */
public class Main extends JFrame {
    /**
     * Enum: Message Status
     */
    public enum MessageStatus {
        /**
         * Waiting for response
         */
        Waiting,

        /**
         * Failed cause user is offline
         */
        Failed,

        /**
         * Send message accepted
         */
        Accepted
    }

    /**
     * Attribute: MessageStatus - messageStatus
     * Status of Message
     */
    public static MessageStatus messageStatus;

    /**
     * Attribute: Socket - server
     */
    private static Socket server;

    /**
     * Attribute: String[] - users
     * List of online users
     */
    private static String[] users;

    /**
     * Attribute: JList - usersList
     * Display List of online users
     */
    private static final JList<String> usersList = new JList<>();

    /**
     * Attribute: JLabel - conversationTitle
     * Display who user are chatting with
     */
    private static JLabel conversationTitle;

    /**
     * Attribute: JPanel - conversationPanel
     * Display conversation
     */
    private static JPanel conversationPanel;

    /**
     * Attribute: HashMap - conversations
     * List of conversations
     */
    private static final HashMap<String, JPanel> conversations = new HashMap<>();

    /**
     * Main function
     * @param args String[]
     */
    public static void main(String[] args) {
        if (connectServer()) new SignIn();
        else JOptionPane.showMessageDialog(null, "Server is not available!",
                "Connect error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Connect to Server
     * @return boolean: True if server are available, False if server are not available
     */
    private static boolean connectServer() {
        try {
            server = new Socket("localhost", 9999);
            Thread receiveServerMessagesThread = new Thread(Main::receiveServerMessages);
            receiveServerMessagesThread.start();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Default constructor
     */
    public Main() {
        addComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Java Chat App");
        setVisible(true);
    }

    /**
     * Add components to Main JFrame
     */
    public void addComponents() {
        // Content Pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JLabel onlineTitle = new JLabel("Online users");
        onlineTitle.setFont(new Font("Arial", Font.BOLD, 20));

        usersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                changeConversation(usersList.getSelectedValue());
            }
        });

        JScrollPane userScroll = new JScrollPane(usersList);
        userScroll.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userPanel.add(onlineTitle, BorderLayout.PAGE_START);
        userPanel.add(userScroll, BorderLayout.CENTER);
        userPanel.setPreferredSize(new Dimension(200, 650));

        leftPanel.add(userPanel);

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        conversationTitle = new JLabel(" ");
        conversationTitle.setFont(new Font("Arial", Font.BOLD, 20));

        conversationPanel = new JPanel(new BorderLayout());
        conversationPanel.setBackground(Color.WHITE);

        JScrollPane messageScroll = new JScrollPane(conversationPanel);
        messageScroll.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton fileButton = new JButton("\uD83D\uDCC1");
        fileButton.addActionListener(e -> fileButtonEventHandler());
        JTextField messageTextField = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            sendButtonEventHandler(messageTextField.getText());
            messageTextField.setText("");
        });

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        messagePanel.add(fileButton);
        messagePanel.add(Box.createHorizontalStrut(5));
        messagePanel.add(messageTextField);
        messagePanel.add(Box.createHorizontalStrut(5));
        messagePanel.add(sendButton);

        rightPanel.add(conversationTitle, BorderLayout.PAGE_START);
        rightPanel.add(messageScroll, BorderLayout.CENTER);
        rightPanel.add(messagePanel, BorderLayout.PAGE_END);

        // Add components to content pane and Settings
        contentPane.add(leftPanel, BorderLayout.LINE_START);
        contentPane.add(rightPanel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(825, 650));
        setContentPane(contentPane);
        pack();
    }

    /**
     * File Button Event Handler
     * Send a file
     */
    private void fileButtonEventHandler() {
        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                // Read file
                FileInputStream fileInputStream = new FileInputStream(fileChooser.getSelectedFile());
                byte[] data = fileInputStream.readAllBytes();
                fileInputStream.close();

                // Send file
                messageStatus = MessageStatus.Waiting;
                sendMessage("Command_SendFile`" + conversationTitle.getText() + "`" +
                        fileChooser.getSelectedFile().getName());
                while (messageStatus == MessageStatus.Waiting) System.out.print("");

                if (messageStatus == MessageStatus.Accepted) {
                    DataOutputStream dataOutputStream = new DataOutputStream(server.getOutputStream());
                    dataOutputStream.writeInt(data.length);
                    dataOutputStream.write(data);

                    conversations.get(conversationTitle.getText()).add(new ChatBubble(ChatBubble.BubbleType.Mine,
                            fileChooser.getSelectedFile().getName()));
                    revalidate();
                } else {
                    JOptionPane.showMessageDialog(this, "User is now offline.",
                            "Send File Failed", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception exception) {
                System.out.println("Error to send file!");
            }
        }
    }

    /**
     * Send Button Event Handler
     * Send a message
     * @param message String
     */
    private void sendButtonEventHandler(String message) {
        if (message.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter message.");
        } else if (conversations.get(conversationTitle.getText()) == null) {
            JOptionPane.showMessageDialog(this, "Please choose a person to chat.");
        } else {
            messageStatus = MessageStatus.Waiting;
            sendMessage("Command_SendMessage`" + conversationTitle.getText() + "`" + message);
            while (messageStatus == MessageStatus.Waiting) System.out.print("");

            if (messageStatus == MessageStatus.Accepted) {
                conversations.get(conversationTitle.getText()).add(new ChatBubble(ChatBubble.BubbleType.Mine, message));
                revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "User is now offline.",
                        "Send Message Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Change conversation when user click on online users list
     * @param conversationUser String: Selected user
     */
    private void changeConversation(String conversationUser) {
        for (int i = 0; i < users.length; i++) {
            if (users[i].contains(conversationUser)) {
                users[i] = users[i].replace(" (New Messages)", "");
                conversationUser = users[i];
            }
        }
        usersList.setListData(users);

        conversationTitle.setText(conversationUser);

        JPanel chatPanel = conversations.get(conversationUser);
        if (chatPanel == null) {
            chatPanel = new JPanel();
            chatPanel.setBackground(Color.WHITE);
            chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
            conversations.put(conversationUser, chatPanel);
        }

        conversationPanel.removeAll();
        conversationPanel.add(chatPanel, BorderLayout.PAGE_START);
        conversationPanel.revalidate();
        conversationPanel.repaint();
    }

    /**
     * Add a new message to corresponding conversation
     * @param username String
     * @param content String
     * @param bubbleType BubbleType
     */
    public static void addNewMessage(String username, String content, ChatBubble.BubbleType bubbleType) {
        for (int i = 0; i < users.length; i++) {
            if (users[i].contains(username) && !users[i].contains(" (New Messages)")) {
                if (!conversationTitle.getText().equals(users[i]))
                    users[i] = users[i] + " (New Messages)";
            }
        }
        usersList.setListData(users);

        if (conversations.get(username) == null) {
            JPanel chatPanel = new JPanel();
            chatPanel.setBackground(Color.WHITE);
            chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
            conversations.put(username, chatPanel);
        }

        conversations.get(username).add(new ChatBubble(bubbleType, content));
        conversationPanel.revalidate();
    }

    /**
     * Send a message to Server
     * @param message String
     */
    public static void sendMessage(String message) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception exception) {
            System.out.println("Send Message Error: " + exception);
        }
    }

    /**
     * Receive message from server and Process the command
     */
    private static void receiveServerMessages() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(server.getInputStream()));

            while (true) {
                String receivedMessage = bufferedReader.readLine();

                if (receivedMessage.contains("Command_CloseConnect")) {
                    System.out.println("Server closed!");
                    break;

                } else if (receivedMessage.contains("Command_UserList")) {
                    String[] str = receivedMessage.split("`");
                    users = new String[str.length - 1];
                    System.arraycopy(str, 1, users, 0, str.length - 1);
                    usersList.setListData(users);

                } else if (receivedMessage.contains("Command_AccountVerifyAccepted")) {
                    SignIn.status = SignIn.SignInStatus.Accepted;

                } else if (receivedMessage.contains("Command_AccountVerifyAlready")) {
                    SignIn.status = SignIn.SignInStatus.Already;

                } else if (receivedMessage.contains("Command_AccountVerifyFailed")) {
                    SignIn.status = SignIn.SignInStatus.Failed;

                } else if (receivedMessage.contains("Command_CreateAccountAccepted")) {
                    SignUp.status = SignUp.SignUpStatus.Accepted;

                } else if (receivedMessage.contains("Command_CreateAccountFailed")) {
                    SignUp.status = SignUp.SignUpStatus.Failed;

                } else if (receivedMessage.contains("Command_SendMessageAccepted")) {
                    messageStatus = MessageStatus.Accepted;

                } else if (receivedMessage.contains("Command_SendMessageFailed")) {
                    messageStatus = MessageStatus.Failed;

                } else if (receivedMessage.contains("Command_Message")) {
                    String[] str = receivedMessage.split("`");
                    addNewMessage(str[1], str[2], ChatBubble.BubbleType.Others);

                } else if (receivedMessage.contains("Command_File")) {
                    sendMessage("Command_Accepted");
                    String[] str = receivedMessage.split("`");

                    DataInputStream dataInputStream = new DataInputStream(server.getInputStream());
                    byte[] data = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(data, 0, data.length);

                    FileOutputStream fileOutputStream = new FileOutputStream(str[2]);
                    fileOutputStream.write(data);
                    fileOutputStream.close();

                    addNewMessage(str[1], str[2], ChatBubble.BubbleType.File);

                } else {
                    System.out.println(receivedMessage);
                }
            }

            bufferedReader.close();
            server.close();
        } catch (Exception exception) {
            System.out.println("Receive Server Message Error: " + exception);
        }
    }
}