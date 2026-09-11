package friday;

/**
 * Signals that Friday could not understand or process a user command,
 * e.g. an unrecognised command word or a command missing required details.
 */
public class FridayException extends Exception {
    public FridayException(String message) {
        super(message);
    }
}
