package com.pard.record_on_be.projects.service;

import com.pard.record_on_be.projects.repo.ProjectsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectsService {
    private final ProjectsRepo projectRepo;
}
