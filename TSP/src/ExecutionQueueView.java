import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class ExecutionQueueView extends JPanel implements ActionListener{
	private static final long serialVersionUID = -3926009345985683959L;
	private JFrame mainFrame;
	private JButton removeAlg;
	private JTable tableAlg;
	private JScrollPane scrollPaneTable;
	private Vector<Object> dataAlgForTable;
	private DefaultTableModel tableModel;
	private static final String[] TABLE_COLUMN_NAMES={"Method","N. Cities","Tour Length","Time","Data"};
	private static final Object[][] data={{"No Data",0,0,0,0}};
	private DrawingPanel drawPath;
	private JLabel previewLabel;
	
	public ExecutionQueueView(Vector<Object> vec){
		//Create the window
		mainFrame=new JFrame();
		mainFrame.setSize(900, 500);
		mainFrame.setTitle("Execution");
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		mainFrame.setLayout(null);
		//Initialize the global variables
		removeAlg=new JButton("Remove Element");
		tableModel=new DefaultTableModel(TABLE_COLUMN_NAMES, 1);
		tableAlg=new JTable(data,TABLE_COLUMN_NAMES);
		scrollPaneTable=new JScrollPane(tableAlg);
			scrollPaneTable.setViewportView(tableAlg);
		drawPath=new DrawingPanel(new Vector<Point>());
		previewLabel=new JLabel("Shortest Path");
		//Set position for the graphical elements
		removeAlg.setBounds(350,15,130,20);
		scrollPaneTable.setBounds(490,10,380,450);
		drawPath.setBounds(35, 85, 340, 325);
		previewLabel.setBounds(15,45,100,20);
		//Add Action Listener to the buttons
		removeAlg.addActionListener(this);
		//Add elements to the frame
		mainFrame.add(removeAlg);
		mainFrame.add(scrollPaneTable);
		mainFrame.add(drawPath);
		mainFrame.add(previewLabel);
		dataAlgForTable=vec;
		//Fill table with data passed from previous view
		this.putDataIntoTable();
		this.performRemoveElementCheck();
	}
	private void performRemoveElementCheck() {
		if(dataAlgForTable.size()>0){
			removeAlg.setEnabled(true);
		}
		else{
			removeAlg.setEnabled(false);
		}		
	}
	//"Method","N. Cities","Tour Length","Time","Data"
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
            setForeground(table.getSelectionForeground());
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