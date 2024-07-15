package model;

public class PersonFactory {

    private static int untolderId=0;
    //There should be no person that's not an Untolder or Employee
    public Untolder createUntolder(String name, String CNP, String emailAddress){
        return new Untolder(++untolderId, name, CNP, emailAddress);
    }

    public Untolder createUntolder(String name, String CNP, String emailAddress ,double balance){
        return new Untolder(++untolderId, name, CNP, emailAddress,balance);
    }

    public Untolder createUntolder(int id, String name, String CNP, String emailAddress ,double balance){
        PersonFactory.untolderId=id+1;
        return new Untolder(id, name, CNP, emailAddress,balance);
    }
    public Artist createArtist(String name, String CNP, String emailAddress, double salary, int fame){
        return new Artist(name, CNP, emailAddress, salary, fame);
    }
}
