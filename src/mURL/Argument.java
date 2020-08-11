package mURL;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class holds information about an argument in the program including
 * argument names, data, sizes and argument actions.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class Argument implements Serializable
{
	private static final long serialVersionUID = 6056798088759949970L;
	private ArrayList<String> names;											// Argument names
	private ArrayList<String> datas;											// Argument values
	private int minSize;														// Minimum number of values
	private int maxSize;														// Maximum number of values
	private ArrayList<Action> actions;											// What an argument does
	
	/**
	 * Constructor gets the minimum size, maximum size and name as input.
	 * It is important to set all names in constructor because name has no setters.
	 * @param minDataSize Minimum number of values
	 * @param maxDataSize Maximum number of values
	 * @param names Names of argument
	 */
	public Argument(int minDataSize, int maxDataSize, String...names)
	{
		this.names = new ArrayList<String>();
		datas = new ArrayList<String>();
		actions = new ArrayList<Action>();
		
		for(String name : names)
			this.names.add(name);
		if(minDataSize >= 0)
			minSize = minDataSize;
		else
			minSize = 0;
		if(maxDataSize > minDataSize || maxDataSize == -1)
			maxSize = maxDataSize;
		else
			maxSize = minDataSize;
	}

	/**
	 * @return the datas
	 */
	public ArrayList<String> getDatas() {
		return datas;
	}

	/**
	 * @return the names
	 */
	public String[] getNames()
	{
		return names.toArray(new String[datas.size()]);
	}
	
	/**
	 * Adds a new action to actions list.
	 * @param a The action to be added
	 */
	public void addAction(Action a)
	{
		if(a!=null)
			actions.add(a);
	}
	
	/**
	 * Gets the datas as input and clears the list and add these datas.
	 * @param args The new datas
	 * @throws InvalidArgumentsException When input size is less than minimum size of
	 * 			values or more than size of maximum values throws exception.
	 */
	public void setDatas(String...args) throws InvalidArgumentsException
	{
		if(args.length < minSize) throw new InvalidArgumentsException("Too few datas for argument " + names);
		if(args.length > maxSize) throw new InvalidArgumentsException("Too many datas for argument " + names);
		datas.clear();
		
		for(String arg : args)
			datas.add(arg);
	}
	
	/**
	 * Runs all actions.
	 * @throws InvalidArgumentsException Throws any exceptions can happen in actions to the caller
	 */
	public void act() throws InvalidArgumentsException
	{
		for(Action a : actions)
			a.act(this);
	}
	
	@Override
	public String toString() {
		return datas.toString();
	}
	
}
