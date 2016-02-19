package geneticAlgorithms;

import java.awt.Point;
import java.util.Vector;
import org.jgap.Configuration;
import org.jgap.IChromosome;

public class TSP_GA_Adapter {
	private TSP_GA tsp;

	private TSP_GA_Worker tspWorker;
	@SuppressWarnings("unused")
	private IChromosome bestPath;
	private Vector<Point> points;
	private Vector<Vector<Point>> resultsData;
	private Vector<Double> pathDistances;
	private int maxGeneration;
	private String crossoverChosen,mutationChosen;

	public TSP_GA_Adapter(TSP_GA_Worker fw, Vector<Point> cities, Vector<Vector<Point>> results, Vector<Double> distances, int maxGen, String crossover, String mutation){
		tspWorker=fw;
		points=cities;
		resultsData=results;
		pathDistances=distances;
		maxGeneration=maxGen;
		crossoverChosen=crossover;
		mutationChosen=mutation;
	}

	public Object startEvolution(){
		if(points!=null && resultsData!=null){
			try {
				tsp= new TSP_GA(points,this,resultsData, pathDistances,crossoverChosen,mutationChosen);
				tsp.setTSP_Adapter(this);
				tsp.setMaxEvolution(maxGeneration);
				bestPath = tsp.findOptimalPath(null);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else{ 
			System.out.println("Error, values are null"); 
		}
		updateProgress(100);
		return null;
	}

	public void updateProgress(int value){
		tspWorker.updateProgress(value);
	}
	public void stopExecution(){
		tsp.stopExecution();
	}
}
