package com.example.delivery.payment.dto.response;

import com.example.delivery.payment.entity.Payment;
import com.example.delivery.payment.entity.PaymentMethod;
import com.example.delivery.payment.entity.PaymentStatus;
import lombok.Getter;

@Getter
public class PaymentResponse {

    private Long id;
    private Integer paymentAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Long orderId;

    public PaymentResponse(Payment payment){
        this.id = payment.getId();
        this.paymentAmount = payment.getPaymentAmount();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentStatus = payment.getPaymentStatus();
        this.orderId = payment.getOrder().getId();
    }
}
