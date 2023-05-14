package world.inetumrealdolmen.backendrlibrary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = EmailException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST)
    public String emailException(Exception ex){
        return ex.getMessage();
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, code = HttpStatus.CONFLICT)
    public String alreadyExistsException(Exception ex){
        return ex.getMessage();
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, code = HttpStatus.NOT_FOUND)
    public String notFoundExceptionHandler(Exception ex){
        return ex.getMessage();
    }

    @ExceptionHandler(value = NotAvailableException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, code = HttpStatus.NOT_FOUND)
    public String notAvailableException(Exception ex){
        return ex.getMessage();
    }
}
