package uk.ac.ucl.nterreri.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.JEditorPane;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;

import uk.ac.ucl.nterreri.task3.Patient;

import java.awt.Color;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;

public class PatientEditorFrame extends JDialog implements ActionListener {

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
	private JLabel lblPatientPic;
	private JLabel lblConditionPic;
	private JScrollPane scrollPaneCondition;
	private JScrollPane scrollPaneAppointments;
	private JScrollPane scrollPaneBilling;
	private JScrollPane scrollPaneComments;
	public	JTabbedPane tabbedPane;
	private JPanel panelPatientPic;
	private JPanel panelConditionPic;
	private EditorFrameMouseListener mouseListener;
	
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
	 * @wbp.parser.constructor
	 */
	
	
	//the constructors call the jdialog constructor to establish parent-child relationship between mainframe and any (finite) number of
	//editorframe jdialogs.
	public PatientEditorFrame(JFrame parent, Patient patient) {
		super(parent);
		//setModalityType(Dialog.ModalityType.MODELESS); //In vain attempted to make parent jframe focus shift parent on top of children, see http://stackoverflow.com/questions/22259765/how-to-make-a-jdialog-not-always-on-top-of-parent
		this.indexInArrayList = -1;
		this.patient = patient;
		
		initialize();
		//drawImage(); //does not draw image with newly created patient, for there will be no image
	}
	
	public PatientEditorFrame(JFrame parent, Patient patient, int indexInArrayList) {
		super(parent);
		//setModalityType(Dialog.ModalityType.MODELESS); //In vain attempted to make parent jframe focus shift parent on top of children, see http://stackoverflow.com/questions/22259765/how-to-make-a-jdialog-not-always-on-top-of-parent
		this.indexInArrayList = indexInArrayList;
		this.patient = patient;

		initialize();
		
	}

	private void initialize() {
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
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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
		
		mouseListener = new EditorFrameMouseListener();
		
		panelPatientPic = new JPanel();
		tabbedPane.addTab("Patient Pic", null, panelPatientPic, null);
		panelPatientPic.setLayout(new BorderLayout(0, 0));
		
		lblPatientPic = new JLabel("Patient Profile Image");
		lblPatientPic.setHorizontalAlignment(SwingConstants.CENTER);
		
		panelPatientPic.add(lblPatientPic);
		
		
		panelConditionPic = new JPanel();
		tabbedPane.addTab("Condition Pic", null, panelConditionPic, null);
		panelConditionPic.setLayout(new BorderLayout(0, 0));
		
		lblConditionPic = new JLabel("Image Placeholder");
		lblConditionPic.setHorizontalAlignment(SwingConstants.CENTER);
		panelConditionPic.add(lblConditionPic);
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
		
		if(patient.getPatientPicture() != null)
			drawImage();	//draws patient profile picture image and scales to tabbedPane size
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
				//txtAreaBilling.setBackground(textFirstName.getBackground());
				lblPatientPic.addMouseListener(this.mouseListener);
				

				
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
				//txtAreaBilling.setBackground(UIManager.getColor("TextField.background"));//did not work to set background color to the same as a disabled jtextfield
				//txtAreaBilling.setBackground(contentPane.getBackground());

			}
		} else if(e.getSource() == btnSaveButton) {
			
			
			//String errorMessage = "";
			
			//originally, more varied exceptions than just were predicted than just ParseException
			try {
				patient.setLastNameNoCheck(textLastName.getText());
				patient.setFirstNameNoCheck(textFirstName.getText());
				patient.setDOB(textDOB.getText());
				patient.setAddress(textAddress.getText());
				patient.setEmergencyPhone(textPhone.getText());
				patient.setCondition(txtAreaCondition.getText());
				patient.setAppointments(txtAreaAppointments.getText());
				patient.setBilling(txtAreaBilling.getText());
				patient.setComments(txtAreaComments.getText());
				
				if(indexInArrayList < 0)
					patient.addRecord();
				else
					Patient.editRecord(patient, indexInArrayList);
				
				try {
					Patient.updateRecordsFile();
					JOptionPane.showMessageDialog(this, "Patient edit successful");
				} catch (IOException excp) {
					JOptionPane.showMessageDialog(this, "Error", "Patient edit unsuccessful:\n" +
																"unable to write to file.", JOptionPane.ERROR_MESSAGE);
				}
				
			} catch (ParseException excp) {
				JOptionPane.showMessageDialog(this, "DOB must be in dd/mm/yyyy format.\n", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException excp) {
				JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits long.\n", "Error", JOptionPane.ERROR_MESSAGE);
			}	
			//would have been better to show a single, cumulative error message
			/*if(errorMessage.length() > 1)
				JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);*/
			
			
		} 
	}
	
	//felt like placing this in a separate class would fragment the source code when this is only needed within the present
	//Jdialog extension
	private class EditorFrameMouseListener extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			//Object [] options = {"From file", "From URL", "Cancel" };
			
			/*JOptionPane.showOptionDialog(PatientEditorFrame.this, "How would you like to change the picture:", "Picture Source Selection", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);*/
			
			//TODO credit: http://stackoverflow.com/questions/1816458/getting-hold-of-the-outer-class-object-from-the-inner-class-object
			EditorPicChooser dialog = new EditorPicChooser(PatientEditorFrame.this, patient);
			dialog.setVisible(true);
		}
		
		/*public PatientEditorFrame getOuter() {
			return PatientEditorFrame.this;
		}*/
		
	}
	
	private void drawImage() {
		//TODO: credit http://stackoverflow.com/questions/8333802/displaying-an-image-in-java-swing
				//TODO: and credit http://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
				try {
					Image img = ImageIO.read(new URL(patient.getPatientPicture()));//<-this may throw an IOException or MalformedURLException
					BufferedImage resizedImg = new BufferedImage(tabbedPane.getWidth(), tabbedPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2 = resizedImg.createGraphics();
					
					g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g2.drawImage(img, 0, 0, tabbedPane.getWidth(), tabbedPane.getHeight(), null);
					g2.dispose();
					
					lblPatientPic.setIcon(new ImageIcon(resizedImg));	//<-this throws a null pointer exception where the URL does not point to an image
																		
				} catch (Exception e) {							
					JOptionPane.showMessageDialog(this, (Object)("Unable to load profile image\n"
							+ "No patient picture will be displayed"),
							"Error displaying image", JOptionPane.WARNING_MESSAGE);
				}
	}
}
