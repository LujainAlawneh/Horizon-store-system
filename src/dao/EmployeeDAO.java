package dao;

import database.DBConnection;
import entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public static List<Employee> getAllEmployees() {

        List<Employee> list = new ArrayList<>();

        String sql ="""
            SELECT
                e.Employee_ID,
                e.First_Name,
                e.Last_Name,
                e.shift,
                e.Salary,
                e.Address,
                e.Manager_ID,
                e.Hire_Date,
                ep.Phone_Number
            FROM Employee e
            LEFT JOIN EmployeePhone ep
                ON e.Employee_ID = ep.Employee_ID
            ORDER BY e.Employee_ID
        """;

        try (Connection con =DBConnection.getConnection();
             Statement st=con.createStatement();
             ResultSet rs =st.executeQuery(sql)) {

            while (rs.next()) {

                Integer managerId =rs.getObject("Manager_ID") != null
                        ? rs.getInt("Manager_ID")
                        : null;

                list.add(new Employee(
                        rs.getInt("Employee_ID"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("shift"),
                        rs.getDouble("Salary"),
                        rs.getString("Address"),
                        managerId,
                        rs.getDate("Hire_Date"),
                        rs.getString("Phone_Number")
                ));
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public static void addEmployee(Employee e){
        String employeeSql = """
            INSERT INTO Employee
            (Employee_ID,First_Name,Last_Name,shift,Salary,Address,Manager_ID,Hire_Date)
            VALUES (?,?,?,?,?,?,?,?)
        """;

        String phoneSql = """
            INSERT INTO EmployeePhone
            (Employee_ID,Phone_Number)
            VALUES (?, ?)
        """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false); // Start transaction — both inserts must succeed or neither will

            try (PreparedStatement ps = con.prepareStatement(employeeSql)) {
                ps.setInt(1, e.getEmployeeId());
                ps.setString(2,e.getFirstName());
                ps.setString(3,e.getLastName());
                ps.setString(4,e.getShift());
                ps.setDouble(5,e.getSalary());
                ps.setString(6,e.getAddress());

                // Handle the nullable Manager_ID — top-level managers won't have one
                if (e.getManagerId() == null) {
                    ps.setNull(7,Types.INTEGER);
                } else {
                    ps.setInt(7,e.getManagerId());
                }

                ps.setDate(8,e.getHireDate());
                ps.executeUpdate();
            }

            // Only insert a phone record if the employee actually has a phone number
            if (e.getPhoneNumber() != null && !e.getPhoneNumber().trim().isEmpty()) {
                try (PreparedStatement psPhone =con.prepareStatement(phoneSql)) {
                    psPhone.setInt(1,e.getEmployeeId());
                    psPhone.setString(2,e.getPhoneNumber().trim());
                    psPhone.executeUpdate();
                }
            }
            con.commit(); // Everything went well — save both records
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void updateEmployee(Employee e) {
        String employeeSql = """
            UPDATE Employee
            SET First_Name = ?,
                Last_Name = ?,
                shift = ?,
                Salary = ?,
                Address = ?,
                Manager_ID = ?,
                Hire_Date = ?
            WHERE Employee_ID = ?
        """;

        String deletePhoneSql = "DELETE FROM EmployeePhone WHERE Employee_ID = ?";

        String insertPhoneSql = """
            INSERT INTO EmployeePhone
            (Employee_ID, Phone_Number)
            VALUES (?,?)
        """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false); // All steps must succeed together

            try (PreparedStatement ps = con.prepareStatement(employeeSql)) {
                ps.setString(1, e.getFirstName());
                ps.setString(2, e.getLastName());
                ps.setString(3, e.getShift());
                ps.setDouble(4, e.getSalary());
                ps.setString(5, e.getAddress());

                // Manager_ID is nullable — set to NULL if the employee has no manager
                if (e.getManagerId() == null) {
                    ps.setNull(6, Types.INTEGER);
                } else {
                    ps.setInt(6, e.getManagerId());
                }

                ps.setDate(7, e.getHireDate());
                ps.setInt(8, e.getEmployeeId());
                ps.executeUpdate();
            }

            // Always wipe the old phone entry first — cleaner than checking if it exists
            try (PreparedStatement psDeletePhone = con.prepareStatement(deletePhoneSql)) {
                psDeletePhone.setInt(1, e.getEmployeeId());
                psDeletePhone.executeUpdate();
            }

            // Only re-insert the phone if the updated employee still has one
            if (e.getPhoneNumber() != null && !e.getPhoneNumber().trim().isEmpty()) {
                try (PreparedStatement psInsertPhone = con.prepareStatement(insertPhoneSql)) {
                    psInsertPhone.setInt(1, e.getEmployeeId());
                    psInsertPhone.setString(2, e.getPhoneNumber().trim());
                    psInsertPhone.executeUpdate();
                }
            }

            con.commit(); // Commit only after all steps succeed
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteEmployee(int id) {
        String deletePhoneSql = "DELETE FROM EmployeePhone WHERE Employee_ID = ?";
        String deleteEmployeeSql = "DELETE FROM Employee WHERE Employee_ID = ?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            // Phone must go first due to the foreign key constraint on EmployeePhone
            try (PreparedStatement psPhone = con.prepareStatement(deletePhoneSql)) {
                psPhone.setInt(1, id);
                psPhone.executeUpdate();
            }

            try (PreparedStatement psEmployee = con.prepareStatement(deleteEmployeeSql)) {
                psEmployee.setInt(1, id);
                psEmployee.executeUpdate();
            }

            con.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static List<String[]> getTechniciansWithDetails() {

        List<String[]> list =new ArrayList<>();

        String sql ="""
            SELECT
                e.Employee_ID,
                e.First_Name,
                e.Last_Name,
                e.shift,
                e.Salary,
                e.Address,
                e.Hire_Date,
                t.Specialty,
                t.Certification_Level
            FROM Employee e
            JOIN Technician t
                ON e.Employee_ID =t.Employee_ID
            ORDER BY e.Employee_ID
        """;

        try (Connection con =DBConnection.getConnection();
             Statement st =con.createStatement();
             ResultSet rs =st.executeQuery(sql)) {

            while (rs.next()) {
                // Combine first and last name for cleaner display in the UI
                String fullName = rs.getString("First_Name") + " " + rs.getString("Last_Name");

                list.add(new String[]{
                        String.valueOf(rs.getInt("Employee_ID")),
                        fullName,
                        rs.getString("shift"),
                        String.valueOf(rs.getDouble("Salary")),
                        rs.getString("Address"),
                        String.valueOf(rs.getDate("Hire_Date")),
                        rs.getString("Specialty"),
                        rs.getString("Certification_Level")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public static List<String[]> getSalesEmployeesExceededTarget(){
        List<String[]> list = new ArrayList<>();

        String sql ="""
            SELECT
                e.Employee_ID,
                e.First_Name,
                e.Last_Name,
                e.shift,
                e.Salary,
                s.Sales_Commission,
                s.Sales_Target,
                SUM(o.Total_Cost) AS Total_Sales
            FROM Employee e
            JOIN Sales_Employee s
                ON e.Employee_ID = s.Employee_ID
            JOIN Orders o
                ON e.Employee_ID = o.Employee_ID
            GROUP BY
                e.Employee_ID,
                e.First_Name,
                e.Last_Name,
                e.shift,
                e.Salary,
                s.Sales_Commission,
                s.Sales_Target
            HAVING SUM(o.Total_Cost)>s.Sales_Target
            ORDER BY Total_Sales DESC
        """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String fullName = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                list.add(new String[]{
                        String.valueOf(rs.getInt("Employee_ID")),
                        fullName,
                        rs.getString("shift"),
                        String.valueOf(rs.getDouble("Salary")),
                        String.valueOf(rs.getDouble("Sales_Commission")),
                        String.valueOf(rs.getDouble("Sales_Target")),
                        String.valueOf(rs.getDouble("Total_Sales"))
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String[]> getWarehouseEmployeesWithDetails() {

        List<String[]> list = new ArrayList<>();

        String sql ="""
            SELECT
                e.Employee_ID,
                e.First_Name,
                e.Last_Name,
                e.shift,
                e.Salary,
                e.Address,
                e.Hire_Date,
                w.Forklift_Certified
            FROM Employee e
            JOIN Warehouse_Employee w
                ON e.Employee_ID = w.Employee_ID
            ORDER BY e.Employee_ID
        """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String fullName = rs.getString("First_Name") + " " + rs.getString("Last_Name");

                list.add(new String[]{
                        String.valueOf(rs.getInt("Employee_ID")),
                        fullName,
                        rs.getString("shift"),
                        String.valueOf(rs.getDouble("Salary")),
                        rs.getString("Address"),
                        String.valueOf(rs.getDate("Hire_Date")),
                        rs.getBoolean("Forklift_Certified") ? "Yes" : "No" // Readable label for the UI
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}