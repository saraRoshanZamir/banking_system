package bankSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Accounts {
    private int userId;
    private int id;
    private double balance;

    public Accounts(int userId, int id, double balance) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public static List<Accounts> getAccounts(int userId) {
        List<Accounts> accounts = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()) {
            String accountQuery = "SELECT * from accounts WHERE user_id =?";
            PreparedStatement st = conn.prepareStatement(accountQuery);
            st.setInt(1, userId);
            ResultSet RS = st.executeQuery();

            while (RS.next()) {
                int id = RS.getInt("id");
                double balance = RS.getDouble("balance");

                Accounts account = new Accounts(userId, id, balance);
                accounts.add(account);

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return accounts;
    }
    @Override
    public String toString() {
        return """
                 id: %d
                 balance: %.3f
                 -------------
                """.formatted(id,balance);

    }
}

