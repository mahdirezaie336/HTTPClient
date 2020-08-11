import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/**
 * This is a MList which has two JTextAreas for key-value headers. 
 * @author Mahdi Rezaie 9728040
 *
 */
public class ResponseHeadersFields extends MList
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public ResponseHeadersFields() {
		super(10);
	}
	
	/**
	 * Gets headers from input in form of a Map Collection and adds them.
	 * @param headers The hears map
	 */
	public void setFields(Map<String,List<String>> headers)
	{
		removeAllItems();																	// Creating the text areas
		if(headers == null) return;
		for(String key : headers.keySet())
		{
			if(key == null) continue;
			if(key.contains("Cookie") || key.contains("cookie")) continue;
			JPanel item = new JPanel(new GridLayout(1,2,5,5));
			JTextArea keyLabel = new JTextArea(key+":");
			JTextArea valueLabel = new JTextArea(headers.get(key).toString());
			keyLabel.setLineWrap(true); 	valueLabel.setLineWrap(true);
			keyLabel.setEditable(false); 	valueLabel.setEditable(false);
			keyLabel.setBackground(getBackground().brighter());;		valueLabel.setBackground(getBackground().brighter());
			item.add(keyLabel);				item.add(valueLabel);
			addItem(item);
		}
		JPanel item = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton btn = new JButton("Copy to clipboard");
		item.add(btn);
		addItem(item);
		
		btn.addActionListener(new ActionListener() {										// The copy to clipboard button
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String headersString = "";
				for(String key : headers.keySet())
					headersString += key + ":" + headers.get(key) + ";";
				StringSelection selection = new StringSelection(headersString);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
		});
	}
}
