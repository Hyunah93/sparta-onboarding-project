package com.example.delivery.order.service;

import com.example.delivery.global.exception.ForbiddenException;
import com.example.delivery.global.exception.MenuNotFoundException;
import com.example.delivery.global.exception.OrderNotFoundException;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.order.dto.request.OrderRequest;
import com.example.delivery.order.dto.response.OrderResponse;
import com.example.delivery.order.entity.Order;
import com.example.delivery.order.entity.OrderStatus;
import com.example.delivery.order.repository.OrderRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    public OrderResponse createOrder(OrderRequest request, User user  ){ // 주문자를 토큰으로 받기
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(()->
                        new MenuNotFoundException("해당 메뉴를 찾을 수 없습니다."));

        if(menu.isDeleted()){
            throw new MenuNotFoundException("해당 메뉴를 찾을 수 없습니다.");
        }

        Integer totalPrice = menu.getPrice() * request.getQuantity();

        Order order = new Order(
                request.getQuantity(),
                totalPrice,
                request.getAddress(),
                user,
                menu
                );

        Order saveOrder = orderRepository.save(order);
        return new OrderResponse(saveOrder);
    }

    public List<OrderResponse> orderList(User user) {

        List<Order> orders;

        if(user.getRole() == UserRoleEnum.CUSTOMER){
            orders = orderRepository.findAllByCustomer(user);
        }else if (user.getRole() == UserRoleEnum.OWNER){
            orders = orderRepository.findAllByMenuOwner(user);
        }else {
            throw new IllegalArgumentException("잘못된 사용자의 접근입니다.");
        }

        return orders
                .stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Transactional
    public OrderResponse cancelOrder(Long id, User user) {

        Order order = orderRepository.findById(id).orElseThrow(()->
                new OrderNotFoundException("해당 주문을 찾을 수 없습니다."));

        if(!order.getCustomer().getId().equals(user.getId())){
            throw new ForbiddenException("본인의 주문만 취소할 수 있습니다.");
        }

        if (order.getOrderStatus() != OrderStatus.ORDERED){
            throw new IllegalArgumentException("주문요청 상태의 주문만 취소할 수 있습니다.");
        }

        order.cancelOrder();
        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse statusOrder(Long id, User user) {

        Order order = orderRepository.findById(id).orElseThrow(()->
                new OrderNotFoundException("해당 주문을 찾을 수 없습니다."));

        if(!order.getMenu().getOwner().getId().equals(user.getId())){
            throw new ForbiddenException("본인 가게의 주문만 변경할 수 있습니다.");
        }

        if(order.getOrderStatus() == OrderStatus.PAID){
            order.acceptOrder();
        } else if (order.getOrderStatus() == OrderStatus.ACCEPTED) {
            order.completeOrder();
        } else {
            throw new IllegalArgumentException("변경할 수 없는 주문 상태입니다.");
        }

        return new OrderResponse(order);
    }
}
