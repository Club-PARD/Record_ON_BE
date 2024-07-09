package com.pard.record_on_be.projects.service;


import com.pard.record_on_be.competency_tag.entity.CompetencyTag;
import com.pard.record_on_be.competency_tag.repo.CompetencyTagRepo;
import com.pard.record_on_be.projects.dto.ProjectsDTO;
import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.projects.repo.ProjectsRepo;
import com.pard.record_on_be.stored_info.entity.StoredCompetencyTagInfo;
import com.pard.record_on_be.stored_info.repo.StoredCompetencyTagInfoRepo;
import com.pard.record_on_be.reference.dto.ReferenceDTO;
import com.pard.record_on_be.reference.service.ReferenceService;
import com.pard.record_on_be.user.dto.UserDTO;
import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.repo.UserRepo;
import com.pard.record_on_be.util.ResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectsService {
    private final ProjectsRepo projectsRepo;
    private final UserRepo userRepo;
    private final StoredCompetencyTagInfoRepo storedCompetencyTagInfoRepo;
    private final ReferenceService referenceService;

    // 프로젝트 생성
    @Transactional
    public ResponseDTO createProject(ProjectsDTO.Create projectCreateDTO) {
        try {

            // 필수 필드 및 날짜 검증
            String validationError = validateProjectCreateDTO(projectCreateDTO);
            if (validationError != null) {
                return new ResponseDTO(false, validationError);
            }

            // 없는 유저이면 예외 발생
            User user = userRepo.findById(projectCreateDTO.getUser_id())
                    .orElseThrow(() -> new NoSuchElementException("User with ID " + projectCreateDTO.getUser_id() + " not found"));

            // 프로젝트 하나 생성
            Projects project = Projects.builder()
                    .user_id(projectCreateDTO.getUser_id())
                    .name(projectCreateDTO.getName())
                    .start_date(projectCreateDTO.getStart_date())
                    .update_date(new Date()) // 현재 날짜로 업데이트 날짜 설정
                    .finish_date(projectCreateDTO.getFinish_date())
                    .description(projectCreateDTO.getDescription())
                    .part(projectCreateDTO.getPart())
                    .is_finished(0)
                    .user(user)
                    .build();

            // 프로젝트 저장
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

    // 프로젝트 수정시 필요한 간략한 정보 리턴
    public ResponseDTO compactReadById(Integer projectId, UserDTO.UserIdDTO userIdDTO) {
        try {
            Projects existingProject = projectsRepo.findById(projectId)
                    .orElseThrow(() -> new NoSuchElementException("Project with ID " + projectId + " not found"));
            if (existingProject.getUser_id().equals(userIdDTO.getUser_id())) {
                ProjectsDTO.CompactRead compactRead = ProjectsDTO.CompactRead.builder()
                        .project_name(existingProject.getName())
                        .description(existingProject.getDescription())
                        .part(existingProject.getPart())
                        .start_date(existingProject.getStart_date())
                        .finish_date(existingProject.getFinish_date())
                        .project_image(existingProject.getPicture())
                        .build();
                return new ResponseDTO(true, "Project compact read successfully", compactRead);
            }else {
                return new ResponseDTO(false, "You are not authorized to update this project");
            }
        } catch (NoSuchElementException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while updating the project: " + e.getMessage());
        }
    }

    // 태그들을 기준으로 필터링해서 보내주기
    public List<ProjectsDTO.ReadDefaultPage> findProjectsShortByCompetencyTags(List<String> competencyTagNameList, List<ProjectsDTO.ReadDefaultPage> readDefaultPageList) {
        List<ProjectsDTO.ReadDefaultPage> filteredList = new ArrayList<>();

        try {
            if (competencyTagNameList == null || competencyTagNameList.isEmpty()) {
                return readDefaultPageList;
            }

            for (ProjectsDTO.ReadDefaultPage readDefaultPage : readDefaultPageList) {
                if (!readDefaultPage.getCompetency_tag_name().equals("") && readDefaultPage != null && readDefaultPage.getCompetency_tag_name() != null &&
                        readDefaultPage.getCompetency_tag_name().containsAll(competencyTagNameList)) {
                    filteredList.add(readDefaultPage);
                }
            }
        } catch (Exception e) {
            // 예외 처리: 예상치 못한 예외가 발생할 경우
            System.err.println("Error while filtering projects by competency tags: " + e.getMessage());
            // 예외를 다시 던지거나, 기본값이나 빈 목록을 반환할 수 있음
            // 여기서는 빈 목록 반환 예시
            return new ArrayList<>();
        }
        return filteredList;
    }

    // 프로젝트 수정
    @Transactional
    public ResponseDTO updateProject(Integer projectId, ProjectsDTO.Create projectCreateDTO, UUID userId) {
        try {
            // 대상 프로젝트의 존재여부 확인
            Projects existingProject = projectsRepo.findById(projectId)
                    .orElseThrow(() -> new NoSuchElementException("Project with ID " + projectId + " not found"));

            // 필수 필드 및 날짜 검증
            String validationError = validateProjectCreateDTO(projectCreateDTO);
            if (validationError != null) {
                return new ResponseDTO(false, validationError);
            }

            // user id 검증 후 기존 프로젝트의 정보 수정 ( 삼항연산자 지워도 정상 작동함 )
            if (existingProject.getUser_id().equals(userId)) {
                Projects updatedProject = Projects.builder()
                        .id(existingProject.getId())
                        .user_id(existingProject.getUser_id())
                        .name(projectCreateDTO.getName() != null ? projectCreateDTO.getName() : existingProject.getName())
                        .start_date(projectCreateDTO.getStart_date() != null ? projectCreateDTO.getStart_date() : existingProject.getStart_date())
                        .finish_date(projectCreateDTO.getFinish_date() != null ? projectCreateDTO.getFinish_date() : existingProject.getFinish_date())
                        .description(projectCreateDTO.getDescription() != null ? projectCreateDTO.getDescription() : existingProject.getDescription())
                        .part(projectCreateDTO.getPart() != null ? projectCreateDTO.getPart() : existingProject.getPart())
                        .is_finished(existingProject.getIs_finished())
                        .update_date(new Date())
                        .user(existingProject.getUser())
                        .experiencesList(existingProject.getExperiencesList())
                        .projectDataList(existingProject.getProjectDataList())
                        .competencyTagList(existingProject.getCompetencyTagList())
                        .build();

                projectsRepo.save(updatedProject);

                return new ResponseDTO(true, "Project updated successfully", updatedProject);
            } else {
                return new ResponseDTO(false, "You are not authorized to update this project");
            }
        } catch (NoSuchElementException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while updating the project: " + e.getMessage());
        }
    }

    // 프로젝트 삭제
    public ResponseDTO deleteProject(Integer projectId, UUID userId) {
        Optional<Projects> projectOpt = projectsRepo.findById(projectId);

        // 프로젝트 존재시 삭제 진행
        if (projectOpt.isPresent()) {
            Projects project = projectOpt.get();
            // user id 검증
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

    // 프로젝트 완료 처리
    @Transactional
    public ResponseDTO finishProject(Integer projectId, ProjectsDTO.Finish finishDTO) {
        try {
            // 대상 프로젝트의 존재여부 확인
            Projects existingProject = projectsRepo.findById(projectId)
                    .orElseThrow(() -> new NoSuchElementException("Project with ID " + projectId + " not found"));

            // user id 검증
            if (existingProject.getUser_id().equals(finishDTO.getUser_id())) {
                // 프로젝트 완료 처리
                existingProject.finishProject();

                // CompetencyTag 처리
                List<CompetencyTag> newCompetencyTags = finishDTO.getCompetency_tag_ids().stream()
                        .map(competencyTagId -> {
                            StoredCompetencyTagInfo storedCompetencyTagInfo = storedCompetencyTagInfoRepo.findById(competencyTagId)
                                    .orElseThrow(() -> new NoSuchElementException("CompetencyTag with ID " + competencyTagId + " not found"));

                            return CompetencyTag.builder()
                                    .storedCompetencyTagId(storedCompetencyTagInfo.getId())
                                    .competencyTagName(storedCompetencyTagInfo.getCompetencyTagName())
                                    .projects(existingProject)
                                    .build();
                        })
                        .toList();

                // 기존 CompetencyTag 삭제
                existingProject.getCompetencyTagList().clear();
                projectsRepo.save(existingProject);

                // 새 CompetencyTag 추가
                newCompetencyTags.forEach(existingProject::addCompetencyTag);
                projectsRepo.save(existingProject);

                return new ResponseDTO(true, "Project finished successfully", existingProject);
            } else {
                return new ResponseDTO(false, "You are not authorized to finish this project");
            }
        } catch (NoSuchElementException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while finishing the project: " + e.getMessage());
        }
    }

    // 프로젝트 재개 처리
    @Transactional
    public ResponseDTO resumeProject(Integer projectId, UUID userId) {
        Optional<Projects> projectOpt = projectsRepo.findById(projectId);

        // 프로젝트 존재시 재개 처리 진행
        if (projectOpt.isPresent()) {
            Projects existingProject = projectOpt.get();
            if (existingProject.getUser_id().equals(userId)) {
                // 프로젝트 재개 처리
                existingProject.resumeProject();

                // CompetencyTag 삭제
                existingProject.getCompetencyTagList().clear();

                projectsRepo.save(existingProject);

                return new ResponseDTO(true, "Project resumed successfully", existingProject);
            } else {
                return new ResponseDTO(false, "You are not authorized to resume this project");
            }
        } else {
            return new ResponseDTO(false, "Project not found");
        }
    }

    // CreateDTO 에 대한 데이터 검증 메서드
    private String validateProjectCreateDTO(ProjectsDTO.Create projectCreateDTO) {
        if (projectCreateDTO.getName() == null || projectCreateDTO.getName().isEmpty()) {
            return "Project name is required";
        }
        if (projectCreateDTO.getStart_date() == null) {
            return "Start date is required";
        }
        if (projectCreateDTO.getFinish_date() == null) {
            return "Finish date is required";
        }
        if (projectCreateDTO.getDescription() == null || projectCreateDTO.getDescription().isEmpty()) {
            return "Description is required";
        }
        if (projectCreateDTO.getStart_date().after(projectCreateDTO.getFinish_date())) {
            return "Start date cannot be after finish date";
        }
        return null;
    }

    // 프로젝트의 대표 사진 URL 리턴
    public ResponseDTO getUrl(Integer projectId) {
        Optional<Projects> projectOpt = projectsRepo.findById(projectId);

        // 대상 프로젝트의 존재여부 확인
        if (projectOpt.isPresent()) {
            String picture = projectOpt.get().getPicture();
            return  picture != null
                    ? new ResponseDTO(true, "Get project picture successfully", projectOpt.get().getPicture())
                    : new ResponseDTO(false, "No picture in the project", null);
        }else {
                return new ResponseDTO(false, "Project not found");
        }
    }

    // URL 의 메타데이터 리턴
    public List<ReferenceDTO.UrlMetadata> getProjectUrlMetadata(ReferenceDTO.UrlCollectRequest urlCollectRequest) {
        // Find the project by ID
        Projects project = projectsRepo.findById(urlCollectRequest.getProject_id())
                .orElseThrow(() -> new NoSuchElementException("Project not found"));

        // Process the project data list and fetch metadata
        return project.getProjectDataList().stream()
                .map(projectData -> {
                    try {
                        // Fetch metadata and create UrlMetadata object
                        return referenceService.fetchMetadata(projectData.getReferences_link());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to fetch metadata", e);
                    }
                })
                .toList();
    }

    public List<ReferenceDTO.MetadataWithUrl> referenceResponse(ReferenceDTO.UrlCollectRequest urlCollectRequest) {
        try {
            // 사용자 정보를 Optional로 받아 옵니다.
            Optional<User> userOptional = userRepo.findById(urlCollectRequest.getUser_id());

            // 사용자 정보가 없을 경우 예외를 던집니다.
            if (!userOptional.isPresent()) {
                throw new NoSuchElementException("User with ID " + urlCollectRequest.getUser_id() + " not found");
            }

            // 프로젝트 정보를 Optional로 받아 옵니다.
            Optional<Projects> projectOptional = projectsRepo.findById(urlCollectRequest.getProject_id());

            // 프로젝트 정보가 없을 경우 예외를 던집니다.
            if (!projectOptional.isPresent()) {
                throw new NoSuchElementException("Project with ID " + urlCollectRequest.getProject_id() + " not found");
            }

            // 사용자가 해당 프로젝트를 가지고 있는지 확인합니다.
            User user = userOptional.get();
            Projects projects = projectOptional.get();
            if (!user.getProjects().contains(projects)) {
                throw new NoSuchElementException("User with ID " + urlCollectRequest.getUser_id() + " does not have the project with ID " + urlCollectRequest.getProject_id());
            }
        } catch (NoSuchElementException e) {
            // 예외를 로그에 남기고 빈 리스트를 반환합니다.
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }

        // 프로젝트 메타 데이터를 가져옵니다.
        return getProjectMetaWithUrl(urlCollectRequest);
    }


    // UUID 를 넘겨주면 해당 UUID 가 가지고 있는 정보를 project 페이지에 들어가는 간소화 정보로 변환하여 넘겨줌
    public List<ReferenceDTO.MetadataWithUrl> getProjectMetaWithUrl(ReferenceDTO.UrlCollectRequest urlCollectRequest) {
        try {
            // Find the project by ID
            Projects project = projectsRepo.findById(urlCollectRequest.getProject_id())
                    .orElseThrow(() -> new NoSuchElementException("Project not found"));

            // Process the project data list and fetch metadata
            return project.getProjectDataList().stream()
                    .map(projectData -> {
                        try {
                            // Fetch metadata and create UrlMetadata object
                            ReferenceDTO.UrlMetadata urlMetadata = referenceService.fetchMetadata(projectData.getReferences_link());
                            return new ReferenceDTO.MetadataWithUrl(projectData.getReferences_link(), urlMetadata.getTitle(), urlMetadata.getImageUrl());
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to fetch metadata", e);
                        }
                    })
                    .toList();
        } catch (NoSuchElementException e) {
            // Handle case where project is not found
            // You can log the error or rethrow as needed
            throw new NoSuchElementException("Project not found", e);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            // Log the error or rethrow as needed
            throw new RuntimeException("Failed to process project metadata", e);
        }
    }


    // UUID를 넘겨주면 해당 UUID가 가지고 있는 정보를 project 페이지에 들어가는 간소화 정보로 변환하여 넘겨줌
    public List<ProjectsDTO.ReadDefaultPage> findProjectsShortByUUID(UUID userId) {
        List<ProjectsDTO.ReadDefaultPage> readDefaultPageList = new ArrayList<>();
        try {
            Optional<User> optionalUser = userRepo.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.getProjects().forEach(project -> readDefaultPageList.add(
                        new ProjectsDTO.ReadDefaultPage(
                                user,
                                project,
                                findProjectsTagId(project),
                                findProjectsTagName(project))));
            } else {
                // Handle the case where user with given userId is not found
                // For example, throw an exception or log a message
                throw new EntityNotFoundException("User with id " + userId + " not found");
            }
        } catch (NoSuchElementException e) {
            // Handle case where Optional.get() throws NoSuchElementException
            // For example, throw a more specific exception or log a message
            throw new EntityNotFoundException("User with id " + userId + " not found", e);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            // For example, log the exception and throw a custom exception
            throw new RuntimeException("Error while finding projects for user with id " + userId, e);
        }
        return readDefaultPageList;
    }

    //  competency 에서 tag id만 뽑아와서 list 로 만들어주기
    public List<Integer> findProjectsTagId(Projects projects) {
        List<Integer> projectsTagId = new ArrayList<>();
        projects.getCompetencyTagList().forEach(
                competencyTag -> projectsTagId.add(competencyTag.getId()));
        return projectsTagId;
    }

    // competency 에서 tag 이름만 뽑아와서 list 로 만들어주기
    public List<String> findProjectsTagName(Projects projects) {
        List<String> projectsTagName = new ArrayList<>();
        projects.getCompetencyTagList().forEach(
                competencyTag -> projectsTagName.add(competencyTag.getCompetencyTagName()));
        return projectsTagName;
    }

    // 프론트에서 보내준 필터링 조건에 맞춘 데이터들을 보내주기
    public ResponseDTO findProjectsByFilter(ProjectsDTO.ProjectsSearchRequest projectsSearchRequest) {
        List<ProjectsDTO.ReadDefaultPage> readDefaultPageList = new ArrayList<>();

        try {
            // user 의 projects 전체 탐색
            readDefaultPageList = findProjectsShortByUUID(projectsSearchRequest.getUser_id());

            // 필수요소인 isfinished 로 1차 필터링
            readDefaultPageList = findProjectsShortByIsFinished(projectsSearchRequest.getIs_finished(), readDefaultPageList);

            // 날짜 선택으로 2차 필터링
            readDefaultPageList = findProjectsShortByDate(projectsSearchRequest.getStart_date(), projectsSearchRequest.getFinish_date(), readDefaultPageList);

            // 태그 선택으로 3차 필터링
            readDefaultPageList = findProjectsShortByCompetencyTags(projectsSearchRequest.getCompetency_tag_name(), readDefaultPageList);
        } catch (EntityNotFoundException e) {
            // 예외 처리: 사용자가 존재하지 않거나, 필터링에 필요한 정보가 부족할 경우
            // 예를 들어, 사용자 ID가 잘못된 경우 등
            // 에러 메시지 출력 또는 로깅
            System.err.println("Error while filtering projects: " + e.getMessage());
            // 예외를 다시 던지거나, 기본값이나 빈 목록을 반환할 수도 있음
            // 여기서는 빈 목록 반환 예시
            return new ResponseDTO(false, "Error while filtering projects" + e.getMessage());
        } catch (Exception e) {
            // 예외 처리: 그 외 예상치 못한 예외 상황 처리
            // 예를 들어, 데이터베이스 연결 문제 등
            System.err.println("Unexpected error while filtering projects: " + e.getMessage());
            // 예외를 다시 던지거나, 기본값이나 빈 목록을 반환할 수도 있음
            // 여기서는 빈 목록 반환 예시
            return new ResponseDTO(false, "Error while filtering projects" + e.getMessage());
        }
        return new ResponseDTO(true, "Successfully found " + readDefaultPageList.size() + " projects", readDefaultPageList);
    }

    public List<ProjectsDTO.ReadDefaultPage> findProjectsShortByIsFinished(Integer isFinished, List<ProjectsDTO.ReadDefaultPage> readDefaultPageList) {
        // readDefaultPageList가 null인지 확인합니다.
        if (readDefaultPageList == null) {
            throw new IllegalArgumentException("The readDefaultPageList cannot be null");
        }

        // isFinished가 유효한 값인지 확인합니다.
        if (isFinished == null || (isFinished != 0 && isFinished != 1 && isFinished != 2)) {
            throw new IllegalArgumentException("isFinished must be 0, 1, or 2");
        }

        // isFinished가 null이거나 2이면 전체 리스트를 반환합니다.
        if (isFinished == 2) {
            return readDefaultPageList;
        }

        List<ProjectsDTO.ReadDefaultPage> filteredList = new ArrayList<>();

        // readDefaultPageList를 순회하면서 조건에 맞는 요소를 필터링합니다.
        for (ProjectsDTO.ReadDefaultPage readDefaultPage : readDefaultPageList) {
            if (readDefaultPage == null) {
                throw new IllegalArgumentException("The readDefaultPageList contains a null element");
            }

            if (readDefaultPage.getIs_finished() != null && readDefaultPage.getIs_finished().equals(isFinished)) {
                filteredList.add(readDefaultPage);
            }
        }

        return filteredList;
    }


    // Date 를 기준으로 필터링 해서 보내주기
    public List<ProjectsDTO.ReadDefaultPage> findProjectsShortByDate(Date start_date, Date end_date, List<ProjectsDTO.ReadDefaultPage> readDefaultPageList) {
        try {
            // readDefaultPageList가 null인지 확인합니다.
            if (readDefaultPageList == null) {
                throw new IllegalArgumentException("The readDefaultPageList cannot be null");
            }

            // start_date가 end_date보다 나중인 경우 예외를 던집니다.
            if (start_date != null && end_date != null && start_date.after(end_date)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }

            if (start_date == null && end_date == null) {
                return readDefaultPageList;
            } else if (end_date == null) {
                return readDefaultPageList.stream()
                        .filter(readDefaultPage -> readDefaultPage.getStart_date().after(start_date))
                        .toList();
            } else if (start_date == null) {
                return readDefaultPageList.stream()
                        .filter(readDefaultPage -> readDefaultPage.getFinish_date().before(end_date))
                        .toList();
            } else {
                return readDefaultPageList.stream()
                        .filter(readDefaultPage -> readDefaultPage.getFinish_date().before(end_date) && readDefaultPage.getStart_date().after(start_date))
                        .toList();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid argument: " + e.getMessage());
            throw e; // 예외를 다시 던져 호출자가 처리할 수 있도록 합니다.
        } catch (Exception e) {
            System.err.println("Error while filtering projects by date: " + e.getMessage());
            // 예외를 다시 던지거나, 기본값이나 빈 목록을 반환할 수 있음
            // 여기서는 빈 목록 반환 예시
            return new ArrayList<>();
        }
    }

}
