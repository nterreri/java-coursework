package uk.ac.ucl.nterreri.task3;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


public class Patient {
	//private:
	//fields:
	private int patient_id = 1000;
	private String l_name = "LASTNAME";
	private String f_name = "FIRSTNAME";
	private String DOB = "00/00/0000";
	private String address = "ADDRESS";
	private String emergency_phone = "0000000000";
	private String condition = "CONDITION";
	private String patientPictureURL = null;
	private String conditionPcitureURL = null;
	private String conditionURL = null;
	private String appointments = null;
	private String billing = null;
	private String comments = null;

	//public:
	//constructors:
	Patient() throws NumberFormatException, IOException, Exception {
		this.patient_id = getNextId();
	}

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

	private int getNextId() throws NumberFormatException, IOException, Exception {
		CSVReader reader = new CSVReader(new FileReader("patient_records"));
		int next_id = Integer.parseInt(reader.readNext()[0]) + 1;

		if(next_id > 9999 || next_id < 1000) {
			reader.close();
			throw (new Exception());
		}
		
		reader.close();
		return next_id;
	}
	
	private void updateRecord() throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter("patient_records"));
		
		writer.
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
