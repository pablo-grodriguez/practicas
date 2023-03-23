package es.udc.paproject.backend.rest.dtos;

public class SessionSummaryDto {

    private Long id;
    private long date;

    public SessionSummaryDto() {}

    public SessionSummaryDto(Long id, long date) {
        this.id = id;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
