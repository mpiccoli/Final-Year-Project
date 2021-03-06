package geneticAlgorithms;

import java.awt.Point;
import java.util.Vector;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.event.EventManager;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.ChromosomePool;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.RangedSwappingMutationOperator;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.SwappingMutationOperator;
import org.jgap.impl.salesman.Salesman;

import geneticAlgorithms.CrossoverMethods.CycleCrossover;
import geneticAlgorithms.CrossoverMethods.ModifiedSequentialConstructiveCrossover;
import geneticAlgorithms.CrossoverMethods.OrderedCrossover;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see Point, Vector, EventManager, Salesman, CycleCrossover, ModifiedSequentialConstructiveCrossover, OrderedCrossover
 * 
 * This class is an implementation of the Salesman class which has been adapted and changed for this project
 * 
 */
public class TSP_GA extends Salesman {

	// Define the constants of this class
	private static final long serialVersionUID = 5489585985877059554L;
	// Global Variables
	private int numCities, crossoverProb, mutationProb, populationSize;
	private int[][] cities;
	public Vector<Point> citiesVector;
	public Vector<Vector<Point>> results;
	public Vector<Double> pathDistances;
	private TSP_GA_Adapter worker;
	private Configuration conf;
	private String crossoverChosen, mutationChosen;

	/*
	 * @param citiesData This Vector contains the list of cities for the TSP
	 * 
	 * @param threadWorker This is a reference to the TSP_GA_Adapter object
	 * 
	 * @param results This Vector of vector contains all the path the algorithm
	 * has tried in order to find the best one
	 * 
	 * @param distances This vector stores the path distance for each path
	 * stored in the results vector
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
	 * @throws InvalidConfigurationException If the configuration object has
	 * some invalid settings
	 * 
	 * Constructor with 8 parameters
	 */
	@SuppressWarnings("unchecked")
	public TSP_GA(Vector<Point> citiesData, TSP_GA_Adapter threadWorker, Vector<Vector<Point>> results,
			Vector<Double> distances, String crossover, int cProb, String mutation, int mProb)
			throws InvalidConfigurationException {
		numCities = citiesData.size();
		this.results = results;
		worker = threadWorker;
		crossoverChosen = crossover;
		crossoverProb = cProb;
		mutationChosen = mutation;
		mutationProb = mProb;
		// Create a copy of the cities
		citiesVector = (Vector<Point>) citiesData.clone();
		// Convert the data contained in the vector to an array matrix
		cities = this.convertVectorToMatrix(citiesData);
		pathDistances = distances;
		// Mathematical formula used to calculate an appropriate number that is
		// going to constitute the population size during the evolutions
		populationSize = (1 * (int) ((Math.log(1 - Math.pow(0.99, (1.0 / cities.length))))
				/ (Math.log(((float) (cities.length - 3) / (float) (cities.length - 1))))));
	}

	public void setTSP_Adapter(TSP_GA_Adapter adapter) {
		// Copy the object reference only if the reference is valid
		if (adapter != null) {
			worker = adapter;
		} else {
			worker = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jgap.impl.salesman.Salesman#createConfiguration(java.lang.Object)
	 */
	public Configuration createConfiguration(final Object a_initial_data) throws InvalidConfigurationException {
		// This is copied from DefaultConfiguration.
		Configuration config = new Configuration();
		BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(config, 1.0d);
		bestChromsSelector.setDoubletteChromosomesAllowed(false);
		config.addNaturalSelector(bestChromsSelector, true);
		config.setRandomGenerator(new StockRandomGenerator());
		config.setMinimumPopSizePercent(0);
		config.setEventManager(new EventManager());
		config.setFitnessEvaluator(new DefaultFitnessEvaluator());
		config.setChromosomePool(new ChromosomePool());
		return config;
	}

	public Vector<Point> getCities() {
		return citiesVector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jgap.impl.salesman.Salesman#createSampleChromosome(java.lang.Object)
	 */
	public IChromosome createSampleChromosome(Object a_initial_data) {
		try {
			Gene[] genes = new Gene[numCities];
			// Create a new integer gene with will be assume a value between 0
			// and the number of cities
			for (int i = 0; i < genes.length; i++) {
				genes[i] = new IntegerGene(conf, 0, numCities - 1);
				genes[i].setAllele(new Integer(i));
			}
			// Create an IChromosome object and pass the genes as well as the
			// configuration object to it
			IChromosome sample = new Chromosome(conf, genes);
			return sample;
		}
		// Interrupt the process in case an error occurs within the
		// configuration setup and print it onto the console
		catch (InvalidConfigurationException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgap.impl.salesman.Salesman#distance(org.jgap.Gene,
	 * org.jgap.Gene)
	 */
	public double distance(Gene a_from, Gene a_to) {
		// Extract the integer value from each gene
		IntegerGene geneA = (IntegerGene) a_from;
		IntegerGene geneB = (IntegerGene) a_to;
		int a = geneA.intValue();
		int b = geneB.intValue();
		// Retrieve the X and Y coordinates looking up in the cities matrix and
		// apply formula
		double xValue = (cities[a][0] - cities[b][0]) * (cities[a][0] - cities[b][0]);
		double yValue = (cities[a][1] - cities[b][1]) * (cities[a][1] - cities[b][1]);
		return Math.sqrt(xValue + yValue);
	}

	/*
	 * @param data This is the vector of cities
	 * 
	 * @return Array matrix containing the city coordinates
	 * 
	 * This method will take in a vector of cities and return an array matrix of
	 * city coordinates
	 */
	private int[][] convertVectorToMatrix(Vector<Point> data) {
		// Check that the vector contains some elements
		if (data.size() > 0) {
			int[][] citiesCoordinates = new int[data.size()][data.size()];
			for (int i = 0; i < data.size(); i++) {
				int tempX = (int) data.get(i).getX();
				int tempY = (int) data.get(i).getY();
				citiesCoordinates[i][0] = tempX;
				citiesCoordinates[i][1] = tempY;
			}
			return citiesCoordinates;
		}
		return null;
	}

	/*
	 * @param data This array contains the resulting path
	 * 
	 * @return A Vector of type Point storing the resulting path
	 * 
	 * This method converts an array of Gene into a Vector of point that the
	 * application will use to display
	 */
	private Vector<Point> convertArrayResultToVectorPoint(Gene[] data) {
		// Create some temporary object to store the data
		Vector<Point> points = new Vector<Point>();
		// Make a copy of the cities
		@SuppressWarnings("unchecked")
		Vector<Point> tempCities = (Vector<Point>) citiesVector.clone();
		points.setSize(data.length);
		// This loop inserts the elements in the right position
		for (int i = 0; i < data.length; i++) {
			IntegerGene gene = (IntegerGene) data[i];
			int index = (int) gene.getAllele();
			Point element = tempCities.elementAt(index);
			points.set(i, element);
		}
		return points;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgap.impl.salesman.Salesman#findOptimalPath(java.lang.Object)
	 */
	@SuppressWarnings("unused")
	@Override
	public IChromosome findOptimalPath(final Object a_initial_data) throws Exception {
		// Create a configuration object
		conf = createConfiguration(a_initial_data);
		// Initialize a fitness function
		FitnessFunction myFunc = createFitnessFunction(conf);
		Configuration.reset();
		conf.setFitnessFunction(myFunc);
		// Create a chromosome sample and pass it to the configuration object
		IChromosome sampleChromosome = createSampleChromosome(conf);
		conf.setSampleChromosome(sampleChromosome);
		// Set the population size to the configuration object
		conf.setPopulationSize(populationSize);
		// Add the crossover and mutation method chosen by the user in the GUI
		this.addCrossoverAndMutationMethodsAndProbability(conf, crossoverChosen, crossoverProb, mutationChosen,
				mutationProb);
		// Create a new population
		IChromosome[] chromosomes = new IChromosome[conf.getPopulationSize()];
		Gene[] samplegenes = sampleChromosome.getGenes();
		for (int i = 0; i < chromosomes.length; i++) {
			Gene[] genes = new Gene[samplegenes.length];
			for (int k = 0; k < genes.length; k++) {
				genes[k] = samplegenes[k].newGene();
				genes[k].setAllele(samplegenes[k].getAllele());
			}
			chromosomes[i] = new Chromosome(conf, genes);
		}
		Genotype population = new Genotype(conf, new Population(conf, chromosomes));
		// Start evolving the initial population and find the best path
		// performed within the given number of generations
		IChromosome best = null;
		IChromosome tempBest = null;
		double bestFitnessValue = Double.MAX_VALUE;
		// Evolve the population and find the best chromosome which guarantees
		// the shortest path for the TSP
		Evolution: for (int i = 0; i < getMaxEvolution(); i++) {
			population.evolve();
			tempBest = population.getFittestChromosome();
			// If the chromosome has got a fitness value inferior to the current
			// best value, store the new best chromosome
			if ((Integer.MAX_VALUE / 2 - tempBest.getFitnessValue()) < bestFitnessValue) {
				best = tempBest;
				bestFitnessValue = (Integer.MAX_VALUE / 2 - tempBest.getFitnessValue());
			}
			this.addToResults(tempBest);
		}
		return best;
	}

	/*
	 * @param c Configuration Object
	 * 
	 * @param crossover String containing the name of the chosen crossover
	 * 
	 * @param cProbability probability associated to the crossover operator
	 * during evolution
	 * 
	 * @param mutation String containing the name of the chosen mutation
	 * operator
	 * 
	 * @param mProbability probability associated to the mutation operator
	 * during evolution
	 * 
	 * This method add the crossover an mutation operators to the configuration
	 * object as well as a given probability value for each operator
	 * 
	 */
	private void addCrossoverAndMutationMethodsAndProbability(Configuration c, String crossover, int cProbability,
			String mutation, int mProbability) {
		try {
			// Verify which crossover operator has the user chosen
			if (crossover.equals("Cycle Crossover")) {
				c.addGeneticOperator(new CycleCrossover(c, crossoverProb));
			} else if (crossover.equals("Ordered Crossover")) {
				c.addGeneticOperator(new OrderedCrossover(c, crossoverProb));
			} else if (crossover.equals("Modified Sequential Constructive Crossover")) {
				c.addGeneticOperator(new ModifiedSequentialConstructiveCrossover(c, crossoverProb, citiesVector));
			}
			// Verify which mutation operator has the user chosen
			if (mutation.equals("Ranged Swapping Mutation")) {
				c.addGeneticOperator(new RangedSwappingMutationOperator(c, mutationProb));
			} else if (mutation.equals("Swapping Mutation")) {
				c.addGeneticOperator(new SwappingMutationOperator(c, mutationProb));
			}
		} catch (InvalidConfigurationException error) {
			error.printStackTrace();
		}
	}

	/*
	 * @param ic This IChromosome object contains the order of genes in the
	 * current evolution
	 * 
	 * This method extracts the genes from the IChromosome and converts them to
	 * points. These points are saved into a temporary vector which is added to
	 * the vector results. It then finds the length of the path performed by
	 * this chromosome and stores it into the pathDistances vector.
	 * 
	 */
	public void addToResults(IChromosome ic) {
		// Create a temporary vector
		Vector<Point> temp = this.convertArrayResultToVectorPoint(ic.getGenes());
		// Add the starting point to the end of the vector so when the drawing
		// occurs, the path is completed
		temp.add(temp.firstElement());
		results.add(temp);
		pathDistances.add((Integer.MAX_VALUE / 2 - ic.getFitnessValue()));
		// Send a thread update to teh GUI
		worker.updateProgress((int) (Math.random() * 10));
	}

	/*
	 * @return int return the index of the shortest path
	 * 
	 * This method searches for the shortest path contained within the
	 * pathDistances vector and returns its index to the application.
	 * 
	 */
	public int getBestPathIndex() {
		int index = 0;
		if (results.size() > 0) {
			double max = Integer.MAX_VALUE;
			for (int i = 0; i < pathDistances.size(); i++) {
				// If the current value is less than the max value, a new best
				// path has been found
				if (pathDistances.elementAt(i) < max) {
					max = pathDistances.elementAt(i);
					index = i;
				}
			}
			return index;
		}
		// Return -1 when the results does not contain any data
		return -1;
	}

	/*
	 * This method will stop the execution of an object of this class running
	 */
	public void stopExecution() {
		Thread.currentThread().interrupt();
	}
}
