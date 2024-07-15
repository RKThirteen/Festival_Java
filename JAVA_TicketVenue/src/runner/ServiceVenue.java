package runner;

import Venue.VenueFactory;
import Venue.Venue;
import Venue.VenueDatabaseSingleton;

import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServiceVenue {

    private List<Venue> venues;

    private final VenueFactory venueFactory=new VenueFactory();

    private final VenueDatabaseSingleton vdb;
    public ServiceVenue(Connection connection) {
        vdb=VenueDatabaseSingleton.getInstance(connection);
        this.venues=vdb.read();
    }

    public void showVenues() {
        if (venues.isEmpty()) {
            System.out.println("No venues added yet");
        }
        for (Venue venue : venues) {
            System.out.println(venue.toString());
        }
    }

    private Venue getVenueFromInput(Scanner input) {
        System.out.println("Enter venue Name: ");
        String name = input.nextLine();
        for (Venue venue : venues) {
            if (venue.getName().equalsIgnoreCase(name)) {
                return venue;
            }
        }
        return null;
    }

    public void addVenue(Scanner input) throws ParseException {
        System.out.println("Name of Venue");
        String name = input.nextLine();
        System.out.println("Sponsorship amount");
        int sponsorship = Integer.parseInt(input.nextLine());
        Venue venue = venueFactory.createVenue(name, sponsorship);
        if (vdb!=null)
            vdb.create(venue);
        venues.add(venue);
    }

    public void showVenue(Scanner input) {
        Venue venue = getVenueFromInput(input);
        if (venue == null) {
            System.out.println("Venue not found");
        } else {
            System.out.println(venue.toString());
        }
    }

    public void removeVenue(Scanner input) {
        Venue venue = getVenueFromInput(input);
        if (venue != null) {
            if (vdb!=null){
                vdb.delete(venue);
            }
            venues.remove(venue);
            System.out.println("Action completed");
        } else {
            System.out.println("Venue not found");
        }
    }
}