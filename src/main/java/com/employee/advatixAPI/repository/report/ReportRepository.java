package com.employee.advatixAPI.repository.report;

import com.employee.advatixAPI.entity.report.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
    List<String> findReportNameByReportId(Integer reportId);
}
