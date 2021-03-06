package geneticAlgorithms;

import java.awt.Point;
import java.util.Vector;

import org.jgap.IChromosome;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see IChromosome, Vector, Point, TSP_GA
 * 
 * This class is an adapter between the TSP_GA and the SwingWorker class
 * The implementation of this class is based on the Adapter Design Pattern which allows two classes that extends two other classes to work together
 * 
 */
public class TSP_GA_Adapter {

	// Global Variables
	private TSP_GA tsp;
	private TSP_GA_Worker tspWorker;
	@SuppressWarnings("unused")
	private IChromosome bestPath;
	private Vector<Point> points;
	private Vector<Vector<Point>> resultsData;
	private Vector<Double> pathDistances;
	private int maxGeneration, crossoverProb, mutationProb;
	private String crossoverChosen, mutationChosen;

	/*
	 * @param fw This is a TSP_GA_Worker reference to allow the data to be
	 * updated in real time to the GUI
	 * 
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
	 * Constructor with 9 parameters
	 * 
	 */
	public TSP_GA_Adapter(TSP_GA_Worker fw, Vector<Point> cities, Vector<Vector<Point>> results,
			Vector<Double> distances, int maxGen, String crossover, int cProb, String mutation, int mProb) {
		tspWorker = fw;
		points = cities;
		resultsData = results;
		pathDistances = distances;
		maxGeneration = maxGen;
		crossoverChosen = crossover;
		crossoverProb = cProb;
		mutationChosen = mutation;
		mutationProb = mProb;
	}

	/*
	 * This method tells the TSP_GA class to start the execution of the genetic
	 * algorithm
	 */
	public Object startEvolution() {
		// Verify that the list of cities and results are valid references
		if (points != null && resultsData != null) {
			try {
				// Create a TSP_GA object passing all the variable this class
				// has received to setup the configuration of the Genetic
				// Algorithm
				tsp = new TSP_GA(points, this, resultsData, pathDistances, crossoverChosen, crossoverProb,
						mutationChosen, mutationProb);
				tsp.setTSP_Adapter(this);
				tsp.setMaxEvolution(maxGeneration);
				// This will make the TSP_GA running a thread executing
				// evolutions around the list of cities given
				bestPath = tsp.findOptimalPath(null);
			}
			// Catch any error may occur during the execution
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// Display an error message otherwise
		else {
			System.out.println("Error, values are null");
		}
		// Set the progress to 100% which means, operations executed and
		// completed
		updateProgress(100);
		return null;
	}

	/*
	 * @param value This integer value is used to set the progress of the
	 * SwingWorker object
	 * 
	 * This method updates the progress of the SwingWorker class which will then
	 * send an update to the GUI
	 */
	public void updateProgress(int value) {
		tspWorker.updateProgress(value);
	}

	/*
	 * This method interrupts anything the TSP_GA is performing
	 */
	public void stopExecution() {
		tsp.stopExecution();
	}
}
