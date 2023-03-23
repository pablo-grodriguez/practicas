package es.udc.paproject.backend.rest.dtos;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class PurchaseParamsDto {

    private Byte amount;
    private String creditCard;
    private Long sessionId;

    public PurchaseParamsDto() {
    }

    public PurchaseParamsDto(Long id, Byte amount, String creditCard, Long sessionId) {
        this.amount = amount;
        this.creditCard = creditCard;
        this.sessionId = sessionId;
    }

    @NotNull
    @Range(min = 1,max = 10)
    public Byte getAmount() {
        return amount;
    }

    public void setAmount(Byte amount) {
        this.amount = amount;
    }

    @NotNull
    @Size(min=16, max=16)
    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @NotNull
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
}
