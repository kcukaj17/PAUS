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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static controller.Controller.uiDateFormat;

public class LeadFrame extends JFrame {
    private JPanel projectsPanel;
    private JPanel projectButtonPanel;
    private JPanel workersPanel;
    private JPanel usedTablePanel;
    private JPanel usedButtonPanel;

    private JTable projectTable;
    private JTable workersTable;
    private JTable usedTable;

    private JButton updateProjectButton = new JButton("Update Project");
    private JButton addProjectButton = new JButton("Add new Project");
    private JButton delProjectButton = new JButton("Delete Project");

    private JButton clearUsedButton = new JButton("Clear Used Materials Form");
    private JButton updateUsedButton = new JButton("Update Used Materials");
    private JButton addUsedButton = new JButton("Add Used Materials");
    private JButton delUsedButton = new JButton("Delete Used Materials");

    private JTextField usedDate = new JTextField();
    private JComboBox<Material> materialBox = new JComboBox<>();
    private JTextField usedAmount = new JTextField();

    private Project selectedProject;
    private UsedMaterial selectedUsed;
    private List<UsedMaterial> usedMaterials = new ArrayList<>();
    private List<Material> materials;

    private Controller controller;

    private Employee currentUser;

    public LeadFrame(Employee user) {
        currentUser = user;

        controller = new Controller();
        setLayout(new BorderLayout(10, 10));
        setSize(800, 500);
        setTitle("Team Lead Window");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        add(mainPanel, BorderLayout.CENTER);

        projectsPanel = createProjectTablePanel();
        workersPanel = createWorkersPanel();
        usedTablePanel = createUsedTablePanel();

        mainPanel.add(projectsPanel, BorderLayout.CENTER);
        mainPanel.add(workersPanel, BorderLayout.EAST);
        mainPanel.add(usedTablePanel, BorderLayout.SOUTH);

        setListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        fillProjectsTable();
        fillEmployeeTable();
        clearProjectSelection();
    }

    private JPanel createProjectTablePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Projects"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        String[] columnNames = {"id", "Description"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        projectTable = new JTable(model);
        projectTable.setPreferredScrollableViewportSize(new Dimension(200, 350));
        projectTable.setFillsViewportHeight(true);
        projectTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        projectTable.getColumnModel().getColumn(1).setPreferredWidth(170);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(projectTable);

        panel.add(scrollPane);
        projectButtonPanel = createProjectButtonPanel();
        panel.add(projectButtonPanel);

        return panel;
    }

    private void fillProjectsTable() {
        DefaultTableModel model = (DefaultTableModel) projectTable.getModel();
        model.setRowCount(0);

        java.util.List<Project> projects = controller.getAllProjects();
        for (Project p : projects) {
            model.addRow(new Object[]{p.getId(), p.getDescription()});
        }
    }

    private JPanel createProjectButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(updateProjectButton);
        panel.add(addProjectButton);
        panel.add(delProjectButton);

        for (int i = 0; i < 8; i++) {  //for spacing
            panel.add(new JLabel(""));
        }
        return panel;
    }

    private JPanel createWorkersPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Worker's List"));

        String[] columnNames = {"Id", "FirstName", "LastName", "projectId"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        workersTable = new JTable(model);
        workersTable.setPreferredScrollableViewportSize(new Dimension(330, 350));
        workersTable.setFillsViewportHeight(true);

        workersTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        workersTable.getColumnModel().getColumn(3).setPreferredWidth(70);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(workersTable);

        workersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (workersTable.getSelectedRow() >= 0) {
                    int employeeId = Integer.parseInt(workersTable.getModel().getValueAt(workersTable.getSelectedRow(), 0).toString());
                    int assignedProjectId = Integer.parseInt(workersTable.getModel().getValueAt(workersTable.getSelectedRow(), 3).toString());
                    if (assignedProjectId != selectedProject.getId()) {
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Assign the worker for this project?");
                        if (confirm == JOptionPane.YES_OPTION) {
                            controller.assignEmployeeForProject(employeeId, selectedProject.getId());
                            fillEmployeeTable();
                        }
                    }
                }
            }
        });

        panel.add(scrollPane);
        return panel;
    }

    private JPanel createUsedButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Date"));
        panel.add(usedDate);
        panel.add(new JLabel("Material"));
        panel.add(materialBox);
        panel.add(new JLabel("Amount"));
        panel.add(usedAmount);

        panel.add(clearUsedButton);
        panel.add(updateUsedButton);
        panel.add(addUsedButton);
        panel.add(delUsedButton);

        for (int i = 0; i < 4; i++) {  //for spacing
            panel.add(new JLabel(""));
        }
        return panel;
    }

    private JPanel createUsedTablePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Used Materials"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        UsedMaterialsTableModel model = new UsedMaterialsTableModel();
        usedTable = new JTable(model);
        usedTable.setPreferredScrollableViewportSize(new Dimension(500, 250));
        usedTable.setFillsViewportHeight(true);
        usedTable.getColumnModel().getColumn(1).setPreferredWidth(50);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(usedTable);

        panel.add(scrollPane);
        usedButtonPanel = createUsedButtonPanel();
        panel.add(usedButtonPanel);

        return panel;
    }

    class UsedMaterialsTableModel extends AbstractTableModel {
        String[] columnNames = {"Date", "Material Id", "Material Name", "Amount"};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (selectedProject == null) return 0;
            return controller.getMaterialsUsedInProject(selectedProject.getId()).size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (col == 0) return uiDateFormat.format(usedMaterials.get(row).getDateUsed());
            if (col == 1) return usedMaterials.get(row).getMaterialId();
            if (col == 2) {
                int matId = usedMaterials.get(row).getMaterialId();
                return controller.findMaterialById(matId).getName();
            }
            if (col == 3) return usedMaterials.get(row).getAmount();
            return null;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    private void setListeners() {
        //projects
        projectTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (projectTable.getSelectedRow() >= 0) {
                    String id = projectTable.getValueAt(projectTable.getSelectedRow(), 0).toString();
                    setSelectedProject(Integer.parseInt(id));
                }
            }
        });

        updateProjectButton.addActionListener(e -> updateProject());
        delProjectButton.addActionListener(e -> deleteProject());
        addProjectButton.addActionListener(e -> addProject());

        //used
        usedTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (usedTable.getSelectedRow() >= 0) {
                    int id = usedMaterials.get(usedTable.getSelectedRow()).getId();
                    setSelectedUsed(id);
                }
            }
        });

        clearUsedButton.addActionListener(e -> clearUsedSelection());
        updateUsedButton.addActionListener(e -> updateUsed());
        delUsedButton.addActionListener(e -> deleteUsed());
        addUsedButton.addActionListener(e -> addUsed());
    }

    private void setSelectedProject(int id) {
        selectedProject = controller.findProjectById(id);
        if (selectedProject == null) {
            fillProjectsTable();
            clearProjectSelection();
        } else {
            updateProjectButton.setEnabled(true);
            delProjectButton.setEnabled(true);

            fillEmployeeTable();
            fillUsedTable();
            addUsedButton.setEnabled(true);
        }
    }

    private void setSelectedUsed(int id) {
        selectedUsed = controller.findUsedMaterialById(id);
        if (selectedUsed == null) {
            fillUsedTable();
            clearUsedSelection();
        } else {
            updateUsedButton.setEnabled(true);
            delUsedButton.setEnabled(true);

            usedDate.setText(uiDateFormat.format(selectedUsed.getDateUsed()));
            usedAmount.setText(selectedUsed.getAmount() + "");

            //find material by id in the combobox
            for (Material material: materials) {
                if (material.getId() == selectedUsed.getMaterialId()) {
                    materialBox.setSelectedItem(material);
                    break;
                }
            }

            addUsedButton.setEnabled(false);
        }
    }

    private void clearProjectSelection() {
        projectTable.clearSelection();
        selectedProject = null;

        updateProjectButton.setEnabled(false);
        addProjectButton.setEnabled(true);
        delProjectButton.setEnabled(false);

        clearUsedSelection();
        clearUsedPanel();
    }

    private void clearUsedPanel() {
        clearUsedSelection();
        addUsedButton.setEnabled(false);
        usedMaterials.clear();
        usedTable.repaint();
    }

    private void clearUsedSelection() {
        usedTable.clearSelection();
        selectedUsed = null;

        usedDate.setText("");
        usedAmount.setText("");

        clearUsedButton.setEnabled(false);
        updateUsedButton.setEnabled(false);
        addUsedButton.setEnabled(true);
        delUsedButton.setEnabled(false);
    }


    ///buttons actions for projects
    private void updateProject() {
        if (selectedProject == null) return;

        String input = JOptionPane.showInputDialog(null, "Enter new description:");
        if (input == null || input.length() == 0) return;

        selectedProject.setDescription(input);

        if (controller.updateProject(selectedProject) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillProjectsTable();
        clearProjectSelection();
    }

    private void addProject() {

        String input = JOptionPane.showInputDialog(null, "Enter description:");
        if (input == null || input.length() == 0) return;

        Project p = new Project(input, false);
        if (controller.createProject(p) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillProjectsTable();
        clearProjectSelection();
    }

    private void deleteProject() {
        if (selectedProject == null) return;

        List<UsedMaterial> materialsUsed = controller.getMaterialsUsedInProject(selectedProject.getId());
        if (materialsUsed.size() > 0) {
            JOptionPane.showMessageDialog(null, "The project can not be deleted. There are materials used in it.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(null,
                "Unassign all the workers off this project ?");
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        controller.unAssignEmployeesFromProject(selectedProject.getId());

        if (controller.deleteProject(selectedProject) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillProjectsTable();
        fillEmployeeTable();
        clearProjectSelection();
    }

    //populating tables
    private void fillEmployeeTable() {
        DefaultTableModel model = (DefaultTableModel) workersTable.getModel();
        model.setRowCount(0);

        List<Employee> workers = controller.getAllEmployees();
        for (Employee e : workers) {
            model.addRow(new Object[]{e.getId(), e.getFirstname(), e.getLastname(), e.getProjectId()});
        }
    }

    private void fillUsedTable() {
        usedMaterials.clear();
        if (selectedProject != null) {
            usedMaterials.addAll(controller.getMaterialsUsedInProject(selectedProject.getId()));
        }
        usedTable.repaint();

        updateMaterialsBox();
    }

    private void updateMaterialsBox() {
        materialBox.removeAllItems();
        materials = controller.getAllMaterials();

        for (Material material: materials) {
            materialBox.addItem(material);
        }
    }

    ///buttons actions for used materials
    private boolean checkFieldsUsed() {
        if (usedDate.getText().length() == 0
                || usedAmount.getText().length() == 0
                || materialBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Fill all the fields");
            return false;
        }

        try {
            double amount = Double.parseDouble(usedAmount.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null, "Amount must be positive");
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Bad number in salary field");
            return false;
        }

        Date date;
        try {
            date = uiDateFormat.parse(usedDate.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Bad date format");
            return false;
        }
        return true;
    }

    private void  updateUsed() {
        if (selectedUsed == null) return;

        if (!checkFieldsUsed()) return;
        Material material = (Material) materialBox.getSelectedItem();

        Date date;
        try {
            date = uiDateFormat.parse(usedDate.getText());
        } catch (Exception ex) {
            return;
        }
        double amount = Double.parseDouble(usedAmount.getText());

        selectedUsed.setAmount(amount);
        selectedUsed.setMaterialId(material.getId());
        selectedUsed.setDateUsed(date);

        if (controller.updateUsedMaterial(selectedUsed) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillUsedTable();
        clearUsedSelection();
    }

    private void deleteUsed() {
        if (selectedUsed == null) return;

        if (controller.deleteUsedMaterial(selectedUsed) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillUsedTable();
        clearUsedSelection();
    }

    private void addUsed() {
        if (selectedUsed != null) return;

        if (!checkFieldsUsed()) return;
        Material material = (Material) materialBox.getSelectedItem();

        Date date;
        try {
            date = uiDateFormat.parse(usedDate.getText());
        } catch (Exception ex) {
            return;
        }
        double amount = Double.parseDouble(usedAmount.getText());

        UsedMaterial usedMaterial = new UsedMaterial(material.getId(), selectedProject.getId(), amount, date);

        if (controller.createUsedMaterial(usedMaterial) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillUsedTable();
        clearUsedSelection();
    }

}
