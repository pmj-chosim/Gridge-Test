package com.example.gridge.repository.entity.Post;

import com.example.gridge.repository.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;
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

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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
    public static Post create(User user, String content, VisibleStatus status, String location, List<String> mediaUrls, List<String> mediaTypes) {
        Post post = new Post();
        post.user = user;
        post.content = content;
        post.status = status;
        post.location = location;
        post.isDeleted = false;
        post.createdAt = new Timestamp(System.currentTimeMillis());
        post.updatedAt = new Timestamp(System.currentTimeMillis());
        post.likeCount = 0;
        post.commentCount = 0;

        // PostMedia 리스트 생성 및 연관 관계 설정
        if(mediaUrls!= null && mediaTypes != null && mediaUrls.size() != mediaTypes.size()) {
            throw new IllegalArgumentException("Media URLs and types lists must have the same size.");
        }

        for (int i = 0; i < mediaUrls.size(); i++) {
            String url = mediaUrls.get(i);
            MediaType type = MediaType.valueOf(mediaTypes.get(i));
            PostMedia media = PostMedia.create(url, type);
            post.addPostMedia(media);
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
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

}
