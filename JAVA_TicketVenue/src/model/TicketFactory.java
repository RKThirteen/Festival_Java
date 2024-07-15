package model;

public class TicketFactory {
    private static int id=0;

    public Ticket createTicket(){
        //System.out.println(id);
        return new Ticket(++id);
    }

    public Ticket createTicket(int id){
        TicketFactory.id=id+1;
        return new Ticket(id);
    }
    public VIPGeneralAccess createVIPTicket(){
        return new VIPGeneralAccess(++id);
    }

    public VIPGeneralAccess createVIPTicket(int id){
        TicketFactory.id=id+1;
        return new VIPGeneralAccess(id);
    }

    public OneDayPass createOneDayPass(String day){
        return new OneDayPass(++id,day);
    }

    public OneDayPass createOneDayPass(int id, String day){
        TicketFactory.id=id+1;
        return new OneDayPass(++id,day);
    }
}
