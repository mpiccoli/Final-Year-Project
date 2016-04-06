import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
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
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import geneticAlgorithms.TSP_GA;
import geneticAlgorithms.TSP_GA_Worker;
import heuristicAlgorithms.ClosestNeighbour;
import heuristicAlgorithms.GreedyHeuristic;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see Files, Path, DateFormat, Date, Vector, Border, MatteBorder, TitlesBorder, FileNameExtensionFilter, TSP_GA, TSP_GA_Worker, 
 * ClosestNeighbour, GreedyHeuristic, ActionListener, PropertyChangeListener, ChangeListener, JFrame, JMenu, JProgressBar
 * 
 * This Class represents the core of this application since it deals with user interactions, genetic and traditional algorithms, export and import of data and so on
 * 
 */
public class MainView extends JPanel implements ActionListener, PropertyChangeListener, ChangeListener {

	// Define the Constants used in this application
	private static final long serialVersionUID = -6661641861407996566L;
	private static final Font BOLD_SEGOE_12 = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font ITALIC_SEGOE_12 = new Font("Segoe UI", Font.ITALIC, 12);
	private static final Font ITALIC_SEGOE_13 = new Font("Segoe UI", Font.ITALIC, 13);
	private static final Font TRUETRYPE_SEGOE_14 = new Font("Segoe UI", Font.TRUETYPE_FONT, 14);
	private static final Font PLAIN_SEGOE_12 = new Font("Segoe UI", Font.PLAIN, 12);
	private static final Font PLAIN_SEGOE_13 = new Font("Segoe UI", Font.PLAIN, 13);
	private static final Color BACKGROUND_COLOUR = new Color(78, 129, 162);
	private static final Color TEXTFIELD_BACKGROUND_COLOUR = new Color(198, 225, 235);
	private static final String CLOSEST_NEIGHBOUR_DETAILS=""
			+ "This algorithm is based on the idea of visit the closest city\n"
			+ "from the current location until all the nodes have been visited\n"
			+ "(Matai et al., 2010).";
	private static final String GREEDY_DETAILS=""
			+ "This constructs a tour choosing the edge to visit from the closest\n"
			+ "ones available, however the degree of any node cannot be > 2.\n"
			+ "(Matai et al., 2010).";

	// Graphical Elements
	private JFrame mainFrame;
	JToolBar toolBar;
	private MatteBorder matteB;
	JMenuBar menuBar;
	private JMenu fileMenu, impExp;
	private JMenuItem close, info, saveFullRes, savePartialRes, expPoints;
	private JProgressBar progressBar;
	// Traditional and Genetic Algorithm variables
	private JPanel tradPanel, genPanel, optionPanel, destinationPanel, executionPanel;
	private JCheckBox trad1CB, trad2CB, trad3CB, manualDrawCB, importPointsCB, numRandomPointsCB;
	private JLabel popSizeLabel, maxGenLabel, crossoverLabel, mutationLabel, crossoverProbLabel, mutationProbLabel,
	randomPointsLabel, manualPointsLabel, currentRunningTimeLabel, currentRunningAlgLabel, numDrawnPoints;
	private JTextField popSizeTF, maxGenTF, randomPointsTF, currentRunningTimeTF, currentRunningAlgTF;
	private JComboBox<String> crossoverMethods, mutationMethods, tradGAChooser;
	private JSlider crossProbSlider, mutProbSlider;
	private JButton goDrawButton, startExecutionButton, addToExecution, viewResultsButton, resetAllFieldsButton,
	undoPoint, resetAllPoints;
	private boolean execStopped;
	// Drawing area which allows the user to draw points and perform the TSP on
	// those points
	private DrawingPanel drawingArea;
	// Global Objects
	private ExitRequest exitReq;
	private Vector<Point> points, results;
	private Vector<Object> algQueueExecution;
	private Vector<Vector<Point>> resultsDataTSP;
	private Vector<Double> pathDistancesTSP;
	private String crossoverSelected, mutationSelected, listenToChanges, currentRunningAlg;
	private int crossoverProb, mutationProb, index;
	private ClosestNeighbour closestNeighbourAlg;
	private GreedyHeuristic greedyHeuristicAlg;
	private TSP_GA tspAlg;
	private TSP_GA_Worker tspWorker;
	private GenResultData currentGeneticAlg;
	private long currentRunningTimeExec;
	// Variables used to export/import data
	private JFileChooser csvImportExport, txtExport;
	private FileNameExtensionFilter csvFilter, txtFilter;

	/*
	 * @param frame Reference to the frame object created in the Controller
	 * Class
	 * 
	 * Constructor with 1 parameter
	 * 
	 */
	public MainView(JFrame frame) {
		this.mainFrame = frame;
		// Add a window listener so the user can decide what operations execute
		// when certain window action are about to occur
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent wE) {
				// Ask the user confirm to exit the application
				initExitRequest();
			}
		});
		// Set the layout to null so the programmer can change it
		this.setLayout(null);
		// Create the MenuBar and its elements
		toolBar = new JToolBar();
		toolBar.setSize(800, 40);
		matteB = new MatteBorder(1, 0, 0, 0, Color.BLACK);
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		impExp = new JMenu("Import/Export");
		close = new JMenuItem("Close");
		info = new JMenuItem("Info");
		saveFullRes = new JMenuItem("Save Extended Results");
		savePartialRes = new JMenuItem("Save Summative Results");
		expPoints = new JMenuItem("Export Points");
		menuBar.add(fileMenu);
		menuBar.add(impExp);
		fileMenu.add(info);
		fileMenu.add(close);
		impExp.add(saveFullRes);
		impExp.add(savePartialRes);
		impExp.add(expPoints);
		toolBar.setBorder(matteB);
		toolBar.setFloatable(true);
		toolBar.setLayout((LayoutManager) new FlowLayout(FlowLayout.CENTER));
		// Initialize all the swing objects
		undoPoint = new JButton("Undo");
		resetAllPoints = new JButton("Clear Points");
		numDrawnPoints = new JLabel("Points: " + 0);
		tradGAChooser = new JComboBox<String>();
		// Add data to the ComboBox
		tradGAChooser.addItem("Traditional Algorithms");
		tradGAChooser.addItem("Genetic Algorithms");
		// Initialize all the JPanel the window contains
		tradPanel = new JPanel();
		tradPanel.setBorder(new TitledBorder("Traditional"));
		tradPanel.setLayout(null);
		genPanel = new JPanel();
		genPanel.setBorder(new TitledBorder("Genetic"));
		genPanel.setLayout(null);
		optionPanel = new JPanel();
		optionPanel.setBorder(new TitledBorder("Options"));
		optionPanel.setLayout(null);
		destinationPanel = new JPanel();
		destinationPanel.setBorder(new TitledBorder("Locations"));
		destinationPanel.setLayout(null);
		executionPanel = new JPanel();
		executionPanel.setBorder(new TitledBorder("Execution"));
		executionPanel.setLayout(null);
		trad1CB = new JCheckBox("Closest Neighbour");
		trad2CB = new JCheckBox("Greedy Heuristic");
		trad3CB = new JCheckBox("Insertion Heuristic");
		popSizeLabel = new JLabel("Population Size");
		popSizeTF = new JTextField();
		popSizeTF.setEditable(false);
		maxGenLabel = new JLabel("Max Gen:");
		maxGenTF = new JTextField();
		crossoverLabel = new JLabel("Crossover Method:");
		crossoverMethods = new JComboBox<String>();
		crossoverMethods.addItem("Cycle Crossover");
		crossoverMethods.addItem("Ordered Crossover");
		crossoverMethods.addItem("Modified Sequential Constructive Crossover");
		// crossoverMethods.addItem("Averaging Crossover");
		// crossoverMethods.addItem("Greedy Crossover");
		// crossoverMethods.addItem("Partially Mapped Crossover");
		mutationLabel = new JLabel("Mutation Method:");
		mutationMethods = new JComboBox<String>();
		mutationMethods.addItem("Ranged Swapping Mutation");
		mutationMethods.addItem("Swapping Mutation");
		// mutationMethods.addItem("Two Way Mutation");
		// mutationMethods.addItem("Insertion Mutation");
		// mutationMethods.addItem("Simple Inversion");
		// mutationMethods.addItem("Scramble Mutation");
		crossoverProbLabel = new JLabel("Probability: 40%");
		crossProbSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 40);
		mutationProbLabel = new JLabel("Probability: 20%");
		mutProbSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 20);
		randomPointsLabel = new JLabel("Random");
		randomPointsTF = new JTextField();
		randomPointsTF.setEnabled(false);
		goDrawButton = new JButton("Go");
		goDrawButton.setEnabled(false);
		manualPointsLabel = new JLabel("Manual");
		numRandomPointsCB = new JCheckBox("n. of Points:");
		manualDrawCB = new JCheckBox("Draw Points");
		manualDrawCB.setSelected(true);
		importPointsCB = new JCheckBox("Import CSV");
		startExecutionButton = new JButton("Start");
		addToExecution = new JButton("Add");
		viewResultsButton = new JButton("Edit Queue/View Results");
		resetAllFieldsButton = new JButton("Reset All");
		currentRunningTimeTF = new JTextField();
		currentRunningTimeTF.setEditable(false);
		currentRunningTimeLabel = new JLabel("Total Time");
		currentRunningAlgTF = new JTextField();
		currentRunningAlgTF.setEditable(false);
		currentRunningAlgLabel = new JLabel("Details");

		// Add a background colour to the following elements
		this.setBackground(BACKGROUND_COLOUR);
		toolBar.setBackground(BACKGROUND_COLOUR);
		tradPanel.setBackground(BACKGROUND_COLOUR);
		genPanel.setBackground(BACKGROUND_COLOUR);
		optionPanel.setBackground(BACKGROUND_COLOUR);
		destinationPanel.setBackground(BACKGROUND_COLOUR);
		executionPanel.setBackground(BACKGROUND_COLOUR);
		popSizeTF.setBackground(TEXTFIELD_BACKGROUND_COLOUR);
		maxGenTF.setBackground(TEXTFIELD_BACKGROUND_COLOUR);
		randomPointsTF.setBackground(TEXTFIELD_BACKGROUND_COLOUR);
		currentRunningAlgTF.setBackground(TEXTFIELD_BACKGROUND_COLOUR);
		currentRunningTimeTF.setBackground(TEXTFIELD_BACKGROUND_COLOUR);

		// Change the font of the following objects
		undoPoint.setFont(BOLD_SEGOE_12);
		resetAllPoints.setFont(BOLD_SEGOE_12);
		numDrawnPoints.setFont(BOLD_SEGOE_12);
		tradGAChooser.setFont(ITALIC_SEGOE_12);
		trad1CB.setFont(TRUETRYPE_SEGOE_14);
		trad2CB.setFont(TRUETRYPE_SEGOE_14);
		trad3CB.setFont(TRUETRYPE_SEGOE_14);
		popSizeLabel.setFont(BOLD_SEGOE_12);
		popSizeTF.setFont(ITALIC_SEGOE_12);
		maxGenLabel.setFont(BOLD_SEGOE_12);
		maxGenTF.setFont(ITALIC_SEGOE_12);
		crossoverLabel.setFont(BOLD_SEGOE_12);
		crossoverMethods.setFont(ITALIC_SEGOE_12);
		mutationLabel.setFont(BOLD_SEGOE_12);
		mutationMethods.setFont(ITALIC_SEGOE_12);
		crossoverProbLabel.setFont(PLAIN_SEGOE_12);
		mutationProbLabel.setFont(PLAIN_SEGOE_12);
		randomPointsLabel.setFont(BOLD_SEGOE_12);
		randomPointsTF.setFont(ITALIC_SEGOE_12);
		goDrawButton.setFont(BOLD_SEGOE_12);
		manualPointsLabel.setFont(BOLD_SEGOE_12);
		numRandomPointsCB.setFont(PLAIN_SEGOE_13);
		manualDrawCB.setFont(PLAIN_SEGOE_13);
		importPointsCB.setFont(PLAIN_SEGOE_13);
		startExecutionButton.setFont(BOLD_SEGOE_12);
		addToExecution.setFont(BOLD_SEGOE_12);
		viewResultsButton.setFont(BOLD_SEGOE_12);
		resetAllFieldsButton.setFont(BOLD_SEGOE_12);
		currentRunningTimeTF.setFont(ITALIC_SEGOE_13);
		currentRunningTimeLabel.setFont(BOLD_SEGOE_12);
		currentRunningAlgTF.setFont(ITALIC_SEGOE_13);
		currentRunningAlgLabel.setFont(BOLD_SEGOE_12);

		// Define the positions of the object on0 the jFrame
		undoPoint.setBounds(560, 15, 70, 20);
		resetAllPoints.setBounds(430, 15, 120, 20);
		numDrawnPoints.setBounds(10, 15, 80, 20);
		tradGAChooser.setBounds(650, 15, 200, 20);
		tradPanel.setBounds(650, 40, 200, 160);
		genPanel.setBounds(650, 200, 400, 235);
		optionPanel.setBounds(190, 10, 200, 115);
		destinationPanel.setBounds(860, 40, 190, 160);
		executionPanel.setBounds(650, 440, 400, 195);
		trad1CB.setBounds(6, 40, 180, 20);
		trad2CB.setBounds(6, 65, 180, 20);
		trad3CB.setBounds(6, 90, 180, 20);
		popSizeLabel.setBounds(8, 30, 100, 20);
		popSizeTF.setBounds(110, 30, 60, 20);
		maxGenLabel.setBounds(8, 60, 60, 20);
		maxGenTF.setBounds(75, 60, 100, 20);
		crossoverLabel.setBounds(8, 90, 120, 20);
		crossoverMethods.setBounds(130, 90, 170, 20);
		crossoverProbLabel.setBounds(28, 120, 110, 20);
		crossProbSlider.setBounds(148, 120, 200, 20);
		mutationLabel.setBounds(8, 160, 120, 10);
		mutationMethods.setBounds(130, 160, 170, 20);
		mutationProbLabel.setBounds(28, 190, 110, 20);
		mutProbSlider.setBounds(148, 190, 200, 20);
		randomPointsLabel.setBounds(8, 20, 80, 20);
		numRandomPointsCB.setBounds(20, 40, 110, 20);
		randomPointsTF.setBounds(130, 40, 50, 20);
		goDrawButton.setBounds(130, 130, 50, 20);
		manualPointsLabel.setBounds(8, 60, 80, 20);
		manualDrawCB.setBounds(20, 80, 110, 20);
		importPointsCB.setBounds(20, 100, 150, 20);
		startExecutionButton.setBounds(15, 25, 80, 20);
		addToExecution.setBounds(105, 25, 80, 20);
		viewResultsButton.setBounds(15, 55, 170, 20);
		resetAllFieldsButton.setBounds(45, 82, 110, 20);
		currentRunningAlgLabel.setBounds(10, 25, 100, 20);
		currentRunningAlgTF.setBounds(10, 45, 180, 20);
		currentRunningTimeLabel.setBounds(10, 65, 100, 20);
		currentRunningTimeTF.setBounds(10, 85, 180, 20);

		// Add control action for each object, thus to provide interactivity
		info.addActionListener(this);
		close.addActionListener(this);
		saveFullRes.addActionListener(this);
		savePartialRes.addActionListener(this);
		expPoints.addActionListener(this);
		resetAllPoints.addActionListener(this);
		undoPoint.addActionListener(this);
		tradGAChooser.addActionListener(this);
		trad1CB.addActionListener(this);
		trad2CB.addActionListener(this);
		trad3CB.addActionListener(this);
		crossoverMethods.addActionListener(this);
		mutationMethods.addActionListener(this);
		crossProbSlider.addChangeListener(this);
		mutProbSlider.addChangeListener(this);
		goDrawButton.addActionListener(this);
		numRandomPointsCB.addActionListener(this);
		manualDrawCB.addActionListener(this);
		importPointsCB.addActionListener(this);
		startExecutionButton.addActionListener(this);
		addToExecution.addActionListener(this);
		viewResultsButton.addActionListener(this);
		resetAllFieldsButton.addActionListener(this);
		// Add the object to the jFrame
		this.add(undoPoint);
		this.add(resetAllPoints);
		this.add(numDrawnPoints);
		this.add(tradGAChooser);
		this.add(tradPanel);
		this.add(genPanel);
		this.add(destinationPanel);
		this.add(executionPanel);
		tradPanel.add(trad1CB);
		tradPanel.add(trad2CB);
		// tradPanel.add(trad3CB);
		genPanel.add(popSizeLabel);
		genPanel.add(popSizeTF);
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
		destinationPanel.add(randomPointsLabel);
		destinationPanel.add(numRandomPointsCB);
		destinationPanel.add(randomPointsTF);
		destinationPanel.add(goDrawButton);
		destinationPanel.add(manualPointsLabel);
		destinationPanel.add(manualDrawCB);
		destinationPanel.add(importPointsCB);
		executionPanel.add(optionPanel);
		executionPanel.add(currentRunningAlgLabel);
		executionPanel.add(currentRunningAlgTF);
		executionPanel.add(currentRunningTimeLabel);
		executionPanel.add(currentRunningTimeTF);
		optionPanel.add(startExecutionButton);
		optionPanel.add(addToExecution);
		optionPanel.add(viewResultsButton);
		optionPanel.add(resetAllFieldsButton);

		// Initialize global objects
		points = new Vector<Point>();
		results = new Vector<Point>();
		algQueueExecution = new Vector<Object>();
		resultsDataTSP = new Vector<Vector<Point>>();
		pathDistancesTSP = new Vector<Double>();
		currentGeneticAlg = null;
		index = 0;
		execStopped = false;
		currentRunningAlg = "";
		currentRunningTimeExec = 0;
		crossoverSelected = crossoverMethods.getItemAt(0).toString();
		mutationSelected = mutationMethods.getItemAt(0).toString();
		crossoverProb = 40;
		mutationProb = 20;
		listenToChanges = "";
		csvImportExport = new JFileChooser();
		csvFilter = new FileNameExtensionFilter(".csv", "CSV");
		txtExport = new JFileChooser();
		txtFilter = new FileNameExtensionFilter(".txt ", "Plain Text");

		// Progress bar
		Border border = BorderFactory.createTitledBorder("Loading...");
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBorder(border);
		progressBar.setBounds(15, 140, 350, 35);
		progressBar.setVisible(false);
		progressBar.setOpaque(false);
		progressBar.setBackground(new Color(73, 157, 98));
		executionPanel.add(progressBar);

		// Add Drawing area
		drawingArea = new DrawingPanel(points, numDrawnPoints, results);
		drawingArea.setBounds(10, 50, 620, 590);
		drawingArea.setBackground(new Color(125, 158, 183));
		// Create a border between the drawing area and the main frame
		drawingArea.setBorder(BorderFactory.createEtchedBorder());
		drawingArea.addMouseListener(new MouseAdapter() {
			// Draw when the mouse is clicked and then released inside the area
			public void mouseReleased(MouseEvent mE) {
				if (manualDrawCB.isSelected()) {
					// Add the new point to the vector and update the view
					drawingArea.passPoint(new Point(mE.getX(), mE.getY()));
				}
			}
		});
		mainFrame.add(drawingArea);
		// Enable the Traditional Algorithm view at the start of the application
		this.tradGenView(tradGAChooser.getSelectedIndex());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent actionE) {
		// Load the application information to explain the user what this
		// application does and how to use it
		if (actionE.getSource().equals(info)) {
			String info = "Author: Michael Piccoli\nDate Created: Nov 2015\nVersion n. 1"
					+ "\n\nWhat does this application do?:\n"
					+ "   This software compares heuristic and genetic algorithms through a user friendly interface to solve\n"
					+ "   the Traveling Salesman Problem.\n\n" + "How can I use it?:\n"
					+ "   To application allows to perform single or multiple algorithms with different cities allocated to each method.\n"
					+ "   Follow theese steps:\n"
					+ "     1) Add some cities by drawing, importing or let the machine create some coordinates for you\n"
					+ "     2) Select which algorithm you may want to add the queue of execution\n"
					+ "     3) Press the \"Add Button\"\n"
					+ "   Follow the same procedure to add more execution to the queue\n\n"
					+ "How does a genetic algorithm work?:\n"
					+ "   To perform a genetic algorithm, add some cities and then specify:\n"
					+ "     1) The population size the algorithm can pick to create a best possible path\n"
					+ "     2) A maximum number of generations (number of interations) the algorithm will perform to find an optimal path\n"
					+ "     3) A Crossover and mutation algorithm\n"
					+ "     4) The probability of using the selected crossover and mutation method\n\n"
					+ "The application can perform the algorithms stored in the queue multiple times, however,\n"
					+ "the Genetic Algorithms can sometimes stop working due to a library issue they are build on\n"
					+ "that does not respond corrently on certain operations.\n"
					+ "In this case, results data will not be available (since it has not been processed),\n"
					+ "a message will be shown to the user and the application will continue performing the other algorithms\n"
					+ "in the queue temporarely bypassing the error.";
			// Display the string created to the user
			JOptionPane.showMessageDialog(null, info, "Application Information", JOptionPane.PLAIN_MESSAGE);
		}
		// Action to perform when the user clicks on the red cross to close the
		// application
		if (actionE.getSource().equals(close)) {
			this.initExitRequest();
		}
		// Action to perform when the user wants to export a partial execution summary
		if(actionE.getSource().equals(savePartialRes)){
			// Verify that the vector containing the algorithm to execute,
			// actually contains some data
			if (algQueueExecution.size() > 0) {
				try {
					// Set the filter to the JFileChooser
					txtExport.setFileFilter(txtFilter);
					// Store whether the user has confirmed or rejected the
					// export request
					int resultSelection = txtExport.showSaveDialog(this);
					// In case the user confirmed the action
					if (resultSelection == JFileChooser.APPROVE_OPTION) {
						// Create writable object that will write data to a file
						FileWriter writer = new FileWriter(txtExport.getSelectedFile() + "_(Partial).txt");
						// Write current date and time on file
						DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date date1 = new Date();
						writer.append("Task Started on: " + dateFormat1.format(date1) + "\n\n");
						// Make a string containing all the data to export and
						// add to the writer
						// Repeat this action for each element of the queue of
						// execution
						for (int i = 0; i < algQueueExecution.size(); i++) {
							Object tempElement = algQueueExecution.elementAt(i);
							String data = "";
							// Verify what element this is and call the
							// opportune method to retrieve the algorithm
							// summary
							if (tempElement instanceof TradResultData) {
								TradResultData tempTrad = (TradResultData) tempElement;
								data += tempTrad.wrapDataForFileWriter();
							} else if (tempElement instanceof GenResultData) {
								GenResultData tempGen = (GenResultData) tempElement;
								data += tempGen.wrapDataForSummativeFileWriter();
							}
							writer.append(data);
							writer.append("------------------------------------------------------\n");
						}
						// Write current date and time on file
						DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date date2 = new Date();
						writer.append("Task Completed on: " + dateFormat2.format(date2));
						// Close the file and release its instance
						writer.close();
						// Show confirmation to the user
						JOptionPane.showMessageDialog(null, "Result Data exported correctly!", "Information",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Irreversible Error!!!, please try again later", "Export Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Sorry, There is no Data to export!", "No Data",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		// Action to perform when the user wants to export the full execution data
		if (actionE.getSource().equals(saveFullRes)) {
			// Verify that the vector containing the algorithm to execute,
			// actually contains some data
			if (algQueueExecution.size() > 0) {
				try {
					// Set the filter to the JFileChooser
					txtExport.setFileFilter(txtFilter);
					// Store whether the user has confirmed or rejected the
					// export request
					int resultSelection = txtExport.showSaveDialog(this);
					// In case the user confirmed the action
					if (resultSelection == JFileChooser.APPROVE_OPTION) {
						// Create writable object that will write data to a file
						FileWriter writer = new FileWriter(txtExport.getSelectedFile() + "_(Complete).txt");
						// Write current date and time on file
						DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date date1 = new Date();
						writer.append("Task Started on: " + dateFormat1.format(date1) + "\n\n");
						// Make a string containing all the data to export and
						// add to the writer
						// Repeat this action for each element of the queue of
						// execution
						for (int i = 0; i < algQueueExecution.size(); i++) {
							Object tempElement = algQueueExecution.elementAt(i);
							String data = "";
							// Verify what element this is and call the
							// opportune method to retrieve the algorithm
							// summary
							if (tempElement instanceof TradResultData) {
								TradResultData tempTrad = (TradResultData) tempElement;
								data += tempTrad.wrapDataForFileWriter();
							} else if (tempElement instanceof GenResultData) {
								GenResultData tempGen = (GenResultData) tempElement;
								data += tempGen.wrapDataForFileWriter();
							}
							writer.append(data);
							writer.append("------------------------------------------------------\n");
						}
						// Write current date and time on file
						DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date date2 = new Date();
						writer.append("Task Completed on: " + dateFormat2.format(date2));
						// Close the file and release its instance
						writer.close();
						// Show confirmation to the user
						JOptionPane.showMessageDialog(null, "Result Data exported correctly!", "Information",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Irreversible Error!!!, please try again later", "Export Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Sorry, There is no Data to export!", "No Data",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		// Action to perform when the user wants to export the coordinates of
		// the cities shown on screen
		if (actionE.getSource().equals(expPoints)) {
			if (points.size() > 0) {
				try {
					// Set the filter to the JFileChooser
					csvImportExport.setFileFilter(csvFilter);
					// Store whether the user has confirmed or closed the export
					// file view
					int resultSelection = csvImportExport.showSaveDialog(this);
					// Dot this in case the action has been confirmed
					if (resultSelection == JFileChooser.APPROVE_OPTION) {
						// Create writable object which will write data to a
						// file
						PrintWriter writer = new PrintWriter(csvImportExport.getSelectedFile() + ".csv");
						// Write current date and time
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date date = new Date();
						writer.println("Data exported on: " + dateFormat.format(date));
						// Create a table type data format
						writer.println("City,X Value,Y Value");
						// Write coordinates
						for (int i = 0; i < points.size(); i++) {
							writer.println(
									(i + 1) + "," + points.elementAt(i).getX() + "," + points.elementAt(i).getY());
						}
						// Close the file and release its instance
						writer.close();
						// Show confirmation to the user
						JOptionPane.showMessageDialog(null, "Cities exported correctly!", "Information",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Irreversible Error!!!, please try again later", "Export Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Sorry, There is no coordinates to export!", "No Data",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		// Action to perform when the user wants to reset the current execution
		// and start with a new one
		if (actionE.getSource().equals(resetAllPoints)) {
			// Wipe all the data inside the vectors and refresh the view
			drawingArea.clearAllPoints();
		}
		// Action to perform when the user wants to remove the last point
		// inserted
		if (actionE.getSource().equals(undoPoint)) {
			// Remove last added point and update the view
			drawingArea.removeLastPoint();
		}
		// Action to perform when the user chooses to perform the traditional
		// algorithms
		if (actionE.getSource().equals(tradGAChooser)) {
			this.tradGenView(tradGAChooser.getSelectedIndex());
		}
		// Action to perform when one traditional algorithm has been selected
		// (deselect all the other choices)
		if (actionE.getSource().equals(trad1CB)) {
			trad1CB.setSelected(true);
			trad2CB.setSelected(false);
			trad3CB.setSelected(false);
		}
		// Action to perform when one traditional algorithm has been selected
		// (deselect all the other choices)
		if (actionE.getSource().equals(trad2CB)) {
			trad1CB.setSelected(false);
			trad2CB.setSelected(true);
			trad3CB.setSelected(false);
		}
		// Action to perform when one traditional algorithm has been selected
		// (deselect all the other choices)
		if (actionE.getSource().equals(trad3CB)) {
			trad1CB.setSelected(false);
			trad2CB.setSelected(false);
			trad3CB.setSelected(true);
		}
		// Action to perform when the user clicks the "GO" button which either
		// draws a defined number of points on the application
		// or pops up a import window asking the user to pick the file
		// containing the city coordinates
		if (actionE.getSource().equals(goDrawButton)) {
			// Start drawing n random points
			if (numRandomPointsCB.isSelected()) {
				try {
					// Try to convert the given input by the user to an integer
					// value
					int randPoints = Integer.parseInt(randomPointsTF.getText());
					// Only 1500 max are allowed to be displayed
					if (randPoints < 1501) {
						// Remove all points previously added
						drawingArea.clearAllPoints();
						// Create n new points, add them to the Vector and draw
						// them in the drawing area
						for (int i = 0; i < randPoints; i++) {
							// Create a random point within the drawing area
							int xP = (int) (Math.random() * 610);
							int yP = (int) (Math.random() * 540);
							drawingArea.passPoint(new Point(xP, yP));
						}
						points = drawingArea.getAllPoints();
						// Change the value of the population size for the
						// genetic algorithms
						this.updatePopulationSize(points.size());
					} else {
						randomPointsTF.setText("");
						JOptionPane.showMessageDialog(null, "Limit of 1500 points exceeded!", "Limit Exceeded",
								JOptionPane.WARNING_MESSAGE);
					}
				}
				// Error while converting the value to integer
				catch (NumberFormatException err) {
					randomPointsTF.setText("");
					JOptionPane.showMessageDialog(null, "Please enter a valid number!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			// Import points from a CSV file, chosen by the user
			else {
				try {
					String lineFile = "";
					// Set the filter to the JFileChooser
					csvImportExport.setFileFilter(csvFilter);
					int resultSelection = csvImportExport.showOpenDialog(this);
					if (resultSelection == JFileChooser.APPROVE_OPTION) {
						// Create a file container that stores the file itself
						// and its location
						File csvFileImported = csvImportExport.getSelectedFile();
						Path csvFilePathImported = Paths.get(csvFileImported.getPath());
						// Clean the cities coordinates
						drawingArea.clearAllPoints();
						try (InputStream inFile = Files.newInputStream(csvFilePathImported);
								BufferedReader buffReader = new BufferedReader(new InputStreamReader(inFile))) {
							// Read each line contained in the file
							// Skip the first 2 lines of the file
							int skipIndex0 = 0;
							while ((lineFile = buffReader.readLine()) != null) {
								if (skipIndex0 < 2) {
									skipIndex0++;
								} else {
									String[] lineData = lineFile.split(",");
									// The X and Y value are in the 1st and 2nd
									// position of each line
									String xValueString = lineData[1];
									String yValueString = lineData[2];
									// Convert the string values into float
									float xValueFloat = Float.parseFloat(xValueString);
									float yValueFloat = Float.parseFloat(yValueString);
									// Extract the integer values from the float
									// variables
									int xValue = Math.round(xValueFloat);
									int yValue = Math.round(yValueFloat);
									// Add the point to the window
									drawingArea.passPoint(new Point(xValue, yValue));
								}
							}
						}
						// Copy the reference of the Vector containing the city
						// coordinates
						points = drawingArea.getAllPoints();
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"There has been an error while importing the city coordinates, Try again later!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		// Action to perform when a crossover algorithm is selected
		if (actionE.getSource().equals(crossoverMethods)) {
			// Store the name of the crossover selected
			crossoverSelected = crossoverMethods.getSelectedItem().toString();
		}
		// Action to perform when a mutation algorithm is selected
		if (actionE.getSource().equals(mutationMethods)) {
			// Store the name of the mutation operator selected
			mutationSelected = mutationMethods.getSelectedItem().toString();
		}
		// Action to perform when the option to draw a randomized number of
		// points is selected (deselect the other options)
		if (actionE.getSource().equals(numRandomPointsCB)) {
			numRandomPointsCB.setSelected(true);
			randomPointsTF.setEnabled(true);
			manualDrawCB.setSelected(false);
			importPointsCB.setSelected(false);
			undoPoint.setEnabled(false);
			resetAllPoints.setEnabled(false);
			goDrawButton.setEnabled(true);
		}
		// Action to perform when the option to manually draw points is selected
		// (deselect the other options)
		if (actionE.getSource().equals(manualDrawCB)) {
			numRandomPointsCB.setSelected(false);
			randomPointsTF.setEnabled(false);
			manualDrawCB.setSelected(true);
			importPointsCB.setSelected(false);
			undoPoint.setEnabled(true);
			resetAllPoints.setEnabled(true);
			goDrawButton.setEnabled(false);
		}
		// Action to perform when the option to import the city coordinates is
		// selected (deselect the other options)
		if (actionE.getSource().equals(importPointsCB)) {
			numRandomPointsCB.setSelected(false);
			randomPointsTF.setEnabled(false);
			manualDrawCB.setSelected(false);
			importPointsCB.setSelected(true);
			undoPoint.setEnabled(false);
			resetAllPoints.setEnabled(false);
			goDrawButton.setEnabled(true);
		}
		// Action to perform when the button "Start" is pressed
		if (actionE.getSource().equals(startExecutionButton)) {
			// Verify that the button contains the text "Start" otherwise it
			// means it has already been pressed and the algorithms
			// are already performing their operations
			if (startExecutionButton.getText().equals("Start")) {
				if (algQueueExecution.size() > 0) {
					execStopped = false;
					// Stop the user from adding more data to execute once the
					// application has began operating the algorithms
					addToExecution.setEnabled(false);
					// Change view, blocking the user to add more data to the
					// execution queue
					this.tradGenView(2);
					// Set the index of the algorithm queue to 0 so the
					// execution starts from the first algorithm in the queue
					index = 0;
					// Visualize the progressBar and start the timer
					progressBar.setValue(0);
					progressBar.setVisible(true);
					currentRunningTimeExec = System.currentTimeMillis();
					this.beginQueueExecution();
				} else {
					JOptionPane.showMessageDialog(null, "No Algorithms have been added to the execution queue!",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
			// If the program goes into this else statement it means that the
			// application is performing some tasks and the user is willing to
			// stop it
			else {
				execStopped = true;
				startExecutionButton.setText("Start");
				startExecutionButton.setEnabled(true);
				// Only the traditional algorithm are stoppable, therefore
				// before trying to stop an execution, verify the type of
				// algorithm that is running
				if (listenToChanges.equals("cn"))
					closestNeighbourAlg.cancel(true);
				else if (listenToChanges.equals("gh")) {
					greedyHeuristicAlg.cancel(true);
				}
				// Remove the progressBar and refresh the GUI
				progressBar.setVisible(false);
				drawingArea.repaint();
				this.repaint();
			}
		}
		// Action to perform when an algorithm is to be added to the queue of
		// execution
		if (actionE.getSource().equals(addToExecution)) {
			int numPoints = points.size();
			// Verify that the points drawn are more than 2
			if (numPoints > 2) {
				// Verify if the selected algorithm with that number of points
				// has already in the execution queue
				// Only Traditional algorithms are available to user selection
				if (tradPanel.isEnabled() && !genPanel.isEnabled()) {
					String tradAlgName = "";
					if (trad1CB.isSelected()) {
						tradAlgName = trad1CB.getText();
						// Unselect the element
						trad1CB.setSelected(false);
					} else if (trad2CB.isSelected()) {
						tradAlgName = trad2CB.getText();
						// Unselect the element
						trad2CB.setSelected(false);
					} else if (trad3CB.isSelected()) {
						tradAlgName = trad3CB.getText();
						// Unselect the element
						trad3CB.setSelected(false);
					}
					// Check that a value has been assigned to the variable
					if (!tradAlgName.equals("")) {
						@SuppressWarnings("unchecked")
						TradResultData tempTra = new TradResultData(tradAlgName, numPoints,
								(Vector<Point>) points.clone());
						//Add the algorithm description to the object
						if(tradAlgName.equals("Closest Neighbour")){
							tempTra.setDetails(CLOSEST_NEIGHBOUR_DETAILS);
						}
						else{
							tempTra.setDetails(GREEDY_DETAILS);
						}
						// Do this if the vector is not empty
						if (algQueueExecution.size() > 0) {
							boolean isAlgInQueue = false;
							for (int i = 0; i < algQueueExecution.size(); i++) {
								try {
									TradResultData tempTr = (TradResultData) algQueueExecution.elementAt(i);
									// Verify the algorithm to add is not
									// already in the queue
									if (tempTr.equals(tempTra)) {
										isAlgInQueue = true;
										break;
									}
								} catch (Exception err) {
								}
							}
							// If the algorithm is not in the queue, add it,
							// display an error message otherwise
							if (!isAlgInQueue) {
								algQueueExecution.add(tempTra);
								JOptionPane.showMessageDialog(null,
										"Element Added!" + "\nAlgorithm Name: " + tempTra.getAlgName()
										+ "\nNum. Cities: " + tempTra.getNumCities(),
										"Confirm", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "This execution is already in the queue",
										"Duplicate Value", JOptionPane.WARNING_MESSAGE);
							}
						}
						// Do this if vector is empty
						else {
							algQueueExecution.add(tempTra);
							JOptionPane.showMessageDialog(
									null, "Element Added!" + "\nAlgorithm Name: " + tempTra.getAlgName()
									+ "\nNum. Cities: " + tempTra.getNumCities(),
									"Confirm", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Select an algorithm to perform", "Confirm",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				// Only Genetic algorithms are available to user selection
				else {
					try {
						// Try to convert the value passed from max gen to an
						// integer number
						int maxG = Integer.parseInt(maxGenTF.getText());
						Vector<Vector<Point>> tempVecVec = new Vector<Vector<Point>>();
						Vector<Double> tempDistances = new Vector<Double>();
						@SuppressWarnings("unchecked")
						Vector<Point> tempCities = (Vector<Point>) points.clone();
						// Define the population size by using this mathematical
						// formula
						int populationSize = (1 * (int) ((Math.log(1 - Math.pow(0.99, (1.0 / tempCities.size()))))
								/ (Math.log(((float) (tempCities.size() - 3) / (float) (tempCities.size() - 1))))));
						TSP_GA tsp = new TSP_GA(tempCities, null, tempVecVec, tempDistances, crossoverSelected,
								crossoverProb, mutationSelected, mutationProb);
						TSP_GA_Worker worker = new TSP_GA_Worker(tempCities, tempVecVec, tempDistances, maxG,
								crossoverSelected, crossoverProb, mutationSelected, mutationProb);
						GenResultData tempGen = new GenResultData(points.size(), populationSize, maxG,
								crossoverSelected, crossoverProb, mutationSelected, mutationProb, tsp, worker);
						tempGen.setCities(tempCities);
						tempGen.setResultsData(tempVecVec, true);
						tempGen.setPathDistances(tempDistances, true);
						// Do this if vector is not empty
						if (algQueueExecution.size() > 0) {
							boolean isAlgInQueue = false;
							for (int i = 0; i < algQueueExecution.size(); i++) {
								try {
									GenResultData tempG = (GenResultData) algQueueExecution.elementAt(i);
									// Verify the algorithm to add is not
									// already in the queue
									if (tempG.equals(tempGen)) {
										isAlgInQueue = true;
										break;
									}
								} catch (Exception err) {
								}
							}
							// Add the algorithm to the queue if not already in
							// it
							if (!isAlgInQueue) {
								algQueueExecution.add(tempGen);
								JOptionPane.showMessageDialog(null,
										"Element Added!" + "\nPopulation Size: " + tempGen.getPopSize() + "\nMax Gen: "
												+ tempGen.getMaxGen() + "\nCrossover Method: "
												+ tempGen.getCrossoverMethod() + "\nCrossover Probability: "
												+ tempGen.getCrossoverProbability() + "\nMutation Method: "
												+ tempGen.getMutationMethod() + "\nMutation Probability: "
												+ tempGen.getMutationProbability(),
												"Confirm", JOptionPane.INFORMATION_MESSAGE);
								JOptionPane.showMessageDialog(null,
										"A Genetic Algorithm CANNOT be stopped once the execution start,\nmake sure the data inserted is correct before continuing!",
										"Warning", JOptionPane.WARNING_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "This execution is already in the queue",
										"Duplicate Value", JOptionPane.WARNING_MESSAGE);
							}
						}
						// Do this if vector is empty
						else {
							algQueueExecution.add(tempGen);
							JOptionPane.showMessageDialog(null,
									"Element Added!" + "\nPopulation Size: " + tempGen.getPopSize() + "\nMax Gen: "
											+ tempGen.getMaxGen() + "\nCrossover Method: "
											+ tempGen.getCrossoverMethod() + "\nCrossover Probability: "
											+ tempGen.getCrossoverProbability() + "\nMutation Method: "
											+ tempGen.getMutationMethod() + "\nMutation Probability: "
											+ tempGen.getMutationProbability(),
											"Confirm", JOptionPane.INFORMATION_MESSAGE);
							JOptionPane.showMessageDialog(null,
									"A Genetic Algorithm CANNOT be stopped once the execution start,\nmake sure the data inserted is correct before continuing!",
									"Warning", JOptionPane.WARNING_MESSAGE);
						}
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Enter valid numbers please!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "Add more points to the drawing area first"+numPoints, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		// Action to perform when the user requests to view the results or edit
		// the queue of execution
		if (actionE.getSource().equals(viewResultsButton)) {
			// Check that the vector has got any element in it and it is not
			// empty
			if (algQueueExecution.size() > 0) {
				@SuppressWarnings("unused")
				ExecutionQueueView execView = new ExecutionQueueView(algQueueExecution);
			}
			// Show an error message otherwise
			else {
				JOptionPane.showMessageDialog(null, "No Data to display", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		// Remove all points and empty the execution queue to start with new
		// data
		if (actionE.getSource().equals(resetAllFieldsButton)) {
			if (algQueueExecution.size() > 0) {
				drawingArea.clearAllPoints();
				points=drawingArea.getAllPoints();
				algQueueExecution.clear();
				numDrawnPoints.setText("Points: 0");
				randomPointsTF.setText("");
				currentRunningAlgTF.setText("");
				currentRunningTimeTF.setText("");
				addToExecution.setEnabled(true);
				startExecutionButton.setText("Start");
				startExecutionButton.setEnabled(true);
				// Re-enable all the operations
				this.tradGenView(3);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pcEvent) {
		// Actions to perform every time the program receives a "progress"
		// property change, coming from any algorithm performing its operations
		if (pcEvent.getPropertyName().equals("progress")) {
			// In case the running algorithm is a "Closest Neighbour"
			if (listenToChanges.equals("cn")) {
				// If the progress is 100, it means it is completed, therefore,
				// do these actions
				if (closestNeighbourAlg.getProgress() == 100 || closestNeighbourAlg.isDone()) {
					// Refresh the drawing area one last time in case of last
					// second changes
					drawingArea.performLinks(true, closestNeighbourAlg.getListOfCities(),
							closestNeighbourAlg.getTravellingOrder());
					startExecutionButton.setText("Start");
					// Get the only element in the vector and update it with the
					// execution information
					TradResultData transferData = (TradResultData) algQueueExecution.get(index);
					transferData.setTourLength(closestNeighbourAlg.getTourDistance());
					transferData.setTimeExecution(closestNeighbourAlg.getExecutionTime());
					transferData.setResultingPoints(closestNeighbourAlg.getTravellingOrder());
					transferData.setCities(closestNeighbourAlg.getListOfCities());
					transferData.setResultingPoints(closestNeighbourAlg.getTravellingOrder());
					// Increment the counter and verify if other algorithms are
					// awaiting to be executed
					if (!execStopped && index < algQueueExecution.size()) {
						progressBar.setValue(((index + 1) * 100) / algQueueExecution.size());
						index++;
						this.beginQueueExecution();
					}
				}
				// Do this in case the progress is not 100, which means the
				// thread is running fine and keeps the Thread of this view
				// updated to show the changes to the user
				else if (closestNeighbourAlg.getProgress() != 100) {
					// Update the progress
					currentRunningAlgTF
					.setText("(" + (index + 1) + "/" + algQueueExecution.size() + ") - " + currentRunningAlg);
					// Update the execution time
					long tempTime = (System.currentTimeMillis() - currentRunningTimeExec);
					currentRunningTimeTF
					.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(tempTime),
							TimeUnit.MILLISECONDS.toSeconds(tempTime) % 60, (tempTime % 100)));
					// Refresh the points on screen
					drawingArea.performLinks(true, closestNeighbourAlg.getListOfCities(),
							closestNeighbourAlg.getTravellingOrder());
				}
			}
			// In case the running algorithm is a "Greedy Heuristic"
			else if (listenToChanges.equals("gh")) {
				// If the progress is 100, it means it is completed, therefore,
				// do these actions
				if (greedyHeuristicAlg.getProgress() == 100 || greedyHeuristicAlg.isDone()) {
					// Refresh the drawing area one last time in case of last
					// second changes
					drawingArea.performLinks(true, greedyHeuristicAlg.getListOfCities(),
							greedyHeuristicAlg.getTravellingOrder());
					startExecutionButton.setText("Start");
					// Get the only element in the vector and update it with the
					// execution information
					TradResultData transferData = (TradResultData) algQueueExecution.get(index);
					transferData.setTourLength(greedyHeuristicAlg.getTourDistance());
					transferData.setTimeExecution(greedyHeuristicAlg.getExecutionTime());
					transferData.setResultingPoints(greedyHeuristicAlg.getTravellingOrder());
					transferData.setCities(greedyHeuristicAlg.getListOfCities());
					transferData.setResultingPoints(greedyHeuristicAlg.getTravellingOrder());
					// Increment the counter and verify if other algorithms are
					// awaiting to be executed
					if (!execStopped && index < algQueueExecution.size()) {
						progressBar.setValue(((index + 1) * 100) / algQueueExecution.size());
						index++;
						this.beginQueueExecution();
					}
				}
				// Do this in case the progress is not 100, which means the
				// thread is running fine and keeps the Thread of this view
				// updated to show the changes to the user
				else if (greedyHeuristicAlg.getProgress() != 100) {
					currentRunningAlgTF
					.setText("(" + (index + 1) + "/" + algQueueExecution.size() + ") - " + currentRunningAlg);
					// Update the execution time
					long tempTime = (System.currentTimeMillis() - currentRunningTimeExec);
					currentRunningTimeTF
					.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(tempTime),
							TimeUnit.MILLISECONDS.toSeconds(tempTime) % 60, (tempTime % 100)));
					// Refresh the points
					drawingArea.performLinks(true, greedyHeuristicAlg.getListOfCities(),
							greedyHeuristicAlg.getTravellingOrder());
				}
			}
			// In case the running algorithm is a "Genetic Algorithm"
			else if (listenToChanges.equals("gas")) {
				try {
					// If the progress is 100, it means it is completed,
					// therefore, do these actions
					if (tspWorker.getProgress() == 100 || tspWorker.isDone()) {
						int indexElement = tspAlg.getBestPathIndex();
						GenResultData transferData = (GenResultData) algQueueExecution.get(index);
						transferData.setPathDistances(((GenResultData) currentGeneticAlg).getPathDistances(), true);
						transferData.setResultsData(((GenResultData) currentGeneticAlg).getResultsData(), true);
						transferData.setResultingPoints(resultsDataTSP.elementAt(indexElement), true);
						transferData.setGenerationCount(resultsDataTSP.size());
						transferData.setFitness(pathDistancesTSP.elementAt(indexElement));
						transferData.setExecutionTime(tspWorker.getExecutionTime());
						transferData.setResultingPoints(resultsDataTSP.elementAt(indexElement), false);
						transferData.setIndexBestResultAtGeneration(indexElement);
						// Refresh the drawing area one last time in case of
						// last second changes
						drawingArea.performLinks(true, ((GenResultData) currentGeneticAlg).getCities(),
								resultsDataTSP.elementAt(indexElement));
						startExecutionButton.setText("Start");
						startExecutionButton.setEnabled(true);
						// Increment the counter and verify if other algorithms
						// are awaiting to be executed
						if (!execStopped && index < algQueueExecution.size()) {
							progressBar.setValue(((index + 1) * 100) / algQueueExecution.size());
							index++;
							this.beginQueueExecution();
						}
					}
					// Do this in case the progress is not 100, which means the
					// thread is running fine and keeps the Thread of this view
					// updated to show the changes to the user
					else if (tspWorker.getProgress() != 100) {
						currentRunningAlgTF.setText(
								"(" + (index + 1) + "/" + algQueueExecution.size() + ") - " + currentRunningAlg);
						long tempTime = (System.currentTimeMillis() - currentRunningTimeExec);
						currentRunningTimeTF
						.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(tempTime),
								TimeUnit.MILLISECONDS.toSeconds(tempTime) % 60, (tempTime % 100)));
						// Refresh the points
						drawingArea.performLinks(true, tspAlg.citiesVector, resultsDataTSP.lastElement());
					}
				}
				// In case the program goes into a catch exception, the genetic
				// algorithm was stopped because there has been a configuration
				// problem
				// In this case, stop the execution and wait until the user
				// resets the application data
				catch (Exception e) {
					currentRunningAlgTF.setText("");
					progressBar.setVisible(false);
					// Refresh the GUI
					this.repaint();
					drawingArea.performLinks(false, tspAlg.citiesVector, null);
					// Stop the user from running the queue again, which would
					// generate the same error.
					startExecutionButton.setEnabled(false);
					GenResultData transferData = (GenResultData) algQueueExecution.get(index);
					transferData.setPathDistances(null, true);
					transferData.setResultsData(null, true);
					transferData.setResultingPoints(null, true);
					transferData.setGenerationCount(0);
					transferData.setFitness(0);
					transferData.setExecutionTime(0);
					transferData.setResultingPoints(null, true);
					currentRunningTimeExec = 0;
					JOptionPane.showMessageDialog(null,
							"Multiple configuration are not accepted!\nExecution Interrupted and blocked,\n\nPress Reset All to perform another list of algorithms",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.
	 * ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent cE) {
		// Action to perform when the slider belonging to the crossover
		// probability changes its value
		if (cE.getSource().equals(crossProbSlider)) {
			crossoverProb = crossProbSlider.getValue();
			// Update the value on screen
			crossoverProbLabel.setText("Probability: " + crossoverProb + "%");
		}
		// Action to perform when the slider belonging to the mutation
		// probability changes its value
		if (cE.getSource().equals(mutProbSlider)) {
			mutationProb = mutProbSlider.getValue();
			// Update the value on screen
			mutationProbLabel.setText("Probability: " + mutationProb + "%");
		}
	}

	/*
	 * This method starts the execution of the queue of algorithms, casting each
	 * element in the queue to its correct instance class and setting the right
	 * references to objects used to update the screen and show the progress of
	 * the application to the end user
	 */
	private void beginQueueExecution() {
		// Verify whether the algorithm to perform is Traditional or Genetic
		startExecutionButton.setText("Stop");
		currentRunningAlgTF.setForeground(Color.BLACK);
		try {
			// Retrieve object from the queue
			Object obj = algQueueExecution.get(index);
			// In case it is a Traditional algorithm, follow these steps
			if (obj instanceof TradResultData) {
				// Copy the references of the cities and resulting path vectors
				results = ((TradResultData) obj).getResultingPoints();
				points = ((TradResultData) obj).getCities();
				currentRunningAlg = ((TradResultData) obj).getAlgName();
				// Clear the vector containing old execution results
				((TradResultData) obj).resetResultData();
				// Verify which heuristic algorithm it belongs to
				if (((TradResultData) obj).getAlgName().equals("Closest Neighbour")) {
					// Listen to changes for this specific class
					listenToChanges = "cn";
					// Create an instance of this class and start the execution
					closestNeighbourAlg = new ClosestNeighbour(((TradResultData) obj).getCities(),
							((TradResultData) obj).getResultingPoints());
					closestNeighbourAlg.addPropertyChangeListener(this);
					closestNeighbourAlg.execute();
				}
				if (((TradResultData) obj).getAlgName().equals("Greedy Heuristic")) {
					// Listen to changes for this specific class
					listenToChanges = "gh";
					// Create an instance of this class and start the execution
					greedyHeuristicAlg = new GreedyHeuristic(((TradResultData) obj).getCities(),
							((TradResultData) obj).getResultingPoints());
					greedyHeuristicAlg.addPropertyChangeListener(this);
					greedyHeuristicAlg.execute();
				}
				if (((TradResultData) obj).getAlgName().equals("Insertion Heuristic")) {
					// TO IMPLEMENT
				}
			}
			// Otherwise check that it is a genetic algorithm and do these
			// actions
			else if (obj instanceof GenResultData) {
				listenToChanges = "gas";
				currentGeneticAlg = (GenResultData) obj;
				int maxG = currentGeneticAlg.getMaxGen();
				// Retrieve the references to the vectors that are going to be
				// used to store the data while the algorithm performs its
				// operations
				Vector<Vector<Point>> tempVecVec = currentGeneticAlg.getResultsData();
				Vector<Point> tempSolution = currentGeneticAlg.getCities();
				Vector<Double> tempDistances = currentGeneticAlg.getPathDistances();
				// Clear any data contained within these vectors
				tempVecVec.clear();
				tempSolution.clear();
				tempDistances.clear();
				@SuppressWarnings("unchecked")
				Vector<Point> tempCities = (Vector<Point>) currentGeneticAlg.getTSP().getCities().clone();
				// Assign a population size to the TSP genetic configuration,
				// using this mathematical formula
				int populationSize = (1 * (int) ((Math.log(1 - Math.pow(0.99, (1.0 / tempCities.size()))))
						/ (Math.log(((float) (tempCities.size() - 3) / (float) (tempCities.size() - 1))))));
				TSP_GA tsp = new TSP_GA(tempCities, null, tempVecVec, tempDistances,
						currentGeneticAlg.getCrossoverMethod(), (int) currentGeneticAlg.getCrossoverProbability(),
						currentGeneticAlg.getMutationMethod(), (int) currentGeneticAlg.getMutationProbability());
				TSP_GA_Worker worker = new TSP_GA_Worker(tempCities, tempVecVec, tempDistances, maxG,
						currentGeneticAlg.getCrossoverMethod(), (int) currentGeneticAlg.getCrossoverProbability(),
						currentGeneticAlg.getMutationMethod(), (int) currentGeneticAlg.getMutationProbability());
				GenResultData tempGen = new GenResultData(tempCities.size(), populationSize, maxG,
						currentGeneticAlg.getCrossoverMethod(), currentGeneticAlg.getCrossoverProbability(),
						currentGeneticAlg.getMutationMethod(), currentGeneticAlg.getMutationProbability(), tsp, worker);
				// Pass the references to the vector to the GenResultData object
				// which is going to keep all the values so the user can see
				// them once exported
				tempGen.setCities(tempCities);
				tempGen.setResultsData(tempVecVec, true);
				tempGen.setPathDistances(tempDistances, true);
				currentGeneticAlg = tempGen;
				// Copy the memory allocations of the following objects
				tspAlg = currentGeneticAlg.getTSP();
				tspWorker = currentGeneticAlg.getTSP_Worker();
				results = currentGeneticAlg.getResultingPoints();
				points = currentGeneticAlg.getCities();
				resultsDataTSP = currentGeneticAlg.getResultsData();
				pathDistancesTSP = currentGeneticAlg.getPathDistances();
				currentRunningAlg = "Genetic Algorithm";
				tspWorker.addPropertyChangeListener(this);
				startExecutionButton.setEnabled(false);
				// Start the execution of the algorithm
				tspWorker.execute();
			}
		}
		// This code is executed when the vector has reached its end and the
		// execution is completed
		catch (Exception e) {
			startExecutionButton.setText("Start");
			currentRunningAlgTF.setForeground(Color.RED);
			currentRunningAlgTF.setText("COMPLETED!");
			progressBar.setVisible(false);
			currentRunningTimeExec = 0;
			// Update the graph one last time
			drawingArea.repaint();
		}
	}

	/*
	 * @param methodSelected This integer value can be wither 0,1,2 or 3. Each
	 * value has a different meaning which are: 0)Display only Traditional
	 * algorithm options 1)Display only Genetic algorithm options 2)Disable both
	 * traditional and genetic fields, including the drawing points panel
	 * 3)Re-enable either traditional or genetic panel and the drawing panel
	 * 
	 * This method changes the view of the application depending on the value
	 * passed as parameter
	 * 
	 */
	private void tradGenView(int methodSelected) {
		// Enable only the Tradition algorithms
		if (methodSelected == 0) {
			tradPanel.setEnabled(true);
			genPanel.setEnabled(false);
			// Disable all the objects contained within the genPanel
			Component[] geneticComponents = genPanel.getComponents();
			for (Component c : geneticComponents) {
				c.setEnabled(false);
			}
			popSizeTF.setText("");
			// Enable all the objects contained within the tradPanel
			Component[] tradComponents = tradPanel.getComponents();
			for (Component c : tradComponents) {
				c.setEnabled(true);
			}
			trad1CB.setSelected(false);
			trad2CB.setSelected(false);
			trad3CB.setSelected(false);
		}
		// Enable only the Genetic algorithms
		else if (methodSelected == 1) {
			tradPanel.setEnabled(false);
			genPanel.setEnabled(true);
			// Enable all the objects contained within the genPanel
			Component[] geneticComponents = genPanel.getComponents();
			for (Component c : geneticComponents) {
				c.setEnabled(true);
			}
			// Disable all the objects contained within the tradPanel
			Component[] tradComponents = tradPanel.getComponents();
			for (Component c : tradComponents) {
				c.setEnabled(false);
			}
			// Update the value for the population size
			this.updatePopulationSize(points.size());
		}
		// Disable both traditional and genetic fields, including the drawing
		// points panel
		else if (methodSelected == 2) {
			tradPanel.setEnabled(false);
			genPanel.setEnabled(false);
			destinationPanel.setEnabled(false);
			tradGAChooser.setEnabled(false);
			// Disable all the objects contained within the genPanel
			Component[] geneticComponents = genPanel.getComponents();
			for (Component c : geneticComponents) {
				c.setEnabled(false);
			}
			// Disable all the objects contained within the tradPanel
			Component[] tradComponents = tradPanel.getComponents();
			for (Component c : tradComponents) {
				c.setEnabled(false);
			}
			// Disable all the objects contained within the destinationPanel
			Component[] drawingComponents = destinationPanel.getComponents();
			for (Component c : drawingComponents) {
				c.setEnabled(false);
			}
			popSizeTF.setText("");
			maxGenTF.setText("");
			trad1CB.setSelected(false);
			trad2CB.setSelected(false);
			trad3CB.setSelected(false);

		}
		// Re-enable either traditional or genetic panel and the drawing panel
		else if (methodSelected == 3) {
			destinationPanel.setEnabled(true);
			tradGAChooser.setEnabled(true);
			// Enable all the objects contained within the destinationPanel
			Component[] drawingComponents = destinationPanel.getComponents();
			for (Component c : drawingComponents) {
				c.setEnabled(true);
			}
			this.tradGenView(tradGAChooser.getSelectedIndex());
		}
	}

	/*
	 * @param size This value represents the number of cities the vector
	 * contains and it will be used to calculate the population size
	 * 
	 * This method uses a mathematical formula containing Logs to produce a
	 * correct value for the population size that is going to be used during the
	 * evolution of the chromosomes
	 */
	private void updatePopulationSize(int size) {
		int populationSize = (1 * (int) ((Math.log(1 - Math.pow(0.99, (1.0 / points.size()))))
				/ (Math.log(((float) (points.size() - 3) / (float) (points.size() - 1))))));
		// Set the value just calculated to the JTextField to make it visible to
		// the user
		popSizeTF.setText("" + populationSize);
	}

	/*
	 * This method creates a new pop up window asking to user to confirm or
	 * reject exiting the application
	 */
	private void initExitRequest() {
		exitReq = new ExitRequest();
		exitReq.setTitle("Exit");
		exitReq.setVisible(true);
		exitReq.setResizable(false);
		exitReq.setSize(280, 150);
		exitReq.setLocation(500, 300);
	}
}
