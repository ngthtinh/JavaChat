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
        // Content Pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add components to content pane and Settings
        logsTableModel = new DefaultTableModel();

        logsTableModel.addColumn("No.");
        logsTableModel.addColumn("Time");
        logsTableModel.addColumn("IP : Port");
        logsTableModel.addColumn("Details");

        JScrollPane logsScrollPane = new JScrollPane(new JTable(logsTableModel));
        logsScrollPane.getVerticalScrollBar().addAdjustmentListener(e ->
                logsScrollPane.getVerticalScrollBar().setValue(logsScrollPane.getVerticalScrollBar().getMaximum()));

        contentPane.add(logsScrollPane);

        JButton testButton = new JButton("Test");
        testButton.addActionListener(e -> {
            String[] temp = {"a", "b", "c", "d"};
            logsTableModel.addRow(temp);
        });
        contentPane.add(testButton, BorderLayout.PAGE_END);

        setContentPane(contentPane);
        pack();
    }
}
