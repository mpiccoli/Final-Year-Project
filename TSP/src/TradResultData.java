import java.awt.Point;
import java.util.Vector;

public class TradResultData {
	private int numCities;
	private double tourLength, timeExecution;
	private String  details, algName;
	private Vector<Point> listOfCities;
	private Vector<Point> results;
	
	public TradResultData(){
		numCities=0;
		tourLength=0.0;
		timeExecution=0.0;
		details="";
		algName="";
	}
	//Constructor with parameters
	public TradResultData(int nCities, double length, double time, String det, String name){
		numCities=nCities;
		tourLength=length;
		timeExecution=time;
		details=det;
		algName=name;
	}
	public TradResultData(String name, int cities, Vector<Point> dataPoints){
		algName=name;
		numCities=cities;
		listOfCities=dataPoints;
	}
	public void setNumCities(int num){
		numCities=num;
	}
	public int getNumCities(){
		return numCities;
	}
	public void setTourLength(double length){
		tourLength=length;
	}
	public double getTourLength(){
		return tourLength;
	}
	public void setTimeExecution(double time){
		timeExecution=time;
	}
	public double getExecutionTime(){
		return timeExecution;
	}
	public void setDetails(String text){
		details=text;
	}
	public String getDetails(){
		return details;
	}
	public void setAlgName(String name){
		algName=name;
	}
	public String getAlgName(){
		return algName;
	}
	public void setResultingPoints(Vector<Point> data){
		results=data; 
	}
	public Vector<Point> getCities(){
		return listOfCities;
	}
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof TradResultData )){
			return false;
		}
		TradResultData temp=(TradResultData)obj;
		return (this.algName.equals(temp.algName) && this.numCities==temp.numCities);
	}
}