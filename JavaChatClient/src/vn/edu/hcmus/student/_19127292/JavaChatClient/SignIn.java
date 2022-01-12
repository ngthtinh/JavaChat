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
    public enum SignInStatus {
        Waiting,
        Failed,
        Successful
    }

    public static SignInStatus status;

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

        // Title Label
        JLabel titleLabel = new JLabel("JAVA CHAT APP", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

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
        passwordPanel.add(Box.createHorizontalStrut(11));
        passwordPanel.add(passwordTextField);

        // Sign in Button
        JButton signInButton = new JButton("SIGN IN");
        signInButton.addActionListener(e ->
                signInButtonEventHandler(usernameTextField.getText(), passwordTextField.getText()));

        // Sign up now Button
        JButton signUpNowButton = new JButton("SIGN UP NOW");
        signUpNowButton.addActionListener(e -> signUpNowButtonEventHandler());

        // Add components to Content Pane and Settings
        contentPane.add(titleLabel);
        contentPane.add(usernamePanel);
        contentPane.add(passwordPanel);
        contentPane.add(signInButton);
        contentPane.add(new JLabel("or", JLabel.CENTER));
        contentPane.add(signUpNowButton);

        setContentPane(contentPane);
        pack();
    }

    void signInButtonEventHandler(String username, String password) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!",
                    "Sign In Failed", JOptionPane.WARNING_MESSAGE);
        }
        else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty!",
                    "Sign In Failed", JOptionPane.WARNING_MESSAGE);
        } else {
            status = SignInStatus.Waiting;

            Main.sendMessage("Command_AccountVerify`" + username + "`" + password);
            while (status == SignInStatus.Waiting) System.out.print("");

            if (status == SignInStatus.Successful) {
                Main.sendMessage("Command_SignedIn`" + username);
                new Main();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong login information.",
                        "Sign In Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void signUpNowButtonEventHandler() {
        new SignUp();
        dispose();
    }
}
