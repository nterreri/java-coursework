package uk.ac.ucl.nterreri.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Vector;

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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import uk.ac.ucl.nterreri.task3.Patient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;

import javax.swing.JToggleButton;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;

public class PatientEditorFrame extends JDialog implements ActionListener, HyperlinkListener {

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
	JTabbedPane tabbedPane;		//<- has to be visible from JDialog generated from this frame
	private JPanel panelPatientPic;
	private JPanel panelConditionPic;
	JComboBox comboBox;			//<- has to be visible from JDialog generated from this frame
	PicturePopupMenu popupMenu;	//<- has to be visible from JDialog generated from this frame
	JLabel lblPatientPic;		//<- has to be visible from JDialog generated from this frame
	JLabel lblConditionPic;		//<- has to be visible from JDialog generated from this frame
	private EditorFrameMouseListener mouseListener;
	private JTextField textCURLOverride;
	private JLabel lblEnterConditionUrl;

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

	}

	public PatientEditorFrame(JFrame parent, Patient patient, int indexInArrayList) {
		super(parent);
		//setModalityType(Dialog.ModalityType.MODELESS); //In vain attempted to make parent jframe focus shift parent on top of children, see http://stackoverflow.com/questions/22259765/how-to-make-a-jdialog-not-always-on-top-of-parent
		this.indexInArrayList = indexInArrayList;
		this.patient = patient;

		initialize();

	}

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

		//credit TODO:http://stackoverflow.com/questions/3693543/hyperlink-in-jeditorpane
		txtEditorCondition = new JEditorPane("text/html", "<a href=\"" + patient.getConditionURL() + "\">" + patient.getCondition() + "</a>");
		txtEditorCondition.addHyperlinkListener(this);
		txtEditorCondition.setEditable(false);
		//txtEditorCondition.setBounds(19, 345, 214, 111);
		txtEditorCondition.setBorder(textBorder);
		//txtEditorCondition.setLineWrap(true);



		scrollPaneCondition = new JScrollPane(txtEditorCondition);
		scrollPaneCondition.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//contentPane.add(txtAreaCondition);

		txtAreaAppointments = new JTextArea();
		txtAreaAppointments.setEditable(false);
		//txtAreaAppointments.setBounds(251, 345, 249, 111);
		txtAreaAppointments.setBorder(textBorder);
		txtAreaAppointments.setLineWrap(true);
		scrollPaneAppointments = new JScrollPane(txtAreaAppointments);
		scrollPaneAppointments.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		txtAreaComments = new JTextArea();
		txtAreaComments.setEditable(false);
		//txtAreaComments.setBounds(526, 311, 287, 255);
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
		//tabbedPane.addTab("Patient Pic", null, lblPatientPic, null);

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
		//txtAreaBilling.setBounds(19, 490, 214, 88);
		txtAreaBilling.setBorder(textBorder);
		txtAreaBilling.setLineWrap(true);
		scrollPaneBilling= new JScrollPane(txtAreaBilling);
		scrollPaneBilling.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//contentPane.add(scrollPaneComments);

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

	private void refreshPictures() {
		try {
			if(!( patient.getPatientPicture() == null || patient.getPatientPicture().length() == 0 ))
				drawPicture(patient.getPatientPicture(), panelPatientPic);	//draws patient profile picture image and scales to tabbedPane size
				//drawPicture(patient.getPatientPicture(), lblPatientPic);
			if(patient.getConditionPictures() != null) 
				drawPicture(patient.getConditionPictures()[0], panelConditionPic);	//draws patient profile picture image and scales to tabbedPane size
				//drawPicture(patient.getConditionPictures()[0], lblConditionPic);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, (Object)("Unable to load one or more images\n"
					+ "Some pictures will not be displayed"),
					"Error loading image", JOptionPane.WARNING_MESSAGE);
		}		
	}


	DefaultComboBoxModel fetchComboBoxModel() {
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		//comboBoxModel.addElement("- Select Condition Pic -");	//removed coaching to extract an index for the condition pic

		String[] restOfModel = patient.getConditionPictures();

		if(restOfModel != null)	//restOfModel will be null where there are no condition pics
			for(int i = 0; i < restOfModel.length; i++)
				comboBoxModel.addElement(restOfModel[i]);

		return comboBoxModel;
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
				//txtEditorCondition.setEditable(false);
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
			//String errorMessage = "";

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

		} else if((e.getSource() == comboBox)/* && (comboBox.getSelectedIndex() != 0)*/) {
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
	private class EditorFrameMouseListener extends MouseAdapter {

		/*@Override
		public void mouseClicked(MouseEvent e) {
			//Object [] options = {"From file", "From URL", "Cancel" };

			//JOptionPane.showOptionDialog(PatientEditorFrame.this, "How would you like to change the picture:", "Picture Source Selection", 
			//		JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			//TODO credit: http://stackoverflow.com/questions/1816458/getting-hold-of-the-outer-class-object-from-the-inner-class-object
			/*EditorPicChooser dialog = new EditorPicChooser(PatientEditorFrame.this, e.getComponent(), patient);
			dialog.setVisible(true);
			
		}*/

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

		private void popUp(MouseEvent e){
			popupMenu.source = e.getComponent();

			if(popupMenu.source == lblPatientPic)
				popupMenu.itemAdd.setText("Replace Profile Pic");

			else
				popupMenu.itemAdd.setText("Add New Pic");

			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}

		/*public PatientEditorFrame getOuter() {
			return PatientEditorFrame.this;
		}*/

	}

	class PicturePopupMenu extends JPopupMenu implements ActionListener {

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

	@Deprecated
	private void drawPictures() {

		//completely broken
		try {
			//drawPicture(patient.getPatientPicture(), panelPatientPic);//broken
		} catch (Exception e) {		
			//e.printStackTrace();//debug
			JOptionPane.showMessageDialog(this, (Object)("Unable to load profile image\n"
					+ "No patient picture will be displayed"),
					"Error loading image", JOptionPane.WARNING_MESSAGE);
		}

		try {
			//drawPicture(patient.getConditionPictures()[0], panelConditionPic);//broken
		} catch (Exception e) {		
			//e.printStackTrace();//debug
			JOptionPane.showMessageDialog(this, (Object)("Unable to load condition image\n"
					+ "Some condition pictures will not be displayed"),
					"Error loading image", JOptionPane.WARNING_MESSAGE);
		}


		/*String [] conditionPictures = patient.getConditionPictures();
		boolean allPicsLoaded = true;
		for(int i = 0; i < conditionPictures.length; i++ ) {

			try {
				drawPicture(conditionPictures[i], panelConditionPic);
			} catch (Exception e) {					
				e.printStackTrace();
				allPicsLoaded = false;
			}
		}

		if(!allPicsLoaded) {
			JOptionPane.showMessageDialog(this, (Object)("Unable to load some condition images\n"
					+ "Some condition pictures will not be displayed"),
					"Error loading image", JOptionPane.WARNING_MESSAGE);
		}*/

	}

	//earlier version
	/*void drawPicture(String source, JComponent component) throws Exception {
		//TODO: credit http://stackoverflow.com/questions/8333802/displaying-an-image-in-java-swing
		//TODO: and credit http://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon

		Image img = ImageIO.read(new URL(source));			//<-this may throw an IOException or MalformedURLException
		BufferedImage resizedImg = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, component.getWidth(), component.getHeight(), null);
		g2.dispose();

		((JLabel)component.getComponent(0)).setIcon(new ImageIcon(resizedImg));


	}*/
	

	void drawPicture(String source, JPanel component) throws Exception {
		//TODO: credit http://stackoverflow.com/questions/8333802/displaying-an-image-in-java-swing
		//TODO: and credit http://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon

		Image img = ImageIO.read(new URL(source));			//<-this may throw an IOException or MalformedURLException
		BufferedImage resizedImg = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, component.getWidth(), component.getHeight(), null);
		g2.dispose();

		((JLabel)component.getComponent(0)).setIcon(new ImageIcon(resizedImg));


	}
	
	void drawPicture(String source, JLabel component) throws Exception {
		//TODO: credit http://stackoverflow.com/questions/8333802/displaying-an-image-in-java-swing
		//TODO: and credit http://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon

		Image img = ImageIO.read(new URL(source));			//<-this may throw an IOException or MalformedURLException
		BufferedImage resizedImg = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, component.getWidth(), component.getHeight(), null);
		g2.dispose();

		component.setIcon(new ImageIcon(resizedImg));


	}


	//TODO: http://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
	//TODO: http://stackoverflow.com/questions/3693543/hyperlink-in-jeditorpane
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
