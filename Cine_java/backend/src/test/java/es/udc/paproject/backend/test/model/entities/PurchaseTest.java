package es.udc.paproject.backend.test.model.entities;

import es.udc.paproject.backend.model.entities.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseTest {

    private Session createSession(float price){
        return new Session( LocalDateTime.now().withSecond(0).withNano(0), price, (short) 20, new Hall(), new Film());
    }

    private Purchase createPurchase(Byte tickets, Session session){
        return new Purchase(tickets, "1234123412341234", LocalDateTime.now().withSecond(0).withNano(0), false, session, new User());
    }

    @Test
    public void testGetTotalPrice(){

        float price = 7.45F;
        byte quantity = 3;

        Session session = createSession(price);
        Purchase purchase = createPurchase(quantity, session);

        float totalPrice = price*quantity;

        assertEquals(totalPrice, purchase.getTotalPrice());
    }
}
