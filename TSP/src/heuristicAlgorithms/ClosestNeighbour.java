package heuristicAlgorithms;

import java.awt.Point;
import java.util.Vector;

import javax.swing.SwingWorker;

@SuppressWarnings("rawtypes")
public class ClosestNeighbour<DrawingPanel> extends SwingWorker{
	//Global Variables
	private Vector<Point> cities;
	private Vector<Point> travellingOrder;
	private long executionTime;
	private int tourLength;
	private double tourDistance;
	private boolean processedCancelled;
	
	public ClosestNeighbour(Vector<Point> points, Vector<Point> solutions){
		cities=points;
		travellingOrder=solutions;
		tourLength=1;
		tourDistance=0;
		processedCancelled=false;
	}
	public long getExecutionTime(){
		return executionTime;
	}
	public int getTourLength(){
		return tourLength;
	}
	public double getTourDistance(){
		return tourDistance;
	}
	public Vector<Point> getListOfCities(){
		return (Vector<Point>) cities.clone();
	}
	public Vector<Point> getTravellingOrder(){
		return (Vector<Point>)travellingOrder.clone();
	}
	private double calculateDistance(Point a, Point b){
		double xValue=(a.getX()-b.getX())*(a.getX()-b.getX());
		double yValue=(a.getY()-b.getY())*(a.getY()-b.getY());
		return Math.sqrt(xValue+yValue);
	}
	private int findPositionMinDistance(Vector<Double> data){
		Double minTemp=1000.0;
		int pos=0;
		for(int i=0; i<data.size(); i++){
			if(data.get(i)<=minTemp){
				minTemp=data.get(i);
				pos=i;
			}
		}
		tourDistance+=minTemp;
		return pos;
	}
	
	private void calculateExecutionTime(long startTime, long finishTime){
		//Verify that the Thread has not been stopped
		if(!processedCancelled){
			executionTime=(finishTime-startTime)/1000000;
		}
		else{
			executionTime=-1;
		}
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		//Start the time for this process
		long startTime=System.nanoTime();
		Vector<Point> citiesTemp=(Vector<Point>) cities.clone();
		//The first place to visit is the first city in the list
		travellingOrder.add(citiesTemp.get(0));
		//Once the city has been added to the path, remove it from the available cities
		citiesTemp.remove(0);
		//Pause the Thread for a few millisecond, to give the opportunity the GUI to pick up the changes
		Thread.sleep(2);
		//Send an update to notify that the program is performing correctly
		this.setProgress((int)(Math.random()*10));
		while(citiesTemp.size()>0){
			Vector<Double> temp= new Vector<Double>();
			for(int j=0; j<citiesTemp.size(); j++){
				temp.add(calculateDistance(travellingOrder.lastElement(), citiesTemp.get(j)));
				this.setProgress((int)(Math.random()*10));
				//Pause the Thread for a few millisecond, to give the opportunity the GUI to pick up the changes
				Thread.sleep(1);
			}
			int pos=this.findPositionMinDistance(temp);
			travellingOrder.add(citiesTemp.get(pos));
			citiesTemp.remove(pos);
			//Increment the counter that keeps track of the number of tours for this algorithm
			tourLength++;
			//Pause the Thread for a few millisecond, to give the opportunity the GUI to pick up the changes
			Thread.sleep(2);
			//Send an update to notify that the program is performing correctly
			this.setProgress((int)(Math.random()*10));
			if(this.isCancelled()){
				processedCancelled=true;
				return false;
			}
		}
		//Calculate the execution time before the thread completes the last action
		this.calculateExecutionTime(startTime, System.nanoTime());
		this.setProgress(100);
		return true;
	}

}