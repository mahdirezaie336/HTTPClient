package mURL;

import java.io.File;
/**
 * This class is the "create" command implementation.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class Create implements Command
{
	/**
	 * Creates a folder in ./data/requests/.
	 */
	@Override
	public String execute(String[] args) throws InvalidArgumentsException
	{
		if(args.length > 2)
			throw new InvalidArgumentsException("Argument " + args[2] + " is invalid!");
		
		if(new File("./data/requests/" + args[1]).exists())
			throw new InvalidArgumentsException("Group " + args[1] + " already exists.");
		
		try {
			new File("./data/requests/" + args[1]).mkdirs();
		}catch(Exception e) {
			throw new InvalidArgumentsException("Cannot create directory \""+args[1]+"\"!");
		}
		
		return "The command completed successfully.";
	}

}
