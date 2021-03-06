import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * @author Michael Piccoli
 * @since October 2015
 * @version 1.0
 * @see JButton, JLabel, ActionListener, JFrame
 * 
 * This Class is used to stop the user from mistakenly close the application, and instead it pops up a confirmation window
 * asking to confirm the exit request or return to the algorithm executions.
 * 
 */
public class ExitRequest extends JFrame implements ActionListener {

	// Define the constants of this class
	private static final long serialVersionUID = -3522747050735126560L;
	// Global Variables
	private JButton yesButton, noButton;
	private JLabel confirm;

	/*
	 * Constructor
	 */
	public ExitRequest() {
		// Initiate the global variables
		yesButton = new JButton(new ImageIcon(getClass().getResource("/images/yes.png")));
		// Round Button Edges
		yesButton.setOpaque(false);
		yesButton.setFocusPainted(false);
		yesButton.setBorderPainted(false);
		yesButton.setContentAreaFilled(false);
		yesButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		noButton = new JButton(new ImageIcon(getClass().getResource("/images/no.png")));
		// Round Button Edges
		noButton.setOpaque(false);
		noButton.setFocusPainted(false);
		noButton.setBorderPainted(false);
		noButton.setContentAreaFilled(false);
		noButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		confirm = new JLabel("Quit Application?");
		// Define their locations
		yesButton.setBounds(40, 35, 80, 80);
		noButton.setBounds(140, 35, 80, 80);
		confirm.setBounds(80, 5, 200, 30);
		// Add an action listener to the buttons
		yesButton.addActionListener(this);
		noButton.addActionListener(this);
		// Create a container and add the elements to it
		Container c = this.getContentPane();
		c.setLayout(null);
		c.add(yesButton);
		c.add(noButton);
		c.add(confirm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent actionE) {
		// Action to perform when the YES button is clicked
		if (actionE.getSource().equals(yesButton)) {
			System.exit(0);
		}
		// Action to perform when the NO button is clicked
		if (actionE.getSource().equals(noButton)) {
			this.setVisible(false);
			// Destroy the object from memory
			this.dispose();
		}
	}
}
