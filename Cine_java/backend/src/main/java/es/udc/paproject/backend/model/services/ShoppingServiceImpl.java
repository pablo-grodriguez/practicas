package es.udc.paproject.backend.model.services;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import es.udc.paproject.backend.model.entities.*;

import es.udc.paproject.backend.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ShoppingServiceImpl implements ShoppingService {

    @Autowired
    private PermissionChecker permissionChecker;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private PurchaseDao purchaseDao;

    /**
     * Realiza la compra de una cantidad de entradas para una sesion
     * @param numEntradas es el numero de entradas que se intenta comprar
     * @param creditCard es el numero de tarjeta de la compra
     * @param userId es el id del usuario de tipo espectador que realiza la compra
     * @param sessionId es el id de la sesion sobre que se realiza la compra
     * @return Devuelve el id de la compra realizada con exito
     * @throws InstanceNotFoundException Se lanza esta excepcion si la sesion no existe
     * @throws SessionFullException Se lanza esta excepcion si la sesion no tiene suficientes asientos libre para satisfacer la compra solicitada
     * @throws AlreadyStartedFilmException Se lanza esta excepcion si la sesion ya comenzo
     */
    @Override
    public Long purchase(int numEntradas, String creditCard, Long userId, Long sessionId)
            throws InstanceNotFoundException, SessionFullException, AlreadyStartedFilmException {

        Optional<Session> optSession = sessionDao.findById(sessionId);
        if (optSession.isEmpty()) {
            throw new InstanceNotFoundException("Session", sessionId);
        }
        Session session = optSession.get(); //obtenemos sesion

        User user = permissionChecker.checkUser(userId); //Obtenemos user

        if (session.getDateTime().isBefore(LocalDateTime.now())){ //comprobamos que la sesión no haya iniciado
            throw new AlreadyStartedFilmException(session.getDateTime());
        }
        if (session.getFreeLocs() < numEntradas){ //comprobamos huecos libres
            throw new SessionFullException(session.getId(), session.getFreeLocs());
        }

        session.setFreeLocs((short) (session.getFreeLocs() - numEntradas));

        Purchase purchase = new Purchase((byte) numEntradas, creditCard, LocalDateTime.now().withSecond(0).withNano(0), false, session, user); //creamos la compra

        purchaseDao.save(purchase);//guardamos session actualizada y la compra

        return purchase.getId(); //devolvemos el Id de la compra
    }

    /**
     *
     * Recupera el historial de compra de un usuario espectador
     * @param userId es el id del ususario de tipo espectador
     * @param page es el numero de pagina que se solicita
     * @param size es el tamaño de pagina que se solicita
     * @return Devuelve el bloque solicitado, con su lista de compras y un boolean que indica si existen mas
     * @throws InstanceNotFoundException Se lanza esta excepcion si el usuario no existe
     */
    @Override
    public Block<Purchase> findAllPurchase(Long userId, int page, int size)
        throws InstanceNotFoundException{

        permissionChecker.checkUserExists(userId);

        Slice<Purchase> slice = purchaseDao.findByUserIdOrderByDateTimeDesc(userId, PageRequest.of(page, size));

        return new Block<>(slice.getContent(), slice.hasNext());
    }

    /**
     * Entrega las entradas de una compra
     * @param id es el id de la compra
     * @param creditCard es la tarjeta que se utilizo para hacer la compra
     * @throws AlreadyTicketsDeliveredException Se lanza esta excepcion si los tickets ya fueron entregados
     * @throws InstanceNotFoundException Se lanza esta excepcion si el id de compra no existe
     * @throws DifferentCreditCardException Se lanza esta excepcion si la tarjeta usada en la compra no es la misma que la pasada en el Dto
     * @throws AlreadyStartedFilmException Se lanza esta excepcion si la sesion/pelicula ya comenzo
     */
    @Override
    public void deliverTickets(Long id, String creditCard)
            throws InstanceNotFoundException, DifferentCreditCardException, AlreadyStartedFilmException, AlreadyTicketsDeliveredException{

        Optional<Purchase> item = purchaseDao.findById(id);

        if (item.isEmpty())
            throw new InstanceNotFoundException("idPurchase",id);//Comprobamos que el id de la compra existe

        Purchase purchase = item.get();

        if (purchase.getSession().getDateTime().isBefore(LocalDateTime.now().withSecond(0).withNano(0)) )
            throw new AlreadyStartedFilmException(purchase.getSession().getDateTime());//Comprobamos que la sessión todavía no comenzó

        if (!purchase.getCreditCard().equals(creditCard))
            throw new DifferentCreditCardException(id, creditCard);//Comprobamos que la tarjeta de crédito es la misma con la que se hizo la compra

        if (purchase.getDelivered())
            throw new AlreadyTicketsDeliveredException();//Comprobamos si las entradas ya fueron entregadas

        purchase.setDelivered(true);
    }

}
