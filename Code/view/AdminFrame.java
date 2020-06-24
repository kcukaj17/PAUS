package view;

import controller.Controller;
import model.Authority;
import model.DaysOff;
import model.Employee;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static controller.Controller.uiDateFormat;

public class AdminFrame extends JFrame {

    private JPanel tablePanel;
    private JPanel employeePanel;
    private JPanel daysPanel;

    private JTable employeeTable;
    private JTable daysTable;
    private JLabel eId = new JLabel("");
    private JTextField eFirst = new JTextField();
    private JTextField eLast = new JTextField();
    private JTextField eLogin = new JTextField();
    private JPasswordField ePass1 = new JPasswordField();
    private JPasswordField ePass2 = new JPasswordField();
    private JTextField ePhone = new JTextField();
    private JTextField eSalary = new JTextField();
    private JTextField eAuth = new JTextField();
    private JTextField ePId = new JTextField();

    private JButton clearButton = new JButton("Clear Form");
    private JButton updateButton = new JButton("Update Employee");
    private JButton addButton = new JButton("Add new Employee");
    private JButton delButton = new JButton("Delete Employee");


    private Controller controller;
    private Employee selectedEmployee;

    private Employee currentUser;

    public AdminFrame(JFrame parent, Employee user) {
        currentUser = user;

        controller = new Controller();
        setLayout(new BorderLayout(10, 10));
        setSize(1000, 400);
        setTitle("Admin Window");

        //GridLayout layout = new GridLayout(1, 3, 10, 10);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        add(mainPanel, BorderLayout.CENTER);

        tablePanel = createTablePanel();
        employeePanel = createEmployeePanel();
        daysPanel = createDaysPanel();

        mainPanel.add(tablePanel, BorderLayout.WEST);
        mainPanel.add(employeePanel, BorderLayout.CENTER);
        mainPanel.add(daysPanel, BorderLayout.EAST);

        setListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        fillEmployeesTable();
        clearEmployeeSelection();

        addWindowListener(new WindowAdapter() {  //return to the parent frame on close
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                parent.setVisible(true);
            }
        });
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Employees"));

        String[] columnNames = {"id", "First Name", "Last Name", "Login", "Phone", "Salary", "Authority", "Project Id"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(model);
        employeeTable.setPreferredScrollableViewportSize(new Dimension(550, 450));
        employeeTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(employeeTable);

        panel.add(scrollPane);
        return panel;
    }

    private void fillEmployeesTable() {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        model.setRowCount(0);

        List<Employee> employees = controller.getAllEmployees();
        for (Employee e : employees) {
            model.addRow(new Object[]{e.getId(), e.getFirstname(), e.getLastname(), e.getLogin(),
                    e.getPhone(), e.getSalary(), e.getAuthority(), e.getProjectId()});
        }
    }

    private JPanel createEmployeePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Selected Employee"));

        panel.add(new JLabel("Id"));
        panel.add(eId);
        panel.add(new JLabel("First Name"));
        panel.add(eFirst);
        panel.add(new JLabel("Last Name"));
        panel.add(eLast);
        panel.add(new JLabel("Login"));
        panel.add(eLogin);
        panel.add(new JLabel("Password"));
        panel.add(ePass1);
        panel.add(new JLabel("Repeat password"));
        panel.add(ePass2);
        panel.add(new JLabel("Phone"));
        panel.add(ePhone);
        panel.add(new JLabel("Salary"));
        panel.add(eSalary);
        panel.add(new JLabel("Authority"));
        panel.add(eAuth);
        panel.add(new JLabel("Project Id"));
        panel.add(ePId);
        panel.add(clearButton);
        panel.add(updateButton);
        panel.add(addButton);
        panel.add(delButton);

        return panel;
    }

    private JPanel createDaysPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Days Off For Approving"));

        String[] columnNames = {"Date", "Approved", "id"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        daysTable = new JTable(model);
        daysTable.setPreferredScrollableViewportSize(new Dimension(150, 450));
        daysTable.setFillsViewportHeight(true);
        daysTable.getColumnModel().getColumn(0).setPreferredWidth(80);

        //hide id column
        TableColumnModel tcm = daysTable.getColumnModel();
        tcm.removeColumn(tcm.getColumn(2));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(daysTable);

        daysTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (daysTable.getSelectedRow() >= 0) {
                    String date = daysTable.getModel().getValueAt(daysTable.getSelectedRow(), 0).toString();
                    boolean approved = daysTable.getModel().getValueAt(daysTable.getSelectedRow(), 1).toString()
                            .equalsIgnoreCase("yes");
                    int id = Integer.parseInt(daysTable.getModel().getValueAt(daysTable.getSelectedRow(), 2).toString());
                    if (!approved) {
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Approve the day off for " + selectedEmployee.getFirstname() + " for " + date + "?");
                        if (confirm == JOptionPane.YES_OPTION) {
                            controller.approveDayOffById(id);
                            fillDaysOffTable(selectedEmployee);
                        }
                    }
                }
            }
        });
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension(170, 450));
        return panel;
    }

    private void setListeners() {
        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (employeeTable.getSelectedRow() >= 0) {
                    String id = employeeTable.getValueAt(employeeTable.getSelectedRow(), 0).toString();
                    setSelectedEmployee(Integer.parseInt(id));
                }
            }
        });

        clearButton.addActionListener(e -> clearEmployeeSelection());

        updateButton.addActionListener(e -> updateEmployee());

        delButton.addActionListener(e -> deleteEmployee());

        addButton.addActionListener(e -> addEmployee());

    }

    private void setSelectedEmployee(int id) {
        Employee e = controller.findEmployeeById(id);
        if (e == null) {
            fillEmployeesTable();
            clearEmployeeSelection();
        } else {
            selectedEmployee = e;
            eId.setText(e.getId() + "");
            eFirst.setText(e.getFirstname());
            eLast.setText(e.getLastname());
            eLogin.setText(e.getLogin());
            ePass1.setText(e.getPassword());
            ePass2.setText(e.getPassword());
            ePhone.setText(e.getPhone());
            eSalary.setText(e.getSalary() + "");
            eAuth.setText(e.getAuthority().toString());
            ePId.setText(e.getProjectId() + "");

            clearButton.setEnabled(true);
            updateButton.setEnabled(true);
            addButton.setEnabled(false);
            delButton.setEnabled(true);

            fillDaysOffTable(e);
        }
    }

    private void clearEmployeeSelection() {
        employeeTable.clearSelection();
        selectedEmployee = null;

        eId.setText("");
        eFirst.setText("");
        eLast.setText("");
        eLogin.setText("");
        ePass1.setText("");
        ePass2.setText("");
        ePhone.setText("");
        eSalary.setText("");
        eAuth.setText("");
        ePId.setText("");

        clearButton.setEnabled(false);
        updateButton.setEnabled(false);
        addButton.setEnabled(true);
        delButton.setEnabled(false);

        ((DefaultTableModel) daysTable.getModel()).setRowCount(0);
    }

    private void updateEmployee() {
        if (selectedEmployee == null) return;

        if (!checkFields()) return;
        String login = eLogin.getText();
        Employee other = controller.findByLogin(login);
        if (other != null && other.getId() != selectedEmployee.getId()) {
            JOptionPane.showMessageDialog(null, "This login already exists");
            return;
        }
        selectedEmployee.setFirstname(eFirst.getText());
        selectedEmployee.setLastname(eLast.getText());
        selectedEmployee.setLogin(eLogin.getText());
        selectedEmployee.setPassword(new String(ePass1.getPassword()));
        selectedEmployee.setPhone(ePhone.getText());
        selectedEmployee.setSalary(Double.parseDouble(eSalary.getText()));
        selectedEmployee.setAuthority(Authority.fromString(eAuth.getText()));
        selectedEmployee.setProjectId(Integer.parseInt(ePId.getText()));

        if (controller.updateEmployee(selectedEmployee) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillEmployeesTable();
        clearEmployeeSelection();
    }

    private void addEmployee() {
        if (selectedEmployee != null) return;

        if (!checkFields()) return;
        String login = eLogin.getText();
        Employee other = controller.findByLogin(login);
        if (other != null) {
            JOptionPane.showMessageDialog(null, "This login already exists");
            return;
        }
        Employee e = new Employee(eFirst.getText(), eLast.getText(), eLogin.getText(), new String(ePass1.getPassword()),
                ePhone.getText(), Double.parseDouble(eSalary.getText()), Authority.fromString(eAuth.getText()), Integer.parseInt(ePId.getText()));

        if (controller.createEmployee(e) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillEmployeesTable();
        clearEmployeeSelection();
    }


    private void deleteEmployee() {
        if (selectedEmployee == null) return;

        controller.deleteDaysOffOfEmployee(selectedEmployee.getId());
        controller.deleteHoursOfEmployee(selectedEmployee.getId());

        if (controller.deleteEmployee(selectedEmployee) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillEmployeesTable();
        clearEmployeeSelection();
    }

    private boolean checkFields() {
        if (eFirst.getText().length() == 0
                || eLast.getText().length() == 0
                || eLogin.getText().length() == 0
                || ePass1.getPassword().length == 0
                || ePass2.getPassword().length == 0
                || ePhone.getText().length() == 0
                || eSalary.getText().length() == 0
                || eAuth.getText().length() == 0
                || ePId.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Fill all the fields");
            return false;
        }
        if (ePass1.getPassword().length != ePass2.getPassword().length
                || !equalPasses()) {
            JOptionPane.showMessageDialog(null, "Passwords mismatch");
            return false;
        }
        if (Authority.fromString(eAuth.getText()) == null) {
            JOptionPane.showMessageDialog(null, "Bad authority field");
            return false;
        }
        try {
            Double.parseDouble(eSalary.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Bad number in salary field");
            return false;
        }
        int pid;
        try {
            pid = Integer.parseInt(ePId.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Bad number in project field");
            return false;
        }
        if (pid < 0 || pid > 0 && controller.findProjectById(pid) == null) {
            JOptionPane.showMessageDialog(null, "Bad project id number in project field");
            return false;
        }
        return true;
    }

    private boolean equalPasses() {
        for (int i = 0; i < ePass1.getPassword().length; i++) {
            if (ePass1.getPassword()[i] != ePass2.getPassword()[i]) {
                return false;
            }
        }
        return true;
    }

    private void fillDaysOffTable(Employee e) {
        DefaultTableModel model = (DefaultTableModel) daysTable.getModel();
        model.setRowCount(0);

        //id, date, employeeId, approved

        List<DaysOff> days = controller.getAllDaysOffOfEmployee(e.getId());
        for (DaysOff d : days) {
            model.addRow(new Object[]{uiDateFormat.format(d.getDateOff()),
                    d.isApproved() ? "yes" : "not", d.getId()});
        }
    }

}