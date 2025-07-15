package Banking_System_Project;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {

    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String userName = "root";
    private static final String password = "User@123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
         try {
             Class.forName("com.mysql.cj.jdbc.Driver");
         }catch (ClassNotFoundException e){
             System.out.println(e.getMessage());
         }

         try {
             Connection connection = DriverManager.getConnection(url, userName, password);
             Scanner scanner = new Scanner(System.in);
             User user = new User(connection, scanner);
             Accounts accounts = new Accounts(connection, scanner);
             AccountManager accountManager = new AccountManager(connection, scanner);

             String email;
             long account_number;

             while (true) {
                 System.out.println("\n=====Welcome to Bank Application Created By Parth Alaiya======");
                 System.out.println("");
                 System.out.println("==>Here You can select certain options!<===");
                 System.out.println("1. Register");
                 System.out.println("2. Login");
                 System.out.println("3. Exit");
                 System.out.print("Enter your choice : ");
                 int choice1 = scanner.nextInt();
                 switch (choice1) {
                     case 1:
                         user.register();
                         break;
                     case 2:
                         email = user.login();
                         if (email != null) {
                             System.out.println();
                             System.out.println("User Successfully Loged In..");
                             if (!accounts.account_exit(email)) {
                                 System.out.println();
                                 System.out.println("Now Next step to what you want? ");
                                 System.out.println();
                                 System.out.println("1. Open New Bank Account");
                                 System.out.println("2. Exit");
                                 System.out.println();
                                 System.out.println("Please enter your choice : ");
                                 if (scanner.nextInt() == 1) {
                                     account_number = accounts.open_account(email);
                                     System.out.println("New Account Created Succesfully");
                                     System.out.println("Your account number is : " + account_number);
                                 } else {
                                     break;
                                 }
                             }
                             account_number = accounts.getAccountNumber(email);
                             int choice2 = 0;
                             while (choice2 != 5) {
                                 System.out.println();
                                 System.out.println("Now next which type of operation you want to choose");
                                 System.out.println("1. Debit Money");
                                 System.out.println("2. Credit Money");
                                 System.out.println("3. Transfer Money");
                                 System.out.println("4. Check Balance");
                                 System.out.println("5. Log out");
                                 System.out.println("Enter your choice : ");
                                 choice2 = scanner.nextInt();
                                 switch (choice2) {
                                     case 1:
                                         accountManager.debitMoney(account_number);
                                         break;
                                     case 2:
                                         accountManager.creditMoney(account_number);
                                         break;
                                     case 3:
                                         accountManager.transfer_money(account_number);
                                         break;
                                     case 4:
                                         accountManager.getBalance(account_number);
                                         break;
                                     case 5:
                                         break;
                                     default:
                                         System.out.println("Enter Valid Choice!");
                                         break;
                                 }
                             }
                         }
                         else {
                             System.out.println("Incorrect email or password");
                         }
                     case 3:
                         System.out.println("======THANK YOU FOR USING OUR BANK APPLICATION========");
                         System.out.println("Existing System.......");
                         return;
                     default:
                         System.out.println("Enter Valid Choice");
                         break;
                 }
             }
         }catch (SQLException e){
             System.out.println(e.getMessage());
         }
    }
}
