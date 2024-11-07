package com.employee.advatixAPI.repository.report;

import com.employee.advatixAPI.entity.report.ReportAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportAttributeRepository extends JpaRepository<ReportAttributes, Integer> {
    List<ReportAttributes> findAllByReportId(Integer reportId);
}
