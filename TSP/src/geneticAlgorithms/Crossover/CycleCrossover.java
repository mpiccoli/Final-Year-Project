package geneticAlgorithms.Crossover;

import java.util.List;
import org.jgap.*;

public class CycleCrossover extends BaseGeneticOperator {

	private static final long serialVersionUID = 3909572788365873008L;

	public CycleCrossover(Configuration a_configuration) throws InvalidConfigurationException {
		super(a_configuration);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void operate(Population arg0, List arg1) {
		//To implement the cycle crossover structure
		//At the minute, it just takes every city and make them available to be picked
		int len = arg0.size(); 
        for ( int i = 0; i < len; i++ ) 
        {
            arg1.add( arg0.getChromosome(i).clone() ); 
        }
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

}
