import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
/** 
 * This Class creates the GUI application and starts background threads for the data elaboration.
 * It also provides methods to elaborates the data and display it correctly on the screen
 * 
 * @author Michael Piccoli - 12099662
 * @version 1.0
 * @since 5 January 2015
 * @see JPanel
 * @see AlgorithmExecution
 * @see ActionListener
 * @see PropertyChangeListener
 * @see ArrayList
 * @see JToolBar
 * @see JMenuBar
 * @see JFileChooser
 */

public class Main_Panel extends JPanel implements ActionListener, PropertyChangeListener{
	//Global Variables
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	JToolBar toolBar;
	private MatteBorder matteB;
	JMenuBar menuBar;
	private JButton bubbleS, selectionS, mergeS, quickS, insertionS, cocktailS, analysis, startSort, stopSort, anBubbleATB, anSelectionATB, anMergeATB, anQuickATB, anInsertionATB, anCocktailATB, reRunAnalysis, importArrayData;
	private JMenu fileMenu, helpMenu;
	private JMenuItem close, exportTXT, exportCSV, importCSV, help;
	private AlgorithmExecution threadExecution, threadExecutionNoSleep;
	private JProgressBar progressBar;
	private JLabel timerL, arraySizeL, arrayVisitL, algNameL, anAlgL, anBubbleL, anSelectionL, anMergeL, anQuickL, anInsertionL, anCocktailL, anShortTimeL, anLongTimeL, anAvgTimeL, anAllTimes;
	private JTextField timerTF, arraySizeTF, arrayVisitTF, shortTBubbleTF, shortTSelectionTF, shortTMergeTF, shortTQuickTF, shortTInsertionTF, shortTCocktailTF, longTBubbleTF, longTSelectionTF, longTMergeTF, longTQuickTF, longTInsertionTF, longTCocktailTF, 
						avgTBubbleTF, avgTSelectionTF, avgTMergeTF, avgTQuickTF, avgTInsertionTF, avgTCocktailTF, anBubbleColour, anSelectionColour, anMergeColour, anQuickColour, anInsertionColour, anCocktailColour;
	private JTextArea algDescriptionTF;
	private String algNameBubble, algNameSelection, algNameMerge, algNameQuick, algNameInsertion, algNameCocktail, algDescriptionBubble, algDescriptionSelection, algDescriptionMerge, algDescriptionQuick, algDescriptionInsertion, algDescriptionCocktail;
	private boolean listenToChanges, analysisClicked, finished, hasDataBeenImported, hasCSVBeenImported;
	private char algSel;
	private int loopCounter;
	private Long timeStart, timeEnd;
	private JFileChooser saveTxt, saveCSV, CSVImport, dataImport;
	private FileNameExtensionFilter filterTXTExport, filterCSVExport, filterCSVImport;
	private ArrayList<Integer> arrayData, arrayResult;
	private ArrayList<ArrayList<Long>> timeStore;
	private ArrayList<ArrayList<Integer>> arrayStorage;
	private ArrayList<Integer> tempData;
	private Image imageBackground;
	/** Constructor with parameters 
	 * 
	 * @param f		This contains the frame of the window
	 * 
	 */
	public Main_Panel(JFrame f){
		//Image Reference: https://www.andapponline.com/pictures/2014/4/simple-galaxy-s4-background-193-aOY1xZ.jpg
		imageBackground = Toolkit.getDefaultToolkit().createImage("Images/backgroundFrame.jpg");
		frame=f;
		//Set the layout to NULL so the author can position the elements where he wants to
		this.setLayout(null);
		//Tool bar
		toolBar=new JToolBar();
		toolBar.setSize(800, 40);
		//This creates a line above the JToolBar
		matteB = new MatteBorder(1, 0, 0, 0, Color.BLACK);
		//ToolBar elements
		bubbleS=new JButton("Bubble Sort");
		selectionS=new JButton("Selection Sort");
		mergeS=new JButton("Merge Sort");
		quickS=new JButton("Quick Sort");
		insertionS=new JButton("Insertion Sort");
		cocktailS=new JButton("Cocktail Sort");
		analysis=new JButton("Analysis");
		
		//MenuBar
		menuBar=new JMenuBar();
		fileMenu=new JMenu("File");	
		helpMenu=new JMenu("Help");
		//MenuBar elements
		close=new JMenuItem("Close");
		exportTXT=new JMenuItem("Export Analysis (.TXT)");
		exportCSV=new JMenuItem("Export Analysis (.CSV)");
		importCSV=new JMenuItem("Import Analysis (.CSV)");
		help=new JMenuItem("Help");
		//Add the elements to the menu "File" and menu "Help)
		fileMenu.add(exportTXT);
		fileMenu.add(exportCSV);
		exportTXT.setEnabled(false);
		exportCSV.setEnabled(false);
		fileMenu.add(importCSV);
		fileMenu.add(close);
		helpMenu.add(help);
		//Add the menu "File" to the MenuBar
		menuBar.add(fileMenu);	
		menuBar.add(helpMenu);
		toolBar.setBorder(matteB);
		//Fit the tool bar to the frame
		toolBar.setFloatable(false);
		//centre the elements of the tool bar
		toolBar.setLayout((LayoutManager) new FlowLayout(FlowLayout.CENTER));
		//Add elements to the tool bar
		toolBar.add(bubbleS);
		toolBar.add(selectionS);
		toolBar.add(mergeS);
		toolBar.add(quickS);
		toolBar.add(insertionS);
		toolBar.add(cocktailS);
		toolBar.add(analysis);	

		//Progress bar
		progressBar = new JProgressBar();
		loopCounter=1;
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		Border border = BorderFactory.createTitledBorder("Loading...");
		progressBar.setBorder(border);
		progressBar.setBounds(40,10,160,40);
		this.add(progressBar);
		progressBar.setVisible(false);
		progressBar.setOpaque(false);
		//Initialise the elements
		startSort=new JButton("Start Sorting");
		stopSort=new JButton("Stop Sorting");
		stopSort.setEnabled(false);
		timerL=new JLabel("Timer");
		arraySizeL=new JLabel("Array Size");
		arrayVisitL=new JLabel("Array Visits");
		algNameBubble="Bubble Sort Algorithm";
		algNameSelection="Selection Sort Algorithm";
		algNameMerge="Merge Sort Algorithm";
		algNameQuick="Quick Sort Algorithm";
		algNameInsertion="Insertion Sort Algorithm";
		algNameCocktail="Cocktail Sort";
		//Algorithm descriptions
		algDescriptionBubble="Bubble sort is a sorting algorithm that works by repeatedly stepping through lists that need to be sorted,\n"
				+ "comparing each pair of adjacent items and swapping them if they are in the wrong order. \n\nThis passing procedure is repeated "
				+ "until no swaps are required, indicating that the list is sorted.\n\nBubble sort gets its name because smaller elements bubble "
				+ "toward the top of the list. \n\nBubble sort is also referred to as sinking sort or comparison sort. (Techopedia, 2015:Online)";
		algDescriptionSelection="A sorting technique that is typically used for sequencing small lists. \n\nIt starts by comparing the entire "
				+ "list for the lowest item and moves it to the #1 position. \nIt then compares the rest of the list for the next-lowest item "
				+ "and places it in the #2 position and so on \nuntil all items are in the required order.\n\n"
				+ "Selection sorts perform numerous comparisons, but fewer data movements than other methods. \n(Encyclopedia, 2015:Online)";
		algDescriptionMerge="A sorting technique that sequences data by continuously merging items in the list. \n\nEvery single item in the "
				+ "original unordered list is merged with another, creating groups of two. \n\nEvery two-item group is merged, creating "
				+ "groups of four and so on until there is one ordered list.\n(Ecyclopedia, 2015:Online)\n";
		algDescriptionQuick="A sorting technique that sequences a list by continuously dividing the list into two parts and moving the \n"
				+ "lower items to one side and the higher items to the other. \nIt starts by picking one item in the entire list to serve "
				+ "as a pivot point. The pivot could be the first item or \na randomly chosen one. All items that compare lower than the "
				+ "pivot are moved to the left of the pivot; \nall equal or higher items are moved to the right. \n"
				+ "It then picks a pivot for the left side and moves those items to left and right of the pivot and continues\nthe pivot "
				+ "picking and dividing until there is only one item left in the group. \nIt then proceeds to the right side and performs "
				+ "the same operation again. (Encyclopedia, 2015:Online)";
		algDescriptionInsertion="Insertion sort is an elementary sorting algorithm that sorts one element at a time. \n\n"
				+ "Most humans, when sorting a deck of cards, will use a strategy similar to insertion sort. \n\n"
				+ "The algorithm takes an element from the list and places it in the correct location in the list. "
				+ "This process is \nrepeated until there are no more unsorted items in the list. \n\n"
				+ "The computational complexity for insertion sort is O(n2). (Chegg, 2015:Online)";
		algDescriptionCocktail="Cocktail sort is a sorting algorithm that works by repeatedly stepping through lists that need to be sorted,\n"
				+ "comparing each pair of adjacent items and swapping them if they are in the wrong order. \n\n"
				+ "This algorithm uses the bubble sort technique to compare and swap "
				+ "elements, the only difference \nis that this method scans and swap elements both ways: from the left to the right and from the right to the left.\n"
				+ "(Techopedia, 2015:Online)";
		timerTF=new JTextField();
		arraySizeTF=new JTextField();
		arrayVisitTF=new JTextField();
		algNameL=new JLabel(""+algNameBubble);
		algNameL.setFont(new Font("Calibri", Font.BOLD, 18));
		algDescriptionTF=new JTextArea(""+algDescriptionBubble);
		algDescriptionTF.setFont(new Font("Calibri", Font.ITALIC, 16));
		algDescriptionTF.setEditable(false);
		algDescriptionTF.setOpaque(false);
		anAlgL=new JLabel("Algorithm");
		anBubbleL=new JLabel("Bubble Sort");
		anSelectionL=new JLabel("Selection Sort");
		anMergeL=new JLabel("Merge Sort");
		anQuickL=new JLabel("Quick Sort");
		anInsertionL=new JLabel("Insertion Sort");
		anCocktailL=new JLabel("Cocktail Sort");
		anShortTimeL=new JLabel("Shortest time");
		anLongTimeL=new JLabel("Longest time");
		anAvgTimeL=new JLabel("AVG time");
		shortTBubbleTF=new JTextField();
		shortTSelectionTF=new JTextField();
		shortTMergeTF=new JTextField();
		shortTQuickTF=new JTextField();
		shortTInsertionTF=new JTextField();
		shortTCocktailTF=new JTextField();
		longTBubbleTF=new JTextField();
		longTSelectionTF=new JTextField();
		longTMergeTF=new JTextField();
		longTQuickTF=new JTextField();
		longTInsertionTF=new JTextField();
		longTCocktailTF=new JTextField();
		avgTBubbleTF=new JTextField();
		avgTSelectionTF=new JTextField();
		avgTMergeTF=new JTextField();
		avgTQuickTF=new JTextField();
		avgTInsertionTF=new JTextField();
		avgTCocktailTF=new JTextField();
		anAllTimes=new JLabel("All Times");
		anBubbleATB=new JButton("Open");
		anSelectionATB=new JButton("Open");
		anMergeATB=new JButton("Open");
		anQuickATB=new JButton("Open");
		anInsertionATB=new JButton("Open");
		anCocktailATB=new JButton("Open");
		reRunAnalysis=new JButton("Re-Run All Algorithms");
		anBubbleColour=new JTextField();
		anBubbleColour.setBackground(Color.BLUE);
		anBubbleColour.setEditable(false);
		anSelectionColour=new JTextField();
		anSelectionColour.setBackground(new Color(0.12f, 0.45f, 0));
		anSelectionColour.setEditable(false);
		anMergeColour=new JTextField();
		anMergeColour.setBackground(Color.RED);
		anMergeColour.setEditable(false);
		anQuickColour=new JTextField();
		anQuickColour.setBackground(new Color(0.16f, 0.6f, 0.7f));
		anQuickColour.setEditable(false);
		anInsertionColour=new JTextField();
		anInsertionColour.setBackground(Color.WHITE);
		anInsertionColour.setEditable(false);
		anCocktailColour=new JTextField();
		anCocktailColour.setBackground(Color.GRAY);
		anCocktailColour.setEditable(false);
		importArrayData=new JButton("Import Array Data");
		hasDataBeenImported=false;
		hasCSVBeenImported=false;
		analysisClicked=false;
		finished=true;
		//Essential objects related to the import/export part of the application
		saveTxt = new JFileChooser();
		filterTXTExport = new FileNameExtensionFilter(".txt","txt");
		saveCSV = new JFileChooser();
		filterCSVExport = new FileNameExtensionFilter(".csv", "csv");
		CSVImport = new JFileChooser();
		filterCSVImport = new FileNameExtensionFilter(".csv", "csv");
		dataImport = new JFileChooser();
		//Attach elements to the JFrame
		this.add(startSort);
		this.add(stopSort);
		this.add(timerL);
		this.add(arraySizeL);
		this.add(arrayVisitL);
		this.add(algNameL);
		this.add(algDescriptionTF);
		this.add(timerTF);
		this.add(arraySizeTF);
		this.add(arrayVisitTF);
		this.add(anAlgL);
		this.add(anBubbleL);
		this.add(anSelectionL);
		this.add(anMergeL);
		this.add(anQuickL);
		this.add(anInsertionL);
		this.add(anCocktailL);
		this.add(anShortTimeL);
		this.add(anLongTimeL);
		this.add(anAvgTimeL);
		this.add(shortTBubbleTF);
		this.add(shortTSelectionTF);
		this.add(shortTMergeTF);
		this.add(shortTQuickTF);
		this.add(shortTInsertionTF);
		this.add(shortTCocktailTF);
		this.add(longTBubbleTF);
		this.add(longTSelectionTF);
		this.add(longTMergeTF);
		this.add(longTQuickTF);
		this.add(longTInsertionTF);
		this.add(longTCocktailTF);
		this.add(avgTBubbleTF);
		this.add(avgTSelectionTF);
		this.add(avgTMergeTF);
		this.add(avgTQuickTF);
		this.add(avgTInsertionTF);
		this.add(avgTCocktailTF);
		this.add(anAllTimes);
		this.add(anBubbleATB);
		this.add(anSelectionATB);
		this.add(anMergeATB);
		this.add(anQuickATB);
		this.add(anInsertionATB);
		this.add(anCocktailATB);
		this.add(anBubbleColour);
		this.add(anSelectionColour);
		this.add(anMergeColour);
		this.add(anQuickColour);
		this.add(anInsertionColour);
		this.add(anCocktailColour);
		this.add(reRunAnalysis);
		this.add(importArrayData);
		//Fit the elements to the JFrame
		startSort.setBounds(250,30,140,30);
		stopSort.setBounds(410,30,140,30);
		timerL.setBounds(10,0,80,30);
		arraySizeL.setBounds(10,25,80,30);
		arrayVisitL.setBounds(10,50,80,30);
		algNameL.setBounds(300,250,200,30);
		algDescriptionTF.setBounds(10,290,775,200);
		timerTF.setBounds(100,5,80,20);
		arraySizeTF.setBounds(100,30,80,20);
		arrayVisitTF.setBounds(100,55,80,20);
		anAlgL.setBounds(560,55,60,30);
		anBubbleL.setBounds(560,95,80,30);
		anBubbleColour.setBounds(560,120,70,10);
		anSelectionL.setBounds(560,135,80,30);
		anSelectionColour.setBounds(560,160,70,10);
		anMergeL.setBounds(560,175,80,30);
		anMergeColour.setBounds(560,200,70,10);
		anQuickL.setBounds(560,215,80,30);
		anQuickColour.setBounds(560,240,70,10);
		anInsertionL.setBounds(560,255,80,30);
		anInsertionColour.setBounds(560,280,70,10);
		anCocktailL.setBounds(560,295,80,30);
		anCocktailColour.setBounds(560,320,70,10);
		anShortTimeL.setBounds(650,55,80,30);
		shortTBubbleTF.setBounds(660,105,70,20);
		shortTSelectionTF.setBounds(660,145,70,20);
		shortTMergeTF.setBounds(660,185,70,20);
		shortTQuickTF.setBounds(660,225,70,20);
		shortTInsertionTF.setBounds(660,265,70,20);
		shortTCocktailTF.setBounds(660,305,70,20);
		anLongTimeL.setBounds(750,55,80,30);
		longTBubbleTF.setBounds(750,105,70,20);
		longTSelectionTF.setBounds(750,145,70,20);
		longTMergeTF.setBounds(750,185,70,20);
		longTQuickTF.setBounds(750,225,70,20);
		longTInsertionTF.setBounds(750,265,70,20);
		longTCocktailTF.setBounds(750,305,70,20);
		anAvgTimeL.setBounds(840,55,60,30);
		avgTBubbleTF.setBounds(840,105,70,20);
		avgTSelectionTF.setBounds(840,145,70,20);
		avgTMergeTF.setBounds(840,185,70,20);
		avgTQuickTF.setBounds(840,225,70,20);
		avgTInsertionTF.setBounds(840,265,70,20);
		avgTCocktailTF.setBounds(840,305,70,20);
		anAllTimes.setBounds(920,55,70,30);
		anBubbleATB.setBounds(920,105,70,20);
		anSelectionATB.setBounds(920,145,70,20);
		anMergeATB.setBounds(920,185,70,20);
		anQuickATB.setBounds(920,225,70,20);
		anInsertionATB.setBounds(920,265,70,20);
		anCocktailATB.setBounds(920,305,70,20);
		reRunAnalysis.setBounds(250,20,170,20);
		importArrayData.setBounds(20,80,150,20);
		//Set the elements visibility
		timerTF.setEditable(false);
		arraySizeTF.setEditable(false);
		arrayVisitTF.setEditable(false);
		avgTMergeTF.setEditable(false);
		avgTSelectionTF.setEditable(false);
		avgTBubbleTF.setEditable(false);
		avgTQuickTF.setEditable(false);
		avgTInsertionTF.setEditable(false);
		avgTCocktailTF.setEditable(false);
		longTQuickTF.setEditable(false);
		longTMergeTF.setEditable(false);
		longTSelectionTF.setEditable(false);
		longTBubbleTF.setEditable(false);
		longTInsertionTF.setEditable(false);
		longTCocktailTF.setEditable(false);
		shortTQuickTF.setEditable(false);
		shortTMergeTF.setEditable(false);
		shortTSelectionTF.setEditable(false);
		shortTBubbleTF.setEditable(false);
		shortTInsertionTF.setEditable(false);
		shortTCocktailTF.setEditable(false);
		anAllTimes.setVisible(false);
		anBubbleATB.setVisible(false);
		anSelectionATB.setVisible(false);
		anMergeATB.setVisible(false);
		anQuickATB.setVisible(false);
		anInsertionATB.setVisible(false);
		anCocktailATB.setVisible(false);
		anAlgL.setVisible(false);
		anBubbleL.setVisible(false);
		anSelectionL.setVisible(false);
		anMergeL.setVisible(false);
		anQuickL.setVisible(false);
		anInsertionL.setVisible(false);
		anCocktailL.setVisible(false);
		anShortTimeL.setVisible(false);
		anLongTimeL.setVisible(false);
		anAvgTimeL.setVisible(false);
		shortTBubbleTF.setVisible(false);
		shortTSelectionTF.setVisible(false);
		shortTMergeTF.setVisible(false);
		shortTQuickTF.setVisible(false);
		shortTInsertionTF.setVisible(false);
		shortTCocktailTF.setVisible(false);
		longTBubbleTF.setVisible(false);
		longTSelectionTF.setVisible(false);
		longTMergeTF.setVisible(false);
		longTQuickTF.setVisible(false);
		longTInsertionTF.setVisible(false);
		longTCocktailTF.setVisible(false);
		avgTBubbleTF.setVisible(false);
		avgTSelectionTF.setVisible(false);
		avgTMergeTF.setVisible(false);
		avgTQuickTF.setVisible(false);
		avgTInsertionTF.setVisible(false);
		avgTCocktailTF.setVisible(false);
		anBubbleColour.setVisible(false);
		anSelectionColour.setVisible(false);
		anMergeColour.setVisible(false);
		anQuickColour.setVisible(false);
		anInsertionColour.setVisible(false);
		anCocktailColour.setVisible(false);
		reRunAnalysis.setVisible(false);
		//Add ActionListener to elements
		bubbleS.addActionListener(this);
		selectionS.addActionListener(this);
		mergeS.addActionListener(this);
		quickS.addActionListener(this);
		insertionS.addActionListener(this);
		cocktailS.addActionListener(this);
		analysis.addActionListener(this);
		exportCSV.addActionListener(this);
		exportTXT.addActionListener(this);
		importCSV.addActionListener(this);
		help.addActionListener(this);
		close.addActionListener(this);
		startSort.addActionListener(this);
		stopSort.addActionListener(this);
		anBubbleATB.addActionListener(this);
		anSelectionATB.addActionListener(this);
		anMergeATB.addActionListener(this);
		anQuickATB.addActionListener(this);
		anInsertionATB.addActionListener(this);
		anCocktailATB.addActionListener(this);
		reRunAnalysis.addActionListener(this);
		importArrayData.addActionListener(this);
		//When the application is launched, the predefined algorithm is bubbleSort
		algSel='b';
		//arrayData will contain all the elements unsorted
		arrayData=new ArrayList<Integer>();
		//The application will work on the arrayResult object, saving the data in sorted order here
		arrayResult=new ArrayList<Integer>();
		//timeStore will contains 1 array list with all the sorting times for each algorithm and 1 array list (saved in position 0) containing the size of the array data
		timeStore=new ArrayList<ArrayList<Long>>();
		listenToChanges=true;
		//arrayStorage will contain all the array data the application is creating in the analysis 
		arrayStorage= new ArrayList<ArrayList<Integer>>();
		this.setUpTimeStore();
		this.setUpArrayStorage();
	}
	
	/** This method draws on the JFrame
	 * 
	 * @param g			This is the object that allows drawing different shapes
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Set the background picture
		g.drawImage(imageBackground, 0, 0, this);
		//Graph for a single algorithm
		if(listenToChanges){
			if(arrayResult.size()!=0){
				try{
					float r = 0.1f;
					float p = 0.2f;
					float b = 0.8f;
					Color randomColour = new Color(r, p, b);
					g.setColor(randomColour);
					int space=0;
					for(int result: arrayResult){
						//Draw a line for each element in the array data
						g.drawLine(50+space, 250, 50+space, 200-result);
						space+=2;
					}
				}
				catch(Exception e){}
			}
		}
		//Graph for the analysis
		else{
			//find the biggest element
			float maxHeight= this.findMaxHeight(timeStore);
			int yLegend=(int) (maxHeight/10);
			for(int i=0; i<timeStore.size(); i++){
				int space=70,space1=62,xNumber=0;
				int x1=40, y1=455, y2=455;
				for(int j=0; j<timeStore.get(i).size(); j++){
					int height=0;
					//change line colour for each algorithm and find the right point on the graph to be drawn
					if(i==1){
						g.setColor(Color.BLUE);
						height=(int)((390/maxHeight)*timeStore.get(1).get(j).intValue());
					}
					if(i==2){
						g.setColor(new Color(0.12f, 0.45f, 0));
						height=(int)((390/maxHeight)*timeStore.get(2).get(j).intValue());
					}
					if(i==3){
						g.setColor(Color.RED);
						height=(int)((390/maxHeight)*timeStore.get(3).get(j).intValue());
					}
					if(i==4){
						g.setColor(new Color(0.16f, 0.6f, 0.7f));
						height=(int)((390/maxHeight)*timeStore.get(4).get(j).intValue());
					}
					if(i==5){
						g.setColor(Color.WHITE);
						height=(int)((390/maxHeight)*timeStore.get(5).get(j).intValue());
					}
					if(i==6){
						g.setColor(Color.GRAY);
						height=(int)((390/maxHeight)*timeStore.get(6).get(j).intValue());
					}
					//draw a squared background that helps to understand better the data
					if(i==0){
						//Bottom border
						g.drawLine(40,455,535,455);
						//Left border
						g.drawLine(40,460,40,60);
						//Top border
						g.drawLine(35,60,540,60);
						//Right border
						g.drawLine(540,60,540,460);
						//Label the X axes
						g.setColor(Color.BLACK);
						g.setFont(new Font("TimesRoman", Font.PLAIN, 10)); 
						g.drawString("S:"+timeStore.get(i).get(j),space,470);					
						//Label the Y axes
						g.drawString((yLegend+xNumber)+"mS",3, 480-space1);
						g.setColor(Color.GRAY);
						//Draw the inner grid
						g.drawLine(x1, 460, x1, 60);
						g.drawLine(35,y2,540,y2);
						y2-=40;

					}
					//If the index is different to 0 (index of the ArrayList containing the size of the sampled ArrayLists), draw
					else{
						//This allows to draw line with thickness '3' on the graph
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(3));
						//draw the line on the graph
						g2.drawLine(x1, y1, x1+50, 450-height);
						//Insert bullet point in the graph to indicate the exact point
						g2.drawImage(Toolkit.getDefaultToolkit().getImage("Images/bulletPoint.png"), x1+45, 445-height, this);
					}
					//increment the space on both X and Y axes for the next line to be drawn
					x1+=50;
					y1=450-height;
					space+=50;
					space1+=40;
					xNumber+=yLegend;
				}
			}
		}
	}
	
	/** This method performs operations every time a button or a selection is selected by the user
	 * 
	 * @param aE			This listens to button clicks and basing on what button has been pressed, certain operations will be performed
	 */
	@Override
	public void actionPerformed(ActionEvent aE) {
		if(aE.getSource().equals(bubbleS)){
			algSel='b';
			algNameL.setText(algNameBubble);
			algDescriptionTF.setText(algDescriptionBubble);
			//make visible/invisible certain object on the JFrame
			this.notAnalysisView();
		}
		if(aE.getSource().equals(selectionS)){
			algSel='s';
			algNameL.setText(algNameSelection);
			algDescriptionTF.setText(algDescriptionSelection);
			//make visible/invisible certain object on the JFrame
			this.notAnalysisView();
		}
		if(aE.getSource().equals(mergeS)){
			algSel='m';
			algNameL.setText(algNameMerge);
			algDescriptionTF.setText(algDescriptionMerge);
			//make visible/invisible certain object on the JFrame
			this.notAnalysisView();
		}
		if(aE.getSource().equals(quickS)){
			algSel='q';
			algNameL.setText(algNameQuick);
			algDescriptionTF.setText(algDescriptionQuick);
			//make visible/invisible certain object on the JFrame
			this.notAnalysisView();
		}
		if(aE.getSource().equals(insertionS)){
			algSel='i';
			algNameL.setText(algNameInsertion);
			algDescriptionTF.setText(algDescriptionInsertion);
			//make visible/invisible certain object on the JFrame
			this.notAnalysisView();
		}
		if(aE.getSource().equals(cocktailS)){
			algSel='c';
			algNameL.setText(algNameCocktail);
			algDescriptionTF.setText(algDescriptionCocktail);
			//make visible/invisible certain object on the JFrame
			this.notAnalysisView();
		}
		if(aE.getSource().equals(analysis)){
			//Change the view to Analysis
			this.analysisView();
			exportCSV.setEnabled(true);
			reRunAnalysis.setEnabled(true);
			//if the button "analysis" has not been clicked yet, run a background thread performing all the algorithms and returning relative times
			if(!analysisClicked){
				progressBar.setVisible(true);
				loopCounter=1;
				//Create some temporary array list to use in the analysis
				ArrayList<Integer> dataTemp=new ArrayList<Integer>();
				ArrayList<Integer> resultTemp=new ArrayList<Integer>();
				threadExecutionNoSleep=new AlgorithmExecution(dataTemp, resultTemp, timeStore, arrayStorage);
				threadExecutionNoSleep.addPropertyChangeListener(this);
				threadExecutionNoSleep.execute();
				analysisClicked=true;
			}
		}
		if(aE.getSource().equals(exportCSV)){
			try{
				//set the filter previously created to the file export frame
				saveCSV.setFileFilter(filterCSVExport);
				int rSel=saveCSV.showSaveDialog(this);
				if(rSel == JFileChooser.APPROVE_OPTION){
					//Generate an object able to write on the file with the specific name inserted previously
					PrintWriter fileWriter = new PrintWriter(saveCSV.getSelectedFile()+".csv");
					//write all the times the array timeStore contains
					for(int i=0; i<timeStore.size(); i++){
						for(Long value: timeStore.get(i)){
							fileWriter.print(value+",");
						}
						fileWriter.println();
					}
					//Close and release the file
					fileWriter.close();
                    //Save Confirmation
                    JOptionPane.showMessageDialog(null,"Data Exported Successfully!!!","Operation Completed",JOptionPane.INFORMATION_MESSAGE);
				}
				//Display this message in case the user cancel the operation
				else{
					JOptionPane.showMessageDialog(null,"Operation Aborted", "Cancelled", JOptionPane.CANCEL_OPTION);
				}
			}
			//Any errors occur, this message will be displayed to the user
			catch(Exception e){
				JOptionPane.showMessageDialog(null, "Error!!!, please try again later", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		if(aE.getSource().equals(exportTXT)){
			try{
				saveTxt.setFileFilter(filterTXTExport);
				int rSel=saveTxt.showSaveDialog(this);
				//When the user presses the YES button, the file is created and data is added to it
				if(rSel == JFileChooser.APPROVE_OPTION){
					PrintWriter fileWriter= new PrintWriter(saveTxt.getSelectedFile()+".txt");
                    DateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date current = new Date();
                    //Add the date of the report to the file
                    fileWriter.println("Analysis Report saved on "+currentDateFormat.format(current));
                    //Ask the user to insert his name to identify who saved the report
                    fileWriter.println("Performed by: "+JOptionPane.showInputDialog("Please, Insert Author name"));
                    fileWriter.println();
                    fileWriter.println();
                    fileWriter.println("---------------------------------");
                    int j=0, k=0;
                    for(Long value: timeStore.get(0)){
                    	//Write the array size
                    	fileWriter.println("Array Size: "+value);
                    	fileWriter.println();
                    	fileWriter.print("Data: ");
                    	fileWriter.println();
                    	//Write all the data enclosed in the array data
                    	fileWriter.print(arrayStorage.get(j++));
                    	fileWriter.println();
                    	fileWriter.print("Data Sorted: ");
                    	fileWriter.println();
                    	//Write all the data sorted
                    	fileWriter.print(arrayStorage.get(j++));
                    	fileWriter.println();
                    	fileWriter.println();
                    	fileWriter.println();
                    	fileWriter.println("Sorting Times -->");
                    	fileWriter.println();
                    	//Retrieve and write the time of execution for that array size for each algorithm
                    	fileWriter.println("Bubble Sort: "+timeStore.get(1).get(k)+" mS");
                    	fileWriter.println("Selection Sort: "+timeStore.get(2).get(k)+" mS");
                    	fileWriter.println("Merge Sort: "+timeStore.get(3).get(k)+" mS");
                    	fileWriter.println("Quick Sort: "+timeStore.get(4).get(k)+" mS");
                    	fileWriter.println("Insertion Sort: "+timeStore.get(5).get(k)+" mS");
                    	fileWriter.println("Cocktail Sort: "+timeStore.get(6).get(k++)+" mS");
                    	fileWriter.println();
                    	fileWriter.println("---------------------------------");
                    }
                    fileWriter.println();
                    fileWriter.print("--- End of Analysis Report ---");
                    //Close and release the file
                    fileWriter.close();
                    //Save Confirmation
                    JOptionPane.showMessageDialog(null,"The report has been successfully completed and saved!!!","Operation Completed",JOptionPane.INFORMATION_MESSAGE);
				}
				//Display this message in case the user cancel the operation
				else{
					JOptionPane.showMessageDialog(null,"Operation Aborted", "Cancelled", JOptionPane.CANCEL_OPTION);
				}
			}
			//Any errors occur, this message will be displayed to the user
			catch(Exception e){
				JOptionPane.showMessageDialog(null, "Error!!!, please try again later", "Error", JOptionPane.ERROR_MESSAGE);
			}	
		}
		if(aE.getSource().equals(importCSV)){
			try{
				String line="";
				//Add a filter to the file import window
				CSVImport.setFileFilter(filterCSVImport);
				int rSel=CSVImport.showOpenDialog(this);
				if(rSel==JFileChooser.APPROVE_OPTION){
					//Create an object containing the file name and path selected from the secondary storage
					File fileImported= CSVImport.getSelectedFile();
                    Path filePath=Paths.get(fileImported.getPath());
                    //Initialise the array timeStore
                    this.setUpTimeStore();
                    try(InputStream in=Files.newInputStream(filePath);
                    		BufferedReader bufReader= new BufferedReader(new InputStreamReader(in))){  
                    	int index=0;
                    	while((line= bufReader.readLine())!=null){
                    		//Read each line of the file and import the data into the timeStore ArrayList
                    		String[] values = line.split(",");
                    		for(int i=0; i<values.length; i++){
                    			timeStore.get(index).add(i, Long.valueOf(values[i]));
                    		}
                    		index++;
                        }
                    	//Setup the view as analysis view
                    	hasCSVBeenImported=true;
                    	this.analysisView();
                    	//Insert data into the view
                		this.fillAnalyseTF();
                		analysisClicked=true;
                		//Refresh the JFrame
                		this.repaint();
                    }
                    //In case an error occurs, the application will return to display the previous view followed by an error message
                    catch(Exception er){
                    	listenToChanges=true;
                    	this.notAnalysisView();
                    	JOptionPane.showMessageDialog(null,"There was been an internal error while trying loading the file!!!","Import Error",JOptionPane.ERROR_MESSAGE);
                    }
				}
				//Display this message in case the user cancel the operation
				else{
					this.notAnalysisView();
					JOptionPane.showMessageDialog(null,"Operation Aborted", "Cancelled", JOptionPane.CANCEL_OPTION);
				}
			}
			//Any errors occur, this message will be displayed to the user
			catch(Exception e){
				this.notAnalysisView();
				JOptionPane.showMessageDialog(null, "Error!!!, please try again later", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(aE.getSource().equals(startSort)){
			finished=false;
			if(hasDataBeenImported){
				//Ask the user if he wants to use the imported data or the random generator 
				if(JOptionPane.showConfirmDialog (null, "Would you like to sort the imported data?","Warning", JOptionPane.YES_NO_OPTION)==0){
					//Create an array data with 350 elements to display
					arrayData=tempData;
				}
				else{
					hasDataBeenImported=false;
					this.fillArray(350);
				}
			}
			else{
				this.fillArray(350);
			}
			//Define the application view
			this.buttonsStartProcess();
			arraySizeTF.setText(""+arrayData.size());
			//Initialise and start the background thread that will elaborate the data
			threadExecution=new AlgorithmExecution(algSel, arrayData, arrayResult, null);
			threadExecution.addPropertyChangeListener(this);
			timeStart=System.currentTimeMillis();
			threadExecution.execute();
		}
		if(aE.getSource().equals(stopSort)){
			finished=true;
			//Stop the background thread
			threadExecution.cancel(true);
			//Change the view
			this.buttonsStopProcess();
		}
		if(aE.getSource().equals(anBubbleATB)){
			//Display times of execution for the bubble sort algorithm 
			JOptionPane.showMessageDialog(null, "All the times for the Bubble Sort algorithm are:\n"+this.getSpecificAlgorithmTimes(1),"Bubble Sort", JOptionPane.INFORMATION_MESSAGE);
		}
		if(aE.getSource().equals(anSelectionATB)){
			//Display times of execution for the selection sort algorithm
			JOptionPane.showMessageDialog(null, "All the times for the Selection Sort algorithm are:\n"+this.getSpecificAlgorithmTimes(2), "Selection Sort", JOptionPane.INFORMATION_MESSAGE);
		}
		if(aE.getSource().equals(anMergeATB)){
			//Display times of execution for the merge sort algorithm
			JOptionPane.showMessageDialog(null, "All the times for the Merge Sort algorithm are:\n"+this.getSpecificAlgorithmTimes(3), "Merge Sort",JOptionPane.INFORMATION_MESSAGE);
		}
		if(aE.getSource().equals(anQuickATB)){
			//Display times of execution for the quick sort algorithm
			JOptionPane.showMessageDialog(null, "All the times for the Quick Sort algorithm are:\n"+this.getSpecificAlgorithmTimes(4), "Quick Sort", JOptionPane.INFORMATION_MESSAGE);
		}
		if(aE.getSource().equals(anInsertionATB)){
			//Display times of execution for the Insertion sort algorithm
			JOptionPane.showMessageDialog(null, "All the times for the Insertion Sort algorithm are:\n"+this.getSpecificAlgorithmTimes(5), "Insertion Sort", JOptionPane.INFORMATION_MESSAGE);
		}
		if(aE.getSource().equals(anCocktailATB)){
			//Display times of execution for the Cocktail sort algorithm
			JOptionPane.showMessageDialog(null, "All the times for the Cocktail Sort algorithm are:\n"+this.getSpecificAlgorithmTimes(6), "Cocktail Sort", JOptionPane.INFORMATION_MESSAGE);
		}
		if(aE.getSource().equals(reRunAnalysis)){
			//Make the progress bar visible while the background thread is running
			progressBar.setVisible(true);
			loopCounter=1;
			//Create temporary array list to use in the analysis
			ArrayList<Integer> dataTemp=new ArrayList<Integer>();
			ArrayList<Integer> resultTemp=new ArrayList<Integer>();
			//Initialise and run the background thread running all the sorting algorithms
			threadExecutionNoSleep=new AlgorithmExecution(dataTemp, resultTemp, timeStore, arrayStorage);
			threadExecutionNoSleep.addPropertyChangeListener(this);
			threadExecutionNoSleep.execute();
			//Set the analysis view
			this.analysisView();
			exportTXT.setEnabled(true);
		}
		if(aE.getSource().equals(importArrayData)){
			try{
				String line="";
				//This array list will keep the data temporarily until the application reads all the data from a file
				tempData = new ArrayList<Integer>();
				//Reminder for the user to not go over the 250 array elements
				JOptionPane.showMessageDialog(null,"Remember, 350 elements max!");
				dataImport.setFileFilter(filterCSVImport);
				int rSel=dataImport.showOpenDialog(this);
				if(rSel==JFileChooser.APPROVE_OPTION){
					//Create an object containing the file name and path selected from the secondary storage
					File fileImported= dataImport.getSelectedFile();
                    Path filePath=Paths.get(fileImported.getPath());
                    try(InputStream in=Files.newInputStream(filePath);
                    		//Create Buffer reader, able to read data from the file, line by line
                    		BufferedReader bufReader= new BufferedReader(new InputStreamReader(in))){  
                    	while((line= bufReader.readLine())!=null){
                    		//Import the data into the timeStore ArrayList
                    		String[] values = line.split(",");
                    		for(int i=0; i<values.length; i++){
                    			//The maximum length of the array data is set to 350 elements, all the other elements will be ignored
                    			if(i<350){
                    				tempData.add(i, Integer.valueOf(values[i]));
                    			}
                    		}
                        }
                    	//pass the reference of the temp data containing all the imported elements to the array data
                    	arrayData=tempData;
                    	hasDataBeenImported=true;
                    }
                    //In case an error occurs, the application will return to display the previous view followed by an error message
                    catch(Exception er){
                    	hasDataBeenImported=false;
                    	JOptionPane.showMessageDialog(null,"There was been an internal error while trying loading the file!!!","Import Error",JOptionPane.ERROR_MESSAGE);
                    }
				}
				//Display this message in case the user cancel the operation
				else{
					hasDataBeenImported=false;
					JOptionPane.showMessageDialog(null,"Operation Aborted", "Cancelled", JOptionPane.CANCEL_OPTION);
				}
			}
			//Any errors occur, this message will be displayed to the user
			catch(Exception e){
				hasDataBeenImported=false;
				JOptionPane.showMessageDialog(null, "Error!!!, please try again later", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		//Help the user to use the application and display the application properties
		if(aE.getSource().equals(help)){
			JOptionPane.showMessageDialog(null,"This application shows different ways to sort arrays of data. \n\n"
					+ "-->Select and algorithm and press Start Sorting to sort the array filled with random numbers\n\n"
					+ "-->To sort data pre-written from the user, press Import Array Data and press Start Sorting\n\n"
					+ "-->You are allowed to start a sorting algorithm and run the analysis simultaneously\n\n"
					+ "-->After have run an Analysis, you can export the execution times in a CSV file,\n"
					+ " or export the whole array data and times in a TXT file\n\n"
					+ "-->The application allows also to open a previous performed Analysis, just go on the File menu and\n"
					+ "click Import Analysis (.CSV)\n\n\n\n"
					+ "Author: Michael Piccoli\nVersion: 1.0\n"
					+ "Date: 6th February 2015\n\n"
					+ "View JavaDoc for further information about this application", "Help", JOptionPane.PLAIN_MESSAGE);
		}	
		//Close the application
		if(aE.getSource().equals(close)){
			System.exit(0);
		}
	}

	/** This method performs different operations after listening to background threads outputs
	 * 
	 * @param pcE			This object listens to background threads outputs and makes the application to perform operations depending on the input received
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pcE) {
		if(pcE.getPropertyName().equals("progress")){
			//This creates the graph for each sorting algorithm in case analysis has not been pressed
			if(listenToChanges){
				try{
					//When the background thread set its progress to 100, the application sets a specific view
					if(threadExecution.getProgress()==100 || threadExecution.isDone()){
						this.repaint();
						this.buttonsStopProcess();
						bubbleS.setEnabled(true);
						selectionS.setEnabled(true);
						mergeS.setEnabled(true);
						quickS.setEnabled(true);
						insertionS.setEnabled(true);
						cocktailS.setEnabled(true);
						finished=true;
						importArrayData.setEnabled(true);
					}
					else{
						//While the background thread is running and sending updates of its progress, this will return the time of execution and the number of times the algorithm access the array
						arrayVisitTF.setText(""+threadExecution.getAlgorithmVisited());
						timeEnd=System.currentTimeMillis();
						long time = timeEnd-timeStart;
						//Print out the time in minutes, seconds and milliseconds
						timerTF.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time),TimeUnit.MILLISECONDS.toSeconds(time)%60, (time%100)));
						switch(algSel){
						case 'b':
							bubbleS.setEnabled(true);
							selectionS.setEnabled(false);
							mergeS.setEnabled(false);
							quickS.setEnabled(false);
							insertionS.setEnabled(false);
							cocktailS.setEnabled(false);
							break;
						case 's':
							bubbleS.setEnabled(false);
							selectionS.setEnabled(true);
							mergeS.setEnabled(false);
							quickS.setEnabled(false);
							insertionS.setEnabled(false);
							cocktailS.setEnabled(false);
							break;
						case 'm':
							bubbleS.setEnabled(false);
							selectionS.setEnabled(false);
							mergeS.setEnabled(true);
							quickS.setEnabled(false);
							insertionS.setEnabled(false);
							cocktailS.setEnabled(false);
							break;
						case 'q':
							bubbleS.setEnabled(false);
							selectionS.setEnabled(false);
							mergeS.setEnabled(false);
							quickS.setEnabled(true);
							insertionS.setEnabled(false);
							cocktailS.setEnabled(false);
							break;
						case 'i':
							bubbleS.setEnabled(false);
							selectionS.setEnabled(false);
							mergeS.setEnabled(false);
							quickS.setEnabled(false);
							insertionS.setEnabled(true);
							cocktailS.setEnabled(false);
							break;
						case 'c':
							bubbleS.setEnabled(false);
							selectionS.setEnabled(false);
							mergeS.setEnabled(false);
							quickS.setEnabled(false);
							insertionS.setEnabled(false);
							cocktailS.setEnabled(true);
							break;
						}
						//Block the user to import array data while the application is sorting
						importArrayData.setEnabled(false);
						//Refresh the screen
						this.repaint();
						//Check whether the process has finished its operations and allow the user to import a CSV file
						if(threadExecutionNoSleep.isDone()){
							importCSV.setEnabled(true);
						} 
					}
				}catch(Exception e){}
			}
			//This creates the graph for the comparison between algorithms
			else{
				try{
					//When the thread has finished its execution, change view and elaborate the data
					if(threadExecutionNoSleep.isDone()){
						listenToChanges=false;
						this.repaint();
						//Hide the progress bar
						progressBar.setVisible(false);
						//Display data analysed
						this.fillAnalyseTF();
						anBubbleATB.setEnabled(true);
						anSelectionATB.setEnabled(true);
						anMergeATB.setEnabled(true);
						anQuickATB.setEnabled(true);
						anInsertionATB.setEnabled(true);
						anCocktailATB.setEnabled(true);
						reRunAnalysis.setEnabled(true);
						exportTXT.setEnabled(true);
						exportCSV.setEnabled(true);
						importCSV.setEnabled(true);
					}
					else{
						//Loop for the progress bar. when it reaches the value 100, restart the progress bar to the value 0
						if(loopCounter==100){ 
							loopCounter=0;
						}
						//Change the value of the progress bar
						progressBar.setValue(loopCounter++);
						progressBar.setVisible(true);
						anBubbleATB.setEnabled(false);
						anSelectionATB.setEnabled(false);
						anMergeATB.setEnabled(false);
						anQuickATB.setEnabled(false);
						anInsertionATB.setEnabled(false);
						anCocktailATB.setEnabled(false);
						importCSV.setEnabled(false);
						exportCSV.setEnabled(false);
						exportTXT.setEnabled(false);
						reRunAnalysis.setEnabled(false);
						//Refresh the screen
						this.repaint();
					}
				}catch(Exception e){}
			}
		}
	}
	
	/** This method inserts random generated numbers inside the array data
	 * 
	 * @param nElement			This defines the size of the array data
	 */
	private void fillArray(int nElement){
		//Clear the content inside the sub-arrays of the array
		arrayData.clear();
		for(int i=0; i<nElement; i++){
			arrayData.add(i, (int)(Math.random()*100));
		}
	}
	
	/** This method loops around all the arrays inside the array to find the biggest value inserted
	 * 
	 * @param al			This is the array of arrays the method needs to get the values from
	 * @return				Return the biggest value contained in the array
	 */
	private int findMaxHeight(ArrayList<ArrayList<Long>> al){
		int max=0;
		for(int i=1; i<al.size(); i++){
			for(int j=0; j<al.get(i).size(); j++){
				if(al.get(i).get(j)>max){
					max=al.get(i).get(j).intValue();
				}
			}
		}
		return max;
	}
	
	/** This method clears and initialises the timeStore array containing all the execution times for each algorithm
	 * 
	 */
	private void setUpTimeStore(){
		timeStore.clear();
		for(int i=0; i<=6; i++){
			timeStore.add(i, new ArrayList<Long>());
		}
	}
	
	/** This method initialises the arrayStorage that will contain all the array data the application will create
	 * 
	 */
	private void setUpArrayStorage(){
		//Two ArrayLists for each algorithms containing both the raw and sorted data for each array size.
		for(int i=0; i<12; i++){
			arrayStorage.add(i, new ArrayList<Integer>());			
		}
	}
	
	/** This method finds all the execution times for each array size of a specific algorithm
	 * 
	 * @param index			This corresponds to a position in the array timeStore where an algorithm has been assigned to save its data
	 * @return				Return a string containing all the times formatted and readable by a final user
	 */
	private String getSpecificAlgorithmTimes(int index){
		String s="";
		int i=0;
		//Only valid if the index is within this range
		if(index>0 && index<7){
			for(Long time: timeStore.get(index)){
				s+="Array Size: "+timeStore.get(0).get(i)+" --->  ";
				//Find seconds, milliseconds and nanoseconds
				s+=String.format("%02dS :%02dmS",time/1000, time%1000 )+"\n";
				//s+=String.format("%02dS :%02dmS :%02dnS", TimeUnit.MILLISECONDS.toMinutes(time),TimeUnit.MILLISECONDS.toSeconds(time)%60, (time%100))+"\n";
				i++;
			}
		}
		return s;
	}
	
	/** This method finds the longest execution time for a specific algorithm passed as index
	 * 
	 * @param index			This corresponds to a position in the array timeStore where the algorithm has been assigned to save its data
	 * @return				Return the longest execution time contained in the array
	 */
	private long getLongestTimeAlgorithm(int index){
		long value=0;
		if(index>0 && index<7){
			ArrayList<Long> temp= timeStore.get(index);
			for(int i=0; i<temp.size(); i++){
				if(temp.get(i)>value){
					value=temp.get(i);
				}
			}
		}
		return value;
	}
	
	/** This method finds the shortest execution time for a specific algorithm passed as index
	 * 
	 * @param index			This corresponds to a position in the array timeStore where the algorithm has been assigned to save its data
	 * @return				Return the shortest execution time contained in the array
	 */
	private long getShortestTimeAlgorithm(int index){
		long value=100000;
		if(index>0 && index<7){
			ArrayList<Long> temp= timeStore.get(index);
			for(int i=0; i<temp.size(); i++){
				if(temp.get(i)<value){
					value=temp.get(i);
				}
			}
		}
		return value;
	}
	
	/** This method calculates the average execution time for a specific algorithm passed as index
	 * 
	 * @param index			This corresponds to a position in the array timeStore where the algorithm has been assigned to save its data
	 * @return				Return the average time of execution for the selected algorithm
	 */
	private long getAvgTimesAlgorithm(int index){
		long avg=0;
		if(index>0 && index<7){
			ArrayList<Long> temp= timeStore.get(index);
			for(int i=0; i<temp.size(); i++){
				avg+=temp.get(i);
			}
			avg=avg/temp.size();
		}
		return avg;
	}
	
	/** This method changes the view on the JFrame, enabling and disabling graphical objects such as JButtons, JLabels, JTextFields and so on 
	 * 
	 */
	private void notAnalysisView(){
		//Change the frame size
		frame.setSize(800,600);
		startSort.setVisible(true);
		stopSort.setVisible(true);
		timerL.setVisible(true);
		arraySizeL.setVisible(true);
		arrayVisitL.setVisible(true);
		algNameL.setVisible(true);
		algDescriptionTF.setVisible(true);
		timerTF.setVisible(true);
		arraySizeTF.setVisible(true);
		arrayVisitTF.setVisible(true);
		anAlgL.setVisible(false);
		anBubbleL.setVisible(false);
		anSelectionL.setVisible(false);
		anMergeL.setVisible(false);
		anQuickL.setVisible(false);
		anInsertionL.setVisible(false);
		anCocktailL.setVisible(false);
		anShortTimeL.setVisible(false);
		anLongTimeL.setVisible(false);
		anAvgTimeL.setVisible(false);
		shortTBubbleTF.setVisible(false);
		shortTSelectionTF.setVisible(false);
		shortTMergeTF.setVisible(false);
		shortTQuickTF.setVisible(false);
		shortTInsertionTF.setVisible(false);
		shortTCocktailTF.setVisible(false);
		longTBubbleTF.setVisible(false);
		longTSelectionTF.setVisible(false);
		longTMergeTF.setVisible(false);
		longTQuickTF.setVisible(false);
		longTInsertionTF.setVisible(false);
		longTCocktailTF.setVisible(false);
		avgTBubbleTF.setVisible(false);
		avgTSelectionTF.setVisible(false);
		avgTMergeTF.setVisible(false);
		avgTQuickTF.setVisible(false);
		avgTInsertionTF.setVisible(false);
		avgTCocktailTF.setVisible(false);
		anAllTimes.setVisible(false);
		anBubbleATB.setVisible(false);
		anSelectionATB.setVisible(false);
		anMergeATB.setVisible(false);
		anQuickATB.setVisible(false);
		anInsertionATB.setVisible(true);
		anCocktailATB.setVisible(true);
		anBubbleColour.setVisible(false);
		anSelectionColour.setVisible(false);
		anMergeColour.setVisible(false);
		anQuickColour.setVisible(false);
		anInsertionColour.setVisible(false);
		anCocktailColour.setVisible(false);
		//Change to algorithm graph
		listenToChanges=true;
		progressBar.setVisible(false);
		exportTXT.setEnabled(false);
		exportCSV.setEnabled(false);
		reRunAnalysis.setVisible(false);
		importArrayData.setVisible(true);
		try{
			if(threadExecution.isDone()){
				this.buttonsStopProcess();
			}
		}catch(Exception e){}
	}
	
	/** This method changes the view on the JFrame, enabling and disabling graphical objects such as JButtons, JLabels, JTextFields and so on. 
	 * It also allows the view of the analysis graph
	 * 
	 */
	private void analysisView(){
		//Change the frame size
		frame.setSize(1000,600);
		startSort.setVisible(false);
		stopSort.setVisible(false);
		if(!finished){
			switch(algSel){
			case 'b':
				bubbleS.setEnabled(true);
				selectionS.setEnabled(false);
				mergeS.setEnabled(false);
				quickS.setEnabled(false);
				insertionS.setEnabled(false);
				cocktailS.setEnabled(false);
				break;
			case 's':
				bubbleS.setEnabled(false);
				selectionS.setEnabled(true);
				mergeS.setEnabled(false);
				quickS.setEnabled(false);
				insertionS.setEnabled(false);
				cocktailS.setEnabled(false);
				break;
			case 'm':
				bubbleS.setEnabled(false);
				selectionS.setEnabled(false);
				mergeS.setEnabled(true);
				quickS.setEnabled(false);
				insertionS.setEnabled(false);
				cocktailS.setEnabled(false);
				break;
			case 'q':
				bubbleS.setEnabled(false);
				selectionS.setEnabled(false);
				mergeS.setEnabled(false);
				quickS.setEnabled(true);
				insertionS.setEnabled(false);
				cocktailS.setEnabled(false);
				break;
			case 'i':
				bubbleS.setEnabled(false);
				selectionS.setEnabled(false);
				mergeS.setEnabled(false);
				quickS.setEnabled(false);
				insertionS.setEnabled(true);
				cocktailS.setEnabled(false);
				break;
			case 'c':
				bubbleS.setEnabled(false);
				selectionS.setEnabled(false);
				mergeS.setEnabled(false);
				quickS.setEnabled(false);
				insertionS.setEnabled(false);
				cocktailS.setEnabled(true);
				break;
			}
		}
		else{
			bubbleS.setEnabled(true);
			selectionS.setEnabled(true);
			mergeS.setEnabled(true);
			quickS.setEnabled(true);
			insertionS.setEnabled(true);
			cocktailS.setEnabled(true);
		}
		timerL.setVisible(false);
		arraySizeL.setVisible(false);
		arrayVisitL.setVisible(false);
		algNameL.setVisible(false);
		algDescriptionTF.setVisible(false);
		timerTF.setVisible(false);
		arraySizeTF.setVisible(false);
		arrayVisitTF.setVisible(false);
		anAlgL.setVisible(true);
		anBubbleL.setVisible(true);
		anSelectionL.setVisible(true);
		anMergeL.setVisible(true);
		anQuickL.setVisible(true);
		anInsertionL.setVisible(true);
		anCocktailL.setVisible(true);
		anShortTimeL.setVisible(true);
		anLongTimeL.setVisible(true);
		anAvgTimeL.setVisible(true);
		shortTBubbleTF.setVisible(true);
		shortTSelectionTF.setVisible(true);
		shortTMergeTF.setVisible(true);
		shortTQuickTF.setVisible(true);
		shortTInsertionTF.setVisible(true);
		shortTCocktailTF.setVisible(true);
		longTBubbleTF.setVisible(true);
		longTSelectionTF.setVisible(true);
		longTMergeTF.setVisible(true);
		longTQuickTF.setVisible(true);
		longTInsertionTF.setVisible(true);
		longTCocktailTF.setVisible(true);
		avgTBubbleTF.setVisible(true);
		avgTSelectionTF.setVisible(true);
		avgTMergeTF.setVisible(true);
		avgTQuickTF.setVisible(true);
		avgTInsertionTF.setVisible(true);
		avgTCocktailTF.setVisible(true);
		anAllTimes.setVisible(true);
		anBubbleATB.setVisible(true);
		anSelectionATB.setVisible(true);
		anMergeATB.setVisible(true);
		anQuickATB.setVisible(true);
		anInsertionATB.setVisible(true);
		anCocktailATB.setVisible(true);
		anBubbleColour.setVisible(true);
		anSelectionColour.setVisible(true);
		anMergeColour.setVisible(true);
		anQuickColour.setVisible(true);
		anInsertionColour.setVisible(true);
		anCocktailColour.setVisible(true);
		reRunAnalysis.setVisible(true);
		//Change to analysis graph
		listenToChanges=false;
		importArrayData.setVisible(false);
		exportCSV.setEnabled(true);
		if(!hasCSVBeenImported){
			exportTXT.setEnabled(true);
		}
		try{
			if(threadExecution.isDone()){
				this.buttonsStopProcess();
			}
		}catch(Exception e){}
	}
	
	/** This method changes the view on the JFrame, enabling and disabling graphical objects such as JButtons, JLabels, JTextFields and so on when the button Start is pressed
	 * 
	 */
	private void buttonsStartProcess(){
		startSort.setEnabled(false);
		stopSort.setEnabled(true);
		listenToChanges=true;
		switch(algSel){
		case 'b':
			bubbleS.setEnabled(true);
			selectionS.setEnabled(false);
			mergeS.setEnabled(false);
			quickS.setEnabled(false);
			insertionS.setEnabled(false);
			cocktailS.setEnabled(false);
			break;
		case 's':
			bubbleS.setEnabled(false);
			selectionS.setEnabled(true);
			mergeS.setEnabled(false);
			quickS.setEnabled(false);
			insertionS.setEnabled(false);
			cocktailS.setEnabled(false);
			break;
		case 'm':
			bubbleS.setEnabled(false);
			selectionS.setEnabled(false);
			mergeS.setEnabled(true);
			insertionS.setEnabled(false);
			cocktailS.setEnabled(false);
			break;
		case 'q':
			bubbleS.setEnabled(false);
			selectionS.setEnabled(false);
			mergeS.setEnabled(false);
			quickS.setEnabled(true);
			insertionS.setEnabled(false);
			cocktailS.setEnabled(false);
			break;
		case 'i':
			bubbleS.setEnabled(false);
			selectionS.setEnabled(false);
			mergeS.setEnabled(false);
			quickS.setEnabled(false);
			insertionS.setEnabled(true);
			cocktailS.setEnabled(false);
			break;
		case 'c':
			bubbleS.setEnabled(false);
			selectionS.setEnabled(false);
			mergeS.setEnabled(false);
			quickS.setEnabled(false);
			insertionS.setEnabled(false);
			cocktailS.setEnabled(true);
			break;
		}
	}
	
	/** This method enables  the buttons fixed at the tool bar to be clicked when a algorithm thread has finished processing data
	 * 
	 */
	private void buttonsStopProcess(){
		startSort.setEnabled(true);
		stopSort.setEnabled(false);
		bubbleS.setEnabled(true);
		selectionS.setEnabled(true);
		mergeS.setEnabled(true);
		quickS.setEnabled(true);
		insertionS.setEnabled(true);
		cocktailS.setEnabled(true);
	}
	
	/** This method calculates and inserts data from the timeStore array to the JTextFields in the analysis window 
	 * 
	 */
	private void fillAnalyseTF(){
		shortTBubbleTF.setText(""+this.getShortestTimeAlgorithm(1)+" mS");
		shortTSelectionTF.setText(""+this.getShortestTimeAlgorithm(2)+" mS");
		shortTMergeTF.setText(""+this.getShortestTimeAlgorithm(3)+" mS");
		shortTQuickTF.setText(""+this.getShortestTimeAlgorithm(4)+" mS");
		shortTInsertionTF.setText(""+this.getShortestTimeAlgorithm(5)+" mS");
		shortTCocktailTF.setText(""+this.getShortestTimeAlgorithm(6)+" mS");
		longTBubbleTF.setText(""+this.getLongestTimeAlgorithm(1)+" mS");
		longTSelectionTF.setText(""+this.getLongestTimeAlgorithm(2)+" mS");
		longTMergeTF.setText(""+this.getLongestTimeAlgorithm(3)+" mS");
		longTQuickTF.setText(""+this.getLongestTimeAlgorithm(4)+" mS");
		longTInsertionTF.setText(""+this.getLongestTimeAlgorithm(5)+" mS");
		longTCocktailTF.setText(""+this.getLongestTimeAlgorithm(6)+" mS");
		avgTBubbleTF.setText(""+this.getAvgTimesAlgorithm(1)+" mS");
		avgTSelectionTF.setText(""+this.getAvgTimesAlgorithm(2)+" mS");
		avgTMergeTF.setText(""+this.getAvgTimesAlgorithm(3)+" mS");
		avgTQuickTF.setText(""+this.getAvgTimesAlgorithm(4)+" mS");
		avgTInsertionTF.setText(""+this.getAvgTimesAlgorithm(5)+" mS");
		avgTCocktailTF.setText(""+this.getAvgTimesAlgorithm(6)+" mS");
	}
}
