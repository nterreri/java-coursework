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
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.JEditorPane;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;

import uk.ac.ucl.nterreri.task3.Patient;

import java.awt.Color;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;

public class PatientEditorFrame extends JFrame implements ActionListener{

	private Patient patient;
	private int indexInArrayList;
	private JPanel contentPane;
	private JTextField textFirstName;
	private JTextField textLastName;
	private JTextField textDOB;
	private JTextField textPhone;
	private JTextField textAddress;
	private JTextArea txtAreaCondition;
	private JTextArea txtAreaAppointments;
	private JTextArea txtAreaComments;
	private JTextArea txtAreaBilling;
	private JButton btnSaveButton;
	private JToggleButton tglbtnToggleEditMode;
	private JComboBox comboBox;
	private JLabel lblNewLabel;
	private JLabel label;
	private JScrollPane scrollPaneCondition;
	private JScrollPane scrollPaneAppointments;
	private JScrollPane scrollPaneBilling;
	private JScrollPane scrollPaneComments;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
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
	}*/

	/**
	 * Create the frame.
	 */
	public PatientEditorFrame(Patient patient, int indexInArrayList) {
		this.indexInArrayList = indexInArrayList;
		this.patient = patient;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 827, 629);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textFirstName = new JTextField();
		textFirstName.setEditable(false);
		textFirstName.setBounds(23, 93, 168, 19);
		textFirstName.setColumns(10);
		textFirstName.setText(patient.getFName());
		
		textLastName = new JTextField();
		textLastName.setEditable(false);
		textLastName.setBounds(251, 93, 202, 19);
		textLastName.setColumns(10);
		textLastName.setText(patient.getLName());
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setBounds(23, 72, 81, 15);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(251, 72, 80, 15);
		
		JLabel lblDateOfBirth = new JLabel("Date of Birth:");
		lblDateOfBirth.setBounds(23, 143, 95, 15);
		
		textDOB = new JTextField();
		textDOB.setEditable(false);
		textDOB.setBounds(23, 164, 168, 19);
		textDOB.setColumns(10);
		textDOB.setText(patient.getDOB());
		
		JLabel lblEmergencyPhoneNumber = new JLabel("Emergency Phone:");
		lblEmergencyPhoneNumber.setBounds(251, 143, 131, 15);
		
		textPhone = new JTextField();
		textPhone.setEditable(false);
		textPhone.setBounds(251, 164, 202, 19);
		textPhone.setColumns(10);
		textPhone.setText(patient.getEmergencyPhone());
		
		JLabel lblPersonalDetails = new JLabel("Personal Details:");
		lblPersonalDetails.setBounds(31, 18, 149, 19);
		lblPersonalDetails.setFont(new Font("Dialog", Font.BOLD, 16));
		
		textAddress = new JTextField();
		textAddress.setEditable(false);
		textAddress.setBounds(23, 225, 434, 19);
		textAddress.setColumns(10);
		textAddress.setText(patient.getAddress());
		
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
		
		tglbtnToggleEditMode = new JToggleButton("Edit Mode Off");
		tglbtnToggleEditMode.setBounds(526, 572, 131, 25);
		tglbtnToggleEditMode.addActionListener(this);
		
		JLabel label_1 = new JLabel("Comments:");
		label_1.setBounds(527, 284, 79, 15);
		
		JLabel label_2 = new JLabel("Billing:");
		label_2.setBounds(19, 476, 48, 15);
		
		txtAreaCondition = new JTextArea();
		txtAreaCondition.setEditable(false);
		//txtAreaCondition.setBounds(19, 345, 214, 111);
		txtAreaCondition.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		txtAreaCondition.setLineWrap(true);
		scrollPaneCondition = new JScrollPane(txtAreaCondition);
		scrollPaneCondition.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneCondition.setBounds(19, 345, 214, 111);
		contentPane.add(scrollPaneCondition);
		//contentPane.add(txtAreaCondition);
		
		txtAreaAppointments = new JTextArea();
		txtAreaAppointments.setEditable(false);
		//txtAreaAppointments.setBounds(251, 345, 249, 111);
		txtAreaAppointments.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		txtAreaAppointments.setLineWrap(true);
		scrollPaneAppointments = new JScrollPane(txtAreaAppointments);
		scrollPaneAppointments.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneAppointments.setBounds(251, 345, 249, 111);
		contentPane.add(scrollPaneAppointments);
		
		txtAreaComments = new JTextArea();
		txtAreaComments.setEditable(false);
		//txtAreaComments.setBounds(526, 311, 287, 255);
		txtAreaComments.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		txtAreaComments.setLineWrap(true);
		scrollPaneComments= new JScrollPane(txtAreaComments);
		scrollPaneComments.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneComments.setBounds(526, 311, 287, 255);
		contentPane.add(scrollPaneComments);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Patient Pic", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("Image Placeholder");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(null);
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Condition Pic", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		label = new JLabel("Image Placeholder");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(label);
		contentPane.setLayout(null);
		contentPane.add(lblPersonalDetails);
		contentPane.add(textFirstName);
		contentPane.add(lblFirstName);
		contentPane.add(lblAddress);
		contentPane.add(textAddress);
		contentPane.add(lblDateOfBirth);
		contentPane.add(textDOB);
		contentPane.add(lblEmergencyPhoneNumber);
		contentPane.add(textPhone);
		contentPane.add(lblLastName);
		contentPane.add(textLastName);
		contentPane.add(label_2);
		contentPane.add(lblMedicalData);
		contentPane.add(lblCondition);
		//contentPane.add(txtAreaCondition);
		contentPane.add(lblAppointments);
		//contentPane.add(txtAreaAppointments);
		contentPane.add(separator);
		contentPane.add(separator_1);
		contentPane.add(tabbedPane);
		//contentPane.add(txtAreaComments);
		contentPane.add(label_1);
		contentPane.add(tglbtnToggleEditMode);
		
		btnSaveButton = new JButton("Save Changes");
		btnSaveButton.setHorizontalAlignment(SwingConstants.LEADING);
		btnSaveButton.setBounds(669, 572, 144, 25);
		btnSaveButton.addActionListener(this);
		contentPane.add(btnSaveButton);
		
		txtAreaBilling = new JTextArea();
		txtAreaBilling.setEditable(false);
		//txtAreaBilling.setBounds(19, 490, 214, 88);
		txtAreaBilling.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		txtAreaBilling.setLineWrap(true);
		scrollPaneBilling= new JScrollPane(txtAreaBilling);
		scrollPaneBilling.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneBilling.setBounds(19, 490, 214, 88);
		contentPane.add(scrollPaneBilling);
		//contentPane.add(scrollPaneComments);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Select Condition Pic"}));
		comboBox.setEnabled(false);
		comboBox.setBounds(517, 250, 214, 24);
		contentPane.add(comboBox);
		
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == tglbtnToggleEditMode) {
			if(tglbtnToggleEditMode.isSelected()) {
				tglbtnToggleEditMode.setText("Edit Mode On");
				textFirstName.setEditable(true);
				textLastName.setEditable(true);
				textDOB.setEditable(true);
				textPhone.setEditable(true);
				textAddress.setEditable(true);
				txtAreaCondition.setEditable(true);
				txtAreaAppointments.setEditable(true);
				txtAreaComments.setEditable(true);
				txtAreaBilling.setEditable(true);

				//txtAreaBilling.setBackground(UIManager.getColor("TextField.background"));//did not work to set background color to the same as a disabled jtextfield
				
			} else {
				tglbtnToggleEditMode.setText("Edit Mode Off");
				textFirstName.setEditable(false);
				textLastName.setEditable(false);
				textDOB.setEditable(false);
				textPhone.setEditable(false);
				textAddress.setEditable(false);
				txtAreaCondition.setEditable(false);
				txtAreaAppointments.setEditable(false);
				txtAreaComments.setEditable(false);
				txtAreaBilling.setEditable(false);

			}
		} else if(e.getSource() == btnSaveButton) {
			Patient.deleteEntry(indexInArrayList);
			Patient.editRecord(patient, indexInArrayList);
			try {
				Patient.updateRecordsFile();
			} catch (IOException excp) {
				JOptionPane.showMessageDialog(this, "Error", "Patient edit unsuccessful:\n" +
															"unable to write to file.", JOptionPane.ERROR_MESSAGE);
			}
			JOptionPane.showMessageDialog(this, "Patient edit successful");
		}
		//TODO add listeners to every text field that call setters for fields in the patient class
	}
}
