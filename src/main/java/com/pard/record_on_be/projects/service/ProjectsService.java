package com.pard.record_on_be.projects.service;

import com.pard.record_on_be.projects.dto.ProjectsDTO;
import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.projects.repo.ProjectsRepo;
import com.pard.record_on_be.s3.service.S3Service;
import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.repo.UserRepo;
import com.pard.record_on_be.util.ResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectsService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectsService.class);
    private final ProjectsRepo projectsRepo;
    private final UserRepo userRepo;

    @Transactional
    public ResponseDTO createProject(ProjectsDTO.Create projectCreateDTO) {
        try {
            // 없는 유저이면 예외 발생
            User user = userRepo.findById(projectCreateDTO.getUser_id())
                    .orElseThrow(() -> new NoSuchElementException("User with ID " + projectCreateDTO.getUser_id() + " not found"));

            Projects project = Projects.builder()
                    .user_id(projectCreateDTO.getUser_id())
                    .name(projectCreateDTO.getName())
                    .start_date(projectCreateDTO.getStart_date())
                    .update_date(new Date()) // 현재 날짜로 업데이트 날짜 설정
                    .finish_date(projectCreateDTO.getFinish_date())
                    .description(projectCreateDTO.getDescription())
                    .picture("")
                    .is_finished(projectCreateDTO.getIs_finished())
                    .part(projectCreateDTO.getPart())
                    .user(user)
                    .build();

            project = projectsRepo.save(project);

            return new ResponseDTO(true, "Project created successfully", project);
        }catch (NoSuchElementException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while creating the project: " + e.getMessage());
        }
    }



    // 사용자의 모든 project를 필요한 내용만 뽑아서 보내주기 위해 dto 만들어서 return 해주기
    public ProjectsDTO.ReadAll findAll(UUID userId) {
        List<ProjectsDTO.ReadDefaultPage> readDefaultPageList;
        readDefaultPageList = findProjectsShortByUUID(userId);
        return new ProjectsDTO.ReadAll(
                userRepo.findById(userId).get().getName(),
                readDefaultPageList
        );
    }

    // UUID를 넘겨주면 해당 UUID가 가지고 있는 정보를 project 페이지에 들어가는 간소화 정보로 변환하여 넘겨줌
    public List<ProjectsDTO.ReadDefaultPage> findProjectsShortByUUID(UUID userId) {
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
        return readDefaultPageList;
    }

    //  competency에서 tag id만 뽑아와서 list로 만들어주기
    public List<Integer> findProjectsTagId(Projects projects) {
        List<Integer> projectsTagId = new ArrayList<>();
        projects.getCompetencyTagList().forEach(
                competencyTag -> {
                    projectsTagId.add(competencyTag.getId());
        });
        return projectsTagId;
    }

    // competency에서 tag 이름만 뽑아와서 list로 만들어주기
    public List<String> findProjectsTagName(Projects projects) {
        List<String> projectsTagName = new ArrayList<>();
        projects.getCompetencyTagList().forEach(
                competencyTag -> {
                    projectsTagName.add(competencyTag.getName());
        });
        return projectsTagName;
    }

    // 프론트에서 보내준 필터링 조건에 맞춘 데이터들을 보내주기
    public List<ProjectsDTO.ReadDefaultPage> findProjectsByFilter(ProjectsDTO.ProjectsSearchRequest projectsSearchRequest) {
        List<ProjectsDTO.ReadDefaultPage> readDefaultPageList;
        // user의 projects 전체 탐색
        readDefaultPageList = findProjectsShortByUUID(projectsSearchRequest.getUser_id());

        // 필수요소인 isfinished로 1차 필터링
        readDefaultPageList = findProjectsShortByIsFinished(projectsSearchRequest.getIs_finished(), readDefaultPageList);

        // 날짜 선택으로 2차 필터링
        readDefaultPageList = findProjectsShortByDate(projectsSearchRequest.getStart_date(), projectsSearchRequest.getFinish_date(), readDefaultPageList);

        // 태그 선택으로 3차 필터링
        readDefaultPageList = findProjectsShortByCompetencyTags(projectsSearchRequest.getCompetency_tag_name(), readDefaultPageList);



        return readDefaultPageList;
    }

    // 0 > 종료 안된 프로젝트 1 > 종료된 프로젝트 2 > 둘 다
    public List<ProjectsDTO.ReadDefaultPage> findProjectsShortByIsFinished(Integer isFinished, List<ProjectsDTO.ReadDefaultPage> readDefaultPageList) {
        if(isFinished == 2){ return readDefaultPageList;}
        return readDefaultPageList.stream().filter(
                readDefaultPage -> readDefaultPage.getIs_finished().equals(isFinished)
        ).collect(Collectors.toList());
    }

    // Date를 기준으로 필터링 해서 보내주기
    public List<ProjectsDTO.ReadDefaultPage> findProjectsShortByDate(Date start_date, Date end_date, List<ProjectsDTO.ReadDefaultPage> readDefaultPageList) {
        if(start_date == null && end_date == null){ return readDefaultPageList; }
        else if(end_date == null){
            return readDefaultPageList.stream().filter(
                    readDefaultPage -> readDefaultPage.getStart_date().after(start_date)
            ).collect(Collectors.toList());
        } else if (start_date == null) {
            return readDefaultPageList.stream().filter(
                    readDefaultPage -> readDefaultPage.getFinish_date().before(end_date)
            ).collect(Collectors.toList());
        } else {
            return readDefaultPageList.stream().filter(
                    readDefaultPage -> readDefaultPage.getFinish_date().before(end_date) && readDefaultPage.getStart_date().after(start_date)
            ).collect(Collectors.toList());
        }
    }

    // 태그들을 기준으로 필터링해서 보내주기
    public List<ProjectsDTO.ReadDefaultPage> findProjectsShortByCompetencyTags(List<String> competencyTagNameList, List<ProjectsDTO.ReadDefaultPage> readDefaultPageList) {
        if (competencyTagNameList == null || competencyTagNameList.isEmpty()) {
            return readDefaultPageList;
        }
        return readDefaultPageList.stream().filter(
                readDefaultPage -> readDefaultPage.getCompetency_tag_name().containsAll(competencyTagNameList)
        ).collect(Collectors.toList());
    }

    public ResponseDTO deleteProject(Integer projectId, UUID userId) {
        Optional<Projects> projectOpt = projectsRepo.findById(projectId);
        if (projectOpt.isPresent()) {
            Projects project = projectOpt.get();
            if (project.getUser_id().equals(userId)) {
                projectsRepo.delete(project);
                return new ResponseDTO(true, "Project deleted successfully");
            } else {
                return new ResponseDTO(false, "You are not authorized to delete this project");
            }
        } else {
            return new ResponseDTO(false, "Project not found");
        }
    }

}
