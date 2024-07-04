package com.pard.record_on_be.projects.controller;


import com.pard.record_on_be.projects.dto.ProjectsDTO;
import com.pard.record_on_be.projects.service.ProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectsController {
    private final ProjectsService projectsService;

    @GetMapping("/list/{userId}")
    @Operation(summary = "프로젝트 카드들 보기", description = "사용자가 생성한 모든 프로젝트 카드들을 보여줍니다.")
    public ProjectsDTO.ReadAll getAllProjects(@PathVariable UUID userId) { return projectsService.findAll(userId); }
}
