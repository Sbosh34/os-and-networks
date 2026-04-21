import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;

//
public class OPT {


   private static int OPT(final Memory frames, final Integer[] pageReferences) {
      int pageFaults = 0;
     /**
      * Your code here.
      * 
      * Using the frames memory object, process the pageReferences using the LRU paging algorithm, returning the number of page faults.
      */
      int framesUsed = 0;
      int farBack = 0;
      int N_found,futurePage;

      boolean foundAll = false;
      Deque<Integer> ago = new ArrayDeque<>(frames.size());
      
      for(int page: pageReferences){
         
         if ( frames.contains(page) ){
            ago.removeFirstOccurrence( frames.indexOf(page) );
            ago.offer(frames.indexOf(page) );
            farBack++;
            
            System.out.println(page+": -");
            
            continue;
         }
         //Page not found, pageFault
         else{
            // If memory is not-full, add page
            if (framesUsed < frames.size() ){
               
               //add page
               frames.put( framesUsed, page);
               ago.offer(framesUsed);
               framesUsed++;
               
               
            }
            // memory is full, we must look for victim page
            else{
            
               // create and reset
               N_found = 0;
               foundAll = false;
               HashSet<Integer> dupeGuard = new HashSet<>();
               int[] futures = new int[frames.size()];

               
               
               for(int k = farBack+1; k < pageReferences.length; k++){
                  futurePage = pageReferences[k];
                  //we found a page that's already in memory
                  if ( frames.contains( futurePage) && (!dupeGuard.contains(futurePage)) ){
                     N_found++;
                     futures[frames.indexOf(futurePage)] = k;
                     
                     //add frame to guard against finding the same page
                     dupeGuard.add(futurePage);
                  }
                  if (N_found == frames.size() - 1){
                     foundAll = true;
                     break;
                  }
                  
               }
               
               //at this point we now know where to find the future guys
               if (foundAll){
                  int victim = farFrame(futures);
                  frames.put(victim, page);
                  ago.removeFirstOccurrence(victim);
                  ago.offer(victim);
               }
            }
            
         }
         System.out.println(page+": "+frames.toString());
         pageFaults++;
         farBack++;
      }
      return pageFaults;
   }
       
         
         
   // return index of farthest frame
   public static int farFrame(int[] frames){
         int farthest = 0;
         for(int i=0; i < frames.length; i++){
            if(frames[i] == 0) return i;
            if (frames[i] > frames[farthest]){
               farthest = i;
            }
         }
         return farthest;  
   }
   public static void main(final String[] args) {
      final Scanner stdIn = new Scanner(System.in);
   
      System.out.println("Enter the physical memory size (number of frames):");
      final int numFrames = stdIn.nextInt();
      stdIn.nextLine();
   
      System.out.println("Enter the string of page references:");
      final String referenceString = stdIn.nextLine();
   
      System.out.printf("Page faults: %d.\n", OPT(new Memory(numFrames), toArray(referenceString)));
   }

   private static Integer[] toArray(final String referenceString) {
      final Integer[] result = new Integer[referenceString.length()];
        
      for(int i=0; i < referenceString.length(); i++) {
         result[i] = Character.digit(referenceString.charAt(i), 10);
      }
      return result;
   }
}

