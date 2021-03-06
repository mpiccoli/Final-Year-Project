import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see Vector, JTable, JButton, Border, Font, JButton, DrawingPanel, JLabel, JFrame, JPanel, ActionListener
 * 
 * This Class allows the user to view and edit the queue of execution
 * 
 */
public class ExecutionQueueView extends JPanel implements ActionListener {

	// Declare the constants of this class
	private static final long serialVersionUID = -3926009345985683959L;
	private static final String[] TABLE_COLUMN_NAMES = { "Method", "N. Cities", "Tour Length", "Time", "Data" };
	private static final Object[][] data = { { "No Data", 0, 0, 0, 0 } };
	private static final Font BOLD_SEGOE_12 = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font ITALIC_SEGOE_12 = new Font("Segoe UI", Font.ITALIC, 12);
	// Global Variables
	private JFrame mainFrame;
	private JPanel optionsPanel;
	private JButton removeAlg, addAlg;
	private JTable tableAlg;
	private JScrollPane scrollPaneTable;
	private Vector<Object> dataAlgForTable;
	private DefaultTableModel tableModel;
	private DrawingPanel drawPath;
	private JLabel previewLabel;

	/*
	 * @param vec Vector containing the data to display in the table
	 * 
	 * Constructor with 1 parameter
	 */
	public ExecutionQueueView(Vector<Object> vec) {
		// Create the window and set its size, title and layout
		mainFrame = new JFrame();
		mainFrame.setSize(900, 500);
		mainFrame.setTitle("Queue Of Execution");
		mainFrame.setResizable(false);
		mainFrame.setLayout(null);
		// Initialize the global variables
		optionsPanel = new JPanel();
		optionsPanel.setBorder(new TitledBorder("Options"));
		optionsPanel.setLayout(null);
		removeAlg = new JButton("Remove Element");
		addAlg = new JButton("Add Element");
		tableModel = new DefaultTableModel(TABLE_COLUMN_NAMES, 1);
		tableAlg = new JTable(data, TABLE_COLUMN_NAMES);
		scrollPaneTable = new JScrollPane(tableAlg);
		scrollPaneTable.setViewportView(tableAlg);
		drawPath = new DrawingPanel(new Vector<Point>());
		drawPath.setBackground(new Color(125, 158, 183));
		drawPath.setBorder(BorderFactory.createEtchedBorder());
		previewLabel = new JLabel("Shortest Path Found");
		// Change the font of the following objects
		removeAlg.setFont(BOLD_SEGOE_12);
		addAlg.setFont(BOLD_SEGOE_12);
		previewLabel.setFont(ITALIC_SEGOE_12);
		// Set position for the graphical elements
		optionsPanel.setBounds(35, 10, 320, 60);
		removeAlg.setBounds(20, 20, 130, 20);
		addAlg.setBounds(160, 20, 130, 20);
		scrollPaneTable.setBounds(420, 10, 450, 450);
		previewLabel.setBounds(130, 80, 130, 20);
		drawPath.setBounds(35, 100, 340, 325);
		// Add Action Listener to the buttons
		removeAlg.addActionListener(this);
		addAlg.addActionListener(this);
		// Add elements to the frame
		mainFrame.add(optionsPanel);
		optionsPanel.add(removeAlg);
		optionsPanel.add(addAlg);
		mainFrame.add(scrollPaneTable);
		mainFrame.add(drawPath);
		mainFrame.add(previewLabel);
		// store the reference of the vector into the global variable
		dataAlgForTable = vec;
		// Fill table with data passed from previous view
		this.putDataIntoTable();
		// Enable/Disable remove button depending on the number of elements in
		// the table
		this.performRemoveElementCheck();
		// Make the window visible
		mainFrame.setVisible(true);
	}

	/*
	 * This method verifies the number of elements in the table and
	 * enable/disable the remove button accordingly
	 */
	private void performRemoveElementCheck() {
		if (dataAlgForTable.size() > 1)
			removeAlg.setEnabled(true);
		else
			removeAlg.setEnabled(false);
	}

	/*
	 * Reference:
	 * http://stackoverflow.com/questions/13833688/adding-jbutton-to-jtable
	 * 
	 * This method inserts the data into the table and creates a new button for
	 * each entry
	 */
	private void putDataIntoTable() {
		// Set the columns name and the table model
		tableModel.setColumnIdentifiers(TABLE_COLUMN_NAMES);
		tableAlg.setModel(tableModel);
		if (dataAlgForTable.size() > 0) {
			// Remove the first element from the table, which contains test data
			tableModel.removeRow(0);
			// Add data contained in the vector passed by the previous windows
			// to the table of this view
			for (int i = 0; i < dataAlgForTable.size(); i++) {
				Object temp = dataAlgForTable.elementAt(i);
				if (temp instanceof GenResultData) {
					long timeExtraction = ((GenResultData) temp).getExecutionTime();
					// Convert the time into minutes, second, milliseconds
					String tempTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeExtraction),
							TimeUnit.MILLISECONDS.toSeconds(timeExtraction) % 60, (timeExtraction % 100));
					tableModel.addRow(new Object[] { "Genetic", ((GenResultData) temp).getnumCities(),
							((GenResultData) temp).getFitness(), tempTime, "Open" });
				} else if (temp instanceof TradResultData) {
					long timeExtraction = ((TradResultData) temp).getExecutionTime();
					// Convert the time into minutes, second, milliseconds
					String tempTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeExtraction),
							TimeUnit.MILLISECONDS.toSeconds(timeExtraction) % 60, (timeExtraction % 100));
					tableModel.addRow(
							new Object[] { ((TradResultData) temp).getAlgName(), ((TradResultData) temp).getNumCities(),
									((TradResultData) temp).getTourLength(), tempTime, "Open" });
				}
			}
			tableAlg.setModel(tableModel);
			// Create and add a button for each table entry
			tableAlg.getColumn("Data").setCellRenderer(new ButtonRenderer(dataAlgForTable, drawPath));
			tableAlg.getColumn("Data").setCellEditor(new ButtonEditor(new JCheckBox(), tableAlg, dataAlgForTable));
			tableAlg.setRowHeight(25);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Perform these actions when the user requests the removal of an object
		// in the table
		if (e.getSource().equals(removeAlg)) {
			if (tableAlg.getSelectedRow() != -1) {
				dataAlgForTable.remove(tableAlg.getSelectedRow());
				tableModel.removeRow(tableAlg.getSelectedRow());
				this.performRemoveElementCheck();
			}
			// Display this message in case no element has been selected from
			// the table
			else
				JOptionPane.showMessageDialog(null, "Please select an algorithm you would like to remove!", "Warning",
						JOptionPane.WARNING_MESSAGE);
		}
		// Perform these actions when the user requests to add a new algorithm
		// in the queue
		if (e.getSource().equals(addAlg)) {
			mainFrame.setVisible(false);
			// Destroy the object and free the memory
			mainFrame.dispose();
		}
	}
}

/*
 * Reference
 * http://stackoverflow.com/questions/13833688/adding-jbutton-to-jtable
 * 
 * This class allows to select data from the table and perform actions with it
 * 
 */
class ButtonRenderer extends JButton implements TableCellRenderer {

	// Declare the constants of this class
	private static final long serialVersionUID = 3144985203935446353L;
	// Global Variables
	private Vector<Object> data;
	private DrawingPanel drawArea;

	/*
	 * @param d vector containing the reference to the data from the table
	 */
	public ButtonRenderer(Vector<Object> d, DrawingPanel area) {
		data = d;
		drawArea = area;
		setOpaque(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.
	 * swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// Define actions for when an item in the table is selected
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			// Display the resulting path from the object selected in the table
			Object temp = data.get(table.getSelectedRow());
			// Verify what type of object it is
			if (temp instanceof TradResultData) {
				TradResultData tem = (TradResultData) temp;
				Vector<Point> pathPreview = tem.getResultingPoints();
				// Extract the final path from the object and display it
				drawArea.updateLinksAndRefresh(pathPreview);
			} else if (temp instanceof GenResultData) {
				GenResultData tem = (GenResultData) temp;
				Vector<Point> pathPreview = tem.getResultingPoints();
				// Extract the final path from the object and display it
				drawArea.updateLinksAndRefresh(pathPreview);
			}
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		setText((value == null) ? "" : value.toString());
		return this;
	}
}

/*
 * Reference
 * http://stackoverflow.com/questions/13833688/adding-jbutton-to-jtable
 * 
 * This class allows the button previously inserted in each row of the table, to
 * be clickable and perform actions
 */
class ButtonEditor extends DefaultCellEditor {

	// Declare the constants of this class
	private static final long serialVersionUID = 7406695248070812217L;
	// Global Variables
	protected JButton button;
	private String label;
	private boolean isPushed;
	private JTable table;
	private Vector<Object> data;

	/*
	 * @param checkbox Table element selected
	 * 
	 * @param tab Table object
	 * 
	 * @param d Vector of data
	 * 
	 * Constructor with 3 parameters
	 * 
	 */
	public ButtonEditor(JCheckBox checkBox, JTable tab, Vector<Object> d) {
		super(checkBox);
		table = tab;
		data = d;
		button = new JButton();
		button.setOpaque(true);
		// Add action listener to the button
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.
	 * JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// Highlight the row of the selected element
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		}
		// Deselect the row previously selected
		else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultCellEditor#getCellEditorValue()
	 */
	@SuppressWarnings("unused")
	@Override
	public Object getCellEditorValue() {
		// Define actions for when the user clicks on the 'Open" button to view
		// more specific results
		if (isPushed) {
			// PopUp ResultView Window for the selected element
			Object temp = data.get(table.getSelectedRow());
			if (temp instanceof TradResultData) {
				ResultsView resView = new ResultsView("Result - Traditional Method", "tra", (TradResultData) temp);
			} else {
				ResultsView resView = new ResultsView("Result - Genetic Method", "gen", (GenResultData) temp);
			}
		}
		isPushed = false;
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultCellEditor#stopCellEditing()
	 */
	@Override
	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractCellEditor#fireEditingStopped()
	 */
	@Override
	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}
