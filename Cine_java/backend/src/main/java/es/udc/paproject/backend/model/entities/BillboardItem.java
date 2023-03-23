package es.udc.paproject.backend.model.entities;

import java.util.List;
import java.util.Objects;

public class BillboardItem {
    private Film film;
    private List<Session> sessionList;


    public BillboardItem(Film film, List<Session> sessionList) {
        this.film = film;
        this.sessionList = sessionList;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    public void addSession(Session session){
        this.sessionList.add(session);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillboardItem that = (BillboardItem) o;
        return Objects.equals(film, that.film) && Objects.equals(sessionList, that.sessionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(film, sessionList);
    }
}
