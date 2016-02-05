package geneticAlgorithms.Crossover;

import java.util.List;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.impl.salesman.*;

public class CycleCrossover extends BaseGeneticOperator {

	public CycleCrossover(Configuration a_configuration) throws InvalidConfigurationException {
		super(a_configuration);
	}

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