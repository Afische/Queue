/* Simulation.java
 * Adam Fischer
 * pa4
 * 7/30/16
 * CMPS 12B
 * A program that simulates jobs in a Queue. Takes inFile input, outputs inFile.trc and inFile.rpt 
 */

import java.io.*;
import java.util.Scanner;

public class Simulation{

	public static void main(String[] args) throws IOException{

		//create printwriters for .rpt and .trc files
		PrintWriter out = null;
		PrintWriter out2 = null;

		//standard command line arg checks
		if( args.length != 1 ){
			System.out.println("Usage: Simulation infile");
			System.exit(1);
		}

		//create scanner, get jobnum from first line
		Scanner in = new Scanner(new File(args[0]));
		String line = in.nextLine().trim();
		int jobnum = Integer.parseInt(line);
		
		//create outfiles
		out2 = new PrintWriter(new FileWriter(args[0]+".trc"));
		out = new PrintWriter(new FileWriter(args[0]+".rpt"));

		//create array with jobs in "correct" order
		Job[] J = new Job[jobnum];
		int i = 0;
		while( i < jobnum ){
			J[i] = getJob(in);
			i++;
		}

		//create queue with jobs (first pass)
		Queue A = new Queue();
		i = 0;
		while( i < jobnum ){
			A.enqueue(J[i]);
			i++;
		}

      out.println("test");
      out.println(A);

		//INITIAL PRINTING TO OUTFILES
		out.println("Report file: "+args[0]+".rpt");
		out.println(jobnum+" Jobs:");
		out.println(A+"\n");
		out.println("***********************************************************");

		out2.println("Trace file: "+args[0]+".trc");
		out2.println(jobnum+" Jobs:");
		out2.println(A+"\n");

		/*note: i = number of processors*/ 
		i = 1;
		int z;
		int time = 0;

		//start loop for number of processors
		while( i < jobnum ){

			//create array of queues for processor queues
			Queue[] Z = new Queue[i+1];
			for ( z = 0; z < i+1; z++ )
				Z[z] = new Queue();
			Z[0] = A;

			//more printing to outfiles
			if( i == 1 ){
				out2.println("*****************************");
				out2.println("1 processor:");
				out2.println("*****************************");
			}else{
				out2.println("*****************************");
				out2.println(i+" processors:");
				out2.println("*****************************");
			}
			
			//in order to avoid exceptions have int b
			int b = -1;	
			while( b == -1 || Z[0].length() != jobnum ){

				//lots of try-catch statements to avoid exceptions caused by empty queues
				try{
					b = ((Job)Z[0].peek()).getFinish();
				}catch(Exception e){
					b = -1;
				}

				//na is next arrival, nf is next finish. can be tricky to get
				//which is why there are so many damn if statements
				int na;
				try{
					na = ((Job)Z[0].peek()).getArrival();
				}catch(Exception e){
					na = -1;
				}

				if ( time == 0 && na != 0 )
					printTrc(out2, Z, i+1, time);

				int nf = nextFinish(Z);

				if( nf == -1 || nf == 0 )
					time = na;
				else
					time = na < nf? na: nf;
				if ( b != -1 )
					time = nf;
				if( na == -1){
					time = nf;
				}

				//i decided to use this arbitrary number to show when things are done!
				//breaks from this loop and starts with next num of processors
				//shouldn't need this but just in case security
				if (time == 999999999)
					break;

				//if there is a finish during this iteration
				if (time == nf){
					for( int p = 1; p <= i; p++ ){
						try{
							if( ((Job)Z[p].peek()).getFinish() == time ){
								Z[0].enqueue(Z[p].dequeue());
								try{
									((Job)Z[p].peek()).computeFinishTime(time);
								}catch(Exception e){
									System.out.print("");
								}
							}
						}catch(Exception e){
							continue;
						}
					}
				}

				// if there is an arrival during this iteration
				if ( na == time ){
					int oq = openQueue(Z);
					Z[oq].enqueue(Z[0].dequeue());
					if( Z[oq].length() == 1 ){
						((Job)Z[oq].peek()).computeFinishTime(time);
					}
				}
				
				//need to reclaculate na and nf to see if we need to rerun at the same
				//time or if we can print to the trace file.
				try{
					na = ((Job)Z[0].peek()).getArrival();
				}catch(Exception e){
					na = -1;
				}
				nf = nextFinish(Z);
				if( na != time && nf != time ){
					printTrc(out2, Z, i+1, time);
				}
			}

			//stuff to print to report file
			float averageWait = 0;
			int totalWait = 0;
			int maxWait = 0;
			int max = 0;
			Queue tempQueue = new Queue();
			while( Z[0].length() != 0 ){
				max = ((Job)Z[0].peek()).getWaitTime();
				if( maxWait < max ){
					maxWait = max;
				}
				totalWait += ((Job)Z[0].peek()).getWaitTime();
				tempQueue.enqueue((Job)Z[0].dequeue());
			}
			averageWait = (float)totalWait/jobnum;
			if (i == 1)
				out.println(i + " processor: totalWait="+totalWait+ " maxWait="+maxWait+" averageWait="+String.format("%.2f", averageWait));
			else
				out.println(i + " processors: totalWait="+totalWait+ " maxWait="+maxWait+" averageWait="+String.format("%.2f", averageWait));

			i++;
			time = 0;

			//clear things, refill Queue A.
			Z = null;
			A.dequeueAll();
			tempQueue.dequeueAll();

			int v = 0;
			while( v < jobnum ){
				A.enqueue(J[v]);
				v++;
			}
			A = clearAll(A);
		}

		//close outfiles
		out2.close();
		out.close();
		in.close();
	}

	//method to reset the finish time of every job in a queue
	public static Queue clearAll( Queue A ){
		Queue B = new Queue();
		while( A.length() != 0 ){
			((Job)A.peek()).resetFinishTime();
			B.enqueue((Job)A.dequeue());
		}
		return B;
	}

	//method to check which queue is the best to place new jobs in
	public static int openQueue( Queue[] Q ){
		int len = Q.length;
		int[] A = new int[len];
		for ( int i = 1; i < len; i++){
			A[i] = Q[i].length();
		}
		int min = 999999;
		int minindex = 1;
		for ( int i = 1; i < len; i++){
			if( Q[i].length() < min ){
				min = Q[i].length();
				minindex = i;
			}
		}
		return minindex;
	}

	//method to calculate next job finish time
	public static int nextFinish( Queue[] Q ){
		int nf = 999999999;
		for( int i = 1; i < Q.length; i++ ){
			try{
				if( ((Job)Q[i].peek()).getFinish() < nf )
					nf = ((Job)Q[i].peek()).getFinish();
			}catch(Exception e){
				continue;
			}
		}
		return nf;
	}

	//method to print to trace file
	public static void printTrc(PrintWriter out, Queue [] Q, int i, int time){
		out.println("time="+time);
		for( int k = 0; k < i; k++)
			out.println(k + ": "+Q[k]);
		out.println("");
	}

	//method to get jobs from infile.
	public static Job getJob(Scanner in){
		String[] s = in.nextLine().split(" ");
		int a = Integer.parseInt(s[0]);
		int d = Integer.parseInt(s[1]);
		return new Job(a, d);
	}

}
