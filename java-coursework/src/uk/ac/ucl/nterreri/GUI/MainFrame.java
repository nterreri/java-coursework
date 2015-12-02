package uk.ac.ucl.nterreri.GUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import uk.ac.ucl.nterreri.task3.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.SwingConstants;
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

public class MainFrame extends JFrame implements ActionListener {

	private JTable table;
	private PatientTableModel model;
	private JScrollPane scrollPaneTable;
	private JScrollPane scrollPaneList;
	private JButton btnRemove;
	private JButton btnEdit;
	private JButton btnAdd;
	private JTabbedPane tabbedPane;
	private JLabel lblPatientManagementSystem;
	
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
			//if failed attempt to recover by restory standard backup file
			//would be nice if user could set custom backup file path but 
			//probably won't happen in time available
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			try {

				Patient.restorePreviousFile(new File("patient_records"));
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(1);
			}
		} catch (IOException e) {

			e.printStackTrace();
			try {

				Patient.restorePreviousFile(new File("patient_records"));
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(1);
			}
		}
		//initialize the window:
		//frame = new JFrame();
				setBounds(100, 50, 1000, 650);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				lblPatientManagementSystem = new JLabel("Patient Management System");
				lblPatientManagementSystem.setHorizontalAlignment(SwingConstants.CENTER);
				lblPatientManagementSystem.setFont(new Font("Lucida Sans", Font.BOLD, 24));

				btnAdd = new JButton("Add");
				btnAdd.addActionListener(this);
				btnEdit = new JButton("View");
				btnEdit.addActionListener(this);

				btnRemove = new JButton("Remove");
				btnRemove.addActionListener(this);

				tabbedPane = new JTabbedPane(JTabbedPane.TOP);
				GroupLayout groupLayout = new GroupLayout(getContentPane());
				groupLayout.setHorizontalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblPatientManagementSystem, GroupLayout.PREFERRED_SIZE, 397, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup()
												.addContainerGap()
												.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
														.addComponent(btnRemove, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(btnEdit, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(btnAdd, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)))
								.addContainerGap())
						);
				groupLayout.setVerticalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(lblPatientManagementSystem, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
												.addGap(18)
												.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
										.addGroup(groupLayout.createSequentialGroup()
												.addGap(50)
												.addComponent(btnAdd)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnEdit)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnRemove)))
								.addContainerGap())
						);

				model = new PatientTableModel();
				table = new JTable();
				scrollPaneTable = new JScrollPane(table);
				scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				tabbedPane.addTab("Table", null, scrollPaneTable, null);
				getContentPane().setLayout(groupLayout);
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
				table.setRowSelectionInterval(0, 0);
				
				JList list = new JList();
				scrollPaneList = new JScrollPane(list);
				tabbedPane.addTab("List", null, scrollPaneList, null);




			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnAdd) {
			try {
				Patient newPatient = new Patient();
			} catch(NumberFormatException excp) {
				JOptionPane.showMessageDialog(this, (Object)("Error: unable to determine next available patient ID:\n"
						+ "Either the patient records file is empty, or full, or the last recorded patient ID has been corrupted."),
						"Error adding new patient", JOptionPane.ERROR_MESSAGE);
			}
			
		} else if(e.getSource() == btnRemove) {
			JOptionPane.showConfirmDialog(this, "Confirm patient deletion?");
			Patient.deleteEntry(table.getSelectedRow());
			model.removeRow(table.getSelectedRow());
			JOptionPane.showMessageDialog(this, "Patient successfully deleted");
		} else if(e.getSource() == btnEdit) {
			//create new patient instance with int parameter (selected row)
			Patient existingPatient = new Patient(table.getSelectedRow());
			PatientEditorFrame editorFrame = new PatientEditorFrame(existingPatient, table.getSelectedRow());
			editorFrame.setVisible(true);
		}
		
	}

}
