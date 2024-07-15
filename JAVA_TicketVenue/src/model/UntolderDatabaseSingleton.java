package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class UntolderDatabaseSingleton {
    private static Connection connection;

    private static UntolderDatabaseSingleton instance=null;
    private final PersonFactory personFactory = new PersonFactory();

    private UntolderDatabaseSingleton(Connection connection) {
        UntolderDatabaseSingleton.connection = connection;
    }

    public static UntolderDatabaseSingleton getInstance(Connection connection) {
        if (instance==null){
            instance=new UntolderDatabaseSingleton(connection);
        }
        return instance;
    }


    public void create(Untolder u){
        try{
            String query = "INSERT INTO untolder (id, CNP, name, emailAddress, balance) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, u.getUntolderId());
            preparedStmt.setString(2,u.getCNP());
            preparedStmt.setString(3,u.getName());
            preparedStmt.setString(4,u.getEmail_address());
            preparedStmt.setDouble(5,u.getBalance());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public Set<Untolder> read(){
        Set<Untolder> untolders = new TreeSet<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM untolder");
            Untolder current=null;
            while(result.next()) {
                int id=result.getInt(1);
                String CNP=result.getString(2);
                String name=result.getString(3);
                String emailAddress=result.getString(4);
                double balance=result.getDouble(5);
                current = personFactory.createUntolder(id,name,CNP,emailAddress,balance);
                current.setBought(addTickets(current));
                untolders.add(current);
            }
            statement.close();

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return untolders;
    }

    List<Integer> addTickets(Untolder u) {
        List<Integer> tickets = new ArrayList<>();
        try{
            String query = "SELECT * from ticket WHERE untolderId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,u.getUntolderId());
            ResultSet result2=preparedStatement.executeQuery();

            while (result2.next()){
                int id=result2.getInt(1);
                tickets.add(id);

            }
        } catch (Exception e){
        System.out.println(e.toString());
        }
        return tickets;
    }



    public void topUp(Untolder u){
        try{
            String query = "UPDATE untolder SET balance = ? WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setDouble(1,u.getBalance());
            preparedStmt.setInt(2, u.getUntolderId());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void delete(Untolder u){
        try{
            String queryTicket = "UPDATE ticket SET checkedIn = 0, bought = 0, timePurchased = NULL, untolderId = NULL WHERE untolderId = ?";
            PreparedStatement preparedStatement= connection.prepareStatement(queryTicket);
            preparedStatement.setInt(1,u.getUntolderId());
            preparedStatement.execute();
            preparedStatement.close();
            String query = "DELETE FROM untolder WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, u.getUntolderId());
            preparedStmt.execute();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
