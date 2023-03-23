package es.udc.paproject.backend.rest.dtos;

public class FilmDto {

    private Long id;
    private String title;
    private String summary;
    private Short duration;

    public FilmDto() {}

    public FilmDto(Long id,String title, String summary, Short duration) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Short getDuration() {
        return duration;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }
}
