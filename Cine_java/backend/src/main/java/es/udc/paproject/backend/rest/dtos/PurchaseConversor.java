package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.Purchase;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseConversor {

    private PurchaseConversor() {
    }

    public final static List<PurchaseDto> toPurchaseDtos(List<Purchase> purchases) {
        return purchases.stream().map(p -> toPurchaseDto(p)).collect(Collectors.toList());
    }

    private final static PurchaseDto toPurchaseDto(Purchase purchase) {
        return new PurchaseDto(purchase.getId(), toMillis(purchase.getDateTime()), purchase.getSession().getFilm().getTitle(), purchase.getAmount(), purchase.getTotalPrice(), toMillis(purchase.getSession().getDateTime()), purchase.getDelivered());
    }

    private final static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
