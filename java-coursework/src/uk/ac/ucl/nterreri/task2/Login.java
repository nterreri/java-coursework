package uk.ac.ucl.nterreri.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
/**
 * Simple console login prompt.
 * <p>
 * The program asks the user to input their username and password until the
 * user gets it right.
 * Correct details are pre-defined in the source code.
 * <p>
 * TODO: 
 * <strike>
 * Task 0: Attend to TODOS attached to individual method javadocs
 * </strike><p>
 * <strike>TASK 1:
 * place login details in a file, make the program access the file
 * looking first for an account name match, then checking for password
 * match.
 * </strike>
 * <p>
 * <strike>TASK 2:
 * allow the user to have a choice to either try again or quit the program.
 * </strike>
 * <p>
 * <strike>TASK 3:
 * Separate various functions into different methods until main method is
 * only calling other methods belonging to the "login" program/class.
 * </strike></p>
 * TASK 4:
 * Integrate with actual login system / attend to last method javadoc todo.
 * </p>
 * 
 * @author ucabter
 * @version 26/10/15 - 2
 *
 */
public class Login {
	final static int DATA_SIZE = 10;

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
		
		Scanner is = new Scanner(System.in);
		String username[] , password[];
		boolean login_success = false;
		
		//Fetch login data
		username = new String[DATA_SIZE];
		password = new String[DATA_SIZE];
		try{

			fetch_login_data(username, password);

		} catch (IOException e){
			System.err.println("Error reading login data file.\n" + 
					"Terminating program.");
			System.exit(1);
		}

		//Main login prompt:
		System.out.println("Log in");
		do
		{
			//Input vars declarations:
			String username_in;
			String pw_in;

			//Assignment of input vars:
			System.out.println("Please enter your username and password.");
			System.out.println();
			System.out.print("Username:");
			username_in = is.next();

			System.out.print("Password:");
			pw_in = is.next();
			is.nextLine();

			//Checking for key validity:
			int i = 0;
			while(i < DATA_SIZE)
			{
				//If username is found in username[],
				if(username[i].equalsIgnoreCase(username_in))
				{
					//, proceed to compare password[],
					if(password[i].equals(pw_in))
						{
							login_success = true;
							System.out.println("Login successful.");
							break;
						}
					//, if password does not match, login failed
					else	
						{
							login_failed(is);
							break;
						}
				}
				
				i++;
			}
			//If i is DATA_SIZE, then input username was not found
			if(i == DATA_SIZE)
				login_failed(is);
			
		} while(!login_success);
		
		is.close();
	}

	/**
	 * Invoked by login_prompt() where data typed does not correspond to a
	 * valid login key.
	 * </p>
	 * @param is An open System.in Scanner instance to read an answer. 
	 * Does not close is.
	 */
	static void login_failed(Scanner is){
		char ans;
		
		System.out.println("Unrecognized username/password combination.");
		System.out.println("Would you like to try again? y/n");
		ans = is.nextLine().charAt(0);
		if(ans == 'y' || ans == 'Y')
			/*do nothing*/;
		else
			{
				System.out.println("Terminating program.");
				is.close();
				System.exit(0);
			}
	}
	
	/**<strike>todo: Read login_data until eof and store usernames and passwords in two 
	 * arrays, matching the position of each by array index.</strike>
	 * @throws IOException 
	 */
	static void fetch_login_data(String[] username, String[] password) throws IOException{


		
		int index = 0;

		BufferedReader is = new BufferedReader(new FileReader("login_data"));

		while(is.ready())
		{
			String nextLine = is.readLine();
			username[index] = nextLine.substring(12);
			nextLine = is.readLine();
			password[index] = nextLine.substring(7);
			index++;
		}

		is.close();
	}
}