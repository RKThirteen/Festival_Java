package Venue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class VenueFactory {

    private static int id=0;
    public static void idIncrementer(int inc){
        VenueFactory.id+=inc;
    }
    public Venue createVenue(String name, int sponsorship) throws ParseException{
        return new Venue(++id,name,sponsorship);
    }

}
