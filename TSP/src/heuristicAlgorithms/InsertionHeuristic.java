package heuristicAlgorithms;

import java.awt.Point;
import java.util.Vector;

public class InsertionHeuristic {
	//Global Variables
		private Vector<Point> cities;
		private Vector<Point> resultData;
		private Vector<Integer> convexHull;
		@SuppressWarnings("unused")
		private Vector<Double> travellingOrderDistances;
		private double resultTourDistance;
		private long executionTime;
		private long tourLength;
		private boolean processedCancelled;
		
		public InsertionHeuristic(Vector<Point> cities, Vector<Point> results){
			this.cities=cities;
			this.resultData=results;
			convexHull=new Vector<Integer>();
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
		@SuppressWarnings("unchecked")
		public Vector<Point> getListOfCities(){
			return (Vector<Point>) cities.clone();
		}
		@SuppressWarnings("unchecked")
		public Vector<Point> getTravellingOrder(){
			return (Vector<Point>)resultData.clone();
		}
		private double calculateDistance(Point a, Point b){
			double xValue=(a.getX()-b.getX())*(a.getX()-b.getX());
			double yValue=(a.getY()-b.getY())*(a.getY()-b.getY());
			return Math.sqrt(xValue+yValue);
		}
		@SuppressWarnings("unused")
		private void calculateExecutionTime(long startTime, long finishTime){
			//Verify that the Thread has not been stopped
			if(!processedCancelled){
				executionTime=(finishTime-startTime)/1000000;
			}
			else{
				executionTime=-1;
			}
		}
		@SuppressWarnings("unused")
		private Vector<Point> findConvexHull(Vector<Point> data, Point startingP, double givenDistance){
			Vector<Integer> positionPointsConvexHull=new Vector<Integer>();
			Vector<Double> distances=new Vector<Double>();
			Vector<Double> tempDistances=new Vector<Double>();
			for(int i=0; i<data.size(); i++){
				distances.add(calculateDistance(startingP, data.get(i)));
			}
			while(convexHull.size()<0){
				for(int i=0; i<distances.size(); i++){
					if(distances.get(i)<=givenDistance){
						positionPointsConvexHull.addElement(i);
						tempDistances.addElement(distances.get(i));
					}
				}
				//Increase the distance for future convex hulls
				givenDistance++;
			}
			//Now sort the data and return it
			return sortConvexHull(tempDistances,positionPointsConvexHull);
		}
		protected Vector<Point> sortConvexHull(Vector<Double> data, Vector<Integer> positionData){
			Vector<Point> sorted=new Vector<Point>();
			@SuppressWarnings("unused")
			Vector<Point> tempPos=new Vector<Point>();
			//Store the position of both distance (inside data) and its position (inside positionData) as X and Y value into tempPos
			//This is to prevent loosing the position of the actual point during the sort
			for(int i=0; i<data.size(); i++){
				
			}
			return sorted;
		}
		protected Boolean doInBackground() throws Exception{
			
			//Create a copy of the cities data
			@SuppressWarnings("unchecked")
			Vector<Point>cities_copy=(Vector<Point>) cities.clone();
			//Get the first element in the list
			@SuppressWarnings("unused")
			Point startingPoint=cities_copy.get(0);
			cities_copy.remove(0);
			for(int i=0; i<cities_copy.size(); i++){
				//Create convex hull with distance=1;
			}
			
			
			return true;
		}

}
