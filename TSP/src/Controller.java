import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Controller {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				JFrame frame=new JFrame("TSP");
				MainView initApp=new MainView(frame);
				frame.setJMenuBar(initApp.menuBar);
				frame.getContentPane().add(initApp.toolBar, BorderLayout.NORTH);
				frame.getContentPane().add(initApp, BorderLayout.CENTER);
				//Take control of the window handling
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.setSize(1100, 700);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
	}

}