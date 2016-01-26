//import java.awt.Graphics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel{
	private Vector<Point> vPoints;
	private JLabel numPoints;
	private boolean singleAlg;
	private Vector<Point> links;

	public DrawingPanel(Vector<Point> vec, JLabel nPoints, Vector<Point> res){
		vPoints=vec;
		numPoints=nPoints;
		singleAlg=false;
		links=res;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Update the label containing the number of drawn points
		numPoints.setText("Points: "+vPoints.size());
		for(int i=0; i<vPoints.size(); i++){
			g.drawOval((int)vPoints.elementAt(i).getX(),(int)vPoints.elementAt(i).getY(), 6, 6);
		}
		//Create city links
		if(singleAlg==true){
			for(int i=0; i<links.size()-1; i++){
				g.drawLine((int)links.get(i).getX(), (int)links.get(i).getY(), (int)links.get(i+1).getX(), (int)links.get(i+1).getY());

			}
		}

	}
	public Vector<Point> getAllPoints(){
		return vPoints;
	}
	//Tell the program that only one algorithm is in the queue, therefore, show salesman path
	public void performLinks(boolean performance, Vector<Point> vec, Vector<Point> res){
		singleAlg=performance;
		vPoints=vec;
		links=res;
		this.repaint();
	}
	public void passPoint(Point p){
		if(!vPoints.contains(p)){
			vPoints.add(p);
			//Update the label containing the number of drawn points.
			numPoints.setText("Points: "+vPoints.size());
			//Repaint the JPanel
			this.repaint();
		}
	}
	public void removeLastPoint(){
		singleAlg=false;
		if(vPoints.size()>0){
			//Remove last drawn point
			vPoints.remove(vPoints.lastElement());
			this.repaint();
		}
	}
	public void clearAllPoints(){
		singleAlg=false;
		if(!vPoints.isEmpty()){
			//Remove all the saved points in the vector and refresh the view
			vPoints.clear();
			this.repaint();
		}
	}

}
