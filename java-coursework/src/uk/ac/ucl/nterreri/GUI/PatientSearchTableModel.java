package uk.ac.ucl.nterreri.GUI;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import uk.ac.ucl.nterreri.task3.Patient;

public class PatientSearchTableModel extends PatientTableModel {

	ArrayList<String[]> queryResult;
	ArrayList<Integer> originalIndexes;
	
	void search(int field, String value) {
		
		ArrayList<String[]> copy = new ArrayList<String[]>();
		originalIndexes = new ArrayList<Integer>();
		
		for(int i = 0; i < Patient.getPatientRecords().size(); i++) {
			if(Patient.getPatientRecords().get(i)[field].contains(value)) {
				copy.add(Patient.getPatientRecords().get(i));
				originalIndexes.add(i);
			}
		}

		queryResult = copy;
	}
	
	@Override
	public int getRowCount() {
		
		if(queryResult == null)
			return 0;
		else
			return queryResult.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return queryResult.get(rowIndex)[columnIndex];
	}
	

}
