package model;

import java.util.Comparator;

public class Artist extends Person{
    private double salary;
    private int noOfFans;

    public double getSalary() {
        return this.salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getNoOfFans() {
        return noOfFans;
    }

    public void setNoOfFans(int noOfFans) {
        this.noOfFans = noOfFans;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Artist(String name, String CNP, String email_address, double salary, int noOfFans){
        super(name,CNP,email_address);
        if (salary<0 || noOfFans<0){
            throw new IllegalArgumentException("Salary and popularity must be positive numbers");
        }
        else{
            this.salary=salary;
            this.noOfFans=noOfFans;
        }
    }

    public String toString(){
        return "Artist: "+this.getName()+"\n"+
                "CNP: "+this.getCNP()+"\n"+
                "Email Address: "+this.getEmail_address()+"\n"+
                "Hired for: "+this.getSalary()+"\n"+
                "Popularity: "+this.getNoOfFans()+"\n";
    }
}
