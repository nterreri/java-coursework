package uk.ac.ucl.nterreri.GUI;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class GUIEntryPoint {


	static LoginFrame login;
	static MainFrame main;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//GUIEntryPoint window = new GUIEntryPoint();
					//window.frame.setVisible(true);
					//create main frame not yet visible
					//main = new MainFrame();
					//main.setVisible(false);
					
					//create visible login frame and register main frame as a listener for when it closes
					login = new LoginFrame();
					login.setVisible(true);
					login.addWindowListener(new WindowAdapter() {

						//if users closes login window otherwise, exit
						@Override
						public void windowClosing(WindowEvent e) {
							System.exit(0);
						}

						//when window is closed
						@Override
						public void windowClosed(WindowEvent e) {
							if(LoginFrame.getOutcome()) {
								main = new MainFrame();
								main.setVisible(true);
							}
							//else login frame closes and application terminates
						}
					});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

}
