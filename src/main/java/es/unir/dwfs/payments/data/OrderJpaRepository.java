package es.unir.dwfs.payments.data;

import es.unir.dwfs.payments.data.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para pedidos
 */
public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
