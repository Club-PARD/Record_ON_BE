package com.pard.record_on_be.projects.controller;


import com.pard.record_on_be.projects.service.ProjectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectsController {
    private final ProjectsService projectsService;
}
