package com.example.delivery.order.entity;

import com.example.delivery.global.entity.BaseEntity;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //수량
    @Column(nullable = false)
    private Integer quantity;

    //총액
    @Column(nullable = false)
    private Integer totalPrice;

    //배송 주소
    @Column(nullable = false)
    private String address;

    //주문 상태
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    public Order(Integer quantity, Integer totalPrice, String address, User customer, Menu menu) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.address = address;
        this.customer = customer;
        this.menu = menu;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCELED;
    }

    public void acceptOrder(){
        this.orderStatus = OrderStatus.ACCEPTED;
    }

    public void completeOrder(){
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public void paidOrder(){this.orderStatus = OrderStatus.PAID;}

}
