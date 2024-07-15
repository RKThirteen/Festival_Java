package model;

import java.util.Comparator;

public class ArtistComparator implements Comparator<Artist> {
    @Override
    public int compare(Artist a1, Artist a2){
        return a1.getNoOfFans()-a2.getNoOfFans();
    }
}
