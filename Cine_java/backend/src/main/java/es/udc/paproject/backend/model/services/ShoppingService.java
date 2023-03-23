package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Purchase;
import es.udc.paproject.backend.model.exceptions.*;

public interface ShoppingService {

    // FUNC-4: comprar entradas. TODO "añadir comprobacion de rol=VIEWER"
    Long purchase(int numEntradas, String creditCard, Long userId, Long sessionId)
            throws InstanceNotFoundException, SessionFullException, AlreadyStartedFilmException;

    //FUNC-5: visualizar el histórico de compras.
    Block<Purchase> findAllPurchase(Long userId, int page, int size)
        throws InstanceNotFoundException;
    /*
    * FUNC-6: entregar las entradas de una compra.
    * */
    void deliverTickets(Long id,String creditCard)
            throws InstanceNotFoundException, DifferentCreditCardException, AlreadyStartedFilmException, AlreadyTicketsDeliveredException;
}
