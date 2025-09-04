package com.example.gridge.controller.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "게시글 생성 요청 DTO")
@Getter
@AllArgsConstructor
public class PostCreateRequestDto {

    @Schema(description = "작성자 유저 ID", example = "1", required = true)
    private Integer userId;

    @Schema(description = "게시글 내용", example = "오늘은 날씨가 좋네요!", required = true)
    private String content;

    @Schema(description = "게시글 공개 상태 (예: PUBLIC, PRIVATE)", example = "PUBLIC", required=true)
    private String visibleStatus;

    @Schema(description = "게시글 위치 정보", example = "서울특별시 강남구")
    private String location;

    @Schema(description = "게시글에 첨부된 미디어 URL 목록", example = "[\"http://exa.image1.jpg\", \"http://~.com/video1.mp4\"]")
    private List<String> mediaUrls;
}
