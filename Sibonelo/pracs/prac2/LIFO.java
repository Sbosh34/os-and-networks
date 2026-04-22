import java.util.Scanner;

//
public class LIFO{


   private static int LIFO(final Memory frames, final Integer[] pageReferences) {
      int pageFaults = 0;
     /**
      * Your code here.
      * 
      * Using the frames memory object, process the pageReferences using the LRU paging algorithm, returning the number of page faults.
      */
      int framesUsed = 0;
      int lastIn = frames.size()-1;
         
         // array to keep frame indexes in
         
         for(int page: pageReferences){
            
            if (frames.contains(page)){
               System.out.println(page+": -");
               continue;
            }
            else{
               
               if (framesUsed < frames.size() ){
               
                  frames.put(framesUsed, page);
                  framesUsed++;
               }
               else{
                  frames.put( lastIn, page);
               }
            }
            pageFaults++;
            System.out.println(page+": "+frames.toString());
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
   
      System.out.printf("Page faults: %d.\n", LIFO(new Memory(numFrames), toArray(referenceString)));
   }

   private static Integer[] toArray(final String referenceString) {
      final Integer[] result = new Integer[referenceString.length()];
        
      for(int i=0; i < referenceString.length(); i++) {
         result[i] = Character.digit(referenceString.charAt(i), 10);
      }
      return result;
   }
}

