package com.example.gridge.repository.entity.Post;


import com.example.gridge.repository.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Getter
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Comment create(Post post, User user, String content) {
        return new Comment(
                null,
                post,
                user,
                content,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
