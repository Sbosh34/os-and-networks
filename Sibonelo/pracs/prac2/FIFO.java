import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
//
public class FIFO {


    private static int firstInFirstOut(final Memory frames, final Integer[] pageReferences) {
        int pageFaults = 0;
        /**
         * Your code here.
         * 
         * Using the frames memory object, process the pageReferences using the FIFO paging algorithm, returning the number of page faults.
         */

        // Queue to store the index of the oldest frames, FIFO
        Deque<Integer> queue = new ArrayDeque<>(frames.size());
        
        //counter used to populate the frames for the first time around
        int idxFrame = 0;
        boolean faulted = false;

        for(int page: pageReferences){

            // 1. Pagefault is created when the page we are looking for is not in the frames.
            // 2. for the first time, all frames will be free, populate them and save their indexes in queue

            //Frames are not yet fully populated
            if ((frames.size() - idxFrame > 0) && !frames.contains(page)){

                pageFaults++;faulted = true;
                queue.offer(idxFrame);
                frames.put(idxFrame, page);
                idxFrame++;

            }
            else{
                 // Now the frames have been fully populated
                // we first have to check if the page given is in memory?, if not we look at the oldest Frame to swap with
                if (!frames.contains(page)){

                    pageFaults++;faulted = true;
                    int frameToSwap = queue.poll();
                    frames.replace( frames.get(frameToSwap), page);

                    // add this frame as the newest in the queue
                    queue.offer(frameToSwap);


                }
            }

            // 2. For each page, check if that page is in Memory using frames.contains(page) --> False, increment pageFaults then add the page into the frame by removing the oldest 
            if (faulted){
                    System.out.println(page+": "+frames.toString());
            }
            else{
                System.out.println(page+": -");
            }
            faulted = false;
        }
        return pageFaults;
    }


    public static void main(final String[] args) {
        final Scanner stdIn = new Scanner(System.in);

        System.out.println("Enter the physical memory size (number of frames):");
        final int numFrames = stdIn.nextInt();
        stdIn.nextLine();

        System.out.println("Enter the string of page references:");
        final String referenceString = stdIn.nextLine();

        System.out.printf("Page faults: %d.\n", firstInFirstOut(new Memory(numFrames), toArray(referenceString)));
    }

    private static Integer[] toArray(final String referenceString) {
        final Integer[] result = new Integer[referenceString.length()];
        
        for(int i=0; i < referenceString.length(); i++) {
            result[i] = Character.digit(referenceString.charAt(i), 10);
        }
        return result;
    }
}
