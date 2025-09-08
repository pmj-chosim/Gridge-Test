package com.example.gridge.repository;

import com.example.gridge.repository.entity.Post.Report;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository {
    Report save(Report report);
}
