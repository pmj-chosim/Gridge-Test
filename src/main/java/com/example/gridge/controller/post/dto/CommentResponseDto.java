package com.example.gridge.controller.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "댓글 응답 Dto")
@AllArgsConstructor
@Getter
public class CommentResponseDto {

    @Schema(description = "댓글 ID", example = "1")
    private Integer id;

    @Schema(description = "댓글 내용", example = "우와 너무 부럽다")
    private String content;

    @Schema(description = "댓글 생성 시간", example = "2023-10-05T14:48:00")
    private String createdAt;

    public static CommentResponseDto from(Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

}