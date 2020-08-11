package mURL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.net.URL;
/**
 * This class is the "list" command implementation.
 * @author Mahdi Rezaie 9728040
 *
 */
public class List implements Command
{
	private final String DATAS_PATH = "./data/requests/";
	
	/**
	 * Gets a list of files or folders on the requests path
	 */
	@Override
	public String execute(String[] args) throws InvalidArgumentsException
	{
		String output = "";
		
		if(args.length > 2)
			throw new InvalidArgumentsException("Too many arguments for command \"list\".");
		
		if(args.length == 2)				// List of groups or directories
		{
			String group = args[1];
			if(!new File(DATAS_PATH + group).exists())
				throw new InvalidArgumentsException("Directory " + group + " does not exist.");
			
			String[] filesNames = new File(DATAS_PATH + group).list(new FilenameFilter() 
			{	
				@Override
				public boolean accept(File dir, String name) {
					return new File(dir,name).isFile();
				}
			});
			
			int i = 1;
			for(String fileName : filesNames)
			{
				Request req;
				try(
						FileInputStream fs = new FileInputStream(DATAS_PATH + group + "/" + fileName);
						ObjectInputStream os = new ObjectInputStream(fs);
						)
				{
					req = (Request) os.readObject();
				} catch(Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
				
				Argument headers = req.getActiveArgument("-H");
				Argument method = req.getActiveArgument("-M");
				URL url = req.getURL();
				
				output += i++ + " . url: " + url + " | method: " + (method!=null?method:"GET")
						+ " | headers: " + headers + "\n";
			}
		}
		if(args.length == 1)				// List of requests in a group
		{
			String[] directories = new File(DATAS_PATH).list(new FilenameFilter() 
			{	
				@Override
				public boolean accept(File dir, String name) {
					return new File(dir,name).isDirectory();
				}
			});
			
			output += "Groups:\n";
			for(String dir : directories)
				output += dir + "\n";
		}
		return output;
	}

}
