package model;

import java.util.ArrayList;
import java.util.List;

public class Untolder extends Person {

    private final int untolderId;
    private List<Integer> bought=new ArrayList<>();
    private double balance;
    public Untolder(int id, String name, String CNP, String emailAddress){
        super(name,CNP,emailAddress);
        this.untolderId=id;
        this.balance=0;
    }

    public Untolder(int id, String name, String CNP, String emailAddress, double balance){
        super(name,CNP,emailAddress);
        this.untolderId=id;
        this.balance=balance;
    }

    public int getUntolderId() {
        return untolderId;
    }

    public double getBalance() {return balance;}

    public void setBalance(double balance) {this.balance = balance;}

    public void receiveTicket(Ticket t){
        bought.add(t.getId());
    }

    public List<Integer> getBought() {
        return bought;
    }

    public void setBought(List<Integer> bought) {
        this.bought = bought;
    }

    public void removeTicket(Ticket t){
        t.setBought(false);
        bought.remove(Integer.valueOf(t.getId()));
    }

    public String ticketsOwned(){
        String str="";
        for (Integer i:bought){
            str=str+i.toString()+" ";
        }
        if (str.isEmpty()){
            return "No tickets owned";
        }
        else{
            return str;
        }

    }
    @Override
    public String toString(){
        String str=""+this.ticketsOwned();
        return "Untolder: "+this.getName()+"\n"+
                "CNP: "+this.getCNP()+"\n"+
                "Email Address: "+this.getEmail_address()+"\n"+
                "Current balance: "+this.getBalance()+"\n"+
                "Tickets Owned(only id's shown): "+"\n"+str;

    }
}
