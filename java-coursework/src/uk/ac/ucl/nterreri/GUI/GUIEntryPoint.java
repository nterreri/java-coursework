package uk.ac.ucl.nterreri.GUI;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;


/**
 * Patient management software entry point.<p>
 * 
 * Responsible for creating the login frame, and creating the main
 * frame listening for login frame closing.<p>
 *  
 * @author nterreri
 *
 */
public abstract class GUIEntryPoint {

	//static frames: must be instance-invariant
	private static LoginFrame login;
	private static MainFrame main;
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		try{
			extractFile("login_data");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Unable to extract login_data from jar file. Please extract this file manually to the root folder of this jar file.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
		}
		
		try{
			extractFile("patient_records");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Unable to extract patient_records from jar file. Please extract this file manually to the root folder of this jar file."
					+"Otherwise a blank file will be created for you.",null, JOptionPane.WARNING_MESSAGE);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//create visible login frame and add a listener for when it closes
					login = new LoginFrame();
					login.setVisible(true);
					login.addWindowListener(new WindowAdapter() {

						//exit if users closes login window
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
	
	/**
	 * Extracts default files from jar should these be missing from the folder from which the jar is being run.
	 * 
	 * @param name
	 * @throws IOException
	 * @see http://stackoverflow.com/questions/7168747/java-creating-self-extracting-jar-that-can-extract-parts-of-itself-out-of-the-a
	 */
	 private static void extractFile(String name) throws IOException
	    {
	        File target = new File(name);
	        if (target.exists())
	            return;

	        JOptionPane.showMessageDialog(null, name + " file not found, extracting default from jar");
	        FileOutputStream out = new FileOutputStream(target);
	        ClassLoader cl = GUIEntryPoint.class.getClassLoader();
	        InputStream in = cl.getResourceAsStream(name);

	        byte[] buf = new byte[8*1024];
	        int len;
	        while((len = in.read(buf)) != -1)
	        {
	            out.write(buf,0,len);
	        }
	        out.close();
	            in.close();
	    }
	

}
