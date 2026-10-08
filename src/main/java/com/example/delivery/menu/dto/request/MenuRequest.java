package com.example.delivery.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequest {

    @NotBlank
    private String foodName;

    @NotNull
    @Min(1)
    private Integer price;

    private String description;
}
