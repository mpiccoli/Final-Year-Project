import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see Color, Graphics, Point, Vector, JLabel, JButton, JTextField, JPanel
 * 
 * This Class creates a drawable panel that the user can use to draw points and define the location of cities on the screen
 * 
 */
public class DrawingPanel extends JPanel {

	// Define the constants of this class
	private static final long serialVersionUID = 2980221873573882055L;
	// Global Variables
	private Vector<Point> vPoints;
	private JLabel numPoints;
	private boolean singleAlg;
	private Vector<Point> links;
	private boolean drawingORDisplaying;

	/*
	 * @param vec the vector containing all the coordinates of the cities
	 * 
	 * @param nPoints reference to the JLabel objects that is updated every time
	 * a new point is added
	 * 
	 * @param res the vector containing the resulting path to draw on the screen
	 * 
	 * Constructor with 3 parameters
	 * 
	 */
	public DrawingPanel(Vector<Point> vec, JLabel nPoints, Vector<Point> res) {
		vPoints = vec;
		numPoints = nPoints;
		singleAlg = false;
		links = res;
		drawingORDisplaying = false;
	}

	/*
	 * @param path reference to the vector containing the resulting path to draw
	 * on the screen
	 * 
	 * Constructor with 1 parameter
	 * 
	 */
	public DrawingPanel(Vector<Point> path) {
		links = path;
		drawingORDisplaying = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// This allows to draw points with a thickness of size 2
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		// This view displays the cities on the screen
		if (!drawingORDisplaying) {
			// Update the label containing the number of drawn points
			if (vPoints.size() > 0) {
				numPoints.setText("Points: " + vPoints.size());
				g2.setColor(Color.BLACK);
				for (int i = 0; i < vPoints.size(); i++) {
					g2.fillOval((int) vPoints.elementAt(i).getX(), (int) vPoints.elementAt(i).getY(), 6, 6);
				}
			}
			// This view creates a visible path between points
			if (singleAlg && links != null && links.size() > 0) {
				g2.setColor(new Color(82, 234, 128));
				for (int i = 0; i < links.size() - 1; i++) {
					g2.drawLine((int) links.get(i).getX(), (int) links.get(i).getY(), (int) links.get(i + 1).getX(),
							(int) links.get(i + 1).getY());
				}
			}
		}
		// This view is for when an element in the table of results is selected
		// The points are halved for a space reason, but the structure of the
		// path will not change
		else {
			if (links != null && links.size() > 0) {
				g2.setColor(Color.BLACK);
				for (int i = 0; i < links.size() - 1; i++) {
					g2.fillOval((int) links.elementAt(i).getX() / 2, (int) links.elementAt(i).getY() / 2, 6, 6);
				}
				g2.setColor(new Color(82, 234, 128));
				for (int i = 0; i < links.size() - 1; i++) {
					g2.drawLine((int) links.get(i).getX() / 2, (int) links.get(i).getY() / 2,
							(int) links.get(i + 1).getX() / 2, (int) links.get(i + 1).getY() / 2);
				}
			}
		}
	}

	/*
	 * @return a vector of type point
	 * 
	 * This method returns the list of cities with their coordinates
	 */
	public Vector<Point> getAllPoints() {
		return vPoints;
	}

	/*
	 * @param performance This defines the operations the program is required to
	 * run in order to display the correct view
	 * 
	 * @param vec This is the vector of cities
	 * 
	 * @param res This is the vector containing the final path
	 * 
	 * This method tells the program that only one algorithm is in the queue,
	 * therefore, show salesman path
	 * 
	 */
	public void performLinks(boolean performance, Vector<Point> vec, Vector<Point> res) {
		singleAlg = performance;
		vPoints = vec;
		links = res;
		this.repaint();
	}

	/*
	 * @param p This is a new Point object containing X,Y coordinates to add to
	 * the list of cities
	 * 
	 * This method passes a new city coordinates and add it to the vector of
	 * cities
	 */
	public void passPoint(Point p) {
		// Verify that the point is not already contained in the list of cities
		if (!vPoints.contains(p)) {
			vPoints.add(p);
			// Update the label containing the number of drawn points.
			numPoints.setText("Points: " + vPoints.size());
			// Refresh the view
			this.repaint();
		}
	}

	/*
	 * This method removes the last inserted point from the list of cities
	 */
	public void removeLastPoint() {
		singleAlg = false;
		if (vPoints.size() > 0) {
			// Remove last drawn point
			vPoints.remove(vPoints.lastElement());
			this.repaint();
		}
	}

	/*
	 * This method erases the vector of cities
	 */
	public void clearAllPoints() {
		singleAlg = false;
		if (!vPoints.isEmpty()) {
			// Remove all the saved points in the vector and refresh the view
			vPoints.clear();
			this.repaint();
		}
	}

	/*
	 * @param points reference to the vector containing the final path
	 * 
	 * This method refreshes the reference of the final path vector, and the
	 * view on the screen
	 */
	public void updateLinksAndRefresh(Vector<Point> points) {
		links = points;
		this.repaint();
	}
}
