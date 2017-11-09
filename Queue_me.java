/* Adam Fischer
 * pa4
 * CMPS 12B
 * 7/28/16
 * Implemented a Queue ADT based on a linked list data structure. Used queue ADT to simulate
 * a set of jobs performed by a set of processors, where there are more jobs than processors,
 * and therefore some jobs may have to wait in a queue.
 */

import java.io.*;

public class Queue implements QueueInterface {

   // private inner Node class
   private class Node {
      Object item;
      Node next;

      Node(Object item){
      	 this.item = item;
         next = null;
      }
   }

   // Fields for the Queue class
   private Node front;     // reference to first Node in List
   private Node back;	  // reference to last Node in list
   private int numItems;  // number of items in this list

   // constructor for the Queue class
   public Queue(){
      front = null;
      back = null;
      numItems = 0;
   }

	// add object to back of list
   public void enqueue(Object newItem){
   	  Node A = front;
   	  if(front == null){
   	  	head = item;
   	  }
   	  else{
   	  	while(A.next != null){
   	  		A=A.next;
   	  	}
   	  	A.next = new Node(newItem);
   	  	back = A.next;
   	  }
   	  numItems++;
   }

   public boolean isEmpty(){
      return (numItems == 0);
   }

   public int length(){
      return numItems;
   }

   public Object dequeue() throws QueueEmptyException{
      Node A = front;
      if (front == null){
         throw new QueueEmptyException("cannot dequeue() on empty Queue");
      } 
      else{
      front = A.next;
      numItems--;
      return A.item;
      }
   }

   public Object peek() throws QueueEmptyException{
      if (front == null){
         throw new QueueEmptyException("cannot peek() on empty Queue");
      } 
      else{
         return front.item;
      }
   }

   public void dequeueAll() throws QueueEmptyException{
      if (front == null){
         throw new QueueEmptyException("cannot dequeueAll() on empty Queue");
      } 
      else{
         while(numItems>0){
            Node A = front.next;
            front = null;
            front = A;
            numItems--;
         }
      }
   }

   public String toString(){
      String a = "";
      Node A = front;
      while(A != null) {
         a = a + A.item + " ";
         A = A.next;
      }
      return a;
   }

}



