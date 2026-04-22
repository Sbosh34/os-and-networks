import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

//
public class Pessimal {


   private static int Historian(final Memory frames, final Integer[] pageReferences) {
      int pageFaults = 0;
     /**
      * Your code here.
      * 
      * Using the frames memory object, process the pageReferences using the LRU paging algorithm, returning the number of page faults.
      */
      int framesUsed = 0;
      int pos=0;
      int victFrame=0;
      int futurePage;
      boolean foundNext;
      Deque<Integer> queue = new ArrayDeque<>(frames.size());
      
      for(int page: pageReferences){
         
         // page already in memory
         if( frames.contains(page) ){
            pos++;
            System.out.println(page+": -");
            continue;
         }
         //page not in memory
         else{
            // frames are not yet full
            if( framesUsed < frames.size() ){
               frames.put(framesUsed, page);
               queue.offer(framesUsed);
               framesUsed++;
               
            }
            // all frames occupied, implement historian
            else{
               
               foundNext = false;
               // loop forward to look for victimFrame
               for(int k = pos+1; k < pageReferences.length; k++){
                  
                  futurePage = pageReferences[k];
                  
                  if( frames.contains( futurePage) ){
                     
                     victFrame = frames.indexOf( futurePage );
                     foundNext = true;
                     break;
                  }
               }
               if(foundNext){
                  // remove the frame from the queue
                  queue.removeFirstOccurrence( victFrame );
                  
               }
               else{
                  // break a tie, get frame from queue
                  victFrame = queue.poll();
                  
               }
               // insert new page
               frames.put( victFrame, page);
               
               //update queue
               queue.offer(victFrame);
            }

         }
         System.out.println(page+": "+frames.toString() );
         pos++;
         pageFaults++;
      }
      return pageFaults;

   }
       
         
         
   // return index of farthest frame

   public static void main(final String[] args) {
      final Scanner stdIn = new Scanner(System.in);
   
      System.out.println("Enter the physical memory size (number of frames):");
      final int numFrames = stdIn.nextInt();
      stdIn.nextLine();
   
      System.out.println("Enter the string of page references:");
      final String referenceString = stdIn.nextLine();
   
      System.out.printf("Page faults: %d.\n", Historian(new Memory(numFrames), toArray(referenceString)));
   }

   private static Integer[] toArray(final String referenceString) {
      final Integer[] result = new Integer[referenceString.length()];
        
      for(int i=0; i < referenceString.length(); i++) {
         result[i] = Character.digit(referenceString.charAt(i), 10);
      }
      return result;
   }
}

