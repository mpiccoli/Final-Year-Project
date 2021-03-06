package heuristicAlgorithms;

import java.awt.Point;
import java.util.Vector;

import javax.swing.SwingWorker;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see Vector, Point, SwingWorker
 * 
 * This class finds the path to the given TSP points by finding the closest element to the previous element in the path.
 * 
 */
@SuppressWarnings("rawtypes")
public class ClosestNeighbour extends SwingWorker {
	// Global Variables
	private Vector<Point> cities, travellingOrder;
	private long executionTime;
	private int tourLength;
	private double tourDistance;
	private boolean processedCancelled;

	/*
	 * @param points Vector containing the list of cities
	 * 
	 * @param results Vector containing the resulting path created with the
	 * execution of this class
	 * 
	 * Constructor with 2 parameters
	 * 
	 */
	public ClosestNeighbour(Vector<Point> points, Vector<Point> results) {
		cities = points;
		travellingOrder = results;
		tourLength = 1;
		tourDistance = 0;
		processedCancelled = false;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public int getTourLength() {
		return tourLength;
	}

	public double getTourDistance() {
		return tourDistance;
	}

	@SuppressWarnings("unchecked")
	public Vector<Point> getListOfCities() {
		// Create an exact copy of the vector cities and send it back
		return (Vector<Point>) cities.clone();
	}

	@SuppressWarnings("unchecked")
	public Vector<Point> getTravellingOrder() {
		// Create an exact copy of the vector travellingOrder and send it back
		return (Vector<Point>) travellingOrder.clone();
	}

	/*
	 * @param a Point A
	 * 
	 * @param b Point B
	 * 
	 * @return double Return the distance value found after the operations
	 * 
	 * This method calculates the distance between two points
	 */
	private double calculateDistance(Point a, Point b) {
		double xValue = (a.getX() - b.getX()) * (a.getX() - b.getX());
		double yValue = (a.getY() - b.getY()) * (a.getY() - b.getY());
		return Math.sqrt(xValue + yValue);
	}

	/*
	 * @param data Vector containing the list of distances
	 * 
	 * @return int Return the index of the shortest path found in the vector of
	 * distances
	 * 
	 * This method finds the shortest path inside a vector of distances and
	 * returns its index
	 */
	private int findPositionMinDistance(Vector<Double> data) {
		Double minTemp = 1000.0;
		int pos = 0;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i) <= minTemp) {
				minTemp = data.get(i);
				pos = i;
			}
		}
		tourDistance += minTemp;
		return pos;
	}

	/*
	 * @param startTime Initial Time
	 * 
	 * @param finishTime Finish Time
	 * 
	 * This method calculates the execution time of this algorithm
	 */
	private void calculateExecutionTime(long startTime, long finishTime) {
		// Verify that the Thread has not been stopped
		if (!processedCancelled) {
			executionTime = (finishTime - startTime);
		} else {
			executionTime = -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Boolean doInBackground() throws Exception {
		// Start the time for this process
		long startTime = System.currentTimeMillis();
		@SuppressWarnings("unchecked")
		Vector<Point> citiesTemp = (Vector<Point>) cities.clone();
		// The first place to visit is the first city in the list
		travellingOrder.add(citiesTemp.get(0));
		// Once the city has been added to the path, remove it from the
		// available cities
		citiesTemp.remove(0);
		// Pause the Thread for a few millisecond, to give the opportunity the
		// GUI to pick up the changes
		Thread.sleep(2);
		// Send an update to notify that the program is performing correctly
		this.setProgress((int) (Math.random() * 10));
		while (citiesTemp.size() > 0) {
			Vector<Double> temp = new Vector<Double>();
			for (int j = 0; j < citiesTemp.size(); j++) {
				temp.add(calculateDistance(travellingOrder.lastElement(), citiesTemp.get(j)));
				this.setProgress((int) (Math.random() * 10));
				// Pause the Thread for a few millisecond, to give the
				// opportunity the GUI to pick up the changes
				Thread.sleep(1);
			}
			int pos = this.findPositionMinDistance(temp);
			travellingOrder.add(citiesTemp.get(pos));
			citiesTemp.remove(pos);
			// Increment the counter that keeps track of the number of tours for
			// this algorithm
			tourLength++;
			// Pause the Thread for a few millisecond, to give the opportunity
			// the GUI to pick up the changes
			Thread.sleep(2);
			// Send an update to notify that the program is performing correctly
			this.setProgress((int) (Math.random() * 10));
			if (this.isCancelled()) {
				processedCancelled = true;
				return false;
			}
		}
		//Add the distance between the last point and the first point to complete the cycle
		tourDistance+=calculateDistance(travellingOrder.lastElement(), travellingOrder.get(0));
		travellingOrder.add(travellingOrder.get(0));
		// Calculate the execution time before the thread completes the last
		// actions
		this.calculateExecutionTime(startTime, System.currentTimeMillis());
		// Set the progress as completed and return nothing
		this.setProgress(100);
		return true;
	}

}
