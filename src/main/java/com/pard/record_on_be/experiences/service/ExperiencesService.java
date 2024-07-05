package com.pard.record_on_be.experiences.service;


import com.pard.record_on_be.experiences.dto.ExperiencesDTO;
import com.pard.record_on_be.experiences.repo.ExperiencesRepo;
import com.pard.record_on_be.projects.dto.ProjectsDTO;
import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.projects.repo.ProjectsRepo;
import com.pard.record_on_be.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperiencesService {
    private final ExperiencesRepo experiencesRepo;
    private final ProjectsRepo projectsRepo;

    // 프론트에서 보내준 필터링 조건에 맞춘 데이터들을 보내주기
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperiencesByFilter(ExperiencesDTO.ExperienceSearchRequest experienceSearchRequest) {
        List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList;
        experienceSearchResponseList = findAll(experienceSearchRequest.getProject_id());

        return experienceSearchResponseList;
    }

    // 보내준 프로젝트 id에 해당하는 경험들을 전부 리턴
    public List<ExperiencesDTO.ExperienceSearchResponse> findAll(Integer projectId) {
        List<ExperiencesDTO.ExperienceSearchResponse> readDefaultPageList = new ArrayList<>();
        Projects projects = projectsRepo.findById(Long.valueOf(projectId)).get();
        projects.getExperiencesList().forEach(experience -> {
            readDefaultPageList.add(
                    new ExperiencesDTO.ExperienceSearchResponse(
                            experience.getId(),
                            experience.getTitle(),
                            findExperiencesTagId(experience.getTagList()),
                            findExperiencesTagName(experience.getTagList())));
        });
        return readDefaultPageList;
    }

    // tag에서 이름만 뽑아와서 list로 만들어주기
    public List<String> findExperiencesTagName(List<Tag> tagList) {
        List<String> experiencesTagNameList = new ArrayList<>();
        tagList.forEach(tag -> experiencesTagNameList.add(tag.getName()));
        return experiencesTagNameList;
    }

    // tag에서 id만 뽑아와서 list로 만들어주기
    public List<Integer> findExperiencesTagId(List<Tag> tagList) {
        List<Integer> experiencesTagIdList = new ArrayList<>();
        tagList.forEach(tag -> experiencesTagIdList.add(tag.getId()));
        return experiencesTagIdList;
    }


}
