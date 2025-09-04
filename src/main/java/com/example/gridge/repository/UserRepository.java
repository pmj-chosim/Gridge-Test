package com.example.gridge.repository;

import com.example.gridge.repository.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findById(Integer id);

    Optional<User> findByName(String name);

    Page<User> findAll(Pageable pageable);


}
