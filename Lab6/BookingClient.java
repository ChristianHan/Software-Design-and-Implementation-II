// Insert header here
package assignment6;

import java.util.List;
import java.util.Map;
import java.lang.Thread;

public class BookingClient{



    /*
    Your BookingClient class file must have a main method.
    This main method must initialize the offices and theater with the same data
    as the example output shown below, and must call simulate().
     */


    public class RunnableBookingClient implements Runnable{
        public void run() {

        }
    }
    Thread thread = new Thread(new RunnableBookingClient());
    //thread.start();



  /*
   * @param office maps box office id to number of customers in line
   * @param theater the theater where the show is playing
   */

  public BookingClient(Map<String, Integer> office, Theater theater) {
    // TODO: Implement this constructor
       // BookingClient bookingClient =

  }

  /*
   * Starts the box office simulation by creating (and starting) threads
   * for each box office to sell tickets for the given theater
   *
   * @return list of threads used in the simulation,
   *         should have as many threads as there are box offices
   */
	public List<Thread> simulate() {

		//TODO: Implement this method

        int numBoxOffices = 3; // Number of threads,  need to figure how many box office parameters

        for (int i = 0; i < numBoxOffices; i++)
        {
          //  MultithreadingDemo object = new MultithreadingDemo();
          //  object.start();
        }


        Thread[] threads = new Thread[numBoxOffices];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    // some code to run in parallel
                    // this could also be another class that implements Runnable
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }




        return null;
	}
}
