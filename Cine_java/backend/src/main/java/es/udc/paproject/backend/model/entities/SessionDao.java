package es.udc.paproject.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionDao extends PagingAndSortingRepository<Session, Long> {

    List<Session> findByDateTimeBetweenOrderByFilmTitleAscDateTimeAsc(LocalDateTime before, LocalDateTime after);
}
