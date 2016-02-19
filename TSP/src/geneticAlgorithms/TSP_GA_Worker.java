package geneticAlgorithms;

import java.awt.Point;
import java.util.Vector;
import javax.swing.SwingWorker;
import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;

@SuppressWarnings("rawtypes")
public class TSP_GA_Worker extends SwingWorker{
	
	private TSP_GA_Adapter work;
	private long executionTime;
	private boolean processedCancelled;
	private String crossoverChosen,mutationChosen;
	
	public TSP_GA_Worker(Vector<Point> cities, Vector<Vector<Point>> results, Vector<Double> distances, int maxGen, String crossover, String mutation) throws InvalidConfigurationException{
		crossoverChosen=crossover;
		mutationChosen=mutation;
		work=new TSP_GA_Adapter(this, cities,results, distances, maxGen,crossoverChosen,mutationChosen);
		processedCancelled=false;
	}
	
	private void calculateExecutionTime(long startTime, long finishTime){
		//Verify that the Thread has not been stopped
		if(!processedCancelled){
			executionTime=(finishTime-startTime);
		}
		else{
			executionTime=-1;
		}
	}

	@Override
	protected Object doInBackground(){
		//Start the time for this process
		long startTime=System.currentTimeMillis();
		try{
			work.startEvolution();
			if(this.isCancelled()){
				processedCancelled=true;
				work.stopExecution();
				this.updateProgress(100);
				this.cancel(true);
				return null;
				//this.cancel(true);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		this.calculateExecutionTime(startTime, System.currentTimeMillis());
		this.done();
		return null;
	}
	
	public void updateProgress(int v){
		this.setProgress(v);
	}
	public long getExecutionTime(){
		return executionTime;
	}
}
