import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * This class is the settings menu frame. It is a tiny frame and always open
 * at center of the main frame.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class PreferrencesFrame extends JFrame
{
	private final int width = 300;
	private final int height = 120;
	private JFrame mainFrame;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor initializes all components of the frame by calling related methods. 
	 */
	public PreferrencesFrame(JFrame mainFrame)
	{
		super("Preferrences");
		this.mainFrame = mainFrame;
		initFrame();
		createComponents();
	}
	
	/**
	 * Sets frame status.
	 */
	private void initFrame()
	{
		setSize(width,height);
		setResizable(false);
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(getBackground().darker(), 2));
		setContentPane(panel);
		setUndecorated(true);
		setAlwaysOnTop(true);
	}
	
	/**
	 * Creates all of the components of the setting frame.
	 */
	private void createComponents()
	{
		JPanel settings = new JPanel(new BorderLayout());							// Creating Main panel
		
		JPanel generalPanel = new JPanel();											// Creating CheckBoxes
		generalPanel.setLayout(new BoxLayout(generalPanel,BoxLayout.Y_AXIS));
		generalPanel.add(new JLabel("  General"));
		JCheckBox c1 = new JCheckBox("Follow redirects auotmatically");
		JCheckBox c2 = new JCheckBox("Hide in tray on close");
		generalPanel.add(c1); generalPanel.add(c2);
		c1.setAlignmentX(Component.LEFT_ALIGNMENT);
		generalPanel.add(new JSeparator());
		settings.add(generalPanel,BorderLayout.CENTER);
		
		JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));			// Creating radio buttons
		ButtonGroup themes = new ButtonGroup();
		JRadioButton dark = new JRadioButton("Dark Theme");
		JRadioButton light = new JRadioButton("Light Theme");
		themes.add(light); themes.add(dark);
		dark.setSelected(true);
		themePanel.add(new JLabel("Themes:"));
		themePanel.add(dark);
		themePanel.add(light);
		settings.add(themePanel,BorderLayout.SOUTH);
		
		add(settings,BorderLayout.CENTER);
		
		JPanel okPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));				// Creating OK and Cancel buttons
		JButton canelButton = new JButton("Cancel");
		JButton okButton = new JButton("OK");
		okPanel.add(okButton); okPanel.add(canelButton);
		
		add(okPanel,BorderLayout.SOUTH);
		
		canelButton.addActionListener(new ActionListener() 							// Setting the cancel eventhandler
		{	
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				mainFrame.setEnabled(true);
				setVisible(false);
			}
		});
	}
}
