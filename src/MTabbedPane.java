import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.*;
/**
 * A special JTabbedPane which can have a tab with multipanels. The tab with multipanels
 * has a ComboBox on the tab.
 * @author Mahdi Rezie 9728040
 *
 */
public class MTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes the TabbedPane and sets the layout policy to scrollable.
	 */
	public MTabbedPane()
	{
		super();
		super.setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
	}
	
	/**
	 * Adds a tab with multipanels.
	 * @param datas The HashMap for multipanels
	 */
	public void addMultiTab(LinkedHashMap<String,JPanel> datas)
	{
		JPanel mainPanel = new JPanel(new BorderLayout());
		int newIndex = getTabCount();
		addTab(""+newIndex, mainPanel);
		int n = indexOfComponent(mainPanel);
		
		JComboBox<String> comboBox = new JComboBox<String>(datas.keySet().toArray(new String[datas.size()]));
		setTabComponentAt(n, comboBox);
		comboBox.setBorder(BorderFactory.createEmptyBorder());
		comboBox.setBackground(getBackground());
		
		comboBox.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setSelectedIndex(n);
				//int panelIndex = comboBox.getSelectedIndex();
				String selected = (String) comboBox.getSelectedItem();
				mainPanel.removeAll();
				if(datas.get(selected)!=null)
					mainPanel.add(datas.get(selected),BorderLayout.CENTER);
				mainPanel.updateUI();
			}
		});
		comboBox.getActionListeners()[0].actionPerformed(new ActionEvent("", 0, ""));
	}
}
