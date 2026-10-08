package com.example.delivery.order.dto.response;

import com.example.delivery.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderResponse {

    private Long id;
    private Long menuId;
    private Integer quantity;
    private Integer totalPrice;
    private String address;
    private String orderStatus;

    public OrderResponse(Order saveOrder) {
        this.id = saveOrder.getId();
        this.menuId = saveOrder.getMenu().getId();
        this.quantity = saveOrder.getQuantity();
        this.totalPrice = saveOrder.getTotalPrice();
        this.address = saveOrder.getAddress();
        this.orderStatus = saveOrder.getOrderStatus().name();
    }
}
