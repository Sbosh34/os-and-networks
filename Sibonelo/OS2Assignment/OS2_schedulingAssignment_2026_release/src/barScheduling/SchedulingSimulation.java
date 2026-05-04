//M. M. Kuttel 2026 mkuttel@gmail.com
//SchedulingSimulation.java - runs the simulation

package barScheduling;
// the main class, starts all threads

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class SchedulingSimulation {
    static int noPatrons = 10;
    static int sched = 3; // 0=FCFS, 1=SJF, 2=Priority, 3=MLFQ
    static int s = 0;
    static long seed = 0;
    static CountDownLatch startSignal;

    static Patron[] patrons;
    static Barman AllegraTheBarman;

    static long simStartTime;
    static String runId;
    static int[] arrivalTimes;
    static int[] drinksPerPatron;

 
    private static void validateScheduler(int sched) {
        if (sched < 0 || sched > 3) {
            throw new IllegalArgumentException(
                "Invalid scheduler " + sched +
                ". Valid values are: 0=FCFS, 1=SJF, 2=Priority, 3=MLFQ."
            );
        }
    }

    private static String schedulerName(int sched) {
        switch (sched) {
            case 0:
                return "FCFS";
            case 1:
                return "SJF";
            case 2:
                return "PRIORITY";
            case 3:
                return "MLFQ";
            default:
                throw new IllegalArgumentException(
                    "Invalid scheduler " + sched +
                    ". Valid values are: 0=FCFS, 1=SJF, 2=Priority, 3=MLFQ."
                );
        }
    }

    private static int[] generateGaussianArrivalTimes(int noPatrons, Random rng,
                                                      int meanArrival, int stdDevArrival,
                                                      int minArrival, int maxArrival) {
        int[] arrivals = new int[noPatrons];

        for (int i = 0; i < noPatrons; i++) {
            int t;
            do {
                t = (int) Math.round(meanArrival + stdDevArrival * rng.nextGaussian());
            } while (t < minArrival || t > maxArrival);

            arrivals[i] = t;
        }

        Arrays.sort(arrivals);
        return arrivals;
    }

    private static int[] generateDrinksPerPatron(int noPatrons, Random rng) {
        int[] drinks = new int[noPatrons];
        for (int i = 0; i < noPatrons; i++) {
            drinks[i] = rng.nextInt(8) + 1; // 1..8 drinks
        }
        return drinks;
    }

 
    public static void main(String[] args) throws InterruptedException, IOException {
        if (args.length >= 1) noPatrons = Integer.parseInt(args[0]);
        if (args.length >= 2) sched = Integer.parseInt(args[1]);
        if (args.length >= 3) s = Integer.parseInt(args[2]);
        if (args.length >= 4) seed = Long.parseLong(args[3]);

        validateScheduler(sched);

        runId = schedulerName(sched) + "_" + noPatrons + "_" + seed;

        // Single master RNG for fully reproducible workload generation
        Random masterRandom = (seed > 0) ? new Random(seed) : new Random();

        arrivalTimes = generateGaussianArrivalTimes(
        	    noPatrons,
        	    masterRandom,
        	    2000, // mean arrival time
        	    1500, // standard deviation
        	    0,    // minimum arrival time
        	    10000 // maximum arrival time
        	);

        drinksPerPatron = generateDrinksPerPatron(noPatrons, masterRandom);

        // Keep drink choices reproducible as well
        if (seed > 0) {
            DrinkOrder.random = new Random(seed);
        }

        startSignal = new CountDownLatch(noPatrons + 2);

        AllegraTheBarman = new Barman(startSignal, sched, s);
        AllegraTheBarman.start();

        patrons = new Patron[noPatrons];
        for (int i = 0; i < noPatrons; i++) {
            patrons[i] = new Patron(
                i,
                startSignal,
                AllegraTheBarman,
                arrivalTimes[i],
                drinksPerPatron[i]
            );
            patrons[i].start();
        }

        System.out.println("------Allegra the Barman Scheduling Simulation------");
        System.out.println("-------------- with " + noPatrons + " patrons---------------");
        System.out.println("-------------- and " + schedulerName(sched) + " scheduling ---------------");

        simStartTime = System.currentTimeMillis();
        startSignal.countDown();

        for (int i = 0; i < noPatrons; i++) {
            patrons[i].join();
        }

        System.out.println("------Waiting for Barman------");
        AllegraTheBarman.interrupt();
        AllegraTheBarman.join();
        System.out.println("------Bar closed------");

     }
}