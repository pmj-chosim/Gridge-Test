package com.example.gridge.controller.post.dto;


import com.example.gridge.repository.entity.Post.Like;
import com.example.gridge.repository.entity.Post.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Schema(description = "좋아요 응답 DTO")
@AllArgsConstructor
@Getter
public class LikeResponseDto {
    @Schema(description = "좋아요 id", example = "ID")
    private Integer likeId;

    @Schema(description = "좋아요가 눌린 게시글 id", example = "ID")
    private Integer postId;

    public static LikeResponseDto from(Like like) {
        return new LikeResponseDto(
                like.getId(),
                like.getPost().getId()
        );
    }
}