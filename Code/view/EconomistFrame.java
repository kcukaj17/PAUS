package view;

import controller.Controller;
import model.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static controller.Controller.uiDateFormat;

public class EconomistFrame extends JFrame {
    private JTable employeeTable;
    private JTable materialsTable;

    private JButton calcSalaryButton = new JButton("Calculate salary for period");
    private JButton enterHoursButton = new JButton("Set working hours");
    private JButton changeDateButton = new JButton("Change Date");
    private JButton updateMaterialsButton = new JButton("Update used materials table for new dates");

    private JButton sendRequestButton = new JButton("Send request");

    private Employee selectedEmployee;
    private JLabel dateHoursLabel = new JLabel("Current date for entering hours: " + uiDateFormat.format(new Date()));
    private Date dateForHours = new Date();

    private JTextField date1Field = new JTextField("");
    private JTextField date2Field = new JTextField(uiDateFormat.format(new Date()));

    private List<Employee> employees = new ArrayList<>();
    private List<UsedMaterial> usedMaterials = new ArrayList<>();

    private Controller controller;

    private Employee currentUser;

    public EconomistFrame(Employee user) {
        currentUser = user;

        controller = new Controller();
        setLayout(new BorderLayout(10, 10));
        setSize(700, 700);
        setTitle("Economist Window");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.add(createEmployeeTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createPeriodPanel(), BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        add(createMaterialsPanel(), BorderLayout.SOUTH);

        setListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        fillEmployeesTable();
        clearEmployeeSelection();

        fillMaterialsTable();
    }

    private JPanel createEmployeeTablePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Employees"));
        panel.setLayout(new BorderLayout(10, 10));

        EmployeeHoursModel model = new EmployeeHoursModel();
        employeeTable = new JTable(model);
        employeeTable.setPreferredScrollableViewportSize(new Dimension(550, 200));
        employeeTable.setFillsViewportHeight(true);
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(50);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(employeeTable);

        panel.add(BorderLayout.CENTER, scrollPane);
        JPanel employeeButtonPanel = createEmployeeButtonPanel();
        panel.add(BorderLayout.EAST, employeeButtonPanel);

        JPanel upper = new JPanel();
        upper.setLayout(new BoxLayout(upper, BoxLayout.X_AXIS));
        upper.add(dateHoursLabel);
        upper.add(new JLabel("  "));
        upper.add(changeDateButton);
        upper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(BorderLayout.NORTH, upper);
        panel.setPreferredSize(new Dimension(700, 300));
        return panel;
    }

    class EmployeeHoursModel extends AbstractTableModel {
        String[] columnNames = {"id", "First name", "Last name", "In hour", "Out hour"};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return employees.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (col == 0) return employees.get(row).getId();
            if (col == 1) return employees.get(row).getFirstname();
            if (col == 2) return employees.get(row).getLastname();

            if (col == 3) {
                Hours hours = controller.getHourForDate(employees.get(row).getId(), dateForHours);
                if (hours.getInHour() < 0) {
                    return "N/A";
                }
                return String.format("%02d:%02d", hours.getInHour(), hours.getInMin());
            }
            if (col == 4) {
                Hours hours = controller.getHourForDate(employees.get(row).getId(), dateForHours);
                if (hours.getOutHour() < 0) {
                    return "N/A";
                }
                return String.format("%02d:%02d", hours.getOutHour(), hours.getOutMin());
            }
            return null;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    private JPanel createPeriodPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Period: Start date: "));
        panel.add(date1Field);
        panel.add(new JLabel("     End date: "));
        panel.add(date2Field);
        panel.add(new JLabel("                                        "));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createMaterialsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Used Materials"));
        panel.setLayout(new BorderLayout(10, 10));

        UsedMaterials model = new UsedMaterials();
        materialsTable = new JTable(model);
        materialsTable.setPreferredScrollableViewportSize(new Dimension(550, 200));
        materialsTable.setFillsViewportHeight(true);
        materialsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        materialsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        materialsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        materialsTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        materialsTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        materialsTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(materialsTable);

        panel.add(BorderLayout.CENTER, scrollPane);
        JPanel materialsButtonPanel = createMaterialsButtonPanel();
        panel.add(BorderLayout.EAST, materialsButtonPanel);

        JPanel upper = new JPanel();
        upper.setLayout(new BoxLayout(upper, BoxLayout.X_AXIS));
        upper.add(updateMaterialsButton);
        upper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(BorderLayout.NORTH, upper);
        panel.setPreferredSize(new Dimension(700, 300));
        return panel;
    }

    class UsedMaterials extends AbstractTableModel {
        String[] columnNames = {"Description", "Date", "Material", "Amount", "Cost"};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return usedMaterials.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (col == 0) return controller.findProjectById(usedMaterials.get(row).getProjectId()).getDescription();
            if (col == 1) return uiDateFormat.format(usedMaterials.get(row).getDateUsed());
            if (col == 2) return controller.findMaterialById(usedMaterials.get(row).getMaterialId()).getName();
            if (col == 3) return usedMaterials.get(row).getAmount();

            if (col == 4) {
                double cost = controller.findMaterialById(usedMaterials.get(row).getMaterialId()).getCost();
                return String.format("%.2f", usedMaterials.get(row).getAmount() * cost);
            }
            return null;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    private void fillMaterialsTable() {
        usedMaterials.clear();
        usedMaterials.addAll(controller.getMaterialsUsedForDates(parseDate1(), parseDate2()));
        materialsTable.repaint();
    }

    private Date parseDate1() {
        try {
            return uiDateFormat.parse(date1Field.getText());
        } catch (Exception ex) {
            Date date = new Date();
            date.setTime(0);
            return date;
        }
    }

    private Date parseDate2() {
        try {
            return uiDateFormat.parse(date2Field.getText());
        } catch (Exception ex) {
            return new Date(); //now
        }
    }

    private JPanel createMaterialsButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //for spacing
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(sendRequestButton);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        return panel;
    }

    private void enterDate() {
        String input = JOptionPane.showInputDialog(null, "Enter date in the 'dd/MM//yyyy' format");

        try {
            dateForHours = uiDateFormat.parse(input);
            resetDateForHours();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Bad date format");
        }
    }

    private void resetDateForHours() {
        dateHoursLabel.setText("Current date for working hours: " + uiDateFormat.format(dateForHours));
    }

    private void fillEmployeesTable() {
        employees.clear();
        employees.addAll(controller.getAllEmployees());
        employeeTable.repaint();
    }

    private JPanel createEmployeeButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setLayout(new GridLayout(0, 1, 0, 10));

        panel.add(new JLabel("Select employee first"));
        panel.add(enterHoursButton);
        panel.add(new JLabel(""));
        panel.add(new JLabel("Enter start and end dates"));
        panel.add(new JLabel("to calculate his/her salary"));
        panel.add(calcSalaryButton);

        return panel;
    }

    private void setListeners() {
        //employees
        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (employeeTable.getSelectedRow() >= 0) {
                    String id = employeeTable.getValueAt(employeeTable.getSelectedRow(), 0).toString();
                    setSelectedEmployee(Integer.parseInt(id));
                }
            }
        });
        changeDateButton.addActionListener(e -> enterDate());

        enterHoursButton.addActionListener(e -> enterHoursDialog());
        calcSalaryButton.addActionListener(e -> calcSalary());
        updateMaterialsButton.addActionListener(e -> fillMaterialsTable());

        sendRequestButton.addActionListener(e -> sendRequest());
    }

    private boolean enterHoursDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        String default1 = employeeTable.getValueAt(employeeTable.getSelectedRow(), 3).toString();
        String default2 = employeeTable.getValueAt(employeeTable.getSelectedRow(), 4).toString();
        if (default1.equalsIgnoreCase("n/a")) {
            default1 = "00:00";
        }
        if (default2.equalsIgnoreCase("n/a")) {
            default2 = "00:00";
        }

        panel.add(new JLabel("Enter in hours"));
        JTextField inField = new JTextField(default1);
        panel.add(inField);

        panel.add(new JLabel("Enter out hours"));
        JTextField outField = new JTextField(default2);
        panel.add(outField);

        JButton okButton = new JButton("Enter");
        JButton cancelButton = new JButton("Cancel");
        panel.add(cancelButton);
        panel.add(okButton);

        JDialog d = new JDialog(new JFrame(), "Enter time using hh:mm format", true);
        d.setLayout(new FlowLayout());
        d.add(panel);

        JLabel errorText = new JLabel("");
        errorText.setForeground(Color.RED);
        errorText.setSize(250, 40);
        panel.add(errorText);

        okButton.setEnabled(true);
        okButton.addActionListener(e -> {
            if (checkHours(inField.getText(), outField.getText())) {
                okButton.setEnabled(false);
                d.setVisible(false);

                String[] hm1 = inField.getText().split(":");
                String[] hm2 = outField.getText().split(":");
                int h1 = Integer.parseInt(hm1[0]);
                int m1 = Integer.parseInt(hm1[1]);
                int h2 = Integer.parseInt(hm2[0]);
                int m2 = Integer.parseInt(hm2[1]);
                saveHours(h1, m1, h2, m2);

                d.dispose();
            } else {
                errorText.setText("Bad times entered");
            }
        });
        cancelButton.addActionListener(e -> {
            d.setVisible(false);
            d.dispose();
        });

        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
        return !okButton.isEnabled();
    }

    private boolean checkHours(String time1, String time2) {
        String[] hm1 = time1.split(":");
        String[] hm2 = time2.split(":");
        if (hm1.length == 2 && hm2.length == 2) {
            try {
                int h1 = Integer.parseInt(hm1[0]);
                int m1 = Integer.parseInt(hm1[1]);
                int h2 = Integer.parseInt(hm2[0]);
                int m2 = Integer.parseInt(hm2[1]);
                if (h1 >= 0 && h1 <= 23 && h2 >= 0 && h2 <= 23
                        && m1 >=0 && m1 <= 59 && m2 >=0 && m2 <= 59){
                    if (h1 * 60 + m1 < h2 * 60 + m2) {
                        return true;
                    }
                }
            } catch (Exception ex) {}
        }
        return false;
    }

    private void saveHours(int h1, int m1, int h2, int m2) {
        if (selectedEmployee == null) return;
        Hours hours = controller.getHourForDate(selectedEmployee.getId(), dateForHours);
        hours.setInHour(h1);
        hours.setInMin(m1);
        hours.setOutHour(h2);
        hours.setOutMin(m2);
        controller.saveHours(hours);
        fillEmployeesTable();
    }

    private void setSelectedEmployee(int id) {
        selectedEmployee = controller.findEmployeeById(id);
        if (selectedEmployee == null) {
            fillEmployeesTable();
            clearEmployeeSelection();
        } else {
            enterHoursButton.setEnabled(true);
            calcSalaryButton.setEnabled(true);
        }
    }

    private void calcSalary() {
        if (selectedEmployee == null) return;
        List<Hours> hh = controller.getHourForPeriod(selectedEmployee.getId(), parseDate1(), parseDate2());

        int mins = 0;
        for (Hours h: hh) {
            mins += h.getOutHour() * 60 + h.getOutMin() - h.getInHour() * 60 - h.getInMin();
        }
        JOptionPane.showMessageDialog(null, "Total time for period is: "
                + String.format("%02dh %02dmin, earned: %.2f", mins / 60, mins % 60, mins / 60. * selectedEmployee.getSalary() / 30 / 8));
    }

    private void clearEmployeeSelection() {
        employeeTable.clearSelection();
        selectedEmployee = null;
        enterHoursButton.setEnabled(false);
        calcSalaryButton.setEnabled(false);
    }

    private void sendRequest() {
        String message = JOptionPane.showInputDialog(null, "Type message text");
        if (message == null || message.length() == 0) return;

        Request request = new Request(currentUser.getId(), new Date(), message, false);
        controller.createRequest(request);
    }
}