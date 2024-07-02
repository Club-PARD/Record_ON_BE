package com.pard.record_on_be.exp_ans_connections.service;

import com.pard.record_on_be.exp_ans_connections.repo.ExpAnsConnectionsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpAnsConnectionsService {
    private final ExpAnsConnectionsRepo expAnsConnectionsRepo;
}
