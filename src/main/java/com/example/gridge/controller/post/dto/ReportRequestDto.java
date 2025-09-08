package com.example.gridge.controller.post.dto;


import com.example.gridge.repository.entity.Post.ReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "게시글 신고 요청 Dto")
public class ReportRequestDto {

    @Schema(description = "신고 사유 enum [SPAM, PRON, SCAM, ETC]", example="SPAM", required = true)
    private ReportReason reportReason;

    @Schema(description = "신고 내용", example = "노출이 많아 선정적입니다", required = false)
    private String reportContent;
}
