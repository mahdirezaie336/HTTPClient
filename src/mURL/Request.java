package mURL;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * This is the request command class. It hold all information of the request in fields.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class Request implements Command, Serializable
{
	private static final long serialVersionUID = -419610976812965079L;
	private ArrayList<Argument> allArgs;												// All possible arguments
	private ArrayList<Argument> activeArgs;												// All activated arguments
	private URL url;																	// The request URL
	private transient HttpURLConnection connection;										// The request connection
	private boolean followRedirects;													// Follows redirects or no
	private String output;																// The output of the command
	private byte[] requestBody;															// The request body
	private boolean verbose;															// Verbose or no
	private long connectionTime;
	private String responseData;
	
	private String DATAS_PATH = "./data/";
	private String[] preConnectionArgs = {"-M","-H","-f","-i","-d","-j","-u","-S"};
	private String[] pastConnectionArgs = {"-O"};
	private String[] VALID_ARGS = {"-M","--method","-H","--headers"
			,"-i","-h","--help","-f","-O","--output","-S","--save","-d"
			,"--data","-j","--json","-u","--upload"};
	
	/**
	 * Creates all arguments and their actions.
	 */
	public Request()
	{
		allArgs = new ArrayList<Argument>();
		activeArgs = new ArrayList<Argument>();
		
		followRedirects = false;
		verbose = false;
		output = "";
		requestBody = null;
		
		allArgs.add(new Argument(1, 1, "-M", "--method"));								// Creating all arguments
		allArgs.add(new Argument(1, 1, "-H", "--headers"));
		allArgs.add(new Argument(0, 0, "-i"));
		allArgs.add(new Argument(0, 0, "-f"));
		allArgs.add(new Argument(0, 1, "-O", "--output"));
		allArgs.add(new Argument(1, 2, "-S", "--save"));
		allArgs.add(new Argument(1, 1, "-d", "--data"));
		allArgs.add(new Argument(1, 1, "-j", "--json"));
		allArgs.add(new Argument(1, 1, "-u", "--upload"));
		
		addAgrumentsActions();
	}
	
	/**
	 * What this command does.
	 */
	@Override
	public String execute(String[] args) throws InvalidArgumentsException 
	{
		for(String arg : args)																// Verifying arguments
			if(arg.startsWith("-") && !contains(VALID_ARGS, arg))
				throw new InvalidArgumentsException("Argument " + arg + " is unknown.");
		
		setArgsDatas(args);
		
		if(getActiveArgument("-h") != null)													// Printing help text
			return new Help().execute(args);
		
		try 																				// Setting URL
		{
			if(!args[0].startsWith("http://") && !args[0].startsWith("https://"))
				args[0] = "http://" + args[0];
			url = new URL(args[0]);
			connection = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new InvalidArgumentsException("Wrong URL!\n"+e.getMessage());
		} catch (IOException e) {
			throw new InvalidArgumentsException("Connection faild!\n"+e.getMessage());
		}
		
		request();
		
		for(String argName : pastConnectionArgs)											// Doing all past connection arguments actions
		{
			Argument arg = getActiveArgument(argName);
			if(arg != null) arg.act();
		}
		
		return output;
	}
	
	/**
	 * Performs the request.
	 * @throws InvalidArgumentsException Any errors might happen on connection
	 */
	private void request() throws InvalidArgumentsException
	{
		int n = 500, i = 0;
		
		long prevTime = System.currentTimeMillis();
		do																								// Redirect handling
		{
			i++;
			for(String argName : preConnectionArgs)
			{
				Argument arg = getActiveArgument(argName);
				if(arg!=null) arg.act();
			}
			
			if(verbose)																					// Verbose handling
			{
				String path = connection.getURL().getPath();
				output += connection.getRequestMethod() + " " + (path.equals("")?"/":path) + " "
							+ "HTTP/1.1\n";
				Map<String, java.util.List<String>> properties = connection.getRequestProperties();
				
				for(String key : properties.keySet())
					output += key + ": " + properties.get(key).get(0) + "\n";
				
				output += "\n";
			}
			
			if(requestBody != null)																		// Sending request body
				try(BufferedOutputStream bo = new 
					BufferedOutputStream( connection.getOutputStream()) ) {
					bo.write(requestBody);
				} catch(IOException e) {
					throw new InvalidArgumentsException(e.getMessage());
				}
			
			if(verbose)																					// Verbose Handling
			{
				Map<String, List<String>> headers = connection.getHeaderFields();
				
				for(String key : headers.keySet())
					output += ( key==null?"":(key + ": ") ) + headers.get(key).get(0) + "\n";
			
				output += "\n";
			}
			
			try {																						// Redirect or no
				if(connection.getResponseCode()/100 == 3 && followRedirects)
				{
					String location = connection.getHeaderField("Location");
					connection.disconnect();
					connection = (HttpURLConnection) new URL(location).openConnection();
					continue;
				}else
				{
					break;
				}
			} catch (Exception e) {
				throw new InvalidArgumentsException("Connection Error!\n"+e.getMessage());
			}
		}
		while(i < n);
		connectionTime = System.currentTimeMillis() - prevTime;
		
		responseData = "";
		try(																			// append response body to output
				InputStream is = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))
				)
		{
			String line = null;
			
			while( (line = br.readLine()) != null)
				responseData += line + "\n";
		} 
		catch (IOException e) 
		{
			throw new InvalidArgumentsException("Error while reading/writing.\n"+e.getMessage());
		}
		output += responseData;
	}
	
	/**
	 * This method must call after saved file is loaded
	 * @throws InvalidArgumentsException
	 */
	public void load() throws InvalidArgumentsException
	{
		try 					// Setting connection
		{
			connection = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new InvalidArgumentsException("Wrong URL!\n"+e.getMessage());
		} catch (IOException e) {
			throw new InvalidArgumentsException("Connection faild!\n"+e.getMessage());
		}
		
		request();
	}
	
	/**
	 * Setting the activated arguments and feed them with data.
	 * @param args The arguments data
	 * @throws InvalidArgumentsException Any exception might happen on feeding data.
	 */
	private void setArgsDatas(String[] args) throws InvalidArgumentsException
	{
		for(int i = 1; i < args.length; ++i)
			if(contains(VALID_ARGS,args[i]))
			{
				int j = i + 1;
				for( ; j < args.length; ++j)
					if(contains(VALID_ARGS,args[j])) break;
				
				ArrayList<String> datas = new ArrayList<String>();
				for(int k = i + 1; k < j; k++)
					datas.add(args[k]);
				
				Argument arg = getArgument(args[i]);
				arg.setDatas(datas.toArray(new String[datas.size()]));
				
				activeArgs.add(arg);
				
				i = j - 1;
			}
	}

	/**
	 * @return the connection
	 */
	public HttpURLConnection getConnection()
	{
		return connection;
	}
	
	/**
	 * Returns activated argument with name. Returns null if not found.
	 * @param name Argument name
	 * @return the argument
	 */
	public Argument getActiveArgument(String name)
	{
		for(Argument arg : activeArgs)
			for(String argName : arg.getNames())
				if(argName.equals(name))
					return arg;
		
		return null;
	}
	
	/**
	 * Returns URL field.
	 * @return the URL
	 */
	public URL getURL()
	{
		return url;
	}
	
	/**
	 * Returns output field.
	 * @return output
	 */
	public String getOutput()
	{
		return output;
	}
	
	/**
	 * Returns the response headers. 
	 * @return The respones headers
	 */
	public Map<String, List<String>> getResponseHeaders()
	{
		if(connection == null) return null;
		return connection.getHeaderFields();
	}
	
	/**
	 * @return response code
	 */
	public int getResponseCode()
	{
		int code = -1;
		try {
			code = connection.getResponseCode();
		} catch (IOException e) {
			code = -1;
		}
		return code;
	}
	
	/**
	 * @return Response message
	 */
	public String getResponseMessage()
	{
		String message = "";
		try {
			message = connection.getResponseMessage();
		} catch (IOException e) {
			return null;
		}
		return message;
	}
	
	/**
	 * @return Response body
	 */
	public String getResponseData()
	{
		return responseData;
	}
	
	/**
	 * @return Connection time
	 */
	public long getConnectionTime()
	{
		return connectionTime;
	}
	
	/**
	 * Gets and argument with name from list of all arguments.
	 * @param name The argument name
	 * @return The argument
	 */
	private Argument getArgument(String name)
	{
		for(Argument arg : allArgs)
			for(String argName : arg.getNames())
				if(argName.equals(name))
					return arg;
		return null;
	}
	
	/**
	 * Looks for a string in an array.
	 * @param args The array
	 * @param arg The string
	 * @return Returns true if found.
	 */
	private boolean contains(String[] args, String arg)
	{
		for(String argument : args)
			if(arg.equals(argument)) return true;
		return false;
	}
	
	/**
	 * Removes " from first and last of a text and returns it.
	 * @param text The string to be trimmed
	 * @return The trimmed string
	 */
	public static String trimCommas(String text)
	{
		return text.substring(text.startsWith("\"")?1:0,
				text.endsWith("\"")?text.length()-1:text.length());
	}
	
	/**
	 * Combines to arrays of bytes. 
	 * @param first The first array
	 * @param second The second array
	 * @return The combined array
	 */
	private byte[] append(byte[] first, byte[] second)
	{
		if(second == null) return first;
		if(first == null) return second;
		byte[] newArr = new byte[first.length + second.length];
		int i = 0;
		for(byte value : first)
			newArr[i++] = value;
		for(byte value : second)
			newArr[i++] = value;
		return newArr;
	}
	
	private byte[] append(byte[] first, byte[] second, int length)
	{
		if(second == null) return first;
		if(first == null) return second;
		if(second.length >= length) return append(first,second);
		byte[] newArr = new byte[first.length + length];
		int i = 0;
		for(byte value : first)
			newArr[i++] = value;
		for(int j = 0; j < length; ++j)
			newArr[i++] = second[j];
		return newArr;
	}
	
	/**
	 * Returns this object.
	 * @return this object
	 */
	private Request getThis()
	{
		return this;
	}
	
	/**
	 * Sets all arguments actions.
	 */
	private void addAgrumentsActions()
	{
		getArgument("-M").addAction(new Action() {

			private static final long serialVersionUID = 627640940727371647L;

			@Override
			public void act(Argument caller) throws InvalidArgumentsException 
			{
				try {
					connection.setRequestMethod(caller.getDatas().get(0));
				} catch (ProtocolException e) {
					throw new InvalidArgumentsException(e.getMessage());
				}
			}
			
		});
		
		getArgument("-H").addAction(new Action() {

			private static final long serialVersionUID = -3297778182437147787L;

			@Override
			public void act(Argument caller) {
				String data = caller.getDatas().get(0);
				data = trimCommas(data);
				String[] values = data.split(";");
				for(String value : values)
				{
					String[] parts = value.split(":");
					if(parts.length == 0)
						parts = new String[] {"",""};
					else if(parts.length == 1)
						parts = new String[] {parts[0],""};
					connection.addRequestProperty(parts[0], parts[1]);
				}
				
			}
			
		});
		
		getArgument("-i").addAction(new Action() {

			private static final long serialVersionUID = 2697017896787366600L;

			@Override
			public void act(Argument caller) throws InvalidArgumentsException 
			{
				verbose = true;
			}
		});
		
		getArgument("-f").addAction(new Action() {

			private static final long serialVersionUID = 1112057712896445788L;

			@Override
			public void act(Argument caller) {
				connection.setInstanceFollowRedirects(false);
				HttpURLConnection.setFollowRedirects(false);
				followRedirects = true;
			}
		});
		
		getArgument("-O").addAction(new Action() {

			private static final long serialVersionUID = -5865114249461470661L;

			@Override
			public void act(Argument caller) throws InvalidArgumentsException 
			{
				String fileName;
				if(caller.getDatas().size() == 1)
					fileName = caller.getDatas().get(0);
				else
					fileName = "output_" + new Date().toString();
				try(
						FileWriter fw = new FileWriter(DATAS_PATH + "outputs/" + fileName);
						BufferedWriter bw = new BufferedWriter(fw,4096);
								)
				{
					bw.write(output);
				} 
				catch (IOException e) 
				{
					throw new InvalidArgumentsException("Error while reading/writing.\n"+e.getMessage());
				}
			}
		});
		
		getArgument("-S").addAction(new Action() {
			
			private static final long serialVersionUID = -6574228731131604496L;
			
			/**
			 * Saves the Request instance into the specified file.
			 */
			@Override
			public void act(Argument caller) throws InvalidArgumentsException 
			{
				String fileName;
				String group = caller.getDatas().get(0);
				
				if(!(new File(DATAS_PATH + "requests/" + group).exists()))								// If group does not exists
					throw new InvalidArgumentsException("Group " + group + " does not exist.\n"
							+ "Create it by \"create\" command.");
				
				if(caller.getDatas().size() == 2)														// Set save file name
					fileName = caller.getDatas().get(1);
				else
					fileName = "" + url.getHost() + "_" + System.currentTimeMillis();
				
				try(																					// Try to save
						FileOutputStream fs = new FileOutputStream(DATAS_PATH + "requests/" + group + "/" + fileName);
						ObjectOutputStream os = new ObjectOutputStream(fs)
						)
				{
					os.writeObject(getThis());
				} catch(Exception e) {
					throw new InvalidArgumentsException("Error while writing to file!\n" + e.getMessage());
				}
			}
		});
		
		getArgument("-d").addAction(new Action() {

			private static final long serialVersionUID = 4581013365865892266L;
			
			/**
			 * Fills request body with the input datas. 
			 */
			@Override
			public void act(Argument caller) 
			{
				String data = caller.getDatas().get(0);
				data = trimCommas(data);
				String[] datas = data.split("&");
				try {
					connection.setRequestMethod("POST");
					connection.setDoOutput(true);
				} catch (ProtocolException e) {
				}
				String boundary = "" + System.currentTimeMillis();
				connection.setRequestProperty("Content-Type", "multipart/form-data;"
						+ " boundary=" + boundary);
				
				requestBody = "".getBytes();
				for(String value : datas)
				{
					String[] parts = value.split("=");
					
					if(parts.length == 0)													// Handling Null Pointer Exception
						parts = new String[] {"",""};
					else if(parts.length == 1)
						parts = new String[] {parts[0],""};
					
					requestBody = append(requestBody,("--" + boundary + "\r\n").getBytes());
					if(parts[0].toLowerCase().contains("file"))
					{
						File fileToAdd = new File(parts[1]);
						requestBody = append(requestBody,(("Content-Disposition: form-data; filename=\"" + 
											fileToAdd.getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes()));
						try(FileInputStream fs = new FileInputStream(fileToAdd)) 
						{
							byte[] fileContent = new byte[(int)fileToAdd.length()];
							fs.read(fileContent);
							requestBody = append(requestBody,fileContent);
						} 
						catch (Exception e) 
						{
							requestBody = append(requestBody,parts[1].getBytes());
						}
						requestBody = append(requestBody,"\r\n".getBytes());
						
					}else
					{
		                requestBody = append(requestBody,("Content-Disposition: form-data; name=\""
		                			+ parts[0] + "\"\r\n\r\n" + parts[1] + "\r\n").getBytes());
					}
				}
				requestBody = append(requestBody,("--" + boundary + "--\r\n").getBytes());
			}
		});
		
		getArgument("-j").addAction(new Action() {

			private static final long serialVersionUID = 7336500402478406893L;
			
			/**
			 * Fills request body with json data.
			 */
			@Override
			public void act(Argument caller) throws InvalidArgumentsException 
			{
				String data = caller.getDatas().get(0);
				data = trimCommas(data);
				try {
					connection.setRequestMethod("POST");
					connection.setDoOutput(true);
				} catch (ProtocolException e) {
				}
				connection.setRequestProperty("Content-Type", "application/json");
				requestBody = ("" + data).getBytes();
			}
		});
		
		getArgument("-u").addAction(new Action() {

			private static final long serialVersionUID = 1L;

			@Override
			public void act(Argument caller) throws InvalidArgumentsException 
			{
				String fileName = caller.getDatas().get(0);
				requestBody = null;
				connection.setRequestProperty("Content-Type", "application/octet-stream");
				connection.setDoOutput(true);
				try(
						FileInputStream fs = new FileInputStream(fileName);
						BufferedInputStream bs = new BufferedInputStream(fs)
						)
				{
					byte[] buffer = new byte[2048];
					int nRead = -1;
					while((nRead = bs.read(buffer)) != -1)
						requestBody = append(requestBody,buffer,nRead);
				} catch(Exception e) {
					throw new InvalidArgumentsException(e.getClass().getName() + ":\n\t" + e.getMessage());
				}
			}
		
		});
	}
}
