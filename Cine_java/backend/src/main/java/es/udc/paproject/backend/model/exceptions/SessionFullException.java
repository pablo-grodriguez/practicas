package es.udc.paproject.backend.model.exceptions;

public class SessionFullException extends Exception{

    Long sessionId;
    Object freeSeats;

    public SessionFullException(Long sessionId, Object freeSeats) {
        super("Para la sesion "+sessionId+" solo quedan "+freeSeats+" huecos libres");
        this.sessionId = sessionId;
        this.freeSeats = freeSeats;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Object getFreeSeats() {
        return freeSeats;
    }
}
