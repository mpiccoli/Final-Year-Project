import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel{
	private Vector<Point> vPoints;
	private JLabel numPoints;
	
	public DrawingPanel(Vector<Point> vec, JLabel nPoints){
		vPoints=vec;
		numPoints=nPoints;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Update the label containing the number of drawn points
		numPoints.setText("Points: "+vPoints.size());
		for(int i=0; i<vPoints.size(); i++){
			g.drawOval((int)vPoints.elementAt(i).getX(),(int)vPoints.elementAt(i).getY(), 6, 6);
		}
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
		if(vPoints.size()>0){
			//Remove last drawn point
			vPoints.remove(vPoints.lastElement());
			this.repaint();
		}
	}
	public void clearAllPoints(){
		if(!vPoints.isEmpty()){
			//Remove all the saved points in the vector and refresh the view
			vPoints.clear();
			this.repaint();
		}
	}

}
