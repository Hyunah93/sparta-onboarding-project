package com.example.delivery.order.controller;

import com.example.delivery.global.security.UserDetailsImpl;
import com.example.delivery.order.dto.request.OrderRequest;
import com.example.delivery.order.dto.response.OrderResponse;
import com.example.delivery.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문 생성
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request, userDetails.getUser()));
    }

    //주문 조회(목록만)
    @GetMapping("/api/orderlist")
    public ResponseEntity<List<OrderResponse>> orderList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return ResponseEntity.ok(orderService.orderList(userDetails.getUser()));
    }

    //주문 취소(customer)
    @PatchMapping("/api/orderlist/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return ResponseEntity.ok(orderService.cancelOrder(id, userDetails.getUser()));
    }
    
    //주문 상태 변경(owner)
    @PatchMapping("/api/orderlist/{id}/status")
    public ResponseEntity<OrderResponse> statusOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return ResponseEntity.ok(orderService.statusOrder(id, userDetails.getUser()));
    }
}
