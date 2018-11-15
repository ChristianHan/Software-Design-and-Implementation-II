package assignment6;

public class Main {



    /*
    Repeat for each client until show is sold out or no more clients
     Seat <- find the best available seat
     If there is an available seat, then
          mark the seat as taken
          print the ticket
    End repeat
    if (sold out) Output to the screen only once “Sorry, we are sold
    out!”
     */
    public static void main (String args[]){

        int numRows = 5;
        int seatsPerRow = 26;
        String show = "Jurrasic Park";

        Theater theater = new Theater(numRows, seatsPerRow, show);

        //BookingClient();

        //BookingClient bookingClient = new BookingClient(new Map<"BX1", 3> map, theater );

        theater.theaterInitialize();

        System.out.println(theater.theaterArray.get(0).toString());//get(0) shows seat A1

        System.out.println(theater.bestAvailableSeat());

        theater.printTicket("BX1", theater.theaterArray.get(25), 1);

/*        while(!showSoldOut || no more clients){ //if sold out print "Sorry, we are sold out!"
            if(bestAvailableSeat != null){
                theaterArray.seatIndex = false;
                ticket.toString;
            };
        }*/

    }
}
