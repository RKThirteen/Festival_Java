package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Ticket{
    protected static final double generic_price=1500;
    private final int id;
    protected double price;

    private Person p;
    private boolean checked_in;

    private boolean bought;
    private LocalDate timePurchased;
    public Ticket(int id){
        this.id = id;
        price=1500;
        checked_in=false;
        bought=false;
        this.timePurchased=null;
    }

    public Ticket(int id, double price, Person p, boolean checked_in, boolean bought, LocalDate timePurchased) {
        this.id = id;
        this.price = price;
        this.p = p;
        this.checked_in = checked_in;
        this.bought = bought;
        this.timePurchased = timePurchased;
    }

    public int getId(){return this.id;}
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {this.price = price;}
    public void setChecked_in(boolean checked_in) {
        this.checked_in = checked_in;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public boolean isChecked_in() {
        return checked_in;
    }
    public boolean isBought() {
        return bought;
    }

    public void setTimePurchased(LocalDate timePurchased) {
        this.timePurchased = timePurchased;
    }

    public LocalDate getTimePurchased() {
        return timePurchased;
    }

    public void setPerson(Person p) {this.p = p;}

    public Person getP() {
        return p;
    }

    public void wasBought(){
        this.bought=true;
    }



    @Override
    public String toString(){
        String checked_in="";
        String bought="";
        if (this.isBought()){
            bought="Bought\n";
            if (this.isChecked_in()){
                checked_in="Checked in, cannot be refunded\n";
            }
            else
            {
                checked_in="Not checked in yet\n";
            }
        }
        else{
            bought="Not bought yet\n";
        }

        return "General Access Ticket: "+this.getId()+"\n"+
                "Date Bought: "+this.getTimePurchased()+"\n"+
                "Total Price: "+this.getPrice()+"\n"+bought+checked_in;

    }
}
