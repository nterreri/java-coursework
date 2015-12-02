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
	private String patientId = "1000";				//0 in patientRecords String array element
	private String lName = "LASTNAME";				//1 in patientRecords String array element
	private String fName = "FIRSTNAME";			//2 in patientRecords String array element
	private String DOB = "00/00/0000";				//3 in patientRecords String array element
	private String address = "ADDRESS";				//4 in patientRecords String array element
	private String emergencyPhone = "0000000000";	//5 in patientRecords String array element, 10 digits number
	private String condition = "CONDITION";			//6 in patientRecords String array element
	private String appointments = null;				//7 in patientRecords String array element
	private String billing = null;					//8 in patientRecords String array element
	private String comments = null;					//9 in patientRecords String array element
	private String patientPictureURL = null;		//10 in patientRecords String array element
	private String conditionPictures = null;		//11 in patientRecords String array element
	private String conditionURL = null;				//12 in patientRecords String array element
	private static ArrayList<String[]> patientRecords; // = new List<String[]>(13);
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
	public Patient() throws NumberFormatException {
		conditionPictures = new String();	//this must be reinitialized at instance creation because its default value should be
		//null, but the modifier methods of this private field append content to the string, and a null value will remain at the start
		//of the string if this is not reinitialized.
		
		this.patientId = Integer.toString(getNextId());	
		
	}
	
	//should return selected patient
	public Patient(int indexInArrayList) {
		//"manually" set every field:
		this.patientId = patientRecords.get(indexInArrayList)[0];				
		this.lName = patientRecords.get(indexInArrayList)[1];
		this.fName = patientRecords.get(indexInArrayList)[2];
		this.DOB = patientRecords.get(indexInArrayList)[3];
		this.address = patientRecords.get(indexInArrayList)[4];
		this.emergencyPhone = patientRecords.get(indexInArrayList)[5];
		this.condition = patientRecords.get(indexInArrayList)[6];
		this.appointments = patientRecords.get(indexInArrayList)[7];
		this.billing = patientRecords.get(indexInArrayList)[8];
		this.comments = patientRecords.get(indexInArrayList)[9];
		this.patientPictureURL = patientRecords.get(indexInArrayList)[10];
		this.conditionPictures = patientRecords.get(indexInArrayList)[11];
		this.conditionURL = patientRecords.get(indexInArrayList)[12];
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
	
	public String getPatientId() {
		return patientId;
	}

	public String getLName() {
		return lName;
	}

	public String getFName() {
		return fName;
	}

	public String getConditionPictures() {
		return conditionPictures;
	}

	public String getDOB() {
		return DOB;
	}

	public String getAddress() {
		return address;
	}

	public String getEmergencyPhone() {
		return emergencyPhone;
	}

	public String getCondition() {
		return condition;
	}

	public String getAppointments() {
		return appointments;
	}

	public String getBilling() {
		return billing;
	}

	public String getComments() {
		return comments;
	}

	public String getPatientPictureURL() {
		return patientPictureURL;
	}

	public String getConditionURL() {
		return conditionURL;
	}

	
	//setters:
	/**
	 * Sets first and last name of patient, throws an exception if the initial character
	 * of each name is not capitalized.
	 * 
	 * @param lName
	 * @param fName
	 * @throws NameFormattingException
	 * @see setNameNoCheck()
	 */
	void setName(String lName, String fName) throws NameFormattingException {
		if(!Character.isUpperCase(lName.charAt(0)) || 
				!Character.isUpperCase(fName.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			throw (new NameFormattingException());

		this.fName = fName;
		this.lName = lName;
	}

	/**
	 * Sets first and last name of patient, does not throw an exception if the initial
	 * character of each name is not capitalized.
	 * 
	 * @param lName
	 * @param fName
	 * @see setName()
	 */
	void setNameNoCheck(String lName, String fName) {

		this.fName = fName;
		this.lName = lName;
	}

	/**
	 * Sets the last name of the patient, throws NameFormattingException where this does
	 * not start with a capital letter.
	 * 
	 * @param lName
	 * @throws NameFormattingException
	 * @see setLastNameNoCheck()
	 */
	void setLastName(String lName) throws NameFormattingException {
		if(!Character.isUpperCase(lName.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			throw (new NameFormattingException());
		this.lName = lName;
	}

	/**
	 * Sets the last name of the patient, does not throw NameFormattingException if this 
	 * does not start with a capital letter.
	 * 
	 * @param lName
	 * @throws NameFormattingException
	 * @see setLastName()
	 */
	void setLastNameNoCheck(String lName) {
		this.lName = lName;
	}

	/**
	 * Sets the first name of the patient, throws NameFormattingException where this does
	 * not start with a capital letter.
	 * 
	 * @param fName
	 * @throws NameFormattingException
	 * @see setFirstNameNoCheck()
	 */
	void setFirstName(String fName) throws NameFormattingException {
		if(!Character.isUpperCase(fName.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			throw (new NameFormattingException());
		this.fName = fName;
	}


	/**
	 * Sets the first name of the patient, does not throw NameFormattingException if 
	 * this does not start with a capital letter.
	 * 
	 * @param fName
	 * @throws NameFormattingException
	 * @see setFirstName()
	 */
	void setFirstNameNoCheck(String fName) {

		this.fName = fName;
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
	/*void setConditionPictureURL(String conditionPictureURL) throws MalformedURLException{

		new URL(conditionPictureURL);
		this.conditionPictureURL = conditionPictureURL;
	}*/
	
	void addConditionPicture(String conditionPictureURL) throws MalformedURLException {
		new URL(conditionPictureURL);
		this.conditionPictures += conditionPictureURL + " ";
	}
	
	void addConditionPicture(File conditionPictureFile) throws FileNotFoundException {
		if(conditionPictureFile.exists())
			this.conditionPictures += conditionPictureFile.getAbsolutePath() + " ";
		else
			throw new FileNotFoundException();
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
		
		/*int next_id = 1001;
		try {
			next_id = Integer.parseInt(patientRecords.get(patientRecords.size() - 1)[0]) + 1;
		} catch (NumberFormatException e) {
			if(patientRecords.size() == 0) {
				//default to 1001
				System.err.println("Warning: records empty, defaulting to patient ID 1001");
				return 1001;
			} else {
				//attempt to default to 1001, but check that no record has this id, if a record has this id
				//increase to + 1, repeat until successful
				System.err.println("Error: unable to read last patient ID value, please check file manually for problems.\n"
						+ "Looking for first available ID.");
				int candidateID = 1001;
				while(candidateID <= 9999) {
					if(search(0, Integer.toString(candidateID)) != null) 
						candidateID++;
					else
						return candidateID;
				}
				System.err.println("Unable to determine available ID");
				//if(result != null) a match was found and the number must be increased
			}
		}*/

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
	 * Where a conflict between the patientId field of the patient instance and the 
	 * mathematical successor to the last patient id number at the bottom of the 
	 * patientRecords stack is detected, then the value of the latter wins.
	 * 
	 * @see initialize()
	 * @see reloadRecordsFromfile()
	 * @see checkIDConsistency()
	 */
	private void addRecord() throws NumberFormatException {
		String [] thisRecord = { patientId, lName, fName, DOB, address, emergencyPhone, condition, 
				appointments, billing, comments ,patientPictureURL,	conditionPictures,
				conditionURL, };
		if(!checkIDConsistency())
			{
				this.patientId = Integer.toString(getNextId());
				thisRecord[0] = this.patientId;
			}
		patientRecords.add(thisRecord);
	}
	
	public static void editRecord(Patient editedPatient, int indexInArrayList) {
		/*//insert element at position of element to be edited
		patientRecords.add(indexInArrayList, editedPatient.toStringArr());
		//patientRecords.remove(indexInArrayList + 1);*/
		System.out.println("Before:\n"
				+ patientRecords.get(indexInArrayList)[1]);
		patientRecords.set(indexInArrayList, editedPatient.toStringArr());
		System.out.println("After:\n"
				+ patientRecords.get(indexInArrayList)[1]);
	}

	@Override
	public String toString() {
		return this.patientId + ", " + 
				this.lName + ", " + 
				this.fName + ", " + 
				this.DOB + ", " + 
				this.address + ", " + 
				this.emergencyPhone + ", " + 
				this.condition + ", " + 
				this.appointments + ", " + 
				this.billing + ", " + 
				this.comments + ", " + 
				this.patientPictureURL + ", " + 
				this.conditionPictures + ", " + 
				this.conditionURL;
	}
	
	public String[] toStringArr() {
		return this.toString().split(", ");
	}
	
	/**
	 * Called by addRecord() to check for patient id consistency.
	 * 
	 * @return false if the patient id of the calling object is not the next expected
	 * patient id, true otherwise.
	 */
	private boolean checkIDConsistency() {
		if(!(this.patientId.equals(Integer.toString(getNextId()))))
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
	public static void updateRecordsFile() throws IOException {
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
	public static void deleteEntry(int indexInArrayList) throws IndexOutOfBoundsException {
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
		System.out.println(p.patientId);

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
			//p.setconditionPictures("https://images.rapgenius.com/155a55a778c356240f936ed02ff46b8e.736x490x1.jpg");
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
		
		
		
		//Separate test for conditionPictures adders:
		System.out.println("First adders test:");
		try {
			p.addConditionPicture("https://images.rapgenius.com/155a55a778c356240f936ed02ff46b8e.736x490x1.jpg");
		} catch (MalformedURLException e) {
			
			System.err.println("Failed to add URL");
		}
		
		try {
			p.addConditionPicture(new File("download.jpg"));
		} catch (FileNotFoundException e) {
			System.err.println("Failed to found sample file");
		}
		
		System.out.println("Result:");
		String[] split = p.conditionPictures.split(" ");
		
		for(int i = 0; i<split.length; i++) {
			System.out.println("pos " + i + ": " + split[i]);
		}
		//This test shows that the methods can successfully recognize valid URLs and valid files.
		
		//Test with invalid url and invalid file path:

		System.out.println("Second adders test:");
		try {
			p.addConditionPicture("mages.rapgenius.com/155a55a778c356240f936ed02ff46b8e.736x490x1.jpg");
		} catch (MalformedURLException e) {
			
			System.err.println("Failed to add URL");
		}
		
		try {
			p.addConditionPicture(new File("download.BROKEN"));
		} catch (FileNotFoundException e) {
			System.err.println("Failed to found sample file");
		}
		

		System.out.println("Result:");
		
		split = p.conditionPictures.split(" ");
		
		for(int i = 0; i<split.length; i++) {
			System.out.println("pos " + i + ": " + split[i]);
		}
		//this test showed that bad URLs, files will not modify the field
		
		//test for well formed URL/file not pointing at a picture
		System.out.println("Third adders test:");
		try {
			p.addConditionPicture("http://pastebin.com/download.php?i=A90wUvxi");
		} catch (MalformedURLException e) {
			
			System.err.println("Failed to add URL");
		}
		
		try {
			p.addConditionPicture(new File("opencsv-3.6.jar"));
		} catch (FileNotFoundException e) {
			System.err.println("Failed to found sample file");
		}
		

		System.out.println("Result:");
		
		split = p.conditionPictures.split(" ");
		
		for(int i = 0; i<split.length; i++) {
			System.out.println("pos " + i + ": " + split[i]);
		}
		
		//toString and toStringArr seem to be working:
		System.out.println(p.toString());
		
		for(int i = 0; i < p.toStringArr().length; i++) {
			System.out.println(p.toStringArr()[i]);
		}
		
		/*Patient pat = new Patient();
		pat.setComments("PAT");
		Patient.editRecord(pat, 2);*/
		//this test showed that these adder methods do not check if the provided resource is a picture
		//so this is either something that should be fixed, or it should be delegated to the GUI
		//Since the GUI will use a jfilechooser, this component will simply limit the acceptable file formats
		//URLs are trickier. May just display an error icon instead of a picture where the user typed in 
		//a valid URL that is not a picture.
		
		//try out String.split() to allow for multiple URLs to be stored inside a single String instance:
		//this works, but only checks the protocol of the first string, meaning:
		//if we split the string by " ", then only whether the first of the strings so obtained specifies a protocol matters,
		//this is inadequate where any successive string (obtained from the split) is not a well formed URL
		
		//Another issue: this does not allow for filepaths, but the end user would be uploading pictures stored locally, not on 
		//the internet.
		/*try{
			p.setconditionPictures("https://images.rapgenius.com/155a55a778c356240f936ed02ff46b8e.736x490x1.jpg http://media.makeadare.com/img/6160cf6aa/image_bceabb1299.jpg");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		}*/
		
		
		/*for(int i = 0; i<p.conditionPictureURL.split(" ").length; i++)
			System.out.println(
					p.conditionPictureURL.split(" ")[i]);*/
		
		
		/*String[] split = p.conditionPictures.split(" ");
		
		for(int i = 0; i < split.length; i++) {
			//checkURIWellformedness(split[i]); //instance method? static method? external method?
		}*/
		
	}



}