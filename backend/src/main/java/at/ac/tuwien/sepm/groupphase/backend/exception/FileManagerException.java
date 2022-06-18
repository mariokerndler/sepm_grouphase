package at.ac.tuwien.sepm.groupphase.backend.exception;

public class FileManagerException extends RuntimeException {

    public FileManagerException(String message) {
        super(message);
    }

    public FileManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileManagerException(Exception e) {
        super(e);
    }
}
