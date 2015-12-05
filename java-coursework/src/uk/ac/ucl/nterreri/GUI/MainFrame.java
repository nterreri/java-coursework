package uk.ac.ucl.nterreri.GUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import uk.ac.ucl.nterreri.task3.*;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.DefaultComboBoxModel;

public class MainFrame extends JFrame implements ActionListener {

	private JTable table;
	private PatientTableModel model;
	private JScrollPane scrollPaneTable;
	private JButton btnRemove;
	private JButton btnEdit;
	private JButton btnAdd;
	private JTabbedPane tabbedPane;
	private JLabel lblPatientManagementSystem;
	private JTextField textSearchField;
	private JComboBox comboRecordsFields;
	private JButton btnSearch;
	private JTable searchTable;
	private JScrollPane scrollPaneSearch;
	private JPanel panelSearch;
	private PatientSearchTableModel searchModel;
	private JButton btnImport;
	private JButton btnExport;

	//private DefaultTableModel temp = new DefaultTableModel() 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
	}

	/**
	 * Create the application.
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
		//initialize the window:
		//frame = new JFrame();
		setBounds(100, 50, 1000, 650);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblPatientManagementSystem = new JLabel("    Patient Management System");
		lblPatientManagementSystem.setFont(new Font("Lucida Sans", Font.BOLD, 24));

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


		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		model = new PatientTableModel();
		table = new JTable();
		scrollPaneTable = new JScrollPane(table);
		scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tabbedPane.addTab("Table", null, scrollPaneTable, null);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setModel(model);
		/*table.setModel(new DefaultTableModel(){
					@Override
					public void removeRow(int row) {
						super.removeRow(row);
					}

					@Override
					public int getRowCount() {
						return Patient.getPatientRecords().size();
					}

					@Override
					public int getColumnCount() {
						return Patient.ELEMENTS - 3;
					}

					@Override
					public Object getValueAt(int rowIndex, int columnIndex) {
						return Patient.getPatientRecords().get(rowIndex)[columnIndex];
					}

					@Override
					public String getColumnName(int column) {
						switch(column)
						{
						case 0:
							return "ID";
						case 1:
							return "Last Name";
						case 2:
							return "First Name";
						case 3:
							return "DOB";
						case 4:
							return "Address";
						case 5:
							return "Emergency Phone";
						case 6:
							return "Condition";
						case 7:
							return "Appointments";
						case 8:
							return "Billing";
						case 9:
							return "Comments";
						//other cases contain urls and should not be displayed in general
							default:
								return super.getColumnName(column);
						}
					}

				});*/

		//forces selection to be on first table item by default,
		//so that if the user presses any of the side buttons the first table row element
		//will be the default selection
		//	table.setRowSelectionInterval(0, 0);

		panelSearch = new JPanel();

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


		comboRecordsFields = new JComboBox();
		comboRecordsFields.setModel(new DefaultComboBoxModel(new String[] 
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
										.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
										.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE))
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
										.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap())
				);
		getContentPane().setLayout(groupLayout);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnAdd) {
			try {
				Patient newPatient = new Patient();
				PatientEditorFrame editorFrame = new PatientEditorFrame(this, newPatient);
				editorFrame.setVisible(true);
				editorFrame.addWindowListener(new WindowAdapter() {

					/*@Override
					public void windowLostFocus(WindowEvent e) {
						//model = new PatientTableModel(); // refresh table data by rebuilding the model from 
						//Patient class static data
						//table.setModel(model);
						model.fireTableChanged(new TableModelEvent());
					}

					@Override
					public void windowDeactivated(WindowEvent e) {
						//model = new PatientTableModel(); // refresh table data by rebuilding the model from 
						//Patient class static data
						//table.setModel(model);
						model.fireTableChanged(new TableModelEvent());
					}*/

					@Override
					public void windowClosed(WindowEvent e){
						//model = new PatientTableModel(); // refresh table data by rebuilding the model from 
						//Patient class static data
						//table.setModel(model);
						model = new PatientTableModel();
						table.setModel(model);
					}
				});
			} catch(NumberFormatException excp) {
				JOptionPane.showMessageDialog(this, (Object)("Error: unable to determine next available patient ID:\n"
						+ "Either the patient records file is empty, or full, or the last recorded patient ID has been corrupted."),
						"Error adding new patient", JOptionPane.ERROR_MESSAGE);
			}

		} else if(e.getSource() == btnRemove) {
			try {
				int indexInArrayList = -1;
				if(tabbedPane.getSelectedIndex() == 0)
					indexInArrayList = table.getSelectedRow();
				else if(tabbedPane.getSelectedIndex() == 1)
					indexInArrayList = searchModel.originalIndexes.get(searchTable.getSelectedRow());

				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, "Confirm patient deletion?", 
						"Deletion", JOptionPane.OK_CANCEL_OPTION)) {
					Patient.deleteEntry(indexInArrayList);
					//model = new PatientTableModel();
					//table.setModel(model);
					//searchModel = new PatientSearchTableModel();
					//searchModel.search(comboRecordsFields.getSelectedIndex(), textSearchField.getText());
					//searchTable.setModel(searchModel);
					model.fireTableChanged(new TableModelEvent(model));
					searchModel.fireTableChanged(new TableModelEvent(searchModel));
					JOptionPane.showMessageDialog(this, "Patient successfully deleted");
				}

			} catch(IndexOutOfBoundsException excp) {

			} catch (IOException excp) {
				JOptionPane.showMessageDialog(this, (Object)("Error updating file record, changes not saved"),
						"Error updating file", JOptionPane.ERROR_MESSAGE);
			}


		} else if(e.getSource() == btnEdit) {
			//create new patient instance with int parameter (selected row)
			Patient existingPatient = new Patient(table.getSelectedRow());
			PatientEditorFrame editorFrame = new PatientEditorFrame(this, existingPatient, table.getSelectedRow());
			editorFrame.setVisible(true);

		} else if((e.getSource() == btnSearch) ||  (e.getSource() == textSearchField)) {
			//search through model, then update model:

			int field = comboRecordsFields.getSelectedIndex();
			String value = textSearchField.getText();

			searchModel.search(field, value);
			searchModel.fireTableChanged(new TableModelEvent(searchModel));

			//feedback:
			String feedback = "Search Complete:\n";
			if(searchModel.queryResult == null) {
				feedback += "No Search Match";
			} else {
				feedback += searchModel.queryResult.size() + " Found";
			}
			JOptionPane.showMessageDialog(this, feedback);

		} else if(e.getSource() == btnExport) {

			JFileChooser fc = new JFileChooser(/*add param for current directory?*/);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"CSV File", "csv");
			fc.setFileFilter(filter);

			try{
				if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
					Patient.exportCSV(new File(fc.getSelectedFile() + ".csv"));

			} catch (IOException excp) {
				JOptionPane.showMessageDialog(this, "Unable to export to selected location: unable to write file", "Error", JOptionPane.ERROR_MESSAGE);

			} catch(HeadlessException excp) {
				JOptionPane.showMessageDialog(this, "Unable to export to selected location: impossible to access IO device (i.e. keyboard and/or mouse)", "Fatal Error", JOptionPane.ERROR_MESSAGE);

			}

		} else if(e.getSource() == btnImport) {

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

	private void attemptToRestoreBackup() {
		try {
			Patient.restorePreviousFile(new File("patient_records"));
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
}
