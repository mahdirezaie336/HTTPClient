package mURL;

import java.io.Serializable;
/**
 * This is the interface of arguments action. The action must be implemented in act method.
 * @author Mahdi Rezaie 9728040
 *
 */
public interface Action extends Serializable
{
	/**
	 * The action implementation.
	 * @param caller The argument which called the action.
	 * @throws InvalidArgumentsException Any exception might happen on implementation
	 */
	public void act(Argument caller) throws InvalidArgumentsException;
}
