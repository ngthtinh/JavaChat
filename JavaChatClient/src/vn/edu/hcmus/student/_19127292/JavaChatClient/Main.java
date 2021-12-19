package vn.edu.hcmus.student._19127292.JavaChatClient;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

import javax.swing.border.EmptyBorder;

/**
 * vn.edu.hcmus.student._19127292.JavaChatClient
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 16-Dec-21 - 23:26
 * Description: Main Class
 */
public class Main extends JFrame {
    JLabel conversationTitle;

    public static void main(String[] args) {
        // new SignIn();

        try
        {
            Socket s = new Socket("localhost",9999);
            System.out.println(s.getPort());

            InputStream is=s.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            OutputStream os=s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            String sentMessage="";
            String receivedMessage;

            System.out.println("Talking to Server");

            do
            {
                DataInputStream din=new DataInputStream(System.in);
                sentMessage=din.readLine();
                bw.write(sentMessage);
                bw.newLine();
                bw.flush();

                if (sentMessage.equalsIgnoreCase("quit"))
                    break;
                else
                {
                    receivedMessage=br.readLine();
                    System.out.println("Received : " + receivedMessage);
                }

            }
            while(true);

            bw.close();
            br.close();
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }
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

        setContentPane(contentPane);
        pack();
    }

    void sendButtonEventListener() {

    }

    void changeConversation(String conversationUser) {
        conversationTitle.setText(conversationUser);
    }
}