package es.udc.paproject.backend.model.exceptions;

import java.time.LocalDateTime;

public class AlreadyStartedFilmException extends Exception {
    private LocalDateTime localDateTime;


    public AlreadyStartedFilmException(LocalDateTime localDateTime) {

        this.localDateTime = localDateTime;


    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }


}
