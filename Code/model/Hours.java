package model;

import java.util.Date;

public class Hours {
    private int id;
    private Date workDate;
    private int employeeId;
    private int inHour;
    private int inMin;
    private int outHour;
    private int outMin;

    public Hours(int id, Date workDate, int employeeId, int inHour, int inMin, int outHour, int outMin) {
        this.id = id;
        this.workDate = workDate;
        this.employeeId = employeeId;
        this.inHour = inHour;
        this.inMin = inMin;
        this.outHour = outHour;
        this.outMin = outMin;
    }

    public Hours(Date workDate, int employeeId, int inHour, int inMin, int outHour, int outMin) {
       this(0, workDate, employeeId, inHour, inMin, outHour, outMin);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getInHour() {
        return inHour;
    }

    public void setInHour(int inHour) {
        this.inHour = inHour;
    }

    public int getInMin() {
        return inMin;
    }

    public void setInMin(int inMin) {
        this.inMin = inMin;
    }

    public int getOutHour() {
        return outHour;
    }

    public void setOutHour(int outHour) {
        this.outHour = outHour;
    }

    public int getOutMin() {
        return outMin;
    }

    public void setOutMin(int outMin) {
        this.outMin = outMin;
    }
}
