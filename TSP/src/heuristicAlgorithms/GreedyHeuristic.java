package heuristicAlgorithms;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;

import javax.swing.SwingWorker;

public class GreedyHeuristic extends SwingWorker{
	//Global Variables
	private Vector<Point> cities;
	private Vector<Vector<Point>> travellingOrders;
	private Vector<Double> travellingOrderDistances;
	private Vector<Point> result;
	private long executionTime;
	private int tourLength;
	private boolean processedCancelled;
	
	public GreedyHeuristic(Vector<Point> places, Vector<Point> results){
		cities=places;
		this.result=results;
		travellingOrders=new Vector<Vector<Point>>();
		travellingOrderDistances=new Vector<Double>();
		executionTime=0;
		tourLength=0;
		processedCancelled=false;
	}
	public long getExecutionTime(){
		return executionTime;
	}
	public int getTourLength(){
		return tourLength;
	}
	public Vector<Point> getListOfCities(){
		return (Vector<Point>) cities.clone();
	}
	public Vector<Point> getTravellingOrder(){
		return (Vector<Point>)result.clone();
	}
	private double calculateDistance(Point a, Point b){
		double xValue=(a.getX()-b.getX())*(a.getX()-b.getX());
		double yValue=(a.getY()-b.getY())*(a.getY()-b.getY());
		return Math.sqrt(xValue+yValue);
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
	private Vector<Point> findMinDistanceTour(){
		Double minTemp=1000.0;
		Vector<Point> temp=null;
		for(int i=0; i<travellingOrderDistances.size(); i++){
			if(travellingOrderDistances.get(i)<=minTemp){
				minTemp=travellingOrderDistances.get(i);
				temp=travellingOrders.get(i);
			}
		}
		return temp;
	}
	@SuppressWarnings("unchecked")
	//CHANGE LENGTH FOR LOOP
	//CFIND FINAL RESULT FOR ALGORITHM (BEST RESULT IN VECTOR OF DATA)
	protected Boolean doInBackground() throws Exception{
		//Start the time for this process
		long startTime=System.nanoTime();
		Point elePos1=null, elePos2=null, firstElement=null;
		for(int j=0; j<cities.size(); j++){
			double lengthTemp=0;
			Vector<Point> data_copy=(Vector<Point>) cities.clone();
			result.clear();
			int counter=0;
			//Pick two random Points from the list of cities, remember the first one
			//since it will be used at the end of the tour to close the cycle
			while(elePos1==elePos2){
				elePos1=data_copy.get(new Random().nextInt(data_copy.size()));
				elePos2=data_copy.get(new Random().nextInt(data_copy.size()));
			}
			while(data_copy.size()-1>0){
				if(counter==0){
					firstElement=elePos1;
					counter=1;
				}
				lengthTemp+=this.calculateDistance(elePos1, elePos2);
				result.add(elePos1);
				result.add(elePos2);
				data_copy.remove(elePos1);
				if(data_copy.size()>1){
					elePos1=elePos2;
					while(elePos1==elePos2){
						elePos2=data_copy.get(new Random().nextInt(data_copy.size()));
					}
				}
				if(this.isCancelled()){
					processedCancelled=true;
					return false;
				}
				this.setProgress((int)(Math.random()*10));
				//Pause the Thread for a few millisecond, to give the opportunity the GUI to pick up the changes
				Thread.sleep(3);
			}
			this.setProgress((int)(Math.random()*10));
			//Pause the Thread for a few millisecond, to give the opportunity the GUI to pick up the changes
			Thread.sleep(3);
			//Close the circle by calculating the distance between the first and last cities in the tour
			lengthTemp+=this.calculateDistance(elePos2, firstElement);
			result.add(firstElement);
			//Add the tour to the list as well as the tourLength
			travellingOrders.add(result);
			travellingOrderDistances.add(lengthTemp);
		}
		//Calculate the execution time before the thread completes the last action
		this.calculateExecutionTime(startTime, System.nanoTime());
		this.setProgress(100);
		return true;
	}
}
