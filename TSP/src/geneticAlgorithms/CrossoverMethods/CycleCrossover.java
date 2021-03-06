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
public class CycleCrossover extends CrossoverOperator {

	// Define the constants of this class
	private static final long serialVersionUID = 3110359141901206201L;

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
	 * 
	 */
	public CycleCrossover(final Configuration a_configuration, final int a_desiredCrossoverRate)
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
		// Identify the size of the chromosome
		int sizeChromosome = firstMate.size();
		// Define how many elements are not going to be moved around within this
		// crossover
		int elementsToTake = sizeChromosome / 3;
		Vector<Integer> posElements = new Vector<Integer>();
		boolean inserted = false;
		// Generate randomly the position of the fixed values
		for (int i = 0; i < elementsToTake - 1; i++) {
			while (!inserted) {
				int elementIndex = (int) (Math.random() * sizeChromosome);
				if (posElements.size() > 0) {
					if (!posElements.contains(elementIndex)) {
						posElements.add(elementIndex);
						inserted = true;
					}
				} else {
					posElements.add(elementIndex);
					inserted = true;
				}
			}
			inserted = false;
		}

		// Generate two children which are going to contain the chromosome
		// swapped
		Vector<Gene> child1 = new Vector<Gene>();
		Vector<Gene> child2 = new Vector<Gene>();
		// Set the initial values for both children to null
		for (int i = 0; i < firstMate.size(); i++) {
			child1.add(null);
			child2.add(null);
		}
		// Copy the elements stored in the vector at the same position as the
		// parents
		for (int i = 0; i < firstMate.size(); i++) {
			if (!posElements.contains(i)) {
				child1.setElementAt(firstMate.getGene(i), i);
				child2.setElementAt(secondMate.getGene(i), i);
			}
		}
		// Now copy the elements from the opposite parent into the child (where
		// possible)
		for (int i = 0; i < secondMate.size(); i++) {
			if (!child1.contains(secondMate.getGene(i))) {
				child1.setElementAt(secondMate.getGene(i), i);
			}
		}
		for (int i = 0; i < firstMate.size(); i++) {
			if (!child2.contains(firstMate.getGene(i))) {
				child2.setElementAt(firstMate.getGene(i), i);
			}
		}
		// Add the remaining elements
		for (int i = 0; i < secondMate.size(); i++) {
			if (!child1.contains(secondMate.getGene(i))) {
				// Find empty space
				int indexInsertion = -1;
				for (int j = 0; j < child1.size(); j++) {
					if (child1.elementAt(j) == null) {
						indexInsertion = j;
						break;
					}
				}
				// Insert element in the set position
				if (indexInsertion != -1) {
					child1.setElementAt(secondMate.getGene(i), indexInsertion);
				}
			}
		}
		for (int i = 0; i < firstMate.size(); i++) {
			if (!child2.contains(firstMate.getGene(i))) {
				// Find empty space
				int indexInsertion = -1;
				for (int j = 0; j < child2.size(); j++) {
					if (child2.elementAt(j) == null) {
						indexInsertion = j;
						break;
					}
				}
				// Insert element in the set position
				if (indexInsertion != -1) {
					child2.setElementAt(firstMate.getGene(i), indexInsertion);
				}
			}
		}

		// Convert Vector<Gene> to an array of Gene for then passing it as a
		// setting to the IChromosome firstMate and secondMate
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
		}
		// Catch any error may occur
		catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		// Add the chromosomes to the candidate list
		a_candidateChromosomes.add(firstMate);
		a_candidateChromosomes.add(secondMate);
	}
}
