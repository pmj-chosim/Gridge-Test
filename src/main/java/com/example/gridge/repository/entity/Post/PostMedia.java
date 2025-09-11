package com.example.gridge.repository.entity.Post;

import com.example.gridge.repository.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMedia {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String url;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    private Status originStatus;

    // 팩토리 메서드
    public static PostMedia create(String url, MediaType mediaType) {
        PostMedia postMedia = new PostMedia();
        postMedia.url = url;
        postMedia.mediaType = mediaType;
        postMedia.originStatus = Status.ACTIVE;
        return postMedia;
    }

}
