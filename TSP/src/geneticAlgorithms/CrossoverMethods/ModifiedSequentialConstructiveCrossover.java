package geneticAlgorithms.CrossoverMethods;

import java.awt.Point;
import java.util.List;
import java.util.Vector;
import org.jgap.*;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.IntegerGene;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see List, Vector, Point, CrossoverOperator, Configuration, IChromosome, List, RandomGenerator, IntegerGene
 * 
 * This class is an extension of the CrossoverOperator class which performs actions on children contained within the population, in order to
 * create some diverse chromosomes to add to the next generation. 
 * 
 */
public class ModifiedSequentialConstructiveCrossover extends CrossoverOperator{

	//Define the constants of this class
	private static final long serialVersionUID = -4600861017821822165L;
	//Global Variables
	private Vector<Point> citiesData;

	/*
	 * @param a_configuration	Configuration object
	 * @param a_desiredCrossoverRate	Integer value indicating the probability of execution of this crossover operator
	 * @param cities	Vector containing the list of cities for the TSP tour
	 * 
	 * @throws InvalidConfigurationException	Throw this error when the configuration object is not correctly set
	 * 
	 * Constructor with 3 parameters
	 */
	@SuppressWarnings("unchecked")
	public ModifiedSequentialConstructiveCrossover(final Configuration a_configuration, final int a_desiredCrossoverRate, Vector<Point> cities) throws InvalidConfigurationException {
		super(a_configuration, a_desiredCrossoverRate);
		//Make an exact copy of the vector data
		citiesData=(Vector<Point>) cities.clone();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jgap.impl.CrossoverOperator#doCrossover(org.jgap.IChromosome, org.jgap.IChromosome, java.util.List, org.jgap.RandomGenerator)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator) {

		//Export the elements from a chromosome object to a vector object
		Vector<Gene> parentA=this.convertArrayGeneToVectorGene(firstMate.getGenes());
		Vector<Gene> parentB=this.convertArrayGeneToVectorGene(secondMate.getGenes());

		//Create the two children which are going to contain the new set of chromosomes 
		//Copy a randomized value from the parents
		Vector<Gene> child1=new Vector<Gene>();
		Vector<Gene> child2=new Vector<Gene>();
		int startingIndex=(int)(Math.random()*firstMate.size());
		child1.add(firstMate.getGene(startingIndex));
		child2.add(secondMate.getGene(startingIndex));
		//Remove the elements just copied from the parents
		parentA.remove(startingIndex);
		parentB.remove(startingIndex);

		
		int indexChild1=0;
		while(parentA.size()>0){
			//Find the closest element to the previous element in the vector child
			Vector<Double> tempDistances= new Vector<Double>();
			for(int i=0; i<parentA.size(); i++){
				tempDistances.add(distance(child1.elementAt(indexChild1), parentA.get(i)));
			}
			//Extract the index of the closest element
			int pos=this.findCurrentShortestPath(tempDistances);
			//Add this element to the child
			child1.add(parentA.get(pos));
			//Remove this element from the parent vector
			parentA.remove(pos);
			indexChild1++;
		}

		int indexChild2=0;
		while(parentB.size()>0){
			//Find the closest element to the previous element in the vector child
			Vector<Double> tempDistances= new Vector<Double>();
			for(int i=0; i<parentB.size(); i++){
				tempDistances.add(distance(child2.elementAt(indexChild2), parentB.get(i)));
			}
			//Extract the index of the closest element
			int pos=this.findCurrentShortestPath(tempDistances);
			//Add this element to the child
			child2.add(parentB.get(pos));
			//Remove this element from the parent vector
			parentB.remove(pos);
			indexChild2++;
		}
		
		//Add the chromosomes to the candidate list
		a_candidateChromosomes.add(firstMate);
		a_candidateChromosomes.add(secondMate);
	}

	/*
	 * @param a_from	Gene element
	 * @param a_to	Gene element
	 * 
	 * @return double	Return the distance value of two points
	 * 
	 * This method will calculate the distance between the two genes given their X and Y coordinates
	 */
	private double distance(Gene a_from, Gene a_to) {
		//Convert the current Gene to IntegerGene objects
		IntegerGene geneA = (IntegerGene) a_from;
		IntegerGene geneB = (IntegerGene) a_to;
		//Extact the integer value from the IntegerGene objects
		int a = geneA.intValue();
		int b = geneB.intValue();
		//Calculate the distance between the two points
		double xValue=(citiesData.elementAt(a).getX()-citiesData.elementAt(b).getX())*(citiesData.elementAt(a).getX()-citiesData.elementAt(b).getX());
		double yValue=(citiesData.elementAt(a).getY()-citiesData.elementAt(b).getY())*(citiesData.elementAt(a).getY()-citiesData.elementAt(b).getY());
		return Math.sqrt(xValue+yValue);
	}

	/*
	 * @param data	array of type Gene 
	 * 
	 * @return Vector<Gene>	Return the vector of gene created with the data from the array of Gene
	 * 
	 * This method converts the array of Gene into a Vector of Gene
	 */
	private Vector<Gene> convertArrayGeneToVectorGene(Gene[] data){
		//Create some temporary object to store the data and set its size
		Vector<Gene> tempGenes=new Vector<Gene>();
		tempGenes.setSize(data.length);
		//This loop inserts the elements in the right position of the vector
		for(int i=0; i<data.length; i++){
			IntegerGene gene = (IntegerGene) data[i];
			tempGenes.set(i, gene);
		}
		return tempGenes;
	}

	/*
	 * @param results	Vector of type double containing all the distances between one point and all the other elements in the chromosome
	 * 
	 * @return int	Return the index of the smallest value found within the vector
	 * 
	 * This method searches for the the smallest distance and return the index of the value
	 */
	private int findCurrentShortestPath(Vector<Double> results){
		int index=0;
		if(results.size()>0){
			double max=Integer.MAX_VALUE;
			for(int i=0; i<results.size(); i++){
				if(results.elementAt(i)<max && results.elementAt(i)!=-1){
					max=results.elementAt(i);
					index=i;
				}
			}
			return index;
		}
		//Return -2 when the vector hasn't got enough data
		return -2;
	}

}
