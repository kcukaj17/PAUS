package controller;

import model.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contains database access methods
 */
public class Controller {
    public static final DateFormat uiDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Fills database with sample data
     *
     * @throws Exception
     */
    public void sampleFill() throws Exception {


        //--------------insert Projects --------------------

        Project p1 = createProject(new Project("Project1 description", false));
        Project p2 = createProject(new Project("Project2 description", true));

        //--------------insert employees --------------------

        Employee e1 = createEmployee(new Employee("Admin name", "last1", "a", "a", "phone1", 3000, Authority.ADMIN, 0));
        Employee e2 = createEmployee(new Employee("Lead name", "last2", "e", "e", "phone2", 2000, Authority.LEAD, 0));
        Employee e3 = createEmployee(new Employee("Eco name", "last3", "l", "l", "phone3", 1000, Authority.ECONOMIST, p1.getId()));
        Employee e4 = createEmployee(new Employee("Worker name", "last4", "w", "w", "phone4", 1000, Authority.WORKER, p1.getId()));
        Employee e5 = createEmployee(new Employee("Manager name", "last5", "m", "m", "phone5", 1000, Authority.MANAGER, p1.getId()));

        //--------------insert Materials --------------------

        Material m1 = createMaterial(new Material("Material name 1", 100));
        Material m2 = createMaterial(new Material("Material name 2", 200));


        //--------------insert Hours --------------------

        Hours h1 = new Hours(uiDateFormat.parse("20/03/2020"), e4.getId(), 7, 30, 16, 45);

        //--------------insert DaysOff --------------------

        DaysOff days1 = createDaysOff(new DaysOff(uiDateFormat.parse("21/03/2020"), e4.getId(), false));

        //--------------insert UsedMaterial --------------------

        createUsedMaterial(new UsedMaterial(m1.getId(), p1.getId(), 10, uiDateFormat.parse("11/02/2020")));
        createUsedMaterial(new UsedMaterial(m2.getId(), p1.getId(), 100, uiDateFormat.parse("12/02/2020")));

        //--------------insert Supplier --------------------

        Supplier s1 = createSupplier(new Supplier("Supplier1", "email1"));
        Supplier s2 = createSupplier(new Supplier("Supplier2", "email2"));

        //--------------insert Orders --------------------

        Order o1 = createOrder(new Order(uiDateFormat.parse("11/02/2020"), s1.getId(), m1.getId(), 100, 9.99));
        Order o2 = createOrder(new Order(uiDateFormat.parse("14/03/2020"), s1.getId(), m2.getId(), 10, 19.99));

        //--------------insert Requests --------------------

        Request r1 = createRequest(new Request(e3.getId(), uiDateFormat.parse("14/03/2020"), "message1", false));
        Request r2 = createRequest(new Request(e2.getId(), uiDateFormat.parse("14/04/2020"), "message2", false));

        System.out.println("Tables filled with sample data");
    }

    public boolean checkUsersTable() throws Exception {
        List<String[]> list = Database.query("SHOW tables");
        for (String[] strings: list) {
            for (String s: strings) {
                if (s.equalsIgnoreCase("Employee")) return true;
            }
        }
        return false;
    }

    ///********************** public database access methods *******************************

    ////////////------------------ Employees -----------------///////////////

    public Employee createEmployee(Employee e) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into Employee " +
                            "(firstname, lastname, login, password, phone, salary, authority, projectId)" +
                            " values(?, ?, ?, ?, ?, ?, ?, ?)",
                    e.getFirstname(), e.getLastname(), e.getLogin(),
                    e.getPassword(), e.getPhone(), e.getSalary(), e.getAuthority().toString().toUpperCase(),
                    e.getProjectId());
            e.setId(id);
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        try {
            List<String[]> list = Database.query("select id, firstname, lastname, login, password, " +
                    "phone, salary, authority, projectId from employee");

            for (String[] res : list) {
                int projectId = res[8] == null ? 0 : Integer.parseInt(res[8]);
                Employee employee = new Employee(Integer.parseInt(res[0]), res[1], res[2], res[3], res[4], res[5],
                        Double.parseDouble(res[6]), Authority.fromString(res[7]), projectId);
                employees.add(employee);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return employees;
    }

    public Employee findEmployeeById(int id) {
        try {
            List<String[]> list = Database.paramQuery("select id, firstname, lastname, login, password, " +
                    "phone, salary, authority, projectId from employee where id=?", id + "");
            if (list.size() > 0) {
                String[] res = list.get(0);
                int projectId = res[8] == null ? 0 : Integer.parseInt(res[8]);
                return new Employee(Integer.parseInt(res[0]), res[1], res[2], res[3], res[4], res[5],
                        Double.parseDouble(res[6]), Authority.fromString(res[7]), projectId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Employee findByLogin(String login) {
        try {
            List<String[]> list = Database.paramQuery("select id, firstname, lastname, login, password, " +
                    "phone, salary, authority, projectId from employee where login=?", login);
            if (list.size() > 0) {
                String[] res = list.get(0);
                int projectId = res[8] == null ? 0 : Integer.parseInt(res[8]);
                return new Employee(Integer.parseInt(res[0]), res[1], res[2], res[3], res[4], res[5],
                        Double.parseDouble(res[6]), Authority.fromString(res[7]), projectId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Employee updateEmployee(Employee e) {
        try {
            Database.paramExecuteUpdate("update Employee SET firstname=?, lastname=?, " +
                            "login=?, password=?, phone=?, salary=?, authority=?, projectId=?" +
                            " where id=?",
                    e.getFirstname(), e.getLastname(), e.getLogin(),
                    e.getPassword(), e.getPhone(), e.getSalary(), e.getAuthority().toString().toUpperCase(),
                    e.getProjectId(), e.getId());
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Employee deleteEmployee(Employee e) {
        try {
            Database.paramExecuteUpdate("delete from Employee where id=?", e.getId());
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /////////// ------------ Project ---------------///////////////

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();

        try {
            List<String[]> list = Database.query("select id, description, approved from project");
            for (String[] res : list) {
                Project project = new Project(Integer.parseInt(res[0]), res[1], Integer.parseInt(res[2]) == 1);
                projects.add(project);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return projects;
    }

    public Project findProjectById(int id) {
        try {
            List<String[]> list = Database.paramQuery("select id, description, approved from project where id=?", id + "");
            if (list.size() > 0) {
                String[] res = list.get(0);
                return new Project(Integer.parseInt(res[0]), res[1], Integer.parseInt(res[2]) == 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Project updateProject(Project p) {
        try {
            Database.paramExecuteUpdate("update Project SET description=? where id=?",
                    p.getDescription(), p.getId());
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Project createProject(Project p) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into Project (description, approved) values(?, ?)",
                    p.getDescription(), p.isApproved() ? 1 : 0);
            p.setId(id);
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Project deleteProject(Project e) {
        try {
            Database.paramExecuteUpdate("delete from Project where id=?", e.getId());
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void unAssignEmployeesFromProject(int projectId) {
        try {
            Database.paramExecuteUpdate("update employee set projectid=0 where projectId=?", projectId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void assignEmployeeForProject(int employeeId, int projectId) {
        try {
            Database.paramExecuteUpdate("update employee set projectId=? where id=?", projectId, employeeId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void approveProject(int id) {
        try {
            Database.paramExecuteUpdate("update project SET approved=true where id=?", id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /////////// ------------ Days off ---------------///////////////

    public List<DaysOff> getAllDaysOffOfEmployee(int employeeId) {

        List<DaysOff> days = new ArrayList<>();

        try {
            List<String[]> list = Database.paramQuery("select id, dateOff, employeeId, approved from daysoff  where employeeId=?", employeeId + "");
            for (String[] res : list) {
                DaysOff day = new DaysOff(Integer.parseInt(res[0]), dbDateFormat.parse(res[1]),
                        Integer.parseInt(res[2]), Integer.parseInt(res[3]) == 1);
                days.add(day);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return days;
    }

    public DaysOff createDaysOff(DaysOff day) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into DaysOff (dateOff, approved, employeeId)" +
                            " values(?, FALSE, ?)",
                    day.getDateOff(), day.getEmployeeId());

            day.setId(id);
            return day;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public void approveDayOffById(int id) {
        try {
            Database.paramExecuteUpdate("update daysoff SET approved=true where id=?", id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteDaysOffOfEmployee(int employeeId) {
        try {
            Database.paramExecuteUpdate("delete from daysoff where employeeId=?", employeeId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /////////// ------------ Hours ---------------///////////////

    public Hours createHours(Hours hours) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into Hours (id, workDate, employeeId, inHour, inMin, outHour, outMin) " +
                            "values(?,?,?,?,?,?,?)",
                    hours.getId(), dbDateFormat.format(hours.getWorkDate()), hours.getEmployeeId(), hours.getInHour(), hours.getInMin(), hours.getOutHour(), hours.getOutMin());
            hours.setId(id);
            return hours;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Hours updateHours(Hours hours) {
        try {
            Database.paramExecuteUpdate("update Hours SET workDate=?, employeeId=?, inHour=?, inMin=?, outHour=?, outMin=? where id=?",
                    dbDateFormat.format(hours.getWorkDate()), hours.getEmployeeId(), hours.getInHour(), hours.getInMin(), hours.getOutHour(), hours.getOutMin(), hours.getId());
            return hours;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void deleteHoursOfEmployee(int employeeId) {
        try {
            Database.paramExecuteUpdate("delete from hours where employeeId=?", employeeId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Hours getHourForDate(int employeeId, Date date) {
        try {
            List<String[]> list = Database.paramQuery("select id, workDate, employeeId, inHour, inMin, outHour, outMin from hours " +
                    " where employeeId=? AND workDate=?", employeeId + "", dbDateFormat.format(date));
            if (list.size() > 0) {
                String[] res = list.get(0);
                Hours hours = new Hours(Integer.parseInt(res[0]), dbDateFormat.parse(res[1]),
                        Integer.parseInt(res[2]),
                        Integer.parseInt(res[3]),
                        Integer.parseInt(res[4]),
                        Integer.parseInt(res[5]),
                        Integer.parseInt(res[6]));
                return hours;
            }
            return new Hours(0, date, employeeId, -1, -1, -1, -1);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Hours> getHourForPeriod(int employeeId, Date date1, Date date2) {
        List<Hours> hh = new ArrayList<>();
        try {
            List<String[]> list = Database.paramQuery("select id, workDate, employeeId, inHour, inMin, outHour, outMin from hours " +
                    " where employeeId=? AND workDate>=? AND workDate<=?", employeeId + "", dbDateFormat.format(date1), dbDateFormat.format(date2));
            for (String[] res : list) {
                Hours hours = new Hours(Integer.parseInt(res[0]), dbDateFormat.parse(res[1]),
                        Integer.parseInt(res[2]),
                        Integer.parseInt(res[3]),
                        Integer.parseInt(res[4]),
                        Integer.parseInt(res[5]),
                        Integer.parseInt(res[6]));
                hh.add(hours);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hh;
    }

    public Hours saveHours(Hours hours) {
        if (hours.getId() == 0) {
            return createHours(hours);
        } else {
            return updateHours(hours);
        }
    }


    /////////// ------------ UsedMaterial ---------------///////////////

    public List<UsedMaterial> getMaterialsUsedInProject(int projectId) {

        List<UsedMaterial> materialsUsed = new ArrayList<>();
        try {
            List<String[]> list = Database.paramQuery("select id, materialId, projectId, amount, dateUsed from UsedMaterial  where projectId=?", projectId + "");
            for (String[] res : list) {
                UsedMaterial used = new UsedMaterial(Integer.parseInt(res[0]), Integer.parseInt(res[1]),
                        Integer.parseInt(res[2]), Double.parseDouble(res[3]), dbDateFormat.parse(res[4]));
                materialsUsed.add(used);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return materialsUsed;
    }

    public List<UsedMaterial> getMaterialsUsedForDates(Date date1, Date date2) {

        List<UsedMaterial> materialsUsed = new ArrayList<>();
        try {
            List<String[]> list = Database.paramQuery("select id, materialId, projectId, amount, dateUsed from UsedMaterial" +
                            " where dateUsed>=? AND dateUsed<=? ORDER BY projectId",
                    dbDateFormat.format(date1), dbDateFormat.format(date2));
            for (String[] res : list) {
                UsedMaterial used = new UsedMaterial(Integer.parseInt(res[0]), Integer.parseInt(res[1]),
                        Integer.parseInt(res[2]), Double.parseDouble(res[3]), dbDateFormat.parse(res[4]));
                materialsUsed.add(used);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return materialsUsed;
    }

    public UsedMaterial findUsedMaterialById(int id) {
        try {
            List<String[]> list = Database.paramQuery("select id, materialId, projectId, amount, dateUsed from UsedMaterial where id=?", id + "");
            if (list.size() > 0) {
                String[] res = list.get(0);
                return new UsedMaterial(Integer.parseInt(res[0]), Integer.parseInt(res[1]),
                        Integer.parseInt(res[2]), Double.parseDouble(res[3]), dbDateFormat.parse(res[4]));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public UsedMaterial updateUsedMaterial(UsedMaterial u) {
        try {
            Database.paramExecuteUpdate("update UsedMaterial SET materialId=?, amount=?, dateUsed=? where id=?",
                    u.getMaterialId(), u.getAmount(), u.getDateUsed(), u.getId());
            return u;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public UsedMaterial deleteUsedMaterial(UsedMaterial u) {
        try {
            Database.paramExecuteUpdate("delete from UsedMaterial where id=?", u.getId());
            return u;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public UsedMaterial createUsedMaterial(UsedMaterial u) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into UsedMaterial (materialId, projectId, amount, dateUsed) values(?,?,?,?)",
                    u.getMaterialId(), u.getProjectId(), u.getAmount(), u.getDateUsed());
            u.setId(id);
            return u;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /////////// ------------ Materials ---------------///////////////

    public Material findMaterialById(int id) {
        try {
            List<String[]> list = Database.paramQuery("select id, name, cost from material where id=?", id + "");
            if (list.size() > 0) {
                String[] res = list.get(0);
                return new Material(Integer.parseInt(res[0]), res[1], Double.parseDouble(res[2]));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Material> getAllMaterials() {
        List<Material> materials = new ArrayList<>();

        try {
            List<String[]> list = Database.query("select id, name, cost from material");

            for (String[] res : list) {
                Material material = new Material(Integer.parseInt(res[0]), res[1], Double.parseDouble(res[2]));
                materials.add(material);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return materials;
    }

    public Material createMaterial(Material m) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into Material (name, cost) values(?, ?)",
                    m.getName(), m.getCost());
            m.setId(id);
            return m;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /////////// ------------ Suppliers ---------------///////////////

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();

        try {
            List<String[]> list = Database.query("select id, name, email from supplier");

            for (String[] res : list) {
                Supplier supplier = new Supplier(Integer.parseInt(res[0]), res[1], res[2]);
                suppliers.add(supplier);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return suppliers;
    }

    public Supplier updateSupplier(Supplier s) {
        try {
            Database.paramExecuteUpdate("update Supplier SET name=?, email=? where id=?",
                    s.getName(), s.getEmail(), s.getId());
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Supplier createSupplier(Supplier s) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into Supplier (name, email) values(?, ?)",
                    s.getName(), s.getEmail());
            s.setId(id);
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Supplier deleteSupplier(Supplier e) {
        try {
            Database.paramExecuteUpdate("delete from Supplier where id=?", e.getId());
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Supplier findSupplierById(int id) {
        try {
            List<String[]> list = Database.paramQuery("select id, name, email from supplier where id=?", id + "");
            if (list.size() > 0) {
                String[] res = list.get(0);
                return new Supplier(Integer.parseInt(res[0]), res[1], res[2]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /////////// ------------ Orders -----------------///////////////

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            List<String[]> list = Database.query("select id, date, supplierId, materialId, amount, price from `order`");

            for (String[] res : list) {
                Order order = new Order(Integer.parseInt(res[0]), dbDateFormat.parse(res[1]),
                        Integer.parseInt(res[2]), Integer.parseInt(res[3]),
                        Double.parseDouble(res[4]), Double.parseDouble(res[5]));
                orders.add(order);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getSupplierOrders(int supplierId) {
        List<Order> orders = new ArrayList<>();
        try {
            List<String[]> list = Database.paramQuery("select id, date, supplierId, materialId, amount, price from `order` where supplierId=?", supplierId + "");

            for (String[] res : list) {
                Order order = new Order(Integer.parseInt(res[0]), dbDateFormat.parse(res[1]),
                        Integer.parseInt(res[2]), Integer.parseInt(res[3]),
                        Double.parseDouble(res[4]), Double.parseDouble(res[5]));
                orders.add(order);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orders;
    }


    public Order updateOrder(Order o) {
        try {
            Database.paramExecuteUpdate("update `order` SET date=?, supplierId=?, materialId=?, amount=?, price=? where id=?",
                    dbDateFormat.format(o.getDate()), o.getSupplierId(), o.getMaterialId(), o.getAmount(), o.getPrice(), o.getId());
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Order createOrder(Order o) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into `order` (date, supplierId, materialId, amount, price) values(?,?,?,?,?)",
                    dbDateFormat.format(o.getDate()), o.getSupplierId(), o.getMaterialId(), o.getAmount(), o.getPrice());
            o.setId(id);
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Order deleteOrder(Order o) {
        try {
            Database.paramExecuteUpdate("delete from `order` where id=?", o.getId());
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Order findOrderById(int id) {
        try {
            List<String[]> list = Database.paramQuery("select id, date, supplierId, materialId, amount, price from `order` where id=?", id + "");
            if (list.size() > 0) {
                String[] res = list.get(0);
                return  new Order(Integer.parseInt(res[0]), dbDateFormat.parse(res[1]),
                        Integer.parseInt(res[2]), Integer.parseInt(res[3]),
                        Double.parseDouble(res[4]), Double.parseDouble(res[5]));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /////////// ------------ Requests ---------------///////////////

    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        try {
            List<String[]> list = Database.query("select id, authorId, date, message, done from request");

            for (String[] res : list) {
                Request request = new Request(Integer.parseInt(res[0]), Integer.parseInt(res[1]),
                        dbDateFormat.parse(res[2]), res[3], Integer.parseInt(res[4]) == 1);
                requests.add(request);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return requests;
    }

    public List<Request> getUnreadRequests() {
        List<Request> requests = new ArrayList<>();
        try {
            List<String[]> list = Database.query("select id, authorId, date, message, done from request where done=FALSE");

            for (String[] res : list) {
                Request request = new Request(Integer.parseInt(res[0]), Integer.parseInt(res[1]),
                        dbDateFormat.parse(res[2]), res[3], Integer.parseInt(res[4]) == 1);
                requests.add(request);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return requests;
    }

    public void markRequestDone(int id) {
        try {
            Database.paramExecuteUpdate("update Request SET done=TRUE where id=?", id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Request updateRequest(Request o) {
        try {
            Database.paramExecuteUpdate("update Request SET authorId=?, date=?, message=?, done=? where id=?",
                    o.getAuthorId(), dbDateFormat.format(o.getDate()), o.getMessage(), o.isDone(), o.getId());
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Request createRequest(Request o) {
        try {
            int id = Database.paramExecuteUpdateAndGetKey("insert into Request (authorId, date, message, done) values(?, ?, ?, ?)",
                    o.getAuthorId(), dbDateFormat.format(o.getDate()), o.getMessage(), o.isDone() ? 1 : 0);
            o.setId(id);
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Request deleteRequest(Request e) {
        try {
            Database.paramExecuteUpdate("delete from Request where id=?", e.getId());
            return e;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Request findRequestById(int id) {
        try {
            List<String[]> list = Database.paramQuery("select id, authorId, date, message, done from request where id=?", id + "");
            if (list.size() > 0) {
                String[] res = list.get(0);
                return new Request(Integer.parseInt(res[0]), Integer.parseInt(res[1]),
                        dbDateFormat.parse(res[2]), res[3], Integer.parseInt(res[4]) == 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //*********************************************************************************//
    //---------------creating tables -----------------//
    public void createTables() throws Exception {

        String sql;
        Database.executeUpdate("DROP TABLE IF exists Request");
        Database.executeUpdate("DROP TABLE IF exists `Order`"); //order is a reserved word
        Database.executeUpdate("DROP TABLE IF exists Supplier");
        Database.executeUpdate("DROP TABLE IF exists UsedMaterial");
        Database.executeUpdate("DROP TABLE IF exists DaysOff");
        Database.executeUpdate("DROP TABLE IF exists Hours");
        Database.executeUpdate("DROP TABLE IF exists Project");
        Database.executeUpdate("DROP TABLE IF exists Material");
        Database.executeUpdate("DROP TABLE IF exists Employee");

        sql = "CREATE TABLE Employee (" +
                "id INTEGER AUTO_INCREMENT," +
                " firstname VARCHAR(40) NOT NULL," +
                " lastname VARCHAR(40) NOT NULL," +
                " login VARCHAR(20)," +
                " password VARCHAR(20)," +
                " phone VARCHAR(15) NOT NULL," +
                " salary DECIMAL(10,2) NOT NULL," +
                " authority VARCHAR(10) NOT NULL," +
                " projectId INTEGER NOT NULL," +
                " PRIMARY KEY (id), UNIQUE (login)" +
                ")";
        Database.executeUpdate(sql);

        sql = "CREATE TABLE Material (" +
                "id INTEGER AUTO_INCREMENT," +
                " name VARCHAR(60) NOT NULL," +
                " cost DECIMAL(10,2) NOT NULL," +
                " PRIMARY KEY (id)" +
                ")";
        Database.executeUpdate(sql);

        sql = "CREATE TABLE Project (" +
                "id INTEGER AUTO_INCREMENT," +
                " description VARCHAR(200) NOT NULL," +
                " approved BOOLEAN," +
                " PRIMARY KEY (id)" +
                ")";
        Database.executeUpdate(sql);

        sql = "CREATE TABLE Hours (" +
                "id INTEGER AUTO_INCREMENT," +
                " workDate DATE NOT NULL," +
                " inHour INTEGER NOT NULL," +
                " inMin INTEGER NOT NULL," +
                " outHour INTEGER," +
                " outMin INTEGER," +
                " employeeId INTEGER NOT NULL," +
                " PRIMARY KEY (id)," +
                " FOREIGN KEY (employeeId) REFERENCES Employee(id)" +
                ")";
        Database.executeUpdate(sql);

        sql = "CREATE TABLE DaysOff (" +
                "id INTEGER AUTO_INCREMENT," +
                " dateOff DATE NOT NULL," +
                " approved BOOLEAN NOT NULL," +
                " employeeId INTEGER NOT NULL," +
                " PRIMARY KEY (id)," +
                " FOREIGN KEY (employeeId) REFERENCES Employee(id)" +
                ")";
        Database.executeUpdate(sql);

        sql = "CREATE TABLE UsedMaterial (" +
                "id INTEGER AUTO_INCREMENT," +
                " amount DECIMAL(10,2) NOT NULL," +
                " dateUsed DATE NOT NULL," +
                " projectId INTEGER NOT NULL," +
                " materialId INTEGER NOT NULL," +
                " PRIMARY KEY (id)," +
                " FOREIGN KEY (projectId) REFERENCES Project(id)," +
                " FOREIGN KEY (materialId) REFERENCES Material(id)" +
                ")";
        Database.executeUpdate(sql);

        sql = "CREATE TABLE Supplier (" +
                "id INTEGER AUTO_INCREMENT," +
                " name VARCHAR(60) NOT NULL," +
                " email VARCHAR(60) NOT NULL," +
                " PRIMARY KEY (id)" +
                ")";
        Database.executeUpdate(sql);

        sql = "CREATE TABLE `Order` (" +
                "id INTEGER AUTO_INCREMENT," +
                " amount DECIMAL(10,2) NOT NULL," +
                " price DECIMAL(10,2) NOT NULL," +
                " date DATE NOT NULL," +
                " supplierId INTEGER NOT NULL," +
                " materialId INTEGER NOT NULL," +
                " PRIMARY KEY (id)," +
                " FOREIGN KEY (supplierId) REFERENCES Supplier(id)," +
                " FOREIGN KEY (materialId) REFERENCES Material(id)" +
                ")";
        Database.executeUpdate(sql);

        sql = "CREATE TABLE Request (" +
                "id INTEGER AUTO_INCREMENT," +
                " authorId INTEGER NOT NULL," +
                " date DATE NOT NULL," +
                " message VARCHAR(60) NOT NULL," +
                " done BOOLEAN NOT NULL," +
                " PRIMARY KEY (id)," +
                " FOREIGN KEY (authorId) REFERENCES Employee(id)" +
                ")";
        Database.executeUpdate(sql);

        System.out.println("Tables created successfully");
    }

    public static void main(String[] args) throws Exception { //only first time
//        Controller controller = new Controller();
//        controller.createTables();
//        controller.sampleFill();
    }
}
