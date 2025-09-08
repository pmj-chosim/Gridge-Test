package com.example.gridge.repository;

import com.example.gridge.repository.entity.Post.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository  extends JpaRepository<Report, Integer> {
    Report save(Report report);
}
