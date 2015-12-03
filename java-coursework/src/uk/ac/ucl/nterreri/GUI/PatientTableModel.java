package uk.ac.ucl.nterreri.GUI;

import javax.swing.table.DefaultTableModel;

import uk.ac.ucl.nterreri.task3.Patient;

public class PatientTableModel extends DefaultTableModel {

	
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
}
