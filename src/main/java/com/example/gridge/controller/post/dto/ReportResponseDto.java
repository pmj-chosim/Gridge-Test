package com.example.gridge.controller.post.dto;


import com.example.gridge.repository.entity.Post.Report;
import com.example.gridge.repository.entity.Post.ReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Schema
@Getter
@AllArgsConstructor
public class ReportResponseDto {
    @Schema(description = "신고 ID", example = "301")
    private Integer reportId;

    @Schema(description = "신고 상태", example = "pending")
    private ReportStatus status;

    @Schema(description = "신고 생성 시간", example = "2023-10-05T14:48:00")
    private String createdAt;

    public static ReportResponseDto from(Report report){
        return new ReportResponseDto(
                report.getId(),
                report.getStatus(),
                report.getCreatedAt().toString()
        );
    }

}