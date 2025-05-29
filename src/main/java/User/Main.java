package User;

import bankSystem.*;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    private static DatabaseManager dm = new DatabaseManager();
    private static AccountServices accountServices = new AccountServices();
    private static Transactions transactions = new Transactions();


    public static void main(String[] args) {
        boolean running_program = true;

        while (running_program) {
            Scanner sc = new Scanner(System.in);

            System.out.println("""
                    1.login
                    2.sing up
                    q.quit
                    """);
            String userCommand = sc.nextLine();
            switch (userCommand) {

                case "1":
                    login(sc);
                    break;

                case "2":
                    singUp(sc);
                    break;

                case "q":
                    running_program = false;
            }
        }

    }


    public static void singUp(Scanner sc) {
        try {
            System.out.println("enter your id number");
            int userId = Integer.parseInt(sc.nextLine());
            System.out.println("enter your first name");
            String firstName = sc.nextLine();

            System.out.println("enter your last name");
            String lastName = sc.nextLine();

            System.out.println("enter your password");
            int password = Integer.parseInt(sc.nextLine());

            if (accountServices.singUp(userId, firstName, lastName, password)) {
                accountServices.logIn(userId, password);
            }
        } catch (Exception e) {
            System.out.println("enter data properly");
        }
    }

    public static void login(Scanner sc) {

        System.out.println("enter your id number");
        int LIuserId = Integer.parseInt(sc.nextLine());
        System.out.println("enter your password");
        int LIpassword = Integer.parseInt(sc.nextLine());
        if (accountServices.logIn(LIuserId, LIpassword)) {

            boolean loggedIn = true;
            while (loggedIn) {
                accountServices.returnAccount();
                System.out.println("""
                        1.transaction
                        2.create new account
                        q.logout
                        """);
                String command = sc.nextLine();
                switch (command) {
                    case "1":
                        transaction(sc);
                        break;
                    case "2":
                        createAccount();
                        break;
                    case "q":
                        loggedIn = false;
                        break;
                }
            }

        }

    }

    public static void transaction(Scanner sc) {

        try {
            System.out.println("enter your account to send from: ");
            int fromAccount = Integer.parseInt(sc.nextLine());
            System.out.println("enter recipient account to send from: ");
            int toAccount = Integer.parseInt(sc.nextLine());
            System.out.println("enter the amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            try {
                transactions.makeTransaction(fromAccount, toAccount, amount);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            System.out.println("enter the data properly");
        }
    }

    public static void createAccount() {
        accountServices.insertAccount();
    }


}
