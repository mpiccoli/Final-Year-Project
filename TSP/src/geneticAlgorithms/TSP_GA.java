package geneticAlgorithms;

import java.awt.Point;
import java.util.Vector;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.impl.salesman.*;

public class TSP_GA extends Salesman{
	private static final long serialVersionUID = 5489585985877059554L;
	private int numCities;
	private int[][] cities;
	public Vector<Point> citiesVector;
	public Configuration conf;
	public Vector<Vector<Point>> results;
	public Vector<Double> pathDistances;
	private TSP_GA_Adapter worker;

	@SuppressWarnings("unchecked")
	public TSP_GA(Vector<Point> citiesData, Configuration c, TSP_GA_Adapter threadWorker, Vector<Vector<Point>> results, Vector<Double> distances){
		numCities=citiesData.size();
		this.results=results;
		worker=threadWorker;
		citiesVector=(Vector<Point>) citiesData.clone();
		cities=this.convertVectorToMatrix(citiesData);
		pathDistances=distances;
		conf=c;
	}
	public void setTSP_Adapter(TSP_GA_Adapter adapter){
		if(adapter!=null){
			worker=adapter;
		}
		else{
			worker=null;
		}
	}

	public IChromosome createSampleChromosome(Object a_initial_data) {
		try {
			Gene[] genes = new Gene[numCities];
			for (int i = 0; i < genes.length; i++) {
				genes[i] = new IntegerGene(conf, 0, numCities-1);
				genes[i].setAllele(new Integer(i));
			}
			IChromosome sample = new Chromosome(conf, genes);
			return sample;
		}
		catch (InvalidConfigurationException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	public double distance(Gene a_from, Gene a_to) {
		IntegerGene geneA = (IntegerGene) a_from;
		IntegerGene geneB = (IntegerGene) a_to;
		int a = geneA.intValue();
		int b = geneB.intValue();
		double xValue=(cities[a][0]-cities[b][0])*(cities[a][0]-cities[b][0]);
		double yValue=(cities[a][1]-cities[b][1])*(cities[a][1]-cities[b][1]);
		return Math.sqrt(xValue+yValue);
	}

	private int[][] convertVectorToMatrix(Vector<Point> data){
		if(data.size()>0){
			int[][] citiesCoordinates=new int[data.size()][data.size()];
			for(int i=0; i<data.size(); i++){
				int tempX=(int) data.get(i).getX();
				int tempY=(int) data.get(i).getY();
				citiesCoordinates[i][0]=tempX;
				citiesCoordinates[i][1]=tempY;
			}
			return citiesCoordinates;
		}
		return null;
	}

	private Vector<Point> convertArrayResultToVectorPoint(Gene[] data){
		//Create some temporary object to store the data
		Vector<Point> points=new Vector<Point>();
		//Make a copy of the cities
		@SuppressWarnings("unchecked")
		Vector<Point> tempCities=(Vector<Point>) citiesVector.clone();
		points.setSize(data.length);
		//This loop inserts the elements in the right position
		for(int i=0; i<data.length; i++){
			IntegerGene gene = (IntegerGene) data[i];
			int index=(int) gene.getAllele();
			Point element=tempCities.elementAt(index);
			points.set(i, element);
		}
		return points;
	}
	//This code below has been adapted from the class Salesman, which this class is extending from
	@SuppressWarnings("unused")
	@Override
	public IChromosome findOptimalPath(final Object a_initial_data) throws Exception {
		conf = createConfiguration(a_initial_data);
		FitnessFunction myFunc = createFitnessFunction(a_initial_data);
		conf.setFitnessFunction(myFunc);
		IChromosome sampleChromosome = createSampleChromosome(a_initial_data);
		conf.setSampleChromosome(sampleChromosome);
		conf.setPopulationSize(getPopulationSize());
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

		IChromosome best = null;
		IChromosome tempBest = null;
		double bestFitnessValue=Double.MAX_VALUE;
		//Evolve the population and find the best chromosome which guarantee the shortest path for the TSP
		Evolution:
			for (int i = 0; i < getMaxEvolution(); i++) {
				population.evolve();
				tempBest = population.getFittestChromosome();
				//If the chromosome has got a fitness value inferior to the current best value, store the new best chromosome 
				if((Integer.MAX_VALUE/2 - tempBest.getFitnessValue()) < bestFitnessValue){
					best=tempBest;
					bestFitnessValue=(Integer.MAX_VALUE/2 - tempBest.getFitnessValue());
				}
				this.addToResults(tempBest);
			}
		return best;
	}
	
	//This method call is executed when a chromosome with a better fitness value is found
	/*public void updateBestResultSoFar(IChromosome ic){
		System.out.println(this.convertArrayResultToVectorPoint(ic.getGenes()).toString());
		double score=(Integer.MAX_VALUE / 2 - ic.getFitnessValue());
		System.out.println("Score " + score);
	}*/
	public void addToResults(IChromosome ic){
		Vector<Point> temp=this.convertArrayResultToVectorPoint(ic.getGenes());
		//Add the starting point so when the drawing occurs, the path is completed
		temp.add(citiesVector.elementAt(0));
		results.add(temp);
		pathDistances.add((Integer.MAX_VALUE/2 - ic.getFitnessValue()));
		worker.updateProgress((int)(Math.random()*10));
	}
	
	public int getBestPathIndex(){
		int index=0;
		if(results.size()>0){
			double max=Integer.MAX_VALUE;
			for(int i=0; i<pathDistances.size(); i++){
				if(pathDistances.elementAt(i)<max){
					max=pathDistances.elementAt(i);
					index=i;
				}
			}
			return index;
		}
		return -1;
	}
}

