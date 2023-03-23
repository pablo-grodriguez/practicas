package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.AlreadyStartedFilmException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VisualizeServiceImpl implements VisualizeService{

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private FilmDao filmDao;

    /**
     * Recupera la informacion de una pelicula
     * @param id es el id de la pelicula que se quiere recuperar la informacion
     * @return Devuelve la informacion de la pelicula asociada
     * @throws InstanceNotFoundException Se lanza esta excepcion si la pelicula no existe
     */
    @Override
    public Film visualizeFilm(Long id) throws InstanceNotFoundException{
        Optional<Film> optionalFilm = filmDao.findById(id);
        if (optionalFilm.isEmpty()) {
            throw new InstanceNotFoundException("Film",id);
        }
        return optionalFilm.get();
    }

    /**
     * Recupera las sesiones aun no coemnzadas para un dia entre hoy y los siguientes 6 dias
     * @param dia es el dia que se solicita(0 a 6)
     * @Devuelve la lista de peliculas disponibles ordenadas alfabeticamente con las sesiones disponibles
     */
    @Override
    public List<BillboardItem> findSessionByDay(int dia){

        List<BillboardItem> filmList = new ArrayList<>();
        int nFilm=0;
        List<Session> x = sessionDao.findByDateTimeBetweenOrderByFilmTitleAscDateTimeAsc(
                (dia == 0) ? LocalDateTime.now().withSecond(0).withNano(0) : LocalDateTime.now().plusDays(dia).withHour(0).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().plusDays(dia).withHour(23).withMinute(59).withSecond(0).withNano(0));
        for(Session session : x){
            if(filmList.isEmpty())
                filmList.add(new BillboardItem(session.getFilm(), new ArrayList<>()));
            if (!session.getFilm().equals(filmList.get(nFilm).getFilm())){
                nFilm++;
                filmList.add(new BillboardItem(session.getFilm(), new ArrayList<>()));
            }
            filmList.get(nFilm).addSession(session);
        }
        return filmList;
    }

    /**
     * Recupera toda la informacion asociada a una sesion al id
     * @param sessionId es el id de la sesion que se quiere recuperar la informacion
     * @return Devuelve la informacion de la sesion asociada al id
     * @throws InstanceNotFoundException Se lanza esta excepcion si la sesion no existe
     * @throws AlreadyStartedFilmException Se lanza esta excepcion si la sesion es anterior al momento actual
     */
    @Override
    public Session visualizeSession(Long sessionId) throws InstanceNotFoundException, AlreadyStartedFilmException{
        Optional<Session> item = sessionDao.findById(sessionId);
        if (item.isEmpty()){
            throw new InstanceNotFoundException("sessionId",sessionId);
        }
        if (item.get().getDateTime().isBefore(LocalDateTime.now().withSecond(0).withNano(0)) )
            throw new AlreadyStartedFilmException(item.get().getDateTime());//Comprobamos que la sessión todavía no comenzó

        return item.get();
    }
}