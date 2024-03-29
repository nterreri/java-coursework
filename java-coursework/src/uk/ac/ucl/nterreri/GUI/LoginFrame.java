package uk.ac.ucl.nterreri.GUI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.opencsv.CSVReader;

/**
 * GUI frame to access the patient management system. <p>
 * 
 * Prompts user for input and only allows access if user enters correct login details.<p>
 * 
 * Closes the program if it is unable to retrieve login data from file.<p>
 * 
 * Implements ActionListener to be the listener to its own components.<p>
 * 
 * @author nterreri
 *
 */
public class LoginFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4806215415944464740L;
	//GUI components:
	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private ArrayList<String[]> loginData;
	private JButton btnLogIn;
	
	private String usernameIn;
	private String passwordIn;
	private static boolean success = false;	//login outcome flag
	
	/**private static boolean instanceExists = false;
	private static LoginFrame instance;
	/**
	 * Very rough get instance method. 
	 * 
	 * Unused. But if the constructor were private for secutrity
	 * reasons, it would make sense to use it.
	 * 
	 * @return
	 */
	/*public LoginFrame getInstance() {
		if(instanceExists)
			return instance;
		else
			return new LoginFrame();
	}*/
	
	/**
	 * Reads loginData from file, then creates the frame.
	 * 
	 * @see #fetchLoginData()
	 */
	public LoginFrame() {
		
		//Retrive login data:
		try{

			loginData = (ArrayList<String[]>) fetchLoginData();
			for(int i = 0; i < loginData.size(); i++) {
				for(int j = 0; j<2; j++)
					loginData.get(i)[j].trim();
			}

		} catch (FileNotFoundException e) {
			
			JOptionPane.showMessageDialog(this, "Fatal error: login data file not found", "Fatal", JOptionPane.ERROR_MESSAGE);
			System.err.println("Error reading login data file: file not found.\n" + 
					"Terminating program.");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e){
			
			JOptionPane.showMessageDialog(this, "Fatal error: error reading login data", "Fatal", JOptionPane.ERROR_MESSAGE);
			System.err.println("Error reading login data file.\n" + 
					"Terminating program.");
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(this, "Fatal error: see console for details", "Fatal", JOptionPane.ERROR_MESSAGE);
			System.err.println("Error reading login data file.\n" + 
					"Terminating program.");
			e.printStackTrace();
			System.exit(1);
		}
		
		//create the frame(mostly swing-designer-generated):
		createFrame();

		//register frame as actionlistener:
		usernameField.addActionListener(this);
		passwordField.addActionListener(this);
		btnLogIn.addActionListener(this);	
	}
	
	/**
	 * Designer-generated create frame method.
	 */
	private void createFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 280, 252);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPatientManagementSystem = new JLabel("Patient Management System");
		lblPatientManagementSystem.setBounds(270/2 - 260/2, 5, 260, 19);
		lblPatientManagementSystem.setFont(new Font("Dialog", Font.BOLD, 16));
		contentPane.add(lblPatientManagementSystem);

		JPanel panel = new JPanel();
		panel.setBounds(5, 47, 273, 176);
		contentPane.add(panel);

		JLabel label = new JLabel("Username");
		label.setBounds(270/2 - 72/2, 19, 72, 15);

		usernameField = new JTextField();
		usernameField.setBounds(270/2 - 134/2, 47, 134, 19);
		usernameField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(270/2 - 74/2, 77, 74, 15);

		passwordField = new JPasswordField();
		
		passwordField.setBounds(270/2 - 134/2, 104, 134, 19);

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 12, 256, 2);
		
		btnLogIn = new JButton("Log in");
		btnLogIn.setBounds(270/2 - 117/2, 135, 117, 25);
		panel.setLayout(null);
		panel.add(label);
		panel.add(usernameField);
		panel.add(lblPassword);
		panel.add(separator);
		panel.add(btnLogIn);
		panel.add(passwordField);

		JLabel label_1 = new JLabel("Login");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Dialog", Font.BOLD, 16));
		label_1.setBounds(270/2 - 263/2, 22, 263, 19);
		contentPane.add(label_1);
		
	}
	
	/**
	 * Reads login data from file.<p>
	 * 
	 * Uses opencsv.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @see http://opencsv.sourceforge.net/
	 */
	private static List<String[]> fetchLoginData() throws FileNotFoundException, IOException{
		@SuppressWarnings("resource")
		CSVReader reader = new CSVReader(new FileReader(System.getProperty("user.dir") + "/login_data"));
		return reader.readAll();
	}

	/**
	 * 
	 * @return true if the login succeeded, false otherwise
	 */
	private boolean validateLogin() {
		//assume login is going to fail:
		boolean result = false;
		
		//Checking for key validity:
		int i = 0;
		for(;i < loginData.size(); i++)
		{
			if(usernameIn.equalsIgnoreCase(loginData.get(i)[0]))//If username is found in loginData,
			{
				result = (passwordIn.equalsIgnoreCase(loginData.get(i)[1])); //, proceed to compare password,
				break;//, if password does not match, login failed
			}
		}
		//If i is the size of loginData, then input username was not found
		return result;
	}

	/**
	 * Private field accessor.
	 * 
	 * @return the value of the success field.
	 */
	public static boolean getOutcome() {
		return success;
	}
	
	/**
	 * actionPerformed override.<p>
	 * 
	 * Reacts to user-action events.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		usernameIn = this.usernameField.getText();
		passwordIn = new String(this.passwordField.getPassword());
		if(validateLogin()) {
			success = true;
			this.dispose(); 
			/* dispose() triggers a window event caught by this frame (the frame catches its own
			* closing event, a check to static boolean success is performed to decide whether
			* to allow the user to proceed to the patient records management frame*/
		} else {
			JOptionPane.showMessageDialog(null, "Login failed");
		}
	}
	
}
