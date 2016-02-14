import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel{
	private static final long serialVersionUID = 2980221873573882055L;
	private Vector<Point> vPoints;
	private JLabel numPoints;
	private boolean singleAlg;
	private Vector<Point> links;
	private boolean drawingORDisplaying;

	public DrawingPanel(Vector<Point> vec, JLabel nPoints, Vector<Point> res){
		vPoints=vec;
		numPoints=nPoints;
		singleAlg=false;
		links=res;
		drawingORDisplaying=false;
	}
	public DrawingPanel(Vector<Point> path){
		links=path;
		drawingORDisplaying=true;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//This allows to draw points with a thickness of 2
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		//This view is for the first view of the application
		if(!drawingORDisplaying){
			//Update the label containing the number of drawn points
			if(vPoints.size()>0){
				numPoints.setText("Points: "+vPoints.size());
				g2.setColor(Color.BLACK);
				for(int i=0; i<vPoints.size(); i++){
					g2.fillOval((int)vPoints.elementAt(i).getX(),(int)vPoints.elementAt(i).getY(), 6, 6);
				}
			}
			//Create city links
			if(singleAlg && links!=null && links.size()>0){
				g2.setColor(Color.BLUE);
				for(int i=0; i<links.size()-1; i++){
					g2.drawLine((int)links.get(i).getX(), (int)links.get(i).getY(), (int)links.get(i+1).getX(), (int)links.get(i+1).getY());
				}
			}
		}
		//This view is for when an element in the table of results is selected
		//The points are halved for a space reason, but the structure of the path will not change
		else{
			if(links!=null && links.size()>0){
				g2.setColor(Color.BLACK);
				for(int i=0; i<links.size()-1; i++){
					g2.fillOval((int)links.elementAt(i).getX()/2,(int)links.elementAt(i).getY()/2, 6, 6);
				}
				g2.setColor(Color.BLUE);
				for(int i=0; i<links.size()-1; i++){
					g2.drawLine((int)links.get(i).getX()/2, (int)links.get(i).getY()/2, (int)links.get(i+1).getX()/2, (int)links.get(i+1).getY()/2);
				}
			}
			//In case the vector containing the results has not got a memory allocation, repaint a blank screen
			else{
				this.repaint();
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
	public void updateLinksAndRefresh(Vector<Point> points){
		links=points;
		this.repaint();
	}
}
