package uk.ac.ucl.nterreri.task3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * Class describing a single patient entry. Features static fields and methods to also
 * hold an instance-invariant log of all patients records.
 * 
 * Should always be used by calling its initialize() method to read existing patient
 * records from file before any Patient instance is created: the Patient instance constructor
 * relies on the instance invariant log to decide the next patient id to assign to the new
 * patient.
 * 
 * Includes setters that check for format correctness where relevant (e.g. URLs must be
 * well-formed, dates should be in "dd/mm/yyyy" format).
 * 
 * Includes static methods to manage on disk records: writing to file, updating file,
 * exporting, backup and backup restore. These features make heavy use of #TODO opencsv.
 * 
 * Includes a static method to delete an entry in the records, and an instance method to
 * add the calling instance to the top of the patient records stack. If a conflict is 
 * detected between the instance patient id number and the next expected patient id, the 
 * value of the latter is assigned to the first. This should happen where multiple patient instances
 * are present, but they are not being added to the stack in the order they were 
 * initialized.
 * 
 * Finally, includes methods to search patient records and retrieve an ArrayList of string 
 * arrays corresponding to the records matched with the query value.
 * 
 * @author nterreri
 *
 */
public class Patient {


	//fields:
	private String patient_id = "1000";				//0 in patientRecords String array element
	private String l_name = "LASTNAME";				//1 in patientRecords String array element
	private String f_name = "FIRSTNAME";			//2 in patientRecords String array element
	private String DOB = "00/00/0000";				//3 in patientRecords String array element
	private String address = "ADDRESS";				//4 in patientRecords String array element
	private String emergencyPhone = "0000000000";	//5 in patientRecords String array element, 10 digits number
	private String condition = "CONDITION";			//6 in patientRecords String array element
	private String appointments = null;				//7 in patientRecords String array element
	private String billing = null;					//8 in patientRecords String array element
	private String comments = null;					//9 in patientRecords String array element
	private String patientPictureURL = null;		//10 in patientRecords String array element
	private String conditionPictureURL = null;		//11 in patientRecords String array element
	private String conditionURL = null;				//12 in patientRecords String array element
	private static ArrayList<String[]> patientRecords; // = new List<String[]>(12);
	private static File recordsFile;// = new File();
	public static final int ELEMENTS = 13;			//number of elements in the arraylist

	//instance constructors:
	/**
	 * Default constructor adds new patient instance by calling getNextId() to figure out
	 * the appropriate patientId for the current element.
	 * 
	 * @throws NumberFormatException
	 * @throws Exception
	 * @see getNextId()
	 */
	Patient() throws NumberFormatException, Exception {

		this.patient_id = Integer.toString(getNextId());
	}

	//public methods:
	/**
	 * Initializes pointers holding data for each patient on file, based on the default
	 * file name and path ("patient_records" in root folder of program )
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @see getRecordsFromFile()
	 */
	public static void initialize() throws FileNotFoundException, IOException {
		recordsFile = new File("patient_records");

		patientRecords = new ArrayList<String[]>(20);
		patientRecords = (ArrayList<String[]>) getRecordsFromFile(recordsFile);

		/*//these lines used to initialize each element of the array list, no longer needed
		for(int i = 0; i < patientRecords.size(); i++) {
			patientRecords.set(i, new String[12]); 
		}*/

	}

	/**
	 * Calls getRecordsFromFile() to reset the array list holding patients data to what
	 * is stored on disk.
	 * 
	 * @see getRecordsFromFile();
	 */
	public static void reloadRecordsFromFile() throws FileNotFoundException, IOException {
		patientRecords = (ArrayList<String[]>) getRecordsFromFile(recordsFile);

	}

	//getters:
	
	public static ArrayList<String[]> getPatientRecords() {
		return patientRecords;
	}
	
	//setters:
	/**
	 * Sets first and last name of patient, throws an exception if the initial character
	 * of each name is not capitalized.
	 * 
	 * @param l_name
	 * @param f_name
	 * @throws NameFormattingException
	 * @see setNameNoCheck()
	 */
	void setName(String l_name, String f_name) throws NameFormattingException {
		if(!Character.isUpperCase(l_name.charAt(0)) || 
				!Character.isUpperCase(f_name.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			throw (new NameFormattingException());

		this.f_name = f_name;
		this.l_name = l_name;
	}

	/**
	 * Sets first and last name of patient, does not throw an exception if the initial
	 * character of each name is not capitalized.
	 * 
	 * @param l_name
	 * @param f_name
	 * @see setName()
	 */
	void setNameNoCheck(String l_name, String f_name) {

		this.f_name = f_name;
		this.l_name = l_name;
	}

	/**
	 * Sets the last name of the patient, throws NameFormattingException where this does
	 * not start with a capital letter.
	 * 
	 * @param l_name
	 * @throws NameFormattingException
	 * @see setLastNameNoCheck()
	 */
	void setLastName(String l_name) throws NameFormattingException {
		if(!Character.isUpperCase(l_name.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			throw (new NameFormattingException());
		this.l_name = l_name;
	}

	/**
	 * Sets the last name of the patient, does not throw NameFormattingException if this 
	 * does not start with a capital letter.
	 * 
	 * @param l_name
	 * @throws NameFormattingException
	 * @see setLastName()
	 */
	void setLastNameNoCheck(String l_name) {
		this.l_name = l_name;
	}

	/**
	 * Sets the first name of the patient, throws NameFormattingException where this does
	 * not start with a capital letter.
	 * 
	 * @param f_name
	 * @throws NameFormattingException
	 * @see setFirstNameNoCheck()
	 */
	void setFirstName(String f_name) throws NameFormattingException {
		if(!Character.isUpperCase(f_name.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			throw (new NameFormattingException());
		this.f_name = f_name;
	}


	/**
	 * Sets the first name of the patient, does not throw NameFormattingException if 
	 * this does not start with a capital letter.
	 * 
	 * @param f_name
	 * @throws NameFormattingException
	 * @see setFirstName()
	 */
	void setFirstNameNoCheck(String f_name) {

		this.f_name = f_name;
	}

	/**
	 * Sets patient's date of birth, does not set DOB and throws exception if the 
	 * argument string does cannot be parsed as a date by a SimpleDateFormat object, 
	 * initialized with a call to DateFormat.getDateInstance().
	 * 
	 * @param DOB
	 * @throws ParseException
	 */
	void setDOB(String DOB) throws ParseException {
		SimpleDateFormat localDF = new SimpleDateFormat("dd/MM/yyyy");
		//localDF = (SimpleDateFormat) DateFormat.getDateInstance();

		//ParseException thrown here, to be caught inside main program
		localDF.parse(DOB);

		//if no exception is thrown:
		this.DOB = DOB;
	}

	/**
	 * Sets patient address, no checks performed.
	 * 
	 * @param address
	 */
	void setAddress(String address) {

		//address can be garbage
		this.address = address;
	}

	/**
	 * Sets patient condition, no checks performed.
	 * 
	 * @param condition
	 */
	void setCondition(String condition) {

		//condition can be garbage:
		this.condition = condition;
	}

	/**
	 * Sets patient appointments, no checks performed.
	 * 
	 * @param appointments
	 */
	void setAppointments(String appointments) {

		//appointments can be garbage
		this.appointments = appointments;
	}

	/**
	 * Sets patient billing, no checks performed.
	 * 
	 * @param billing
	 */
	void setBilling(String billing) {

		//billing address/info can be garbage:
		this.billing = billing;
	}

	/**
	 * Sets patient comments, no checks performed.
	 * 
	 * @param comments
	 */
	void setComments(String comments) {

		//comments can be garbage:
		this.comments = comments;
	}

	//URL setters:
	//TODO: credit http://stackoverflow.com/a/5803008 for url checking
	/**
	 * Sets patient picture URL unless URL is malformed. In which case, an
	 * exception is thrown.
	 * 
	 * @param patientPictureURL
	 * @throws MalformedURLException
	 */
	void setPatientPictureURL(String patientPictureURL) throws MalformedURLException{

		URL constructionAttempt = new URL(patientPictureURL);
		this.patientPictureURL = patientPictureURL;
	}

	/**
	 * Sets patient condition picture URL unless URL is malformed, in which case an 
	 * exception is thrown.
	 * 
	 * @param conditionPictureURL
	 * @throws MalformedURLException
	 */
	void setConditionPictureURL(String conditionPictureURL) throws MalformedURLException{

		URL constructionAttempt = new URL(conditionPictureURL);
		this.conditionPictureURL = conditionPictureURL;
	}

	/**
	 * Sets patient condition description URL unless URL is malformed, in which case
	 * an exception is thrown.
	 * 
	 * @param conditionURL
	 * @throws MalformedURLException
	 */
	void setConditionURL(String conditionURL) throws MalformedURLException{

		URL constructionAttempt = new URL(conditionURL);
		this.conditionURL = conditionURL;
	}

	/**
	 * Sets emergency phone number. Throws a number format exception if the string argument
	 * cannot be parsed as a long integer, or if it is not exactly 10 digits in length.
	 * 
	 * @param emergencyPhone
	 * @throws NumberFormatException
	 */
	void setEmergencyPhone(String emergencyPhone) throws NumberFormatException {
		if(emergencyPhone.length() != 10)
		{
			NumberFormatException e = new NumberFormatException("Phone number length "
					+ "must be exactly 10 digits.");
			throw e;
		}

		Long.parseLong(emergencyPhone);

		//If no exception has been thrown:
		this.emergencyPhone = emergencyPhone;
	}

	/**
	 * 
	 * @return the next integer after the patient id number of the last registered patient,
	 * assumes the patientRecords field is not empty. Throws number format exception if this
	 * occurs.
	 * 
	 * @throws NumberFormatException
	 * @see Patient()
	 */
	private int getNextId() throws NumberFormatException {
		//the last int is obtained by parsing the first element of the last String 
		//array in the static field "patientRecords":
		int next_id = Integer.parseInt(patientRecords.get(patientRecords.size() - 1)[0]) + 1;

		if(next_id > 9999 || next_id < 1000) {
			throw (new NumberFormatException("Unexpected patient id number as last patient" +
					" record."));
		}

		return next_id;
	}

	/**
	 * Adds the current patient instance to the array list patientRecords. Should only be
	 * used after these have been loaded into memory through initialize() or 
	 * reloadRecordsFromFile().
	 * 
	 * Where a conflict between the patient_id field of the patient instance and the 
	 * mathematical successor to the last patient id number at the bottom of the 
	 * patientRecords stack is detected, then the value of the latter wins.
	 * 
	 * @see initialize()
	 * @see reloadRecordsFromfile()
	 * @see checkIDConsistency()
	 */
	private void addRecord() throws NumberFormatException {
		String [] thisRecord = { patient_id, l_name, f_name, DOB, address, emergencyPhone, condition, 
				appointments, billing, comments ,patientPictureURL,	conditionPictureURL,
				conditionURL, };
		if(!checkIDConsistency())
			{
				this.patient_id = Integer.toString(getNextId());
				thisRecord[0] = this.patient_id;
			}
		patientRecords.add(thisRecord);
	}

	/**
	 * Called by addRecord() to check for patient id consistency.
	 * 
	 * @return false if the patient id of the calling object is not the next expected
	 * patient id, true otherwise.
	 */
	private boolean checkIDConsistency() {
		if(!(this.patient_id.equals(Integer.toString(getNextId()))))
			return false;
		else
			return true;
	}
	
	
	/**
	 * Reads patient data from default file into the patientRecords field.
	 * 
	 * #TODO credit opencsv 
	 * 
	 * @return a List<String[]> object initialized by a CSV reader (see http://opencsv.sourceforge.net/apidocs/)
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @see initialize();
	 */
	private static List<String[]> getRecordsFromFile(File recordsFile) throws FileNotFoundException, IOException  {
		CSVReader reader = new CSVReader(new FileReader(recordsFile));
		return reader.readAll();
	}

	/**
	 * Writes content of array list patientRecords to file specified by the recordsFile
	 * field.
	 * 
	 * @throws IOException
	 */
	private static void updateRecordsFile() throws IOException {
		backup(recordsFile);

		FileWriter fw = new FileWriter(recordsFile);
		CSVWriter csvw = new CSVWriter(fw);

		csvw.writeAll(patientRecords);
		csvw.flush();

		csvw.close();
		fw.close();
	}

	/**
	 * Backs up current records file by making a copy of it and appending ".back" to the end
	 * of its filename.
	 * 
	 * @param recordsFile
	 * @throws IOException
	 */
	private static void backup(File recordsFile) throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(recordsFile));
		FileWriter fw = new FileWriter(recordsFile.getPath() + ".back");

		CSVReader csvr = new CSVReader(fr);
		ArrayList<String[]> buffer = (ArrayList<String[]>) csvr.readAll();

		CSVWriter csvw = new CSVWriter(fw);
		csvw.writeAll(buffer);
		csvw.flush();

		csvr.close();
		csvw.close();
		fr.close();
		fw.close();
	}

	/**
	 * Attempts to restore data file by reading recordsFile + ".back" and writing to the file
	 * specified by the field recordsFile.
	 * 
	 * @param recordsFile
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void restorePreviousFile(File recordsFile) throws IOException, FileNotFoundException {

		BufferedReader fr = new BufferedReader(new FileReader(recordsFile.getPath() + ".back"));
		FileWriter fw = new FileWriter(recordsFile);

		CSVReader csvr = new CSVReader(fr);
		ArrayList<String[]> buffer = (ArrayList<String[]>) csvr.readAll();

		CSVWriter csvw = new CSVWriter(fw);
		csvw.writeAll(buffer);
		csvw.flush();

		csvr.close();
		csvw.close();
		fr.close();
		fw.close();
	}

	//private static void writeCSV(File targetFile) throws IOException, FileNotFoundException

	/**
	 * Removes specified patient record from the array list loaded in memory, the file
	 * should then be update through updateRecordsFile()
	 * 
	 * @param indexInArrayList
	 * @throws IndexOutOfBoundsException
	 * @see updateRecordsFile()
	 */
	private static void deleteEntry(int indexInArrayList) throws IndexOutOfBoundsException {
		patientRecords.remove(indexInArrayList);
	}

	/**
	 * Queries array list patientRecords for the specified string value within the specified
	 * field. Returns an empty array list where no match was found.
	 * 
	 * @param field
	 * @param value
	 * @return an array list of string arrays corresponding to the patient records
	 * that match the query.
	 */
	private static ArrayList<String[]> search(int field, String value) {

		ArrayList<String[]> result = new ArrayList<String[]>();

		for(int i = 0; i < patientRecords.size(); i++) {
			if(patientRecords.get(i)[field].equals(value))
				result.add(patientRecords.get(i));
		}

		return result;
	}

	//driver method, only used for testing
	@Deprecated
	public static void driver() {

		//initialize static data:
		try {
			Patient.initialize();
			//if failed attempt to recover by restory standard backup file
			//would be nice if user could set custom backup file path but 
			//probably won't happen in time available
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			try {

				Patient.restorePreviousFile(new File("patient_records"));
				System.out.println("Restored backup succesfully.");	
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(1);
			}
		} catch (IOException e) {

			e.printStackTrace();
			try {

				Patient.restorePreviousFile(new File("patient_records"));
				System.out.println("Restored backup succesfully.");	
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(1);
			}
		}

		System.out.println("Initialization succeeded without error on default file.");

		//attempt to modify the record list:
		Patient.deleteEntry(1);

		try {
			Patient.updateRecordsFile();
			Patient.reloadRecordsFromFile();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Modified records file successfully.");

		try{
			Patient.restorePreviousFile(new File("patient_records"));
			Patient.reloadRecordsFromFile();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Restored previous version.");
		System.out.println("\"Socrates\" occurs first at patient id:");
		System.out.println(Patient.search(2, "Socrates").get(0)[0]);

		//tested all static methods so far...
		//testing instance:
		//create instance:

		Patient p = null;
		try {
			p = new Patient();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Successfully instantiated Patient object with patient id:");
		System.out.println(p.patient_id);

		try {
			p.setName("Pinco", "palla");
		} catch(NameFormattingException e) {
			System.out.println("Warning: name set with lowercase initial, proceed anyway?"
					+ "(will proceed anyway in driver)");

			p.setNameNoCheck("Pinco", "palla");

		}
		p.setAddress("Baker Street");
		p.setAppointments("Yesterday");
		p.setBilling("none");
		p.setComments("is weird");
		p.setCondition("Hypochondriac");

		try {
			p.setConditionPictureURL("https://images.rapgenius.com/155a55a778c356240f936ed02ff46b8e.736x490x1.jpg");
			p.setConditionURL("http://www.nhs.uk/conditions/hypochondria/Pages/Introduction.aspx");
			p.setPatientPictureURL("http://media.makeadare.com/img/6160cf6aa/image_bceabb1299.jpg");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		try{ 
			p.setDOB("notadate");
		} catch (ParseException e) {
			try {
				p.setDOB("99/99/9999");
			} catch(ParseException ex) {
				ex.printStackTrace();
			}
		}
		try {
			p.setEmergencyPhone("notanumber");
		} catch(Exception e) {
			try { 
				p.setEmergencyPhone("9999999999");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		p.addRecord();

		System.out.println("New patient created:");
		for(int i = 0; i < Patient.patientRecords.get(2).length; i++) {
			System.out.print(Patient.patientRecords.get(2)[i] + ", ");
		}
		System.out.println();
	}


}