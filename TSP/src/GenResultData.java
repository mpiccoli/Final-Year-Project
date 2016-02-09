import java.awt.Point;
import java.util.Vector;

public class GenResultData {
	private int numCities, generationCount;
	private float crossoverProbability, mutationProbability;
	private String crossoverMethod, mutationMethod;
	private double tourLength, fitness;
	private long timeExecution;
	private int populationSize, popFrom, popTo, maxGen;
	private Vector<Point> listOfCities;
	private Vector<Point> results;
	
	public GenResultData(){
		numCities=0;
		generationCount=0;
		crossoverProbability=0f;
		mutationProbability=0f;
		crossoverMethod="";
		mutationMethod="";
		tourLength=0.0;
		fitness=0.0;
		timeExecution=0;
		populationSize=0;
		popFrom=0;
		popTo=0;
		maxGen=0;
	}
	//Constructor with parameters
	public GenResultData(int cities, int genCount, float crossP, float mutP, String crossMet, String mutMet, double tour, double fit, long time,int popS, int popF, int popT, int maxG){
		numCities=cities;
		generationCount=genCount;
		crossoverProbability=crossP;
		mutationProbability=mutP;
		crossoverMethod=crossMet;
		mutationMethod=mutMet;
		tourLength=tour;
		fitness=fit;
		timeExecution=time;
		populationSize=popS;
		popFrom=popF;
		popTo=popT;
		maxGen=maxG;		
	}
	public GenResultData(int cities, int popS, int popF, int popT, int maxG, String crossM, String mutM, float crossP, float mutP){
		numCities=cities;
		populationSize=popS;
		popFrom=popF;
		popTo=popT;
		maxGen=maxG;
		crossoverMethod=crossM;
		mutationMethod=mutM;
		crossoverProbability=crossP;
		mutationProbability=mutP;
	}
	public void setNumCities(int cities){
		numCities=cities;
	}
	public int getNumCities(){
		return numCities;
	}
	public void setGenerationCount(int genC){
		generationCount=genC;
	}
	public int getGenerationCount(){
		return generationCount;
	}
	public void setCrossOverProbability(float cP){
		crossoverProbability=cP;
	}
	public float getCrossoverProbability(){
		return crossoverProbability;
	}
	public void setMutationProbability(float mP){
		mutationProbability=mP;
	}
	public float getMutationProbability(){
		return mutationProbability;
	}
	public void setCrossoverMethod(String cM){
		crossoverMethod=cM;
	}
	public String getCrossoverMethod(){
		return crossoverMethod;
	}
	public void setMutationMethod(String mM){
		mutationMethod=mM;
	}
	public String getMutationMethod(){
		return mutationMethod;
	}
	public void setTourLength(double tL){
		tourLength=tL;
	}
	public double getTourLength(){
		return tourLength;
	}
	public void setFitness(double fit){
		fitness=fit;
	}
	public double getFitness(){
		return fitness;
	}
	public void setExecutionTime(long time){
		timeExecution=time;
	}
	public long getExecutionTime(){
		return timeExecution;
	}
	public void setPopSize(int p){
		populationSize=p;
	}
	public int getPopSize(){
		return populationSize;
	}
	public void setPopFrom(int pF){
		popFrom=pF;
	}
	public int getPopFrom(){
		return popFrom;
	}
	public void setPopTo(int pT){
		popTo=pT;
	}
	public int getPopTo(){
		return popTo;
	}
	public void setMaxGen(int mG){
		maxGen=mG;
	}
	public int getMaxGen(){
		return maxGen;
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
		if (!(obj instanceof GenResultData )){
			return false;
		}
		GenResultData temp=(GenResultData)obj;
		return (this.numCities==temp.numCities && this.populationSize==temp.populationSize && this.popFrom==temp.popFrom && this.popTo==temp.popTo &&
				this.maxGen==temp.maxGen && this.crossoverMethod.equals(temp.crossoverMethod) && this.crossoverProbability==temp.crossoverProbability &&
				this.mutationMethod.equals(temp.mutationMethod) && this.mutationProbability==temp.mutationProbability);
	}
}
