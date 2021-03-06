package geneticAlgorithms;

import java.awt.Point;
import java.util.Vector;

import javax.swing.SwingWorker;

import org.jgap.InvalidConfigurationException;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see Point, Vector, SwingWorker, InvalidConfigurationException, TSP_GA_Adapter
 * 
 * This Class the Thread running for the TSP_GA into a SwingWorker Thread to allow responsiveness of the GUI
 * 
 */
@SuppressWarnings("rawtypes")
public class TSP_GA_Worker extends SwingWorker {

	// Global Variables
	private TSP_GA_Adapter work;
	private long executionTime;
	private boolean processedCancelled;
	private String crossoverChosen, mutationChosen;
	private int crossoverProb, mutationProb;

	/*
	 * @param cities This Vector contains the list of cities for the TSP
	 * 
	 * @param results This Vector of vector contains all the path the algorithm
	 * has tried in order to find the best one
	 * 
	 * @param distances This vector stores the path distance for each path
	 * stored in the results vector
	 * 
	 * @param maxGen This value stores the max number of generations the
	 * algorithm will evolve
	 * 
	 * @param crossover This contains the name of the crossover method
	 * 
	 * @param cProb This contains the probability related to the crossover
	 * method
	 * 
	 * @param mutation This contains the name of the mutation operator
	 * 
	 * @param mProb This contains the probability related to the mutation
	 * operator
	 * 
	 * @throws InvalidConfigurationException If the configuration for the TSP_GA
	 * has some invalid parameters
	 * 
	 * Constructor with 8 parameters
	 */
	public TSP_GA_Worker(Vector<Point> cities, Vector<Vector<Point>> results, Vector<Double> distances, int maxGen,
			String crossover, int cProb, String mutation, int mProb) throws InvalidConfigurationException {
		crossoverChosen = crossover;
		mutationChosen = mutation;
		crossoverProb = cProb;
		mutationProb = mProb;
		work = new TSP_GA_Adapter(this, cities, results, distances, maxGen, crossoverChosen, crossoverProb,
				mutationChosen, mutationProb);
		processedCancelled = false;
	}

	/*
	 * @param startTime This is the time the execution started
	 * 
	 * @param finishTime This is the time the execution was successfully
	 * completed
	 * 
	 * This method calculates the execution time for a genetic algorithm
	 */
	private void calculateExecutionTime(long startTime, long finishTime) {
		// Verify that the Thread has not been stopped
		if (!processedCancelled) {
			executionTime = (finishTime - startTime);
		} else {
			executionTime = -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Object doInBackground() {
		// Start the time for this process
		long startTime = System.currentTimeMillis();
		try {
			work.startEvolution();
			if (this.isCancelled()) {
				processedCancelled = true;
				work.stopExecution();
				this.updateProgress(100);
				this.cancel(true);
				return null;
			}
		}
		// Catch any exceptions may occur during the execution
		catch (Exception e) {
			e.printStackTrace();
		}
		// When the process is finishes, calculate the execution time
		this.calculateExecutionTime(startTime, System.currentTimeMillis());
		// Set the process status as completed
		this.done();
		return null;
	}

	/*
	 * @param v This is the value the tells the GUI the process is proceeding
	 * its execution correctly
	 * 
	 * This method changes the value of the progress parameter which helps the
	 * user understanding the progress of a running thread
	 */
	public void updateProgress(int v) {
		this.setProgress(v);
	}

	/*
	 * This method return the execution time for the running algorithm
	 */
	public long getExecutionTime() {
		return executionTime;
	}
}
