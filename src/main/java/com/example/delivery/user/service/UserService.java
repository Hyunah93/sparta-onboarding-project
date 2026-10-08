package com.example.delivery.user.service;

import com.example.delivery.user.dto.request.SignupRequest;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void signup(SignupRequest request){
        String userId = request.getUserId();
        String password = passwordEncoder.encode(request.getPassword());

        //회원 중복 확인
        Optional<User> checkUserId = userRepository.findByUserId(userId);
        if (checkUserId.isPresent()){
            throw new IllegalArgumentException("중복된 아이디가 존재합니다. 아이디를 확인해주세요.");
        }

        //role 확인
        UserRoleEnum role = request.getRole();

        //회원 등록
        User user = new User(userId, password, role);
        userRepository.save(user);
    }
}
