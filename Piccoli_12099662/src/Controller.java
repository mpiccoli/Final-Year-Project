import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/** 
 * This Class starts up the application GUI
 * 
 * @author Michael Piccoli - 12099662
 * @version 1.0
 * @since 5 January 2015
 * @see Main_Panel
 * @see Runnable
 * @see JFrame
 */

public class Controller {
	
	/** This is the only method included in the class which starts the application GUI
	 * 
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				//Create the window and pass the application inside the frame
				JFrame frame=new JFrame("Sorting Algorithms");
				Main_Panel startApp=new Main_Panel(frame);
				//attach the menu to the frame
				frame.setJMenuBar(startApp.menuBar);
				//attach the tool bar to the frame
				frame.getContentPane().add(startApp.toolBar, BorderLayout.NORTH);
				//position all the other elements at the centre of the window
				frame.getContentPane().add(startApp, BorderLayout.CENTER);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(800,600);
				//The window is not resizable
				frame.setResizable(false);
				frame.setVisible(true);				
			}
		});
		
	}

}
