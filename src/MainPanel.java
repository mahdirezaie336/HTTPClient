import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import mURL.Request;
/**
 * The main panel of the program which includes all components.
 * @author Mahdi Rezaie 972840
 *
 */
public class MainPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JPanel sidePanel;
	private JPanel requestPanel;
	private JPanel responsePanel;
	private HashMap<String,Component> impComponents;							// Keeping some important components
	
	/**
	 * Initializes all components.
	 */
	public MainPanel()
	{
		super();
		impComponents = new HashMap<String,Component>();
		setLayout(new GridLayout(1,1));
		createAppBody();
	}
	
	/**
	 * Creates all components on the program body.
	 */
	private void createAppBody()
	{
		sidePanel = new JPanel(new BorderLayout());
		requestPanel = new JPanel(new BorderLayout());
		responsePanel = new JPanel(new BorderLayout());
		
		
		sidePanel.setPreferredSize(new Dimension(250,100));						// Creating main panels
		requestPanel.setBackground(Color.decode("#45494A"));
		responsePanel.setBackground(Color.decode("#45494A"));
		
		
		JButton minsomnia = new JButton("MInsomnia");							// Creating minsomnia button
		minsomnia.setBackground(Color.decode("#695EB8"));
		minsomnia.setUI(new BasicButtonUI());
		minsomnia.setFont(new Font("Arial", 14, 14));
		minsomnia.setPreferredSize(new Dimension(100,50));
		sidePanel.add(minsomnia,BorderLayout.NORTH);
		
		
		FilesList requestsTree = new FilesList("./data/requests/");						// Creating tree
		requestsTree.addMouseListenerToTree(new ListHandler(requestsTree));
		JButton addGroupButton = new JButton("Create new group");
		sidePanel.add(addGroupButton,BorderLayout.SOUTH);
		sidePanel.add(requestsTree,BorderLayout.CENTER);
		
		
		JPanel requestFields = new JPanel(new BorderLayout());						// Creating URL components
		requestFields.setPreferredSize(new Dimension(50,50));
		JComboBox<String> requestMethods = new JComboBox<String>(
				new String[]{"GET","DELETE","POST","PUT","PATCH"});
		requestMethods.setPreferredSize(new Dimension(100,50));
		requestFields.add(requestMethods,BorderLayout.WEST);
		JTextField urlField = new JTextField("http://www.ping.eu");
		urlField.setFont(new Font("Arial",10,10));
		requestFields.add(urlField,BorderLayout.CENTER);
		JButton sendButton = new JButton("Send");
		sendButton.setPreferredSize(new Dimension(70,50));
		requestFields.add(sendButton,BorderLayout.EAST);
		requestPanel.add(requestFields,BorderLayout.NORTH);
		
		
		MTabbedPane requestTabs = new MTabbedPane();												// Creating request tabs
		LinkedHashMap<String,JPanel> datas = new LinkedHashMap<String,JPanel>();
		JPanel formData = new JPanel(new BorderLayout()), json = new JPanel(new BorderLayout()),
				binData = new JPanel(new BorderLayout()), fileChoose = new JPanel();
		JTextArea formDataTextArea = new JTextArea();
		formData.add(new JScrollPane(formDataTextArea),BorderLayout.CENTER);
		JTextArea jsonTextArea = new JTextArea();
		json.add(new JScrollPane(jsonTextArea),BorderLayout.CENTER);
		JTextField fileAddress = new JTextField();
		fileChoose.setLayout(new BoxLayout(fileChoose,BoxLayout.Y_AXIS));
		fileChoose.add(new JLabel("Choose a file:"));			fileChoose.add(fileAddress);
		JButton browseButton = new JButton("Browse");
		browseButton.addActionListener(new FileChooseHandler(fileAddress));
		fileChoose.add(browseButton);
		binData.add(fileChoose,BorderLayout.NORTH);
		datas.put("No Data", null);
		datas.put("Form Data", formData);
		datas.put("JSON", json);
		datas.put("Binary Data", binData);
		requestTabs.addMultiTab(datas);
		requestTabs.addTab("Auth", new JPanel());
		requestTabs.addTab("Query", new JPanel());
		requestTabs.addTab("Header", new JPanel(new BorderLayout()));
		//requestTabs.addTab("Docs", new JPanel());
		requestPanel.add(requestTabs,BorderLayout.CENTER);
		
		
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));							// Creating status bar
		statusPanel.setPreferredSize(new Dimension(100,50));
		statusPanel.setBackground(Color.white.darker());
		MLabel status = new MLabel("200 OK"); status.setVisible(false);
		MLabel timeStatus = new MLabel("TIME 980 ms"); timeStatus.setVisible(false);
		MLabel sizeStatus = new MLabel("SIZE 2.1 KB"); sizeStatus.setVisible(false);
		status.setOpaque(true); timeStatus.setOpaque(true); sizeStatus.setOpaque(true);
		statusPanel.add(status); statusPanel.add(timeStatus); statusPanel.add(sizeStatus);
		responsePanel.add(statusPanel,BorderLayout.NORTH);
		
		
		MTabbedPane responseTabs = new MTabbedPane();												// Creating response tabs
		LinkedHashMap<String,JPanel> responseDatas = new LinkedHashMap<String,JPanel>();
		JPanel raw = new JPanel(new BorderLayout());
		JTextArea rawPreview = new JTextArea();
		rawPreview.setEditable(false);
		raw.add(new JScrollPane(rawPreview),BorderLayout.CENTER);
		responseDatas.put("Raw", raw);
		JEditorPane ep = new JEditorPane();
		JPanel htmlPreview = new JPanel(new BorderLayout());
		htmlPreview.add(ep);
		responseDatas.put("Preview", htmlPreview);
		//responseDatas.put("JSON", new JPanel());
		responseTabs.addMultiTab(responseDatas);
		responseTabs.addTab("Header", new ResponseHeadersFields());
		responseTabs.addTab("Cookie", new JPanel());
		JPanel timeLine = new JPanel(new BorderLayout());
		JTextArea timeLineText = new JTextArea();
		timeLineText.setEditable(false);
		timeLine.add(new JScrollPane(timeLineText));
		responseTabs.addTab("Timeline", timeLine);
		responsePanel.add(responseTabs,BorderLayout.CENTER);
		
		
		JSplitPane rightSide = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestPanel, responsePanel);
		rightSide.setDividerSize(3);
		rightSide.setDividerLocation(250);
		changeSplitDividerColor(rightSide, Color.decode("#45494A"));
		
		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, rightSide);
		jsp.setDividerSize(2);
		add(jsp);
		
		((JPanel) requestTabs.getComponentAt(3)).add(new HeadersList());
		
																								// Performing a request
		sendButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Runnable task = new Runnable()
				{
					@Override
					public void run() 
					{
						Request request = new Request();								// Initializing variables
						String url = urlField.getText();
						String method = requestMethods.getSelectedItem().toString();
						String headers = "";
						String data = "";
						String dataType = "";
						HeadersList headersList = (HeadersList)((JPanel) requestTabs.getComponentAt(3)).getComponent(0);
						
						
						String[] keys = headersList.getItemsFields(0);										// Getting header fields
						String[] values = headersList.getItemsFields(1);
						for(int i = 0; i < keys.length; ++i)
							headers += keys[i] + ":" + values[i] + ";";
						if(headers.endsWith(";")) headers = headers.substring(0,headers.length()-1);
						
						JComboBox<?> combo = (JComboBox<?>) requestTabs.getTabComponentAt(0);				// Getting data fields
						switch(combo.getSelectedItem().toString())
						{
						case "Form Data":
							dataType = "-d";
							data = formDataTextArea.getText();
							break;
						case "JSON":
							dataType = "-j";
							data = jsonTextArea.getText();
							break;
						case "Binary Data":
							dataType = "-u";
							data = fileAddress.getText();
							break;
						default:
							data = "";
							dataType = "";
						}
						ArrayList<String> args = new ArrayList<String>();									// Adding headers to request object 
						args.add(url); args.add("-f"); args.add("-i"); args.add("-M"); args.add(method);
						if(!headers.trim().equals("")) {
							args.add("-H"); args.add(headers);
						}
						if(!dataType.equals("")) {
							args.add(dataType); args.add(data);
						}
						
						try {																				// Trying to connect
							request.execute(args.toArray(new String[args.size()]));
						} catch (Exception e) {
							showError(e.getMessage());
							e.printStackTrace();
							return;
						}
						
						ResponseHeadersFields rf = (ResponseHeadersFields) responseTabs.getComponentAt(1);		// Setting outputs
						rf.setFields(request.getResponseHeaders());
						status.setText(request.getResponseCode()+ " " + request.getResponseMessage());
						String responseData = request.getResponseData();
						String size = String.format("SIZE %.1f KB", ((float)responseData.getBytes().length)/1024.0f);
						String time = String.format("TIME %.2f s", ((float)request.getConnectionTime())/1000.0f);
						sizeStatus.setText(size);
						timeStatus.setText(time);
						if(request.getResponseCode()/100 == 2)
							status.setType(MLabel.OK_TYPE);
						else
							status.setType(MLabel.ERROR_TYPE);
						sizeStatus.setVisible(true);
						status.setVisible(true);
						timeStatus.setVisible(true);
						rawPreview.setText(responseData);
						timeLineText.setText(request.getOutput());
						ep.setContentType("text/html");
						ep.setText(responseData);
						ep.setEditable(false);
					}
				};
				new Thread(task,"").start();
			}
		});
		
		impComponents.put("URL Field", urlField);
		impComponents.put("Methods", requestMethods);
		impComponents.put("Request Headers", ((JPanel) requestTabs.getComponentAt(3)).getComponent(0));
		impComponents.put("JSON Text Area", jsonTextArea);
		impComponents.put("Form Data Text Area", formDataTextArea);
		impComponents.put("File Address Text Field", fileAddress);
		impComponents.put("Data Type ComboBox", requestTabs.getTabComponentAt(0));
	}
	
	/**
	 * Changes the color of the JSplitPane divider.
	 * @param splitPane The JSplitPane to be edited
	 * @param c The new color of the divider
	 */
	private void changeSplitDividerColor(JSplitPane splitPane,Color c)
	{
		splitPane.setUI(new BasicSplitPaneUI() 
		{
		    @Override
		    public BasicSplitPaneDivider createDefaultDivider() 
		    {
		        return new BasicSplitPaneDivider(this) 
		        {
					private static final long serialVersionUID = 1L;

					public void setBorder(Border b) {}

		            @Override
		            public void paint(Graphics g) 
		            {
		                g.setColor(c);
		                g.fillRect(0, 0, getSize().width, getSize().height);
		                super.paint(g);
		            }
		        };
		    }
		});

		splitPane.setBorder(null);
	}
	
	/**
	 * Displays an error message box.
	 * @param message The message box message
	 */
	private void showError(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Handles list double click event. Actually loads the saved file.
	 * @author Mahdi Rezaie 9728040
	 *
	 */
	public class ListHandler extends MouseAdapter
	{
		private FilesList list;
		
		public ListHandler(FilesList f)
		{
			list = f;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) 
		{
			DefaultMutableTreeNode selected = list.getSelectedNode();
			if(arg0.getClickCount() == 2 && selected.isLeaf())
			{
				String path = "";
				for(TreeNode node : selected.getPath())
					path += "/" + node.toString();
				path = path.replaceFirst("/Groups", "./data");
				
				Request request;
				try(
						FileInputStream fis = new FileInputStream(path);
						ObjectInputStream ois = new ObjectInputStream(fis);
						)
				{
					request = (Request) ois.readObject();
				}catch(Exception e) 
				{
					showError(e.getMessage());
					e.printStackTrace();
					return;
				}
				
				((JTextField)impComponents.get("URL Field")).setText(request.getURL().toString());
				
				if(request.getActiveArgument("-m") != null) 
				{
					JComboBox<?> methods = (JComboBox<?>) impComponents.get("Methods");
					String method = request.getActiveArgument("-m").getDatas().get(0);
					methods.setSelectedItem(method.toUpperCase());
				}else
					((JComboBox<?>) impComponents.get("Methods")).setSelectedItem("GET");
				
				if(request.getActiveArgument("-H") != null)
				{
					String data = request.getActiveArgument("-H").getDatas().get(0);
					((HeadersList)impComponents.get("Request Headers")).setHeaders(data);
				}
				
				if(request.getActiveArgument("-j") != null)
				{
					String data = request.getActiveArgument("-j").getDatas().get(0);
					((JTextArea)impComponents.get("JSON Text Area")).setText(data);
					JComboBox<?> c = ((JComboBox<?>)impComponents.get("Data Type ComboBox"));
					c.setSelectedItem("JSON");
					c.getActionListeners()[0].actionPerformed(null);
				}
				
				if(request.getActiveArgument("-d") != null)
				{
					String data = request.getActiveArgument("-d").getDatas().get(0);
					((JTextArea)impComponents.get("Form Data Text Area")).setText(data);
					JComboBox<?> c = ((JComboBox<?>)impComponents.get("Data Type ComboBox"));
					c.setSelectedItem("Form Data");
					c.getActionListeners()[0].actionPerformed(null);
				}
				
				if(request.getActiveArgument("-u") != null)
				{
					String data = request.getActiveArgument("-u").getDatas().get(0);
					((JTextField)impComponents.get("File Address Text Field")).setText(data);
					JComboBox<?> c = ((JComboBox<?>)impComponents.get("Data Type ComboBox"));
					c.setSelectedItem("Binary Data");
					c.getActionListeners()[0].actionPerformed(null);
				}
				
				//System.out.println("Here");
			}
		}
		
	}
	
	/**
	 * Handles JFileChooser events.
	 * @author Mahdi Rezaie 9728040
	 *
	 */
	public class FileChooseHandler implements ActionListener
	{
		private JTextField textField;
		
		public FileChooseHandler(JTextField tf)
		{
			textField = tf;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = fc.showOpenDialog(MainPanel.this);
			if(result == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				textField.setText(file.getPath());
			}
		}
		
		
	}
}
