package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

public class ArtistDatabaseSingleton {

    private static Connection connection;
    private static ArtistDatabaseSingleton instance=null;
    private final PersonFactory personFactory = new PersonFactory();

    private ArtistDatabaseSingleton(Connection connection) {
        ArtistDatabaseSingleton.connection = connection;
    }

    public static ArtistDatabaseSingleton getInstance(Connection connection) {
        if (instance==null){
            instance=new ArtistDatabaseSingleton(connection);
        }
        return instance;
    }

    public void create(Artist a){
        try{
            String query = "INSERT INTO artist (CNP, name, emailAddress, salary, noOfFans) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1,a.getCNP());
            preparedStmt.setString(2,a.getName());
            preparedStmt.setString(3,a.getEmail_address());
            preparedStmt.setDouble(4,a.getSalary());
            preparedStmt.setInt(5,a.getNoOfFans());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public Set<Artist> read(){
        Set<Artist> artists = new TreeSet<>(new ArtistComparator());
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM artist");
            while(result.next()) {
                String CNP=result.getString(1);
                String name=result.getString(2);
                String emailAddress=result.getString(3);
                double salary=result.getDouble(4);
                int noOfFans=result.getInt(5);
                Artist current = personFactory.createArtist(name, CNP, emailAddress,salary,noOfFans);
                artists.add(current);
            }
            statement.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return artists;
    }

    public void delete(Artist a){
        try{
            String query = "DELETE FROM artist WHERE CNP = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, a.getCNP());
            preparedStmt.execute();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
