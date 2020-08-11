package mURL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
/**
 * This class is the implementation of the "fire" command.
 * @author Mahdi
 *
 */
public class Fire implements Command
{
	private final String DATAS_PATH = "./data/requests/";
	
	/**
	 * Runs some requests in a group by sending its indexes as input argument.
	 */
	@Override
	public String execute(String[] args) throws InvalidArgumentsException
	{
		String output = "";
		
		if(args.length < 3)
			throw new InvalidArgumentsException("Too few arguments for command \"fire\".");
		
		String group = args[1];
		
		if(!new File(DATAS_PATH + group).exists())
			throw new InvalidArgumentsException("Directory " + group + " does not exist.");
		
		String[] filesNames = new File(DATAS_PATH + group).list(new FilenameFilter() 				// Getting files names
		{	
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir,name).isFile();
			}
		});
		
		for(int i = 2; i < args.length; ++i)									// Getting saved instances and loading them
		{
			Request req;
			String fileName = filesNames[Integer.parseInt(args[i]) - 1];
			
			try(
					FileInputStream fs = new FileInputStream(DATAS_PATH + group + "/" + fileName);
					ObjectInputStream os = new ObjectInputStream(fs)
					)
			{
				req = (Request) os.readObject();
			} catch(Exception e) {
				throw new InvalidArgumentsException("File number " + args[i] + " not found!");
			}
			
			req.load();
			output += req.getOutput();
		}
		
		return output;
	}

}
