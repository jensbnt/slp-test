package be.bewire.slp.config;

import be.bewire.slp.exceptions.MalformedBodyException;
import be.bewire.slp.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Error handling. If there is an error in the application, return a 500-error with the message in the body.
 *
 * @author Jens Beernaert
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {

    private Dictionary<Class, HttpStatus> errorHandlingTable;

    public ApplicationExceptionHandler() {
        this.errorHandlingTable = new Hashtable<>();

        errorHandlingTable.put(ResourceNotFoundException.class, HttpStatus.NOT_FOUND);
        errorHandlingTable.put(MalformedBodyException.class, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception ex) {

        HttpStatus status = errorHandlingTable.get(ex.getClass());

        if(status == null)
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(ex.getMessage());
    }
}
