package com.pard.record_on_be.exp_ans_connections.repo;

import com.pard.record_on_be.exp_ans_connections.entity.ExpAnsConnections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpAnsConnectionsRepo extends JpaRepository<ExpAnsConnections, Long> {
}
