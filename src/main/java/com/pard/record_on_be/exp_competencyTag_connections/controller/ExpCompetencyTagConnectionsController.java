package com.pard.record_on_be.exp_competencyTag_connections.controller;

import com.pard.record_on_be.exp_competencyTag_connections.service.ExpCompetencyTagConnectionsService;
import lombok.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpCompetencyTagConnectionsController {
    private final ExpCompetencyTagConnectionsService expCompetencyTagConnectionsService;
}
