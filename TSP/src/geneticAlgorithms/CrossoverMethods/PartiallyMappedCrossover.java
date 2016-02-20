package geneticAlgorithms.CrossoverMethods;

import java.util.List;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.GeneticOperator;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.impl.CrossoverOperator;

public class PartiallyMappedCrossover extends CrossoverOperator {

	private static final long serialVersionUID = -4782053395498733330L;

	public PartiallyMappedCrossover(Configuration a_configuration) throws InvalidConfigurationException {
		super(a_configuration);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator) {
		
		//Gene[] parentA = firstMate.getGenes();
		//Gene[] parentB = secondMate.getGenes();
		int sizeChromosome=firstMate.size();
	
		//Add the chromosomes to the candidate list
		a_candidateChromosomes.add(firstMate);
		a_candidateChromosomes.add(secondMate);
	}
}
