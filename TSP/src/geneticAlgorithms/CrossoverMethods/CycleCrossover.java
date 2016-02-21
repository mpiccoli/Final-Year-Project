package geneticAlgorithms.CrossoverMethods;

import java.util.List;
import java.util.Vector;

import org.jgap.*;
import org.jgap.impl.CrossoverOperator;

public class CycleCrossover extends CrossoverOperator{
	private static final long serialVersionUID = 3110359141901206201L;

	public CycleCrossover(final Configuration a_configuration, final int a_desiredCrossoverRate) throws InvalidConfigurationException {
		super(a_configuration, a_desiredCrossoverRate);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator) {
		int sizeChromosome=firstMate.size();
		int elementsToTake=sizeChromosome/3;
		Vector<Integer> posElements=new Vector<Integer>();
		boolean inserted=false;
		//Generate randomly the position of the fixed values
		for(int i=0; i<elementsToTake-1; i++){
			while(!inserted){
				int elementIndex=(int)(Math.random()*sizeChromosome);
				if(posElements.size()>0){
					if(!posElements.contains(elementIndex)){
						posElements.add(elementIndex);
						inserted=true;
					}
				}
				else{
					posElements.add(elementIndex);
					inserted=true;
				}
			}
			inserted=false;
		}

		//Generate two children which are going to contain the chromosome swapped 
		Vector<Gene> child1=new Vector<Gene>();
		Vector<Gene> child2=new Vector<Gene>();
		for(int i=0; i<firstMate.size(); i++){
			child1.add(null);
			child2.add(null);
		}
		//Copy the elements stored in the vector at the same position as the parents
		for(int i=0; i<firstMate.size(); i++){
			if(!posElements.contains(i)){
				child1.setElementAt(firstMate.getGene(i),i);
				child2.setElementAt(secondMate.getGene(i),i);
			}
		}
		//Now copy the elements from the opposite parent to the child (where possible)
		for(int i=0; i<secondMate.size(); i++){
			if(!child1.contains(secondMate.getGene(i))){
				child1.setElementAt(secondMate.getGene(i),i);
			}
		}
		for(int i=0; i<firstMate.size(); i++){
			if(!child2.contains(firstMate.getGene(i))){
				child2.setElementAt(firstMate.getGene(i),i);
			}
		}
		//Add the remaining elements
		for(int i=0; i<secondMate.size(); i++){
			if(!child1.contains(secondMate.getGene(i))){
				//Find empty space
				int indexInsertion=-1;
				for(int j=0; j<child1.size(); j++){
					if(child1.elementAt(j)==null){
						indexInsertion=j;
						break;
					}
				}
				if(indexInsertion!=-1){
					child1.setElementAt(secondMate.getGene(i), indexInsertion);
				}
			}
		}
		for(int i=0; i<firstMate.size(); i++){
			if(!child2.contains(firstMate.getGene(i))){
				//Find empty space
				int indexInsertion=-1;
				for(int j=0; j<child2.size(); j++){
					if(child2.elementAt(j)==null){
						indexInsertion=j;
						break;
					}
				}
				if(indexInsertion!=-1){
					child2.setElementAt(firstMate.getGene(i),indexInsertion);
				}
			}
		}

		//Convert Vector<Gene> to Gene[] for then passing it as a setting to the IChromosome firstMate and secondMate
		Gene[] child1Array=new Gene[firstMate.size()];
		Gene[] child2Array=new Gene[secondMate.size()];
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
