package uk.ac.ucl.nterreri.task3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.jsoup.Jsoup;

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
	private String fName = "FIRSTNAME";				//2 in patientRecords String array element
	private String DOB = "00/00/0000";				//3 in patientRecords String array element
	private String address = "ADDRESS";				//4 in patientRecords String array element
	private String emergencyPhone = "0000000000";	//5 in patientRecords String array element, 10 digits number
	private String condition = "CONDITION";			//6 in patientRecords String array element
	private String appointments = null;				//7 in patientRecords String array element
	private String billing = null;					//8 in patientRecords String array element
	private String comments = null;					//9 in patientRecords String array element
	private String patientPicture = null;			//10 in patientRecords String array element
	private String conditionPictures = null;		//11 in patientRecords String array element
	private String conditionURL = null;				//12 in patientRecords String array element
	private static ArrayList<String[]> patientRecords; // = new List<String[]>(13);
	private static File recordsFile;// = new File();
	public static final int ELEMENTS = 13;			//number of elements in each member of the arraylist

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
		conditionPictures = new String();//this must be reinitialized at instance creation because its default value should be
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
		this.patientPicture = patientRecords.get(indexInArrayList)[10];
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

	public String[] getConditionPictures() {
		String[] returnVal;
		
		if(conditionPictures.length() != 0) {
			returnVal = conditionPictures.split("\\s+");
		} else {
			returnVal = null;
		}
		
		return returnVal;
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

	public String getPatientPicture() {
		return patientPicture;
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
	public void setName(String lName, String fName) throws NameFormattingException {
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
	public void setNameNoCheck(String lName, String fName) {

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
	public void setLastName(String lName) throws NameFormattingException {
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
	public void setLastNameNoCheck(String lName) {
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
	public void setFirstName(String fName) throws NameFormattingException {
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
	public void setFirstNameNoCheck(String fName) {

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
	public void setDOB(String DOB) throws ParseException {
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
	public void setAddress(String address) {

		//address can be garbage
		this.address = address;
	}

	//TODO http://stackoverflow.com/questions/3507353/how-to-parse-only-text-from-html
	/**
	 * Sets patient condition.<p>
	 * Will parse the html document which is the text displayed in
	 * a PatientEditorFrame's "Condition" editor pane, and extract text
	 * from the body of the html.<p>
	 * 
	 * When typing into a JEditorPane which has been set to "text/html",
	 * the text typed will automatically between the "body" tags of the html document.
	 * Therefore, the text conted typed by the user must be retrieved by extracting it.<p>
	 * 
	 * The text extracted is appeneded to the string "https://en.wikipedia.org/wiki/"
	 * to automatically provide a default hyperreference for the patient's condition.<p>
	 * 
	 * Uses Jsoup: http://jsoup.org/<p>
	 * 
	 * @param condition
	 * @throws MalformedURLException 
	 * @see JEditorPane
	 */
	public void setCondition(String condition) throws MalformedURLException {

		this.condition = Jsoup.parse(condition).body().text();
		setConditionURL("https://en.wikipedia.org/wiki/" + this.condition);
	}

	/**
	 * Sets patient appointments, no checks performed.
	 * 
	 * @param appointments
	 */
	public void setAppointments(String appointments) {

		//appointments can be garbage
		this.appointments = appointments;
	}

	/**
	 * Sets patient billing, no checks performed.
	 * 
	 * @param billing
	 */
	public void setBilling(String billing) {

		//billing address/info can be garbage:
		this.billing = billing;
	}

	/**
	 * Sets patient comments, no checks performed.
	 * 
	 * @param comments
	 */
	public void setComments(String comments) {

		//comments can be garbage:
		this.comments = comments;
	}

	//URL setters:
	//TODO: credit http://stackoverflow.com/a/5803008 for url checking
	/**
	 * Sets patient picture URL unless URL is malformed. In which case, an
	 * exception is thrown.
	 * 
	 * @param patientPicture
	 * @throws MalformedURLException
	 */
	public void setPatientPictureURL(String patientPictureURL) throws MalformedURLException{

		URL constructionAttempt = new URL(patientPictureURL);
		this.patientPicture = patientPictureURL;
	}

	public void setPatientPictureFilepath(String patientPicturePath) throws FileNotFoundException, URISyntaxException{

		if(new File(new URI(patientPicturePath)).isFile())
			this.patientPicture = patientPicturePath;
		else
			throw new FileNotFoundException();
		//if the file is chosen through filechooser it should not fail to find the file
		//the original version of this method had a File parameter, this was designed to receive
		//a file directly from the filechooser, this made it hard to retrieve the absolute path
		//preceeded by the file protocol for storage inside a String/file, so the idea was discarded
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

	public void removePatientPicture() {
		this.patientPicture = "";
		
	}
	
	public void addConditionPicture(String conditionPictureURL) throws MalformedURLException {
		new URL(conditionPictureURL);
		this.conditionPictures += conditionPictureURL + " ";
	}

	public void addConditionPicture(File conditionPictureFile) throws FileNotFoundException {
		if(conditionPictureFile.exists())
			this.conditionPictures += conditionPictureFile.getAbsolutePath() + " ";
		else
			throw new FileNotFoundException();
	}

	public void removeConditionPicture(int conditionPicIndex) throws IndexOutOfBoundsException {
		
		String[] condPicArrBuffer = getConditionPictures();
		condPicArrBuffer[conditionPicIndex] = "";
		//this.conditionPictures = Arrays.toString(getConditionPictures()); results in "[http1","http2","http3]", not good for our purposes
		this.conditionPictures = "";
		for(int i = 0; i < condPicArrBuffer.length; i++)
			this.conditionPictures += condPicArrBuffer[i] + " ";
		
	}
	
	
	/**
	 * Sets patient condition description URL unless URL is malformed, in which case
	 * an exception is thrown.
	 * 
	 * @param conditionURL
	 * @throws MalformedURLException
	 */
	public void setConditionURL(String conditionURL) throws MalformedURLException{

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
	public void setEmergencyPhone(String emergencyPhone) throws NumberFormatException {
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

	//has to be static to be used in importCSV()
	/**
	 * 
	 * @return the next integer after the patient id number of the last registered patient,
	 * assumes the patientRecords field is not empty. Throws number format exception if this
	 * occurs.
	 * 
	 * @throws NumberFormatException
	 * @see Patient()
	 */
	public static int getNextId() throws NumberFormatException {
		//the last int is obtained by parsing the first element of the last String 
		//array in the static field "patientRecords":
		int next_id;
		try{

			next_id = Integer.parseInt(patientRecords.get(patientRecords.size() - 1)[0]) + 1;
		} catch (ArrayIndexOutOfBoundsException e) {
			//if patientRecords is not initialized, set next_id to base:
			next_id = 1000;
		}

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
	public void addRecord() throws NumberFormatException {
		String [] thisRecord = { patientId, lName, fName, DOB, address, emergencyPhone, condition, 
				appointments, billing, comments ,patientPicture,	conditionPictures,
				conditionURL, };
		
		if(!checkIDConsistency()) {
			this.patientId = Integer.toString(getNextId());
			thisRecord[0] = this.patientId;
		}
		patientRecords.add(thisRecord);
	}

	public static void editRecord(Patient editedPatient, int indexInArrayList) {
		patientRecords.set(indexInArrayList, editedPatient.toStringArr());
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
				this.patientPicture + ", " + 
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
	
	public static void exportCSV(File exportFile) throws IOException {

		FileWriter fw = new FileWriter(exportFile);
		CSVWriter csvw = new CSVWriter(fw);

		csvw.writeAll(patientRecords);
		csvw.flush();

		csvw.close();
		fw.close();
	}

	public static void importCSV(File importFile) throws IOException {
		//reenable eventually:
		//backup(recordsFile);
		//method();
		
		BufferedReader fr = new BufferedReader(new FileReader(importFile));
		FileWriter fw = new FileWriter(recordsFile.getPath(), true);

		CSVReader csvr = new CSVReader(fr);
		CSVWriter csvw = new CSVWriter(fw);
		
		String[] nextLine;
		String[] buffer;
		
		nextLine = csvr.readNext();
		buffer = new String[ELEMENTS];
		
		//Vector<String[]> synchronousList = new Vector<String[]>(patientRecords);
		
		while(nextLine != null && nextLine.length > 0 ) {
			
			buffer[0] = Integer.toString(getNextId());
			for(int i = 1; i < ELEMENTS; i++) {
				buffer[i] = nextLine[i];
			}
			
			
			
			//fw.flush();
			//patientRecords.add(buffer);  //would have been better to keep adding them
			//as the loop went on, but I discovered every time the array would grow, it would replace
			//all of the elements appended to the array list so far with the last appended element
			//I believe this has something to do with array list being not synchonous
			//Attempted increasing the size manually, but then discovered there is no clean way
			//to get the capacity of an array list in Java (unlike in C++)
			//
			//very, very frustrating.
			patientRecords.add(buffer);
			csvw.writeNext(buffer);
			nextLine = csvr.readNext();
			/*for(int i = 1; i < ELEMENTS; i++) {
				buffer[i] = nextLine[i];
				if(i < ELEMENTS - 1)
					fw.write(buffer[i] + ", ");
				else
					fw.write(buffer[i]);
				fw.flush();
			}
			
			fw.write("\n");
			nextLine = csvr.readNext();*/
		}


		csvr.close();
		csvw.close();
		fr.close();
		fw.close();
		//refresh records:
		patientRecords = (ArrayList<String[]>) getRecordsFromFile(recordsFile);
	}
	
	public static void method() {
		
		String [] toadd = new String[10];
		for(int i = 0; i < 10; i++) {
			
			toadd[i] = Integer.toString(i);
			patientRecords.add(toadd);
		}
		
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

	public static void generateEmptyRecords() throws IOException {
		recordsFile = new File(recordsFile.getAbsolutePath());
		recordsFile.createNewFile();
	}
	//private static void writeCSV(File targetFile) throws IOException, FileNotFoundException

	/**
	 * Removes specified patient record from the array list loaded in memory, the file
	 * should then be update through updateRecordsFile()
	 * 
	 * @param indexInArrayList
	 * @throws IndexOutOfBoundsException
	 * @throws IOException 
	 * @see updateRecordsFile()
	 */
	public static void deleteEntry(int indexInArrayList) throws IndexOutOfBoundsException, IOException {
		patientRecords.remove(indexInArrayList);
		Patient.updateRecordsFile();
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
	public static ArrayList<String[]> search(int field, String value) {

		ArrayList<String[]> result = new ArrayList<String[]>();

		for(int i = 0; i < patientRecords.size(); i++) {
			if(patientRecords.get(i)[field].contains(value))
				result.add(patientRecords.get(i));
		}

		return result;
	}

}