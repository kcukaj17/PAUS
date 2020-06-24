package model;

public class Employee {
    private int id;
    private String firstname;
    private String lastname;
    private String login;		//nullable
    private String password;	//nullable
    private String phone;
    private double salary;
    private Authority authority;          //admin, lead, economist.
    private int projectId;	//nullable

    public Employee(int id, String firstname, String lastname, String login, String password, String phone, double salary, Authority authority, int projectId) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.password = password;
        this.phone = phone;
        this.salary = salary;
        this.authority = authority;
        this.projectId = projectId;
    }

    public Employee(String firstname, String lastname, String login, String password, String phone, double salary, Authority authority, int projectId) {
        this(0, firstname, lastname, login, password, phone, salary, authority, projectId);
    }


    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public double getSalary() {
        return salary;
    }

    public Authority getAuthority() {
        return authority;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
