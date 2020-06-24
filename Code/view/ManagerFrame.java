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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static controller.Controller.uiDateFormat;

public class ManagerFrame extends JFrame {
    private JPanel suppliersPanel;
    private JPanel supplierButtonPanel;
    private JPanel requestsPanel;
    private JPanel orderTablePanel;
    private JPanel orderButtonPanel;

    private JTable supplierTable;
    private JTable requestsTable;
    private JTable orderTable;

    private JButton updateSupplierButton = new JButton("Update Supplier");
    private JButton addSupplierButton = new JButton("Add new Supplier");
    private JButton delSupplierButton = new JButton("Delete Supplier");

    private JButton clearOrderButton = new JButton("Clear Order");
    private JButton updateOrderButton = new JButton("Update Order");
    private JButton addOrderButton = new JButton("Add Order");
    private JButton delOrderButton = new JButton("Delete Order");
    private JButton sendOrderButton = new JButton("Send Order");

    private JButton addMaterialButton = new JButton("New Material");

    private JTextField orderDate = new JTextField();
    private JComboBox<Material> materialBox = new JComboBox<>();
    private JTextField orderAmount = new JTextField();
    private JTextField orderPrice = new JTextField();

    private Supplier selectedSupplier;
    private Order selectedOrder;
    private List<Order> orders = new ArrayList<>();
    private List<Material> materials;

    private Controller controller;

    private Employee currentUser;

    public ManagerFrame(Employee user) {
        currentUser = user;

        controller = new Controller();
        setLayout(new BorderLayout(10, 10));
        setSize(1100, 600);
        setTitle("Manager Window");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        add(mainPanel, BorderLayout.CENTER);

        suppliersPanel = createSupplierTablePanel();
        requestsPanel = createRequestsPanel();
        orderTablePanel = createOrderTablePanel();

        mainPanel.add(suppliersPanel, BorderLayout.CENTER);
        mainPanel.add(requestsPanel, BorderLayout.SOUTH);
        mainPanel.add(orderTablePanel, BorderLayout.EAST);

        setListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        fillSuppliersTable();
        fillRequestTable();
        clearSupplierSelection();
        updateMaterialsBox();

    }

    private JPanel createSupplierTablePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Suppliers"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        String[] columnNames = {"id", "Name", "Email"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        supplierTable = new JTable(model);
        supplierTable.setPreferredScrollableViewportSize(new Dimension(200, 300));
        supplierTable.setFillsViewportHeight(true);
        supplierTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        supplierTable.getColumnModel().getColumn(1).setPreferredWidth(170);
        supplierTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(supplierTable);

        panel.add(scrollPane);
        supplierButtonPanel = createSupplierButtonPanel();
        panel.add(supplierButtonPanel);

        panel.setPreferredSize(new Dimension(400, 300));
        return panel;
    }

    private void fillSuppliersTable() {
        DefaultTableModel model = (DefaultTableModel) supplierTable.getModel();
        model.setRowCount(0);

        List<Supplier> suppliers = controller.getAllSuppliers();
        for (Supplier s : suppliers) {
            model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail()});
        }
    }

    private JPanel createSupplierButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(updateSupplierButton);
        panel.add(addSupplierButton);
        panel.add(delSupplierButton);

        for (int i = 0; i < 4; i++) {  //for spacing
            panel.add(new JLabel(""));
        }
        return panel;
    }

    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Requests"));

        requestsTable = new JTable(new RequestTableModel());
        requestsTable.setPreferredScrollableViewportSize(new Dimension(350, 300));
        requestsTable.setFillsViewportHeight(true);

//        requestsTable.getColumnModel().getColumn(0).setPreferredWidth(30);
//        requestsTable.getColumnModel().getColumn(3).setPreferredWidth(70);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(requestsTable);

        requestsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (requestsTable.getSelectedRow() >= 0) {
                    int requestId = Integer.parseInt(requestsTable.getModel().getValueAt(requestsTable.getSelectedRow(), 3).toString());
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Mark as done?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        controller.markRequestDone(requestId);
                        fillRequestTable();
                    }

                }
            }
        });

        panel.add(scrollPane);
        //panel.setPreferredSize(new Dimension(350, 300));
        return panel;
    }

    class RequestTableModel extends AbstractTableModel {
        String[] columnNames = {"Date", "Author", "Message"};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return controller.getUnreadRequests().size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (col == 0) return uiDateFormat.format(controller.getUnreadRequests().get(row).getDate());
            if (col == 1) {
                int id =  controller.getUnreadRequests().get(row).getAuthorId();
                return controller.findEmployeeById(id).getFirstname() + " " + controller.findEmployeeById(id).getLastname();
            }
            if (col == 2) {
                return controller.getUnreadRequests().get(row).getMessage();
            }
            if (col == 3) {
                return controller.getUnreadRequests().get(row).getId();
            }
            return null;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    private JPanel createOrdersButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(new JLabel("Date"));
        panel.add(orderDate);
        panel.add(new JLabel("Material"));
        panel.add(materialBox);
        panel.add(new JLabel("Amount"));
        panel.add(orderAmount);
        panel.add(new JLabel("Price"));
        panel.add(orderPrice);

        panel.add(clearOrderButton);
        panel.add(updateOrderButton);
        panel.add(addOrderButton);
        panel.add(delOrderButton);

        panel.add(new JLabel(""));
        panel.add(sendOrderButton);

        panel.add(addMaterialButton);


        return panel;
    }

    private JPanel createOrderTablePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Orders"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        orderTable = new JTable(new OrdersTableModel());
        orderTable.setPreferredScrollableViewportSize(new Dimension(450, 300));
        orderTable.setFillsViewportHeight(true);
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(90);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        orderTable.getColumnModel().getColumn(5).setPreferredWidth(60);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        orderTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        orderTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(orderTable);

        panel.add(scrollPane);
        orderButtonPanel = createOrdersButtonPanel();
        orderButtonPanel.setPreferredSize(new Dimension(250, 300));

        panel.add(orderButtonPanel);
        panel.setPreferredSize(new Dimension(700, 300));

        return panel;
    }

    class OrdersTableModel extends AbstractTableModel {
       String[] columnNames = {"Date", "Supplier", "Material", "Price", "Amount", "Total"};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (selectedSupplier == null) return 0;
            return orders.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (col == 0) return uiDateFormat.format(orders.get(row).getDate());
            if (col == 1) return selectedSupplier.getName();
            if (col == 2) {
                int matId = orders.get(row).getMaterialId();
                return controller.findMaterialById(matId).getName();
            }
            if (col == 3) return String.format("%.2f", orders.get(row).getPrice());
            if (col == 4) return orders.get(row).getAmount();
            if (col == 5) return String.format("%.2f", orders.get(row).getAmount() * orders.get(row).getPrice());
            return null;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    private void setListeners() {
        //suppliers
        supplierTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (supplierTable.getSelectedRow() >= 0) {
                    String id = supplierTable.getValueAt(supplierTable.getSelectedRow(), 0).toString();
                    setSelectedSupplier(Integer.parseInt(id));
                }
            }
        });

        updateSupplierButton.addActionListener(e -> updateSupplier());
        delSupplierButton.addActionListener(e -> deleteSupplier());
        addSupplierButton.addActionListener(e -> addSupplier());

        //orders
        orderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (orderTable.getSelectedRow() >= 0) {
                    int id = orders.get(orderTable.getSelectedRow()).getId();
                    setSelectedOrder(id);
                }
            }
        });

        clearOrderButton.addActionListener(e -> clearOrderSelection());
        updateOrderButton.addActionListener(e -> updateOrder());
        delOrderButton.addActionListener(e -> deleteOrder());
        sendOrderButton.addActionListener(e -> sendOrder());
        addOrderButton.addActionListener(e -> addOrder());

        addMaterialButton.addActionListener(e -> addMaterial());

    }

    private void addMaterial() {
        String input = JOptionPane.showInputDialog(null, "Enter material name");
        if (input.length() == 0) return;
        String input2 = JOptionPane.showInputDialog(null, "Enter material's cost");
        try {
            double d = Double.parseDouble(input2);
            Material m = new Material(input, d);
            controller.createMaterial(m);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Bad number entered");
        }
        updateMaterialsBox();
    }

    private void setSelectedSupplier(int id) {
        selectedSupplier = controller.findSupplierById(id);
        if (selectedSupplier == null) {
            fillSuppliersTable();
            clearSupplierSelection();
        } else {
            updateSupplierButton.setEnabled(true);
            delSupplierButton.setEnabled(true);

            fillOrderTable();
            clearOrderSelection();
        }
    }

    private void setSelectedOrder(int id) {
        if (selectedSupplier == null) return;

        selectedOrder = controller.findOrderById(id);
        if (selectedOrder == null) {
            fillOrderTable();
            clearOrderSelection();
        } else {
            updateOrderButton.setEnabled(true);
            delOrderButton.setEnabled(true);
            sendOrderButton.setEnabled(true);
            addOrderButton.setEnabled(false);
            clearOrderButton.setEnabled(true);

            orderDate.setText(uiDateFormat.format(selectedOrder.getDate()));
            orderAmount.setText(selectedOrder.getAmount() + "");
            orderPrice.setText(selectedOrder.getPrice() + "");

            //find material by id in the combobox
            for (Material material : materials) {
                if (material.getId() == selectedOrder.getMaterialId()) {
                    materialBox.setSelectedItem(material);
                    break;
                }
            }
        }
    }

    private void clearSupplierSelection() {
        supplierTable.clearSelection();
        selectedSupplier = null;

        updateSupplierButton.setEnabled(false);
        delSupplierButton.setEnabled(false);

        clearOrderSelection();
        clearOrderPanel();
    }

    private void clearOrderPanel() {
        clearOrderSelection();
        addOrderButton.setEnabled(false);
        orders.clear();
        orderTable.repaint();
    }

    private void clearOrderSelection() {
        orderTable.clearSelection();
        selectedOrder = null;

        orderDate.setText("");
        orderAmount.setText("");
        orderPrice.setText("");

        if (selectedSupplier != null) {
            clearOrderButton.setEnabled(true);
            addOrderButton.setEnabled(true);
        } else {
            clearOrderButton.setEnabled(false);
            updateOrderButton.setEnabled(false);
        }
        updateOrderButton.setEnabled(false);
        delOrderButton.setEnabled(false);
        sendOrderButton.setEnabled(false);
    }


    ///buttons actions for suppliers
    private void updateSupplier() {
        if (selectedSupplier == null) return;

        String name = JOptionPane.showInputDialog(null, "Enter supplier's name:");
        if (name == null || name.length() == 0) return;

        String email = JOptionPane.showInputDialog(null, "Enter supplier's email:");
        if (email == null || email.length() == 0) return;

        selectedSupplier.setName(name);
        selectedSupplier.setEmail(email);

        if (controller.updateSupplier(selectedSupplier) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillSuppliersTable();
        clearSupplierSelection();
    }

    private void addSupplier() {

        String name = JOptionPane.showInputDialog(null, "Enter supplier's name:");
        if (name == null || name.length() == 0) return;

        String email = JOptionPane.showInputDialog(null, "Enter supplier's email:");
        if (email == null || email.length() == 0) return;

        Supplier s = new Supplier(name, email);
        if (controller.createSupplier(s) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillSuppliersTable();
        clearSupplierSelection();
    }

    private void deleteSupplier() {
        if (selectedSupplier == null) return;

        List<Order> materialsOrder = controller.getSupplierOrders(selectedSupplier.getId());
        if (materialsOrder.size() > 0) {
            JOptionPane.showMessageDialog(null, "The supplier can not be deleted. There are orders with it.");
            return;
        }

        if (controller.deleteSupplier(selectedSupplier) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillSuppliersTable();
        fillRequestTable();
        clearSupplierSelection();
    }

    //populating tables
    private void fillRequestTable() {
        requestsTable.repaint();
    }

    private void fillOrderTable() {
        orders.clear();
        if (selectedSupplier != null) {
            orders.addAll(controller.getSupplierOrders(selectedSupplier.getId()));
        }
        orderTable.repaint();
    }

    private void updateMaterialsBox() {
        materialBox.removeAllItems();
        materials = controller.getAllMaterials();

        for (Material material : materials) {
            materialBox.addItem(material);
        }
    }

    ///buttons actions for order
    private boolean checkFieldsOrder() {
        if (orderDate.getText().length() == 0
                || orderAmount.getText().length() == 0
                || orderPrice.getText().length() == 0
                || materialBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Fill all the fields");
            return false;
        }

        try {
            double amount = Double.parseDouble(orderAmount.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null, "Amount must be positive");
                return false;
            }
            double price = Double.parseDouble(orderPrice.getText());
            if (price <= 0) {
                JOptionPane.showMessageDialog(null, "Price must be positive");
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Bad number in salary field");
            return false;
        }

        Date date;
        try {
            date = uiDateFormat.parse(orderDate.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Bad date format");
            return false;
        }
        return true;
    }

    private void updateOrder() {
        if (selectedOrder == null) return;

        if (!checkFieldsOrder()) return;
        Material material = (Material) materialBox.getSelectedItem();

        Date date;
        try {
            date = uiDateFormat.parse(orderDate.getText());
        } catch (Exception ex) {
            return;
        }
        double amount = Double.parseDouble(orderAmount.getText());
        double price = Double.parseDouble(orderPrice.getText());

        selectedOrder.setAmount(amount);
        selectedOrder.setPrice(price);
        selectedOrder.setMaterialId(material.getId());
        selectedOrder.setDate(date);

        if (controller.updateOrder(selectedOrder) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillOrderTable();
        clearOrderSelection();
    }

    private void deleteOrder() {
        if (selectedOrder == null) return;
        System.out.println("del " + selectedOrder.getId() + " " + selectedOrder.getSupplierId());
        if (controller.deleteOrder(selectedOrder) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillOrderTable();
        clearOrderSelection();
    }

    private void addOrder() {
        if (selectedOrder != null) return;

        if (!checkFieldsOrder()) return;
        Material material = (Material) materialBox.getSelectedItem();

        Date date;
        try {
            date = uiDateFormat.parse(orderDate.getText());
        } catch (Exception ex) {
            return;
        }
        double amount = Double.parseDouble(orderAmount.getText());
        double price = Double.parseDouble(orderPrice.getText());

        Order order = new Order(date, selectedSupplier.getId(), material.getId(), amount, price);

        if (controller.createOrder(order) == null) {
            JOptionPane.showMessageDialog(null, "Error occurred while updating database");
        }
        fillOrderTable();
        clearOrderSelection();
    }

    private void sendOrder() {
        JOptionPane.showMessageDialog(null, "The order has been sent");
    }

}
