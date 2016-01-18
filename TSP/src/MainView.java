import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.omg.CORBA.portable.InputStream;

public class MainView extends JPanel implements ActionListener, PropertyChangeListener, ChangeListener {

	//Graphical Elements
	private JFrame mainFrame;
	JToolBar toolBar;
	private MatteBorder matteB;
	JMenuBar menuBar;
	private JMenu fileMenu, impExp;
	private JMenuItem close, loadMap, info, saveRes, expPoints;
	private JButton undoPoint, resetAllPoints;
	private JLabel numDrawnPoints;
	private JComboBox<String> tradGAChooser;
	//Traditional and Genetic Algorithms
	private JPanel tradPanel, genPanel, optionPanel, destinationPanel, executionPanel;
	private JCheckBox trad1CB,trad2CB,trad3CB,trad4CB, manualDrawCB, importPointsCB, numCasualPointsCB;
	private JLabel popSizeLabel, fromLabel, toLabel, maxGenLabel, crossoverLabel, mutationLabel, crossoverProbLabel, mutationProbLabel, casualPointsLabel, manualPointsLabel;
	private JTextField popSizeTF, fromTF, toTF, maxGenTF, casualPointsTF;
	private JComboBox<String> crossoverMethods, mutationMethods;
	private JSlider crossProbSlider, mutProbSlider;
	private JButton goDrawButton, startExecutionButton, addToExecution, automaticExecution, viewResultsButton;
	private boolean importXmlJson;
	//Drawing area which allows the user to draw points and perform the TSP on those points
	private DrawingPanel drawingArea;
	//Global Objects
	private ExitRequest exitReq;
	private Vector<Point> points;
	private Vector<Object> algQueueExecution;
	private JFileChooser jpgImport;
	private FileNameExtensionFilter jpgFilter;
	private String crossoverSelected, mutationSelected;
	private float crossoverProb, mutationProb;
	
	public MainView(JFrame frame){
		this.mainFrame=frame;
		//Add a window listener so the programmer can decide what operations execute when certain window action are about to occur
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent wE){
				//Ask the user confirm to exit the application
				initExitRequest();
			}
		});
		this.setLayout(null);
		//Create the Menu Bar and its elements
		toolBar=new JToolBar();
		toolBar.setSize(800,40);
		matteB=new MatteBorder(1,0,0,0, Color.BLACK);
		menuBar=new JMenuBar();
		fileMenu=new JMenu("File");
		impExp=new JMenu("Import/Export");
		close=new JMenuItem("Close");
		loadMap=new JMenuItem("Load Map");
		info=new JMenuItem("Info");
		saveRes=new JMenuItem("Save Results");
		expPoints=new JMenuItem("Export Points");
		menuBar.add(fileMenu);
		menuBar.add(impExp);
		fileMenu.add(loadMap);
		fileMenu.add(info);
		fileMenu.add(close);
		impExp.add(saveRes);
		impExp.add(expPoints);
		toolBar.setBorder(matteB);
		toolBar.setFloatable(true);
		toolBar.setLayout((LayoutManager) new FlowLayout(FlowLayout.CENTER));
		//Initialise all the swing objects
		undoPoint=new JButton("Undo");
		resetAllPoints=new JButton("Clear Points");
		numDrawnPoints=new JLabel("Points: "+0);
		tradGAChooser=new JComboBox<String>();
			//Add data to the ComboBox 
			tradGAChooser.addItem("Traditional Algorithms");
			tradGAChooser.addItem("Genetic Algorithms");
		tradPanel=new JPanel();
			tradPanel.setBorder(new TitledBorder("Traditional"));
			tradPanel.setLayout(null);
		genPanel=new JPanel();
			genPanel.setBorder(new TitledBorder("Genetic"));
			genPanel.setLayout(null);
		optionPanel=new JPanel();
			optionPanel.setBorder(new TitledBorder("Options"));
			optionPanel.setLayout(null);
		destinationPanel=new JPanel();
			destinationPanel.setBorder(new TitledBorder("Locations"));
			destinationPanel.setLayout(null);
		executionPanel=new JPanel();
			executionPanel.setBorder(new TitledBorder("Execution"));
			executionPanel.setLayout(null);
		trad1CB=new JCheckBox("Closest Neighbour");
		trad2CB=new JCheckBox("Greedy Heuristic");
		trad3CB=new JCheckBox("Insertion Heuristic");
		trad4CB=new JCheckBox("<empty>");
		popSizeLabel=new JLabel("Population Size:");
		fromLabel=new JLabel("From:");
		toLabel=new JLabel("To:");
		maxGenLabel=new JLabel("Max Gen:");
		popSizeTF=new JTextField();
		fromTF=new JTextField();
		toTF=new JTextField();
		maxGenTF=new JTextField();
		crossoverLabel=new JLabel("Crossover Method:");
		crossoverMethods=new JComboBox<String>();
			crossoverMethods.addItem("Cycle Crossover");
			crossoverMethods.addItem("One Point Crossover");
			crossoverMethods.addItem("Ordered Crossover");
			crossoverMethods.addItem("Partially Mapped Crossover");
			crossoverMethods.addItem("Two Point Crossover");
		mutationLabel=new JLabel("Mutation Method:");
		mutationMethods=new JComboBox<String>();
			mutationMethods.addItem("Insertion Mutation");
			mutationMethods.addItem("Reciprocal Exchange Mutation");
		crossoverProbLabel=new JLabel("Probability: 0.4");
		crossProbSlider= new JSlider(JSlider.HORIZONTAL,0,100,40);
			//crossProbSlider.setMinorTickSpacing(0);
			//crossProbSlider.setMajorTickSpacing(50);
			//crossProbSlider.setPaintTicks(true);
			//crossProbSlider.setPaintLabels(true);
		mutationProbLabel=new JLabel("Probability: 0.20");
		mutProbSlider=new JSlider(JSlider.HORIZONTAL,0,100,20);
		casualPointsLabel=new JLabel("Casual");
		casualPointsTF=new JTextField();
			casualPointsTF.setEnabled(false);
		goDrawButton=new JButton("Go");
			goDrawButton.setEnabled(false);
		manualPointsLabel=new JLabel("Casual");
		numCasualPointsCB=new JCheckBox("n. of Points:");
		manualDrawCB=new JCheckBox("Draw Points");
			manualDrawCB.setSelected(true);
		importPointsCB=new JCheckBox("Import XML/JSON");
		importXmlJson=false;
		startExecutionButton=new JButton("Start");
		addToExecution=new JButton("Add");
		automaticExecution=new JButton("Automatic");
		viewResultsButton=new JButton("View Results");
		//Define position of the object on the jFrame
		undoPoint.setBounds(560,15,70,20);
		resetAllPoints.setBounds(430,15,120,20);
		numDrawnPoints.setBounds(10,15,80,20);
		tradGAChooser.setBounds(650,15,140,20);
		tradPanel.setBounds(650,40,200,250);
		genPanel.setBounds(650,335,420,295);
		optionPanel.setBounds(860,40,210,295);
		destinationPanel.setBounds(10,20,190,160);
		executionPanel.setBounds(10,180,190,105);
		trad1CB.setBounds(6,20,180,20);
		trad2CB.setBounds(6,45,180,20);
		trad3CB.setBounds(6,70,180,20);
		trad4CB.setBounds(6,95,120,20);
		popSizeLabel.setBounds(8,20,100,20);
		popSizeTF.setBounds(110,20,70,20);
		fromLabel.setBounds(8,50,40,20);
		fromTF.setBounds(50,50,55,20);
		toLabel.setBounds(115,50,20,20);
		toTF.setBounds(145,50,70,20);
		maxGenLabel.setBounds(8,80,60,20);
		maxGenTF.setBounds(75,80,80,20);
		crossoverLabel.setBounds(8,110,120,20);
		crossoverMethods.setBounds(130,110,170,20);
		crossoverProbLabel.setBounds(28,140,100,20);
		crossProbSlider.setBounds(148,140,200,20);
		mutationLabel.setBounds(8,180,120,10);
		mutationMethods.setBounds(130,180,170,20);
		mutationProbLabel.setBounds(28,210,100,20);
		mutProbSlider.setBounds(148,210,200,20);
		casualPointsLabel.setBounds(8,20,80,20);
		numCasualPointsCB.setBounds(20,40,110,20);
		casualPointsTF.setBounds(130,40,50,20);
		goDrawButton.setBounds(130,130,50,20);
		manualPointsLabel.setBounds(8,60,80,20);
		manualDrawCB.setBounds(20,80,110,20);
		importPointsCB.setBounds(20,100,150,20);
		startExecutionButton.setBounds(10,25,80,20);
		addToExecution.setBounds(100,25,80,20);
		automaticExecution.setBounds(40,50,110,20);
		viewResultsButton.setBounds(40,75,110,20);
		//Add control action for each object, thus to provide interactivity
		loadMap.addActionListener(this);
		info.addActionListener(this);
		close.addActionListener(this);
		saveRes.addActionListener(this);
		expPoints.addActionListener(this);
		resetAllPoints.addActionListener(this);
		undoPoint.addActionListener(this);
		tradGAChooser.addActionListener(this);
		trad1CB.addActionListener(this);
		trad2CB.addActionListener(this);
		trad3CB.addActionListener(this);
		trad4CB.addActionListener(this);
		crossoverMethods.addActionListener(this);
		mutationMethods.addActionListener(this);
		crossProbSlider.addChangeListener(this);
		mutProbSlider.addChangeListener(this);
		goDrawButton.addActionListener(this);
		numCasualPointsCB.addActionListener(this);
		manualDrawCB.addActionListener(this);
		importPointsCB.addActionListener(this);
		startExecutionButton.addActionListener(this);
		addToExecution.addActionListener(this);
		automaticExecution.addActionListener(this);
		viewResultsButton.addActionListener(this);
		//Add the object to the jFrame
		this.add(undoPoint);
		this.add(resetAllPoints);
		this.add(numDrawnPoints);
		this.add(tradGAChooser);
		this.add(tradPanel);
		this.add(genPanel);
		this.add(optionPanel);
		tradPanel.add(trad1CB);
		tradPanel.add(trad2CB);
		tradPanel.add(trad3CB);
		tradPanel.add(trad4CB);
		genPanel.add(popSizeLabel);
		genPanel.add(popSizeTF);
		genPanel.add(fromLabel);
		genPanel.add(fromTF);
		genPanel.add(toLabel);
		genPanel.add(toTF);
		genPanel.add(maxGenLabel);
		genPanel.add(maxGenTF);
		genPanel.add(crossoverLabel);
		genPanel.add(crossoverMethods);
		genPanel.add(crossoverProbLabel);
		genPanel.add(crossProbSlider);
		genPanel.add(mutationLabel);
		genPanel.add(mutationMethods);
		genPanel.add(mutationProbLabel);
		genPanel.add(mutProbSlider);
		optionPanel.add(destinationPanel);
		optionPanel.add(executionPanel);
		destinationPanel.add(casualPointsLabel);
		destinationPanel.add(numCasualPointsCB);
		destinationPanel.add(casualPointsTF);
		destinationPanel.add(goDrawButton);
		destinationPanel.add(manualPointsLabel);
		destinationPanel.add(manualDrawCB);
		destinationPanel.add(importPointsCB);
		executionPanel.add(startExecutionButton);
		executionPanel.add(addToExecution);
		executionPanel.add(automaticExecution);
		executionPanel.add(viewResultsButton);
		//Initialise global objects
		points=new Vector<Point>();
		algQueueExecution=new Vector<Object>();
		crossoverSelected=crossoverMethods.getItemAt(0).toString();
		mutationSelected=mutationMethods.getItemAt(0).toString();
		crossoverProb=0.40f;
		mutationProb=0.20f;
		jpgImport=new JFileChooser();
		jpgFilter=new FileNameExtensionFilter(".jpg", "jpg");
		//Add Drawing area
		drawingArea=new DrawingPanel(points,numDrawnPoints);
			drawingArea.setBounds(10, 50, 620, 590);
			drawingArea.setBackground(new Color(200,200,200));
		mainFrame.add(drawingArea);
		//Create a level difference between the main window and the drawing area
		drawingArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		drawingArea.addMouseListener(new MouseAdapter(){
			//Draw when the mouse is clicked and then released inside the area
			public void mouseReleased(MouseEvent mE){
				if(manualDrawCB.isSelected()){
					//Add the new point to the vector and update the view
					drawingArea.passPoint(new Point(mE.getX(),mE.getY()));
				}
			}
		});
		//Enable the Traditional Algorithm view
		this.tradGenView(tradGAChooser.getSelectedIndex());
	}
	
	@Override
	public void actionPerformed(ActionEvent actionE) {
		//Load a background picture used as a map for the TSP
		if(actionE.getSource().equals(loadMap)){
			//It imports the pic, but it doesn't resize it properly and position it where i want it to go
			/*BufferedImage mapJpg=null;
			try{
				jpgImport.setFileFilter(jpgFilter);
				int resSel=jpgImport.showOpenDialog(this);
				if(resSel==JFileChooser.APPROVE_OPTION){
					File imageToImport=jpgImport.getSelectedFile();
					mapJpg=ImageIO.read(imageToImport);
					BufferedImage resizedMap=new BufferedImage(300,400,BufferedImage.TYPE_INT_RGB);
					Graphics2D g2D=resizedMap.createGraphics();
					//10, 50, 620, 590
					g2D.drawImage(mapJpg,0,0,300,400,null);
					g2D.dispose();
					JLabel test=new JLabel();
					test.setIcon(new ImageIcon(mapJpg));
					test.setBounds(0,0,300,400);
					drawingArea.add(test);
				}
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(null, e.toString());
			}*/
		}
		if(actionE.getSource().equals(info)){
			
		}
		if(actionE.getSource().equals(close)){
			//this.initExitRequest();
			System.exit(0);
		}
		if(actionE.getSource().equals(saveRes)){
					
		}
		if(actionE.getSource().equals(expPoints)){
			
		}
		if(actionE.getSource().equals(resetAllPoints)){
			drawingArea.clearAllPoints();
		}
		if(actionE.getSource().equals(undoPoint)){
			//Remove last added point and update the view
			drawingArea.removeLastPoint();
		}
		if(actionE.getSource().equals(tradGAChooser)){
			this.tradGenView(tradGAChooser.getSelectedIndex());
		}
		if(actionE.getSource().equals(trad1CB)){
			trad1CB.setSelected(true);
			trad2CB.setSelected(false);
			trad3CB.setSelected(false);
			trad4CB.setSelected(false);
		}
		if(actionE.getSource().equals(trad2CB)){
			trad1CB.setSelected(false);
			trad2CB.setSelected(true);
			trad3CB.setSelected(false);
			trad4CB.setSelected(false);
		}
		if(actionE.getSource().equals(trad3CB)){
			trad1CB.setSelected(false);
			trad2CB.setSelected(false);
			trad3CB.setSelected(true);
			trad4CB.setSelected(false);
		}
		if(actionE.getSource().equals(trad4CB)){
			trad1CB.setSelected(false);
			trad2CB.setSelected(false);
			trad3CB.setSelected(false);
			trad4CB.setSelected(true);
		}
		if(actionE.getSource().equals(goDrawButton)){
			//Start drawing n random points
			if(numCasualPointsCB.isSelected()){
				try{
					int randPoints=Integer.parseInt(casualPointsTF.getText());
					if(randPoints<1501){
						//Remove all points previously added
						drawingArea.clearAllPoints();
						//Create n new points, add them to the Vector and draw them in the drawing area
						for(int i=0; i<randPoints; i++){
							//Creare a random point within the drawing area
							int xP=(int)(Math.random()*610);
							int yP=(int)(Math.random()*540);
							drawingArea.passPoint(new Point(xP, yP));
						}
					}
					else{
						casualPointsTF.setText("");
						JOptionPane.showMessageDialog(null, "Limit of 1500 points exceeded!", "Limit Exceeded", JOptionPane.WARNING_MESSAGE);
					}
				}
				catch(NumberFormatException err){
					casualPointsTF.setText("");
					JOptionPane.showMessageDialog(null,"Please enter a valid number!","Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			//Import points from a XML/JSON file, chosen by the user
			else{
				
			}
		}
		if(actionE.getSource().equals(crossoverMethods)){
			crossoverSelected=crossoverMethods.getSelectedItem().toString();
		}
		if(actionE.getSource().equals(mutationMethods)){
			mutationSelected=mutationMethods.getSelectedItem().toString();
		}
		if(actionE.getSource().equals(numCasualPointsCB)){
			numCasualPointsCB.setSelected(true);
			casualPointsTF.setEnabled(true);
			manualDrawCB.setSelected(false);
			importPointsCB.setSelected(false);
			importXmlJson=false;
			undoPoint.setEnabled(false);
			resetAllPoints.setEnabled(false);
			goDrawButton.setEnabled(true);
		}
		if(actionE.getSource().equals(manualDrawCB)){
			numCasualPointsCB.setSelected(false);
			casualPointsTF.setEnabled(false);
			manualDrawCB.setSelected(true);
			importPointsCB.setSelected(false);
			importXmlJson=false;
			undoPoint.setEnabled(true);
			resetAllPoints.setEnabled(true);
			goDrawButton.setEnabled(false);
		}
		if(actionE.getSource().equals(importPointsCB)){
			numCasualPointsCB.setSelected(false);
			casualPointsTF.setEnabled(false);
			manualDrawCB.setSelected(false);
			importPointsCB.setSelected(true);
			importXmlJson=true;
			undoPoint.setEnabled(false);
			resetAllPoints.setEnabled(false);
			goDrawButton.setEnabled(true);
			//drawingArea.removeMouseListener(this);
		}
		if(actionE.getSource().equals(startExecutionButton)){
			if(algQueueExecution.size()>0){
				//If only 1 algorithm is added to the queue, perform the execution on the mainView
				if(algQueueExecution.size()==1){
					//Start performing the algorithm
					if(startExecutionButton.getText().equals("Start")){
						startExecutionButton.setText("Stop");
					}
					//Stop execution of the algorithm
					else{
						startExecutionButton.setText("Start");
					}
				}
				//If many algorithms are ready to be executed, open a new window
				else{
					ExecutionView execView=new ExecutionView(algQueueExecution);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "No Algorithms have been added to the execution queue!","Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		if(actionE.getSource().equals(addToExecution)){			
			int numPoints=points.size();
			if(numPoints>0){
				//Verify if the selected algorithm with that number of points has already in the execution queue
				//Only Traditional algorithms are available to user selection
				if(tradPanel.isEnabled() && !genPanel.isEnabled()){
					String tradAlgName="";
					if(trad1CB.isSelected()){
						tradAlgName=trad1CB.getText();
					}
					else if (trad2CB.isSelected()){
						tradAlgName=trad2CB.getText();
					}
					else if (trad3CB.isSelected()){
						tradAlgName=trad3CB.getText();	
					}
					else if (trad4CB.isSelected()){
						tradAlgName=trad4CB.getText();
					}
					//Check that a value has been assigned to the variable
					if(!tradAlgName.equals("")){
						
						TradResultData tempTra=new TradResultData(tradAlgName,numPoints);
						//Do this if the vector is not empty
						if(algQueueExecution.size()>0){
							boolean isAlgInQueue=false;
							for(int i=0; i<algQueueExecution.size(); i++){
								try{
									TradResultData tempTr=(TradResultData)algQueueExecution.elementAt(i);
									//If this traditional algorithm with this num of points is not already contained in the execution queue, add it to the queue
									if(tempTra.equals(tempTr)){
										isAlgInQueue=true;
									}
								}
								catch(Exception err){
								}
							}
							if(!isAlgInQueue){
								algQueueExecution.add(tempTra);
								JOptionPane.showMessageDialog(null, "Element Added!"
										+ "\nAlgorithm Name: "+tempTra.getAlgName()
										+"\nNum. Cities: "+tempTra.getNumCities(),
										"Confirm", JOptionPane.INFORMATION_MESSAGE);
							}
							else{
								JOptionPane.showMessageDialog(null, "This execution is already in the queue", "Duplicate Value", JOptionPane.WARNING_MESSAGE);
							}
						}
						//Do this if vector is empty
						else{
							algQueueExecution.add(tempTra);
							JOptionPane.showMessageDialog(null, "Element Added!"
									+ "\nAlgorithm Name: "+tempTra.getAlgName()
									+"\nNum. Cities: "+tempTra.getNumCities(),
									"Confirm", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "Select an algorithm to perform", "Confirm", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				//Only Genetic algorithms are available to user selection
				else{
					int popS, fromP, toP, maxG;
					try{
						popS=Integer.parseInt(popSizeTF.getText());
						fromP=Integer.parseInt(fromTF.getText());
						toP=Integer.parseInt(toTF.getText());
						maxG=Integer.parseInt(maxGenTF.getText());
						//String crossM, String mutM, float crossP, float mutP
						GenResultData tempGen=new GenResultData(numPoints, popS,fromP, toP, maxG, crossoverSelected, mutationSelected, crossoverProb,mutationProb);
						//Do this if vector is not empty
						if(algQueueExecution.size()>0){
							boolean isAlgInQueue=false;
							for(int i=0; i<algQueueExecution.size(); i++){
								try{
									GenResultData tempG=(GenResultData)algQueueExecution.elementAt(i);
									//If this genetic algorithm is not already contained in the execution queue, add it to the queue
									if(tempG.equals(tempGen)){
										isAlgInQueue=true;
									}
								}
								catch(Exception err){
								}
							}
							if(!isAlgInQueue){
								algQueueExecution.add(tempGen);
								JOptionPane.showMessageDialog(null, "Element Added!"
										+ "\nPopulation Size: "+tempGen.getPopSize()
										+"\nNum. Cities: "+tempGen.getNumCities()
										+"\nFrom: "+tempGen.getPopFrom()
										+"\nTo: "+tempGen.getPopTo()
										+"\nMax Gen: "+tempGen.getMaxGen()
										+"\nCrossover Method: "+tempGen.getCrossoverMethod()
										+"\nCrossover Probability: "+tempGen.getCrossoverProbability()
										+"\nMutation Method: "+tempGen.getMutationMethod()
										+"\nMutation Probability: "+tempGen.getMutationProbability(),
										"Confirm", JOptionPane.INFORMATION_MESSAGE);
							}
							else{
								JOptionPane.showMessageDialog(null, "This execution is already in the queue", "Duplicate Value", JOptionPane.WARNING_MESSAGE);
							}
						}
						//Do this if vector is empty
						else{
							algQueueExecution.add(tempGen);
							JOptionPane.showMessageDialog(null, "Element Added!"
									+ "\nPopulation Size: "+tempGen.getPopSize()
									+"\nNum. Cities: "+tempGen.getNumCities()
									+"\nFrom: "+tempGen.getPopFrom()
									+"\nTo: "+tempGen.getPopTo()
									+"\nMax Gen: "+tempGen.getMaxGen()
									+"\nCrossover Method: "+tempGen.getCrossoverMethod()
									+"\nCrossover Probability: "+tempGen.getCrossoverProbability()
									+"\nMutation Method: "+tempGen.getMutationMethod()
									+"\nMutation Probability: "+tempGen.getMutationProbability(),
									"Confirm", JOptionPane.INFORMATION_MESSAGE);
						}
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, "Enter valid numbers please!","Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Add some points to the drawing area first","Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(actionE.getSource().equals(automaticExecution)){
			ExecutionView execView=new ExecutionView(algQueueExecution);
		}
		if(actionE.getSource().equals(viewResultsButton)){
			if(algQueueExecution.size()>0){
				//If only 1 algorithm is in the queue, view the results for only that performance
				if(algQueueExecution.size()==1){
					//Verify the type of object (Genetic or Traditional)
					if(algQueueExecution.elementAt(0) instanceof TradResultData){
						ResultsView resView=new ResultsView("Results - Traditional Algorithm","tra",algQueueExecution.elementAt(0));
					}
					else{
						ResultsView resView=new ResultsView("Results - Genetic Algorithm","gen",algQueueExecution.elementAt(0));
					}
				}
				//If many algorithms are in the queue, open a new window
				else{
					ExecutionView execView=new ExecutionView(algQueueExecution);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "No Results to view","Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private void tradGenView(int methodSelected) {
		//Enable only the Tradition algorithms
		if(methodSelected==0){
			tradPanel.setEnabled(true);
			genPanel.setEnabled(false);
			Component[] geneticComponents=genPanel.getComponents();
			for(Component c:geneticComponents){
				c.setEnabled(false);
			}
			Component[] tradComponents=tradPanel.getComponents();
			for(Component c:tradComponents){
				c.setEnabled(true);
			}
			trad1CB.setSelected(false);
			trad2CB.setSelected(false);
			trad3CB.setSelected(false);
			trad4CB.setSelected(false);
		}
		//Enable only the Genetic algorithms
		else{
			tradPanel.setEnabled(false);
			genPanel.setEnabled(true);
			Component[] geneticComponents=genPanel.getComponents();
			for(Component c:geneticComponents){
				c.setEnabled(true);
			}
			Component[] tradComponents=tradPanel.getComponents();
			for(Component c:tradComponents){
				c.setEnabled(false);
			}
		}		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		
	}
	private void initExitRequest(){
		exitReq=new ExitRequest();
		exitReq.setTitle("Exit");
		exitReq.setVisible(true);
		exitReq.setResizable(false);
		exitReq.setSize(280,150);
		exitReq.setLocation(500,300);
	}

	@Override
	public void stateChanged(ChangeEvent cE) {
		if(cE.getSource().equals(crossProbSlider)){
			crossoverProb=(float)crossProbSlider.getValue()/100;
			crossoverProbLabel.setText("Probability: "+crossoverProb);
		}
		if(cE.getSource().equals(mutProbSlider)){
			mutationProb=(float)mutProbSlider.getValue()/100;
			mutationProbLabel.setText("Probability: "+mutationProb);
		}
		
	}
}