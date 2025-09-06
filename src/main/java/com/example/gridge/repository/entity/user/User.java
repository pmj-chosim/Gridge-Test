package com.example.gridge.repository.entity.user;

import com.example.gridge.repository.entity.Post.Comment;
import com.example.gridge.repository.entity.Post.Like;
import com.example.gridge.repository.entity.Post.Post;
import com.example.gridge.repository.entity.Post.Report;
import com.example.gridge.repository.entity.payment.Payment;
import com.example.gridge.repository.entity.payment.Subscription;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;
    private String pwd;
    @Enumerated(EnumType.STRING)
    private VisibleStatus status;
    private String phonenumber;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private Boolean isAdmin;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Payment> payment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Subscription> subscription;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="user", cascade = CascadeType.REMOVE)
    private List<Post> post;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="user", cascade = CascadeType.REMOVE)
    private List<Comment> comment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Like> like;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Report> report;

    public static User create(
            String name, String pwd, String phone_number,
            Boolean isAdmin, LocalDate birthDate, LoginType loginType
    ){
        if (loginType==null) {
            loginType = LoginType.GENERAL;
        }
        return new User(
                null, name, pwd, VisibleStatus.PUBLIC, phone_number,
                LocalDateTime.now(), LocalDateTime.now(), isAdmin, birthDate,
                loginType,
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>()
        );
    }
}
