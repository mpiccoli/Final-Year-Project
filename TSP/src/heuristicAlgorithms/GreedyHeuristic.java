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
	private Vector<Point> resultData;
	private double resultTourDistance;
	private long executionTime;
	private long tourLength;
	private boolean processedCancelled;
	
	public GreedyHeuristic(Vector<Point> places, Vector<Point> results){
		cities=places;
		this.resultData=results;
		travellingOrders=new Vector<Vector<Point>>();
		travellingOrderDistances=new Vector<Double>();
		resultTourDistance=0;
		executionTime=0;
		tourLength=0;
		processedCancelled=false;
	}
	public long getExecutionTime(){
		return executionTime;
	}
	public long getTourLength(){
		return tourLength;
	}
	public double getTourDistance(){
		return resultTourDistance;
	}
	public Vector<Point> getListOfCities(){
		return (Vector<Point>) cities.clone();
	}
	public Vector<Point> getTravellingOrder(){
		return (Vector<Point>)resultData.clone();
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
	private void findTourLength(int n){
		//The formula used to find the number of cycles is: (n*n)(log2 of n)
		tourLength=(long) ((n*n)*(Math.log(n)/Math.log(2)));
	}
	private Vector<Point> findBestResult(){
		//Store the current min value found and its position in the Vector
		double min=10000;
		int pos=0;
		for(int i=0; i<travellingOrderDistances.size(); i++){
			if(travellingOrderDistances.get(i)<min){
				//If a new min is found, store it
				min=travellingOrderDistances.get(i);
				pos=i;
			}
		}
		//Search for the element with the minimum distance into the vector of vector containing the 
		//list of points for the best tour. Accept only 4 decimal places
		resultTourDistance=(double)Math.round(min * 10000d) / 10000d;
		return travellingOrders.get(pos);
	}
	@SuppressWarnings("unchecked")
	protected Boolean doInBackground() throws Exception{
		//Start the time for this process
		long startTime=System.nanoTime();
		Point elePos1=null, elePos2=null, firstElement=null;
		//Find how many tours are possible given the number of cities
		this.findTourLength(cities.size());
		for(int j=0; j<tourLength; j++){
			double lengthTemp=0;
			Vector<Point> data_copy=(Vector<Point>) cities.clone();
			resultData.clear();
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
				resultData.add(elePos1);
				resultData.add(elePos2);
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
			resultData.add(firstElement);
			//Add the tour to the list as well as the tour length
			travellingOrders.add(resultData);
			travellingOrderDistances.add(lengthTemp);
		}
		//Find the best result evaluated
		resultData=this.findBestResult();
		this.setProgress((int)(Math.random()*10));
		//Calculate the execution time before the thread completes the last action
		this.calculateExecutionTime(startTime, System.nanoTime());
		this.setProgress(100);
		return true;
	}
}