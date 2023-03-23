package es.udc.paproject.backend.rest.dtos;

public class SessionDto {
    //Revisar
    private Long idFilm;
    private String nameFilm;
    private Short duration;
    private Float price;
    private Long dateTime;
    private String nameHall;
    private Short freeLocs;

    public SessionDto(){}

    public SessionDto(Long idFilm,String nameFilm, Short duration, Float price, Long dateTime, String nameHall, Short freeLocs) {
        this.idFilm = idFilm;
        this.nameFilm = nameFilm;
        this.duration = duration;
        this.price = price;
        this.dateTime = dateTime;
        this.nameHall = nameHall;
        this.freeLocs = freeLocs;
    }

    public Long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Long idFilm) {
        this.idFilm = idFilm;
    }

    public String getNameFilm() {
        return nameFilm;
    }

    public void setNameFilm(String nameFilm) {
        this.nameFilm = nameFilm;
    }

    public Short getDuration() {
        return duration;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public String getNameHall() {
        return nameHall;
    }

    public void setNameHall(String nameHall) {
        this.nameHall = nameHall;
    }

    public Short getFreeLocs() {
        return freeLocs;
    }

    public void setFreeLocs(Short freeLocs) {
        this.freeLocs = freeLocs;
    }
}
