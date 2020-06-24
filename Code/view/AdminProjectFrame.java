package view;

import controller.Controller;
import model.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static controller.Controller.uiDateFormat;

public class AdminProjectFrame extends JFrame {
    private JPanel projectsPanel;

    private JTable projectTable;

    private Controller controller;

    private Employee currentUser;

    public AdminProjectFrame(JFrame parent, Employee user) {
        currentUser = user;

        controller = new Controller();
        setLayout(new BorderLayout(10, 10));
        setSize(800, 500);
        setTitle("Approving projects Window");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        add(mainPanel, BorderLayout.CENTER);

        projectsPanel = createProjectTablePanel();

        mainPanel.add(projectsPanel, BorderLayout.CENTER);

        setListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        fillProjectsTable();

        addWindowListener(new WindowAdapter() {  //return to the parent frame on close
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                parent.setVisible(true);
            }
        });
    }

    private JPanel createProjectTablePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Projects"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        String[] columnNames = {"id", "Description", "Approved"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        projectTable = new JTable(model);
        projectTable.setPreferredScrollableViewportSize(new Dimension(300, 350));
        projectTable.setFillsViewportHeight(true);
        projectTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        projectTable.getColumnModel().getColumn(1).setPreferredWidth(170);
        projectTable.getColumnModel().getColumn(2).setPreferredWidth(40);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(projectTable);

        panel.add(scrollPane);
        return panel;
    }

    private void fillProjectsTable() {
        DefaultTableModel model = (DefaultTableModel) projectTable.getModel();
        model.setRowCount(0);

        List<Project> projects = controller.getAllProjects();
        for (Project p : projects) {
            model.addRow(new Object[]{p.getId(), p.getDescription(), p.isApproved() ? "yes" : "not"});
        }
    }

    private void setListeners() {
        //projects
        projectTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (projectTable.getSelectedRow() >= 0) {
                    int id = Integer.parseInt(projectTable.getModel().getValueAt(projectTable.getSelectedRow(), 0).toString());
                    String description = projectTable.getModel().getValueAt(projectTable.getSelectedRow(), 1).toString();
                    boolean approved = projectTable.getModel().getValueAt(projectTable.getSelectedRow(), 2).toString()
                            .equalsIgnoreCase("yes");
                    if (!approved) {
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Approve the project " + description + "?");
                        if (confirm == JOptionPane.YES_OPTION) {
                            controller.approveProject(id);
                            fillProjectsTable();
                        }
                    }
                }
            }
        });
    }

}
