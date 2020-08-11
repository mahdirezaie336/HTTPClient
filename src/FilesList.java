import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 * This is a JScrollPane which contains a JTree inside it. This class gets a directory
 * from constructor and opens and shows it into the JTree.
 *  
 * @author Mahdi Rezaie 9728040
 *
 */
public class FilesList extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	private JTree tree;														// The files tree
	private String dir;														// The dierctory to be displayed
	
	/**
	 * Gets the directory field as input.
	 * @param directory The directory to be displayed.
	 */
	public FilesList(String directory)
	{
		super();
		dir = directory;
		init();
	}
	
	/**
	 * Initializes the JTree and other variables.
	 */
	private void init()
	{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Groups",true);
		getList(root,new File(dir));
		tree = new JTree(root);
		setViewportView(tree);
	}
	
	/**
	 * Gets the list of files.
	 * @param node The node to add list to
	 * @param file The directory to be added to tree
	 */
	private void getList(DefaultMutableTreeNode node, File file)
	{
		if(file.isFile())
		{
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.getName(),false);
			node.add(child);
		}else if(file.isDirectory())
		{
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.getName(),true);
			File[] files = file.listFiles();
			for(File subFile : files)
				getList(child,subFile);
			node.add(child);
		}
	}
	
	/**
	 * Returns the selected JTree node.
	 * @return The selected JTree node
	 */
	public DefaultMutableTreeNode getSelectedNode()
	{
		return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	}
	
	/**
	 * Gets a mouse listener from input and adds it to JTree listeners.
	 * @param l The mouse listener to be added
	 */
	public void addMouseListenerToTree(MouseListener l)
	{
		tree.addMouseListener(l);
	}
}
