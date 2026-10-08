package com.example.delivery.menu.controller;

import com.example.delivery.global.security.UserDetailsImpl;
import com.example.delivery.menu.dto.request.MenuRequest;
import com.example.delivery.menu.dto.response.MenuResponse;
import com.example.delivery.menu.service.MenuService;
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
public class MenuController {

    private final MenuService menuService;

    //메뉴 등록
    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> createMenu(
            @Valid @RequestBody MenuRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuService.createMenu(request, userDetails.getUser()));
    }

    //메뉴 목록 조회
    @GetMapping("/api/menulist")
    public ResponseEntity<List<MenuResponse>> menuList(
    ){
        return ResponseEntity.ok(menuService.menuList());
    }

    //메뉴 단건 조회
    @GetMapping("/api/menulist/{id}")
    public MenuResponse menuOne(
            @PathVariable Long id
    ){
        return menuService.menuOne(id);
    }

    //메뉴 수정
    @PutMapping("/api/menus/{id}")
    public MenuResponse updateMenu(
            @PathVariable Long id,
            @Valid @RequestBody MenuRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return menuService.updateMenu(id, request, userDetails);
    }

    //메뉴 삭제
    @DeleteMapping("/api/menus/{id}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        menuService.deleteMenu(id, userDetails);

        return ResponseEntity.noContent().build();
    }
}
