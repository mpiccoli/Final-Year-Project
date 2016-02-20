package geneticAlgorithms.CrossoverMethods;

import java.util.List;
import java.util.Vector;

import org.jgap.*;
import org.jgap.impl.CrossoverOperator;

public class CycleCrossover extends CrossoverOperator{
	private static final long serialVersionUID = 3110359141901206201L;

	public CycleCrossover(Configuration a_configuration) throws InvalidConfigurationException {
		super(a_configuration);
	}
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator) {
		//System.out.println("Crossover ----------------------------");
		//System.out.println("firstMate: "+firstMate.toString());
		//System.out.println("secondMate: "+secondMate.toString());
		//System.out.println("List of candidates: "+a_candidateChromosomes.toString());
		//Gene[] parentA = firstMate.getGenes();
		//Gene[] parentB = secondMate.getGenes();
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
		//child1.setSize(firstMate.size());
		for(int i=0; i<firstMate.size(); i++){
			child1.add(null);
			child2.add(null);
		}
		//child2.setSize(secondMate.size());
		//Gene[] child1=new Gene[firstMate.size()];
		//Gene[] child2=new Gene[firstMate.size()];
		//Copy the elements stored in the vector at the same position as the parents
		for(int i=0; i<firstMate.size(); i++){
			if(!posElements.contains(i)){
				child1.setElementAt(firstMate.getGene(i),i);
				child2.setElementAt(secondMate.getGene(i),i);
				//child1[i]=firstMate.getGene(i);
				//child2[i]=secondMate.getGene(i);
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
		//Now that we have swapped the elements, let's copy the genes inside the two chromosomes for then pass them back to the execution
		//System.out.println("First Mate: "+firstMate.toString());
		//System.out.println("Second Mate: "+secondMate.toString());
		//System.out.println("Indexes: "+posElements.toString());
		//System.out.println("Child1: "+child1.toString());
		//System.out.println("Child2: "+child2.toString());

		
		/*
		//Generate two children which are going to contain the chromosome swapped 
		Vector<Gene> child1=new Vector<Gene>();
		Vector<Gene> child2=new Vector<Gene>();
		//child1.setSize(firstMate.size());
		// First add the elements that are fixed from the parents
		for (int i=0; i<firstMate.size(); i++) {
			if (posElements.contains(i)) {
				child1.add(firstMate.getGene(i));
				child2.add(secondMate.getGene(i));
			} else {
				child1.add(null);
				child2.add(null);
			}
		}
		// Then, where possible swap the elements from the parents
		for (int i=0; i<firstMate.size(); i++) {
			if (child1.elementAt(i) == null) {
				Gene g1 = secondMate.getGene(i);
				Gene g2 = firstMate.getGene(i);
				if (!child1.contains(g1))
					child1.setElementAt(g1, i);
				else
					child1.setElementAt(g2,i);
				if (!child2.contains(g2))
					child2.setElementAt(g2, i);
				else
					child2.setElementAt(g1, i);
			}
		}*/







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

		/*int locus = generator.nextInt(parentA.length);
	    Gene gene1;
	    Gene gene2;
	    Object firstAllele;
	    for (int j = locus; j < parentA.length; j++) {
	      // Make a distinction for ICompositeGene for the first gene.
	      // ---------------------------------------------------------
	      int index = 0;
	      if (parentA[j] instanceof ICompositeGene) {
	        // Randomly determine gene to be considered.
	        index = generator.nextInt(parentA[j].size());
	        //System.out.println("Index "+index);
	        gene1 = ( (ICompositeGene) parentA[j]).geneAt(index);
	        //System.out.println("Gene1 for index "+gene1.toString());
	      }
	      else {
	        gene1 = parentA[j];
	      }
	      // Make a distinction for the second gene if CompositeGene.
	      // --------------------------------------------------------
	      if (parentB[j] instanceof ICompositeGene) {
	        gene2 = ( (ICompositeGene) parentB[j]).geneAt(index);
	        //System.out.println("Gene2 for index"+gene2.toString());
	      }
	      else {
	        gene2 = parentB[j];
	      }
	      if (m_monitorActive) {
	        gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
	        gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
	      }
	      //Swap values from gene1 and gene2
	      firstAllele = gene1.getAllele();
	      gene1.setAllele(gene2.getAllele());
	      gene2.setAllele(firstAllele);
	      //System.out.println("Gene1 Swapped"+gene1.toString());
	      //System.out.println("Gene2 Swapped"+gene2.toString());
	    }*/
		// Add the modified chromosomes to the candidate pool so that
		// they'll be considered for natural selection during the next
		// phase of evolution.
		// -----------------------------------------------------------
		//a_candidateChromosomes.add(firstMate);
		//a_candidateChromosomes.add(secondMate);


		//Add the chromosomes to the candidate list
		a_candidateChromosomes.add(firstMate);
		a_candidateChromosomes.add(secondMate);

		//System.out.println("End of Method N of candidates: "+a_candidateChromosomes.size());
	}
}
