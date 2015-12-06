package uk.ac.ucl.nterreri.GUI;

import java.util.ArrayList;

import uk.ac.ucl.nterreri.patient.Patient;

/**
 * Extends PatientTableModel to inherit the methods responsible for correctly
 * setting up the table view.<p>
 * 
 * Overrides some of these where relevant to only display the user search query results.<p>
 * 
 * Provides facilities to store and retrieve the original indexes of the elements in the
 * result of the search query to allow a user to edit and remove patients in the search view.
 * 
 * @author nterreri
 *
 */
public class PatientSearchTableModel extends PatientTableModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2308944134482720547L;
	private ArrayList<String[]> queryResult;	//Retrieved from PatientClass
	/**
	 * Must also retrieve indexes in original data array (in patient class) because
	 * the search view displays a different data model (queryResult) with different indexes.
	 * This causes problems since MainFrame passes the rows selected in the JTables views to
	 * the Patient class patient edit and remove methods. And while the main JTable view 
	 * (the parent class to this class) displays the Patient.patientRecords ArrayList, the present
	 * model displays the queryResult ArrayList, therefore it is necessary to store and retrieve
	 * also the original indexes of the elements of the original data in order to allow a user
	 * to edit/delete patients in the search view.
	 */
	private ArrayList<Integer> originalIndexes;	

	/**
	 * MainFrame in this package must access this data.
	 * 
	 * @return queryResult
	 */
	public ArrayList<String[]> getQueryResult() {
		return queryResult;
	}
	
	/**
	 * MainFrame in this package must access this data.
	 * 
	 * @return originalIndexes
	 */
	public ArrayList<Integer> getOriginalIndexes() {
		return originalIndexes;
	}
	
	/**
	 * Search view data search method. Used in place of Patient.search().<p>
	 * 
	 * Makes a partial copy of Patient.patientRecords (based on user query), and stores each of the 
	 * original indexes of each element matching the query inside originalIndexes.<p>
	 * 
	 * Then stores the partial copy inside queryResult.
	 * 
	 * @param field the field in the patient records
	 * @param value the value to search for (uses String.contains(String))
	 */
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
	
	/**
	 * Displays as many rows as the size of queryResult when this has been initialized.
	 * Displays no rows otherwise.
	 */
	@Override
	public int getRowCount() {
		
		if(queryResult == null)
			return 0;
		else
			return queryResult.size();
	}
	
	/**
	 * Overrides parent method to get values from queryResult.
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return queryResult.get(rowIndex)[columnIndex];
	}
	

}
