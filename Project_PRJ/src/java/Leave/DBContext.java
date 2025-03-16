/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Leave;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
/**
 *
 * @author LTSon
 */
public class DBContext {
    protected Connection getConnection() {
        try {
            String user = "sonlt";
            String pass = "1509";
            String url = "jdbc:sqlserver://THINKPAD-SON\\SQLEXPRESS:1433;databaseName=LeaveManagement;trustServerCertificate=true;";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            System.out.println("Failed to load JDBC Driver: " + ex.getMessage());
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Lỗi tải driver JDBC: ", ex);
            return null;
        } catch (SQLException ex) {
            System.out.println("Failed to connect to database: " + ex.getMessage());
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Lỗi kết nối database: ", ex);
            return null;
        }
}
    public abstract ArrayList<T> list();
    public abstract T get(int id);
    public abstract void insert(T model);
    public abstract void update(T model);
    public abstract void delete(T model);
}
