import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ExitRequest extends JFrame implements ActionListener{
	private static final long serialVersionUID = -3522747050735126560L;
	//Global Variables
	private JButton yesButton, noButton;
	private JLabel confirm;
	
	public ExitRequest(){
		yesButton=new JButton(new ImageIcon("images/yes.png"));
		noButton=new JButton(new ImageIcon("images/no.png"));
		confirm=new JLabel("Quit Application?");
		
		yesButton.setBounds(40,35,80,80);
		noButton.setBounds(140,35,80,80);
		confirm.setBounds(80,5,200,30);
		//Round Button Edges
		yesButton.setOpaque(false);
		yesButton.setFocusPainted(false);
		yesButton.setBorderPainted(false);
		yesButton.setContentAreaFilled(false);
		yesButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		//Round Button Edges
		noButton.setOpaque(false);
		noButton.setFocusPainted(false);
		noButton.setBorderPainted(false);
		noButton.setContentAreaFilled(false);
		noButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
		yesButton.addActionListener(this);
		noButton.addActionListener(this);
		
		Container c=this.getContentPane();
		c.setLayout(null);
		c.add(yesButton);
		c.add(noButton);
		c.add(confirm);
	}
	
	@Override
	public void actionPerformed(ActionEvent actionE) {
		if(actionE.getSource().equals(yesButton)){
			System.exit(0);
		}
		if(actionE.getSource().equals(noButton)){
			this.setVisible(false);
			//Destroy the object from memory
			this.dispose();
		}
	}
}
