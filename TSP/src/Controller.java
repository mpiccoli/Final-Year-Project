import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * 
 * This Class initializes the application GUI and all its elements
 */
public class Controller {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create a new instance of the GUI class frame
				JFrame frame = new JFrame("Travelling Salesman Problem");
				MainView initApp = new MainView(frame);
				// Add a menu bar to the frame
				frame.setJMenuBar(initApp.menuBar);
				frame.getContentPane().add(initApp.toolBar, BorderLayout.NORTH);
				frame.getContentPane().add(initApp, BorderLayout.CENTER);
				// Take control of the window handling
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.setSize(1060, 700);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
	}
}
