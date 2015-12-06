package uk.ac.ucl.nterreri.GUI;


import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.ac.ucl.nterreri.patient.Patient;

/**
 * Main GUI frame for the patient management system.<p>
 * 
 * Manages file recovery at startup.<p>
 * 
 * Implements ActionListener to be the listener to its own
 * components.<p>
 * 
 * @author nterreri
 *
 */
public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7094959832923954494L;
	private JTable table;
	private PatientTableModel model;
	private JScrollPane scrollPaneTable;
	private JButton btnRemove;
	private JButton btnEdit;
	private JButton btnAdd;
	private JTabbedPane tabbedPane;
	private JLabel lblPatientManagementSystem;
	private JTextField textSearchField;
	private JComboBox<String> comboRecordsFields;
	private JButton btnSearch;
	private JTable searchTable;
	private JScrollPane scrollPaneSearch;
	private JPanel panelSearch;
	private PatientSearchTableModel searchModel;
	private JButton btnImport;
	private JButton btnExport;

	/**
	 * Launch the application.
	 * Debug only
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * The constructor attempts to initialize patient class static data.
	 * If this fails, it attempts to restore a backup.
	 * If this fails, it attempts to create a blank patient records file.
	 * If this fails, the application closes.<p>
	 * 
	 * Registers itself as the action listener to the various components.<p>
	 * 
	 * Finally, creates the frame.<p>
	 */
	public MainFrame() {

		//Attempt to initialize patient class static data
		try {
			Patient.initialize();
			//if failed attempt to recover by restoring standard backup file
			//would be nice if user could set custom backup file path but 
			//probably won't happen in time available
		} catch (FileNotFoundException  e ) {

			JOptionPane.showMessageDialog(null, "Records file not found, attempting to restore backup");
			attemptToRestoreBackup();
		} catch ( IOException e ) {

			JOptionPane.showMessageDialog(null, "Error reading records file, attempting to restore backup");
			attemptToRestoreBackup();
		}
		
		//register itself as action listener:
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(this);
		btnEdit = new JButton("View");
		btnEdit.addActionListener(this);
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(this);
		btnExport = new JButton("Export");
		btnExport.addActionListener(this);
		btnImport = new JButton("Import");
		btnImport.addActionListener(this);
		
		createFrame();
	}
	
	/**
	 * Create the frame, called by constructor. Mostly swing-designer generated.
	 */
	private void createFrame() {
		//initialize the window:
				//frame = new JFrame();
				setBounds(100, 50, 1000, 650);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				lblPatientManagementSystem = new JLabel("    Patient Management System");
				lblPatientManagementSystem.setFont(new Font("Lucida Sans", Font.BOLD, 24));


				tabbedPane = new JTabbedPane(JTabbedPane.TOP);

				//set view model
				model = new PatientTableModel();
				table = new JTable();
				scrollPaneTable = new JScrollPane(table);
				scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				tabbedPane.addTab("Table", null, scrollPaneTable, null);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setModel(model);
				
				panelSearch = new JPanel();

				//set search view model
				searchModel = new PatientSearchTableModel();
				searchTable = new JTable();
				scrollPaneSearch = new JScrollPane(searchTable);
				scrollPaneSearch.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPaneSearch.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				tabbedPane.addTab("Search", null, panelSearch, null);
				searchTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				searchTable.setModel(searchModel);

				JLabel lblSearchQuery = new JLabel("Search query:");

				textSearchField = new JTextField();
				textSearchField.addActionListener(this);
				textSearchField.setColumns(10);


				comboRecordsFields = new JComboBox<String>();
				comboRecordsFields.setModel(new DefaultComboBoxModel<String>(new String[] 
						{"ID", "Last Name", "First Name", "DOB", "Address", "Emergency Phone", 
								"Condition", "Appointments", "Billing", "Comments"}));

				btnSearch = new JButton("Search");
				btnSearch.addActionListener(this);
				

				JSeparator separator = new JSeparator();

				scrollPaneSearch.setBackground(Color.WHITE);
				GroupLayout gl_panelSearch = new GroupLayout(panelSearch);
				gl_panelSearch.setHorizontalGroup(
						gl_panelSearch.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneSearch, GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panelSearch.createSequentialGroup()
								.addContainerGap()
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
								.addContainerGap())
						.addGroup(gl_panelSearch.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblSearchQuery, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(comboRecordsFields, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
								.addGap(12)
								.addComponent(textSearchField)
								.addGap(18)
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
						);
				gl_panelSearch.setVerticalGroup(
						gl_panelSearch.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelSearch.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panelSearch.createParallelGroup(Alignment.BASELINE)
										.addComponent(textSearchField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnSearch)
										.addComponent(comboRecordsFields, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblSearchQuery))
								.addGap(7)
								.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPaneSearch, GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE))
						);
				panelSearch.setLayout(gl_panelSearch);
				scrollPaneTable.setBackground(Color.WHITE);

				JSeparator separator_1 = new JSeparator();

				GroupLayout groupLayout = new GroupLayout(getContentPane());
				groupLayout.setHorizontalGroup(
					groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnImport, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnExport, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnAdd, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
										.addComponent(btnEdit, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
										.addComponent(btnRemove, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
										.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
									.addGap(11)
									.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE))
								.addComponent(lblPatientManagementSystem, GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE))
							.addContainerGap())
				);
				groupLayout.setVerticalGroup(
					groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPatientManagementSystem, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(44)
									.addComponent(btnAdd)
									.addGap(6)
									.addComponent(btnEdit)
									.addGap(6)
									.addComponent(btnRemove)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnExport)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnImport))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(6)
									.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)))
							.addContainerGap())
				);
				getContentPane().setLayout(groupLayout);
	}

	/**
	 * Attempts restoring data from backup file through Patient.restorePreviousFile().
	 * Calls attemptGenerateEmptyFile() if this fails.
	 * 
	 * @see #attemptGenerateEmptyFile()
	 */
	private void attemptToRestoreBackup() {
		try {
			Patient.restorePreviousFile(new File(System.getProperty("user.dir") + "/patient_records"));
			Patient.initialize();
			JOptionPane.showMessageDialog(null, "Backup restored");

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Patient backup records file not found, generating empty file");
			attemptGenerateEmptyFile();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error reading patient records file, generating empty file");
			attemptGenerateEmptyFile();
		} 
	}

	/**
	 * Attempts to generate empty data file if backup restore was unsucessful
	 * through Patient.generateEmptyRecords().<p>
	 * 
	 * Intended to be used to recover from errors.
	 */
	private void attemptGenerateEmptyFile() {
		try {
			Patient.generateEmptyRecords();
			Patient.initialize();
			JOptionPane.showMessageDialog(null, "Empty file generated, patient records are now empty");
			//throw new IOException();
		} catch (IOException e) {

			JOptionPane.showMessageDialog(null, "Unable to create new file, perhaps permissions insufficient", 
					"Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unable to create new file", 
					"Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	/**
	 * Action performed override. Split up into diferent "Action" methods for legibility.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnAdd) {
			addAction();

		} else if(e.getSource() == btnRemove) {
			removeAction();

		} else if(e.getSource() == btnEdit) {
			editAction();

		} else if((e.getSource() == btnSearch) ||  (e.getSource() == textSearchField)) {
			searchAction();
			
		} else if(e.getSource() == btnExport) {
			exportAction();
			
		} else if(e.getSource() == btnImport) {
			importAction();
			
		}
	}

	/**
	 * Defines what happens when user gives add patient command.<p>
	 * 
	 * Creates new empty patient instance with new unique id.
	 * Creates a PatientEditorFrame with this data.<p>
	 * 
	 * Adds a window listener to this child frame to wait until it closes
	 * before refreshing data view in JTable.<p>
	 */
	private void addAction() {
		try {
			Patient newPatient = new Patient();
			PatientEditorFrame editorFrame = new PatientEditorFrame(this, newPatient);
			editorFrame.setVisible(true);
			editorFrame.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e){
					//refresh view
					model = new PatientTableModel();
					table.setModel(model);
				}
			});
		} catch(NumberFormatException excp) {
			JOptionPane.showMessageDialog(this, (Object)("Error: unable to determine next available patient ID:\n"
					+ "Either the patient records file is empty, or full, or the last recorded patient ID has been corrupted."),
					"Error adding new patient", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Defines what happens when user gives remove command.<p>
	 * 
	 * Depending on which view is displayed, and which item on this view
	 * is selected, deletes the selected item, then refreshes both main and
	 * search view.<p>
	 */
	private void removeAction() {
		try {
			int indexInArrayList = -1;
			if(tabbedPane.getSelectedIndex() == 0)
				indexInArrayList = table.getSelectedRow();
			else if(tabbedPane.getSelectedIndex() == 1)
				indexInArrayList = searchModel.getOriginalIndexes().get(searchTable.getSelectedRow());

			if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, "Confirm patient deletion?", 
					"Deletion", JOptionPane.OK_CANCEL_OPTION)) {
				Patient.deleteEntry(indexInArrayList);
				//update tables:
				//model = new PatientTableModel();
				//table.setModel(model);
				//searchModel = new PatientSearchTableModel();
				//searchTable.setModel(searchModel);
				model.fireTableChanged(new TableModelEvent(model));
				searchModel.search(comboRecordsFields.getSelectedIndex(), textSearchField.getText());
				searchModel.fireTableChanged(new TableModelEvent(searchModel));
				JOptionPane.showMessageDialog(this, "Patient successfully deleted");
			}

		} catch(IndexOutOfBoundsException excp) {
			//do nothing if no row selected
		} catch (IOException excp) {
			JOptionPane.showMessageDialog(this, (Object)("Error updating file record, changes not saved"),
					"Error updating file", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	/**
	 * Defines what happens when user gives edit command.<p>
	 * 
	 * Creates patient instance with relevant data from selected table
	 * row. And a corresponding PatientEditorFrame
	 */
	private void editAction() {
		//create new patient instance with int parameter (selected row)
		
		try{
			Patient existingPatient = new Patient(table.getSelectedRow());
			PatientEditorFrame editorFrame = new PatientEditorFrame(this, existingPatient, table.getSelectedRow());
			editorFrame.setVisible(true);
		} catch(IndexOutOfBoundsException excp) {
			//do nothing if no row is selected
		}
		
	}
	
	/**
	 * Defines what happens when user gives search command.<p>
	 * 
	 * Searches through the data for matches with text and field
	 * specified.<p>
	 * 
	 * Updates search view. And feedback to user.<p>
	 */
	private void searchAction() {
		//search through model, then update model:

		int field = comboRecordsFields.getSelectedIndex();
		String value = textSearchField.getText();

		searchModel.search(field, value);
		searchModel.fireTableChanged(new TableModelEvent(searchModel));

		//feedback:
		String feedback = "Search Complete:\n";
		if(searchModel.getQueryResult() == null) {
			feedback += "No Search Match";
		} else {
			feedback += searchModel.getQueryResult().size() + " Found";
		}
		JOptionPane.showMessageDialog(this, feedback);

	}
	
	/**
	 * Defines what happens when user gives export command.<p>
	 * 
	 * Creates appropriate filechooser and calls Patient.exportCSV().<p>
	 * 
	 * Feedback errors to user if necessary.<p>
	 * 
	 */
	private void exportAction() {
		JFileChooser fc = new JFileChooser(/*add param for current directory?*/);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CSV File", "csv");
		fc.setFileFilter(filter);

		try{
			if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				int [] indexesInArrayList = null;
				if(tabbedPane.getSelectedIndex() == 0)
					indexesInArrayList = table.getSelectedRows();
				else if(tabbedPane.getSelectedIndex() == 1)
					//Not enough time to implement this:
					//indexesInArrayList = searchModel.getOriginalIndexes().get(searchTable.getSelectedRow());
					JOptionPane.showMessageDialog(this, "Export function is only available from the main table, not from the search table.");
				if(indexesInArrayList != null)
					Patient.exportCSV(new File(fc.getSelectedFile() + ".csv"), indexesInArrayList);
			}
				

		} catch (IOException excp) {
			JOptionPane.showMessageDialog(this, "Unable to export to selected location: unable to write file", "Error", JOptionPane.ERROR_MESSAGE);

		} catch(HeadlessException excp) {
			JOptionPane.showMessageDialog(this, "Unable to export to selected location: impossible to access IO device (i.e. keyboard and/or mouse)", "Fatal Error", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	/**
	 * Defines what happens when user gives import command.<p>
	 * 
	 * Creates appropriate fielchooser and appends content of 
	 * file chosen to end of current patient records file calling Patient.importCSV().<p>
	 * 
	 * Feedback erros to user if needed.<p>
	 */
	private void importAction() {
		JFileChooser fc = new JFileChooser(/*add param for current directory?*/);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CSV File", "csv");
		fc.setFileFilter(filter);

		try{
			if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
				Patient.importCSV(fc.getSelectedFile());
			
			model.fireTableChanged(new TableModelEvent(model));
			searchModel.fireTableChanged(new TableModelEvent(searchModel));

		} catch (FileNotFoundException excp) {
			JOptionPane.showMessageDialog(this, "Unable to import file content: file not found", "Error", JOptionPane.ERROR_MESSAGE);

		} catch (IOException excp) {
			JOptionPane.showMessageDialog(this, "Unable to import file content: unable to read file", "Error", JOptionPane.ERROR_MESSAGE);

		} catch(HeadlessException excp) {
			JOptionPane.showMessageDialog(this, "Unable to import file content: impossible to access IO device (i.e. keyboard and/or mouse)", "Fatal Error", JOptionPane.ERROR_MESSAGE);

		}
	}
	
}
