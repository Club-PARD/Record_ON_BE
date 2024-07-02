package com.pard.record_on_be.project_data.service;

import com.pard.record_on_be.project_data.repo.ProjectDataRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectDataService {
    private final ProjectDataRepo projectDataRepo;
}
