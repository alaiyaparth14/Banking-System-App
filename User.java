package Banking_System_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection connection;

    private Scanner scanner;

    public User(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    //register User method
    public void register() throws SQLException{
        System.out.print("Please enter your full name : ");
        scanner.nextLine();
        String full_name= scanner.nextLine();
        System.out.print("Please enter your mail id : ");
        String email = scanner.nextLine();
        System.out.print("Please enter your password : ");
        String password = scanner.nextLine();
        if (user_exits(email)){
            System.out.println("User already exits for this email address!");
            return;
        }
        String register_query = "Insert into User(full_name,email,password)values(?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("========    Registration Successfully===========");
            }else{
                System.out.println("Registration failed!");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //login method
    public String login(){
        System.out.print("Please enter your mail id : ");
        scanner.nextLine();
        String email = scanner.nextLine();
        System.out.print("Please enter your password : ");
        String password = scanner.nextLine();
        String login_query = "Select * from User where email=? and password=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return email;
            }else{
                return null;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
         return null;
    }

    //user_exits method
    public boolean user_exits(String email){
        String query = "Select * from User where email=?";

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
