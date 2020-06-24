package model;

import java.util.Date;

public class DaysOff {
    private int id;
    private Date dateOff;  //not date!!
    private int employeeId;
    private boolean approved;

    public DaysOff(int id, Date dateOff, int employeeId, boolean approved) {
        this.id = id;
        this.dateOff = dateOff;
        this.employeeId = employeeId;
        this.approved = approved;
    }

    public DaysOff(Date dateOff, int employeeId, boolean approved) {
        this(0, dateOff, employeeId, approved);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateOff() {
        return dateOff;
    }

    public void setDateOff(Date dateOff) {
        this.dateOff = dateOff;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
