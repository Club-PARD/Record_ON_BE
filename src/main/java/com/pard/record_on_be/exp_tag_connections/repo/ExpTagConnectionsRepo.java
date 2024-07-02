package com.pard.record_on_be.exp_tag_connections.repo;

import com.pard.record_on_be.exp_tag_connections.entity.ExpTagConnections;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpTagConnectionsRepo extends CrudRepository<ExpTagConnections, Long> {
}
