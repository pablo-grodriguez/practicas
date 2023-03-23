package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.BillboardItem;
import es.udc.paproject.backend.model.entities.Session;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class BillboardConversor {

    private BillboardConversor(){}

    public final static List<BillboardItemDto> toBillboardItemDtos(List<BillboardItem> billboardItems) {
        return billboardItems.stream().map(p -> toBillboardItemDto(p)).collect(Collectors.toList());
    }

    public final static BillboardItemDto toBillboardItemDto(BillboardItem billboardItem) {

        return new BillboardItemDto(billboardItem.getFilm().getTitle(), billboardItem.getFilm().getId(), toSessionSummaryDtos(billboardItem.getSessionList()));

    }

    public final static List<SessionSummaryDto> toSessionSummaryDtos(List<Session> sessions) {
        return sessions.stream().map(p -> toSessionSummaryDto(p)).collect(Collectors.toList());
    }

    public final static SessionSummaryDto toSessionSummaryDto(Session session) {

        return new SessionSummaryDto(session.getId(), toMillis(session.getDateTime()));
    }

    private final static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
