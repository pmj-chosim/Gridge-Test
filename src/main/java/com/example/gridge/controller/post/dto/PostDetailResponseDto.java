package com.example.gridge.controller.post.dto;


import com.example.gridge.repository.entity.Post.Post;
import com.example.gridge.repository.entity.Post.PostMedia;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "게시글 상세 응답 DTO")
@Getter
@AllArgsConstructor
public class PostDetailResponseDto {

    @Schema(description = "게시글 ID", example = "1")
    private Integer postId;

    @Schema(description = "작성자 유저 ID", example = "1")
    private Integer userId;

    @Schema(description = "게시글 내용", example = "오늘은 날씨가 좋네요!")
    private String content;

    @Schema(description = "게시글 생성 일시", example = "2023-10-01T12:34:56")
    private String createdAt;

    @Schema(description = "게시글 위치 정보", example = "서울특별시 강남구")
    private String location;

    @Schema(description = "좋아요 수", example = "10")
    private Integer likeCount;

    @Schema(description = "댓글 수", example = "5")
    private Integer commentCount;

    @Schema(description = "게시글에 첨부된 미디어 목록")
    private List<PostMedia> postMediaList;

    public static PostDetailResponseDto from(Post post){
        return new PostDetailResponseDto(
                post.getId(),
                post.getUser().getId(),
                post.getContent(),
                post.getCreatedAt().toString(),
                post.getLocation(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getPostMediaList().stream().toList()
        );
    }
}