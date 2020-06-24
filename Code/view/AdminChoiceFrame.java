package view;

import model.Employee;

import javax.swing.*;
import java.awt.*;

public class AdminChoiceFrame extends JFrame {

    private JButton projectsButton = new JButton("Approve projects");
    private JButton employeesButton = new JButton("Edit employees");
    private JButton exitButton = new JButton("Exit");

    private Employee currentUser;

    public AdminChoiceFrame(Employee user) {
        currentUser = user;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 25, 25));
        setSize(350, 250);

        panel.add(projectsButton);
        panel.add(employeesButton);
        panel.add(exitButton);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);

        setListeners();
    }

    private void setListeners() {
        exitButton.addActionListener(e -> dispose());
        projectsButton.addActionListener(e -> goProjects());
        employeesButton.addActionListener(e -> goEmployees());
    }

    private void goProjects() {
        EventQueue.invokeLater(() -> new AdminProjectFrame(this, currentUser));
        setVisible(false);
    }
    private void goEmployees() {
        EventQueue.invokeLater(() -> new AdminFrame(this, currentUser));
        setVisible(false);
    }
}