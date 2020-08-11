import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * This special class is a MList. It's items are headers JTextFields which are
 * headers keys and values. Also there are disabled fields that when user clicks
 * on them, they will be enabled and another disabled fields will be added.
 * 
 * @author Mahdi Rezaie 9728040
 *
 */
public class HeadersList extends MList
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes the list and fields.
	 */
	public HeadersList()
	{
		super(30,10);
		addNewFields();
	}
	
	/**
	 * Adds disabled fields to end of the list.
	 */
	private void addNewFields()
	{
		JPanel fields = new JPanel(new GridLayout(1,2));						// Creating fields and buttons
		JTextField f1 = new JTextField();
		JTextField f2 = new JTextField();
		fields.add(f1); fields.add(f2);
		JButton remove = new JButton("R");
		JPanel item = new JPanel(new BorderLayout());
		item.add(remove,BorderLayout.EAST);
		item.add(fields,BorderLayout.CENTER);
		
		f1.setEnabled(false);													// Setting components status
		f2.setEnabled(false);
		remove.setVisible(false);
		MyMouseAdapter ma = new MyMouseAdapter(f1,f2,remove);
		f1.addMouseListener(ma);
		f2.addMouseListener(ma);
		remove.addActionListener(new MyActionListener());
		
		super.addItem(item);
	}
	
	/**
	 * Adds new enabled fields and fill them with key and value.
	 * @param key The first field text
	 * @param value The second field text
	 */
	public void addItem(String key, String value)
	{
		int index = getItems().length;
		JPanel lastItem = getItems()[index-1];
		JTextField keyField = (JTextField)((JPanel)lastItem.getComponent(1)).getComponent(0);
		JTextField valueField = (JTextField)((JPanel)lastItem.getComponent(1)).getComponent(1);
		keyField.getMouseListeners()[0].mouseClicked(null);
		keyField.setText(key);
		valueField.setText(value);
	}
	
	/**
	 * Returns values of a column.
	 * @param column Column 0 or 1
	 * @return The keys or values array
	 */
	public String[] getItemsFields(int column)
	{
		ArrayList<String> keys = new ArrayList<String>();
		JPanel[] items = getItems();
		for(int i = 0; i < items.length - 1; ++i)
		{
			JTextField f1 = (JTextField)((JPanel)items[i].getComponent(1)).getComponent(column);
			keys.add(f1.getText());
		}
		
		return keys.toArray(new String[keys.size()]);
	}
	
	/**
	 * Gets the headers from a string and creates items and fills them.
	 * @param data The headers string
	 */
	public void setHeaders(String data)
	{
		removeAllItems();
		data = mURL.Request.trimCommas(data);
		String[] values = data.split(";");
		for(String value : values)
		{
			String[] parts = value.split(":");
			JPanel fields = new JPanel(new GridLayout(1,2));
			
			JTextField f1 = new JTextField(parts[0]);
			JTextField f2 = new JTextField(parts[1]);
			fields.add(f1); fields.add(f2);
			JButton remove = new JButton("R");
			JPanel item = new JPanel(new BorderLayout());
			item.add(remove,BorderLayout.EAST);
			item.add(fields,BorderLayout.CENTER);
			
			remove.addActionListener(new MyActionListener());
			
			super.addItem(item);
		}
		addNewFields();
	}
	
	/**
	 * This is a mouse listener for remove button.
	 * @author Mahdi Rezaie 9728040
	 *
	 */
	public class MyMouseAdapter extends MouseAdapter
	{
		private JTextField f1,f2;
		private JButton btn;
		
		public MyMouseAdapter(JTextField f1, JTextField f2, JButton btn)
		{
			this.f1 = f1;
			this.f2 = f2;
			this.btn = btn;
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if(!f1.isEnabled())
			{
				f1.setEnabled(true);
				f2.setEnabled(true);
				btn.setVisible(true);
				addNewFields();
			}
		}
	}
	
	public class MyActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			removeItem(((JButton)arg0.getSource()).getParent());
		}
		
	}
}
