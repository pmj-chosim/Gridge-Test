package com.example.gridge.controller.post.dto;


import com.example.gridge.repository.entity.Post.VisibleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Schema(description = "게시글 수정 요청 DTO")
@AllArgsConstructor
@Getter
public class PostUpdateRequestDto {
    @Schema(description = "수정할 게시글 내용", example = "수정된 내용입니다.", required = true)
    private String content;

    @Schema(description = "수정할 게시글 공개 상태 (예: PUBLIC, PRIVATE)", example = "PRIVATE", required = false)
    private VisibleStatus visibleStatus;
}