package Decommissioned;

import Venue.Venue;
import model.*;

import java.util.*;

public class Service {
    private Set<Untolder> untolders = new TreeSet<>();
    private Set<Artist> artists = new TreeSet<>(new ArtistComparator());
    private List<Ticket> tickets= new ArrayList<>();
    private final TicketFactory ticketFactory=new TicketFactory();
    private final PersonFactory personFactory=new PersonFactory();
    private MainStage mainStage=MainStage.getInstance();

    public Set<Untolder> getUntolders() {
        return untolders;
    }

    public void setUntolders(Set<Untolder> untolders) {
        this.untolders = untolders;
    }

    public Set<Artist> getEmployees() {
        return artists;
    }

    public void setEmployees(Set<Artist> artists) {
        this.artists = artists;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }


    public Service(){
    }


//    public void getTotalProfit(){
//        double total_profit=0;
//        for (Ticket ticket: tickets){
//            if (ticket.isBought()){
//                total_profit+=ticket.getPrice();
//            }
//
//        }
//        for (Venue venue : this.venues){
//            total_profit+= venue.getSponsorship();
//        }
//        for (Artist artist: this.artists){
//            total_profit-= artist.getSalary();
//        }
//
//        System.out.println("Estimated profit:"+total_profit);
//    }

}
