package com.gestion.tourisme_app.dto;

import com.gestion.tourisme_app.entity.Order;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String orderNumber;
    private double totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private Order.OrderStatus orderStatus;
    private Order.PaymentStatus paymentStatus;
    private String notes;
    private LocalDateTime createdDate;
    private List<OrderItemDto> orderItems;
}
