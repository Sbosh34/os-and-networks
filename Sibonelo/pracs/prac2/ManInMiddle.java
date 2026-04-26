import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Arrays;
//
public class ManInMiddle {

    private static int middle(final Memory frames, final Integer[] pageReferences) {
        int pageFaults = 0;
        /**
         * Your code here.
         * 
         * Using the frames memory object, process the pageReferences using the Man-in-the-Middle paging algorithm, returning the number of page faults.
         */
         
         int framesUsed = 0;
         int time = 1;
         int thresh = frames.size() * 2;
         int[] TimeStamps = new int[frames.size()];
         int oldestPage = 0;
         Deque<Integer> oldies = new ArrayDeque<>(frames.size());
         int victimFrame;
         
         //[frame, timeStamp]
         
         
         for(int page: pageReferences){
            
            if( frames.contains(page)){
               
               time++;
               System.out.println(page+": -");
               continue;
            }
            
            else{
               
               //frames are not yet full
               if( framesUsed < frames.size() ){
                  
                  frames.put( framesUsed, page);
                  oldies.offer(framesUsed);
                  TimeStamps[framesUsed] = time;
                  framesUsed++;
                  
               }
               else{
                  //frames are now full, need to find page to replace
                  oldestPage = oldies.peek();
                  int age = time - TimeStamps[oldestPage];
                  
                  if ( age > thresh ){
                     //page is old enough to be replaced
                     victimFrame = oldies.poll();
                     oldies.offer(victimFrame);
                     
                  }
                  else{
                     // oldest page not old enough, need to find median oldest
                     victimFrame = getMedianFrame(TimeStamps, frames.size());
                     oldies.remove(victimFrame);
                     oldies.offer(victimFrame);
                  }
                  
                  //hopefully found victim, now perform swap;
                  frames.put(victimFrame, page);
                  TimeStamps[victimFrame] = time;
                  
                  
                  
               }
            }
            System.out.println(page+": "+frames.toString());
            pageFaults++;
            time++;
         }
         
        return pageFaults;
    }
    public static int getMedianFrame(int[] array, int size){
      int[] copyArray = new int[size];
      for (int k = 0; k < size; k++){
         copyArray[k] = array[k];
      }
      
      Arrays.sort(copyArray);
      int idxFinder= copyArray[ Math.floorDiv(size, 2) ];
      
      
      for(int i = 0; i< size; i++){
         
         if (array[i] == idxFinder) return i;
      }
      
      return -1;
    }
    
    
    
 
//     //Create insert in Order Method
//     public static void insertInOrder(ArrayList<HashMap<Integer, Integer>> Sets, HashMap<Integer, Integer> NewPage, int page){
//       int ageCompare = NewPage.get(page)
//       
//       for(int i=0; i< Sets.size(); i++){
//          
//          if( Sets.get(i)
//       }
      
      


    public static void main(final String[] args) {
        final Scanner stdIn = new Scanner(System.in);

        System.out.println("Enter the physical memory size (number of frames):");
        final int numFrames = stdIn.nextInt();
        stdIn.nextLine();

        System.out.println("Enter the string of page references:");
        final String referenceString = stdIn.nextLine();

        System.out.printf("Page faults: %d.\n", middle(new Memory(numFrames), toArray(referenceString)));
    }

    private static Integer[] toArray(final String referenceString) {
        final Integer[] result = new Integer[referenceString.length()];
        
        for(int i=0; i < referenceString.length(); i++) {
            result[i] = Character.digit(referenceString.charAt(i), 10);
        }
        return result;
    }
}