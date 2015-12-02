package uk.ac.ucl.nterreri.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.opencsv.CSVReader;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JButton;

public class LoginFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private ArrayList<String[]> loginData;
	private String usernameIn;
	private String passwordIn;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {


		try{

			loginData = (ArrayList<String[]>) fetchLoginData();
			for(int i = 0; i < loginData.size(); i++) {
				for(int j = 0; j<2; j++)
					loginData.get(i)[j].trim();
			}

		} catch(FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Error reading login data file: file not found.\n" + 
					"Terminating program.");
			System.exit(1);
		}
		catch (IOException e){
			e.printStackTrace();
			System.err.println("Error reading login data file.\n" + 
					"Terminating program.");
			System.exit(1);
		}
		
		for(int i = 0; i < loginData.size(); i++) {
			for(int j = 0; j<2; j++)
				System.out.println(loginData.get(i)[j]);
		}

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
		passwordField.addActionListener(this);
		
		passwordField.setBounds(270/2 - 134/2, 104, 134, 19);

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 12, 256, 2);
		
		JButton btnLogIn = new JButton("Log in");
		btnLogIn.setBounds(270/2 - 117/2, 135, 117, 25);
		btnLogIn.addActionListener(this);
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

	private static List<String[]> fetchLoginData() throws FileNotFoundException, IOException{
		CSVReader reader = new CSVReader(new FileReader("login_data"));
		return reader.readAll();
	}

	private boolean validateLogin() {
		//Checking for key validity:
		int i = 0;
		while(i < loginData.size())
		{
			//If username is found in username[],
			if(usernameIn.equalsIgnoreCase(loginData.get(i)[0]))
			{
				//, proceed to compare password[],
				if(passwordIn.equalsIgnoreCase(loginData.get(i)[1]))
					return true;
				
				//, if password does not match, login failed
				else	
					return false;
			}

			i++;
		}
		//If i is the size of loginData, then input username was not found
		if(i == loginData.size())
			return false;
		return false; // if loginData.size() is 0
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {

		usernameIn = this.usernameField.getText();
		passwordIn = new String(this.passwordField.getPassword());
		if(validateLogin()) {
			this.dispose(); //destroy this window, the main window should listen for this event
			//from this frame
		}
	}
	
}
