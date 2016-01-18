
public class TradResultData {
	private int numCities;
	private double tourLength, timeExecution;
	private String  details, algName;
	
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
	public TradResultData(String name, int cities){
		algName=name;
		numCities=cities;
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
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof TradResultData )){
			return false;
		}
		TradResultData temp=(TradResultData)obj;
		return (this.algName.equals(temp.algName) && this.numCities==temp.numCities);
	}
}