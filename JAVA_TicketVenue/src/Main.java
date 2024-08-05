import Decommissioned.Service;
import runner.ServicePerson;
import runner.ServiceTicket;
import runner.ServiceVenue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final String DB_URL = "jdbc:mysql://localhost:3307/untolddb";
    static final String USER = "root";
    static final String PASS = "ABC!23";
    static List<String> commands= Arrays.asList("List Untolders","Show Untolder","List Artists","Show Artist","Remove Artist","Add Person","List Venues","Add Venue","Show Venue","Remove Venue","Generate Tickets","List Tickets","Show Ticket","Refund Ticket","Buy Ticket","Check In","Remove Untolder","Top Up","Help","Exit");
    static List<String> description=Arrays.asList("List of all Untolders","Look up specific Untolder","List of all artists","Look up specific artist","Remove artist from database","Add Person(you will be prompted to create them as Untolder or Artist)",
                                                    "List of all Venues","Look up specific Venue","Add Venue to database","Remove Venue from database","Generate Tickets, VIP Tickets and One Day Passes at once","List all available tickets, or only a category",
                                                    "Show specific Ticket","Refund a ticket that was bought","Buy a ticket using its ID","Check In, thus making the purchase permanent","Remove an untolder",
                                                    "Add balance to an Untolder's digital wallet","The list of all available commands","Exit the application");
    static void printCommands(){
        System.out.println("Here is the list of available commands: ");
        for (int i=0;i<commands.size();i++){
            String str=commands.get(i)+": "+description.get(i);
            System.out.println(str);
        }
        System.out.println("To use, type in the command exactly as it is shown");
    }
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    public static void main(String[] args) throws Exception{
        Scanner input=new Scanner(System.in);
        boolean running=true;
        Connection connection=Main.getConnection();

        ServicePerson servicePerson=ServicePerson.getInstance(connection);
        ServiceTicket serviceTicket=ServiceTicket.getInstance(connection);
        ServiceVenue serviceVenue=new ServiceVenue(connection);

        AuditService auditService=new AuditService();

        while (running){
            System.out.println("Enter command: (Help to see all available commands)");
            String command=input.nextLine();
            switch(command){
                case "List Untolders":
                    servicePerson.showUntolders();
                    break;
                case "List Artists":
                    servicePerson.showArtists();
                    break;
                case "List Venues":
                    serviceVenue.showVenues();
                    break;
                case "Main Stage":
                    servicePerson.showMainStage();
                    break;
                case "Add to Main Stage":
                    servicePerson.addToMainStage(input);
                    break;
                case "List Tickets":
                    serviceTicket.showTickets(input);
                    break;
                case "Show Untolder":
                    servicePerson.showUntolder(input);
                    break;
                case "Show Artist":
                    servicePerson.showArtist(input);
                    break;
                case "Remove Artist":
                    servicePerson.removeArtist(input);
                    break;
                case "Show Ticket":
                    serviceTicket.showTicket(input);
                    break;
                case "Add Venue":
                    serviceVenue.addVenue(input);
                    break;
                case "Show Venue":
                    serviceVenue.showVenue(input);
                    break;
                case "Remove Venue":
                    serviceVenue.removeVenue(input);
                    break;
                case "Generate Tickets":
                    serviceTicket.generateAllTickets(input);
                    break;
                case "Add Person":
                    servicePerson.addPerson(input);
                    break;
                case "Buy Ticket":
                    serviceTicket.buyTicket(input);
                    break;
                case "Check In":
                    serviceTicket.checkIn(input);
                    break;
                case "Refund Ticket":
                    serviceTicket.refundTicket(input);
                    break;
                case "Remove Untolder":
                    servicePerson.removeUntolder(input);
                    break;
                case "Top Up":
                    servicePerson.topUp(input);
                    break;
                case "Help":
                    printCommands();
                    break;
                case "Exit":
                    System.out.println("Quitting application");
                    running=false;
                    break;
                default:
                    System.out.println("No command found");
                    break;
            }
            if (commands.contains(command)){
                auditService.logAction(command);
            }
        }
    }
}
