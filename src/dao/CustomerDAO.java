package dao;

import database.DBConnection;
import entity.Customer;
import entity.TopCustomer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO{

    // get all customers from db
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = """
            SELECT
                c.Customer_ID,
                c.First_Name,
                c.Last_Name,
                c.Customer_BirthDate,
                c.Customer_Email,
                c.Address,
                cp.Phone_Number
            FROM Customer c
            LEFT JOIN CustomerPhone cp
                ON c.Customer_ID = cp.Customer_ID
        """;

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getDate("Customer_BirthDate"),
                        rs.getString("Customer_Email"),
                        rs.getString("Address"),
                        rs.getString("Phone_Number")
                ));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return customers;
    }

    // add new customer , also add phone if he have one
    public static boolean addCustomer(Customer customer){
        String customerSql ="""
            INSERT INTO Customer
            (First_Name,Last_Name,Customer_BirthDate,Customer_Email,Address)
            VALUES (?,?,?,?,?)
        """;

        String phoneSql ="""
            INSERT INTO CustomerPhone
            (Customer_ID,Phone_Number)
            VALUES(?,?)
        """;

        try (Connection con =DBConnection.getConnection()) {
            con.setAutoCommit(false);
            int newCustomerId;
            try (PreparedStatement ps = con.prepareStatement(customerSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,customer.getFirstName());
                ps.setString(2,customer.getLastName());
                ps.setDate(3,customer.getCustomerBirthDate());
                ps.setString(4,customer.getCustomerEmail());
                ps.setString(5,customer.getAddress());

                int rows = ps.executeUpdate();
                if (rows == 0) {
                    con.rollback();
                    return false;
                }

                // get the id that the db gave to new customer
                try (ResultSet keys =ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        newCustomerId = keys.getInt(1);
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }

            if (customer.getPhoneNumber()!=null && !customer.getPhoneNumber().trim().isEmpty()) {
                try (PreparedStatement psPhone = con.prepareStatement(phoneSql)) {
                    psPhone.setInt(1,newCustomerId);
                    psPhone.setString(2,customer.getPhoneNumber().trim());
                    psPhone.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // update customer info , delete old phone and insert new one
    public static boolean updateCustomer(Customer customer) {
        String customerSql = """
            UPDATE Customer SET
                First_Name = ?,
                Last_Name = ?,
                Customer_BirthDate = ?,
                Customer_Email = ?,
                Address = ?
            WHERE Customer_ID = ?
        """;

        String deletePhoneSql = "DELETE FROM CustomerPhone WHERE Customer_ID = ?";

        String insertPhoneSql = """
            INSERT INTO CustomerPhone
            (Customer_ID, Phone_Number)
            VALUES (?, ?)
        """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(customerSql)) {
                ps.setString(1,customer.getFirstName());
                ps.setString(2,customer.getLastName());
                ps.setDate(3,customer.getCustomerBirthDate());
                ps.setString(4,customer.getCustomerEmail());
                ps.setString(5,customer.getAddress());
                ps.setInt(6,customer.getCustomerId());

                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            // remove old phone first
            try (PreparedStatement psDeletePhone = con.prepareStatement(deletePhoneSql)) {
                psDeletePhone.setInt(1,customer.getCustomerId());
                psDeletePhone.executeUpdate();
            }

            if (customer.getPhoneNumber() != null && !customer.getPhoneNumber().trim().isEmpty()) {
                try (PreparedStatement psInsertPhone = con.prepareStatement(insertPhoneSql)) {
                    psInsertPhone.setInt(1,customer.getCustomerId());
                    psInsertPhone.setString(2,customer.getPhoneNumber().trim());
                    psInsertPhone.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete customer and his phone number
    public static boolean deleteCustomer(int customerId) {

        String checkOrdersSql =
                "SELECT COUNT(*) FROM Orders WHERE Customer_ID = ?";

        String deletePhoneSql =
                "DELETE FROM CustomerPhone WHERE Customer_ID = ?";

        String deleteCustomerSql =
                "DELETE FROM Customer WHERE Customer_ID = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement psPhone = con.prepareStatement(deletePhoneSql)) {
                psPhone.setInt(1,customerId);
                psPhone.executeUpdate();
            }

            try (PreparedStatement psCustomer = con.prepareStatement(deleteCustomerSql)) {

                psCustomer.setInt(1,customerId);

                boolean deleted = psCustomer.executeUpdate() > 0;

                if (deleted) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // search by name or email or phone or address
    public static List<Customer> searchCustomers(String keyword) {
        List<Customer> customers = new ArrayList<>();

        String sql = """
            SELECT
                c.Customer_ID,
                c.First_Name,
                c.Last_Name,
                c.Customer_BirthDate,
                c.Customer_Email,
                c.Address,
                cp.Phone_Number
            FROM Customer c
            LEFT JOIN CustomerPhone cp
                ON c.Customer_ID = cp.Customer_ID
            WHERE c.First_Name LIKE ?
               OR c.Last_Name LIKE ?
               OR c.Customer_Email LIKE ?
               OR c.Address LIKE ?
               OR cp.Phone_Number LIKE ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String searchValue ="%"+keyword+"%";

            ps.setString(1,searchValue);
            ps.setString(2,searchValue);
            ps.setString(3,searchValue);
            ps.setString(4,searchValue);
            ps.setString(5,searchValue);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getDate("Customer_BirthDate"),
                        rs.getString("Customer_Email"),
                        rs.getString("Address"),
                        rs.getString("Phone_Number")
                ));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

    // get top 5 customers who made most orders
    public static List<TopCustomer> getTop5CustomersByOrders() {
        List<TopCustomer> customers = new ArrayList<>();

        String sql = """
            SELECT
                c.Customer_ID,
                CONCAT(c.First_Name,' ',c.Last_Name) AS Customer_Name,
                COUNT(o.Order_ID) AS Number_Of_Orders
            FROM Customer c
            JOIN Orders o
                ON c.Customer_ID = o.Customer_ID
            GROUP BY c.Customer_ID, c.First_Name, c.Last_Name
            ORDER BY Number_Of_Orders DESC
            LIMIT 5
        """;

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(new TopCustomer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getInt("Number_Of_Orders")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
}