
package guideLines.exceptions;

public class FullDestinationsException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6106186743708342260L;

	public FullDestinationsException() {
        super("The maximum of 3 destinations is reached.");
    }
    
}
