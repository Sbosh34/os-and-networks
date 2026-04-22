import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;

//
public class Clock_Second_Chance {


   private static int Clock(final Memory frames, final Integer[] pageReferences) {
      int pageFaults = 0;
     /**
      * Your code here.
      * 
      * Using the frames memory object, process the pageReferences using the LRU paging algorithm, returning the number of page faults.
      */
      int framesUsed = 0;
      int clockHand = 0;
      int pointFrame;
      
      boolean[] refBits = new boolean[frames.size()];
      
      for(int page: pageReferences){
         
         //page already in memory, just set it's ref bit to 1, no move clock hand
         if( frames.contains(page) ){
            
            pointFrame = frames.indexOf(page);
            refBits[pointFrame] = true;
            System.out.println(page+": -");
               continue;
         }
         // now page is not in memory
         else{
            
            // when frames are still not full
            if (framesUsed < frames.size() ){
               //insert page into empty frame
               frames.put(framesUsed, page);
               
               // set refbit = 1
               refBits[framesUsed] = true;
               
               framesUsed++; 
            }
            // frames are full, implement clock now type stuff
            else{
               
               // keep looking for empty frame and changing refbits until i find empty one
               while( refBits[clockHand] ){
                  
                  refBits[clockHand] = false;
                  clockHand = (clockHand+1)%frames.size();
                  
               }
               // once we get here clockHand will be pointing to victim page
               frames.put( clockHand, page);
               refBits[clockHand] = true;
               
               //move clockHand forward
               clockHand = (clockHand+1)%frames.size();
            }
            
            System.out.println(page+": "+frames.toString());
            pageFaults++;
         }
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
   
      System.out.printf("Page faults: %d.\n", Clock(new Memory(numFrames), toArray(referenceString)));
   }

   private static Integer[] toArray(final String referenceString) {
      final Integer[] result = new Integer[referenceString.length()];
        
      for(int i=0; i < referenceString.length(); i++) {
         result[i] = Character.digit(referenceString.charAt(i), 10);
      }
      return result;
   }
}

