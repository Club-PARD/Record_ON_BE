package com.pard.record_on_be.exp_competencyTag_connections.repo;

import com.pard.record_on_be.exp_competencyTag_connections.entity.ExpCompetencyTagConnections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpCompetencyTagConnectionsRepo extends JpaRepository<ExpCompetencyTagConnections, Long> {
}
