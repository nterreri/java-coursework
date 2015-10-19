package task2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Stream;
/**
 * Simple console login prompt.
 * <p>
 * The program asks the user to input their username and password until the
 * user gets it right.
 * Correct details are pre-defined in the source code.
 * <p>
 * TODO: 
 * Task 0: Attend to TODOS attached to individual method javadocs
 * <p>
 * TASK 1:
 * place login details in a file, make the program access the file
 * looking first for an account name match, then checking for password
 * match.
 * <p>
 * <strike>TASK 2:
 * allow the user to have a choice to either try again or quit the program.
 * </strike>
 * <p>
 * <strike>TASK 3:
 * Separate various functions into different methods until main method is
 * only calling other methods belonging to the "login" program/class.
 * </strike>
 * 
 * @author ucabter
 * @version 19/10/15 - 1
 *
 */
public class login {
	//Existing data:
	static String account = "Vosgi Di Pasqua";
	static String pw = "4cZpAf";

public static void main(String[] args) {
	
		
		login_prompt();
		System.out.println("Terminating program.");
	}

	/**
	 * Reads one line (the account name) and then one token (the password)
	 * Confronts both with pre-existing data, then does nothing.
	 * <p>
	 * 
	 * TODO: modify the function so that the string values are passed onto
	 * some login system.
	 */
static void login_prompt() {

		String username[] = null, password[] = null;
		
		try{
			
			fetch_login_data(username, password);
			
		} catch (IOException e){
			System.err.println("Error reading login data file.\n" + 
					"Terminating program.");
			System.exit(1);
		}
		
		Scanner is = new Scanner(System.in);
		
		System.out.println("Log in");
		
		while(true)
		{
			char ans;
			
			String account_in;
			String pw_in;
			
			System.out.println("Please enter your username and password.");
			System.out.println();
			System.out.print("Username:");
			account_in = is.next();
			
			if(!account.equalsIgnoreCase(account_in))
			{
				System.out.println("Account name not recognized.");
				System.out.println("Would you like to try again? y/n");
				ans = is.nextLine().charAt(0);
				if(ans == 'y' || ans == 'Y')
				continue;
				else
					break;
			}
			
			System.out.println();
			System.out.print("Password:");
			pw_in = is.next();
			is.nextLine();
			if(!pw.equals(pw_in))
			{
				System.out.println("Incorrect password.");
				System.out.println("Would you like to try again? y/n");
				ans = is.nextLine().charAt(0);
				if(ans == 'y' || ans == 'Y')
				continue;
				else
					break;
			}
			
			System.out.println("Login successful.");
			break;
		}
		
		is.close();
	}

/**TODO: Read login_data until eof and store usernames and passwords in two 
 * arrays, matching the position of each by array index.
 * @throws IOException 
 */
static void fetch_login_data(String[] username, String[] password) throws IOException{
	
	
	username = new String[10];
	password = new String[10];
	int index = 0;
	
	BufferedReader is = new BufferedReader(new FileReader("login_data"));
	
	while(is.ready())
	{
		String nextLine = is.readLine();
		username[index]= nextLine.substring(11);
		nextLine = is.readLine();
		password[index] = nextLine.substring(9);
		
	}
	
	
}
}
