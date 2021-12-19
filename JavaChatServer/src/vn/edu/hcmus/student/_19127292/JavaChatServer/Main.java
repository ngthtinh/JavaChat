package vn.edu.hcmus.student._19127292.JavaChatServer;

import javax.swing.*;
import java.awt.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * vn.edu.hcmus.student._19127292.JavaChatServer
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 16-Dec-21 - 23:29
 * Description: Main Class
 */
public class Main extends JFrame {
    private DefaultTableModel logsTableModel;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        addComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Java Chat Server");
        setVisible(true);

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

    public void addLogs(Object localAdress, Object port, Object details) {
        Object[] rowObjects = {logsTableModel.getRowCount() + 1, new Date(), localAdress, port, details};
        logsTableModel.addRow(rowObjects);
    }

    public void waitClients() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);

            while (true) {
                Socket client = serverSocket.accept();
                if (client == null) break;

                Thread receiveClientMessage = new Thread(() -> receiveClientMessages(client));
                receiveClientMessage.start();

                addLogs(client.getLocalAddress(), client.getPort(), "Client connected");
            }
        } catch(Exception exception) {
            addLogs(null, null, exception);
        }
    }

    public void receiveClientMessages(Socket client) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            while (true) {
                String receivedMessage = bufferedReader.readLine();
                addLogs(client.getLocalAddress(), client.getPort(), receivedMessage);

                if (receivedMessage.contains("Close")) {
                    addLogs(client.getLocalAddress(), client.getPort(), "Client has left!");
                    break;
                }
                // Other commands here
            }

            bufferedReader.close();
            bufferedWriter.close();
            client.close();
        } catch (Exception exception) {
            addLogs(client.getLocalAddress(), client.getPort(), "Client has left!");
        }
    }
}