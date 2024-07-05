package com.pard.record_on_be.experiences.service;


import com.pard.record_on_be.experiences.dto.ExperiencesDTO;
import com.pard.record_on_be.experiences.entity.Experiences;
import com.pard.record_on_be.experiences.repo.ExperiencesRepo;
import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.projects.repo.ProjectsRepo;
import com.pard.record_on_be.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperiencesService {
    private final ExperiencesRepo experiencesRepo;
    private final ProjectsRepo projectsRepo;

    // 프론트에서 보내준 필터링 조건에 맞춘 데이터들을 보내주기
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperiencesByFilter(ExperiencesDTO.ExperienceSearchRequest experienceSearchRequest) {
        List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList;
        // project의 experience 전체 탐색
        experienceSearchResponseList = findExperienceShortByProjectId(experienceSearchRequest.getProject_id());
        // 날짜 선택으로 1차 필터링
        experienceSearchResponseList = findExperienceShortByDate(experienceSearchRequest.getStart_date(), experienceSearchRequest.getFinish_date(), experienceSearchResponseList);
        // 태그 선택으로 2차 필터링
        experienceSearchResponseList = findExperiencesShortByTag(experienceSearchRequest.getTag_name(), experienceSearchResponseList);
        // 텍스트 검색으로 3차 필터링
        experienceSearchResponseList = findExperiencesShortByText(experienceSearchRequest.getSearch_text(), experienceSearchResponseList);
        return experienceSearchResponseList;
    }

    public List<ExperiencesDTO.ExperienceSearchResponse> findExperiencesShortByText(String text, List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList) {
        List<ExperiencesDTO.ExperienceSearchResponse> expFilteredList = experienceSearchResponseList.stream()
                .filter(experienceSearchResponse -> {
                    Optional<Experiences> experiencesOptional = experiencesRepo.findById(Long.valueOf(experienceSearchResponse.getExperience_id()));
                    if (experiencesOptional.isPresent()) {
                        Experiences experiences = experiencesOptional.get();
                        return experiences.getAnswerHistoriesList().stream()
                                .anyMatch(answerHistories -> answerHistories.getContent().contains(text));
                    }
                    return false;
                }).collect(Collectors.toList());

        experienceSearchResponseList.stream().filter(
                experienceSearchResponse -> {
                    Optional<Experiences> finalFilteredOptional = experiencesRepo.findById(Long.valueOf(experienceSearchResponse.getExperience_id()));
                    if (finalFilteredOptional.isPresent()) {
                        Experiences experiences = finalFilteredOptional.get();
                        return experiences.getFree_content().contains(text);
                    }
                    return false;
                }).forEach(expFilteredList::add);

        return expFilteredList;
    }

    // 보내준 experience들 중에 tag가 전부 들어간 것들을 전부 찾기
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperiencesShortByTag(List<String> tagNameList, List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList) {
        if (tagNameList == null || tagNameList.isEmpty()) {
            return experienceSearchResponseList;
        }
        return experienceSearchResponseList.stream().filter(
            experienceSearchResponse -> experienceSearchResponse.getTag_name().containsAll(tagNameList)
        ).collect(Collectors.toList());
    }


    // 보내준 프로젝트 id에 해당하는 경험들을 전부 리턴
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperienceShortByProjectId(Integer projectId) {
        List<ExperiencesDTO.ExperienceSearchResponse> readDefaultPageList = new ArrayList<>();
        Projects projects = projectsRepo.findById(Long.valueOf(projectId)).get();
        projects.getExperiencesList().forEach(experience -> {
            readDefaultPageList.add(
                    new ExperiencesDTO.ExperienceSearchResponse(
                            experience.getId(),
                            experience.getTitle(),
                            findExperiencesTagId(experience.getTagList()),
                            findExperiencesTagName(experience.getTagList()),
                            experience.getExp_date()
                    ));
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

    // 날짜 기준으로 sort하는 list 만들어주기
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperienceShortByDate(Date start_date, Date end_date, List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList) {
        if(start_date == null && end_date == null){ return experienceSearchResponseList; }
        else if(end_date == null){
            return experienceSearchResponseList.stream().filter(
                    experienceSearchResponse -> experienceSearchResponse.getExp_date().after(start_date)
            ).collect(Collectors.toList());
        } else if (start_date == null) {
            return experienceSearchResponseList.stream().filter(
                    experienceSearchResponse -> experienceSearchResponse.getExp_date().before(end_date)
            ).collect(Collectors.toList());
        } else {
            return experienceSearchResponseList.stream().filter(
                    experienceSearchResponse -> experienceSearchResponse.getExp_date().before(end_date) && experienceSearchResponse.getExp_date().after(start_date)
            ).collect(Collectors.toList());
        }
    }

    // 단 project view 페이지에 넘어가는 데이터
    public ExperiencesDTO.ExperiencesCollectionPageResponse findAllExpCollectionPage(Integer project_id) {
        Optional<Projects> projects = projectsRepo.findById(Long.valueOf(project_id));
        Projects project = projects.get();
        return new ExperiencesDTO.ExperiencesCollectionPageResponse(
                project.getName(),
                project.getPicture(),
                project.getIs_finished(),
                project.getStart_date(),
                project.getFinish_date(),
                project.getDescription(),
                project.getPart(),
                findExperienceShortByProjectId(project_id)
        );
    }
}
