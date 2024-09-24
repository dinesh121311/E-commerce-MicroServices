package com.example.microservices.order.service;

import com.example.microservices.order.client.InventoryClient;
import com.example.microservices.order.dto.OrderRequest;
import com.example.microservices.order.event.OrderPlacedEvent;
import com.example.microservices.order.model.Order;
import com.example.microservices.order.repositroy.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isProductInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            // Calculate total price by multiplying price with quantity
            order.setPrice(orderRequest.price().multiply(BigDecimal.valueOf(orderRequest.quantity())));
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            var userDetails = orderRequest.userDetails();
            if (userDetails != null) {
                // Defaulting to empty strings if firstName or lastName is null
                String firstName = userDetails.firstName() != null ? userDetails.firstName() : "Unknown";
                String lastName = userDetails.lastName() != null ? userDetails.lastName() : "Unknown";

                var orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(),
                        userDetails.email(),
                        firstName,
                        lastName);
                log.info("Start - Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);
                kafkaTemplate.send("order-placed", orderPlacedEvent);
                log.info("End - Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);
            } else {
                log.error("UserDetails are missing in OrderRequest.");
                throw new RuntimeException("UserDetails are required to place an order.");
            }
        } else {
            throw new RuntimeException("Product with SkuCode " + orderRequest.skuCode() + " is not in stock");
        }
    }
}
