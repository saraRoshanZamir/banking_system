package bankSystem;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String USE_SCHEMA = "USE banking_system";
    private static HikariDataSource dataSource;


    public DatabaseManager() {
        try {
            HikariConfig tempConfig = new HikariConfig();
            tempConfig.setJdbcUrl("jdbc:mysql://localhost:3306/");
            tempConfig.setUsername(System.getenv("user"));
            tempConfig.setPassword(System.getenv("pass"));
            tempConfig.setMaximumPoolSize(1);

            try (HikariDataSource tempDataSource = new HikariDataSource(tempConfig);
                 Connection conn = tempDataSource.getConnection();
                 Statement st = conn.createStatement()) {


                st.execute("CREATE DATABASE IF NOT EXISTS banking_system ");
                st.execute(USE_SCHEMA);

                setupSchema(conn);
            }

            //new connection for schema
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/banking_system");
            config.setUsername(System.getenv("user"));
            config.setPassword(System.getenv("pass"));
            config.setMaximumPoolSize(10);

            dataSource = new HikariDataSource(config);
        } catch (SQLException e) {
            throw new RuntimeException("Database initialization failed: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }



    private void setupSchema(Connection conn) throws SQLException {
        System.out.println("Setting up schema...");

        String createUsers = """
                CREATE TABLE IF NOT EXISTS user (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT UNIQUE,
                    first_name VARCHAR(20) NOT NULL,
                    last_name VARCHAR(20) NOT NULL,
                    pin INT NOT NULL
                );
                """;

        String createAccounts = """
                CREATE TABLE IF NOT EXISTS accounts (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT NOT NULL,
                    balance REAL NOT NULL DEFAULT 50,
                    FOREIGN KEY (user_id) REFERENCES user(id)
                );
                """;

        String createTransactions = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    from_account INT,
                    to_account INT,
                    amount REAL NOT NULL,
                    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                    version INT DEFAULT 0,
                    FOREIGN KEY (from_account) REFERENCES accounts(id),
                    FOREIGN KEY (to_account) REFERENCES accounts(id)
                );
                """;

        try (Statement st = conn.createStatement()) {
            st.execute(createUsers);
            st.execute(createAccounts);
            st.execute(createTransactions);
            System.out.println("✅ Schema setup complete.");
        }
    }
}
