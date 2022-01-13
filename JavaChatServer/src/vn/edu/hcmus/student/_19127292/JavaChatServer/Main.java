package vn.edu.hcmus.student._19127292.JavaChatServer;

import javax.swing.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.io.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * vn.edu.hcmus.student._19127292.JavaChatServer
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 16-Dec-21 - 23:29
 * Description: Main Class
 */
public class Main extends JFrame {
    private HashMap<Socket, String> users;
    private HashMap<String, String> accounts;
    private DefaultTableModel logsTableModel;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        addComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Java Chat Server");
        setVisible(true);

        loadAccounts();

        Thread waitClients = new Thread(this::waitClients);
        waitClients.start();
    }

    public void addComponents() {
        // Logs Table
        logsTableModel = new DefaultTableModel();
        logsTableModel.addColumn("No.");
        logsTableModel.addColumn("Time");
        logsTableModel.addColumn("IP");
        logsTableModel.addColumn("Port");
        logsTableModel.addColumn("Details");

        JTable logsTable = new JTable(logsTableModel);
        logsTable.getColumnModel().getColumn(0).setMinWidth(50);
        logsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        logsTable.getColumnModel().getColumn(1).setMinWidth(200);
        logsTable.getColumnModel().getColumn(1).setMaxWidth(200);
        logsTable.getColumnModel().getColumn(2).setMinWidth(75);
        logsTable.getColumnModel().getColumn(2).setMaxWidth(75);
        logsTable.getColumnModel().getColumn(3).setMinWidth(50);
        logsTable.getColumnModel().getColumn(3).setMaxWidth(50);
        logsTable.getColumnModel().getColumn(4).setMinWidth(400);

        JScrollPane logsScrollPane = new JScrollPane(logsTable);
        logsScrollPane.setPreferredSize(new Dimension(800, 600));
        logsScrollPane.getVerticalScrollBar().addAdjustmentListener(e ->
                logsScrollPane.getVerticalScrollBar().setValue(logsScrollPane.getVerticalScrollBar().getMaximum()));

        // Content Pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(logsScrollPane);

        setContentPane(contentPane);
        pack();
    }

    public void addLogs(Object details) {
        Object[] rowObjects = {logsTableModel.getRowCount() + 1, new Date(), "", "", details};
        logsTableModel.addRow(rowObjects);
    }

    public void addLogs(Socket client, Object details) {
        Object[] rowObjects = {logsTableModel.getRowCount() + 1, new Date(),
                client.getLocalAddress(), client.getPort(), details};
        logsTableModel.addRow(rowObjects);
    }

    @SuppressWarnings("unchecked")
    public void loadAccounts() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("Accounts.DAT"));
            accounts = (HashMap<String, String>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception exception) {
            accounts = new HashMap<>();
        }
    }

    public void saveAccounts() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Accounts.DAT"));
            objectOutputStream.writeObject(accounts);
            objectOutputStream.close();
        } catch (Exception exception) {
            assert true;
        }
    }

    public void addUser(Socket socket, String username) {
        users.put(socket, username);
        sendUserList();
    }

    public void removeUser(Socket socket) {
        users.remove(socket);
        sendUserList();
    }

    public void sendUserList() {
        for (Socket socket : users.keySet()) {
            StringBuilder userList = new StringBuilder("Command_UserList");
            for (String username : users.values())
                if (!username.equals(users.get(socket)))
                    userList.append("`").append(username);
            sendMessage(socket, userList.toString());
        }
    }

    private void waitClients() {
        try {
            users = new HashMap<>();
            ServerSocket serverSocket = new ServerSocket(9999);

            while (true) {
                Socket client = serverSocket.accept();
                if (client == null) break;

                addLogs(client, "Client connected");

                Thread receiveClientMessage = new Thread(() -> receiveClientMessages(client));
                receiveClientMessage.start();
            }
        } catch(Exception exception) {
            addLogs("Failed to connect client");
        }
    }

    public void sendMessage(Socket socket, String message) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception exception) {
            addLogs("Client disconnected");
            removeUser(socket);
        }
    }

    private void receiveClientMessages(Socket client) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (true) {
                String receivedMessage = bufferedReader.readLine();

                if (receivedMessage.contains("Command_CloseConnect")) {
                    addLogs(client, "Client has left!");
                    break;

                } else if (receivedMessage.contains("Command_SignedIn")) {
                    String[] str = receivedMessage.split("`");
                    addUser(client, str[1]);
                    addLogs(client, "Client has signed in with username is " + str[1]);

                } else if (receivedMessage.contains("Command_AccountVerify")) {
                    String[] str = receivedMessage.split("`");
                    String query = accounts.get(str[1]);
                    if (query == null) {
                        sendMessage(client, "Command_AccountVerifyFailed");
                    } else if (query.equals(str[2])) {
                        if (users.containsValue(str[1])) sendMessage(client, "Command_AccountVerifyAlready");
                        else sendMessage(client, "Command_AccountVerifySuccessful");
                    } else {
                        sendMessage(client, "Command_AccountVerifyFailed");
                    }

                } else if (receivedMessage.contains("Command_CreateAccount")) {
                    String[] str = receivedMessage.split("`");
                    String query = accounts.get(str[1]);
                    if (query == null) {
                        accounts.put(str[1], str[2]);
                        saveAccounts();
                        sendMessage(client, "Command_CreateAccountSuccessful");
                    } else {
                        sendMessage(client, "Command_CreateAccountFailed");
                    }

                } else {
                    addLogs(client, receivedMessage);
                }
            }

            bufferedReader.close();
            removeUser(client);
            client.close();
        } catch (Exception exception) {
            addLogs(client, "Client disconnected");
            removeUser(client);
        }
    }
}