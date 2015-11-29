package uk.ac.ucl.nterreri.GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class GUIEntryPoint {


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//GUIEntryPoint window = new GUIEntryPoint();
					//window.frame.setVisible(true);
					
					LoginFrame login = new LoginFrame();
					login.setVisible(true);
					MainFrame main = new MainFrame();
					main.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}
