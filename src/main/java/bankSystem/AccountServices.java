package bankSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountServices {
    private final static int DUPLICATE_ENTRY = 1062;
    private int userId;
    public int Account_userId;
    public static List<Integer> accountIds = new ArrayList<>();


    public static List<Integer> getAccountIds() {
        return accountIds;
    }


    public boolean singUp(int userId, String firstName, String lastName, int pin) {
        if (firstName.length() <= 20 && lastName.length() <= 20) {

            String insertUser = """
                    INSERT INTO user ( user_id, first_name, last_name, pin)
                     VALUES (?, ?, ?, ?);
                    """;

            try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(insertUser)) {
                ps.setInt(1, userId);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setInt(4, pin);
                int updates = ps.executeUpdate();

                if (updates > 0) {
                    System.out.println("user added successfully");
                    return true;
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == DUPLICATE_ENTRY) {
                    System.out.println("user is already in the database");

                } else {
                    System.err.println("Message: " + e.getMessage());
                }

            }
        } else {
            System.out.println("firstname and last name must be 20 character maximum");

        }
        return false;
    }

    public boolean logIn(int userid, int pin) {

        String login = "SELECT * FROM user WHERE user_id=? AND pin=? ";
        try (Connection conn = DatabaseManager.getConnection()) {

            PreparedStatement LoginPS = conn.prepareStatement(login);
            LoginPS.setInt(1, userid);
            LoginPS.setInt(2, pin);
            ResultSet loginRS = LoginPS.executeQuery();

            if (!loginRS.isBeforeFirst()) {
                System.out.println("account does not exist");
                return false;
            } else {

                while (loginRS.next()) {
                    String firstName = loginRS.getString("first_name");
                    String lastName = loginRS.getString("last_name");
                    Account_userId = loginRS.getInt("id");
                    System.out.println("welcome %s %s".formatted(firstName, lastName));
                }
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnAccount() {
        for (Accounts acc : Accounts.getAccounts(Account_userId)) {
            accountIds.add(acc.getId());
            System.out.print(acc);
        }
    }

    public void insertAccount() {

        String insertUser = """
                INSERT INTO accounts (user_id)
                 VALUES (?);
                """;

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement
                    (insertUser, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Account_userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int accountId = rs.getInt(1);
                System.out.println("account created successfully. Account id: " + accountId);
            }
        } catch (SQLException e) {
            System.err.println("Message: " + e.getMessage());
        }
    }
}



