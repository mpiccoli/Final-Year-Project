import java.awt.Point;
import java.util.Vector;

public class TradResultData {
	private int numCities;
	private double tourLength; 
	private long timeExecution;
	private String  details, algName;
	private Vector<Point> listOfCities;
	private Vector<Point> results;
	
	public TradResultData(){
		numCities=0;
		tourLength=0.0;
		timeExecution=0;
		details="";
		algName="";
		results=new Vector<Point>();
	}
	//Constructor with parameters
	public TradResultData(int nCities, double length, long time, String det, String name){
		numCities=nCities;
		tourLength=length;
		timeExecution=time;
		details=det;
		algName=name;
		results=new Vector<Point>();
	}
	public TradResultData(String name, int cities, Vector<Point> dataPoints){
		algName=name;
		numCities=cities;
		listOfCities=dataPoints;
		results=new Vector<Point>();
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
	public void setTimeExecution(long time){
		timeExecution=time;
	}
	public long getExecutionTime(){
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
	public Vector<Point> getResultingPoints(){
		return results;
	}
	public void setCities(Vector<Point> data){
		listOfCities=(Vector<Point>) data.clone();
	}
	public Vector<Point> getCities(){
		return listOfCities;
	}
	public void resetResultData(){
		results.clear();
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