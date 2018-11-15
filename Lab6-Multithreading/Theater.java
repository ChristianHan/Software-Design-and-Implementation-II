// insert header here
package assignment6;

import java.util.*;


public class Theater {

	int numRows;
	int seatsPerRow;
	String show;

	ArrayList<Seat> theaterArray = new ArrayList<Seat>();

	public void theaterInitialize(){
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < seatsPerRow; j++){
				theaterArray.add(new Seat(i+1,j+1));
			}
		}
	}

	/*
	 * Represents a seat in the theater
	 * A1, A2, A3, ... B1, B2, B3 ...
	 */
	static class Seat {
		private int rowNum;
		private int seatNum;
		private boolean isTaken;

		public Seat(int rowNum, int seatNum) {
			this.rowNum = rowNum;
			this.seatNum = seatNum;
			isTaken = false;
		}

		public int getSeatNum() {
			return seatNum;
		}

		public int getRowNum() {
			return rowNum;
		}

		private boolean isTaken(){return isTaken;}

		private String intToAlphabet(int n){//make this method private later

			int quot = n/26;
			int rem = n%26;
			char letter = (char)((int)'A' + rem);
			if(quot == 0){
				return "" + letter;
			}
			else{
				return intToAlphabet(quot - 1) + letter;
			}
		}

		@Override
		public String toString() {
			// TODO: Implement this method to return the full Seat location ex: A1
			int n = getRowNum() - 1;//has integer row number

			if (n <= -1){ //if rownumber is less than or equal to 0
				throw new IllegalArgumentException("Only Positive Row Numbers");
			}

			if (getSeatNum() <= 0){
				throw new IllegalArgumentException("Only Positive Seat Numbers");
			}

			String string = intToAlphabet(n) + getSeatNum();
			return string;

		}
	}

  /*
	 * Represents a ticket purchased by a client
	 */
	static class Ticket {
		private String show;
		private String boxOfficeId;
		private Seat seat;
	  	private int client;

		public Ticket(String show, String boxOfficeId, Seat seat, int client) {
			this.show = show;
			this.boxOfficeId = boxOfficeId;
			this.seat = seat;
			this.client = client;
		}

		public Seat getSeat() {
			return seat;
		}

		public String getShow() {
			return show;
		}

		public String getBoxOfficeId() {
			return boxOfficeId;
		}

		public int getClient() {
			return client;
		}

		@Override
		public String toString() {
			// TODO: Implement this method to return a string that resembles a ticket

			int ticketWidthParam;
			ticketWidthParam = 30;

			String ticketString = "";

			for(int i = 0; i < ticketWidthParam; i++){
				ticketString = ticketString + "-";
			}

			ticketString = ticketString + "\n"; //creates a new line
			ticketString = ticketString + "|"; //creates a bar

			ticketString = ticketString + " " + "Show: " + show;

			for(int i = 0; i < ticketWidthParam - 9 - show.length(); i++){
				ticketString = ticketString + " ";
			}
			ticketString = ticketString + "|";

			ticketString = ticketString + "\n"; //creates a new line
			ticketString = ticketString + "|"; //creates a bar

			ticketString = ticketString + " " + "Box Office ID: " + boxOfficeId;

			for(int i = 0; i < ticketWidthParam - 18 - boxOfficeId.length(); i++){
				ticketString = ticketString + " ";
			}
			ticketString = ticketString + "|";

			ticketString = ticketString + "\n"; //creates a new line
			ticketString = ticketString + "|"; //creates a bar

			ticketString = ticketString + " " + "Seat: " + seat.toString();


			for(int i = 0; i < ticketWidthParam - 9 - seat.toString().length(); i++){
				ticketString = ticketString + " ";
			}
			ticketString = ticketString + "|";

			ticketString = ticketString + "\n"; //creates a new line
			ticketString = ticketString + "|"; //creates a bar

			ticketString = ticketString + " " + "Client: " + client;

			for(int i = 0; i < ticketWidthParam - 11 - String.valueOf(client).length(); i++){
				ticketString = ticketString + " ";
			}
			ticketString = ticketString + "|";


			ticketString = ticketString + "\n"; //creates a new line
			for(int i = 0; i < ticketWidthParam; i++){
				ticketString = ticketString + "-";
			}
			return ticketString;

		}
	}

	public Theater(int numRows, int seatsPerRow, String show) {
		// TODO: Implement this constructor
		this.numRows = numRows;
		this.seatsPerRow = seatsPerRow;
		this.show = show;

		Seat [][] theaterArray = new Seat[numRows][seatsPerRow];//array of seats

		for(int i = 0; i < numRows; i ++){
			for(int j = 0; j < seatsPerRow; j++){
				theaterArray[i][j] = new Seat(i+1,j+1);
			}
		}

	}

	/*
	 * Calculates the best seat not yet reserved
	 *
 	 * @return the best seat or null if theater is full
   */
	public Seat bestAvailableSeat() {
		//TODO: Implement this method

		for(int i = 0; i < theaterArray.size(); i++){
			if(theaterArray.get(i).isTaken() == false){
				return theaterArray.get(i);
			}
		}

		return null; //need to create theater full.

	}
	/*
	 * Prints a ticket for the client after they reserve a seat
   * Also prints the ticket to the console
	 *
   * @param seat a particular seat in the theater
   * @return a ticket or null if a box office failed to reserve the seat
   */

	public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
		//TODO: Implement this method

		Ticket ticketToPrint = new Ticket(show, boxOfficeId, seat, client);

		System.out.println(ticketToPrint.toString());

		return ticketToPrint; ///////////////////////still need to implement return null if box office failed to reserve the seat
	}

	/*
	 * Lists all tickets sold for this theater in order of purchase
	 *
   * @return list of tickets sold
   */
	public List<Ticket> getTransactionLog() {
		//TODO: Implement this method

		List<Ticket> ticketList = new ArrayList<Ticket>();


		return ticketList;

	}
}
