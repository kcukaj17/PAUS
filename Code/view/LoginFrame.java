package view;

import controller.Controller;
import model.Database;
import model.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class LoginFrame extends JFrame {

    private JTextField loginField = new JTextField();
    private JPasswordField passField = new JPasswordField();
    private JButton loginButton = new JButton("Login");
    private JButton exitButton = new JButton("Exit");

    private Controller controller;

    public LoginFrame() {
        setLayout(new BorderLayout(30, 30));
        setSize(350, 250);
        GridLayout layout = new GridLayout(3, 2, 20, 20);
        JPanel panel = new JPanel(layout);

        addControls(panel);
        add(panel, BorderLayout.CENTER);
        JLabel hello = new JLabel("Enter login and password", SwingConstants.CENTER);
        hello.setSize(300, 30);
        add(hello, BorderLayout.NORTH);
        add(new JLabel(""), BorderLayout.SOUTH);
        add(new JLabel(""), BorderLayout.EAST);
        add(new JLabel(""), BorderLayout.WEST);

        setLocationRelativeTo(null);
        setVisible(true);

        setListeners();

        controller = new Controller();
    }

    private void addControls(JPanel panel) {
        panel.add(new JLabel("Login:"));
        panel.add(loginField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(loginButton);
        panel.add(exitButton);

        exitButton.setSize(100, 30);
        loginButton.setSize(100, 30);
    }

    private void setListeners() {
        exitButton.addActionListener(e -> closeFrame());

        loginButton.addActionListener(new LoginAction());
    }

    private void closeFrame() {
        dispose();
    }

    private boolean checkAll() {
        boolean dbExists = false;
        try {
            dbExists = Database.checkDatabase();
        } catch (Exception ex) {
        }
        if (!dbExists) {
            JOptionPane.showMessageDialog(null, "There is no database connection, create database and grant all privileges");
            return false;
        }

        boolean userTableExists = false;
        try {
            if (controller.checkUsersTable()) {
                userTableExists = true;
            } else {
                int reply = JOptionPane.showConfirmDialog(null, "There is no Employee table. Create all the tables?");
                if (reply == JOptionPane.YES_OPTION) {
                    controller.createTables();
                    userTableExists = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!userTableExists) {
            JOptionPane.showMessageDialog(null, " Employee table does not exist. The work is impossible.");
            return false;
        }

        boolean userTableFilled = false;
        if (controller.getAllEmployees().size() == 0) {
            int reply = JOptionPane.showConfirmDialog(null, "The Employee table is empty. Fill all the database with sample data?");
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    controller.sampleFill();
                    userTableFilled = true;
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        } else {
            userTableFilled = true;
        }
        if (!userTableFilled) {
            JOptionPane.showMessageDialog(null, " Employee table is empty.");
            return false;
        }
        return true;
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!checkAll()) return;

            String login = loginField.getText();
            char[] pass = passField.getPassword();
            if (login.length() == 0 || pass.length == 0) {
                JOptionPane.showMessageDialog(null, "Enter login and password fields");
                return;
            }
            Employee employee = controller.findByLogin(login);
            if (employee == null || (employee.getPassword() != null && !employee.getPassword().equals(new String(pass)))) {
                JOptionPane.showMessageDialog(null, "Wrong login or password");
                return;
            }
            if (employee.getPassword() == null || employee.getPassword().length() == 0) {
                JOptionPane.showMessageDialog(null, "You are not signed up in the system");
                return;
            }
            switch (employee.getAuthority()) {
                case ADMIN:
                    EventQueue.invokeLater(() -> new AdminChoiceFrame(employee));
                    break;
                case LEAD:
                    EventQueue.invokeLater(() -> new LeadFrame(employee));
                    break;
                case ECONOMIST:
                    EventQueue.invokeLater(() -> new EconomistFrame(employee));
                    break;
                case MANAGER:
                    EventQueue.invokeLater(() -> new ManagerFrame(employee));
                    break;
                case WORKER:
                    JOptionPane.showMessageDialog(null, "Welcome to the system, " + employee.getFirstname());
                    break;
            }
            loginField.setText("");
            passField.setText("");
        }
    }
}