package com.pard.record_on_be.projects.repo;


import com.pard.record_on_be.projects.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepo extends JpaRepository<Projects, Long> {
}
