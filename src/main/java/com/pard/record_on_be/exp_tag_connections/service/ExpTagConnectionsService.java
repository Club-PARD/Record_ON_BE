package com.pard.record_on_be.exp_tag_connections.service;

import com.pard.record_on_be.exp_tag_connections.repo.ExpTagConnectionsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpTagConnectionsService {
    private final ExpTagConnectionsRepo expTagConnectionsRepo;
}
