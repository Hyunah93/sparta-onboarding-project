package com.example.delivery.payment.dto.request;

import com.example.delivery.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private PaymentMethod paymentMethod;
}
