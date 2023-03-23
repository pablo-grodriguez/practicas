package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.BillboardItem;
import es.udc.paproject.backend.model.entities.Film;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.exceptions.AlreadyStartedFilmException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;

import java.util.List;

public interface VisualizeService {
    /*
    * FUNC-1: visualizar la cartelera.
    */
    // FUNC-2: visualizar la información detallada de una película.
    Film visualizeFilm(Long id) throws InstanceNotFoundException; // título, resumen, duración
    /*
    * FUNC-3: visualizar la información detallada de una sesión.
    * */
    List<BillboardItem> findSessionByDay(int dia);
    Session visualizeSession(Long sessionId) throws InstanceNotFoundException, AlreadyStartedFilmException;
}
