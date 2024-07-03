package com.pard.record_on_be.exp_competencyTag_connections.service;

import com.pard.record_on_be.exp_competencyTag_connections.repo.ExpCompetencyTagConnectionsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpCompetencyTagConnectionsService {
    private final ExpCompetencyTagConnectionsRepo expCompetencyTagConnectionsRepo;
}
