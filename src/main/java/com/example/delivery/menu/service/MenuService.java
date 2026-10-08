package com.example.delivery.menu.service;

import com.example.delivery.global.exception.ForbiddenException;
import com.example.delivery.global.exception.MenuNotFoundException;
import com.example.delivery.global.security.UserDetailsImpl;
import com.example.delivery.menu.dto.request.MenuRequest;
import com.example.delivery.menu.dto.response.MenuResponse;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    //메뉴 생성
    public MenuResponse createMenu(MenuRequest request, User user) {

        Menu menu = new Menu(
                request.getFoodName(),
                request.getPrice(),
                request.getDescription(),
                user
        );

        Menu saveMenu = menuRepository.save(menu);

        return new MenuResponse(saveMenu);
    }

    //메뉴 목록 조회
    public List<MenuResponse> menuList() {

        return menuRepository.findAll()
                .stream()
                .filter(menu -> !menu.isDeleted())
                .map(MenuResponse::new)
                .toList();
    }

    //메뉴 단건 조회
    public MenuResponse menuOne(Long id) {

        Menu menu = menuRepository.findById(id).orElseThrow(()->
                new MenuNotFoundException("해당 메뉴를 찾을 수 없습니다."));

        if(menu.isDeleted()){
            throw new MenuNotFoundException("해당 메뉴를 찾을 수 없습니다.");
        }

        return new MenuResponse(menu);
    }

    //메뉴 수정
    @Transactional
    public MenuResponse updateMenu(Long id, MenuRequest request, UserDetailsImpl userDetails) {

        Menu menu = menuRepository.findById(id).orElseThrow(()->
                new MenuNotFoundException("해당 메뉴를 찾을 수 없습니다."));

        if(menu.isDeleted()){
            throw new MenuNotFoundException("해당 메뉴를 찾을 수 없습니다.");
        }

        if(!menu.getOwner().getId().equals(userDetails.getUser().getId())){
            throw new ForbiddenException("본인 가게의 메뉴만 수정할 수 있습니다.");

        }

        menu.updateMenu(request);

        return new MenuResponse(menu);
    }

    //메뉴 삭제
    @Transactional
    public void deleteMenu(Long id, UserDetailsImpl userDetails) {

        Menu menu = menuRepository.findById(id)
                .orElseThrow(()->
                        new MenuNotFoundException("해당 메뉴를 찾을 수 없습니다."));

        if(menu.isDeleted()){
            throw new MenuNotFoundException("해당 메뉴를 찾을 수 없습니다.");
        }

        if (!menu.getOwner().getId().equals(userDetails.getUser().getId())){
            throw new ForbiddenException("본인 가게의 메뉴만 삭제할 수 있습니다.");
        }

        menu.deleteMenu();
    }
}
