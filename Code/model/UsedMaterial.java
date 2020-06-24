package model;

import java.util.Date;

public class UsedMaterial {
    private int id;
    private int materialId;
    private int projectId;
    private double amount;
    private Date dateUsed;  //not date

    public UsedMaterial(int id, int materialId, int projectId, double amount, Date dateUsed) {
        this.id = id;
        this.materialId = materialId;
        this.projectId = projectId;
        this.amount = amount;
        this.dateUsed = dateUsed;
    }

    public UsedMaterial(int materialId, int projectId, double amount, Date dateUsed) {
        this(0, materialId, projectId, amount, dateUsed);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDateUsed() {
        return dateUsed;
    }

    public void setDateUsed(Date dateUsed) {
        this.dateUsed = dateUsed;
    }
}
