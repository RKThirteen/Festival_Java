package model;

import java.util.ArrayList;
import java.util.List;

public class MainStage {

    private static MainStage instance;
    private int capacity;

    private List<Artist> artists=new ArrayList<>();
    private MainStage(){
        this.capacity=2000;
    }

    public static MainStage getInstance() {
        if (instance == null) {
            instance = new MainStage();
        }
        return instance;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public void addArtist(Artist artist){
        this.artists.add(artist);
    }

    public void removeArtist(Artist artist) {this.artists.remove(artist);}

    @Override
    public String toString(){
        String str="";
        for (Artist artist:artists){
            str=str+artist.toString();
        }
        return "Capacity: "+"\n"+this.getCapacity()+"\n"+
                "No. of Artists: "+this.getArtists().size()+"\n"+
                "Artists: "+"\n"+str;
    }
}
