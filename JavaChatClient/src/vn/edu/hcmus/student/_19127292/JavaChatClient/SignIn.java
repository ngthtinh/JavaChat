package vn.edu.hcmus.student._19127292.JavaChatClient;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.EmptyBorder;

/**
 * vn.edu.hcmus.student._19127292.JavaChatClient
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 17-Dec-21 - 18:50
 * Description: Sign In Frame
 */
public class SignIn extends JFrame {
    public SignIn() {
        addComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Sign In");
        setResizable(false);
        setVisible(true);
    }

    public void addComponents() {
        // Content Pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(6, 1, 0, 5));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Username Panel
        JTextField usernameTextField = new JTextField(15);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.add(new JLabel("Username"));
        usernamePanel.add(Box.createHorizontalStrut(10));
        usernamePanel.add(usernameTextField);

        // Password Panel
        JTextField passwordTextField = new JTextField(15);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.add(new JLabel("Password"));
        passwordPanel.add(Box.createHorizontalStrut(10));
        passwordPanel.add(passwordTextField);

        // Sign in Button
        JButton signInButton = new JButton("SIGN IN");
        signInButton.addActionListener(e -> {});

        // Sign up now Button
        JButton signUpNowButton = new JButton("SIGN UP NOW");
        signUpNowButton.addActionListener(e -> {});

        // Add components to Content Pane and Settings
        contentPane.add(new JLabel("JAVA CHAT APP", JLabel.CENTER));
        contentPane.add(usernamePanel);
        contentPane.add(passwordPanel);
        contentPane.add(signInButton);
        contentPane.add(new JLabel("or", JLabel.CENTER));
        contentPane.add(signUpNowButton);

        setContentPane(contentPane);
        pack();
    }
}
