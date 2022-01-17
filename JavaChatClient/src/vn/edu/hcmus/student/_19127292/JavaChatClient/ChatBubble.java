package vn.edu.hcmus.student._19127292.JavaChatClient;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * vn.edu.hcmus.student._19127292.JavaChatClient
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 16-Jan-22 - 10:04
 * Description: Chat Bubble JPanel
 */
public class ChatBubble extends JPanel{
    public enum BubbleType {
        Mine,
        Others,
        File
    }

    public ChatBubble(BubbleType bubbleType, String content) {
        setBackground(Color.WHITE);

        JLabel timeLabel = new JLabel(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()));
        timeLabel.setFont(new Font("Arial", Font.ITALIC, 10));

        JButton contentButton = new JButton(content);
        contentButton.setBorderPainted(false);

        switch (bubbleType) {
            case Mine -> {
                contentButton.setBackground(Color.getHSBColor(0.6F, 1F, 1F));
                contentButton.setForeground(Color.WHITE);
                setLayout(new FlowLayout(FlowLayout.RIGHT));
                add(timeLabel);
                add(contentButton);
            }
            case Others -> {
                contentButton.setBackground(Color.getHSBColor(0F, 0F, 0.85F));
                setLayout(new FlowLayout(FlowLayout.LEFT));
                add(contentButton);
                add(timeLabel);
            }
            case File -> {
                contentButton.addActionListener(e -> downloadFile(content));
                contentButton.setBackground(Color.getHSBColor(0F, 0F, 0.85F));
                setLayout(new FlowLayout(FlowLayout.LEFT));
                add(contentButton);
                add(timeLabel);
            }
        }
    }

    private void downloadFile(String filename) {
        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                FileInputStream fileInputStream = new FileInputStream(filename);
                byte[] data = fileInputStream.readAllBytes();
                fileInputStream.close();

                FileOutputStream fileOutputStream = new FileOutputStream(
                        fileChooser.getSelectedFile().getAbsolutePath());
                fileOutputStream.write(data);
                fileOutputStream.close();

                File file = new File(filename);
                if (file.delete()) JOptionPane.showMessageDialog(this, "Downloaded!");
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this,
                        "Cannot download this file!\nError: " + exception,
                        "Download Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
