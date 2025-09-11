package com.example.gridge.repository.entity.user;

import com.example.gridge.repository.entity.Post.*;
import com.example.gridge.repository.entity.Status;
import com.example.gridge.repository.entity.payment.Payment;
import com.example.gridge.repository.entity.payment.Subscription;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails { // UserDetails 인터페이스 구현

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;
    private String pwd;
    @Enumerated(EnumType.STRING)
    private VisibleStatus status;
    private String phonenumber;
    private Timestamp createdAt;
    private Timestamp lastLoginAt;
    private Boolean isAdmin;
    private LocalDate birthDate;
    private LocalDateTime lastConsentDate;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private ActiveLevel activeLevel;

    private String socialId;

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

    @Enumerated(EnumType.STRING)
    private Status originStatus;

    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    public final static SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
    public final static SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    public static User create(
            String name, String pwd, String phone_number,
            Boolean isAdmin, LocalDate birthDate, LoginType loginType, String socialId
    ){
        if (loginType==null) {
            loginType = LoginType.GENERAL;
        }

        User user = new User(
                null, name, pwd, VisibleStatus.PUBLIC, phone_number,
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                isAdmin, birthDate, LocalDateTime.now(),
                loginType, ActiveLevel.ACTIVE,socialId,
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                Status.ACTIVE,
                new ArrayList<>() // authorities 필드 초기화,
        );
        // 권한 설정 로직은 create 메서드에서 별도로 처리합니다.
        user.setRoles(isAdmin);
        return user;
    }

    public void setRoles(Boolean isAdmin) {
        if (isAdmin) {
            this.authorities.add(ROLE_USER);
            this.authorities.add(ROLE_ADMIN);
        } else {
            this.authorities.add(ROLE_USER);
        }
    }

    // UserDetails 인터페이스 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 추가: 비밀번호 재설정 메서드
    public void resetPassword(String newEncodedPassword) {
        this.pwd = newEncodedPassword;
    }

    public void updateConsentDate() {
        this.lastConsentDate = LocalDateTime.now();
    }
}