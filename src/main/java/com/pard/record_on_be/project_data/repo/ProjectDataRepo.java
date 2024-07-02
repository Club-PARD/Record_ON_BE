package com.pard.record_on_be.project_data.repo;

import com.pard.record_on_be.project_data.entity.ProjectData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDataRepo extends JpaRepository<ProjectData, Long> {
}
