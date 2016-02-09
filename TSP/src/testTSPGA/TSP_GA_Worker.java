package testTSPGA;

import java.awt.Point;
import java.util.Vector;

import javax.swing.SwingWorker;

import org.jgap.Configuration;
import org.jgap.IChromosome;

import geneticAlgorithms.TSP_GA;

public class TSP_GA_Worker extends SwingWorker{
	
	private TSP_GA_Adapter work;
	
	public TSP_GA_Worker(TSP_GA tspGaRef, Vector<Point> cities, Configuration c, Vector<Vector<Point>> results, Vector<Double> distances, int maxGen){
		work=new TSP_GA_Adapter(this, cities,c,results, distances, maxGen);
	}

	@Override
	protected Object doInBackground(){
		try{
			work.startEvolution();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		this.done();
		return null;
	}
	
	public void updateProgress(int v){
		this.setProgress(v);
	}
}
