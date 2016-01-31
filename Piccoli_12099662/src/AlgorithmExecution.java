import java.util.ArrayList;

import javax.swing.SwingWorker;
/** 
 * This Class is an extension of the Class SwingWorker which implements the Class Runnable
 * The aim of this class object is fill the arrays (passed as reference) with data and sort them in ascending order with different algorithms including
 * Bubble Sort, Selection Sort, Merge Sort and Quick Sort.
 * 
 * @author Michael Piccoli - 12099662
 * @version 1.0
 * @since 5 January 2015
 * @see SwingWorker
 * @see Thread
 * @see Runnable
 * @see ArrayList
 */

public class AlgorithmExecution extends SwingWorker<Boolean, Boolean> implements Runnable{
	//Global Variables
	private ArrayList<Integer> array, arraySorted;
	private ArrayList<ArrayList<Long>> timeStore;
	private ArrayList<ArrayList<Integer>> allArrays;
	private char selection;
	private int algVisited;
	private long startTime, finishTime, executionTime;
	private boolean processCancelledFlag, singleOrMultipleAlgorithm;
	
	/** This is the Constructor with parameters that allows the user to sort the data with a single sorting algorithm, selected by the user
	 * 
	 * @param sel		This selects the algorithm which will be used to sort the data
	 * @param list		This contains the reference of the array list arrayData that will be filled with unsorted data
	 * @param result	This contains the reference of the array list arraySorted, which will be used to save the sorted data
	 * @param times		This contains the reference of the array list timeStore where the sorting times will be stored
	 * 
	 */
	public AlgorithmExecution(char sel, ArrayList<Integer> list,  ArrayList<Integer> result, ArrayList<ArrayList<Long>> times){
		selection=sel;
		array=list;
		arraySorted=result;
		this.resetTimes();
		processCancelledFlag=false;
		singleOrMultipleAlgorithm=true;
		timeStore=times;
		algVisited=0;
	}
	
	/** This is the Constructor with parameters that allows the user to sort the data with all sorting algorithms
	 * 
	 * @param list		This contains the reference of the array list arrayData that will be filled with unsorted data
	 * @param result	This contains the reference of the array list arraySorted, which will be used to save the sorted data
	 * @param times		This contains the reference of the array list timeStore where the sorting times will be stored
	 * @param ar		This contains the reference of the array list arrayStorage which will save all the array with different sizes, created during the execution
	 * 
	 */
	public AlgorithmExecution(ArrayList<Integer> list,  ArrayList<Integer> result, ArrayList<ArrayList<Long>> times, ArrayList<ArrayList<Integer>> ar){
		array=list;
		arraySorted=result;
		this.resetTimes();
		singleOrMultipleAlgorithm=false;
		timeStore=times;
		allArrays=ar;
	}

	/** This method sorts the elements in the array using the bubble sort algorithm
	 * 
	 */
	private void bubbleSort(){
		//Clear the sorted data (when available) from the array to start the sorting
		this.resetArraySorted();
		for(int i=0; i<=arraySorted.size(); i++){
			for(int j=0; j<arraySorted.size()-1; j++){
				//swap elements
				if(arraySorted.get(j)>arraySorted.get(j+1)){
					int temp=arraySorted.get(j);
					arraySorted.set(j, arraySorted.get(j+1));
					arraySorted.set(j+1, temp);
					this.pauseThread();
				}
				//This verifies that the thread job has not been stop by the user (Stop Button)
				if(singleOrMultipleAlgorithm && this.isCancelled()){
					processCancelledFlag=true;
					return;
				}
			}
		}
		//If a single algorithm has to run, change the progress of thread to finished (100)
		if(singleOrMultipleAlgorithm){
			this.setProgress(100);
		}
	}

	/** This method sorts the elements in the array using the selection sort algorithm
	 * 
	 */
	private void selectionSort(){
		this.resetArraySorted();
		int minPosition=0, temp=0;
		for(int i=0; i<arraySorted.size()-1; i++){
			minPosition=i;
			for(int k=i+1; k<arraySorted.size(); k++){
				if(arraySorted.get(k)<=arraySorted.get(minPosition)){
					minPosition=k;
					//Pause the thread jobs
					this.pauseThread();
				}
			}
			//Swap elements in the array only when a element has been found smaller than the one compared to
			if(minPosition!=i){
				temp=arraySorted.get(i);
				arraySorted.set(i, arraySorted.get(minPosition));
				arraySorted.set(minPosition, temp);
			}
			//This verifies that the thread job has not been stop by the user (Stop Button)
			if(singleOrMultipleAlgorithm && this.isCancelled()){
				processCancelledFlag=true;
				return;
			}
		}
		//If a single algorithm has to run, change the progress of thread to finished (100)
		if(singleOrMultipleAlgorithm){
			this.setProgress(100);
		}
	}
	/** This method starts the merge sort algorithm
	 * 
	 * @param numbers	This contains the sub array to sort
	 * @param left		This is the index of where the new sub-array starts
	 * @param right		This is the index of where the new su-array ends
	 */
	private void startMergeSort(ArrayList<Integer> numbers, int left, int right){
		int mid;
		if (right > left){
			//Create a sub-array with half of the size of the previous array
			mid = (right + left) / 2;
			//Recursive calls
			startMergeSort(numbers, left, mid);
			startMergeSort(numbers, (mid + 1), right);
			mergeSort(numbers, left, (mid+1), right);
			//If a single algorithm has to run, change the progress of thread to finished (100)
			if(singleOrMultipleAlgorithm){
				this.setProgress(100);
			}
		}
	}
	
	/** This method sorts the elements in the array using the merge sort algorithm
	 * 
	 * @param data		This contains the array to sort
	 * @param left		This is the index of where the sub-array starts
	 * @param mid		This is the index of the middle value in the sub-array
	 * @param right		This is the index of where the sub-array ends
	 */
	private void mergeSort(ArrayList<Integer> data, int left, int mid, int right){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		//Copy the array data into a temporary array
		for(int i=0; i<data.size(); i++){
			temp.add(i, data.get(i));
		}
		int i, leftEnd, totNum, posTemp;
		leftEnd = (mid - 1);
		posTemp = left;
		totNum = (right - left + 1);
		while ((left <= leftEnd) && (mid <= right)){
			//Order elements in the sub-array
			if (data.get(left) <= data.get(mid))
				temp.set(posTemp++, data.get(left++));
			else
				temp.set(posTemp++, data.get(mid++));
			//This verifies that the thread job has not been stop by the user (Stop Button)
			if(singleOrMultipleAlgorithm && this.isCancelled()){
				processCancelledFlag=true;
				return;
			}
		}
		//Add the data on the left size of the array to the temp array
		while (left <= leftEnd){
			temp.set(posTemp++, data.get(left++));
			//This verifies that the thread job has not been stop by the user (Stop Button)
			if(singleOrMultipleAlgorithm && this.isCancelled()){
				processCancelledFlag=true;
				return;
			}
		}
		//Add the data on the right size of the array to the temp array
		while (mid <= right){
			temp.set(posTemp++, data.get(mid++));
			//This verifies that the thread job has not been stop by the user (Stop Button)
			if(singleOrMultipleAlgorithm && this.isCancelled()){
				processCancelledFlag=true;
				return;
			}
		}
		//Merge the sub-arrays together
		for (i = 0; i < totNum; i++){
			data.set(right, temp.get(right--));
			//Pause the thread jobs
			this.pauseThread();
			//This verifies that the thread job has not been stop by the user (Stop Button)
			if(singleOrMultipleAlgorithm && this.isCancelled()){
				processCancelledFlag=true;
				return;
			}
		}
	}
	
	/** This method starts the quick sort algorithm
	 * 
	 */
	private void startQuickSort(){
		this.resetArraySorted();
		//Recursive call
		quickSort(0, arraySorted.size()-1);
		//If a single algorithm has to run, change the progress of thread to finished (100)
		if(singleOrMultipleAlgorithm){
			this.setProgress(100);
		}
	}
	
	/** This method sorts the elements in the array using the quick sort algorithm
	 * 
	 * @param i		This is the lowest index of the array
	 * @param j		This is the highest index of the array
	 */
	private void quickSort(int i, int j){
		//Define the starting, ending and middle point of the array
		int low=i, high=j, centre=arraySorted.get(low+(high-low)/2);
		while(low <=high){
			//Compare the value on the left side of the array with the central value and if it finds a smaller value, increase the left index. 
			//This means that elements ignored are already in ascending order
			while(arraySorted.get(low) < centre){
				low++;
			}
			//Compare the value on the right side of the array with the central value and if it finds a bigger value, decrease the right index. 
			//This means that elements ignored are already in ascending order
			while(arraySorted.get(high) > centre){
				high--;
			}
			//Swap elements
			if( low <= high){
				int temp= arraySorted.get(low);
				arraySorted.set(low++, arraySorted.get(high));
				arraySorted.set(high--, temp);
			}
			//Pause the thread jobs
			this.pauseThread();
			//This verifies that the thread job has not been stop by the user (Stop Button)
			if(singleOrMultipleAlgorithm && this.isCancelled()){
				processCancelledFlag=true;
				return;
			}
		}
		if(i < high){
			//Recursive call for the right side of the array
			quickSort(i, high);
		}
		if(low < j){
			//Recursive call for the left side of the array
			quickSort(low, j);
		}
	}
	
	/** This method sorts the elements in the array using the insertion sort algorithm
	 * 
	 */
	private void insertionSort(){
		this.resetArraySorted();
		for(int i=1; i<arraySorted.size(); i++){
			//get a pivot value to compare the other numbers with
			int value= arraySorted.get(i);
			int j=i;
			for(; j>0 && value< arraySorted.get(j-1); j--){
				//Move the elements after the pivot if the value is smaller than the pivot
				arraySorted.set(j, arraySorted.get(j-1));
				//Pause the Thread
				this.pauseThread();
				if(singleOrMultipleAlgorithm && this.isCancelled()){
					processCancelledFlag=true;
					return;
				}
			}
			//Move the smaller value to the right position in the array
			arraySorted.set(j, value);
			//pause the thread
			this.pauseThread();
			if(singleOrMultipleAlgorithm && this.isCancelled()){
				processCancelledFlag=true;
				return;
			}
		}
		if(singleOrMultipleAlgorithm){
			this.setProgress(100);
		}
	}
	
	/** This method sorts the elements in the array using the cocktail sort algorithm
	 * 
	 */
	private void cocktailSort(){
		this.resetArraySorted();
		boolean swap=false;
		//create the domain of where the algorithm needs to check the elements
		int startPoint=0, endPoint=arraySorted.size()-1;
		do{
			swap=false;
			//look for a greater value to move to the right side of the array
			for(int i=1; i<endPoint; i++){
				if(arraySorted.get(i) > arraySorted.get(i+1)){
					//swap elements
					int temp=arraySorted.get(i);
					arraySorted.set(i, arraySorted.get(i+1));
					arraySorted.set(i+1, temp);
					swap=true;
					//pause the thread
					this.pauseThread();
					if(singleOrMultipleAlgorithm && this.isCancelled()){
						processCancelledFlag=true;
						return;
					}
				}
			}
			if(!swap){
				break;
			}
			swap=false;
			//change right side of domain
			endPoint--;
			//look for a smaller value to move to the left side of the array
			for(int i=endPoint; i>=startPoint; i--){
				if(arraySorted.get(i)> arraySorted.get(i+1)){
					//swap elements
					int temp=arraySorted.get(i);
					arraySorted.set(i, arraySorted.get(i+1));
					arraySorted.set(i+1, temp);
					swap=true;
					//pause the thread
					this.pauseThread();
					if(singleOrMultipleAlgorithm && this.isCancelled()){
						processCancelledFlag=true;
						return;
					}
				}
			}
			//change left side of the domain
			startPoint++;
		} while(swap);
		if(singleOrMultipleAlgorithm){
			this.setProgress(100);
		}
	}
	
	/** This method removes all the values from the sorted array and re-input the original unsorted values in the array list
	 * 
	 */
	private void resetArraySorted(){
		arraySorted.clear();
		//Copy the data from the unsorted list
		for(int i=0; i<array.size(); i++){
			arraySorted.add(i, array.get(i));
		}
	}
	
	/** This method initiliases the objects that will be used to check the execution time to 0.
	 * 
	 */
	private void resetTimes(){
		startTime=0;
		finishTime=0;
		executionTime=0;
	}
	
	/** This method removes all the execution times from the memory
	 * 
	 */
	private void resetTimeArray(){
		for(int i=0; i<timeStore.size(); i++){
			timeStore.get(i).clear();
		}
	}
	
	/** This method inserts random numbers in the arrayData
	 * 
	 * @param nElement		This defines the size of the array
	 */
	private void fillArray(int nElement){
		array.clear();
		for(int i=0; i<nElement; i++){
			array.add(i, (int)(Math.random()*100));
		}
	}
	
	/** This method allows the user to know the execution time of an algorithm
	 * 
	 * @return		Return the execution time in a Long object
	 */
	public long getExecutionTime(){
		return executionTime;
	}
	
	/** This method calculates the execution time of an algorithm
	 * 
	 */
	private void calculateExecutionTime(){
		//If the process has not been cancelled by the user, find the execution time, return -1 otherwise
		if(!processCancelledFlag){
			//Set the finish time to the current system time
			finishTime= System.nanoTime();
			//Subtract the start time to the finish time and divide the results by 1000000 to make it more readable to the end user
			executionTime=(finishTime-startTime)/1000000;
		}
		else{
			executionTime=-1;
		}
	}
	
	/** This method allows the user to understand what sorting algorithm has been used to sort the array data
	 * 
	 * @return		Return the algorithm char identificator
	 */
	public int getAlgorithmVisited(){
		return algVisited;
	}
	
	/** This method pauses the current job for few milliseconds to allow the GUI to draw the data 
	 * 
	 */
	private void pauseThread(){
		//Pause the thread only when a single sorting algorithm has been selected, change the thread progress otherwise
		if(singleOrMultipleAlgorithm){
			try{
				algVisited++;
				//Change the job progress
				this.setProgress((int)(Math.random()*90));
				//If the algorithm is bubble sort, pause the thread for 1mS, 5mS otherwise because those algorithms are very fast
				if(selection!='b' && selection!='i'){
					Thread.sleep(5);
				}
				else{
					Thread.sleep(1);
				}
			}catch(Exception e){}
		}
		else{
			this.setProgress((int)(Math.random()*10));
		}
	}
	
	/** This method runs the whole thread in background, calling the sorting algorithms and other methods when needed
	 * 
	 * @return		Return true when all the jobs are completed correctly
	 */
	@Override
	protected Boolean doInBackground() throws Exception {
		//If a single algorithm has been selected, perform the user choice
		if(singleOrMultipleAlgorithm){
			switch(selection){
			case 'b':
				startTime= System.nanoTime();
				this.bubbleSort();
				break;
			case 's':
				startTime= System.nanoTime();
				this.selectionSort();
				break;
			case 'm':
				this.resetArraySorted();
				startTime= System.nanoTime();
				this.startMergeSort(arraySorted,0, arraySorted.size()-1);
				break;
			case 'q':
				startTime= System.nanoTime();
				this.startQuickSort();
				break;
			case 'i':
				startTime=System.nanoTime();
				this.insertionSort();
				break;
			case 'c':
				startTime=System.nanoTime();
				this.cocktailSort();
				break;
			}
			this.calculateExecutionTime();
		}
		//Otherwise, run all the sorting algorithms, one by one
		else{
			//Reset data
			this.resetTimeArray();
			this.resetTimes();
			//Run 5 times, 1 for each algorithm
			for(int i=1; i<=7; i++){
				//For each algorithm create 10 arrays with increasing size
				for(int j=1000; j<=10000; j+=1000, this.resetTimes()){
					//Fill the array with the right amount of values
					this.fillArray(j);
					switch(i){
					case 1:
						//Add the array size to the timeStore array for a better understanding of the times once the thread has completed its operations
						this.timeStore.get(i-1).add((long) j);
						break;
					case 2:	
						//Execute bubble sort
						startTime= System.nanoTime();
						this.bubbleSort();
						this.calculateExecutionTime();
						this.timeStore.get(i-1).add(this.getExecutionTime());
						//Add the data and data sorted to the array
						allArrays.add(0, array);
						allArrays.add(1,arraySorted);
						break;
					case 3:
						//Execute selection sort
						startTime= System.nanoTime();
						this.selectionSort();
						this.calculateExecutionTime();
						this.timeStore.get(i-1).add(this.getExecutionTime());
						//Add the data and data sorted to the array
						allArrays.add(2, array);
						allArrays.add(3,arraySorted);
						break;
					case 4:
						//Execute merge sort
						this.resetArraySorted();
						startTime= System.nanoTime();
						this.startMergeSort(arraySorted,0, arraySorted.size()-1);
						this.calculateExecutionTime();
						this.timeStore.get(i-1).add(this.getExecutionTime());
						//Add the data and data sorted to the array
						allArrays.add(4, array);
						allArrays.add(5,arraySorted);
						break;
					case 5:
						//Execute quick sort
						startTime= System.nanoTime();
						this.startQuickSort();
						this.calculateExecutionTime();
						this.timeStore.get(i-1).add(this.getExecutionTime());
						//Add the data and data sorted to the array
						allArrays.add(6, array);
						allArrays.add(7,arraySorted);
						break;
					case 6:
						//Execute Insertion sort
						startTime= System.nanoTime();
						this.insertionSort();
						this.calculateExecutionTime();
						this.timeStore.get(i-1).add(this.getExecutionTime());
						//Add the data and data sorted to the array
						allArrays.add(8, array);
						allArrays.add(9,arraySorted);
						break;
					case 7:
						//Execute Insertion sort
						startTime= System.nanoTime();
						this.cocktailSort();
						this.calculateExecutionTime();
						this.timeStore.get(i-1).add(this.getExecutionTime());
						//Add the data and data sorted to the array
						allArrays.add(10, array);
						allArrays.add(11,arraySorted);
						break;
					}
				}
				//After each algorithm has finished, change progress to the thread
				this.setProgress(100/6*i);
			}
		}
		return true;
	}
}
