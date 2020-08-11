package mURL;
/**
 * This is the interface of the commands inside program core.
 * @author Mahdi Rezaie 9728040
 *
 */
public interface Command 
{	
	/**
	 * The command implementation.
	 * @param args The controller arguments
	 * @return The output message
	 * @throws InvalidArgumentsException any exception might happen on running command
	 */
	public String execute(String[] args) throws InvalidArgumentsException;
}
