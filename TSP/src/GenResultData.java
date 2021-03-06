import java.awt.Point;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import geneticAlgorithms.TSP_GA;
import geneticAlgorithms.TSP_GA_Worker;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see Point, Vector, TSP_GA, TSP_GA_Worker
 * 
 * This Class is used to store all the settings and execution data a genetic algorithm produces from when it is added to the queue
 * until the end of its execution
 * 
 */
public class GenResultData {
	private int generationCount, numCities, popSize, maxGen, indexBestResultAtGen;
	private float crossoverProbability, mutationProbability;
	private String crossoverMethod, mutationMethod;
	private double fitness;
	private long timeExecution;
	private Vector<Point> listOfCities, results;
	private Vector<Double> pathDistances;
	private Vector<Vector<Point>> resultsData;
	private TSP_GA tsp;
	private TSP_GA_Worker tspWorker;

	/*
	 * @param nCities This defines the number of cities
	 * 
	 * @param popS This defines the size of the population
	 * 
	 * @param maxG This defines the maximum number of generations the algorithm
	 * will perform
	 * 
	 * @param crossMet This defines the crossover methods the algorithm will use
	 * during execution
	 * 
	 * @param crossP This defines the probability of using the crossover
	 * operator
	 * 
	 * @param mutMet This defines the mutation methods the algorithm will use
	 * during execution
	 * 
	 * @param mutP This defines the probability of using the mutation operator
	 * 
	 * @param tsp Reference to the TSP_GA object
	 * 
	 * @param tspWorker Reference to the TSP_GA_worker object
	 * 
	 * Constructor with parameters
	 * 
	 */
	public GenResultData(int nCities, int popS, int maxG, String crossMet, float crossP, String mutMet, float mutP,
			TSP_GA tsp, TSP_GA_Worker tspWorker) {
		// Store the references into the global objects of this class
		crossoverProbability = crossP;
		mutationProbability = mutP;
		crossoverMethod = crossMet;
		mutationMethod = mutMet;
		numCities = nCities;
		popSize = popS;
		maxGen = maxG;
		this.tsp = tsp;
		this.tspWorker = tspWorker;
		// Instantiate global variables
		fitness = 0;
		generationCount = 0;
		timeExecution = 0;
		indexBestResultAtGen = 0;
		results = new Vector<Point>();
	}

	public void setGenerationCount(int genC) {
		generationCount = genC;
	}

	public int getGenerationCount() {
		return generationCount;
	}

	public void setIndexBestResultAtGeneration(int index) {
		indexBestResultAtGen = index;
	}

	public int getIndexBestResultAtGeneration() {
		return indexBestResultAtGen;
	}

	public void setCrossOverProbability(float cP) {
		crossoverProbability = cP;
	}

	public float getCrossoverProbability() {
		return crossoverProbability;
	}

	public void setMutationProbability(float mP) {
		mutationProbability = mP;
	}

	public float getMutationProbability() {
		return mutationProbability;
	}

	public void setCrossoverMethod(String cM) {
		crossoverMethod = cM;
	}

	public String getCrossoverMethod() {
		return crossoverMethod;
	}

	public void setMutationMethod(String mM) {
		mutationMethod = mM;
	}

	public String getMutationMethod() {
		return mutationMethod;
	}

	public void setFitness(double fit) {
		fitness = fit;
	}

	public double getFitness() {
		return fitness;
	}

	public void setExecutionTime(long time) {
		timeExecution = time;
	}

	public long getExecutionTime() {
		return timeExecution;
	}

	public void setnumCities(int nC) {
		numCities = nC;
	}

	public int getnumCities() {
		return numCities;
	}

	public void setPopSize(int pS) {
		popSize = pS;
	}

	public int getPopSize() {
		return popSize;
	}

	public void setMaxGen(int mG) {
		maxGen = mG;
	}

	public int getMaxGen() {
		return maxGen;
	}

	/*
	 * @param data This is the vector containing all the city information
	 * 
	 * @param option This boolean values defines whether the vector object needs
	 * to be copied or just used its reference
	 */
	@SuppressWarnings("unchecked")
	public void setResultingPoints(Vector<Point> data, boolean option) {
		if (option) {
			results = data;
		} else {
			// Create an exact copy of the vector
			results = (Vector<Point>) data.clone();
		}
	}

	public Vector<Point> getResultingPoints() {
		return results;
	}

	@SuppressWarnings("unchecked")
	public void setCities(Vector<Point> data) {
		listOfCities = (Vector<Point>) data.clone();
	}

	@SuppressWarnings("unchecked")
	public Vector<Point> getCities() {
		return (Vector<Point>) listOfCities.clone();
	}

	@SuppressWarnings("unchecked")
	public void setPathDistances(Vector<Double> distances, boolean option) {
		if (option) {
			pathDistances = distances;
		} else {
			pathDistances = (Vector<Double>) distances.clone();
		}
	}

	public Vector<Double> getPathDistances() {
		return pathDistances;
	}

	public void setTSP(TSP_GA tsp) {
		this.tsp = tsp;
	}

	public TSP_GA getTSP() {
		return tsp;
	}

	public void setTSP_Worker(TSP_GA_Worker tspWork) {
		tspWorker = tspWork;
	}

	public TSP_GA_Worker getTSP_Worker() {
		return tspWorker;
	}

	@SuppressWarnings("unchecked")
	public void setResultsData(Vector<Vector<Point>> data, boolean option) {
		if (option) {
			resultsData = data;
		} else {
			resultsData = (Vector<Vector<Point>>) data.clone();
		}
	}

	public Vector<Vector<Point>> getResultsData() {
		return resultsData;
	}

	/*
	 * This method creates a string containing all the class information, ready
	 * to be exported to a file
	 */
	public String wrapDataForSummativeFileWriter(){
		String s = "";
		// Add the algorithm basic information
		s += "Algorithm Name: Genetic Algorithm\n" + "Number of Cities: " + numCities + "\n"
				+ "Population Size During Evolution: " + popSize + "\n" + "Number of Generations: " + maxGen + "\n"
				+ "Crossover Method: " + crossoverMethod + "\n" + "Probability associated to the Crossover: "
				+ crossoverProbability + "%\n" + "Mutation Method: " + mutationMethod + "\n"
				+ "Probability associated to the Mutation: " + mutationProbability + "%\n\n" + "Best Fitness Value: "
				+ fitness + "\n" + "The Best Path has been found within generation: " + indexBestResultAtGen + "\n"
				+ "Performance Time: "
				+ String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeExecution),
						TimeUnit.MILLISECONDS.toSeconds(timeExecution) % 60, (timeExecution % 100))
				+ "\n\n" + "List of Cities:\n";
		// Try to add the list of cities
		try {
			String cities = "";
			for (int i = 0; i < listOfCities.size(); i++) {
				Point temp = listOfCities.elementAt(i);
				cities += "   City n. " + (i + 1) + " --> (" + Math.round(temp.getX()) + "," + Math.round(temp.getY())
				+ ")\n";
			}
			s += cities + "\n";
		} catch (Exception e) {
			s += "   No Data\n\n";
		}
		s += "Best Path Found:\n";
		// Try to add the list of cities for the best solution found
		try {
			String path = "";
			for (int i = 0; i < results.size(); i++) {
				Point temp = results.elementAt(i);
				path += "   City n. " + (i + 1) + " --> (" + Math.round(temp.getX()) + "," + Math.round(temp.getY())
				+ ")\n";
			}
			s += path + "\n";
		} catch (Exception e) {
			s += "   No Data\n\n";
		}
		return s;
	}

	/*
	 * This method creates a string containing all the class information, ready
	 * to be exported to a file
	 */
	public String wrapDataForFileWriter() {
		String s = "";
		// Add the algorithm basic information
		s += "Algorithm Name: Genetic Algorithm\n" + "Number of Cities: " + numCities + "\n"
				+ "Population Size During Evolution: " + popSize + "\n" + "Number of Generations: " + maxGen + "\n"
				+ "Crossover Method: " + crossoverMethod + "\n" + "Probability associated to the Crossover: "
				+ crossoverProbability + "%\n" + "Mutation Method: " + mutationMethod + "\n"
				+ "Probability associated to the Mutation: " + mutationProbability + "%\n\n" + "Best Fitness Value: "
				+ fitness + "\n" + "The Best Path has been found within generation: " + indexBestResultAtGen + "\n"
				+ "Performance Time: "
				+ String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeExecution),
						TimeUnit.MILLISECONDS.toSeconds(timeExecution) % 60, (timeExecution % 100))
				+ "\n\n" + "List of Cities:\n";
		// Try to add the list of cities
		try {
			String cities = "";
			for (int i = 0; i < listOfCities.size(); i++) {
				Point temp = listOfCities.elementAt(i);
				cities += "   City n. " + (i + 1) + " --> (" + Math.round(temp.getX()) + "," + Math.round(temp.getY())
				+ ")\n";
			}
			s += cities + "\n";
		} catch (Exception e) {
			s += "   No Data\n\n";
		}
		s += "Best Path Found:\n";
		// Try to add the list of cities for the best solution found
		try {
			String path = "";
			for (int i = 0; i < results.size(); i++) {
				Point temp = results.elementAt(i);
				path += "   City n. " + (i + 1) + " --> (" + Math.round(temp.getX()) + "," + Math.round(temp.getY())
				+ ")\n";
			}
			s += path + "\n";
		} catch (Exception e) {
			s += "   No Data\n\n";
		}

		s += "List of all the Results computed:\n";
		// Try to add the list of results
		try {
			String resultOfResult = "";
			for (int i = 0; i < resultsData.size(); i++) {
				String result = "  --> Result n. " + (i + 1) + "\n";
				for (int j = 0; j < resultsData.elementAt(i).size(); j++) {
					Point temp = resultsData.elementAt(i).elementAt(j);
					result += "    City n. " + (j + 1) + " --> (" + Math.round(temp.getX()) + ","
							+ Math.round(temp.getY()) + ")\n";
				}
				resultOfResult += result + "\n";
			}
			s += resultOfResult + "\n";
		} catch (Exception e) {
			s += "   No Data\n\n";
		}
		// Try to add the list of all the distanced computed
		s += "List of all the Distances computed:\n";
		try {
			String paths = "";
			for (int i = 0; i < pathDistances.size(); i++) {
				paths += "   Path Distance (" + (i + 1) + ") --> " + pathDistances.elementAt(i) + "\n";
			}
			s += paths + "\n";
		} catch (Exception e) {
			s += "   No Data\n\n";
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GenResultData)) {
			return false;
		}
		// Define when two objects of this class can be considered identical
		GenResultData temp = (GenResultData) obj;
		return (this.numCities == temp.numCities && this.maxGen == temp.maxGen
				&& this.crossoverMethod.equals(temp.crossoverMethod)
				&& this.crossoverProbability == temp.crossoverProbability
				&& this.mutationMethod.equals(temp.mutationMethod)
				&& this.mutationProbability == temp.mutationProbability);
	}
}
