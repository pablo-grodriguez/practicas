package es.udc.paproject.backend.model.entities;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
public class Session {

    private Long id;
    private LocalDateTime dateTime;
    private Float price;
    private Short freeLocs;
    private Hall hall;
    private Film film;
    private Long version;

    public Session() {
    }

    public Session(LocalDateTime dateTime, Float price, Short freeLocs, Hall hall, Film film) {
        this.dateTime = dateTime;
        this.price = price;
        this.freeLocs = freeLocs;
        this.hall = hall;
        this.film = film;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Short getFreeLocs() {
        return freeLocs;
    }

    public void setFreeLocs(Short freeLocs) {
        this.freeLocs = freeLocs;
    }

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="hallId")
    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="filmId")
    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}