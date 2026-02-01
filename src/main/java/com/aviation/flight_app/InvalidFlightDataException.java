package aviation;
public class InvalidFlightDataException extends Exception {
    public InvalidFlightDataException(String message) {
        super(message);
    }
}