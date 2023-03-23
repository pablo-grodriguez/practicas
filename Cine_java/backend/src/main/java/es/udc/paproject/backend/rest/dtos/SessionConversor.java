package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.Session;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class SessionConversor {

    private SessionConversor(){}

    public final static SessionDto toSessionDto(Session session){
        return new SessionDto(session.getFilm().getId(),session.getFilm().getTitle(),session.getFilm().getDuration(),session.getPrice(),
                toMillis(session.getDateTime()), session.getHall().getName(),session.getFreeLocs());
    }

    private final static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

}
