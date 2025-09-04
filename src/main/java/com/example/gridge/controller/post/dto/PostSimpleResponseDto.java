package com.example.gridge.controller.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "게시글 생성/수정 응답 DTO")
@Getter
@AllArgsConstructor
public class PostSimpleResponseDto {

    @Schema(description = "게시글 ID", example = "1")
    public Integer postId;

    @Schema(description = "게시글 생성 시각", example = "2023-10-01T12:34:56")
    public String createdAt;

    @Schema(description = "게시글 최종 수정 시각", example = "2023-10-01T12:34:56")
    public String updatedAt;

    public static PostSimpleResponseDto from(Post post){
        return new PostSimpleResponseDto(
                post.getId(),
                post.getCreatedAt().toString(),
                post.getUpdatedAt().toString()
        );
    }

}