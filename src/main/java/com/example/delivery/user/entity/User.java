package com.example.delivery.user.entity;

import com.example.delivery.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //아이디 중복 불가
    @Column(nullable = false, unique = true)
    private String userId;

    //비밀번호(BCrypt 암호화 · 단방향 해시)
    @Column(nullable = false)
    private String password;

    // 역할
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(String userId, String password, UserRoleEnum role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }
}
