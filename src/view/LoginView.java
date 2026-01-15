package view;

import controller.RecipeController;
import model.RecipeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class LoginView {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginView() {
        createLoginWindow();
    }

    private void createLoginWindow() {
        frame = new JFrame("Login - RecipeHub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        JButton exitBtn = new JButton("Exit");

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterView(frame).show();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        buttonPanel.add(exitBtn);

        frame.add(formPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Email and password are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(email) && parts[1].equals(password)) {
                    found = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet → no users registered
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading users file!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (found) {
            JOptionPane.showMessageDialog(frame, "Login successful!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose(); // Close login window

            // Start the main app
            RecipeModel model = new RecipeModel();
            RecipeController controller = new RecipeController(model);
            controller.start();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid email or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView());
    }
}