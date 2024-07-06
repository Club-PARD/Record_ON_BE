package com.pard.record_on_be.project_data.repo;

import com.pard.record_on_be.project_data.entity.ProjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDataRepo extends JpaRepository<ProjectData, Integer> {
    void deleteByExperiencesId(Integer experiencesId);
    List<ProjectData> findAllByExperiencesId(Integer experiencesId);
}
