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
	private Configuration config;
	private Vector<Vector<Point>> resultsData;
	private Vector<Double> pathDistances;
	private int maxGeneration;

	public TSP_GA_Adapter(TSP_GA_Worker fw, Vector<Point> cities, Configuration c, Vector<Vector<Point>> results, Vector<Double> distances, int maxGen){
		tspWorker=fw;
		points=cities;
		config=c;
		resultsData=results;
		pathDistances=distances;
		maxGeneration=maxGen;
	}

	public Object startEvolution(){
		if(points!=null && config!=null && resultsData!=null){
			try {
				tsp= new TSP_GA(points,config,this,resultsData, pathDistances);
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