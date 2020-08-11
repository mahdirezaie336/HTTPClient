import javax.swing.*;

import com.formdev.flatlaf.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * This class is the main frame of the program which includes all components
 * on the program main environment.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class MainFrame 
{
	private JFrame frame;														// Main Frame
	private PreferrencesFrame preferrencesFrame;								// Settings Frame
	private MainPanel mainPanel;
	
	/**
	 * Constructor gets the title and initializes all components by calling
	 * init method.
	 * @param title The title of the frame
	 */
	public MainFrame(String title)
	{
		this.frame = new JFrame(title);
		setLookAndFeel();
		mainPanel = new MainPanel();
		createFrame();
		createMenuBar();
		frame.setLayout(new GridLayout());
		frame.add(mainPanel);
	}
	
	/**
	 * Sets the app look and feel to the FlatDarkLAF.
	 */
	private void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(new FlatDarkLaf());
		}catch(Exception e)
		{
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}catch(Exception ex){}
		}
	}
	
	/**
	 * Initializes all status of the frame.
	 */
	private void createFrame()
	{
		frame.setMinimumSize(new Dimension(500,400));
		frame.setBackground(Color.magenta);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		preferrencesFrame = new PreferrencesFrame(frame);
	}
	
	/**
	 * Creates the program menu bar.
	 */
	private void createMenuBar()
	{
		JMenuBar bar = new JMenuBar();
		
		JMenu application = new JMenu("Application");								// JMenus
		//JMenu edit = new JMenu("Edit");
		JMenu view = new JMenu("View");
		//JMenu window = new JMenu("Window");
		//JMenu tools = new JMenu("Tools");
		JMenu help = new JMenu("Help");
		
		JMenuItem preferences = new JMenuItem("Preferences");						// Items
		JMenuItem changelog = new JMenuItem("Changelog");
		JMenuItem quit = new JMenuItem("Quit");
		application.add(preferences);
		application.add(changelog);
		application.addSeparator();
		application.add(quit);
		
		/*JMenuItem undo = new JMenuItem("Undo");
		JMenuItem redo = new JMenuItem("Redo");
		edit.add(undo);
		edit.add(redo);*/
		
		JMenuItem toggleFullScreen = new JMenuItem("Toggle Fullscreen");
		JMenuItem toggleSidebar = new JMenuItem("Toggle Sidebar");
		view.add(toggleFullScreen);
		view.add(toggleSidebar);
		
		JMenuItem about = new JMenuItem("About");
		JMenuItem helpI = new JMenuItem("Help");
		help.add(about);
		help.add(helpI);
		
		bar.add(application);
		//bar.add(edit);
		bar.add(view);
		//bar.add(window);
		//bar.add(tools);
		bar.add(help);
		
		frame.setJMenuBar(bar);
		
		preferences.addActionListener(new ActionListener() 							// Event Handlers
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if(!preferrencesFrame.isVisible())
				{
					Point mfLoc = frame.getLocation();
					Dimension mfSize = frame.getSize();
					Dimension thisSize = preferrencesFrame.getSize();
					int x = (int)mfLoc.getX() + (mfSize.width - thisSize.width)/2;
					int y = (int)mfLoc.getY() + (mfSize.height - thisSize.height)/2;
					
					preferrencesFrame.setLocation(x,y);
					preferrencesFrame.setVisible(true);
					frame.setEnabled(false);
				}else
				{
					preferrencesFrame.setVisible(false);
					frame.setEnabled(true);
				}
			}
		});
		
		quit.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});
		
		toggleFullScreen.addActionListener(new ActionListener() 
		{
			private Point prevLoc;
			private Dimension prevDim;
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if(!frame.isUndecorated())
				{
					prevLoc = frame.getLocation();
					prevDim = frame.getSize();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.dispose();
					frame.setUndecorated(true);
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				}else
				{
					frame.dispose();
					frame.setUndecorated(false);
					frame.pack();
					frame.setSize(prevDim);
					frame.setLocation(prevLoc);
					frame.setVisible(true);
				}
			}
		});
		
		preferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.ALT_MASK));		// Accelerators
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.ALT_MASK));
		toggleFullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,KeyEvent.VK_ALT));
		
	}
	
	/**
	 * Displays the frame.
	 */
	public void show()
	{
		frame.setVisible(true);
	}
}
