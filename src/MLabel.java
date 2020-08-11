import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * This class is a child class of JLabel. Instances of this class has more
 * distance from text to margins of the background.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class MLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	private static final Color OK_COLOR = Color.decode("#75BA24");
	private static final Color NO_COLOR = Color.decode("#E0E0E0");
	private static final Color ERROR_COLOR = Color.decode("#DD5800");
	public static final int NO_TYPE = 0, OK_TYPE = 1, ERROR_TYPE = 2;
	
	
	/**
	 * Constructor gets the text and calls the parent's constructor.
	 * 
	 * @param label The label text
	 */
	public MLabel(String label)
	{
		super(label);
		super.setHorizontalAlignment(JLabel.CENTER);
		Border border = BorderFactory.createLineBorder(super.getBackground().darker(), 1);
		super.setBorder(border);
	}
	
	/**
	 * Sets a color for a label
	 * @param type
	 */
	public void setType(int type)
	{
		switch(type)
		{
		case OK_TYPE:
			setBackground(OK_COLOR);
			break;
		case ERROR_TYPE:
			setBackground(ERROR_COLOR);
			break;
		default:
			setBackground(NO_COLOR);
		}
	}
	
	/**
	 * Changes the label background. Also changes the foreground if the background colors
	 * are "#75ba24" and "#e0e0e0"
	 */
	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		Border border = BorderFactory.createLineBorder(super.getBackground().darker(), 1);
		super.setBorder(border);
		if(bg.equals(OK_COLOR))
			super.setForeground(Color.white);
		else if(bg.equals(NO_COLOR))
			super.setForeground(Color.black.brighter());
		else if(bg.equals(ERROR_COLOR))
			super.setForeground(Color.white);
	}
	
	/**
	 * Makes more distance form the background margins.
	 */
	@Override
	public Dimension getPreferredSize()
	{
		Dimension dim = super.getPreferredSize();
		return new Dimension(dim.width+15,dim.height+5);
	}
	
	public Dimension getMinimumSize()
	{
		Dimension dim = super.getMinimumSize();
		return new Dimension(dim.width+15,dim.height+5);
	}
}
