package com.example.gridge.repository;

import com.example.gridge.repository.entity.Post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    //findByUserId 이거 확인 필
    Page<Post> findByUserId(Integer userId, Pageable pageable);
    Page<Post> findAll(Pageable pageable);



}
