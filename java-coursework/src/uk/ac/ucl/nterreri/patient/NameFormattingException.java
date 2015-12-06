package uk.ac.ucl.nterreri.patient;

/**
 * Used to throw an exception where a name field is initialized without
 * the first letter being capital.
 * 
 * #Unused
 * 
 * @author nterreri
 *
 */
public class NameFormattingException extends Exception {

	private static final long serialVersionUID = -4194508202009343211L;

	NameFormattingException() {
		super();
	}
	
}
