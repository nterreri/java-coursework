package uk.ac.ucl.nterreri.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JEditorPane;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;
import javax.swing.JButton;

public class PatientEditorFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PatientEditorFrame frame = new PatientEditorFrame();
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
	public PatientEditorFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 827, 629);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textField = new JTextField();
		textField.setBounds(23, 93, 168, 19);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(251, 93, 202, 19);
		textField_1.setColumns(10);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setBounds(23, 72, 81, 15);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(251, 72, 80, 15);
		
		JLabel lblDateOfBirth = new JLabel("Date of Birth:");
		lblDateOfBirth.setBounds(23, 143, 95, 15);
		
		textField_2 = new JTextField();
		textField_2.setBounds(23, 164, 168, 19);
		textField_2.setColumns(10);
		
		JLabel lblEmergencyPhoneNumber = new JLabel("Emergency Phone:");
		lblEmergencyPhoneNumber.setBounds(251, 143, 131, 15);
		
		textField_3 = new JTextField();
		textField_3.setBounds(251, 164, 202, 19);
		textField_3.setColumns(10);
		
		JLabel lblPersonalDetails = new JLabel("Personal Details:");
		lblPersonalDetails.setBounds(31, 18, 149, 19);
		lblPersonalDetails.setFont(new Font("Dialog", Font.BOLD, 16));
		
		textField_4 = new JTextField();
		textField_4.setBounds(23, 225, 434, 19);
		textField_4.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setBounds(23, 204, 63, 15);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(19, 272, 481, 2);
		
		JLabel lblMedicalData = new JLabel("Administartive data:");
		lblMedicalData.setBounds(31, 286, 207, 19);
		lblMedicalData.setFont(new Font("Dialog", Font.BOLD, 16));
		
		JLabel lblCondition = new JLabel("Condition:");
		lblCondition.setBounds(19, 324, 73, 15);
		
		JLabel lblAppointments = new JLabel("Appointments:");
		lblAppointments.setBounds(251, 324, 104, 15);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(512, 29, 2, 559);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(517, 17, 296, 236);
		
		JToggleButton tglbtnToggleEditMode = new JToggleButton("Edit Mode Off");
		tglbtnToggleEditMode.setBounds(526, 572, 131, 25);
		
		JLabel label_1 = new JLabel("Comments:");
		label_1.setBounds(526, 265, 79, 15);
		
		JLabel label_2 = new JLabel("Billing:");
		label_2.setBounds(23, 476, 48, 15);
		
		JEditorPane editorPane_1 = new JEditorPane();
		editorPane_1.setBounds(19, 345, 214, 111);
		
		JEditorPane editorPane_2 = new JEditorPane();
		editorPane_2.setBounds(251, 345, 249, 111);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(526, 286, 287, 280);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Patient Pic", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Image Placeholder");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(null);
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Condition Pic", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("Image Placeholder");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(label);
		contentPane.setLayout(null);
		contentPane.add(lblPersonalDetails);
		contentPane.add(textField);
		contentPane.add(lblFirstName);
		contentPane.add(lblAddress);
		contentPane.add(textField_4);
		contentPane.add(lblDateOfBirth);
		contentPane.add(textField_2);
		contentPane.add(lblEmergencyPhoneNumber);
		contentPane.add(textField_3);
		contentPane.add(lblLastName);
		contentPane.add(textField_1);
		contentPane.add(label_2);
		contentPane.add(lblMedicalData);
		contentPane.add(lblCondition);
		contentPane.add(editorPane_1);
		contentPane.add(lblAppointments);
		contentPane.add(editorPane_2);
		contentPane.add(separator);
		contentPane.add(separator_1);
		contentPane.add(tabbedPane);
		contentPane.add(editorPane);
		contentPane.add(label_1);
		contentPane.add(tglbtnToggleEditMode);
		
		JButton btnNewButton = new JButton("Save Changes");
		btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
		btnNewButton.setBounds(669, 572, 144, 25);
		contentPane.add(btnNewButton);
		
		JEditorPane editorPane_3 = new JEditorPane();
		editorPane_3.setBounds(12, 490, 214, 88);
		contentPane.add(editorPane_3);
	}
}
