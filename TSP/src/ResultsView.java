import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ResultsView extends JPanel{
	
	//Graphical Elements
	private JFrame mainFrame;
	private JPanel mainGeDetailsPanel, geDataProducedPanel, mainTradDetailsPanel, traDetailsPanel;
	private JLabel nCitiesLabel, crossMetLabel, crossProbLabel, mutMetLabel, mutProbLabel, tourLabel, generLabel, fitnessLabel, timeLabel, popSizeLabel,fromToLabel,maxGenLabel;
	private JTextField nCitiesTF, crossMetTF, crossProbTF, mutMetTF, mutProbTF, tourTF, generTF, fitnessTF, timeTF, popSizeTF, fromToTF, maxGenTF;
	private JTextArea traDetailsTA;
	private TradResultData tradObj;
	private GenResultData genObj;
	private String traGen;
	
	public ResultsView(String windowTitle, String traGe, Object obj){
		//Create the window
		mainFrame=new JFrame();
		mainFrame.setTitle(windowTitle);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		mainFrame.setLayout(null);
		//Initiate shared graphical objects
		nCitiesLabel=new JLabel("N. Cities");
		nCitiesTF=new JTextField();
		nCitiesTF.setEditable(false);
		tourLabel=new JLabel("Tour Length");
		tourTF=new JTextField();
		tourTF.setEditable(false);
		timeLabel=new JLabel("Time");
		timeTF=new JTextField();
		timeTF.setEditable(false);
		//Adapt the view for a tradition algorithm
		this.traGenView(traGe, obj);
		//Add data to the Text Fields
		this.setTextBoxes();
	}
	
	private void traGenView(String s, Object objPassed){
		if(s.equals("tra")){
			mainFrame.setSize(450, 250);
			traGen="tra";
			tradObj=(TradResultData)objPassed;
			mainTradDetailsPanel=new JPanel();
				//mainTradDetailsPanel.setBorder(new TitledBorder("Method "+tradObj.getAlgName()+" Details"));
				mainTradDetailsPanel.setBorder(new TitledBorder("Method Details"));
				mainTradDetailsPanel.setLayout(null);
			traDetailsPanel=new JPanel();
				traDetailsPanel.setBorder(new TitledBorder("Details"));
				traDetailsPanel.setLayout(null);
			traDetailsTA=new JTextArea();
				//The area is not editable by the user and the its background is translucent
				traDetailsTA.setOpaque(false);
				traDetailsTA.setEditable(false);
			
			mainTradDetailsPanel.setBounds(10,5,430,100);
			nCitiesLabel.setBounds(20,30,50,20);
			nCitiesTF.setBounds(70,30,60,20);
			tourLabel.setBounds(240,30,80,20);
			tourTF.setBounds(320,30,80,20);
			timeLabel.setBounds(135,70,40,20);
			timeTF.setBounds(175,70,80,20);
			traDetailsPanel.setBounds(10,110,430,100);
			traDetailsTA.setBounds(7,20,416,75);
			
			mainFrame.add(mainTradDetailsPanel);
			mainTradDetailsPanel.add(nCitiesLabel);
			mainTradDetailsPanel.add(nCitiesTF);
			mainTradDetailsPanel.add(tourLabel);
			mainTradDetailsPanel.add(tourTF);
			mainTradDetailsPanel.add(timeLabel);
			mainTradDetailsPanel.add(timeTF);
			mainFrame.add(traDetailsPanel);
			traDetailsPanel.add(traDetailsTA);
		}
		//Adapt the view for a genetic algorithm
		else{
			mainFrame.setSize(535, 250);
			traGen="gen";
			genObj=(GenResultData)objPassed;
			mainGeDetailsPanel=new JPanel();
				mainGeDetailsPanel.setBorder(new TitledBorder("Method Details"));
				mainGeDetailsPanel.setLayout(null);
			geDataProducedPanel=new JPanel();
				geDataProducedPanel.setBorder(new TitledBorder("Data Produced"));
				geDataProducedPanel.setLayout(null);
			crossMetLabel=new JLabel("Crossover Method");
			crossMetTF=new JTextField();
			crossMetTF.setEditable(false);
			crossProbLabel=new JLabel("Probability");
			crossProbTF=new JTextField();
			crossProbTF.setEditable(false);
			mutMetLabel=new JLabel("Metation Method");
			mutMetTF=new JTextField();
			mutMetTF.setEditable(false);
			mutProbLabel=new JLabel("Probability");
			mutProbTF=new JTextField();
			mutProbTF.setEditable(false);
			generLabel=new JLabel("Generation");
			generTF=new JTextField();
			generTF.setEditable(false);
			fitnessLabel=new JLabel("Fitness");
			fitnessTF=new JTextField();
			fitnessTF.setEditable(false);
			popSizeLabel=new JLabel("Pop. size");
			popSizeTF=new JTextField();
			popSizeTF.setEditable(false);
			fromToLabel=new JLabel("Lapse");
			fromToTF=new JTextField();
			fromToTF.setEditable(false);
			maxGenLabel=new JLabel("Max Gen");
			maxGenTF=new JTextField();
			maxGenTF.setEditable(false);
			
			mainGeDetailsPanel.setBounds(10,5,515,110);
			nCitiesLabel.setBounds(7,20,50,20);
			nCitiesTF.setBounds(60,20,70,20);
			popSizeLabel.setBounds(140,20,60,20);
			popSizeTF.setBounds(195,20,60,20);
			fromToLabel.setBounds(265,20,50,20);
			fromToTF.setBounds(305,20,60,20);
			maxGenLabel.setBounds(385,20,50,20);
			maxGenTF.setBounds(440,20,65,20);
			crossMetLabel.setBounds(7,45,110,20);
			crossMetTF.setBounds(120,45,180,20);
			crossProbLabel.setBounds(315,45,70,20);
			crossProbTF.setBounds(385,45,40,20);
			mutMetLabel.setBounds(7,75,110,20);
			mutMetTF.setBounds(120,75,180,20);
			mutProbLabel.setBounds(315,75,70,20);
			mutProbTF.setBounds(385,75,40,20);
			geDataProducedPanel.setBounds(10,120,430,90);
			tourLabel.setBounds(10,25,80,20);
			tourTF.setBounds(90,25,90,20);
			generLabel.setBounds(210,25,80,20);
			generTF.setBounds(290,25,80,20);
			fitnessLabel.setBounds(10,55,60,20);
			fitnessTF.setBounds(70,55,50,20);
			timeLabel.setBounds(210,55,50,20);
			timeTF.setBounds(260,55,80,20);
			
			mainFrame.add(mainGeDetailsPanel);
			mainGeDetailsPanel.add(nCitiesLabel);
			mainGeDetailsPanel.add(nCitiesTF);
			mainGeDetailsPanel.add(popSizeLabel);
			mainGeDetailsPanel.add(popSizeTF);
			mainGeDetailsPanel.add(fromToLabel);
			mainGeDetailsPanel.add(fromToTF);
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
			geDataProducedPanel.add(tourLabel);
			geDataProducedPanel.add(tourTF);
			geDataProducedPanel.add(generLabel);
			geDataProducedPanel.add(generTF);
			geDataProducedPanel.add(fitnessLabel);
			geDataProducedPanel.add(fitnessTF);
			geDataProducedPanel.add(timeLabel);
			geDataProducedPanel.add(timeTF);
		}
	}
	
	private void setTextBoxes(){
		if(traGen.equals("tra")){
			nCitiesTF.setText(""+tradObj.getNumCities());
			tourTF.setText(""+tradObj.getTourLength());
			long tempTime=tradObj.getExecutionTime();
			String timeString=String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(tempTime),TimeUnit.MILLISECONDS.toSeconds(tempTime)%60, (tempTime%100));
			timeTF.setText(""+timeString);
			traDetailsTA.setText(tradObj.getDetails());
		}
		else{
			popSizeTF.setText(""+genObj.getPopSize());
			fromToTF.setText(""+genObj.getPopFrom()+"-"+genObj.getPopTo());
			maxGenTF.setText(""+genObj.getMaxGen());
			crossMetTF.setText(""+genObj.getCrossoverMethod());
			crossProbTF.setText(""+genObj.getCrossoverProbability());
			mutMetTF.setText(""+genObj.getMutationMethod());
			mutProbTF.setText(""+genObj.getMutationProbability());
			generTF.setText(""+genObj.getGenerationCount());
			fitnessTF.setText(""+genObj.getFitness());
			long tempTime=genObj.getExecutionTime();
			String timeString=String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(tempTime),TimeUnit.MILLISECONDS.toSeconds(tempTime)%60, (tempTime%100));
			timeTF.setText(timeString);
		}
	}
}
