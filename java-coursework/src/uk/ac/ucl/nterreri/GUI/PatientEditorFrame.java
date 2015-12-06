package uk.ac.ucl.nterreri.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import uk.ac.ucl.nterreri.patient.Patient;

/**
 * Extends JDialog to set up frame parent-child relationship between originating MainFrame frame
 * and patient editor frame.<p>
 * 
 * Sets up a JEditorPane to display a hyperlink to the location of the patient condition.<p>
 * 
 * Is its own Action and Hyperlink Listener to handled user clicking buttons and condition
 * hyperlink.<p>
 * 
 * Holds simple nested classes (only used for the present class) to allow the user to modify
 * patient pictures by right-clicking on them.<p>
 * 
 * Also displays patient pictures by resizing them to fit picture panels (WARNING: may become slow
 * while rendering these).
 * 
 * @author nterreri
 *
 */
public class PatientEditorFrame extends JDialog implements ActionListener, HyperlinkListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8708149947882606477L;
	private Patient patient;
	private int indexInArrayList;
	private JPanel contentPane;
	private JTextField textFirstName;
	private JTextField textLastName;
	private JTextField textDOB;
	private JTextField textPhone;
	private JTextField textAddress;
	private JEditorPane txtEditorCondition; //<- cannot be textarea because must allow for hyperlink
	private JTextArea txtAreaAppointments;
	private JTextArea txtAreaComments;
	private JTextArea txtAreaBilling;
	private JButton btnSaveButton;
	private JToggleButton tglbtnToggleEditMode;
	private JScrollPane scrollPaneCondition;
	private JScrollPane scrollPaneAppointments;
	private JScrollPane scrollPaneBilling;
	private JScrollPane scrollPaneComments;
	JTabbedPane tabbedPane;		//<- has to be visible from JDialog generated from this frame (on second though, should have made getters, no time now)
	private JPanel panelPatientPic;
	private JPanel panelConditionPic;
	@SuppressWarnings("rawtypes")
	JComboBox comboBox;			//<- has to be visible from JDialog generated from this frame
	PicturePopupMenu popupMenu;	//<- has to be visible from JDialog generated from this frame
	JLabel lblPatientPic;		//<- has to be visible from JDialog generated from this frame
	JLabel lblConditionPic;		//<- has to be visible from JDialog generated from this frame
	private EditorFrameMouseListener mouseListener;
	private JTextField textCURLOverride;
	private JLabel lblEnterConditionUrl;

	/**
	 * Launch the application.
	 * debug only
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
	 * The constructor relies on the class parent JDialog constructor to establish parent-child relationship between
	 * MainFrame instance and any (finite) number of EditorFrame JDialogs. So that if the MainFrame instance is closed,
	 * so are all the EditorFrames.<p>
	 *
	 * @param parent
	 * @param patient
	 */
	public PatientEditorFrame(JFrame parent, Patient patient) {
		super(parent);
		/*In vain attempted to make parent jframe focus shift parent on top of children, 
		 * see http://stackoverflow.com/questions/22259765/how-to-make-a-jdialog-not-always-on-top-of-parent
		 * PatientEditorFrame should be a jdialog in order to establish parent-child frame relationship between 
		 * instances of this class and their originating MainFrame, so that if the MainFrame is closed,
		 * all of them are also closed.
		//setModalityType(Dialog.ModalityType.MODELESS);this.indexInArrayList = -1;*/

		this.patient = patient;
		this.indexInArrayList = -1;

		initialize();

	}

	
	/**
	 * The constructor relies on the class parent JDialog constructor to establish parent-child relationship between
	 * MainFrame instance and any (finite) number of EditorFrame JDialogs. So that if the MainFrame instance is closed,
	 * so are all the EditorFrames.<p>
	 * 
	 * Variant receiving an index in Patient.patientRecords from the selected item in a JTable view in MainFrame.<p>
	 * 
	 * @param parent
	 * @param patient
	 * @param indexInArrayList
	 */
	public PatientEditorFrame(JFrame parent, Patient patient, int indexInArrayList) {
		super(parent);
		/*In vain attempted to make parent jframe focus shift parent on top of children, 
		 * see http://stackoverflow.com/questions/22259765/how-to-make-a-jdialog-not-always-on-top-of-parent
		 * PatientEditorFrame should be a jdialog in order to establish parent-child frame relationship between 
		 * instances of this class and their originating MainFrame, so that if the MainFrame is closed,
		 * all of them are also closed.
		//setModalityType(Dialog.ModalityType.MODELESS);this.indexInArrayList = -1;*/

		this.indexInArrayList = indexInArrayList;
		this.patient = patient;

		initialize();

	}

	
	/**
	 * Creates the frame and registers listeners. Mostly swing-designer generated.<p>
	 * 
	 * Used in constructor calls.<p>
	 * 
	 * Sets up a hyperlink in the condition JEditorPane.<p>
	 * 
	 * Calls refreshPictures() to render pictures.<p>
	 * 
	 * @see #refreshPictures()
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {

		mouseListener = new EditorFrameMouseListener();
		popupMenu = new PicturePopupMenu();

		setResizable(false);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 827, 629);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		Border textBorder = BorderFactory.createLineBorder(Color.BLACK);

		textFirstName = new JTextField();
		textFirstName.setEditable(false);
		textFirstName.setBorder(textBorder);
		textFirstName.setColumns(10);
		textFirstName.setText(patient.getFName());

		textLastName = new JTextField();
		textLastName.setEditable(false);
		textLastName.setBorder(textBorder);
		textLastName.setColumns(10);
		textLastName.setText(patient.getLName());

		JLabel lblFirstName = new JLabel("First Name:");

		JLabel lblLastName = new JLabel("Last Name:");

		JLabel lblDateOfBirth = new JLabel("Date of Birth:");

		textDOB = new JTextField();
		textDOB.setEditable(false);
		textDOB.setBorder(textBorder);
		textDOB.setColumns(10);
		textDOB.setText(patient.getDOB());

		JLabel lblEmergencyPhoneNumber = new JLabel("Emergency Phone:");

		textPhone = new JTextField();
		textPhone.setEditable(false);
		textPhone.setBorder(textBorder);
		textPhone.setColumns(10);
		textPhone.setText(patient.getEmergencyPhone());

		JLabel lblPersonalDetails = new JLabel("Personal Details:");
		lblPersonalDetails.setFont(new Font("Dialog", Font.BOLD, 16));

		textAddress = new JTextField();
		textAddress.setEditable(false);
		textAddress.setBorder(textBorder);
		textAddress.setColumns(10);
		textAddress.setText(patient.getAddress());

		JLabel lblAddress = new JLabel("Address:");

		JSeparator separator = new JSeparator();

		JLabel lblMedicalData = new JLabel("Administartive data:");
		lblMedicalData.setFont(new Font("Dialog", Font.BOLD, 16));

		JLabel lblCondition = new JLabel("Condition:");

		JLabel lblAppointments = new JLabel("Appointments:");

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);

		tglbtnToggleEditMode = new JToggleButton("Edit Mode Off");
		tglbtnToggleEditMode.addActionListener(this);

		JLabel label_1 = new JLabel("Comments:");

		JLabel label_2 = new JLabel("Billing:");

		//sets hyperlink in editor pane from patient.condition data
		txtEditorCondition = new JEditorPane("text/html", "<a href=\"" + patient.getConditionURL() + "\">" + patient.getCondition() + "</a>");
		txtEditorCondition.addHyperlinkListener(this);
		txtEditorCondition.setEditable(false);
		txtEditorCondition.setBorder(textBorder);

		scrollPaneCondition = new JScrollPane(txtEditorCondition);
		scrollPaneCondition.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		txtAreaAppointments = new JTextArea();
		txtAreaAppointments.setEditable(false);
		txtAreaAppointments.setBorder(textBorder);
		txtAreaAppointments.setLineWrap(true);
		scrollPaneAppointments = new JScrollPane(txtAreaAppointments);
		scrollPaneAppointments.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		txtAreaComments = new JTextArea();
		txtAreaComments.setEditable(false);
		txtAreaComments.setBorder(textBorder);
		txtAreaComments.setLineWrap(true);
		scrollPaneComments= new JScrollPane(txtAreaComments);
		scrollPaneComments.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		panelPatientPic = new JPanel();
		tabbedPane.addTab("Patient Pic", null, panelPatientPic, null);
		panelPatientPic.setLayout(new BorderLayout(0, 0));
		panelPatientPic.setBounds(517, 17, 296, 236);

		lblPatientPic = new JLabel();
		lblPatientPic.setHorizontalAlignment(SwingConstants.CENTER);
		lblPatientPic.setIcon(null);
		panelPatientPic.add(lblPatientPic);

		panelConditionPic = new JPanel();
		tabbedPane.addTab("Condition Pic", null, panelConditionPic, null);
		panelConditionPic.setLayout(new BorderLayout(0, 0));
		panelConditionPic.setBounds(517, 17, 296, 236);

		lblConditionPic = new JLabel();
		lblConditionPic.setHorizontalAlignment(SwingConstants.CENTER);
		lblConditionPic.setIcon(null);
		panelConditionPic.add(lblConditionPic);

		btnSaveButton = new JButton("Save Changes");
		btnSaveButton.setHorizontalAlignment(SwingConstants.LEADING);
		btnSaveButton.addActionListener(this);

		txtAreaBilling = new JTextArea();
		txtAreaBilling.setEditable(false);
		txtAreaBilling.setBorder(textBorder);
		txtAreaBilling.setLineWrap(true);
		scrollPaneBilling= new JScrollPane(txtAreaBilling);
		scrollPaneBilling.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		comboBox = new JComboBox();
		comboBox.setModel(fetchComboBoxModel());
		comboBox.setEnabled(true);
		comboBox.addActionListener(this);

		textCURLOverride = new JTextField();
		textCURLOverride.setBorder(textBorder);
		textCURLOverride.setColumns(10);
		textCURLOverride.setEditable(false);

		lblEnterConditionUrl = new JLabel("Override default condition URL:");
		lblEnterConditionUrl.setVerticalAlignment(SwingConstants.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(14)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(12)
										.addComponent(lblPersonalDetails))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(4)
										.addComponent(lblFirstName)
										.addGap(147)
										.addComponent(lblLastName))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(4)
										.addComponent(textFirstName, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(textLastName, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(4)
										.addComponent(lblDateOfBirth)
										.addGap(133)
										.addComponent(lblEmergencyPhoneNumber))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(4)
										.addComponent(textDOB, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(textPhone, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(4)
										.addComponent(lblAddress))
								.addComponent(separator, GroupLayout.PREFERRED_SIZE, 481, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(12)
										.addComponent(lblMedicalData, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(4)
										.addComponent(lblCondition)
										.addGap(152)
										.addComponent(lblEnterConditionUrl, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_contentPane.createSequentialGroup()
																.addGap(8)
																.addComponent(label_2))
														.addGroup(gl_contentPane.createSequentialGroup()
																.addGap(4)
																.addComponent(scrollPaneBilling, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)))
												.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
														.addGap(4)
														.addComponent(scrollPaneCondition, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)))
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addGap(12)
														.addComponent(textCURLOverride, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_contentPane.createSequentialGroup()
														.addGap(48)
														.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
																.addComponent(lblAppointments)
																.addComponent(scrollPaneAppointments, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)))))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(4)
										.addComponent(textAddress, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)))
						.addGap(12)
						.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(3)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(10)
										.addComponent(label_1))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(9)
										.addComponent(tglbtnToggleEditMode, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
										.addGap(12)
										.addComponent(btnSaveButton, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(9)
										.addComponent(scrollPaneComments, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
								.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
						.addContainerGap())
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(13)
										.addComponent(lblPersonalDetails)
										.addGap(35)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblFirstName)
												.addComponent(lblLastName))
										.addGap(6)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(textFirstName, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
												.addComponent(textLastName, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
										.addGap(31)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblDateOfBirth)
												.addComponent(lblEmergencyPhoneNumber))
										.addGap(6)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addComponent(textDOB, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
														.addGap(21)
														.addComponent(lblAddress))
												.addComponent(textPhone, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textAddress, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
										.addGap(28)
										.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(12)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addComponent(lblMedicalData)
														.addGap(19)
														.addComponent(label_2))
												.addComponent(lblAppointments))
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addGap(8)
														.addComponent(scrollPaneBilling, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_contentPane.createSequentialGroup()
														.addGap(6)
														.addComponent(scrollPaneAppointments, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)))
										.addGap(24)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblCondition)
												.addComponent(lblEnterConditionUrl))
										.addGap(6)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(scrollPaneCondition, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addGap(6)
														.addComponent(textCURLOverride, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(24)
										.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 559, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(245)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(10)
										.addComponent(label_1)
										.addGap(12)
										.addComponent(scrollPaneComments, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
												.addComponent(tglbtnToggleEditMode, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnSaveButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addContainerGap()
										.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(27, Short.MAX_VALUE))
				);
		contentPane.setLayout(gl_contentPane);


		refreshPictures();

	}

	
	/**
	 * Calls drawPicture() to scale picture to picture pane.
	 * Displays error if failed.
	 * 
	 * @see #drawPicture(String, JPanel)
	 */
	private void refreshPictures() {
		try {
			if(!( patient.getPatientPicture() == null || patient.getPatientPicture().length() == 0 ))
				drawPicture(patient.getPatientPicture(), panelPatientPic);	//draws patient profile picture image and scales to tabbedPane size
			if(patient.getConditionPictures() != null) 
				drawPicture(patient.getConditionPictures()[0], panelConditionPic);	//draws patient profile picture image and scales to tabbedPane size
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, (Object)("Unable to load one or more images\n"
					+ "Some pictures will not be displayed"),
					"Error loading image", JOptionPane.WARNING_MESSAGE);
		}		
	}

	
	/**
	 * 
	 * @return the combobox model based on the content of patient.conditionPictures
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	DefaultComboBoxModel fetchComboBoxModel() {
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		//comboBoxModel.addElement("- Select Condition Pic -");	//removed coaching to extract an index for the condition pic

		String[] model = patient.getConditionPictures();

		if(model != null)	//restOfModel will be null where there are no condition pics
			for(int i = 0; i < model.length; i++)
				comboBoxModel.addElement(model[i]);

		return comboBoxModel;
	}

	
	/**
	 * Action performed implementation to listen for Toggle Edit Mode button, Save Button and combo box
	 * user action.<p>
	 * 
	 * The first adds/removes editable flags from text fields and panes, and mouse
	 * listeners from the picture panes.<p>
	 * 
	 * The second, updates the values in the current patient instance with the data in the 
	 * text fields and the Strings recording the URIs of the pictures.<p>
	 * 
	 * Then updates on-file patient records through Patient class static method.
	 */

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
				textCURLOverride.setEditable(true);
				txtEditorCondition.setEditable(true);
				txtAreaAppointments.setEditable(true);
				txtAreaComments.setEditable(true);
				txtAreaBilling.setEditable(true);
				//txtAreaBilling.setBackground(textFirstName.getBackground());
				lblPatientPic.addMouseListener(mouseListener);
				lblConditionPic.addMouseListener(mouseListener);


			} else {
				tglbtnToggleEditMode.setText("Edit Mode Off");
				textFirstName.setEditable(false);
				textLastName.setEditable(false);
				textDOB.setEditable(false);
				textPhone.setEditable(false);
				textAddress.setEditable(false);
				textCURLOverride.setEditable(false);
				//txtEditorCondition.setEditable(false); see below
				txtAreaAppointments.setEditable(false);
				txtAreaComments.setEditable(false);
				txtAreaBilling.setEditable(false);
				//txtAreaBilling.setBackground(UIManager.getColor("TextField.background"));//did not work to set background color to the same as a disabled jtextfield
				//txtAreaBilling.setBackground(contentPane.getBackground());
				lblPatientPic.removeMouseListener(mouseListener);
				lblConditionPic.removeMouseListener(mouseListener);

				refreshPictures();

				try {
					patient.setCondition(txtEditorCondition.getText());
				} catch (MalformedURLException excp) {
					JOptionPane.showMessageDialog(this, "Condition URL not well formed\n", "Error", JOptionPane.ERROR_MESSAGE);
					//this should never happen
				}
				txtEditorCondition.setText("<body><a href=\"" + patient.getConditionURL() + "\">" + patient.getCondition() + "</a></body>");
				txtEditorCondition.setEditable(false);

			}

		} else if(e.getSource() == btnSaveButton) {

			try {
				patient.setLastNameNoCheck(textLastName.getText());
				patient.setFirstNameNoCheck(textFirstName.getText());
				patient.setDOB(textDOB.getText());
				patient.setAddress(textAddress.getText());
				patient.setEmergencyPhone(textPhone.getText());
				patient.setCondition(txtEditorCondition.getText());
				patient.setAppointments(txtAreaAppointments.getText());
				patient.setBilling(txtAreaBilling.getText());
				patient.setComments(txtAreaComments.getText());


				if((textCURLOverride.getText()) != null && !(textCURLOverride.getText()).equals(""))	//override default url if user typed text into the override field
					patient.setConditionURL(textCURLOverride.getText());

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
			} catch (MalformedURLException excp) {
				JOptionPane.showMessageDialog(this, "Condition URL not well formed\n", "Error", JOptionPane.ERROR_MESSAGE);
			}
			//would have been better to show a single, cumulative error message
			/*if(errorMessage.length() > 1)
				JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);*/

		} else if(e.getSource() == comboBox) {
			try {
				drawPicture((String)comboBox.getSelectedItem(), lblConditionPic);
			} catch (Exception excp) {
				JOptionPane.showMessageDialog(this, (Object)("Unable to load selected condition image\n"
						+ "No condition pictures will be displayed"),
						"Error loading image", JOptionPane.WARNING_MESSAGE);
			}
			tabbedPane.setSelectedIndex(1);

		} 
	}

	//felt like placing this in a separate class would fragment the source code when this is only needed within the present
	//Jdialog extension
	//https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html

	
	/**
	 * Nested MouseListener class to popup a mousedrop menu when user right clicks
	 * a pticure pane when in editor mode.
	 * 
	 * @author nterreri
	 */
	private class EditorFrameMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.isPopupTrigger()) {
				popUp(e);
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger()) {
				popUp(e);
			}

		}

		/**
		 * Creates a popup menu with different text based on whether the user clicked
		 * on the profile pic pane or the condition pic pane.
		 * 
		 * @param e
		 */
		private void popUp(MouseEvent e){
			popupMenu.source = e.getComponent();

			if(popupMenu.source == lblPatientPic)
				popupMenu.itemAdd.setText("Replace Profile Pic");

			else
				popupMenu.itemAdd.setText("Add New Pic");

			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}

	}
	
	
	/**
	 * PicturePopUpMenu inner class.
	 *<p>
	 *Defines what happens on user right mouse clicking on the picture pane.
	 *<p>
	 *Constructs an EditorPicChooser dialog for user to select a picture from either file
	 *or specifying a URL.
	 *
	 * @author nterreri
	 *
	 */
	class PicturePopupMenu extends JPopupMenu implements ActionListener {

		
		private static final long serialVersionUID = -7050098197854826112L;
		Component source;
		JMenuItem itemAdd;
		JMenuItem itemRemove;

		PicturePopupMenu() {

			itemAdd = new JMenuItem("Add New Pic");
			itemAdd.addActionListener(this);
			add(itemAdd);


			itemRemove = new JMenuItem("Remove Current Pic");
			itemRemove.addActionListener(this);
			add(itemRemove);
		}

		/**
		 * Constructs an EditorPicChooser for user to select a file.
		 * Also handles picture removal through patient.removeXXXPicture()
		 * Finally refreshes the combobox model.
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == itemAdd) {
				EditorPicChooser dialog = new EditorPicChooser(PatientEditorFrame.this, source, patient); //should change to "getInstance()" or similar
				dialog.setVisible(true);


			} else if(e.getSource() == itemRemove) {
				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(PatientEditorFrame.this, "Remove picture?", "Confirm", JOptionPane.OK_CANCEL_OPTION)) {

					//add option to remove profile pic
					if(source == lblConditionPic) {
						patient.removeConditionPicture(comboBox.getSelectedIndex());
						//set blank label:
						lblConditionPic.setIcon(null);
					} else if (source == lblPatientPic) {
						patient.removePatientPicture();
						//set blank label:
						lblPatientPic.setIcon(null);
					}

					/*lblPatientPic = new JLabel();
					lblPatientPic.setHorizontalAlignment(SwingConstants.CENTER);
					panelPatientPic.add(lblPatientPic);*/


					//update combobox model:
					comboBox.setModel(fetchComboBoxModel());
				}
			}
		}
	}

	
	/**
	 * Attempts to resize the image from the provided url to properly fit into the picture pane.
	 * Must take a JPanel component when creating the frame.<p>
	 * 
	 * @param source
	 * @param component
	 * @throws Exception
	 * @see #drawPicture(String, JLabel)
	 * @see http://stackoverflow.com/questions/8333802/displaying-an-image-in-java-swing		
	 * @see http://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
	 */
	void drawPicture(String source, JPanel component) throws Exception {

		Image img = ImageIO.read(new URL(source));			//<-this may throw an IOException or MalformedURLException
		BufferedImage resizedImg = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, component.getWidth(), component.getHeight(), null);
		g2.dispose();

		//cast JPanel to JLabel:
		((JLabel)component.getComponent(0)).setIcon(new ImageIcon(resizedImg));
	}

	
	/**
	 * Attempts to resize the image from the provided url to properly fit into the picture pane.
	 * Uses JLabel directly after the frame has been created to avoid AWT threading issues (see drawPicture(String, JPanel).<p>
	 * 
	 * @param source
	 * @param component
	 * @throws Exception
	 * @see #drawPicture(String, JPanel)
	 * @see http://stackoverflow.com/questions/8333802/displaying-an-image-in-java-swing		
	 * @see http://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
	 */
	void drawPicture(String source, JLabel component) throws Exception {

		Image img = ImageIO.read(new URL(source));			//<-this may throw an IOException or MalformedURLException
		BufferedImage resizedImg = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, component.getWidth(), component.getHeight(), null);
		g2.dispose();

		component.setIcon(new ImageIcon(resizedImg));
	}
	
	
	/**
	 * Attempts to open user browser when clicking on hyperlink in JEditorPane.
	 * Note: link is only clickable when EditorPane editable flag is set to false (see JEditorPane javadoc),
	 * so the link is only clickable when editor mode is toggled off.
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

			if(Desktop.isDesktopSupported()) {

				try{ 
					Desktop.getDesktop().browse(new URL(patient.getConditionURL()).toURI());

				} catch (URISyntaxException excp) {
					JOptionPane.showMessageDialog(this, "Unable to open URL: invalid URL");

				} catch (IOException excp ) {
					JOptionPane.showMessageDialog(this, "Unable to open URL: connection error");

				}
			}
		}
	}

}
