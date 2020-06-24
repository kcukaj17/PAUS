package model;

public class Material {
    private int id;
    private String name;
    private double cost; 		//per unit

    public Material(int id, String name, double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public Material(String name, double cost) {
        this(0, name,cost);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return name;
    }
}
