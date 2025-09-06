package com.example.gridge.repository;

import com.example.gridge.repository.entity.Post.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {

}
