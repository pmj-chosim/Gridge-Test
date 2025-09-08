package com.example.gridge.repository;

import com.example.gridge.repository.entity.Post.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    boolean existsByUserIdAndPostId(Integer id, Integer postId);

    Optional<Like> findByUserIdAndPostId(Integer id, Integer postId);
}
