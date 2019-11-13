package util.exception;

public class CustomerExistsException extends Exception {

    public CustomerExistsException() {
    }

    public CustomerExistsException(String msg) {
        super(msg);
    }
}
