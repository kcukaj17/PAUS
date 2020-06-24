package model;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class Database {

    private static Connection connection = null;

    private static Connection getConnection() throws Exception {
        if (connection == null) setConnection();
        return connection;
    }

    private static void setConnection() throws Exception {

        //mysql commands to do before the program start:

        //CREATE database construction;
        //USE construction;
        //CREATE USER 'boss'@'localhost' IDENTIFIED BY 'qwerty';
        //GRANT ALL ON construction.* TO 'boss'@'localhost';

        String user = "boss";
        String password = "qwerty";
        String url = "jdbc:mysql://localhost:3306/construction";

        Class.forName("com.mysql.cj.jdbc.Driver");      //mysql 8.0, jdbc connector: mysql-connector-java-8.0.13.jar

        url += "?verifyServerCertificate=false&useSSL=true" +
                "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        connection = DriverManager.getConnection(url, user, password);
    }

    public static boolean checkDatabase() throws Exception {
        return getConnection() != null;
    }

    public static void executeUpdate(String sql) throws Exception {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    public static void paramExecuteUpdate(String sql, Object ... params) throws Exception {
        PreparedStatement ps = getConnection().prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof Integer) {
                ps.setInt(i + 1, (Integer)(params[i]));
            } else {
                if (param instanceof Double) {
                    ps.setDouble(i + 1, (Double) (params[i]));
                } else {
                    if (param instanceof Date) {
                        ps.setDate(i + 1, new java.sql.Date(((Date)(params[i])).getTime()));
                    } else {
                        ps.setString(i + 1, params[i].toString());
                    }
                }
            }
        }
        ps.executeUpdate();
        ps.close();
    }

    public static int paramExecuteUpdateAndGetKey(String sql, Object ... params) throws Exception {
        PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof Integer) {
                ps.setInt(i + 1, (Integer)(params[i]));
            } else {
                if (param instanceof Double) {
                    ps.setDouble(i + 1, (Double) (params[i]));
                } else {
                    if (param instanceof Date) {
                        ps.setDate(i + 1, new java.sql.Date(((Date)(params[i])).getTime()));
                    } else {
                        ps.setString(i + 1, params[i].toString());
                    }
                }
            }
        }
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int key = rs.getInt(1);
        ps.close();
        return key;
    }

    public static int executeUpdateAndGetKey(String sql) throws Exception {
        Statement statement = getConnection().createStatement();

        statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        int key = rs.getInt(1);

        statement.close();
        return key;
    }


    public static ArrayList<String[]> query(String query) throws Exception {
        ArrayList<String[]> list = new ArrayList<>();

        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            ResultSetMetaData meta = resultSet.getMetaData();
            int columns = meta.getColumnCount();

            String[] cols = new String[columns];
            for (int i = 1; i <= columns; i++) {
                cols[i - 1] = resultSet.getString(i);
            }
            list.add(cols);
        }
        statement.close();
        return list;
    }

    public static ArrayList<String[]> paramQuery(String query, String ... params) throws Exception {
        ArrayList<String[]> list = new ArrayList<>();

        PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setString(i + 1, params[i]);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            ResultSetMetaData meta = resultSet.getMetaData();
            int columns = meta.getColumnCount();

            String[] cols = new String[columns];
            for (int i = 1; i <= columns; i++) {
                cols[i - 1] = resultSet.getString(i);
            }
            list.add(cols);
        }
        preparedStatement.close();
        return list;
    }
}

