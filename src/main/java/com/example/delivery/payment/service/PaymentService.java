package com.example.delivery.payment.service;

import com.example.delivery.global.exception.ForbiddenException;
import com.example.delivery.global.exception.OrderNotFoundException;
import com.example.delivery.global.security.UserDetailsImpl;
import com.example.delivery.order.entity.Order;
import com.example.delivery.order.entity.OrderStatus;
import com.example.delivery.order.repository.OrderRepository;
import com.example.delivery.payment.dto.request.PaymentRequest;
import com.example.delivery.payment.dto.response.PaymentResponse;
import com.example.delivery.payment.entity.Payment;
import com.example.delivery.payment.entity.PaymentStatus;
import com.example.delivery.payment.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse createPayment(
            @Valid @RequestBody PaymentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(()->
                        new OrderNotFoundException("해당 주문을 찾을 수 없습니다."));

        if(!order.getCustomer().getId().equals(userDetails.getUser().getId())){
            throw new ForbiddenException("본인 주문만 결제할 수 있습니다.");
        }

        if(order.getOrderStatus() != OrderStatus.ORDERED){
            throw new IllegalArgumentException("결제할 수 없는 주문 상태입니다. 주문을 확인해주세요");
        }

        Payment payment = new Payment();
        payment.setPaymentAmount(order.getTotalPrice());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setOrder(order);

        paymentRepository.save(payment);

        order.paidOrder();
        return new PaymentResponse(payment);
    }
}
