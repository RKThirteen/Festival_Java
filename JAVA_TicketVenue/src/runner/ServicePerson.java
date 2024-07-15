package runner;

import model.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class ServicePerson {

    private static ServicePerson instance=null;
    private Set<Untolder> untolders;
    private Set<Artist> artists;

    private final PersonFactory personFactory=new PersonFactory();

    private final UntolderDatabaseSingleton udb;

    private final ArtistDatabaseSingleton adb;

    private final MainStage mainStage=MainStage.getInstance();

    public Set<Untolder> getUntolders() {
        return untolders;
    }

    public void setUntolders(Set<Untolder> untolders) {
        this.untolders = untolders;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setEmployees(Set<Artist> artists) {
        this.artists = artists;
    }

    public static ServicePerson getInstance(Connection connection){
        if (instance==null){
            instance=new ServicePerson(connection);
        }
        return instance;
    }

    public ServicePerson(Connection connection){

        adb=ArtistDatabaseSingleton.getInstance(connection);
        udb=UntolderDatabaseSingleton.getInstance(connection);

        this.artists=this.adb.read();
        this.untolders=this.udb.read();
    }

    private boolean artistAlreadyAdded(String CNP){
        for (Artist artist:artists)
            if (artist.getCNP().equalsIgnoreCase(CNP))
                return true;
        return false;
    }
    private boolean untolderAlreadyAdded(String CNP){
        for (Untolder untolder:untolders)
            if (untolder.getCNP().equalsIgnoreCase(CNP))
                return true;
        return false;
    }
    public void addPerson(Scanner input){
        System.out.println("CNP(13 digits):");
        String CNP=input.nextLine();
        System.out.println("Name:");
        String name=input.nextLine();
        System.out.println("Email Address:");
        String email= input.nextLine();
        boolean correct=false;
        while (!correct){
            System.out.println("Untolder or Artist?");
            String option=input.nextLine();
            if (!option.equalsIgnoreCase("untolder") && !option.equalsIgnoreCase("artist")){
                System.out.println("Unrecognized input, please try again");
            }
            else{
                correct=true;
                if (option.equalsIgnoreCase("untolder")){
                    if (!untolderAlreadyAdded(CNP) && !(artistAlreadyAdded(CNP))){
                        try{
                            Untolder untolder= personFactory.createUntolder(name,CNP,email);
                            untolders.add(untolder);
                            if (this.udb!=null){
                                this.udb.create(untolder);
                            }
                            System.out.println("Untolder has been added to database");
                        }
                        catch(Exception e){
                            System.out.println(e.toString());
                            return;
                        }

                    }
                    else{
                        System.out.println("Untolder already added to database");

                    }
                }
                else if (option.equalsIgnoreCase("artist")){
                    if (!artistAlreadyAdded(CNP) && !untolderAlreadyAdded(CNP)){
                        System.out.println("Salary:");
                        double salary=Integer.parseInt(input.nextLine());
                        System.out.println("Popularity:");
                        int fame=Integer.parseInt(input.nextLine());
                        Artist artist = personFactory.createArtist(name,CNP,email,salary,fame);
                        artists.add(artist);
                        if (this.adb!=null){
                            this.adb.create(artist);
                        }
                        System.out.println("Artist has been added to database");
                    }
                    else{
                        System.out.println("Artist already added, aborting");
                    }

                }

            }
        }


    }

    public void showUntolders(){
        if(untolders.isEmpty()){
            System.out.println("No untolders registered");
        }
        else{
            for (Untolder untolder:untolders){
                System.out.println(untolder.toString());
            }
        }
    }

    public void showArtists(){
        if(artists.isEmpty()){
            System.out.println("No artists registered");
        }
        else{
            for (Artist artist :artists){
                System.out.println(artist.toString());
            }
        }
    }

    public Untolder getUntolderFromInput(Scanner input){
        //System.out.println("Enter CNP: ");
        String CNP=input.nextLine();
        for (Untolder untolder: untolders){
            System.out.println(untolder.getCNP());
            if (untolder.getCNP().equalsIgnoreCase(CNP)){
                return untolder;
            }
        }
        return null;

    }

    public Artist getArtistFromInput(Scanner input){
        if (this.artists.isEmpty()){
            System.out.println("No artists registered");
        }
        System.out.println("Enter CNP: ");
        String CNP=input.nextLine();
        for (Artist artist: artists){
            if (artist.getCNP().equalsIgnoreCase(CNP)){
                return artist;
            }
        }
        return null;

    }


    public void removeArtist(Scanner input){
        Artist artist=getArtistFromInput(input);
        artists.remove(artist);
        mainStage.removeArtist(artist);
        if (adb!=null){
            adb.delete(artist);
        }
        System.out.println("Artist removed");
    }
    public void showUntolder(Scanner input){
        Untolder untolder=getUntolderFromInput(input);
        if (untolder==null){
            System.out.println("Untolder not added in database");
        }
        else{
            System.out.println(untolder.toString());
        }
    }

    public void removeUntolder(Scanner input){
        Untolder untolder=getUntolderFromInput(input);
        for (Integer i: untolder.getBought()){
            ServiceTicket serviceTicket=ServiceTicket.getInstance();
            for (Ticket t: serviceTicket.getTickets()){
                if (t.getId()==i){
                    t.setChecked_in(false);
                    t.setBought(false);
                    t.setTimePurchased(null);
                }
            }
        }
        untolders.remove(untolder);
        if (udb!=null){
            udb.delete(untolder);
        }
        System.out.println("Untolder removed");
    }
    public void showArtist(Scanner input) {
        Artist artist=getArtistFromInput(input);
        if (artist==null){
            System.out.println("Artist not added in database");
        }
        else{
            System.out.println(artist.toString());
        }
    }

    public void topUp(Scanner input){
        Untolder u=getUntolderFromInput(input);
        if (u==null){
            System.out.println("Untolder not found in database");
            return;
        }
        System.out.println("How much would you like to add to balance?");
        double sum=Double.parseDouble(input.nextLine());
        u.setBalance(u.getBalance()+sum);
        if (this.udb!=null){
            this.udb.topUp(u);
        }
        System.out.println("Added "+sum+" to balance");
    }

    private boolean alreadyInMainStage(String CNP){
        for (Artist artist: mainStage.getArtists()){
            if (artist.getCNP().equalsIgnoreCase(CNP)){
                return true;
            }
        }
        return false;
    }
    public void addToMainStage(Scanner input) {
        System.out.println("Who would you like to add to the main stage?");
        Artist artist=getArtistFromInput(input);
        if (artist==null){
            System.out.println("Artist not found");
            return;
        }
        if (mainStage.getArtists().size()>=15){
            System.out.println("Too many artists on main stage");
            return;
        }
        if (alreadyInMainStage(artist.getCNP())){
            System.out.println("Artist already on main stage");
            return;
        }
        mainStage.addArtist(artist);
    }

    public void showMainStage(){
        System.out.println(mainStage.toString());
    }
}
