package mURL;

import java.io.BufferedReader;
import java.io.FileReader;
/**
 * This class is the implementation of the help command.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class Help implements Command
{
	/**
	 * Returns the "helpfile.txt" content.
	 */
	@Override
	public String execute(String[] args) throws InvalidArgumentsException 
	{
		String output = "";
		
		try(
				FileReader fr = new FileReader("helpfile.txt");
				BufferedReader br = new BufferedReader(fr);
				)
		{
			String line;
			while((line = br.readLine()) != null)
				output += line + "\n";
		} catch(Exception e) {
			throw new InvalidArgumentsException("Error while reading helpfile.txt");
		}
		
		return output;
	}

}
