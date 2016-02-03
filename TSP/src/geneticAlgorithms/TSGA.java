package geneticAlgorithms;
import java.awt.Point;
import java.util.Vector;

import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.impl.salesman.*;

import geneticAlgorithms.Crossover.CycleCrossover;

public class TSGA extends Salesman{
	private int numCities;
	private int[][] cities;
	private Configuration conf;

	public TSGA(Vector<Point> citiesData, Configuration c){
		numCities=citiesData.size();
		cities=this.convertVectorToMatrix(citiesData);
		conf=c;
	}

	public IChromosome createSampleChromosome(Object a_initial_data) {
		try {
			//Configuration conf=this.getConfiguration();
			Gene[] genes = new Gene[numCities];
			for (int i = 0; i < genes.length; i++) {
				//genes[i] = new IntegerGene(getConfiguration(), 0, numCities-1);
				genes[i] = new IntegerGene(conf, 0, numCities-1);
				genes[i].setAllele(new Integer(i));
			}
			IChromosome sample = new Chromosome(conf, genes);
			//System.out.println(conf.toString());
			return sample;
		}
		catch (InvalidConfigurationException iex) {
			throw new IllegalStateException(iex.getMessage());
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
		//new int[][] { {2, 4}, {7, 5}, {7, 11}, {8, 1}, {1, 6}, {5, 9}, {0, 11} };
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
	//This code below has been adapted from the class Salesman, which this class is extending from
	@Override
	public IChromosome findOptimalPath(final Object a_initial_data) throws Exception {
		conf = createConfiguration(a_initial_data);
	    FitnessFunction myFunc = createFitnessFunction(a_initial_data);
	    conf.setFitnessFunction(myFunc);
	    IChromosome sampleChromosome = createSampleChromosome(a_initial_data);
	    conf.setSampleChromosome(sampleChromosome);
	    conf.setPopulationSize(getPopulationSize());
	    IChromosome[] chromosomes =
	        new IChromosome[conf.getPopulationSize()];
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
	    Evolution:
	        for (int i = 0; i < getMaxEvolution(); i++) {
	      population.evolve();
	      best = population.getFittestChromosome();
	      System.out.println(best.toString());
	      System.out.println("Score " + (Integer.MAX_VALUE / 2 - best.getFitnessValue()));
	    }
	    return best;
	}

	public static void main(String[] args) {
		try {
			Configuration conf = new Configuration();
			Vector<Point> data= new Vector<Point>();
			data.add(new Point(2,4));
			data.add(new Point(7,5));
			data.add(new Point(7,11));
			data.add(new Point(8,1));
			data.add(new Point(1,6));
			data.add(new Point(5,9));
			data.add(new Point(0,11));
			//Setup the Configuration
			//Specify the type of Crossover method
			conf.addGeneticOperator(new CycleCrossover(conf));
			//Specify the type of Mutation method
			//conf.addGeneticOperator(a_operatorToAdd);
			conf.setKeepPopulationSizeConstant(false);
			conf.setMinimumPopSizePercent(2);
			conf.setPopulationSize(data.size());
			//Initiliaze the process and Start the evolution
			TSGA t = new TSGA(data,conf);
			IChromosome optimal = t.findOptimalPath(null);
			System.out.println("Solution: ");
			System.out.println(optimal);
			System.out.println("Score " + (Integer.MAX_VALUE / 2 - optimal.getFitnessValue()));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

