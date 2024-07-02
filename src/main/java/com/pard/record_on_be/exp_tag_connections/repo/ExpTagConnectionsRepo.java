package com.pard.record_on_be.exp_tag_connections.repo;

import com.pard.record_on_be.exp_tag_connections.entity.ExpTagConnections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpTagConnectionsRepo extends JpaRepository<ExpTagConnections, Long> {
}
