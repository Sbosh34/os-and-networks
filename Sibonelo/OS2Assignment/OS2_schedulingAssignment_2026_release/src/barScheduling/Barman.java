//M. M. Kuttel 2026 mkuttel@gmail.com

/*
 Barman Thread class.
 Schedulers:
 0 = FCFS
 1 = SJF
 2 = Priority
 3 = MLFQ with aging
 */


package barScheduling;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Barman extends Thread {

    private final CountDownLatch startSignal;
    private final int schedAlg;
    private final int switchTime;

    // Single-queue schedulers
    private LinkedBlockingQueue<DrinkOrder> fcfsQueue;
    private PriorityBlockingQueue<DrinkOrder> sjfQueue;
    private PriorityBlockingQueue<DrinkOrder> priorityQueue;

    // MLFQ queues
    private LinkedBlockingQueue<DrinkOrder> q0;
    private LinkedBlockingQueue<DrinkOrder> q1;
    private LinkedBlockingQueue<DrinkOrder> q2;

    // Track how many drinks each patron has already had served
    private ConcurrentHashMap<Integer, Integer> drinksServedPerPatron;

    // FIFO tie-breaker for priority scheduling
    private long sequenceCounter = 0;

    // Aging threshold for MLFQ
    private static final long AGING_THRESHOLD = 4000; // ms

    private final String schedulerName;

 

 //=NO CHANGE AREA BEINGS=========================================================   
    Barman(CountDownLatch startSignal, int sAlg, int sTime) {
        this.startSignal = startSignal;
        this.schedAlg = sAlg;
        this.switchTime = sTime;
        this.schedulerName = schedulerName(sAlg);

        switch (schedAlg) {
            case 0:
                fcfsQueue = new LinkedBlockingQueue<DrinkOrder>();
                break;

            case 1:
                sjfQueue = new PriorityBlockingQueue<DrinkOrder>(
                        5000,
                        Comparator.comparingInt(DrinkOrder::getExecutionTime)
                                  .thenComparingLong(DrinkOrder::getSequenceNumber)
                );
                break;

            case 2:
                priorityQueue = new PriorityBlockingQueue<DrinkOrder>(
                        5000,
                        Comparator.comparingInt(DrinkOrder::getPriority)
                                  .thenComparingLong(DrinkOrder::getSequenceNumber)
                );
                break;

            case 3:
                q0 = new LinkedBlockingQueue<DrinkOrder>();
                q1 = new LinkedBlockingQueue<DrinkOrder>();
                q2 = new LinkedBlockingQueue<DrinkOrder>();
                drinksServedPerPatron = new ConcurrentHashMap<Integer, Integer>();
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid scheduler " + sAlg +
                        ". Valid values are: 0=FCFS, 1=SJF, 2=Priority, 3=MLFQ."
                );
        }
    }

    private static String schedulerName(int schedAlg) {
        switch (schedAlg) {
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
                        "Invalid scheduler " + schedAlg +
                        ". Valid values are: 0=FCFS, 1=SJF, 2=Priority, 3=MLFQ."
                );
        }
    }

    public void placeDrinkOrder(DrinkOrder order) throws InterruptedException, IOException {
        long now = System.currentTimeMillis();
        order.setArrivalTime(now);
        order.setEnqueueTime(now);
        order.setSequenceNumber(nextSequenceNumber());

        switch (schedAlg) {
            case 0:
                fcfsQueue.put(order);
                break;

            case 1:
                sjfQueue.put(order);
                break;

            case 2:
                order.setPriority(order.getOrderer()); // lower patron ID = higher priority
                priorityQueue.put(order);
                break;

            case 3:
                int level = initialQueueFor(order);
                order.setQueueLevel(level);
                enqueueMLFQ(order, level);
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid scheduler " + schedAlg +
                        ". Valid values are: 0=FCFS, 1=SJF, 2=Priority, 3=MLFQ."
                );
        }
    }

    private synchronized long nextSequenceNumber() {
        return sequenceCounter++;
    }

    private int initialQueueFor(DrinkOrder order) {
        int patron = order.getOrderer();
        int served = drinksServedPerPatron.getOrDefault(patron, 0);

        if (served == 0) {
            return 0;
        }
        if (served == 1) {
            return 1;
        }
        return 2;
    }

    private void enqueueMLFQ(DrinkOrder order, int level) throws InterruptedException {
        order.setQueueLevel(level);
        order.setEnqueueTime(System.currentTimeMillis());

        switch (level) {
            case 0:
                q0.put(order);
                break;
            case 1:
                q1.put(order);
                break;
            case 2:
                q2.put(order);
                break;
            default:
                throw new IllegalArgumentException("Invalid queue level: " + level);
        }
    }

    private void ageQueues() throws InterruptedException {
        long now = System.currentTimeMillis();
        promoteOldOrders(q2, q1, 2, 1, now);
        promoteOldOrders(q1, q0, 1, 0, now);
    }

    private void promoteOldOrders(LinkedBlockingQueue<DrinkOrder> from,
                                  LinkedBlockingQueue<DrinkOrder> to,
                                  int fromLevel,
                                  int toLevel,
                                  long now) throws InterruptedException {

        int originalSize = from.size();

        for (int i = 0; i < originalSize; i++) {
            DrinkOrder order = from.poll();
            if (order == null) {
                break;
            }

            long waited = now - order.getEnqueueTime();

            if (order.getQueueLevel() == fromLevel && waited >= AGING_THRESHOLD) {
                order.setQueueLevel(toLevel);
                order.setEnqueueTime(now);
                to.put(order);

            } else {
                from.put(order);
            }
        }
    }

    private DrinkOrder takeNextMLFQOrder() throws InterruptedException {
        while (true) {
            ageQueues();

            DrinkOrder order = q0.poll();
            if (order != null) {
                return order;
            }

            order = q1.poll();
            if (order != null) {
                return order;
            }

            order = q2.poll();
            if (order != null) {
                return order;
            }

            TimeUnit.MILLISECONDS.sleep(1);
        }
    }

    private void recordServedDrink(DrinkOrder order) {
        if (schedAlg == 3) {
            int patron = order.getOrderer();
            drinksServedPerPatron.merge(patron, 1, Integer::sum);
        }
    }

 
    @Override
    public void run() {
        try {
            startSignal.countDown();
            startSignal.await();

            switch (schedAlg) {
                case 0:
                    runFCFS();
                    break;
                case 1:
                    runSJF();
                    break;
                case 2:
                    runPriority();
                    break;
                case 3:
                    runMLFQ();
                    break;
                default:
                    throw new IllegalStateException(
                            "Unexpected scheduler " + schedAlg +
                            ". Valid values are: 0=FCFS, 1=SJF, 2=Priority, 3=MLFQ."
                    );
            }

        } catch (InterruptedException e) {
            System.out.println("---Barman is packing up");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write output", e);
        }
    }

    private void runFCFS() throws InterruptedException, IOException {
        while (true) {
            DrinkOrder currentOrder = fcfsQueue.take();
            processOrder(currentOrder, "---Barman preparing drink for patron " + currentOrder);
        }
    }

    private void runSJF() throws InterruptedException, IOException {
        while (true) {
            DrinkOrder currentOrder = sjfQueue.take();
            processOrder(currentOrder, "---Barman preparing drink for patron " + currentOrder);
        }
    }

    private void runPriority() throws InterruptedException, IOException {
        while (true) {
            DrinkOrder currentOrder = priorityQueue.take();
            processOrder(
                    currentOrder,
                    "---Barman preparing drink for patron " + currentOrder
                            + " with priority " + currentOrder.getPriority()
            );
        }
    }

    private void runMLFQ() throws InterruptedException, IOException {
        while (true) {
            DrinkOrder currentOrder = takeNextMLFQOrder();
            processOrder(
                    currentOrder,
                    "---Barman preparing drink for patron " + currentOrder
                            + " from Q" + currentOrder.getQueueLevel()
            );
        }
    }

  

    private void processOrder(DrinkOrder currentOrder, String startMessage)
            throws InterruptedException, IOException {

        currentOrder.setServiceStartTime(System.currentTimeMillis());
        System.out.println(startMessage);
        sleep(currentOrder.getExecutionTime());
        currentOrder.setCompletionTime(System.currentTimeMillis());
        System.out.println("---Barman has made drink for patron " + currentOrder);
        currentOrder.orderDone();

        recordServedDrink(currentOrder);
        recordCompletedOrder(currentOrder);

        sleep(switchTime);
    }
    
//=NO CHANGE AREA ENDS=========================================================   
      
    
    
    private void recordCompletedOrder(DrinkOrder order) throws IOException {
    	// THIS IS THE ONLY FUNCTION YOU MAY CHANGE
    }

}