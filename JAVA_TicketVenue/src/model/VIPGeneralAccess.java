package model;

public class VIPGeneralAccess extends Ticket{
    public VIPGeneralAccess(int id) {
        super(id);
        this.price=generic_price*2;
    }

    @Override
    public String toString(){
        String checked_in="";
        String bought="";
        if (this.isBought()){
            bought="Bought\n";
            if (this.isChecked_in()){
                System.out.println("CHECK");
                checked_in="Checked in to "+this.getP().getName()+"\n";
            }
            else
            {
                checked_in="Not checked in yet\n";
            }
        }
        else{
            bought="Not bought yet\n";
        }

        return "VIP General Access Ticket: "+this.getId()+"\n"+
                "Date Purchased: "+this.getTimePurchased()+"\n"+
                "Total Price: "+this.getPrice()+"\n"+bought+checked_in;

    }
}
