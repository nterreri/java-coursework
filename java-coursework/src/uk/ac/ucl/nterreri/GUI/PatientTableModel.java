package uk.ac.ucl.nterreri.GUI;

import javax.swing.table.DefaultTableModel;

import uk.ac.ucl.nterreri.patient.Patient;

/**
 * JTable model for the main data view in the patient management system.<p>
 * 
 * Overrides DefaultTableModel methods where necessary in order to display data
 * properly.<p>
 * 
 * @author nterreri
 *
 */
public class PatientTableModel extends DefaultTableModel {


	/**
	 * 
	 */
	
	private static final long serialVersionUID = -5686547607142976924L;

	/**
	 * Makes all cells not editable.
	 * 
	 * @see http://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	/**
	 * Retrieves row data from Patient.patientRecords.
	 */
	@Override
	public int getRowCount() {
		return Patient.getPatientRecords().size();
	}

	/**
	 * Retrieves column data from Patient.ELEMENTS.
	 */
	@Override
	public int getColumnCount() {
		return Patient.ELEMENTS - 3;
	}

	/**
	 * Retrieves cell values from Patient.patientRecords getter method.
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return Patient.getPatientRecords().get(rowIndex)[columnIndex];
	}
	
	/**
	 * Sets column name data according to switch statement, tailored to
	 * current Patient class version patient fields. Will require manual update.
	 */
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
}
