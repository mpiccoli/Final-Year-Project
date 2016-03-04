package geneticAlgorithms.CrossoverMethods;

import java.util.List;
import java.util.Vector;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.impl.CrossoverOperator;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see List, Vector, CrossoverOperator, Configuration, IChromosome, List, RandomGenerator
 * 
 * This class is an extension of the CrossoverOperator class which performs actions on children contained within the population, in order to
 * create some diverse chromosomes to add to the next generation. 
 * 
 */
public class OrderedCrossover extends CrossoverOperator {

	// Define the constants of this class
	private static final long serialVersionUID = -3205548027106797115L;

	/*
	 * @param a_configuration Configuration object
	 * 
	 * @param a_desiredCrossoverRate Integer value indicating the probability of
	 * execution of this crossover operator
	 * 
	 * @throws InvalidConfigurationException Throw this error when the
	 * configuration object is not correctly set
	 * 
	 * Constructor with 2 parameters
	 */
	public OrderedCrossover(final Configuration a_configuration, final int a_desiredCrossoverRate)
			throws InvalidConfigurationException {
		super(a_configuration, a_desiredCrossoverRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgap.impl.CrossoverOperator#doCrossover(org.jgap.IChromosome,
	 * org.jgap.IChromosome, java.util.List, org.jgap.RandomGenerator)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes,
			RandomGenerator generator) {

		// Estimate the size of the chromosome
		int sizeChromosome = firstMate.size();
		// The fixed elements are 1/3 of the actual chromosome
		int numberFixedElements = sizeChromosome / 3;

		// Determine the position of the elements that are going to be kept
		// fixed in the children
		boolean done = false;
		int startIndex = 0;
		while (!done) {
			int indexFirstElement = (int) (Math.random() * sizeChromosome);
			// if the current randomized index + the number of fixed elements is
			// less than the size of the chromosome,
			// that means the scope is valid, therefore proceed with it,
			// otherwise try again with another randomized index
			if (indexFirstElement + numberFixedElements < (sizeChromosome - 1) && indexFirstElement != 0) {
				startIndex = indexFirstElement;
				done = true;
			}
		}
		// Create the two children which are going to contain the chromosome
		// swapped
		// Set their value to null for an easier manipulation later on with the
		// development of this crossover
		Vector<Gene> child1 = new Vector<Gene>();
		Vector<Gene> child2 = new Vector<Gene>();
		for (int i = 0; i < firstMate.size(); i++) {
			child1.add(null);
			child2.add(null);
		}
		// Copy the fixed elements containing within the scope found earlier
		// from the parents to the children
		for (int i = startIndex; i < (startIndex + numberFixedElements); i++) {
			child1.setElementAt(firstMate.getGene(i), i);
			child2.setElementAt(secondMate.getGene(i), i);
		}

		// The following loop fills the end-side of child1 with the elements
		// from parentB that are not included yet
		boolean endChromosomeDone = false;
		int currentIndexInsertionChild = startIndex + numberFixedElements;
		int currentPosParent = startIndex + numberFixedElements;
		int elementsInChild = numberFixedElements;
		while (!endChromosomeDone) {
			// Add element only if the child does not contain that gene already
			if (!child1.contains(secondMate.getGene(currentPosParent))) {
				child1.setElementAt(secondMate.getGene(currentPosParent), currentIndexInsertionChild);
				// Increase the index value of the child once an element has
				// been added
				currentIndexInsertionChild++;
				// Increase the index storing the number of genes inside a child
				elementsInChild++;
			}
			// Set the index of the parent 0 when the parent reaches the end of
			// the array, increase the index otherwise
			if (currentPosParent == secondMate.size() - 1) {
				currentPosParent = 0;
			} else {
				currentPosParent++;
			}
			// Set the index of the child 0 when the child reaches the end of
			// the vector
			if (currentIndexInsertionChild > secondMate.size() - 1) {
				currentIndexInsertionChild = 0;
			}
			// If the child has got the same number of genes as the parents, it
			// means it is completed and the loop can now end
			if (elementsInChild == secondMate.size()) {
				endChromosomeDone = true;
			}
		}
		// The following loop fills the end-side of child2 with the elements
		// from parentA that are not included yet
		endChromosomeDone = false;
		currentIndexInsertionChild = startIndex + numberFixedElements;
		currentPosParent = startIndex + numberFixedElements;
		elementsInChild = numberFixedElements;
		while (!endChromosomeDone) {
			// Add element only if the child does not contain that gene already
			if (!child2.contains(firstMate.getGene(currentPosParent))) {
				child2.setElementAt(firstMate.getGene(currentPosParent), currentIndexInsertionChild);
				// Increase the index value of the child once an element has
				// been added
				currentIndexInsertionChild++;
				// Increase the index storing the number of genes inside a child
				elementsInChild++;
			}
			// Set the index of the parent 0 when the parent reaches the end of
			// the array, increase the index otherwise
			if (currentPosParent == firstMate.size() - 1) {
				currentPosParent = 0;
			} else {
				currentPosParent++;
			}
			// Set the index of the child 0 when the child reaches the end of
			// the vector
			if (currentIndexInsertionChild > firstMate.size() - 1) {
				currentIndexInsertionChild = 0;
			}
			// If the child has got the same number of genes as the parents, it
			// means it is completed and the loop can now end
			if (elementsInChild == firstMate.size()) {
				endChromosomeDone = true;
			}
		}
		// Convert Vector<Gene> to an array of type Gene for then passing it as
		// a setting to the IChromosome firstMate and secondMate
		Gene[] child1Array = new Gene[firstMate.size()];
		Gene[] child2Array = new Gene[secondMate.size()];
		for (int i = 0; i < child1.size(); i++) {
			if (!child1.get(i).equals(null)) {
				child1Array[i] = child1.get(i);
			}
			if (!child2.get(i).equals(null)) {
				child2Array[i] = child2.get(i);
			}
		}
		try {
			firstMate.setGenes(child1Array);
			secondMate.setGenes(child2Array);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		// Add the chromosomes to the candidate list
		a_candidateChromosomes.add(firstMate);
		a_candidateChromosomes.add(secondMate);
	}
}
