package exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(Long eventId, Long clientId) {
        super("No permission to event request ID: " + eventId + " for client ID: " + clientId);
    }
}
