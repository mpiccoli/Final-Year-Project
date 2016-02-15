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

public class ExecutionQueueView extends JPanel implements ActionListener{
	
	//Declare the constants of this class
	private static final long serialVersionUID = -3926009345985683959L;
	private static final String[] TABLE_COLUMN_NAMES={"Method","N. Cities","Tour Length","Time","Data"};
	private static final Object[][] data={{"No Data",0,0,0,0}};
	private static final Font BOLD_SEGOE_12=new Font("Segoe UI", Font.BOLD, 12);
	private static final Font ITALIC_SEGOE_12=new Font("Segoe UI", Font.ITALIC, 12);
	//Global Objects
	private JFrame mainFrame;
	private JPanel optionsPanel;
	private JButton removeAlg, addAlg;
	private JTable tableAlg;
	private JScrollPane scrollPaneTable;
	private Vector<Object> dataAlgForTable;
	private DefaultTableModel tableModel;
	private DrawingPanel drawPath;
	private JLabel previewLabel;
	
	public ExecutionQueueView(Vector<Object> vec){
		//Create the window
		mainFrame=new JFrame();
		//mainFrame.setBackground(new Color(78, 129, 162));
		mainFrame.setSize(900, 500);
		mainFrame.setTitle("Queue Of Execution");
		mainFrame.setResizable(false);
		mainFrame.setLayout(null);
		//Initialize the global variables
		optionsPanel=new JPanel();
			optionsPanel.setBorder(new TitledBorder("Options"));
			optionsPanel.setLayout(null);
		removeAlg=new JButton("Remove Element");
		addAlg=new JButton("Add Element");
		tableModel=new DefaultTableModel(TABLE_COLUMN_NAMES, 1);
		tableAlg=new JTable(data,TABLE_COLUMN_NAMES);
		scrollPaneTable=new JScrollPane(tableAlg);
			scrollPaneTable.setViewportView(tableAlg);
		drawPath=new DrawingPanel(new Vector<Point>());
			drawPath.setBackground(new Color(125, 158, 183));
			drawPath.setBorder(BorderFactory.createEtchedBorder());
		previewLabel=new JLabel("Shortest Path Found");
		//Change the font of the following objects
		removeAlg.setFont(BOLD_SEGOE_12);
		addAlg.setFont(BOLD_SEGOE_12);
		previewLabel.setFont(ITALIC_SEGOE_12);
		//Set position for the graphical elements
		optionsPanel.setBounds(35,10,320,60);
		removeAlg.setBounds(20,20,130,20);
		addAlg.setBounds(160,20,130,20);
		scrollPaneTable.setBounds(420,10,450,450);
		previewLabel.setBounds(130,80,130,20);
		drawPath.setBounds(35, 100, 340, 325);
		//Add Action Listener to the buttons
		removeAlg.addActionListener(this);
		addAlg.addActionListener(this);
		//Add elements to the frame
		mainFrame.add(optionsPanel);
		optionsPanel.add(removeAlg);
		optionsPanel.add(addAlg);
		mainFrame.add(scrollPaneTable);
		mainFrame.add(drawPath);
		mainFrame.add(previewLabel);
		
		dataAlgForTable=vec;
		//Fill table with data passed from previous view
		this.putDataIntoTable();
		this.performRemoveElementCheck();
		//Make the window visible
		mainFrame.setVisible(true);
	}
	private void performRemoveElementCheck() {
		if(dataAlgForTable.size()>0){
			removeAlg.setEnabled(true);
		}
		else{
			removeAlg.setEnabled(false);
		}		
	}
	
	private void putDataIntoTable(){
		tableModel.setColumnIdentifiers(TABLE_COLUMN_NAMES);
		tableAlg.setModel(tableModel);
		if(dataAlgForTable.size()>0){
			tableModel.removeRow(0);
			//Add data contained in the vector passed by the previous windows to the table of this view
			for(int i=0; i<dataAlgForTable.size(); i++){
				Object temp=dataAlgForTable.elementAt(i);
				if (temp instanceof GenResultData ){
					long timeExtraction=((GenResultData)temp).getExecutionTime();
					String tempTime=String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeExtraction),TimeUnit.MILLISECONDS.toSeconds(timeExtraction)%60, (timeExtraction%100));
					tableModel.addRow(new Object[]{"Genetic",((GenResultData) temp).getPopSize(),((GenResultData) temp).getFitness(),tempTime,"Open"});
				}
				else if(temp instanceof TradResultData){
					long timeExtraction=((TradResultData) temp).getExecutionTime();
					String tempTime=String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeExtraction),TimeUnit.MILLISECONDS.toSeconds(timeExtraction)%60, (timeExtraction%100));
					tableModel.addRow(new Object[]{((TradResultData) temp).getAlgName(),((TradResultData) temp).getNumCities(),((TradResultData) temp).getTourLength(),tempTime,"Open"});
				}
			}
	        tableAlg.setModel(tableModel);
	        tableAlg.getColumn("Data").setCellRenderer(new ButtonRenderer(dataAlgForTable, drawPath));
	        tableAlg.getColumn("Data").setCellEditor(new ButtonEditor(new JCheckBox(), tableAlg, dataAlgForTable));
	        tableAlg.setRowHeight(25);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(removeAlg)){
			if(tableAlg.getSelectedRow()!=-1){
				dataAlgForTable.remove(tableAlg.getSelectedRow());
				tableModel.removeRow(tableAlg.getSelectedRow());
				this.performRemoveElementCheck();
			}
			else{
				JOptionPane.showMessageDialog(null, "Please select an algorithm you would like to remove!","Warning",JOptionPane.WARNING_MESSAGE);
			}
		}
		if(e.getSource().equals(addAlg)){
			mainFrame.setVisible(false);
			//Destroy the object from memory
			mainFrame.dispose();
		}
	}
}

class ButtonRenderer extends JButton implements TableCellRenderer {
	private static final long serialVersionUID = 3144985203935446353L;
	private Vector<Object> data;
	private DrawingPanel drawArea;
    public ButtonRenderer(Vector<Object> d, DrawingPanel area) {
    	data=d;
    	drawArea=area;
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
    	//Define actions for when an item in the table is selected
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            //Display the resulting path
            Object temp=data.get(table.getSelectedRow());
            
        	if(temp instanceof TradResultData){
        		TradResultData tem=(TradResultData)temp;
        		Vector<Point> pathPreview=tem.getResultingPoints();
        		drawArea.updateLinksAndRefresh(pathPreview);
        	}
        	else if(temp instanceof GenResultData){
        		GenResultData tem=(GenResultData)temp;
        		Vector<Point> pathPreview=tem.getResultingPoints();
        		drawArea.updateLinksAndRefresh(pathPreview);
        	}
        } 
        else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 7406695248070812217L;
	protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private Vector<Object> data;

    public ButtonEditor(JCheckBox checkBox, JTable tab, Vector<Object> d) {
        super(checkBox);
        table=tab;
        data=d;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @SuppressWarnings("unused")
	@Override
    public Object getCellEditorValue() {
    	//Define actions for when the user clicks on the 'Open" button to view more specific results
        if (isPushed) {
            //PopUp ResultView Window for the selected row
        	Object temp=data.get(table.getSelectedRow());
        	if(temp instanceof TradResultData){
        		ResultsView resView=new ResultsView("Result - Traditional Method", "tra",(TradResultData)temp);
        	}
        	else{
        		ResultsView resView=new ResultsView("Result - Genetic Method", "gen",(GenResultData)temp);
        	}
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
