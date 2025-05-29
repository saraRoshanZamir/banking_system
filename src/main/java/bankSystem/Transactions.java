package bankSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transactions {

    public double getBalance(Connection conn, int accId) throws SQLException {
        String balanceQuery = "SELECT balance FROM accounts WHERE id = ?";
        PreparedStatement balancePS = conn.prepareStatement(balanceQuery);
        balancePS.setInt(1, accId);
        ResultSet balanceRS = balancePS.executeQuery();
        balanceRS.next();
        double balance = balanceRS.getDouble("balance");
        return balance;
    }

    public void makeTransaction(int fromAccount, int toAccount, double amount) throws SQLException {

        if (bankSystem.AccountServices.getAccountIds().contains(fromAccount)) {
            String toAccountExistQuery = "SELECT * From accounts WHERE id = ?";
            Connection conn = DatabaseManager.getConnection();
            try {
                PreparedStatement toAccountPS = conn.prepareStatement(toAccountExistQuery);
                toAccountPS.setInt(1, toAccount);
                ResultSet toAccountRS = toAccountPS.executeQuery();
                if (toAccountRS.isBeforeFirst()) {

                    String lockQuery = "SELECT * FROM accounts WHERE id IN (?,?) FOR UPDATE";

                    conn.setAutoCommit(false);

                    PreparedStatement lockAccPS = conn.prepareStatement(lockQuery);
                    int firstAcc = Math.min(fromAccount, toAccount);
                    int secondAcc = Math.max(fromAccount, toAccount);

                    lockAccPS.setInt(1, firstAcc);
                    lockAccPS.setInt(2, secondAcc);


                    double fromAccBalance = getBalance(conn, fromAccount);
                    double toAccBalance = getBalance(conn, toAccount);


                    if (amount > fromAccBalance) {
                        System.out.println("Insufficient balance!");
                        conn.rollback();
                        return;
                    } else {
                        fromAccBalance -= amount;
                        toAccBalance += amount;
                        System.out.println("Transaction completed successfully.");
                        String updateQuery = "UPDATE accounts SET balance = ? WHERE id = ?";
                        PreparedStatement updatePS = conn.prepareStatement(updateQuery);

                        updatePS.setDouble(1, fromAccBalance);
                        updatePS.setInt(2, fromAccount);
                        updatePS.executeUpdate();

                        updatePS.setDouble(1, toAccBalance);
                        updatePS.setInt(2, toAccount);
                        updatePS.executeUpdate();

                    }
                    conn.commit();
                    conn.setAutoCommit(true);

                } else {
                    System.out.println("recipient account does not exist!");
                }

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction failed: " + e.getMessage());

            }

        } else {
            System.out.println("user doesn't own the entered account id");
        }

    }

}

