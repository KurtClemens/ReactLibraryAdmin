package world.inetumrealdolmen.backendrlibrary.exception;

/**
 * This exception will be thrown if a user wants to borrow
 * a book that is currently not available.
 *
 * @author  Kurt Clemens
 * @version 1.0
 * @since   2023-04-05
 */
public class NotAvailableException extends RuntimeException{
    public NotAvailableException(String message) {super(message);}
}
