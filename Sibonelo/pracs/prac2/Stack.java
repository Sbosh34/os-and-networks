import java.util.Deque;
import java.util.ArrayDeque;

public class Stack{
   
   public static void main(String[] ntokz){
      
      Deque<String> stk = new ArrayDeque<>();
      
      System.out.println("Is the stack empty? " + stk.isEmpty());
      
      //Push stuff onto the stack
      stk.push("Yello");
      stk.push("How");
      stk.push("Private");
      stk.push("I Love YOu");
      
      System.out.println("Stack after pushing "+stk);
      
      System.out.println("Peek before pop "+stk.peek()+" size is "+stk.size());
      stk.pop();
      System.out.println("Peek after pop "+stk.peek()+" size is "+stk.size());
      System.out.println("Stack After "+stk);
      
      
   }
}