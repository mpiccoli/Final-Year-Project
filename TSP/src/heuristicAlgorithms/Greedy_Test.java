package heuristicAlgorithms;

import java.awt.Point;
import java.util.Vector;

public class Greedy_Test {

	public static void main(String[] args) {
		Vector<Point> data=new Vector<Point>();
		Vector<Point> results=new Vector<Point>();
		data.add(new Point(1,1));
		data.add(new Point(6,2));
		data.add(new Point(3,2));
		data.add(new Point(1,3));
		data.add(new Point(4,4));
		data.add(new Point(8,4));
		data.add(new Point(2,5));
		data.add(new Point(4,6));
		data.add(new Point(1,2));
		GreedyHeuristic gh=new GreedyHeuristic(data,results);
		try {
			gh.doInBackground();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}

	}

}
