package es.udc.paproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DeliverTickedParamsDto {

    private Long idPurchase;
    private String creditCard;

    public DeliverTickedParamsDto(){}

    @NotNull
    public Long getIdPurchase() {
        return idPurchase;
    }

    public void setIdPurchase(Long idPurchase) {
        this.idPurchase = idPurchase;
    }

    @NotNull
    @Size(min = 16, max = 16)
    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }
}
