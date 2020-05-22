package be.bewire.slp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// TODO: handle exception here or in ApplicationExceptionHandler
//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "bad request")
public class MalformedBodyException extends RuntimeException {
    public MalformedBodyException() {
        super();
    }

    public MalformedBodyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedBodyException(String message) {
        super(message);
    }

    public MalformedBodyException(Throwable cause) {
        super(cause);
    }
}
