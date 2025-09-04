package com.example.gridge.controller.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Schema(description = "좋아요 응답 DTO")
@AllArgsConstructor
@Getter
public class LikeResponseDto {
    @Schema(description = "좋아요 상태 반환(예: 'liked', 'unliked')", example = "liked", required = true)
    private String status;

    public static LikeResponseDto from(String status) {
        return new LikeResponseDto(status);
    }
}