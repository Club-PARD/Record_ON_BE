package com.pard.record_on_be.projects.service;

import com.pard.record_on_be.projects.dto.ProjectsDTO;
import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.projects.repo.ProjectsRepo;
import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectsService {
    private final ProjectsRepo projectRepo;
    private final UserRepo userRepo;

    // 사용자의 모든 project를 필요한 내용만 뽑아서 보내주기 위해 dto 만들어서 return 해주기
    public ProjectsDTO.ReadAll findAll(UUID userId) {
        List<ProjectsDTO.ReadDefaultPage> readDefaultPageList = new ArrayList<>();
        User user = userRepo.findById(userId).get();
        user.getProjects().forEach(project -> {
            readDefaultPageList.add(
                    new ProjectsDTO.ReadDefaultPage(
                            user,
                            project,
                            findProjectsTagId(project),
                            findProjectsTagName(project)));
        });
        return new ProjectsDTO.ReadAll(
                userRepo.findById(userId).get().getName(),
                readDefaultPageList
        );
    }

    //  competency에서 tag id만 뽑아와서 list로 만들어주기
    public List<Integer> findProjectsTagId(Projects projects) {
        List<Integer> projectsTagId = new ArrayList<>();
        projects.getCompetencyTagList().forEach(tag -> {
            projectsTagId.add(tag.getId());
        });
        return projectsTagId;
    }

    // competency에서 tag 이름만 뽑아와서 list로 만들어주기
    public List<String> findProjectsTagName(Projects projects) {
        List<String> projectsTagName = new ArrayList<>();
        projects.getCompetencyTagList().forEach(tag -> {
            projectsTagName.add(tag.getName());
        });
        return projectsTagName;
    }

    // 프론트에서 보내준 필터링 조건에 맞춘 데이터들을 보내주기
    public List<ProjectsDTO.ReadDefaultPage> findProjectsByFilter(ProjectsDTO.ProjectsSearchRequest projectsSearchRequest) {

    }
}
