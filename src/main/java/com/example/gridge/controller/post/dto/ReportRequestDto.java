package com.example.gridge.controller.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "게시글 신고 요청 Dto")
public class ReportRequestDto {
    @Schema(description = "신고할 게시글 ID", example = "1", required = true)
    private Integer postId;

    @Schema(description = "신고 사유", example="[porn, ...]", required = true)
    private String reportReason;

    @Schema(description = "신고 내용", example = "노출이 많아 선정적입니다", required = false)
    private String reportContent;
}
