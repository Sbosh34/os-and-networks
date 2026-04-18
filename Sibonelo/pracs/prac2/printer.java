/**Sibonelo Sikhosatia - SKHSIB022
 * Java queue examples just to jog the mind a tad bit
 * 18 April 2026 - 6 days left
 */
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Scanner;

public class printer{
    private int numDoc = 1;
    private Deque<String> queue = new ArrayDeque<>();
    private Scanner sc = new Scanner(System.in);

    public static void main(String[] ntokz){
        printer P = new printer();
        int statusQuo = 0;
        while ((statusQuo-3)!=0){
            statusQuo = P.queryUser();
        }
        System.out.println("You are the Bestest bruh");


    }

   public void queuePrint(){
       System.out.println(queue);
   }

   public int queryUser(){
       System.out.println("""
               Pick an option:
               1. Input Doc
               2. Remove older Doc in the records
               3. End program
               """);
       int option = sc.nextInt();
       sc.nextLine();
   
       switch (option){
           case 1:
               //Create input element method
               option1();
               return 1;

           case 2:
               //Create remove doc method
               option2();
               return 2;
           case 3:
               System.out.println("Program will now end. Thank you very much");
           
       }
       return 3;
           
   }
   public  void option1(){
       System.out.println("""
               Enter the name of the record you want to enter:\n
               """);
       String input = sc.nextLine();
       queue.offer(input);
       queuePrint();
   }
   public void option2(){
       if ( !queue.isEmpty() ){
           queue.poll();
       }
       else{
           System.out.println("No records are available for you to delete");
           return ;
       }
       queuePrint();
   }
}
