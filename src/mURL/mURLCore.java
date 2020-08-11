package mURL;

import java.util.HashMap;
/**
 * This class holds the commands collection execute them by what user inputs as argument.
 * @author Mahdi Rezaie 9728040
 *
 */
public class mURLCore
{
	private String[] args;
	private HashMap<String, Command> commands;
	
	/**
	 * Creates all commands
	 * @param args The program arguments
	 */
	public mURLCore(String[] args)
	{
		commands = new HashMap<String,Command>();
		commands.put("fire", new Fire());
		commands.put("list", new List());
		commands.put("create", new Create());
		commands.put("request", new Request());
		commands.put("help", new Help());
		
		this.args = args;
	}
	
	/**
	 * Calling the related command.
	 * @return The return text.
	 * @throws InvalidArgumentsException Any exception might happen on running command
	 */
	public String run() throws InvalidArgumentsException
	{
		switch(args[0])
		{
		case "help":
		case "fire":
		case "list":
		case "create":
			return commands.get(args[0]).execute(args);
		default:
			return commands.get("request").execute(args);	
		}
	}
}
