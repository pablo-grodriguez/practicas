package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.SessionConversor.toSessionDto;
import static es.udc.paproject.backend.rest.dtos.BillboardConversor.toBillboardItemDtos;

import es.udc.paproject.backend.model.entities.Film;
import es.udc.paproject.backend.model.exceptions.AlreadyStartedFilmException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.SessionFullException;
import es.udc.paproject.backend.model.services.VisualizeService;
import es.udc.paproject.backend.rest.common.ErrorsDto;
import es.udc.paproject.backend.rest.dtos.BillboardItemDto;
import es.udc.paproject.backend.rest.dtos.BillboardParamsDto;
import es.udc.paproject.backend.rest.dtos.FilmDto;
import es.udc.paproject.backend.rest.dtos.SessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/visualize")
public class VisualizeController {

    private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
    @Autowired
    private VisualizeService visualizeService;

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(SessionFullException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE, new Object[]{exception.getName(),exception.getKey()},
                INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);
    }

    /**
     * La funcion pertenece a la peticion GET a <a href="http://.../visualize/billboard">...</a>
     * Recupera las sesiones aun no coemnzadas para un dia entre hoy y los siguientes 6 dias
     * @param params es el Dto entrante formado por el dia que se solicita(0 a 6)
     * @return Devuelve la lista de peliculas disponibles ordenadas alfabeticamente con las sesiones disponibles
     */
    @GetMapping("/billboard")
    public List<BillboardItemDto> findSessionByDay(@Validated @RequestBody BillboardParamsDto params){

        return toBillboardItemDtos(visualizeService.findSessionByDay(params.getDay()));
    }

    /**
     * La funcion pertenece a la peticion GET a <a href="http://.../visualize/filmDetails/{filmId}">...</a>
     * Recupera toda la informacion asociada a una pelicula al id
     * @param filmId es el id de la pelicula que se quiere recuperar la informacion
     * @return Devuelve la informacion de la pelicula asociada
     * @throws InstanceNotFoundException Se lanza esta excepcion si la pelicula no existe
     */
    @GetMapping("/filmDetails/{filmId}")
    public FilmDto visualizeFilmDetails(@PathVariable Long filmId) throws InstanceNotFoundException {

        Film film = visualizeService.visualizeFilm(filmId);

        return new FilmDto(film.getId(), film.getTitle(), film.getSummary(), film.getDuration());
    }

    /**
     * La funcion pertenece a la peticion GET a <a href="http://.../visualize/sessionDetails/{sessionId}">...</a>
     * Recupera toda la informacion asociada a una sesion al id
     * @param sessionId es el id de la sesion que se quiere recuperar la informacion
     * @return Devuelve la informacion de la sesion asociada al id
     * @throws InstanceNotFoundException Se lanza esta excepcion si la sesion no existe
     * @throws AlreadyStartedFilmException Se lanza esta excepcion si la sesion es anterior al momento actual
     */
    @GetMapping("/sessionDetails/{sessionId}")
    public SessionDto visualizeSessionDetails(@PathVariable Long sessionId)throws InstanceNotFoundException, AlreadyStartedFilmException {

        return toSessionDto(visualizeService.visualizeSession(sessionId));
    }

}
