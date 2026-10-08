package com.example.delivery.menu.dto.response;

import com.example.delivery.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponse {

    private Long id;
    private String foodName;
    private Integer price;
    private String description;

    public MenuResponse(Menu menu){

        this.id = menu.getId();
        this.foodName = menu.getFoodName();
        this.price = menu.getPrice();
        this.description = menu.getDescription();

    }
}
