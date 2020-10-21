package at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions;

/**
 * The type Insufficient staff exception.
 *
 * @author Philipp Schießl
 */
public class InsufficientStaffException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new Insufficient staff exception.
	 */
	public InsufficientStaffException() {
		super();
	}

	/**
	 * Instantiates a new Insufficient staff exception.
	 *
	 * @param message the message
	 */
	public InsufficientStaffException(String message) {
		super(message);
	}
	

}
