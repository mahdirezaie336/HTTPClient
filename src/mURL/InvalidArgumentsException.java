package mURL;
/**
 * This is a simple exception have a default message. The message can be set by the programmer also.
 * @author Mahdi Rezaie 9728040
 *
 */
public class InvalidArgumentsException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sets the default message.
	 */
	public InvalidArgumentsException() 
	{
		super("Invalid arguments.\nUse -h or --help for help.");
	}
	
	/**
	 * Sets a custom message.
	 * @param message The custom message
	 */
	public InvalidArgumentsException(String message) 
	{
		super(message);
	}
}
