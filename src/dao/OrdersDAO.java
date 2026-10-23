package dao;

import database.DBConnection;
import entity.Orders;
import entity.BestSellingEmployee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {

    public static List<Orders> getAllOrders() {
        List<Orders> orders = new ArrayList<>();

        String sql = """
            SELECT
                o.Order_ID,
                o.Order_Date,
                SUM(od.Line_Total) AS Total_Cost,
                o.Customer_ID,
                o.Employee_ID
            FROM Orders o
            LEFT JOIN OrderDetails od
                ON o.Order_ID = od.Order_ID
            GROUP BY o.Order_ID, o.Order_Date, o.Customer_ID, o.Employee_ID
            ORDER BY o.Order_Date DESC
        """;

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(new Orders(
                        rs.getInt("Order_ID"),
                        rs.getDate("Order_Date"),
                        rs.getDouble("Total_Cost"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("Employee_ID")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static List<String[]> getOrdersWithNames() {
        List<String[]> list = new ArrayList<>();

        String sql = """
            SELECT
                o.Order_ID,
                o.Order_Date,
                IFNULL(SUM(od.Line_Total), 0) AS Total_Cost,
                c.First_Name,
                c.Last_Name,
                e.First_Name AS Emp_First_Name,
                e.Last_Name AS Emp_Last_Name
            FROM Orders o
            JOIN Customer c
                ON o.Customer_ID = c.Customer_ID
            JOIN Employee e
                ON o.Employee_ID = e.Employee_ID
            LEFT JOIN OrderDetails od
                ON o.Order_ID = od.Order_ID
            GROUP BY
                o.Order_ID,
                o.Order_Date,
                c.First_Name,
                c.Last_Name,
                e.First_Name,
                e.Last_Name
            ORDER BY o.Order_Date DESC
        """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String customerName = rs.getString("First_Name") + " " + rs.getString("Last_Name");
                String employeeName = rs.getString("Emp_First_Name") + " " + rs.getString("Emp_Last_Name");

                list.add(new String[]{
                        String.valueOf(rs.getInt("Order_ID")),
                        String.valueOf(rs.getDate("Order_Date")),
                        String.valueOf(rs.getDouble("Total_Cost")),
                        customerName,
                        employeeName
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String[]> getOrderDetailsByOrderId(int orderId) {
        List<String[]> list = new ArrayList<>();

        String sql = """
            SELECT
                od.Order_ID,
                p.Product_Name,
                p.Brand,
                od.Quantity,
                od.Unit_Price,
                od.Line_Total,

                pay.Payment_Date,
                pay.Amount,
                pay.Payment_Status,

                cp.Payment_ID AS Cash_Payment_ID,
                cp.Cash_Received,
                cp.Change_Amount,

                ip.Payment_ID AS Installment_Payment_ID,
                ip.Down_Payment,
                ip.Number_Of_Installments,
                ip.Monthly_Amount,
                ip.Remaining_Balance

            FROM OrderDetails od
            JOIN Product p
                ON od.Product_ID = p.Product_ID

            LEFT JOIN Payment pay
                ON od.Order_ID = pay.Order_ID

            LEFT JOIN Cash_Payment cp
                ON pay.Payment_ID = cp.Payment_ID

            LEFT JOIN Installment_Payment ip
                ON pay.Payment_ID = ip.Payment_ID

            WHERE od.Order_ID = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String method;
                String paymentDetails;

                if (rs.getObject("Cash_Payment_ID") != null) {
                    method = "Cash";
                    paymentDetails =
                            "Cash Received: " + rs.getDouble("Cash_Received") +
                            ", Change: " + rs.getDouble("Change_Amount");

                } else if (rs.getObject("Installment_Payment_ID") != null) {
                    method = "Installment";
                    paymentDetails =
                            "Down Payment: " + rs.getDouble("Down_Payment") +
                            ", Installments: " + rs.getInt("Number_Of_Installments") +
                            ", Monthly: " + rs.getDouble("Monthly_Amount") +
                            ", Remaining: " + rs.getDouble("Remaining_Balance");

                } else {
                    method = "No Payment";
                    paymentDetails = "No payment details";
                }

                list.add(new String[]{
                        String.valueOf(rs.getInt("Order_ID")),
                        rs.getString("Product_Name"),
                        rs.getString("Brand"),
                        String.valueOf(rs.getInt("Quantity")),
                        String.valueOf(rs.getDouble("Unit_Price")),
                        String.valueOf(rs.getDouble("Line_Total")),
                        String.valueOf(rs.getDate("Payment_Date")),
                        String.valueOf(rs.getDouble("Amount")),
                        rs.getString("Payment_Status"),
                        method,
                        paymentDetails
                });
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean addOrder(Orders order) {
        String sql = "INSERT INTO Orders (Order_Date, Total_Cost, Customer_ID, Employee_ID) "
                + "VALUES (?, 0, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, order.getOrderDate());
            ps.setInt(2, order.getCustomerId());
            ps.setInt(3, order.getEmployeeId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateOrder(Orders order) {
        String sql = "UPDATE Orders SET Order_Date=?, Customer_ID=?, Employee_ID=? WHERE Order_ID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, order.getOrderDate());
            ps.setInt(2, order.getCustomerId());
            ps.setInt(3, order.getEmployeeId());
            ps.setInt(4, order.getOrderId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteOrder(int orderId) {
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps1 = con.prepareStatement("DELETE FROM TheReturns WHERE Order_ID = ?");
            ps1.setInt(1, orderId);
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement("DELETE FROM ProductSerial WHERE Order_ID = ?");
            ps2.setInt(1, orderId);
            ps2.executeUpdate();

            PreparedStatement ps3 = con.prepareStatement("DELETE FROM OrderDetails WHERE Order_ID = ?");
            ps3.setInt(1, orderId);
            ps3.executeUpdate();

            PreparedStatement ps4 = con.prepareStatement(
                    "DELETE FROM Cash_Payment WHERE Payment_ID IN " +
                            "(SELECT Payment_ID FROM Payment WHERE Order_ID = ?)");
            ps4.setInt(1, orderId);
            ps4.executeUpdate();

            PreparedStatement ps5 = con.prepareStatement(
                    "DELETE FROM Installment_Payment WHERE Payment_ID IN " +
                            "(SELECT Payment_ID FROM Payment WHERE Order_ID = ?)");
            ps5.setInt(1, orderId);
            ps5.executeUpdate();

            PreparedStatement ps6 = con.prepareStatement("DELETE FROM Payment WHERE Order_ID = ?");
            ps6.setInt(1, orderId);
            ps6.executeUpdate();

            PreparedStatement ps7 = con.prepareStatement("DELETE FROM Orders WHERE Order_ID = ?");
            ps7.setInt(1, orderId);

            return ps7.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<BestSellingEmployee> getBestSellingEmployee() {
        List<BestSellingEmployee> list = new ArrayList<>();

        String sql = """
            SELECT
                e.Employee_ID,
                CONCAT(e.First_Name, ' ', e.Last_Name) AS Employee_Name,
                COUNT(o.Order_ID) AS Number_Of_Orders,
                IFNULL(SUM(od.Line_Total), 0) AS Total_Sales
            FROM Employee e
            JOIN Orders o
                ON e.Employee_ID = o.Employee_ID
            LEFT JOIN OrderDetails od
                ON o.Order_ID = od.Order_ID
            GROUP BY e.Employee_ID, e.First_Name, e.Last_Name
            ORDER BY Total_Sales DESC
            LIMIT 1
        """;

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new BestSellingEmployee(
                        rs.getInt("Employee_ID"),
                        rs.getString("Employee_Name"),
                        rs.getInt("Number_Of_Orders"),
                        rs.getDouble("Total_Sales")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean addOrderDetails(int orderId, int productId, int quantity) {
        String getPriceSql = "SELECT Price FROM Product WHERE Product_ID = ?";

        String insertSql =
                "INSERT INTO OrderDetails (Quantity, Unit_Price, Line_Total, Order_ID, Product_ID) " +
                        "VALUES (?, ?, ?, ?, ?)";

        String updateTotalSql =
                "UPDATE Orders SET Total_Cost = " +
                        "(SELECT IFNULL(SUM(Line_Total), 0) FROM OrderDetails WHERE Order_ID = ?) " +
                        "WHERE Order_ID = ?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            double unitPrice;

            try (PreparedStatement psPrice = con.prepareStatement(getPriceSql)) {
                psPrice.setInt(1, productId);
                ResultSet rs = psPrice.executeQuery();

                if (!rs.next()) {
                    con.rollback();
                    return false;
                }

                unitPrice = rs.getDouble("Price");
            }

            double lineTotal = quantity * unitPrice;

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, quantity);
                ps.setDouble(2, unitPrice);
                ps.setDouble(3, lineTotal);
                ps.setInt(4, orderId);
                ps.setInt(5, productId);
                ps.executeUpdate();
            }

            try (PreparedStatement psUpdate = con.prepareStatement(updateTotalSql)) {
                psUpdate.setInt(1, orderId);
                psUpdate.setInt(2, orderId);
                psUpdate.executeUpdate();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String[]> getDailySalesReport() {
        List<String[]> list = new ArrayList<>();

        String sql = """
            SELECT
                o.Order_Date,
                COUNT(DISTINCT o.Order_ID) AS Number_Of_Orders,
                IFNULL(SUM(od.Line_Total), 0) AS Total_Sales
            FROM Orders o
            LEFT JOIN OrderDetails od
                ON o.Order_ID = od.Order_ID
            GROUP BY o.Order_Date
            ORDER BY o.Order_Date
        """;
        
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[]{
                        String.valueOf(rs.getDate("Order_Date")),
                        String.valueOf(rs.getInt("Number_Of_Orders")),
                        String.valueOf(rs.getDouble("Total_Sales"))
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}