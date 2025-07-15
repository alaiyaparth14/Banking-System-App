package Banking_System_Project;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;

    private Scanner scanner;

    public Accounts(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    //open new Account

    public long open_account(String email){
        if (!account_exit(email)){
            String open_account_query = "Insert into accounts(account_number,full_name,email,balance,security_pin)values(?,?,?,?,?)";
            System.out.print("Please enter your full name : ");
            scanner.nextLine();
            String full_name = scanner.nextLine();
            System.out.print("Please enter your initial amount : ");
            double balance= scanner.nextDouble();
            System.out.print("Please enter your security pin : ");
            scanner.nextLine();
            String security_pin = scanner.nextLine();
            try {
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected>0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account Creation Failed");
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        throw  new RuntimeException("Accounts Already Exits");
    }

    //get Account Number method
    public long getAccountNumber(String email){
        String query = "Select account_number from accounts where email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Number doesn't exit");
    }

    //generate account number
    private long generateAccountNumber(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select account_number from accounts order by account_number desc limit 1");
            if (resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            }else{
                return 10000100;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 10000100;
    }

    //account_exits method
    public boolean account_exit(String email){
        String query = "Select * from accounts where email=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
