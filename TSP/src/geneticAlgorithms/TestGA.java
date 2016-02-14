package geneticAlgorithms;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;

public class TestGA {

	public static void main(String[] args) {
		try{
			Vector<Vector<Point>> resultsData=new Vector<Vector<Point>>();
			Vector<Point> cities= new Vector<Point>();
			Vector<Double> pathDistances=new Vector<Double>();
			Configuration conf = new Configuration();
			TSP_GA tsp;
			TSP_GA_Worker ft;

			cities.add(new Point(2,4)); 
			cities.add(new Point(7,5)); 
			cities.add(new Point(7,11)); 
			cities.add(new Point(8,1)); 
			cities.add(new Point(5,9)); 
			cities.add(new Point(0,11)); 
			cities.add(new Point(1,6));
			cities.add(new Point(2,7)); 
			cities.add(new Point(9,9)); 
			cities.add(new Point(0,8));
			cities.add(new Point(2232,43)); 
			cities.add(new Point(23,45)); 
			cities.add(new Point(45,21));
			cities.add(new Point(35,11232)); 
			cities.add(new Point(68,41235)); 
			cities.add(new Point(11233,46));
			cities.add(new Point(56,23)); 
			cities.add(new Point(21233,65)); 
			cities.add(new Point(45,12));
			//Setup the Configuration
			conf.setKeepPopulationSizeConstant(false);
			conf.setMinimumPopSizePercent(2);
			conf.setPopulationSize(cities.size());
			//Specify the type of Crossover method
			//conf.addGeneticOperator(new CycleCrossover(conf));
			//Specify the type of Mutation method
			//conf.addGeneticOperator(a_operatorToAdd);
			//Initialize the process and Start the evolution
			tsp= new TSP_GA(cities,conf,null,resultsData, pathDistances);
			ft=new TSP_GA_Worker(cities,conf,resultsData, pathDistances,600);
			ft.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if(ft.getProgress()==100 || ft.isDone()){
						System.out.println("Final Result:");
						int index=tsp.getBestPathIndex();
						System.out.println("Index: "+index+"\nBest Distance: "+pathDistances.elementAt(index));
						System.out.println("Elements: "+resultsData.elementAt(index).toString());
					}
					else{
						if(pathDistances.size()>0){
						System.out.println("In Progress: "+ pathDistances.size());
						System.out.println(resultsData.lastElement().toString()+"\nPath Distance: "+pathDistances.lastElement());
						}
					}
				}
			});
			ft.execute();
		} catch(Exception e){}
	}

	public static void updateBestResultSoFar(IChromosome ic, Vector<Point> cities){
		System.out.println(convertArrayResultToVectorPoint(ic.getGenes(), cities).toString());
		double score=(Integer.MAX_VALUE / 2 - ic.getFitnessValue());
		System.out.println("Score " + score);
	}

	private static Vector<Point> convertArrayResultToVectorPoint(Gene[] data, Vector<Point> cities){
		//Create some temporary object to store the data
		Vector<Point> points=new Vector<Point>();
		//Make a copy of the cities
		@SuppressWarnings("unchecked")
		Vector<Point> tempCities=(Vector<Point>) cities.clone();
		points.setSize(data.length);
		//This loop inserts the elements in the right position
		for(int i=0; i<data.length; i++){
			IntegerGene gene = (IntegerGene) data[i];
			int index=(int) gene.getAllele();
			Point element=tempCities.elementAt(index);
			points.set(i, element);
		}
		return points;
	}
}