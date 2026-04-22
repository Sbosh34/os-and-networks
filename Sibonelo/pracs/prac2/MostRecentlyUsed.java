import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;

//
public class MostRecentlyUsed{


   private static int MRU(final Memory frames, final Integer[] pageReferences) {
      int pageFaults = 0;
     /**
      * Your code here.
      * 
      * Using the frames memory object, process the pageReferences using the LRU paging algorithm, returning the number of page faults.
      */
      int framesUsed = 0;
      int frameFound;
      
      //Stack for keeping track of frame indexes
      Deque<Integer> stkFrame = new ArrayDeque<>(frames.size());
      
      for(int page: pageReferences){
         
         // page already in memory, pop and put back in stack
         if(frames.contains(page)){
            
            frameFound = frames.indexOf(page);
            stkFrame.removeFirstOccurrence(frameFound);    // potential error
            stkFrame.push(frameFound);
            
            continue;
            
         }
         // page was not loaded into memory
         else{
            
            // when frames are not yet filled
            if ( framesUsed < frames.size() ){
               
               frames.put(framesUsed, page);
               stkFrame.push(framesUsed);
               framesUsed++;
            }
            // frames are full used up, now implement MRU
            else{
               frameFound = stkFrame.pop();
               frames.put(frameFound, page);
               stkFrame.push(frameFound);
            }
         }
         pageFaults++;
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
   
      System.out.printf("Page faults: %d.\n", MRU(new Memory(numFrames), toArray(referenceString)));
   }

   private static Integer[] toArray(final String referenceString) {
      final Integer[] result = new Integer[referenceString.length()];
        
      for(int i=0; i < referenceString.length(); i++) {
         result[i] = Character.digit(referenceString.charAt(i), 10);
      }
      return result;
   }
}

