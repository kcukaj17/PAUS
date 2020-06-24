package model;

import java.util.Date;

public class Order {
    private int id;
    private Date date;
    private int supplierId;
    private int materialId;
    private double amount;
    private double price;

    public Order(int id, Date date, int supplierId, int materialId, double amount, double price) {
        this.id = id;
        this.date = date;
        this.supplierId = supplierId;
        this.materialId = materialId;
        this.amount = amount;
        this.price = price;
    }

    public Order(Date date, int supplierId, int materialId, double amount, double price) {
        this(0, date, supplierId, materialId, amount, price);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }
}
