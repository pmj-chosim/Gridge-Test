package com.example.gridge.controller.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "댓글 생성 요청 Dto")
@Getter
@AllArgsConstructor
public class CommentRequestDto {

    @Schema(description = "댓글 내용", example = "넘 맛있겠다", maxLength = 50, required = true)
    private String content;
}
