import java.io.*;
import java.util.Scanner;

public class QueueTest {
	public static void main(String[] args) throws IOException {

		/* Variables*/
		Scanner sc = null;
		PrintWriter report = null;
		PrintWriter trace = null;
		Queue StorageC = new Queue();
		Queue Storage = new Queue();
		Queue finished = new Queue();
		Queue[] processorQueues = null;
		int m = 0;
		Job j = null;
		int time = 0;

		Scanner in = new Scanner(new File(args[0]));
		String line = in.nextLine().trim();
		int jobnum = Integer.parseInt(line);

		System.out.println("Report file: "+args[0]+".rpt");
		System.out.println(jobnum+" Jobs:");
		System.out.println("\n");
		System.out.println("***********************************************************");

	}
}