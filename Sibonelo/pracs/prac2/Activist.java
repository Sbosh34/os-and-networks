import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

//
public class Activist{


   private static int Activist(final Memory frames, final Integer[] pageReferences, final char[] accessTypes) {
      int pageFaults = 0;
     /**
      * Your code here.
      * 
      * Using the frames memory object, process the pageReferences using the LRU paging algorithm, returning the number of page faults.
      */
      int framesUsed = 0;
      int countPass = 1;
     int idxFrame;
     boolean foundOne = false;
     boolean foundNone = true;
     int victimFrame;
      
      ArrayList<Integer> keepFoundForPass = new ArrayList<>(frames.size());  // reset after each and every pass
      Deque<Integer> univQueue = new ArrayDeque<>(frames.size());
      HashMap<Integer, boolean[]> passChars = new HashMap<>(4);
      
      boolean[] one = {false, false};
      boolean[] two = {false, true};
      boolean[] three = {true, false};
      boolean[] four = {true, true};
      
      passChars.put(1, one);
      passChars.put(2, two);
      passChars.put(3, three);
      passChars.put(4, four);

      
      boolean[] refBits = new boolean[frames.size()];
      boolean[] dirtyBits = new boolean[frames.size()];
      // initialise these guys
//       for(int i=0; i < frames.size(); i++){
//          refBits[i] = false;
//          dirtyBits[i] = false;
//       }

      
      // Job number 1, find victim Page, do that first
      // Job number 2, decide how we will then put things
      for(int pos = 0; pos < pageReferences.length; pos++){
         
         int page = pageReferences[pos];
         char instr = accessTypes[pos];
         
         if (frames.contains(page)){
            //1. check operation being done on page
            // for R, we do not mind, but for W, cannot write if already written before
            idxFrame = frames.indexOf(page);
            
            if( instr == 'R' ){
               refBits[idxFrame] = true;
            }
            // instr is 'W'
            else{
               
               // both bits should be 1
               refBits[idxFrame] = true;
               dirtyBits[idxFrame] = true;
            }
            System.out.println(page+" "+instr+": -");
            continue;
            
         }
         //page was not found in memory, first time insertion
         else{
            // frames not yet full
            if( framesUsed < frames.size() ){
            
               //put in frame and add to FIFO queue
               frames.put(framesUsed, page);
               univQueue.offer(framesUsed);
               
               
               // set bits
               if( instr == 'W' ){
                  insertFirstW(framesUsed, dirtyBits, refBits);
               }
               else{
                  insertFirstR(framesUsed, dirtyBits, refBits);
               }
               
               //move to next frame 
               framesUsed++;
               
               
            }
            else{
               // frames are full. must find victim FRame
               while (countPass < 5){
                  //reset keepForPass
                  keepFoundForPass = new ArrayList<>(frames.size());
                  foundOne = false;
                  foundNone = false;
                  victimFrame = -1;
                  
                  
                  for(int clockHand = 0; clockHand < frames.size(); clockHand++){
                     if( evictCandids(clockHand, refBits, dirtyBits, countPass, passChars) ){
                        
                        keepFoundForPass.add(clockHand);
                     }
                  }
                  if( keepFoundForPass.size() > 0){
                     if (keepFoundForPass.size() == 1) foundOne = true;
                     
                  }else{
                     foundNone = true;
                  }
                  
                  // if we found any, move to determining victPage
                  if(!foundNone){
                     
                     if( foundOne) {
                        victimFrame = keepFoundForPass.get(0);
                        
                     }
                     // otherwise we found many, must break using FIFO
                     else{
                        Deque<Integer> copyQueue = new ArrayDeque(univQueue);
                        
                        while( copyQueue.size() != 0){
                           int posVict = copyQueue.poll();
                           
                           if( keepFoundForPass.contains(posVict) ){
                              victimFrame = posVict;
                              break;
                              
                           }
                        }
                     }
                     if (instr == 'W') insertFirstW(victimFrame, refBits, dirtyBits);
                     else insertFirstR(victimFrame, refBits, dirtyBits);
                     
                     frames.put(victimFrame, page);
                     //remove and readd victFrame from queue
                     univQueue.remove(victimFrame);
                     univQueue.offer(victimFrame);
                     break;
                  }else{
                     // move to next pass
                     countPass++;
                  }
               }
               countPass = 1;
            }
         }
         pageFaults++;
         System.out.println(page+" "+instr+": "+frames.toString());
      }
      return pageFaults;

   }
       
   // Helper methods
   public static void insertFirstW(int frame,boolean[] refBits, boolean[] dirtyBits){
      dirtyBits[frame] = true;
      refBits[frame] = false;
   }
   public static void insertFirstR(int frame,boolean[] refBits, boolean[] dirtyBits){
      dirtyBits[frame] = false;
      refBits[frame] = false;
   }
   public static boolean evictCandids(int frame, boolean[] refBits,boolean[] dirtyBits, int pass, HashMap<Integer, boolean[]> map){
      boolean refBit = map.get(pass)[0];
      boolean dirtBit = map.get(pass)[1];
      
      return ((refBits[frame] == refBit) && (dirtyBits[frame] == dirtBit));
   }
   public static void main(final String[] args) {
      final Scanner stdIn = new Scanner(System.in);
   
      System.out.println("Enter the physical memory size (number of frames):");
      final int numFrames = stdIn.nextInt();
      stdIn.nextLine();
   
      System.out.println("Enter the string of page references:");
      final String referenceString = stdIn.nextLine();
      
      System.out.println("Enter the access types (R/W):");
      final String accessString = stdIn.nextLine();
   
      System.out.printf("Page faults: %d.\n", Activist(new Memory(numFrames), toArray(referenceString), accessString.toCharArray()));
   }

   private static Integer[] toArray(final String referenceString) {
      final Integer[] result = new Integer[referenceString.length()];
        
      for(int i=0; i < referenceString.length(); i++) {
         result[i] = Character.digit(referenceString.charAt(i), 10);
      }
      return result;
   }
}

