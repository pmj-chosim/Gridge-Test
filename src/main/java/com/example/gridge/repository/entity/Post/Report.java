package com.example.gridge.repository.entity.Post;

import com.example.gridge.repository.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Report {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    private String detail;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDate createdAt;

    public static Report create(User user, Post post, ReportReason reason, String detail) {
        return new Report(
                null,
                user,
                post,
                reason,
                detail,
                ReportStatus.PROCESSING,
                LocalDate.now()
        );
    }

    public void updateStatus(ReportStatus newStatus) {
        this.status = newStatus;
    }
}
