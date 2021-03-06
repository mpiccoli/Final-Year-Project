import java.awt.Point;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see Point, Vector
 * 
 * This Class is used to store all the settings and execution data a heuristic algorithm produces from when it is added to the queue
 * until the end of its execution
 * 
 */
public class TradResultData {

	// Global Variables
	private int numCities;
	private double tourLength;
	private long timeExecution;
	private String details, algName;
	private Vector<Point> listOfCities;
	private Vector<Point> results;

	/*
	 * Empty Constructor
	 */
	public TradResultData() {
		numCities = 0;
		tourLength = 0.0;
		timeExecution = 0;
		details = "";
		algName = "";
		results = new Vector<Point>();
	}

	/*
	 * @param nCities Defines the number of cities the algorithm has associated
	 * 
	 * @param length Defines the path length of the tour
	 * 
	 * @param time Defines the time of execution of the algorithm
	 * 
	 * @param det Defines the algorithm specifications
	 * 
	 * @param name Define the name of the algorithm
	 * 
	 * Constructor with 5 parameters
	 * 
	 */
	public TradResultData(int nCities, double length, long time, String det, String name) {
		numCities = nCities;
		tourLength = length;
		timeExecution = time;
		details = det;
		algName = name;
		results = new Vector<Point>();
	}

	/*
	 * @param name Define the name of the algorithm
	 * 
	 * @param cities DEfiens the number of cities the algorithm has associated
	 * 
	 * @param dataPoints Reference to the city vectors
	 * 
	 * Constructor with 3 parameters
	 * 
	 */
	public TradResultData(String name, int cities, Vector<Point> dataPoints) {
		algName = name;
		numCities = cities;
		listOfCities = dataPoints;
		results = new Vector<Point>();
	}

	public void setNumCities(int num) {
		numCities = num;
	}

	public int getNumCities() {
		return numCities;
	}

	public void setTourLength(double length) {
		tourLength = length;
	}

	public double getTourLength() {
		return tourLength;
	}

	public void setTimeExecution(long time) {
		timeExecution = time;
	}

	public long getExecutionTime() {
		return timeExecution;
	}

	public void setDetails(String text) {
		details = text;
	}

	public String getDetails() {
		return details;
	}

	public void setAlgName(String name) {
		algName = name;
	}

	public String getAlgName() {
		return algName;
	}

	public void setResultingPoints(Vector<Point> data) {
		results = data;
	}

	public Vector<Point> getResultingPoints() {
		return results;
	}

	@SuppressWarnings("unchecked")
	public void setCities(Vector<Point> data) {
		listOfCities = (Vector<Point>) data.clone();
	}

	public Vector<Point> getCities() {
		return listOfCities;
	}

	/*
	 * This method resets the vector that contains the final path
	 */
	public void resetResultData() {
		results.clear();
	}

	/*
	 * This method creates a string containing all the class information, ready
	 * to be exported to a file
	 */
	public String wrapDataForFileWriter() {
		// Add algorithm basic information
		String s = "Algorithm Name: " + algName + "\n" + "Algorithm Details: " + details + "\n" + "Number of Cities: "
				+ numCities + "\n" + "Performance Time: "
				+ String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeExecution),
						TimeUnit.MILLISECONDS.toSeconds(timeExecution) % 60, (timeExecution % 100))
				+ "\n" + "Tour Length: " + tourLength + "\n\n" + "List of Cities:\n";
		// Try to add a list of cities
		try {
			String cities = "";
			for (int i = 0; i < listOfCities.size(); i++) {
				Point temp = listOfCities.elementAt(i);
				cities += "   City n. " + (i + 1) + " --> (" + Math.round(temp.getX()) + "," + Math.round(temp.getY())
						+ ")\n";
			}
			s += cities;
		} catch (Exception e) {
			s += "   No Data\n";
		}
		s += "\n\nFinal Path:\n";
		// Try to add a list of the resulting path
		try {
			String path = "";
			for (int i = 0; i < results.size(); i++) {
				Point temp = results.elementAt(i);
				path += "   City n. " + (i + 1) + " --> (" + Math.round(temp.getX()) + "," + Math.round(temp.getY())
						+ ")\n";
			}
			s += path + "\n";
		} catch (Exception e) {
			s += "   No Data\n";
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TradResultData)) {
			return false;
		}
		// Define how to compare two objects of this class
		TradResultData temp = (TradResultData) obj;
		return (this.algName.equals(temp.algName) && this.numCities == temp.numCities);
	}
}