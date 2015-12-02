package uk.ac.ucl.nterreri.GUI;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public abstract class GUIEntryPoint extends WindowAdapter {


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
					main = new MainFrame();
					main.setVisible(false);
					
					//create visible login frame and register main frame as a listener for when it closes
					login = new LoginFrame();
					login.setVisible(true);
					login.addWindowListener(new WindowAdapter() {

						//if users closes login window otherwise, exit
						@Override
						public void windowClosing(WindowEvent e) {
							System.exit(0);
						}

						//sets itself visible when the window it is registered to (login window) is disposed of
						@Override
						public void windowClosed(WindowEvent e) {
							main.setVisible(true);
							
						}
					});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

}
