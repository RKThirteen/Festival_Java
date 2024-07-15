package model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDatabaseSingleton {
    private static Connection connection;

    private static TicketDatabaseSingleton instance=null;
    private final TicketFactory ticketFactory = new TicketFactory();
    private TicketDatabaseSingleton(Connection connection) {
        TicketDatabaseSingleton.connection = connection;
    }

    public static TicketDatabaseSingleton getInstance(Connection connection) {
        if (instance==null){
            instance=new TicketDatabaseSingleton(connection);
        }
        return instance;
    }





    public void create(Ticket ticket){
        try{
            String query = "INSERT INTO ticket (id, price, checkedIn, bought, timePurchased, untolderId, VIPonly, ODPonly,dayAvailable) VALUES (?,1500,0,0,NULL,NULL,0,0,NULL)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,ticket.getId());
            preparedStmt.executeUpdate();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public List<Ticket> read(){
        List<Ticket> tickets = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM ticket WHERE VIPonly = 0 AND ODPonly = 0");
            while(result.next()) {
                int id=result.getInt(1);
                Ticket current = ticketFactory.createTicket(id);
                if (result.getString(5)!=null){
                    current.setTimePurchased(LocalDate.parse(result.getString(5)));
                }
                if (result.getInt(4)==1){
                    current.setBought(true);
                }
                if (result.getInt(3)==1){
                    current.setChecked_in(true);
                }
                tickets.add(current);
            }
            statement.close();
        }catch (Exception e){
            System.out.println("In ticket: " + e.toString());
        }
        return tickets;
    }

    public void checkIn(Ticket ticket){
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

    public void wasBought(Ticket ticket,Untolder u){
        try{
            String query = "UPDATE ticket SET bought = 1, timePurchased = ?, untolderId=? WHERE id = ?";
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

    public void refund(Ticket t){
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

    public void delete(Ticket ticket){
        try{
            String query = "DELETE FROM ticket WHERE id = ? and VIPonly = 0 and ODPonly = 0";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, ticket.getId());
            preparedStmt.execute();
            preparedStmt.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
