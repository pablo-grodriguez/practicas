package es.udc.paproject.backend.model.exceptions;

public class DifferentCreditCardException extends Exception{

    private Long id;
    private String creditCard;

    public DifferentCreditCardException(Long id, String creditCard) {

        this.id = id;
        this.creditCard = creditCard;

    }

    public Long getUserName() {
        return id;
    }

    public String getPassword() {
        return creditCard;
    }
}
