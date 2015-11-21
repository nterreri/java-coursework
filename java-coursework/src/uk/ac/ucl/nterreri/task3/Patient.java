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


class Patient {

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


	//instance constructors:
	Patient() throws NumberFormatException, Exception {
		this.patient_id = Integer.toString(getNextId());
	}

	//setters:
	void setName(String l_name, String f_name) {
		if(!Character.isUpperCase(l_name.charAt(0)) || 
				!Character.isUpperCase(f_name.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			;
		this.f_name = f_name;
		this.l_name = l_name;
	}

	void setLastName(String l_name) {
		if(!Character.isUpperCase(l_name.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			;
		this.l_name = l_name;
	}

	void setFirstName(String f_name) {
		if(!Character.isUpperCase(f_name.charAt(0)))
			//First letter is lowercase. Confirm? JDIALOG
			//textbox in dialog to edit, ok cancel buttons
			;
		this.f_name = f_name;
	}

	void setDOB(String DOB) throws ParseException {
		SimpleDateFormat localDF;
		localDF = (SimpleDateFormat) DateFormat.getDateInstance();

		//ParseException thrown here, to be caught inside main program
		localDF.parse(DOB);

		//if no exception is thrown:
		this.DOB = DOB;
	}

	void setAddress(String address) {

		//address can be garbage
		this.address = address;
	}

	void setCondition(String condition) {

		//condition can be garbage:
		this.condition = condition;
	}

	void setAppointments(String appointments) {

		//appointments can be garbage
		this.appointments = appointments;
	}

	void setBilling(String billing) {

		//billing address/info can be garbage:
		this.billing = billing;
	}

	void setComments(String comments) {

		//comments can be garbage:
		this.comments = comments;
	}

	//URL setters:
	//TODO: credit http://stackoverflow.com/a/5803008 for url checking
	void setPatientPictureURL(String patientPictureURL) {

		try {
			URL constructionAttempt = new URL(patientPictureURL);
			this.patientPictureURL = patientPictureURL;
		} catch (MalformedURLException e) {
			//feedback to user via gui
		}
	}

	void setConditionPictureURL(String conditionPictureURL) {

		try {
			URL constructionAttempt = new URL(conditionPictureURL);
			this.conditionPictureURL = conditionPictureURL;
		} catch (MalformedURLException e) {
			//feedback to user via gui
		}
	}

	void setConditionURL(String conditionURL) {

		try {
			URL constructionAttempt = new URL(conditionURL);
			this.conditionURL = conditionURL;
		} catch (MalformedURLException e) {
			//feedback to user via gui
		}
	}

	//change throw here to something that identifies the problem or
	void setEmergencyPhone(String emergencyPhone) throws Exception {
		if(emergencyPhone.length() != 10)
		{
			Exception e = new Exception(/*message?*/);
			throw e;
		}

		Integer.parseUnsignedInt(emergencyPhone);

		//If no exception has been thrown:
		this.emergencyPhone = emergencyPhone;
	}

	static void initialize() throws FileNotFoundException, IOException {
		patientRecords = new ArrayList<String[]>(20);
		patientRecords = (ArrayList<String[]>) getRecordsFromFile();
		
		for(int i = 0; i < patientRecords.size(); i++) {
			patientRecords.set(i, new String[12]); 
		}
	}

	private static List<String[]> getRecordsFromFile() throws FileNotFoundException, IOException  {
		CSVReader reader = new CSVReader(new FileReader("patient_records"));
		return reader.readAll();
	}

	private int getNextId() throws NumberFormatException, Exception {
		//the last int is obtained by parsing the first element of the last String 
		//array in the static field "patientRecords":
		int next_id = Integer.parseInt(patientRecords.get(patientRecords.size() - 1)[0]);

		if(next_id > 9999 || next_id < 1000) {
			throw (new Exception("Unexpected patient id number as last patient" +
					" record."));
		}

		return next_id;
	}

	private void addRecord() {
		String [] thisRecord = { l_name, f_name, DOB, address, emergencyPhone, condition, patientPictureURL,
				conditionPictureURL, conditionURL, appointments, billing, comments };
		patientRecords.add(thisRecord);
	}

	private static void updateRecordsFile() throws IOException {
		backup(recordsFile);


	}

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

	private static void restorePreviousFile(File recordsFile) throws IOException, FileNotFoundException {
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

	private static void deleteEntry(int indexInArrayList) throws IndexOutOfBoundsException {
		patientRecords.remove(indexInArrayList);
	}

	private static ArrayList<String[]> search(int field, String value) {

		ArrayList<String[]> result = new ArrayList<String[]>();
		
		for(int i = 0; i < patientRecords.size(); i++) {
			if(patientRecords.get(i)[field].equals(value))
				result.add(patientRecords.get(i));
		}
		
		return result;
	}
	/*Patient(String l_name, String f_name, String DOB, String address, 
			String emergency_phone, String condition, String patientPictureURL,
			String conditionURL){
		this.l_name = l_name;
		this.f_name = f_name;
		this.DOB = DOB;
		this.address = address;
		this.emergency_phone = emergency_phone;
		this.condition = condition;
	}
	Patient(String l_name, String f_name, String DOB, String address, 
			String emergency_phone, String condition, String conditionURL, 
			boolean dummy){
		this.l_name = l_name;
		this.f_name = f_name;
		this.DOB = DOB;
		this.address = address;
		this.emergency_phone = emergency_phone;
		this.condition = condition;
	}
	Patient(String l_name, String f_name, String DOB, String address, 
			String emergency_phone, String condition, String patientPictureURL){
		this.l_name = l_name;
		this.f_name = f_name;
		this.DOB = DOB;
		this.address = address;
		this.emergency_phone = emergency_phone;
		this.condition = condition;
	}
	Patient(String l_name, String f_name, String DOB, String address, 
			String emergency_phone, String condition){
		this.l_name = l_name;
		this.f_name = f_name;
		this.DOB = DOB;
		this.address = address;
		this.emergency_phone = emergency_phone;
		this.condition = condition;
	}
	Patient(String l_name, String f_name, String DOB, String address, 
			String condition){
		this.l_name = l_name;
		this.f_name = f_name;
		this.DOB = DOB;
		this.address = address;
		this.condition = condition;
	}
	Patient(String l_name, String f_name, String condition){
		this.l_name = l_name;
		this.f_name = f_name;
		this.condition = condition;
	}*/

	//other methods:



}