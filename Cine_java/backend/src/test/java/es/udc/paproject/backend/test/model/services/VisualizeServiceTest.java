package es.udc.paproject.backend.test.model.services;


import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.AlreadyStartedFilmException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.services.VisualizeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class VisualizeServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private FilmDao filmDao;

    @Autowired
    private HallDao hallDao;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private VisualizeService visualizeService;

    private Film createFilm(String name){
        return new Film(name, "Summary", (short) 4);
    }

    private Hall createHall(){
        return new Hall("Hall", (short) 20);
    }

    private Session createSession(LocalDateTime dateTime, Hall hall, Film film){
        return new Session(dateTime, 2.75F, (short) 20, hall, film);
    }

    private LocalDateTime now(){
        return LocalDateTime.now().withSecond(0).withNano(0);
    }

    private LocalDateTime nowSpe(int hour, int minute){
        return now().withHour(hour).withMinute(minute);
    }

    private LocalDateTime tomorrow(int hour, int minute){
        return nowSpe(hour, minute).plusDays(1);
    }

    @Test
    public void testFindSessionByDateOfToday() { //Falla, falta corregir la funcion para eliminar
        Film film1 = createFilm("A");
        Film film2 = createFilm("B");
        Hall hall = createHall();
        Session session1 = createSession(now(), hall, film1);
        Session session2 = createSession(now().plusHours(2), hall, film1);
        Session session3 = createSession(now(), hall, film2);
        Session session4 = createSession(now().minusHours(2), hall, film2); //Esta no se ver

        filmDao.save(film1);
        filmDao.save(film2);
        hallDao.save(hall);
        sessionDao.save(session4);
        sessionDao.save(session3);
        sessionDao.save(session2);
        sessionDao.save(session1);

        List<BillboardItem> expectedResult = null;

        if(now().isBefore(tomorrow(0,0)))
            expectedResult = Arrays.asList(new BillboardItem(film1, Arrays.asList(session1)), new BillboardItem(film2, Arrays.asList(session3)));
        if(now().isBefore(nowSpe(22,0)))
            expectedResult = Arrays.asList(new BillboardItem(film1, Arrays.asList(session1, session2)), new BillboardItem(film2, Arrays.asList(session3)));

        assertEquals(expectedResult, visualizeService.findSessionByDay(0));
    }

    @Test
    public void testFindSessionByDateOfTomorrow() {
        Film film1 = createFilm("A");
        Film film2 = createFilm("B");
        Hall hall = createHall();
        Session session1 = createSession(tomorrow(12, 0), hall, film1);
        Session session2 = createSession(now(), hall, film1); //Esta no se ver
        Session session3 = createSession(tomorrow(12, 0), hall, film2);
        Session session4 = createSession(tomorrow(14, 0), hall, film2);

        filmDao.save(film1);
        filmDao.save(film2);
        hallDao.save(hall);
        sessionDao.save(session4);
        sessionDao.save(session3);
        sessionDao.save(session2);
        sessionDao.save(session1);

        List<BillboardItem> expectedResult = Arrays.asList(new BillboardItem(film1, Arrays.asList(session1)), new BillboardItem(film2, Arrays.asList(session3, session4)));

        assertEquals(expectedResult, visualizeService.findSessionByDay(1));
    }

    @Test
    public void testVisualizeFilm() throws InstanceNotFoundException {
        Film film = createFilm("avatar");
        film.setDuration((short) 180);
        film.setSummary("Alienigenas nadan y hacen cosas");
        Film film2 = createFilm("Los 3 cerditos");
        film.setDuration((short) 25);
        film.setSummary("El lobo sopla");

        filmDao.save(film);
        filmDao.save(film2);

        assertNotEquals(visualizeService.visualizeFilm(film2.getId()), visualizeService.visualizeFilm(film.getId()));
        assertEquals(film2, visualizeService.visualizeFilm(film2.getId()));
        assertEquals(film, visualizeService.visualizeFilm(film.getId()));
    }

    @Test
    public void testVisualizeNonExistingFilm() {
        assertThrows(InstanceNotFoundException.class, () -> visualizeService.visualizeFilm(NON_EXISTENT_ID));
    }
    @Test
    public void testVisualizeSession()throws InstanceNotFoundException, AlreadyStartedFilmException {
        Hall hall=createHall();
        Film film=createFilm("Film");
        Session session = createSession(now(),hall,film);

        filmDao.save(film);
        hallDao.save(hall);
        sessionDao.save(session);

        assertEquals(session,visualizeService.visualizeSession(session.getId()));
    }
    @Test
    public void testVisualzeSessionFromNonExistingSession(){
        assertThrows(InstanceNotFoundException.class,() -> visualizeService.visualizeSession(NON_EXISTENT_ID));
    }
    @Test
    public void testVisualizeSessionWithAlredyStartedFilm(){
        Hall hall=createHall();
        Film film=createFilm("Film");
        Session session = createSession(now().minusHours(1),hall,film);

        filmDao.save(film);
        hallDao.save(hall);
        sessionDao.save(session);

        assertThrows(AlreadyStartedFilmException.class, () -> visualizeService.visualizeSession(session.getId()));

    }

}
