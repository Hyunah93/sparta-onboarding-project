package com.example.delivery.menu.entity;

import com.example.delivery.global.entity.BaseEntity;
import com.example.delivery.menu.dto.request.MenuRequest;
import com.example.delivery.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //메뉴 이름
    @Column(nullable = false)
    private String foodName;

    //메뉴 가격
    @Column(nullable = false)
    private Integer price;

    //메뉴 설명
    private String description;

    //삭제 여부 Soft Delete
    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Menu(String foodName, Integer price, String description, User owner) {
        this.foodName = foodName;
        this.price = price;
        this.description = description;
        this.owner = owner;
    }

    public void updateMenu(MenuRequest request) {
        this.foodName = request.getFoodName();
        this.price = request.getPrice();
        this.description = request.getDescription();
    }

    public void deleteMenu(){
        this.deleted = true;
    }
}
