package model;

public class OneDayPass extends Ticket{
    private final String dayAvailable;

    public String getDayAvailable() {
        return dayAvailable;
    }

    public OneDayPass(int id, String dayAvailable){
        super(id);
        if (dayAvailable.equalsIgnoreCase("FRIDAY") || dayAvailable.equalsIgnoreCase("SUNDAY")){
            this.price=generic_price*0.6;
            this.dayAvailable=dayAvailable;
        } else if (dayAvailable.equalsIgnoreCase("SATURDAY")) {
            this.price=generic_price*0.8;
            this.dayAvailable=dayAvailable;
        }
        else{
            throw new IllegalArgumentException("Invalid Day");
        }


    }

    @Override
    public String toString(){
        String checked_in="";
        String bought="";
        if (this.isBought()){
            bought="Bought\n";
            if (this.isChecked_in()){
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

        return "One Day Pass: "+this.getId()+"\n"+
                "Date Purchased: "+this.getTimePurchased()+"\n"+
                "Day Available: "+this.getDayAvailable()+"\n"+
                "Total Price: "+this.getPrice()+"\n"+bought+checked_in;

    }
}
