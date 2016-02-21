package geneticAlgorithms.CrossoverMethods;

import java.awt.Point;
import java.util.List;
import java.util.Vector;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.IntegerGene;

public class ModifiedSequentialConstructiveCrossover extends CrossoverOperator{

	private static final long serialVersionUID = -4600861017821822165L;
	private Vector<Point> citiesData;

	@SuppressWarnings("unchecked")
	public ModifiedSequentialConstructiveCrossover(final Configuration a_configuration, final int a_desiredCrossoverRate, Vector<Point> cities) throws InvalidConfigurationException {
		super(a_configuration, a_desiredCrossoverRate);
		citiesData=(Vector<Point>) cities.clone();
	}

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
			Vector<Double> tempDistances= new Vector<Double>();
			for(int i=0; i<parentA.size(); i++){
				tempDistances.add(distance(child1.elementAt(indexChild1), parentA.get(i)));
			}
			int pos=this.findCurrentShortestPath(tempDistances);
			child1.add(parentA.get(pos));
			parentA.remove(pos);
			indexChild1++;
		}

		int indexChild2=0;
		while(parentB.size()>0){
			Vector<Double> tempDistances= new Vector<Double>();
			for(int i=0; i<parentB.size(); i++){
				tempDistances.add(distance(child2.elementAt(indexChild2), parentB.get(i)));
			}
			int pos=this.findCurrentShortestPath(tempDistances);
			child2.add(parentB.get(pos));
			parentB.remove(pos);
			indexChild2++;
		}
		//Add the chromosomes to the candidate list
		a_candidateChromosomes.add(firstMate);
		a_candidateChromosomes.add(secondMate);
	}

	private double distance(Gene a_from, Gene a_to) {
		IntegerGene geneA = (IntegerGene) a_from;
		IntegerGene geneB = (IntegerGene) a_to;
		int a = geneA.intValue();
		int b = geneB.intValue();
		double xValue=(citiesData.elementAt(a).getX()-citiesData.elementAt(b).getX())*(citiesData.elementAt(a).getX()-citiesData.elementAt(b).getX());
		double yValue=(citiesData.elementAt(a).getY()-citiesData.elementAt(b).getY())*(citiesData.elementAt(a).getY()-citiesData.elementAt(b).getY());
		return Math.sqrt(xValue+yValue);
	}

	private Vector<Gene> convertArrayGeneToVectorGene(Gene[] data){
		//Create some temporary object to store the data
		Vector<Gene> tempGenes=new Vector<Gene>();
		tempGenes.setSize(data.length);
		//This loop inserts the elements in the right position
		for(int i=0; i<data.length; i++){
			IntegerGene gene = (IntegerGene) data[i];
			tempGenes.set(i, gene);
		}
		return tempGenes;
	}

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
		return -2;
	}

}