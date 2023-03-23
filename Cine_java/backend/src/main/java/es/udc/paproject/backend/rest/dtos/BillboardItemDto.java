package es.udc.paproject.backend.rest.dtos;

import java.util.List;

public class BillboardItemDto {

    private String filmName;
    private Long idFilm;
    private List<SessionSummaryDto> sessionList;

    public BillboardItemDto() {}

    public BillboardItemDto(String filmName, Long idFilm, List<SessionSummaryDto> sessionList) {
        this.filmName = filmName;
        this.idFilm = idFilm;
        this.sessionList = sessionList;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public Long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Long idFilm) {
        this.idFilm = idFilm;
    }

    public List<SessionSummaryDto> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<SessionSummaryDto> sessionList) {
        this.sessionList = sessionList;
    }
}
