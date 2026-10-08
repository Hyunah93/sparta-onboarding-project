package com.example.delivery.order.repository;

import com.example.delivery.order.entity.Order;
import com.example.delivery.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomer(User customer);

    // Order에 owner 있는게 아니라서 Menu 통해서 가져옴
    List<Order> findAllByMenuOwner(User owner);

}
