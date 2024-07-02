package com.pard.record_on_be.project_data.controller;

import com.pard.record_on_be.project_data.service.ProjectDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectDataController {
    private final ProjectDataService projectDataService;
}
