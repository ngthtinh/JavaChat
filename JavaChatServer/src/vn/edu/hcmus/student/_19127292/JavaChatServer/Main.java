package vn.edu.hcmus.student._19127292.JavaChatServer;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * vn.edu.hcmus.student._19127292.JavaChatServer
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 16-Dec-21 - 23:29
 * Description: Main Class
 */
public class Main extends JFrame{
    DefaultTableModel logsTableModel;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        addComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Java Chat Server");
        setVisible(true);
    }

    public void addComponents() {
        // Logs Table
        logsTableModel = new DefaultTableModel();

        logsTableModel.addColumn("No.");
        logsTableModel.addColumn("Time");
        logsTableModel.addColumn("IP : Port");
        logsTableModel.addColumn("Details");

        JScrollPane logsScrollPane = new JScrollPane(new JTable(logsTableModel));
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
}