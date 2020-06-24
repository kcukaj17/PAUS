package model;

public class Project {
    private int id;
    private String description;
    private boolean approved;


    public Project(int id, String description, boolean approved) {
        this.id = id;
        this.description = description;
        this.approved = approved;
    }

    public Project(String description, boolean approved) {
        this(0, description, approved);
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
