package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.PurchaseConversor.toPurchaseDtos;

import es.udc.paproject.backend.model.entities.Purchase;
import es.udc.paproject.backend.model.exceptions.*;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.ShoppingService;
import es.udc.paproject.backend.rest.common.ErrorsDto;
import es.udc.paproject.backend.rest.dtos.BlockDto;
import es.udc.paproject.backend.rest.dtos.DeliverTickedParamsDto;
import es.udc.paproject.backend.rest.dtos.PurchaseDto;
import es.udc.paproject.backend.rest.dtos.PurchaseParamsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;



@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    private final static String SESSION_FULL_EXCEPTION_CODE = "project.exceptions.SessionFullException";
    private final static String ALREADY_STARTED_FILM_EXCEPTION_CODE = "project.exceptions.AlreadyStartedFilmException";
    private final static String DIFFERENT_CREDIT_CARD_EXCEPTION_CODE  = "project.exceptions.DifferentCreditCardException";
    private final static String ALREADY_TICKETS_DELIVERED_EXCEPTION_CODE = "project.exceptions.AlreadyTicketsDeliveredException";
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ShoppingService shoppingService;

    @ExceptionHandler(DifferentCreditCardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDto handleDiferentCreditCardException(DifferentCreditCardException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(DIFFERENT_CREDIT_CARD_EXCEPTION_CODE, new Object[]{exception.getUserName()},
                DIFFERENT_CREDIT_CARD_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);
    }

    @ExceptionHandler(SessionFullException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorsDto handleSessionFullException(SessionFullException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(SESSION_FULL_EXCEPTION_CODE, new Object[]{exception.getSessionId(),exception.getFreeSeats()},
                SESSION_FULL_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);
    }

    @ExceptionHandler(AlreadyStartedFilmException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorsDto handleAlreadyStartedFilmException(AlreadyStartedFilmException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(ALREADY_STARTED_FILM_EXCEPTION_CODE, new Object[]{exception.getLocalDateTime()},
                ALREADY_STARTED_FILM_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);
    }

    @ExceptionHandler(AlreadyTicketsDeliveredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorsDto handleAlreadyTicketsDeliveredException(AlreadyTicketsDeliveredException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(ALREADY_TICKETS_DELIVERED_EXCEPTION_CODE, null,
                ALREADY_TICKETS_DELIVERED_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);
    }

    /**
     * La funcion pertenece a la peticion POST a <a href="http://.../shopping/purchase">...</a>
     * Realiza la compra de una cantidad de entradas para una sesion
     * @param userId es el id del usuario de tipo espectador que realiza la compra
     * @param purchaseParamsDto es el Dto entrante formado por la cantidad de entradas, tarjeta de credito y sesion de la compra que se intenta realizar.
     * @return Devuelve el id de la compra realizada con exito
     * @throws InstanceNotFoundException Se lanza esta excepcion si la sesion no existe
     * @throws SessionFullException Se lanza esta excepcion si la sesion no tiene suficientes asientos libre para satisfacer la compra solicitada
     * @throws AlreadyStartedFilmException Se lanza esta excepcion si la sesion ya comenzo
     */
    @PostMapping("/purchase")
    public Long purchase(@RequestAttribute Long userId, @Validated @RequestBody PurchaseParamsDto purchaseParamsDto)
            throws InstanceNotFoundException, SessionFullException, AlreadyStartedFilmException {

        return shoppingService.purchase(purchaseParamsDto.getAmount(), purchaseParamsDto.getCreditCard(), userId, purchaseParamsDto.getSessionId());
    }

    /**
     * La funcion pertenece a la peticion GET a <a href="http://.../shopping/purchaseHistory">...</a>
     * Recupera el historial de compra de un usuario espectador
     * @param userId es el id del ususario de tipo espectador
     * @param page es el numero de pagina que se solicita, en caso de no pasarse se recuperara la primera pagina
     * @return Devuelve el bloque solicitado, con su lista de compras y un boolean que indica si existen mas
     * @throws InstanceNotFoundException Se lanza esta excepcion si el usuario no existe
     */
    @GetMapping("/purchaseHistory")
    public BlockDto<PurchaseDto> findAllPurchase(@RequestAttribute Long userId,
                                                 @RequestParam(defaultValue="0") int page) throws InstanceNotFoundException{

        Block<Purchase> purchaseBlock = shoppingService.findAllPurchase(userId, page, 2);

        return new BlockDto<>(toPurchaseDtos(purchaseBlock.getItems()), purchaseBlock.getExistMoreItems());
    }

    /**
     * La funcion pertenece a la peticion POST a <a href="http://.../shopping/deliverTickets">...</a>
     * Entrega las entradas de una comora
     * @param deliverTickedParamsDto es el Dto entrante formado por el id de la compra y la tarjeta que se utilizo para hacer la compra
     * @throws AlreadyTicketsDeliveredException Se lanza esta excepcion si los tickets ya fueron entregados
     * @throws InstanceNotFoundException Se lanza esta excepcion si el id de compra no existe
     * @throws DifferentCreditCardException Se lanza esta excepcion si la tarjeta usada en la compra no es la misma que la pasada en el Dto
     * @throws AlreadyStartedFilmException Se lanza esta excepcion si la sesion/pelicula ya comenzo
     */
    @PostMapping("/deliverTickets")
    public void deliverTickets(@Validated @RequestBody DeliverTickedParamsDto deliverTickedParamsDto)
            throws AlreadyTicketsDeliveredException, InstanceNotFoundException, DifferentCreditCardException, AlreadyStartedFilmException {
        shoppingService.deliverTickets(deliverTickedParamsDto.getIdPurchase(),deliverTickedParamsDto.getCreditCard());
    }

}
