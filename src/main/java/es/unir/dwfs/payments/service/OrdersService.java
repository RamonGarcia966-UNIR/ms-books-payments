package es.unir.dwfs.payments.service;

import es.unir.dwfs.payments.controller.model.OrderRequest;
import es.unir.dwfs.payments.data.model.Order;

import java.util.List;

/**
 * Interfaz del servicio de pedidos
 */
public interface OrdersService {

    Order createOrder(OrderRequest request);

    Order getOrder(String id);

    List<Order> getOrders();
}
