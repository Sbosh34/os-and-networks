//M. M. Kuttel 2026 mkuttel@gmail.com
//Patron.java - represents a customer at the bar ordering drinks
//YOU MAY NOT ALTER THIS CLASS

package barScheduling;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class Patron extends Thread {

    private final CountDownLatch startSignal;
    private final Barman theBarman;

    private final int ID;
    private final int numberOfDrinks;
    private final int arrivalTime;

    private final DrinkOrder[] drinksOrder;

    Patron(int ID, CountDownLatch startSignal, Barman aBarman,
           int arrivalTime, int numberOfDrinks) {
        this.ID = ID;
        this.startSignal = startSignal;
        this.theBarman = aBarman;
        this.arrivalTime = arrivalTime;
        this.numberOfDrinks = numberOfDrinks;
        this.drinksOrder = new DrinkOrder[numberOfDrinks];
    }

    public void run() {
        try {
            startSignal.countDown();
            startSignal.await();

            sleep(arrivalTime);
            System.out.println("+" + this.ID + ": new thirsty Patron arrived at time " + arrivalTime);
            System.out.println( ID + ": will order " + numberOfDrinks + " drinks");

            for (int i = 0; i < numberOfDrinks; i++) {
                drinksOrder[i] = new DrinkOrder(this.ID);
                System.out.println(drinksOrder[i].toString()+ " ordered");
                theBarman.placeDrinkOrder(drinksOrder[i]);
                drinksOrder[i].waitForOrder();

                System.out.println(drinksOrder[i].toString()+ " -> patron drinking");
                sleep(drinksOrder[i].getImbibingTime());
            }

            System.out.println(this.ID + ": completed");

        } catch (InterruptedException e1) {
            // do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}