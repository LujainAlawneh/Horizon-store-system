package dao;

import database.DBConnection;
import entity.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO{

    public static List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();

        String sql = """
            SELECT
                s.Supplier_ID,
                s.Supplier_Name,
                s.Supplier_Address,
                s.Company_Name,
                s.Email,
                sp.Supplier_Phone
            FROM Supplier s
            LEFT JOIN SupplierPhone sp
                ON s.Supplier_ID = sp.Supplier_ID
            ORDER BY s.Supplier_Name
        """;

        try (Connection conn=DBConnection.getConnection();
             Statement stmt=conn.createStatement();
             ResultSet rs=stmt.executeQuery(sql)){
            while (rs.next()){
                suppliers.add(new Supplier(
                        rs.getInt("Supplier_ID"),
                        rs.getString("Supplier_Name"),
                        rs.getString("Supplier_Address"),
                        rs.getString("Company_Name"),
                        rs.getString("Email"),
                        rs.getString("Supplier_Phone")
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return suppliers;
    }

    public static boolean addSupplier(Supplier supplier) {
        String supplierSql ="""
            INSERT INTO Supplier
            (Supplier_Name,Supplier_Address,Company_Name,Email)
            VALUES(?,?,?,?)
        """;

        String phoneSql ="""
            INSERT INTO SupplierPhone
            (Supplier_ID,Supplier_Phone)
            VALUES (?,?)
        """;

        try (Connection conn =DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int newSupplierId;

            try (PreparedStatement ps =conn.prepareStatement(supplierSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,supplier.getSupplierName());
                ps.setString(2,supplier.getSupplierAddress());
                ps.setString(3,supplier.getCompanyName());
                ps.setString(4,supplier.getEmail());
                
                int rows=ps.executeUpdate();
                if (rows==0){
                    conn.rollback();
                    return false;
                }

                try (ResultSet keys=ps.getGeneratedKeys()){
                    if (keys.next()){
                        newSupplierId=keys.getInt(1);
                    } else{
                        conn.rollback();
                        return false;
                    }
                }
            }

            if (supplier.getPhoneNumber() !=null && !supplier.getPhoneNumber().trim().isEmpty()){
                try (PreparedStatement psPhone =conn.prepareStatement(phoneSql)){
                    psPhone.setInt(1,newSupplierId);
                    psPhone.setString(2,supplier.getPhoneNumber().trim());
                    psPhone.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateSupplier(Supplier supplier){
        String supplierSql ="""
            UPDATE Supplier SET
                Supplier_Name =?,
                Supplier_Address =?,
                Company_Name =?,
                Email =?
            WHERE Supplier_ID =?
        """;

        String deletePhoneSql="DELETE FROM SupplierPhone WHERE Supplier_ID = ?";

        String insertPhoneSql="""
            INSERT INTO SupplierPhone
            (Supplier_ID,Supplier_Phone)
            VALUES (?,?)
        """;

        try (Connection conn=DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps=conn.prepareStatement(supplierSql)) {
                ps.setString(1,supplier.getSupplierName());
                ps.setString(2,supplier.getSupplierAddress());
                ps.setString(3,supplier.getCompanyName());
                ps.setString(4,supplier.getEmail());
                ps.setInt(5,supplier.getSupplierId());

                if (ps.executeUpdate() ==0){
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement psDeletePhone =conn.prepareStatement(deletePhoneSql)) {
                psDeletePhone.setInt(1,supplier.getSupplierId());
                psDeletePhone.executeUpdate();
            }

            if (supplier.getPhoneNumber() != null && !supplier.getPhoneNumber().trim().isEmpty()) {
                try (PreparedStatement psInsertPhone = conn.prepareStatement(insertPhoneSql)) {
                    psInsertPhone.setInt(1,supplier.getSupplierId());
                    psInsertPhone.setString(2,supplier.getPhoneNumber().trim());
                    psInsertPhone.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM Supplier WHERE Supplier_ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, supplierId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}