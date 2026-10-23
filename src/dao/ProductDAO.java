package dao;

import database.DBConnection;
import entity.Product;
import entity.BestSellingProduct;
import entity.CategoryProductCount;
import entity.WarehouseCapacityReport;
import entity.LowStockProduct;
import entity.ProductRevenueReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // get all products with their quantity from inventory
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String sql =
                "SELECT p.Product_ID, p.Product_Name, p.Brand, p.Price, " +
                "p.Warranty_Period, p.Category_ID, " +
                "IFNULL(i.Quantity_Available, 0) AS Quantity " +
                "FROM Product p " +
                "LEFT JOIN Inventory i ON p.Product_ID = i.Product_ID";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Product_Name"),
                        rs.getString("Brand"),
                        rs.getDouble("Price"),
                        rs.getInt("Warranty_Period"),
                        rs.getInt("Category_ID"),
                        rs.getInt("Quantity")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // add new product to db
    public static boolean addProduct(Product product) {
        String sql =
                "INSERT INTO Product " +
                "(Product_Name, Brand, Price, Warranty_Period, Category_ID) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,product.getProductName());
            ps.setString(2,product.getBrand());
            ps.setDouble(3,product.getPrice());
            ps.setInt(4,product.getWarrantyPeriod());
            ps.setInt(5,product.getCategoryId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // update product info
    public static boolean updateProduct(Product product) {
        String sql =
                "UPDATE Product SET " +
                "Product_Name = ?, Brand = ?, Price = ?, " +
                "Warranty_Period = ?, Category_ID = ? " +
                "WHERE Product_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,product.getProductName());
            ps.setString(2,product.getBrand());
            ps.setDouble(3,product.getPrice());
            ps.setInt(4,product.getWarrantyPeriod());
            ps.setInt(5,product.getCategoryId());
            ps.setInt(6,product.getProductId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete product by id
    public static boolean deleteProduct(int productId) {
        String sql = "DELETE FROM Product WHERE Product_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1,productId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // get products that price more than given value
    public static List<Product> getProductsAbovePrice(double price) {
        List<Product> products = new ArrayList<>();

        String sql =
                "SELECT p.Product_ID, p.Product_Name, p.Brand, p.Price, " +
                "p.Warranty_Period, p.Category_ID, " +
                "IFNULL(i.Quantity_Available, 0) AS Quantity " +
                "FROM Product p " +
                "LEFT JOIN Inventory i ON p.Product_ID = i.Product_ID " +
                "WHERE p.Price > ? " +
                "ORDER BY p.Price DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1,price);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Product_Name"),
                        rs.getString("Brand"),
                        rs.getDouble("Price"),
                        rs.getInt("Warranty_Period"),
                        rs.getInt("Category_ID"),
                        rs.getInt("Quantity")
                ));
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // top 5 products with most sales
    public static List<BestSellingProduct> getTop5BestSellingProducts() {
        List<BestSellingProduct> products = new ArrayList<>();

        String sql =
                "SELECT p.Product_ID, p.Product_Name, " +
                "SUM(od.Quantity) AS Total_Sold " +
                "FROM Product p " +
                "JOIN OrderDetails od ON p.Product_ID = od.Product_ID " +
                "GROUP BY p.Product_ID, p.Product_Name " +
                "ORDER BY Total_Sold DESC " +
                "LIMIT 5";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new BestSellingProduct(
                        rs.getInt("Product_ID"),
                        rs.getString("Product_Name"),
                        rs.getInt("Total_Sold")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // count products in each category and get average price
    public static List<CategoryProductCount> getProductsCountByCategory() {
        List<CategoryProductCount> categories = new ArrayList<>();

        String sql =
                "SELECT c.Category_Name, " +
                "COUNT(p.Product_ID) AS Product_Count, " +
                "IFNULL(AVG(p.Price), 0) AS Average_Price " +
                "FROM Category c " +
                "LEFT JOIN Product p ON c.Category_ID = p.Category_ID " +
                "GROUP BY c.Category_ID, c.Category_Name " +
                "ORDER BY Average_Price DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new CategoryProductCount(
                        rs.getString("Category_Name"),
                        rs.getInt("Product_Count"),
                        rs.getDouble("Average_Price")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // get warehouses that almost full (below 20%)
    public static List<WarehouseCapacityReport> getWarehousesBelow20PercentStorage() {
        List<WarehouseCapacityReport> warehouses = new ArrayList<>();

        String sql =
                "SELECT Warehouse_ID, Location, Maximum_Storage, Available_Storage " +
                "FROM Warehouse " +
                "WHERE Available_Storage <= (Maximum_Storage * 0.20) " +
                "ORDER BY Available_Storage ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                warehouses.add(new WarehouseCapacityReport(
                        rs.getInt("Warehouse_ID"),
                        rs.getString("Location"),
                        rs.getInt("Maximum_Storage"),
                        rs.getInt("Available_Storage")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warehouses;
    }

    // products that quantity less than minimum stock
    public static List<LowStockProduct> getLowStockProducts() {
        List<LowStockProduct> products = new ArrayList<>();

        String sql =
                "SELECT p.Product_Name, " +
                "w.Location, " +
                "i.Quantity_Available, " +
                "i.Minimum_Stock_Level " +
                "FROM Inventory i " +
                "JOIN Product p ON i.Product_ID = p.Product_ID " +
                "JOIN Warehouse w ON i.Warehouse_ID = w.Warehouse_ID " +
                "WHERE i.Quantity_Available < i.Minimum_Stock_Level " +
                "ORDER BY i.Quantity_Available ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new LowStockProduct(
                        rs.getString("Product_Name"),
                        rs.getString("Location"),
                        rs.getInt("Quantity_Available"),
                        rs.getInt("Minimum_Stock_Level")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
    public static List<ProductRevenueReport> getProductRevenueReport() {

        List<ProductRevenueReport> list = new ArrayList<>();

        String sql = """
            SELECT
                p.Product_ID,
                p.Product_Name,
                SUM(od.Line_Total) AS Sales_Revenue,
                IFNULL(SUM(r.Refund_Amount), 0) AS Return_Loss,
                SUM(od.Line_Total) -
                IFNULL(SUM(r.Refund_Amount), 0) AS Net_Revenue

            FROM Product p

            JOIN OrderDetails od
                ON p.Product_ID = od.Product_ID

            LEFT JOIN TheReturns r
                ON od.Order_ID = r.Order_ID
               AND od.Product_ID = r.Product_ID

            GROUP BY p.Product_ID, p.Product_Name

            ORDER BY Net_Revenue DESC
        """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                list.add(new ProductRevenueReport(
                        rs.getInt("Product_ID"),
                        rs.getString("Product_Name"),
                        rs.getDouble("Sales_Revenue"),
                        rs.getDouble("Return_Loss"),
                        rs.getDouble("Net_Revenue")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}