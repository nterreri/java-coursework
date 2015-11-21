package uk.ac.ucl.nterreri.task3backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


class PatientCopy {
	//private:
	//fields:
	private String patient_id = "1000";
	private String l_name = "LASTNAME";
	private String f_name = "FIRSTNAME";
	private String DOB = "00/00/0000";
	private String address = "ADDRESS";
	private String emergencyPhone = "0000000000";//10 digits
	private String condition = "CONDITION";
	private String appointments = null;
	private String billing = null;
	private String comments = null;
	private String patientPictureURL = null;
	private String conditionPictureURL = null;
	private String conditionURL = null;
	private static List<String[]> patientRecords;
	private static File recordsFile;// = new File();
	
	
	//public:
	//constructors:
	PatientCopy() throws NumberFormatException, Exception {
		this.patient_id = Integer.toString(getNextId());
	}

	//other methods:
	
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
	
	private static List<String[]> getRecordsFile() throws FileNotFoundException, IOException  {
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
		String [] thisRecord = { l_name, f_name, DOB, address, emergency_phone, condition, patientPictureURL,
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