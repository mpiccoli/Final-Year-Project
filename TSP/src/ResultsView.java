import java.awt.Font;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see JPanel, JFRame, JLabel, JTextField, JTextArea, TradResultData, GenResultData
 * 
 * This Class gather the information data from TradResultData and GenResultData object to display it in a user friendly manner
 * 
 */
public class ResultsView extends JPanel {

	// Define constants for this class
	private static final long serialVersionUID = 8268951247350658510L;
	private static final Font BOLD_SEGOE_12 = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font ITALIC_SEGOE_12 = new Font("Segoe UI", Font.ITALIC, 12);
	private static final Font PLAIN_SEGOE_12 = new Font("Segoe UI", Font.PLAIN, 12);
	// Graphical Elements
	private JFrame mainFrame;
	private JPanel mainGeDetailsPanel, geDataProducedPanel, mainTradDetailsPanel, traDetailsPanel;
	private JLabel nCitiesLabel, crossMetLabel, crossProbLabel, mutMetLabel, mutProbLabel, tourLabel, generLabel,
			fitnessLabel, timeLabel, popSizeLabel, maxGenLabel, tradNameLabel, bestResultAtLabel;
	private JTextField nCitiesTF, crossMetTF, crossProbTF, mutMetTF, mutProbTF, tourTF, generTF, fitnessTF, timeTF,
			popSizeTF, maxGenTF, tradNameTF, bestResultAtTF;
	private JTextArea traDetailsTA;
	private TradResultData tradObj;
	private GenResultData genObj;
	private String traGen;

	/*
	 * @param windoeTitle Define the title of the window
	 * 
	 * @param traGe Define whether the object passed is a TradResultData or
	 * GenResultData instance
	 * 
	 * @param obj The actual data object that this class has to extract
	 * information from
	 */
	public ResultsView(String windowTitle, String traGe, Object obj) {
		// Create the window
		mainFrame = new JFrame();
		mainFrame.setTitle(windowTitle);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		mainFrame.setLayout(null);
		// Initiate shared graphical objects
		nCitiesLabel = new JLabel("N. Cities");
		nCitiesLabel.setFont(BOLD_SEGOE_12);
		nCitiesTF = new JTextField();
		nCitiesTF.setFont(ITALIC_SEGOE_12);
		nCitiesTF.setEditable(false);
		tourLabel = new JLabel("Tour Length");
		tourLabel.setFont(BOLD_SEGOE_12);
		tourTF = new JTextField();
		tourTF.setFont(ITALIC_SEGOE_12);
		tourTF.setEditable(false);
		timeLabel = new JLabel("Time");
		timeLabel.setFont(BOLD_SEGOE_12);
		timeTF = new JTextField();
		timeTF.setFont(ITALIC_SEGOE_12);
		timeTF.setEditable(false);
		// Adapt the view for a tradition algorithm
		this.traGenView(traGe, obj);
		// Add data to the Text Fields
		this.setTextBoxes();
	}

	/*
	 * @param s This says whether the object is a TradResultData or
	 * GenResultData instance
	 * 
	 * @param objPassed The actual data object
	 */
	private void traGenView(String s, Object objPassed) {
		// In case the string contains "tra" which stands for "traditional", set
		// the view this way
		if (s.equals("tra")) {
			mainFrame.setSize(450, 250);
			traGen = "tra";
			// Convert the object to a TradResultData instance
			tradObj = (TradResultData) objPassed;
			// Initiate the graphic objects to show to the user
			tradNameLabel = new JLabel("Name");
			tradNameLabel.setFont(BOLD_SEGOE_12);
			tradNameTF = new JTextField();
			mainTradDetailsPanel = new JPanel();
			mainTradDetailsPanel.setBorder(new TitledBorder("Method Details"));
			mainTradDetailsPanel.setFont(PLAIN_SEGOE_12);
			mainTradDetailsPanel.setLayout(null);
			traDetailsPanel = new JPanel();
			traDetailsPanel.setBorder(new TitledBorder("Details"));
			traDetailsPanel.setLayout(null);
			traDetailsTA = new JTextArea();
			// The area is not editable by the user and its background is
			// translucent
			traDetailsTA.setOpaque(false);
			traDetailsTA.setEditable(false);
			// Define the object locations on the screen
			mainTradDetailsPanel.setBounds(10, 5, 430, 100);
			nCitiesLabel.setBounds(20, 30, 60, 20);
			nCitiesTF.setBounds(80, 30, 65, 20);
			tourLabel.setBounds(160, 30, 80, 20);
			tourTF.setBounds(250, 30, 140, 20);
			timeLabel.setBounds(20, 65, 40, 20);
			timeTF.setBounds(65, 65, 80, 20);
			tradNameLabel.setBounds(170, 65, 45, 20);
			tradNameTF.setBounds(215, 65, 150, 20);
			traDetailsPanel.setBounds(10, 110, 430, 100);
			traDetailsTA.setBounds(7, 20, 416, 75);
			// Add the graphic objects to the current panel
			mainFrame.add(mainTradDetailsPanel);
			mainTradDetailsPanel.add(nCitiesLabel);
			mainTradDetailsPanel.add(nCitiesTF);
			mainTradDetailsPanel.add(tourLabel);
			mainTradDetailsPanel.add(tourTF);
			mainTradDetailsPanel.add(timeLabel);
			mainTradDetailsPanel.add(timeTF);
			mainTradDetailsPanel.add(tradNameLabel);
			mainTradDetailsPanel.add(tradNameTF);
			mainFrame.add(traDetailsPanel);
			traDetailsPanel.add(traDetailsTA);
		}
		// Adapt the view for a genetic algorithm
		else {
			mainFrame.setSize(535, 250);
			traGen = "gen";
			// Convert the object to a GenResultData instance
			genObj = (GenResultData) objPassed;
			// Initiate the graphic objects to show to the user
			mainGeDetailsPanel = new JPanel();
			mainGeDetailsPanel.setBorder(new TitledBorder("Method Details"));
			mainGeDetailsPanel.setLayout(null);
			geDataProducedPanel = new JPanel();
			geDataProducedPanel.setBorder(new TitledBorder("Data Produced"));
			geDataProducedPanel.setLayout(null);
			crossMetLabel = new JLabel("Crossover Method");
			crossMetLabel.setFont(BOLD_SEGOE_12);
			crossMetTF = new JTextField();
			crossMetTF.setFont(ITALIC_SEGOE_12);
			crossMetTF.setEditable(false);
			crossProbLabel = new JLabel("Probability");
			crossProbLabel.setFont(BOLD_SEGOE_12);
			crossProbTF = new JTextField();
			crossProbTF.setFont(ITALIC_SEGOE_12);
			crossProbTF.setEditable(false);
			mutMetLabel = new JLabel("Metation Method");
			mutMetLabel.setFont(BOLD_SEGOE_12);
			mutMetTF = new JTextField();
			mutMetTF.setFont(ITALIC_SEGOE_12);
			mutMetTF.setEditable(false);
			mutProbLabel = new JLabel("Probability");
			mutProbLabel.setFont(BOLD_SEGOE_12);
			mutProbTF = new JTextField();
			mutProbTF.setFont(ITALIC_SEGOE_12);
			mutProbTF.setEditable(false);
			generLabel = new JLabel("Generations");
			generLabel.setFont(BOLD_SEGOE_12);
			generTF = new JTextField();
			generTF.setFont(ITALIC_SEGOE_12);
			generTF.setEditable(false);
			fitnessLabel = new JLabel("Fitness");
			fitnessLabel.setFont(BOLD_SEGOE_12);
			fitnessTF = new JTextField();
			fitnessTF.setFont(ITALIC_SEGOE_12);
			fitnessTF.setEditable(false);
			popSizeLabel = new JLabel("Pop. Size");
			popSizeLabel.setFont(BOLD_SEGOE_12);
			popSizeTF = new JTextField();
			popSizeTF.setFont(ITALIC_SEGOE_12);
			popSizeTF.setEditable(false);
			maxGenLabel = new JLabel("Max Gen");
			maxGenLabel.setFont(BOLD_SEGOE_12);
			maxGenTF = new JTextField();
			maxGenTF.setFont(ITALIC_SEGOE_12);
			maxGenTF.setEditable(false);
			bestResultAtLabel = new JLabel("Best Path at Gen");
			bestResultAtLabel.setFont(BOLD_SEGOE_12);
			bestResultAtTF = new JTextField();
			bestResultAtTF.setFont(ITALIC_SEGOE_12);
			bestResultAtTF.setEditable(false);
			// Set the object locations
			mainGeDetailsPanel.setBounds(10, 5, 515, 110);
			nCitiesLabel.setBounds(7, 20, 60, 20);
			nCitiesTF.setBounds(70, 20, 70, 20);
			popSizeLabel.setBounds(160, 20, 60, 20);
			popSizeTF.setBounds(220, 20, 85, 20);
			maxGenLabel.setBounds(360, 20, 60, 20);
			maxGenTF.setBounds(420, 20, 80, 20);
			crossMetLabel.setBounds(7, 45, 115, 20);
			crossMetTF.setBounds(125, 45, 180, 20);
			crossProbLabel.setBounds(320, 45, 70, 20);
			crossProbTF.setBounds(390, 45, 60, 20);
			mutMetLabel.setBounds(7, 75, 115, 20);
			mutMetTF.setBounds(125, 75, 180, 20);
			mutProbLabel.setBounds(320, 75, 70, 20);
			mutProbTF.setBounds(390, 75, 60, 20);
			geDataProducedPanel.setBounds(10, 120, 430, 90);
			generLabel.setBounds(10, 25, 80, 20);
			generTF.setBounds(90, 25, 90, 20);
			fitnessLabel.setBounds(200, 25, 60, 20);
			fitnessTF.setBounds(260, 25, 140, 20);
			bestResultAtLabel.setBounds(10, 55, 110, 20);
			bestResultAtTF.setBounds(120, 55, 60, 20);
			timeLabel.setBounds(200, 55, 50, 20);
			timeTF.setBounds(250, 55, 80, 20);
			// Add the graphic elements to the current panels
			mainFrame.add(mainGeDetailsPanel);
			mainGeDetailsPanel.add(nCitiesLabel);
			mainGeDetailsPanel.add(nCitiesTF);
			mainGeDetailsPanel.add(popSizeLabel);
			mainGeDetailsPanel.add(popSizeTF);
			mainGeDetailsPanel.add(maxGenLabel);
			mainGeDetailsPanel.add(maxGenTF);
			mainGeDetailsPanel.add(crossMetLabel);
			mainGeDetailsPanel.add(crossMetTF);
			mainGeDetailsPanel.add(crossProbLabel);
			mainGeDetailsPanel.add(crossProbTF);
			mainGeDetailsPanel.add(mutMetLabel);
			mainGeDetailsPanel.add(mutMetTF);
			mainGeDetailsPanel.add(mutProbLabel);
			mainGeDetailsPanel.add(mutProbTF);
			mainFrame.add(geDataProducedPanel);
			geDataProducedPanel.add(generLabel);
			geDataProducedPanel.add(generTF);
			geDataProducedPanel.add(fitnessLabel);
			geDataProducedPanel.add(fitnessTF);
			geDataProducedPanel.add(bestResultAtLabel);
			geDataProducedPanel.add(bestResultAtTF);
			geDataProducedPanel.add(timeLabel);
			geDataProducedPanel.add(timeTF);
		}
	}

	/*
	 * This methods sets the value to the graphic objects, retrieving data from
	 * the object previously passed
	 */
	private void setTextBoxes() {
		// Set the traditional element values on screen
		if (traGen.equals("tra")) {
			nCitiesTF.setText("" + tradObj.getNumCities());
			double tourLengthValue = tradObj.getTourLength();
			// Some times the Tour length is too long to be displayed, in this
			// way only 8 decimal places are kept to keep the application
			// consistent
			DecimalFormat fv = new DecimalFormat("#.########");
			tourTF.setText("" + fv.format(tourLengthValue));
			long tempTime = tradObj.getExecutionTime();
			String timeString = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(tempTime),
					TimeUnit.MILLISECONDS.toSeconds(tempTime) % 60, (tempTime % 100));
			timeTF.setText("" + timeString);
			tradNameTF.setText(tradObj.getAlgName());
			traDetailsTA.setText(tradObj.getDetails());
		}
		// Set the genetic element values on screen
		else {
			nCitiesTF.setText("" + genObj.getnumCities());
			popSizeTF.setText("" + genObj.getPopSize());
			maxGenTF.setText("" + genObj.getMaxGen());
			crossMetTF.setText("" + genObj.getCrossoverMethod());
			crossProbTF.setText("" + genObj.getCrossoverProbability() + "%");
			mutMetTF.setText("" + genObj.getMutationMethod());
			mutProbTF.setText("" + genObj.getMutationProbability() + "%");
			generTF.setText("" + genObj.getGenerationCount());
			double fitnessValue = genObj.getFitness();
			// Some times the fitness value is too long to be displayed, in this
			// way only 8 decimal places are kept to keep the application
			// consistant
			DecimalFormat fv = new DecimalFormat("#.########");
			fitnessTF.setText("" + fv.format(fitnessValue));
			bestResultAtTF.setText("" + genObj.getIndexBestResultAtGeneration());
			long tempTime = genObj.getExecutionTime();
			String timeString = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(tempTime),
					TimeUnit.MILLISECONDS.toSeconds(tempTime) % 60, (tempTime % 100));
			timeTF.setText(timeString);
		}
	}
}
