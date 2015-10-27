package task3;

import java.util.ArrayList;

public class Patient {
	//public:
	//constructors:
	Patient(){}
	Patient(String l_name, String f_name, String DOB, String address, 
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
	}
	
	//other methods:
	
	//private:
	//fields:
	int parient_id = 1000;
	String l_name = null;
	String f_name = null;
	String DOB = "00/00/00";
	String address = null;
	String emergency_phone = "0000000000";
	String condition = null;
	String patientPictureURL = null;
	String[] conditionPcitureURL = null;
	String conditionURL = null;
	ArrayList<String[]> appointments = null;
	String[] billing = null;
	String comments = null;
}
