package es.unir.dwfs.payments.service;

import es.unir.dwfs.payments.controller.model.OrderItemRequest;
import es.unir.dwfs.payments.controller.model.OrderRequest;
import es.unir.dwfs.payments.data.OrderJpaRepository;
import es.unir.dwfs.payments.data.model.Order;
import es.unir.dwfs.payments.data.model.OrderItem;
import es.unir.dwfs.payments.facade.BooksCatalogueFacade;
import es.unir.dwfs.payments.facade.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import es.unir.dwfs.payments.exception.BusinessRuleViolationException;
import es.unir.dwfs.payments.exception.ConverterErrors;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de pedidos
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final BooksCatalogueFacade booksCatalogueFacade;
    private final OrderJpaRepository repository;
    private final ConverterErrors converterErrors;

    @Override
    public Order createOrder(OrderRequest request) {

        log.info("Creating order with {} items", request.getItems().size());

        // 1. Validar y construir OrderItems
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            // Obtener libro del catálogo
            Book book = booksCatalogueFacade.getBook(itemRequest.getBookId().toString());

            // Validar que existe
            if (book == null) {
                log.error("Book with ID {} not found", itemRequest.getBookId());
                throw new BusinessRuleViolationException(
                        "Libro no encontrado",
                        "ORDER_BUSINESS-001",
                        converterErrors.getMessage("ORDER_BUSINESS-001", itemRequest.getBookId()));
            }

            // Validar que es visible
            if (!book.getVisible()) {
                log.error("Book '{}' is not visible", book.getTitle());
                throw new BusinessRuleViolationException(
                        "Libro no disponible",
                        "ORDER_BUSINESS-002",
                        converterErrors.getMessage("ORDER_BUSINESS-002", itemRequest.getBookId()));
            }

            // Crear OrderItem con precio capturado del catálogo
            OrderItem item = OrderItem.builder()
                    .bookId(itemRequest.getBookId())
                    .quantity(itemRequest.getQuantity())
                    .capturedUnitPrice(book.getPrice())
                    .build();
            orderItems.add(item);

            log.info("Added item: Book '{}' (ID: {}), quantity: {}, price: {}",
                    book.getTitle(), book.getId(), itemRequest.getQuantity(), book.getPrice());
        }

        // 2. Persistir order
        Order order = Order.builder()
                .items(orderItems)
                .orderDate(Instant.now()) // Timestamp en UTC
                .build();

        Order savedOrder = repository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());

        return savedOrder;
    }

    @Override
    public Order getOrder(String id) {
        return repository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public List<Order> getOrders() {
        List<Order> orders = repository.findAll();
        return orders.isEmpty() ? null : orders;
    }
}
