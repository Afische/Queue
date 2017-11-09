/* Queue.java
 * Adam Fischer
 * pa4
 * 7/30/16
 * CMPS 12B
 * A program that creates queues, implements QueueInterface.
 */


public class Queue implements QueueInterface{

	//Node consutructor
	private class Node{
		Object job;
		Node next;

		Node(Object job){
			this.job = job;
			next = null;
		}
	}

	private Node head;
	private int numItems;

	//Queue constructor
	public Queue(){
		head = null;
		numItems = 0;
	}

	//method to check if queue has items in it
	public boolean isEmpty(){
		return(numItems == 0);
	}

	//method that returns length of a queue
	public int length(){
		return numItems;
	}

	//method to enqueue an object (at back)
	public void enqueue(Object newItem){
		if( head == null ){
			Node N = new Node(newItem);
			head = N;
		}else{ 
			Node P = head;
			while( P != null ){
				if( P.next == null )
					break;
				P = P.next;
			}
			P.next = new Node(newItem);
		}
		numItems++;
	}

	//method to dequeue an object (from front)
	public Object dequeue() throws QueueEmptyException{
		if( isEmpty() )
			throw new QueueEmptyException("the queue is empty");

		Node A = head;
		head = A.next;
		numItems--;
		return A.job;
	}

	//return the front object
	public Object peek() throws QueueEmptyException{
		if ( isEmpty() )
			throw new QueueEmptyException("the queue is empty");
		return head.job;
	}

	//dequeue everything...
	public void dequeueAll() throws QueueEmptyException{
		head = null;
		numItems = 0;
	}

	//tostring method to print queue.
	public String toString(){
		String s = "";
		Node A = head;
		while( A != null ){
			s += A.job + " ";
			A = A.next;
		}
		return s;
	}
}