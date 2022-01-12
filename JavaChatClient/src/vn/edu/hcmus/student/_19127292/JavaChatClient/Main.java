package vn.edu.hcmus.student._19127292.JavaChatClient;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;

import javax.swing.border.EmptyBorder;

/**
 * vn.edu.hcmus.student._19127292.JavaChatClient
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 16-Dec-21 - 23:26
 * Description: Main Class
 */
public class Main extends JFrame {
    private static Socket server;

    JLabel conversationTitle;

    public static void main(String[] args) {
        if (connectServer())
            new SignIn();
        else JOptionPane.showMessageDialog(null, "Server is not available!",
                "Connect error", JOptionPane.ERROR_MESSAGE);
    }

    public Main() {
        addComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Java Chat App");
        setVisible(true);
    }

    public void addComponents() {
        // Content Pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JLabel onlineTitle = new JLabel("Online users       ");
        onlineTitle.setFont(new Font("Arial", Font.BOLD, 20));

        String[] user = {"A", "B"};
        JList<String> userList = new JList<>(user);
        userList.addListSelectionListener(e -> changeConversation(userList.getSelectedValue()));

        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userPanel.add(onlineTitle, BorderLayout.PAGE_START);
        userPanel.add(userScroll, BorderLayout.CENTER);

        leftPanel.add(userPanel);

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        conversationTitle = new JLabel("");
        conversationTitle.setFont(new Font("Arial", Font.BOLD, 20));

        JTextArea conversationTextArea = new JTextArea();
        JScrollPane messageScroll = new JScrollPane(conversationTextArea);
        messageScroll.setBorder(new EmptyBorder(10, 0, 10, 0));

        JTextField messageTextField = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendButtonEventListener());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
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

    private void sendButtonEventListener() {

    }

    private void changeConversation(String conversationUser) {
        conversationTitle.setText(conversationUser);
    }

    private static boolean connectServer() {
        try {
            server = new Socket("localhost", 9999);
            Thread receiveServerMessagesThread = new Thread(() -> receiveServerMessages(server));
            receiveServerMessagesThread.start();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

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

    private static void receiveServerMessages(Socket server) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(server.getInputStream()));

            while (true) {
                String receivedMessage = bufferedReader.readLine();

                if (receivedMessage.contains("Command_CloseConnect")) {
                    System.out.println("Server closed!");
                    break;

                } else if (receivedMessage.contains("Command_AccountVerifySuccessful")) {
                    SignIn.status = SignIn.SignInStatus.Successful;

                } else if (receivedMessage.contains("Command_AccountVerifyFailed")) {
                    SignIn.status = SignIn.SignInStatus.Failed;

                } else if (receivedMessage.contains("Command_CreateAccountSuccessful")) {
                    SignUp.status = SignUp.SignUpStatus.Successful;

                } else if (receivedMessage.contains("Command_CreateAccountFailed")) {
                    SignUp.status = SignUp.SignUpStatus.Failed;

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