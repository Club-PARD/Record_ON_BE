package com.pard.record_on_be.exp_tag_connections.controller;


import com.pard.record_on_be.exp_tag_connections.service.ExpTagConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpTagConnectionsController {
    private  final ExpTagConnectionsService expTagConnectionsService;
}
