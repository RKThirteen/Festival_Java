package model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ODPDatabaseSingleton {
    private static Connection connection;

    private static ODPDatabaseSingleton instance=null;
    private final TicketFactory ticketFactory = new TicketFactory();

    private ODPDatabaseSingleton(Connection connection) {
        ODPDatabaseSingleton.connection = connection;
    }

    public static ODPDatabaseSingleton getInstance(Connection connection) {
        if (instance==null){
            instance=new ODPDatabaseSingleton(connection);
        }
        return instance;
    }


    public void create(OneDayPass ticket){
        try{
            String query = "INSERT INTO ticket (id, price, checkedIn, bought, timePurchased, untolderId, VIPonly, ODPonly,dayAvailable) VALUES (?,3000,0,0,NULL,NULL,0,1,?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,ticket.getId());
            preparedStmt.setString(2,ticket.getDayAvailable());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public List<OneDayPass> read(){
        List<OneDayPass> tickets = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM ticket WHERE VIPonly = 0 AND ODPonly = 1");
            while(result.next()) {
                int id=result.getInt(1);
                String dayAvailable=result.getString(9);
                OneDayPass current = ticketFactory.createOneDayPass(id,dayAvailable);
                if (result.getString(5)!=null)
                    current.setTimePurchased(LocalDate.parse(result.getString(5)));
                if (result.getInt(3)==1){
                    current.setBought(true);
                }
                if (result.getInt(4)==1){
                    current.setChecked_in(true);
                }
                tickets.add(current);
            }
            statement.close();
        }catch (Exception e){
            System.out.println("In odp: "+ e.toString());
        }
        return tickets;
    }

    public void checkIn(OneDayPass ticket){
        try{
            String query = "UPDATE ticket SET checkedIn = 1 WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,ticket.getId());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void wasBought(OneDayPass ticket,Untolder u){
        try{
            String query = "UPDATE venue SET bought = 1, timePurchased = ?, untolderId = ? WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, String.valueOf(LocalDateTime.now()));
            preparedStmt.setInt(2,u.getUntolderId());
            preparedStmt.setInt(3,ticket.getId());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void refund(OneDayPass t){
        try{
            String query = "UPDATE ticket SET bought = 0, timePurchased = NULL, untolderId= NULL WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, t.getId());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void delete(OneDayPass ticket){
        try{
            String query = "DELETE FROM ticket WHERE id = ? and ODPonly = 1";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, ticket.getId());
            preparedStmt.execute();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}

