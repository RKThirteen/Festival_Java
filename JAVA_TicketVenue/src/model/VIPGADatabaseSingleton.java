package model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VIPGADatabaseSingleton {
    private static Connection connection;
    private static VIPGADatabaseSingleton instance=null;
    private final TicketFactory ticketFactory = new TicketFactory();

    private VIPGADatabaseSingleton(Connection connection) {
        VIPGADatabaseSingleton.connection = connection;
    }

    public static VIPGADatabaseSingleton getInstance(Connection connection) {
        if (instance==null){
            instance=new VIPGADatabaseSingleton(connection);
        }
        return instance;
    }

    public void create(VIPGeneralAccess ticket){
        try{
            String query = "INSERT INTO ticket (id, price, checkedIn, bought, timePurchased, untolderId, VIPonly, ODPonly,dayAvailable) VALUES (?,3000,0,0,NULL,NULL,1,0,NULL)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,ticket.getId());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public List<VIPGeneralAccess> read(){
        List<VIPGeneralAccess> tickets = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM ticket WHERE VIPonly = 1 AND ODPonly = 0");
            while(result.next()) {
                int id=result.getInt(1);
                VIPGeneralAccess current = ticketFactory.createVIPTicket(id);
                if (result.getString(5)!=null)
                    current.setTimePurchased(LocalDate.parse(result.getString(5)));
                if (result.getInt(3)==1){
                    current.setChecked_in(true);
                }
                if (result.getInt(4)==1){
                    current.setBought(true);
                }
                tickets.add(current);
            }
            statement.close();
        }catch (Exception e){
            System.out.println("In VIPticket "+e.toString());
        }
        return tickets;
    }

    public void checkIn(VIPGeneralAccess ticket){
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

    public void wasBought(VIPGeneralAccess ticket,Untolder u){
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

    public void refund(VIPGeneralAccess t){
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

    public void delete(VIPGeneralAccess ticket){
        try{
            String query = "DELETE FROM ticket WHERE id = ? and VIPonly = 1";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, ticket.getId());
            preparedStmt.execute();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}

