package com.pard.record_on_be.exp_ans_connections.controller;

import com.pard.record_on_be.exp_ans_connections.service.ExpAnsConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpAnsConnectionsController {
    private final ExpAnsConnectionsService expAnsConnectionsService;
}
