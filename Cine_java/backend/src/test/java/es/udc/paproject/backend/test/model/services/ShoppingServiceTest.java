package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.*;
import es.udc.paproject.backend.model.services.ShoppingService;
import es.udc.paproject.backend.model.services.Block;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ShoppingServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;
    private final String VALID_CREDIT_CARD = "1234123412341234";

    @Autowired
    private FilmDao filmDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HallDao hallDao;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private ShoppingService shoppingService;

    private Film createFilm(String name){
        return new Film(name, "Summary", (short) 4);
    }

    private Hall createHall(){
        return new Hall("Hall", (short) 20);
    }

    private Session createSession(LocalDateTime dateTime, Short freeLocs, Hall hall, Film film){
        return new Session(dateTime, 2.75F, freeLocs, hall, film);
    }

    private User createUser(String userName){
        return new User(userName, "password", "Paco", "Mon", "paco@mon.com");
    }

    private Purchase createPurchase(Session session, User user){
        return new Purchase((byte) 5, VALID_CREDIT_CARD, now(), false, session, user);
    }

    private LocalDateTime now(){
        return LocalDateTime.now().withSecond(0).withNano(0);
    }

    @Test
    public void testPurchase() throws SessionFullException, InstanceNotFoundException, AlreadyStartedFilmException {
        //creacion
        User user = createUser("tuPaquitoFav");
        Film film = createFilm("Titanic");
        Hall hall = createHall();
        Session session = createSession(now().plusMinutes(2), (short) 7, hall, film);
        userDao.save(user);
        filmDao.save(film);
        hallDao.save(hall);
        sessionDao.save(session);

        Long userId = user.getId();
        Long sessionId = session.getId();

        //ejecucion
        Long purchaseId = shoppingService.purchase(5, VALID_CREDIT_CARD, userId, sessionId);
        Long purchaseId2 = shoppingService.purchase(2, VALID_CREDIT_CARD, userId, sessionId);

        //assert
        assertTrue(purchaseDao.existsById(purchaseId));
        assertTrue(purchaseDao.existsById(purchaseId2));

        assertThrows(SessionFullException.class, () ->
                shoppingService.purchase(5, VALID_CREDIT_CARD, userId, sessionId));
    }

    @Test
    public void testPurchaseSessionFull() {
        //creacion
        User user = createUser("tuPaquitoFav");
        Film film = createFilm("Titanic");
        Hall hall = createHall();
        Session session = createSession(now().plusMinutes(2), (short) 0, hall, film);
        userDao.save(user);
        filmDao.save(film);
        hallDao.save(hall);
        sessionDao.save(session);

        Long userId = user.getId();
        Long sessionId = session.getId();

        //ejecucion
        assertThrows(SessionFullException.class, () ->
                shoppingService.purchase(5, VALID_CREDIT_CARD, userId, sessionId));
    }

    @Test
    public void testPurchaseSessionAlreadyStarted() {
        //creacion
        User user = createUser("tuPaquitoFav");
        Film film = createFilm("Titanic");
        Hall hall = createHall();
        Session session = createSession(now().minusMinutes(2), (short) 100, hall, film);
        userDao.save(user);
        filmDao.save(film);
        hallDao.save(hall);
        sessionDao.save(session);

        Long userId = user.getId();
        Long sessionId = session.getId();

        //ejecucion
        assertThrows(AlreadyStartedFilmException.class, () ->
                shoppingService.purchase(5, VALID_CREDIT_CARD, userId, sessionId));
    }

    @Test
    public void testPurchaseSessionNotFound() {
        //creacion
        User user = createUser("tuPaquitoFav");
        userDao.save(user);

        //ejecucion
        assertThrows(InstanceNotFoundException.class, () ->
                shoppingService.purchase(5, VALID_CREDIT_CARD, user.getId(), NON_EXISTENT_ID));
    }

    @Test
    public void testPurchaseUserNotFound() {
        //creacion
        Film film = createFilm("Titanic");
        Hall hall = createHall();
        Session session = createSession(now().plusMinutes(2), (short) 5, hall, film);
        filmDao.save(film);
        hallDao.save(hall);
        sessionDao.save(session);

        Long sessionId = session.getId();

        //ejecucion
        assertThrows(InstanceNotFoundException.class, () ->  // sin user
                shoppingService.purchase(5, "1234", NON_EXISTENT_ID, sessionId));
    }

    @Test
    public void testFindAllPurchase() throws InstanceNotFoundException {
        Film film1 = createFilm("A");
        Film film2 = createFilm("B");
        Hall hall = createHall();
        User user1 = createUser("User1");
        User user2 = createUser("Use2");
        Session session1 = createSession(now(), (short) 20, hall, film1);
        Session session2 = createSession(now(), (short) 20, hall, film2);
        Purchase purchase1 = createPurchase(session1, user1);
        Purchase purchase2 = createPurchase(session2, user1);
        Purchase purchase3 = createPurchase(session1, user2);
        purchase2.setDateTime(purchase2.getDateTime().minusHours(2));

        filmDao.save(film1);
        filmDao.save(film2);
        hallDao.save(hall);
        userDao.save(user1);
        userDao.save(user2);
        sessionDao.save(session2);
        sessionDao.save(session1);
        purchaseDao.save(purchase3);
        purchaseDao.save(purchase2);
        purchaseDao.save(purchase1);

        Block<Purchase> expectedBlock = new Block<>(Arrays.asList(purchase1, purchase2), false);
        assertEquals(expectedBlock, shoppingService.findAllPurchase(user1.getId(), 0, 2));
    }

    @Test
    public void testFindAllPurchaseFromNonExistentId(){
        assertThrows(InstanceNotFoundException.class, () ->
                shoppingService.findAllPurchase(NON_EXISTENT_ID, 0, 2));
    }

    @Test
    public void testDeliveredTickets() throws InstanceNotFoundException, AlreadyStartedFilmException, AlreadyTicketsDeliveredException, DifferentCreditCardException {
        Film film = createFilm("A");

        Hall hall = createHall();

        User user = createUser("User");

        Session session = createSession(now().plusHours(1), (short) 20, hall, film);

        Purchase purchase = createPurchase(session, user);


        filmDao.save(film);

        hallDao.save(hall);

        userDao.save(user);

        sessionDao.save(session);

        purchaseDao.save(purchase);

        shoppingService.deliverTickets(purchase.getId(), purchase.getCreditCard());

        assertTrue(purchase.getDelivered());
    }

    @Test
    public void testDeliveredTicketsFromNonExistentId(){
        assertThrows(InstanceNotFoundException.class, () ->
                shoppingService.deliverTickets(NON_EXISTENT_ID, VALID_CREDIT_CARD));
    }

    @Test
    public void testDeliveredTicketsFromDifferentCreditCard(){//Pista antes tiene que pasar la seccion de que existe la compra
        //creacion
        Film film = createFilm("A");

        Hall hall = createHall();

        User user = createUser("User");

        Session session = createSession(now().plusHours(1), (short) 20, hall, film);

        Purchase purchase = createPurchase(session, user);


        filmDao.save(film);

        hallDao.save(hall);

        userDao.save(user);

        sessionDao.save(session);

        purchaseDao.save(purchase);

        //ejecucion
        assertThrows(DifferentCreditCardException.class, () ->
                shoppingService.deliverTickets(purchase.getId(), "12120012001200"));
    }

    @Test
    public void testDeliveredTicketsWithAlredyStartedFilm(){//Pista antes tiene que pasar la seccion de que existe la compra y las anteriores
        //creacion
        Film film = createFilm("A");

        Hall hall = createHall();

        User user = createUser("User");

        Session session = createSession(now().minusHours(1), (short) 20, hall, film);

        Purchase purchase = createPurchase(session, user);

        filmDao.save(film);

        hallDao.save(hall);

        userDao.save(user);

        sessionDao.save(session);

        purchaseDao.save(purchase);
        //ejecucion
        assertThrows(AlreadyStartedFilmException.class, () ->
                shoppingService.deliverTickets(purchase.getId(), purchase.getCreditCard()));
    }

    @Test
    public void testDeliveredTicketsWithAlreadyDeliveredTickets(){//Pista antes tiene que pasar la seccion de que existe la compray las anteriores y las anteriores de las anteriores
        //creacion
        Film film = createFilm("A");

        Hall hall = createHall();

        User user = createUser("User");

        Session session = createSession(now().plusHours(1), (short) 20, hall, film);

        Purchase purchase = createPurchase(session, user);
        purchase.setDelivered(true);

        filmDao.save(film);

        hallDao.save(hall);

        userDao.save(user);

        sessionDao.save(session);

        purchaseDao.save(purchase);
        //ejecucion
        assertThrows(AlreadyTicketsDeliveredException.class, () ->
                shoppingService.deliverTickets(purchase.getId(), purchase.getCreditCard()));
    }
}