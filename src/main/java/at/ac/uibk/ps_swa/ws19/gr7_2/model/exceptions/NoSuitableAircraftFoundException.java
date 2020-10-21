package at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions;

/**
 * The type No suitable aircraft found exception.
 *
 * @author Philipp Schießl
 */
public class NoSuitableAircraftFoundException extends Exception {


	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new No suitable aircraft found exception.
	 */
	public NoSuitableAircraftFoundException() {
		super();
	}

	/**
	 * Instantiates a new No suitable aircraft found exception.
	 *
	 * @param message the message
	 */
	public NoSuitableAircraftFoundException(String message) {
		super(message);
	}

}
