package com.example.delivery.order.entity;

public enum OrderStatus {

    ORDERED,    //주문요청 ORDERED
    PAID,       //결제완료 PAID
    ACCEPTED,   //주문수락 ACCEPTED
    COMPLETED,  //배달완료 COMPLETED
    CANCELED    //주문취소 CANCELED

}
