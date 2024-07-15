package model;

import java.util.Arrays;
import java.util.Comparator;

public class Person implements Comparable<Person> {
    private String name;
    protected final String CNP;
    private String email_address;
    //protected int date_of_birth;

    public Person(String name, String CNP,String email_address){
        if (CNP.length()!=13)
            throw new IllegalArgumentException("CNP must be a string of 13 characters!");
        else{
            for(char ch: CNP.toCharArray()){
                if (!Character.isDigit(ch))
                    throw new IllegalArgumentException("CNP must only have numbers");
            }
        }

        this.name=name;
        this.CNP=CNP;
        this.email_address=email_address;
    }

    public int compareTo(Person p){
        return this.name.compareTo(p.getName());
    }
    public String getName(){return this.name;}

    public String getCNP(){return this.CNP;}

    public String getEmail_address(){return this.email_address;}




}
