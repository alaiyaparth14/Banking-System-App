package Banking_System_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;

    private Scanner scanner;

    public AccountManager(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    //credit money
    public void creditMoney(long account_numnber) throws SQLException {
        System.out.println("Please enter your amount you want to credit : ");
        double balance = scanner.nextDouble();
        System.out.println("Please enter your security pin : ");
        scanner.nextLine();
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_numnber!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,account_numnber);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    String credit_query = "Update accounts set balance=balance+? where account_number=?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1,balance);
                    preparedStatement1.setLong(2,account_numnber);
                    int rowAffected = preparedStatement1.executeUpdate();
                    if (rowAffected>0){
                        System.out.println("Rs " + balance + "credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("Transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security pin");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
         connection.setAutoCommit(true);
    }

    //debit money
    public void debitMoney(long account_number) throws SQLException{
        System.out.print("Please enter your amount you want to debit : ");
        double amount = scanner.nextDouble();
        System.out.print("Please enter your security pin : ");
        scanner.nextLine();
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query = "update accounts set balance = balance- ? where account_number=?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowAffected = preparedStatement1.executeUpdate();
                        if (rowAffected>0){
                            System.out.println("Rs " + amount + " debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient balance");
                    }
                }else{
                    System.out.println("Invalid Pin!");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    //tranfer money
    public void transfer_money(long sender_account_number) throws SQLException {
        System.out.println("Please enter receiver account number : ");
        long receiver_account_number = scanner.nextLong();
        System.out.println("Please enter your amount : ");
        double amount = scanner.nextDouble();
        System.out.println("Please enter your security pin : ");
        scanner.nextLine();
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("Select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    double current_balance = resultSet.getLong("balance");
                    if (amount<=current_balance){
                        String debit_query = "update accounts set balance=balance-? where account_number=?";
                        String credit_query = "update accounts set balance=balance+> where account_number=?";
                        PreparedStatement creditPreparedStatment = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatment = connection.prepareStatement(debit_query);
                        creditPreparedStatment.setDouble(1,amount);
                        creditPreparedStatment.setLong(2,receiver_account_number);
                        debitPreparedStatment.setDouble(1,amount);
                        debitPreparedStatment.setLong(2,sender_account_number);
                        int rowAffected1 = creditPreparedStatment.executeUpdate();
                        int rowAffected2 = debitPreparedStatment.executeUpdate();
                        if (rowAffected1>0 && rowAffected2>0){
                            System.out.println("Transaction Successfull");
                            System.out.println("Rs : " + amount + "Transfered Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient balance");
                    }
                }else{
                    System.out.println("Invalid Security Pin");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    //get balance
    public void getBalance(long account_number){
        System.out.print("Please enter your security pin : ");
        scanner.nextLine();
        String security_pin = scanner.nextLine();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select balance from accounts where account_number=? and security_pin=?");
            preparedStatement.setDouble(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                double balance  = resultSet.getDouble("balance");
                System.out.println("In your account balance is  : " + balance);
            }else{
                System.out.println("Invalid pin");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
