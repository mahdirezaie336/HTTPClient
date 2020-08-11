import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
/**
 * This class is a ScrollPane containing a panel with BoxLayout. This can have
 * some items of JPanels which are shown in rows and fills all panel width.
 * @author Mahdi Rezaie 9728040
 *
 */
public class MList extends JScrollPane
{
	private static final long serialVersionUID = 4035309613005009109L;
	private JPanel panel;																		// The main panel
	private LinkedList<JPanel> items;															// Items of list
	private int cellHeight;																		// Each item maximum height
	
	/**
	 * Initializes the items list and main panel and sets cell height.
	 * @param cellHeight The cell height
	 * @param margin Distance from margins
	 */
	public MList(int cellHeight, int margin)
	{
		super();
		this.cellHeight = cellHeight;
		items = new LinkedList<JPanel>();
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		setViewportView(panel);
		panel.setBorder(new EmptyBorder(margin,margin,margin,margin));
	}
	
	/**
	 * Sets the cell height to -1.
	 * @param margin Distance from margins
	 */
	public MList(int margin)
	{
		this(-1,margin);
	}
	
	/**
	 * Adds new JPanel item to the list
	 * @param item The item to be added
	 * @return The added panel itself
	 */
	public JPanel addItem(JPanel item)
	{
		if(item == null) return null;
		if(cellHeight != -1)
		{
			item.setMaximumSize(new Dimension(10000,cellHeight));
			item.setMinimumSize(new Dimension(100,cellHeight));
		}
		items.add(item);
		update();
		return item;
	}
	
	/**
	 * Clears the list and updates it.
	 */
	public void removeAllItems()
	{
		items.clear();
		update();
	}
	
	/**
	 * Removes a specified item.
	 * @param item The item to be removed.
	 */
	public void removeItem(Object item)
	{
		items.remove(item);
		update();
	}
	
	/**
	 * Gets the items list
	 * @return The items list
	 */
	public JPanel[] getItems()
	{
		return items.toArray(new JPanel[items.size()]);
	}
	
	/**
	 * removes all components from panel and adds the list to that again.
	 */
	protected void update()
	{
		panel.removeAll();
		for(JPanel item : items)
			panel.add(item);
		updateUI();
		panel.updateUI();
	}
}
