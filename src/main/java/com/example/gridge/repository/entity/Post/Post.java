package com.example.gridge.repository.entity.Post;

import com.example.gridge.repository.entity.user.User;
import com.example.gridge.repository.entity.user.VisibleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Post {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;

    @Enumerated(EnumType.STRING)
    private VisibleStatus status;

    private String location;

    private Integer likeCount;
    private Integer commentCount;

    private boolean isDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Post를 삭제해도 PostMedia는 남아야 하므로 REMOVE는 사용하지 않음
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<PostMedia> postMediaList = new ArrayList<>();

    // 댓글 기록도 함께 삭제
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    // 좋아요 기록도 함께 삭제 관리
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Like> likeList;

    // 신고 기록은 보존되어야 하므로 CascadeType.REMOVE 사용하지 않음
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Report> reportList;

    // 팩토리 메서드
    public static Post create(User user, String content, VisibleStatus status, String location, List<String> mediaUrls) {
        Post post = new Post();
        post.user = user;
        post.content = content;
        post.status = status;
        post.location = location;
        post.isDeleted = false;
        post.createdAt = LocalDateTime.now();
        post.updatedAt = LocalDateTime.now();
        post.likeCount = 0;
        post.commentCount = 0;

        // PostMedia 리스트 생성 및 연관 관계 설정
        if (mediaUrls != null) {
            for (String url : mediaUrls) {
                PostMedia media = PostMedia.create(url, MediaType.IMAGE);
                post.addPostMedia(media); // 연관 관계 설정
            }
        }
        return post;
    }

    // 연관 관계 편의 메서드
    public void addPostMedia(PostMedia media) {
        this.postMediaList.add(media);
        media.setPost(this);
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void update(String content, VisibleStatus status) {
        this.content = content;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
