import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
//
public class LRU {


   private static int leastRecentlyUsed(final Memory frames, final Integer[] pageReferences) {
      int pageFaults = 0;
        /**
         * Your code here.
         * 
         * Using the frames memory object, process the pageReferences using the LRU paging algorithm, returning the number of page faults.
         */
         
         //Queue for the LRU
      int sizeQueue = frames.size();
      Deque<Integer> deque = new ArrayDeque<>(sizeQueue);
      int frameIdx = 0;
         
      for (int page: pageReferences){
         
            // page already in memory, remove and add it to last pos, it's the new LRU
         if ( frames.contains(page) ){
               //if yes, poll it and move it to new entry
            int frameOfPage = frames.indexOf(page);
            deque.removeFirstOccurrence(frameOfPage);
            deque.offer(frameOfPage);
               
            //move to next iteration
            System.out.println(page+": -");
            continue;
               
         }
            // Check if frames are full.
            //Case for full
         if ( frameIdx > 2 ){
               
            // page was never there, and frames are full, find victim from queue, replace it's page contents
            int victimFrame = deque.poll();
            frames.put(victimFrame, page);
            deque.offer(victimFrame);
         }
          // frames are not yet full, so add anywhere basically
         else{
            deque.offer(frameIdx);
            frames.put(frameIdx, page);
            frameIdx++;
               
         }
         System.out.println(page+": "+frames.toString());
         pageFaults++;
               
      }
      return pageFaults++;
   }


   public static void main(final String[] args) {
      final Scanner stdIn = new Scanner(System.in);
   
      System.out.println("Enter the physical memory size (number of frames):");
      final int numFrames = stdIn.nextInt();
      stdIn.nextLine();
   
      System.out.println("Enter the string of page references:");
      final String referenceString = stdIn.nextLine();
   
      System.out.printf("Page faults: %d.\n", leastRecentlyUsed(new Memory(numFrames), toArray(referenceString)));
   }

   private static Integer[] toArray(final String referenceString) {
      final Integer[] result = new Integer[referenceString.length()];
        
      for(int i=0; i < referenceString.length(); i++) {
         result[i] = Character.digit(referenceString.charAt(i), 10);
      }
      return result;
   }
}

