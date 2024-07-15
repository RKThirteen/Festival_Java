package runner;

import model.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServiceTicket {

    private static ServiceTicket instance=null;
    private List<Ticket> tickets= new ArrayList<>();

    private List<VIPGeneralAccess> VIPTickets = new ArrayList<>();

    private List<OneDayPass> oneDayPasses = new ArrayList<>();

    private List<Ticket> allTickets = new ArrayList<>();
    private final TicketFactory ticketFactory=new TicketFactory();

    private final TicketDatabaseSingleton tdb;
    private final VIPGADatabaseSingleton vdb;
    private final ODPDatabaseSingleton odb;

    private ServicePerson servicePerson=null;

    private final MainStage mainStage=MainStage.getInstance();

    public List<Ticket> getTickets() {
        return allTickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public static ServiceTicket getInstance(Connection connection){
        if (instance==null){
            instance=new ServiceTicket(connection);
        }
        return instance;
    }

    public static ServiceTicket getInstance(){
        return instance;
    }
    public ServiceTicket(Connection connection){
        tdb=TicketDatabaseSingleton.getInstance(connection);
        vdb=VIPGADatabaseSingleton.getInstance(connection);
        odb=ODPDatabaseSingleton.getInstance(connection);

        servicePerson= ServicePerson.getInstance(connection);

        tickets=tdb.read();
        VIPTickets=vdb.read();
        oneDayPasses=odb.read();
        allTickets.addAll(tickets);
        allTickets.addAll(VIPTickets);
        allTickets.addAll(oneDayPasses);
    }

    public void generateTickets(int counter){
        for (int i=0;i<counter;i++){
            Ticket ticket= ticketFactory.createTicket();
            tickets.add(ticket);
            if (this.tdb!=null)
                tdb.create(ticket);
        }
    }

    public void generateVIPTickets(int counter){
        for (int i=0;i<counter;i++){
            VIPGeneralAccess vip=  ticketFactory.createVIPTicket();
            VIPTickets.add(vip);
            if (this.vdb!=null)
                vdb.create(vip);
        }
    }

    public void generateOneDayPasses(int counter, String day){
        for (int i=0;i<counter;i++){
            OneDayPass odp=ticketFactory.createOneDayPass(day);
            oneDayPasses.add(odp);
            if (this.odb!=null)
                odb.create(odp);
        }

    }

    public void generateAllTickets(Scanner input){
        int counter;
        System.out.println("How many Tickets?");
        counter=Integer.parseInt(input.nextLine());
        generateTickets(counter);
        System.out.println("How many VIP Tickets?");
        counter=Integer.parseInt(input.nextLine());
        generateVIPTickets(counter);
        System.out.println("How many One Day Passes?");
        counter=Integer.parseInt(input.nextLine());
        System.out.println("What day should they be available on?");
        String day=input.nextLine();
        generateOneDayPasses(counter,day);
        System.out.println("Tickets generated");
    }

    public void showTickets(Scanner input){
        System.out.println("Select option: Tickets, VIPTickets, OneDayPasses, All");
        String option=input.nextLine();
        if (option.equalsIgnoreCase("Tickets") || option.equalsIgnoreCase("All")){
            for (Ticket ticket: tickets){
                    System.out.println(ticket.toString());
            }
        }
        if (option.equalsIgnoreCase("VIPTickets") || option.equalsIgnoreCase("All")){
            for (VIPGeneralAccess ticket: VIPTickets){
                    System.out.println(ticket.toString());
            }
        }
        if (option.equalsIgnoreCase("OneDayPasses") || option.equalsIgnoreCase("All")){
            for (OneDayPass ticket: oneDayPasses){
                if (ticket != null) {
                    System.out.println(ticket.toString());
                }
            }
        }
    }

    public Ticket getTicket(int id){
        for (Ticket ticket:allTickets){
            if (ticket.getId()==id) {
                return ticket;
            }
        }
        return null;
    }

    public void showTicket(Scanner input){
        System.out.println("Enter ticket id:");
        Ticket ticket=getTicket(Integer.parseInt(input.nextLine()));
        if (ticket==null){
            System.out.println("Ticket not found");
        }
        else{
            System.out.println(ticket.toString());
        }

    }

    public void buyTicket(Scanner input){
        System.out.println("Who is buying the ticket?");
        Untolder u=servicePerson.getUntolderFromInput(input);
        if (u==null){
            System.out.println("Untolder not found in database");
        }
        else{
            System.out.println("What ticket are you buying?");
            Ticket t=getTicket(Integer.parseInt(input.nextLine()));
            if (t==null){
                System.out.println("Ticket not found in database");
            }
            else {
                if (t.isBought()){
                    System.out.println("Ticket already bought by someone else");
                }
                else{
                    t.setBought(true);
                    t.setTimePurchased(LocalDate.now());
                    u.receiveTicket(t);
                    tdb.wasBought(t,u);
                    System.out.println("Ticket bought by "+u.getName());
                }

            }
        }
    }
    public void refundTicket(Scanner input) {
        System.out.println("Who needs the refund? (ENTER CNP)");
        Untolder u = servicePerson.getUntolderFromInput(input);
        if (u == null) {
            System.out.println("Person not found in database");
            return;
        }
        System.out.println("Which ticket needs refunding? (Enter ID)");
        Ticket t = getTicket(Integer.parseInt(input.nextLine()));
        if (t == null) {
            System.out.println("Ticket not found");
            return;
        }
        if (t.isChecked_in()) {
            System.out.println("Ticket already checked in, cannot perform refund");
            return;
        }
        if (!t.isBought()) {
            System.out.println("Ticket isn't bought");
            return;
        }
        u.removeTicket(t);
        t.setTimePurchased(null);
        tdb.refund(t);
        System.out.println("Refunded ticket");
    }


    public void checkIn(Scanner input){
        System.out.println("Who is checking in?");
        Untolder u=servicePerson.getUntolderFromInput(input);
        if (u==null){
            System.out.println("Untolder not found in database");
        }
        else{
            System.out.println("What ticket are you checking in?");
            Ticket t=getTicket(Integer.parseInt(input.nextLine()));
            if (t==null){
                System.out.println("Ticket not found in database");
                return;
            }
            if (t.isChecked_in()){
                System.out.println("Ticket already checked in");
            }
            else {
                if (t.isBought()){
                    t.setChecked_in(true);
                    t.setPerson(u);
                    tdb.checkIn(t);
                }
                else{
                    System.out.println("Cannot check-in a ticket that was not bought");
                }

            }
        }

    }


    public void refund(Ticket t){
        tdb.refund(t);
    }

    public void refund(VIPGeneralAccess t){
        vdb.refund(t);
    }

    public void refund(OneDayPass t){
        odb.refund(t);
    }


    public void wasBought(Ticket t, Untolder u){
        tdb.wasBought(t,u);
    }


}
