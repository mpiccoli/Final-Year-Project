package geneticAlgorithms.CrossoverMethods;

import java.util.List;
import org.jgap.*;

public class CycleCrossover implements GeneticOperator {

	private static final long serialVersionUID = -2139868815300620378L;

	public void operate(Population arg0, List arg1) {
		//To implement the cycle crossover structure
		//At the minute, it just takes every city and make them available to be picked
		//The population is the current set of chromosomes in the current evolution process
		//The List is the elements that are picked and used in the following evolution because considered more fit
		int len = arg0.size(); 
        for ( int i = 0; i < len; i++ ) 
        {
        	if(i%2==0)
            arg1.add( arg0.getChromosome(i).clone() ); 
        }
	}

}
