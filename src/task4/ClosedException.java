package task4;

public class ClosedException extends Exception {

    // no msg constructor
    public ClosedException() {
    };

    // msg constructor
    public ClosedException(String message) {
        super(message);
    }
}
