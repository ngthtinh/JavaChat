package vn.edu.hcmus.student._19127292.JavaChatClient;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.EmptyBorder;

/**
 * vn.edu.hcmus.student._19127292.JavaChatClient
 * Created by 19127292 - Nguyen Thanh Tinh
 * Date 18-Dec-21 - 21:19
 * Description: Sign Up Frame
 */
public class SignUp extends JFrame {
    public SignUp() {
        addComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Sign Up Now");
        setResizable(false);
        setVisible(true);
    }

    public void addComponents() {
        // Content Pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(6, 1, 0, 5));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Back to Sign In Button
        JButton backButton = new JButton("Back to Sign In");
        backButton.addActionListener(e -> backButtonEventHandler());

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        backPanel.add(backButton);

        // Title Label
        JLabel titleLabel = new JLabel("SIGN UP NOW", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        // Username Panel
        JTextField usernameTextField = new JTextField(15);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.add(new JLabel("Username"));
        usernamePanel.add(Box.createHorizontalStrut(40));
        usernamePanel.add(usernameTextField);

        // Password Panel
        JTextField passwordTextField = new JTextField(15);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.add(new JLabel("Password"));
        passwordPanel.add(Box.createHorizontalStrut(41));
        passwordPanel.add(passwordTextField);

        // Password Panel
        JTextField repasswordTextField = new JTextField(15);

        JPanel repasswordPanel = new JPanel();
        repasswordPanel.setLayout(new BoxLayout(repasswordPanel, BoxLayout.X_AXIS));
        repasswordPanel.add(new JLabel("Re-password"));
        repasswordPanel.add(Box.createHorizontalStrut(23));
        repasswordPanel.add(repasswordTextField);

        // Sign Up Now Button
        JButton signUpNowButton = new JButton("SIGN UP NOW");
        signUpNowButton.addActionListener(e -> signUpNowButtonEventHandler());

        // Add components to Content Pane and Settings
        contentPane.add(backPanel);
        contentPane.add(titleLabel);
        contentPane.add(usernamePanel);
        contentPane.add(passwordPanel);
        contentPane.add(repasswordPanel);
        contentPane.add(signUpNowButton);

        setContentPane(contentPane);
        pack();
    }

    void backButtonEventHandler() {
        new SignIn();
        dispose();
    }

    void signUpNowButtonEventHandler() {
        new Main();
        dispose();
    }
}