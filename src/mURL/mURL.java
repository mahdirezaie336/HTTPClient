package mURL;


import java.util.Scanner;
/**
 * mURL application driver class.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class mURL
{
	public static void main(String[] args) 
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter args:");
		args = scanner.nextLine().split(" ");
		mURLCore core = new mURLCore(args);
		
		try {
			String out = core.run();
			System.out.print(out);
		} catch (InvalidArgumentsException e) {
			System.err.println(e.getMessage());
		}
		scanner.close();
	}
}