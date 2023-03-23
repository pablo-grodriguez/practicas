package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface PurchaseDao extends PagingAndSortingRepository<Purchase, Long> {

    Slice<Purchase> findByUserIdOrderByDateTimeDesc(Long userId, Pageable pageable);
}
