package geneticAlgorithms.CrossoverMethods;

import java.util.List;
import java.util.Vector;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.impl.CrossoverOperator;

public class OrderedCrossover extends CrossoverOperator{

	private static final long serialVersionUID = -3205548027106797115L;

	public OrderedCrossover(Configuration a_configuration) throws InvalidConfigurationException {
		super(a_configuration);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator) {

		Gene[] parentA = firstMate.getGenes();
		Gene[] parentB = secondMate.getGenes();
		int sizeChromosome=firstMate.size();
		//The fixed elements are 1/3 all the actual chromosome
		int numberFixedElements=sizeChromosome/3;

		//Determine the position of the elements that are going to be kept fixed in the children
		boolean done=false;
		int startIndex=0;
		while(!done){
			int indexFirstElement=(int)(Math.random()*sizeChromosome);
			//if the current randomized index + the number of fixed elements is less than the size of the chromosome, 
			//that means the scope is valid, therefore proceed with it, otherwise try again with another randomized index
			if(indexFirstElement+numberFixedElements<(sizeChromosome-1) && indexFirstElement!=0){
				startIndex=indexFirstElement;
				done=true;
			}
		}
		//Create the two children which are going to contain the chromosome swapped 
		//Set their value to null for an easier manipulation later on with the developement of this crossover
		Vector<Gene> child1=new Vector<Gene>();
		Vector<Gene> child2=new Vector<Gene>();
		for(int i=0; i<firstMate.size(); i++){
			child1.add(null);
			child2.add(null);
		}
		//Copy the fixed elements containing within the scope found earlier from the parents to the children
		for(int i=startIndex; i<(startIndex+numberFixedElements); i++){
			child1.setElementAt(firstMate.getGene(i), i);
			child2.setElementAt(secondMate.getGene(i), i);
		}

		//The following loop fills the end-side of child1 with the elements from parentB that are not included yet
		boolean endChromosomeDone=false;
		int currentIndexInsertionChild=startIndex+numberFixedElements;
		int currentPosParent=startIndex+numberFixedElements;
		int elementsInChild=numberFixedElements;
		while(!endChromosomeDone){
			if(!child1.contains(secondMate.getGene(currentPosParent))){
				child1.setElementAt(secondMate.getGene(currentPosParent), currentIndexInsertionChild);
				//currentPosParentB++;
				currentIndexInsertionChild++;
				elementsInChild++;
			}
			if(currentPosParent==secondMate.size()-1){
				currentPosParent=0;
			}
			else{
				currentPosParent++;
			}
			if(currentIndexInsertionChild>secondMate.size()-1){
				currentIndexInsertionChild=0;
			}
			if(elementsInChild==secondMate.size()){
				endChromosomeDone=true;
			}
		}
		//The following loop fills the end-side of child2 with the elements from parentA that are not included yet
		endChromosomeDone=false;
		currentIndexInsertionChild=startIndex+numberFixedElements;
		currentPosParent=startIndex+numberFixedElements;
		elementsInChild=numberFixedElements;
		while(!endChromosomeDone){
			if(!child2.contains(firstMate.getGene(currentPosParent))){
				child2.setElementAt(firstMate.getGene(currentPosParent), currentIndexInsertionChild);
				//currentPosParentB++;
				currentIndexInsertionChild++;
				elementsInChild++;
			}
			if(currentPosParent==firstMate.size()-1){
				currentPosParent=0;
			}
			else{
				currentPosParent++;
			}
			if(currentIndexInsertionChild>firstMate.size()-1){
				currentIndexInsertionChild=0;
			}
			if(elementsInChild==firstMate.size()){
				endChromosomeDone=true;
			}
		}
		//System.out.println(firstMate.toString());
		//System.out.println(secondMate.toString());
		//System.out.println(child1.toString());
		//System.out.println(child2.toString());

		//Convert Vector<Gene> to Gene[] for then passing it as a setting to the IChromosome firstMate and secondMate
		Gene[] child1Array=new Gene[firstMate.size()];
		Gene[] child2Array=new Gene[secondMate.size()];
		//IT DOES NOT REMOVE ALL THE NULL, THEREFORE THE APPLICATION STOPS WORKING
		//FIND A WAY TO ASSIGN PARENTS' VALUES CORRECTLY (.SET/.SETELEMENTAT.....)-->FIND THE RIGHT ONE
		for(int i=0; i<child1.size(); i++){
			if(!child1.get(i).equals(null)){
				child1Array[i]=child1.get(i);
			}
			if(!child2.get(i).equals(null)){
				child2Array[i]=child2.get(i);
			}
		}
		try {
			firstMate.setGenes(child1Array);
			secondMate.setGenes(child2Array);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		//Add the chromosomes to the candidate list
		a_candidateChromosomes.add(firstMate);
		a_candidateChromosomes.add(secondMate);
	}
}
