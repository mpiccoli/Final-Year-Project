import java.awt.Point;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.jgap.Configuration;
import geneticAlgorithms.TSP_GA;
import geneticAlgorithms.TSP_GA_Worker;

public class GenResultData {
	private int generationCount, popSize, popFrom, popTo, maxGen;
	private float crossoverProbability, mutationProbability;
	private String crossoverMethod, mutationMethod;
	private double fitness;
	private long timeExecution;
	private Vector<Point> listOfCities, results;
	private Vector<Double> pathDistances;
	private Vector<Vector<Point>> resultsData;
	private Configuration conf;
	private TSP_GA tsp;
	private TSP_GA_Worker tspWorker;

	//Constructor with parameters
	public GenResultData(int popS, int popF, int popT, int maxG, String crossMet, float crossP, String mutMet, float mutP, 
			Configuration conf, TSP_GA tsp, TSP_GA_Worker tspWorker){	
		crossoverProbability=crossP;
		mutationProbability=mutP;
		crossoverMethod=crossMet;
		mutationMethod=mutMet;
		popSize=popS;
		popFrom=popF;
		popTo=popT;
		maxGen=maxG;
		this.conf=conf;
		this.tsp=tsp;
		this.tspWorker=tspWorker;
		//Instantiate variables
		fitness=0;
		generationCount=0;
		timeExecution=0;
		results=new Vector<Point>();
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
		popSize=p;
	}
	public int getPopSize(){
		return popSize;
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
	@SuppressWarnings("unchecked")
	public void setResultingPoints(Vector<Point> data, boolean option){
		if(option){
			results=data;
		}
		else{
			results=(Vector<Point>) data.clone(); 
		}
	}
	public Vector<Point> getResultingPoints(){
		return results;
	}
	@SuppressWarnings("unchecked")
	public void setCities(Vector<Point> data, boolean option){
		if(option){
			listOfCities=data;
		}
		else{
			listOfCities=(Vector<Point>) data.clone();
		}
	}
	public Vector<Point> getCities(){
		return listOfCities;
	}
	@SuppressWarnings("unchecked")
	public void setPathDistances(Vector<Double> distances, boolean option){
		if(option){
			pathDistances=distances;
		}
		else{
			pathDistances=(Vector<Double>) distances.clone();
		}
	}
	public Vector<Double> getPathDistances(){
		return pathDistances;
	}
	public void setConfigurationTSP(Configuration c){
		conf=c;
	}
	public Configuration getConfigurationTSP(){
		return conf;
	}
	public void setTSP(TSP_GA tsp){
		this.tsp=tsp;
	}
	public TSP_GA getTSP(){
		return tsp;
	}
	public void setTSP_Worker(TSP_GA_Worker tspWork){
		tspWorker=tspWork;
	}
	public TSP_GA_Worker getTSP_Worker(){
		return tspWorker;
	}
	@SuppressWarnings("unchecked")
	public void setResultsData(Vector<Vector<Point>> data, boolean option){
		if(option){
			resultsData=data;
		}
		else{
			resultsData=(Vector<Vector<Point>>) data.clone();
		}
	}
	public Vector<Vector<Point>> getResultsData(){
		return resultsData;
	}
	public String wrapDataForFileWriter(){
		String s="";
		//Add the algorithm information
		s+="Algorithm Name: Genetic Algorithm\n"
				+ "Number of Cities: "+popSize+"\n"
				+ "Chromosome Range: "+popFrom+"-"+popTo+"\n"
				+ "Number of Generations: "+maxGen+"\n"
				+ "Crossover Method: "+crossoverMethod+"\n"
				+ "Probability associated to the Crossover: "+(crossoverProbability*100)+"%\n"
				+ "Mutation Method: "+mutationMethod+"\n"
				+ "Probability associated to the Mutation: "+(mutationProbability*100)+"%\n\n"
				+ "Best Fitness Value: "+fitness+"\n"
				+ "Performance Time: "+String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timeExecution),TimeUnit.MILLISECONDS.toSeconds(timeExecution)%60, (timeExecution%100))+"\n\n"
				+ "List of Cities:\n";
		//Try to add the list of cities
		try{
			String cities="";
			for(int i=0; i<listOfCities.size(); i++){
				Point temp=listOfCities.elementAt(i);
				cities+="   City n. "+(i+1)+" --> ("+Math.round(temp.getX())+","+Math.round(temp.getY())+")\n";
			}
			s+=cities+"\n";
		}catch(Exception e){
			s+="   No Data\n\n";
		}
		s+="Best Path Found:\n";
		//Try to add the list of cities for the best solution found
		try{
			String path="";
			for(int i=0; i<results.size(); i++){
				Point temp=results.elementAt(i);
				path+="   City n. "+(i+1)+" --> ("+Math.round(temp.getX())+","+Math.round(temp.getY())+")\n";
			}
			s+=path+"\n";
		}catch(Exception e){
			s+="   No Data\n\n";
		}

		s+="List of all the Results computed:\n";
		//Try to add the list of results
		try{
			String resultOfResult="";
			for(int i=0; i<resultsData.size(); i++){
				String result="  --> Result n. "+(i+1)+"\n";
				for(int j=0; j<resultsData.elementAt(i).size(); j++){
					Point temp=resultsData.elementAt(i).elementAt(j);
					result+="    City n. "+(j+1)+" --> ("+Math.round(temp.getX())+","+Math.round(temp.getY())+")\n";
				}
				resultOfResult+=result+"\n";
			}
			s+=resultOfResult+"\n";
		}catch(Exception e){
			s+="   No Data\n\n";
		}
		//Try to add the list of all the distanced computed
		s+="List of all the Distances computed:\n";
		try{
			String paths="";
			for(int i=0; i<pathDistances.size(); i++){
				paths+="   Path Distance ("+(i+1)+") --> "+pathDistances.elementAt(i)+"\n";
			}
			s+=paths+"\n";
		}catch(Exception e){
			s+="   No Data\n\n";
		}
		return s;
	}
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof GenResultData )){
			return false;
		}
		GenResultData temp=(GenResultData)obj;
		return (this.popSize==temp.popSize && this.popFrom==temp.popFrom && this.popTo==temp.popTo &&
				this.maxGen==temp.maxGen && this.crossoverMethod.equals(temp.crossoverMethod) && this.crossoverProbability==temp.crossoverProbability &&
				this.mutationMethod.equals(temp.mutationMethod) && this.mutationProbability==temp.mutationProbability);
	}
}
