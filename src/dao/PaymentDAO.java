package dao;

import database.DBConnection;

import java.sql.*;

public class PaymentDAO {

    public static boolean addCashPayment(Date paymentDate,double amount,String paymentStatus,int orderId,double cashReceived,double changeAmount){
        String paymentSql ="""
            INSERT INTO Payment
            (Payment_Date,Amount,Payment_Status,Order_ID)
            VALUES (?,?,?,?)
        """;
        String cashSql="""
            INSERT INTO Cash_Payment
            (Payment_ID,Cash_Received,Change_Amount)
            VALUES(?,?,?)
        """;
        try (Connection con =DBConnection.getConnection()){
            con.setAutoCommit(false);
            PreparedStatement paymentPs =con.prepareStatement(paymentSql, Statement.RETURN_GENERATED_KEYS);

            paymentPs.setDate(1,paymentDate);
            paymentPs.setDouble(2,amount);
            paymentPs.setString(3,paymentStatus);
            paymentPs.setInt(4,orderId);
            paymentPs.executeUpdate();

            ResultSet keys=paymentPs.getGeneratedKeys();

            if (keys.next()){
                int paymentId=keys.getInt(1);

                PreparedStatement cashPs=con.prepareStatement(cashSql);
                cashPs.setInt(1,paymentId);
                cashPs.setDouble(2,cashReceived);
                cashPs.setDouble(3,changeAmount);
                cashPs.executeUpdate();

                con.commit();
                return true;
            }

            con.rollback();
            return false;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addInstallmentPayment(Date paymentDate,double amount,String paymentStatus,int orderId,double downPayment,int numberOfInstallments,double monthlyAmount, double remainingBalance){
        String paymentSql ="""
            INSERT INTO Payment
            (Payment_Date, Amount, Payment_Status, Order_ID)
            VALUES(?,?,?,?)
        """;

        String installmentSql ="""
            INSERT INTO Installment_Payment
            (Payment_ID, Down_Payment, Number_Of_Installments, Monthly_Amount, Remaining_Balance)
            VALUES (?,?,?,?,?)
        """;

        try (Connection con = DBConnection.getConnection()){
            con.setAutoCommit(false);
            PreparedStatement paymentPs =
                    con.prepareStatement(paymentSql, Statement.RETURN_GENERATED_KEYS);

            paymentPs.setDate(1,paymentDate);
            paymentPs.setDouble(2,amount);
            paymentPs.setString(3,paymentStatus);
            paymentPs.setInt(4,orderId);
            paymentPs.executeUpdate();
            ResultSet keys = paymentPs.getGeneratedKeys();

            if (keys.next()){
                int paymentId=keys.getInt(1);

                PreparedStatement installmentPs=con.prepareStatement(installmentSql);
                installmentPs.setInt(1,paymentId);
                installmentPs.setDouble(2,downPayment);
                installmentPs.setInt(3,numberOfInstallments);
                installmentPs.setDouble(4,monthlyAmount);
                installmentPs.setDouble(5,remainingBalance);
                installmentPs.executeUpdate();
                con.commit();
                return true;
            }
            con.rollback();
            return false;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}