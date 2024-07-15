package Venue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VenueDatabaseSingleton {

    private static Connection connection;
    private static VenueDatabaseSingleton instance=null;
    VenueFactory venueFactory = new VenueFactory();

    private VenueDatabaseSingleton(Connection connection) {
        VenueDatabaseSingleton.connection = connection;
    }

    public static VenueDatabaseSingleton getInstance(Connection connection) {
        if (instance==null){
            instance=new VenueDatabaseSingleton(connection);
        }
        return instance;
    }

    public static VenueDatabaseSingleton getInstance() {
        return instance;
    }

    public void create(Venue venue){
        try{
            String query = "INSERT INTO venue (id, name, sponsorship, vipOnly) VALUES (?,?,?,?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(2, venue.getName());
            preparedStmt.setInt(3, venue.getSponsorship());
            preparedStmt.setInt(1,venue.getId());
            int value=0;
            if (venue.getSponsorship()>10000){
                value=1;
            }
            preparedStmt.setInt(4,value);
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public List<Venue> read(){
        List<Venue> venues = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM venue");
            while(result.next()) {
                String name =result.getString(2);
                int sponsorship=result.getInt(3);
                Venue current = venueFactory.createVenue(name,sponsorship);
                venues.add(current);
            }
            statement.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return venues;
    }

    public void update(Venue venue){
        try{
            String query = "UPDATE venue SET name = ? sponsorship = ? WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(2, venue.getName());
            preparedStmt.setInt(3, venue.getSponsorship());
            preparedStmt.setInt(1,venue.getId());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void delete(Venue venue){
        try{
            String query = "DELETE FROM venue WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, venue.getId());
            preparedStmt.execute();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}