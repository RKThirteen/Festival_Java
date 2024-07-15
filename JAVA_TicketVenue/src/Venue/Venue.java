package Venue;

public class Venue implements Comparable<Venue> {

    private int id;
    private String name;
    private final boolean vip_only;
    private int sponsorship;

    public int getSponsorship() {return sponsorship;}
    public void setSponsorship(int sponsorship) {
        this.sponsorship = sponsorship;
    }
    public Venue(int id, String name, int sponsorship){
        this.id=id;
        this.name=name;
        this.sponsorship=sponsorship;
        if (this.sponsorship>10000){
            this.vip_only=true;
        }
        else{
            this.vip_only=false;
        }
    }

    public int compareTo(Venue venue){
        return Double.compare(this.sponsorship,venue.getSponsorship());
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVip_only() {
        return vip_only;
    }

    @Override
    public String toString(){
        return this.name+"\n"+
                "Vip Only: "+this.vip_only+"\n"+
                "Sponsorship offered: "+this.sponsorship;
    }
}
