package at.ac.tuwien.sepm.groupphase.backend.exception;

public class InvalidOldPasswordException extends RuntimeException {

    public InvalidOldPasswordException(String e) {
        super(e);
    }

    public InvalidOldPasswordException() {}
}
