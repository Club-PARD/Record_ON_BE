package com.pard.record_on_be.experiences.controller;


import com.pard.record_on_be.experiences.dto.ExperiencesDTO;
import com.pard.record_on_be.experiences.service.ExperiencesService;
import com.pard.record_on_be.projects.dto.ProjectsDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExperiencesController {
    private final ExperiencesService experiencesService;

    @GetMapping("/experience")
    @Operation(summary = "경험카드 모여있는 전체 페이지 뷰", description = "프로젝트를 처음 클릭했을 때 보여줄 경험들이 모여있는 페이지 입니다.")
    public ExperiencesDTO.ExperiencesCollectionPageResponse getExperiences(@RequestBody ExperiencesDTO.ExperiencesCollectionPageRequest experiencesCollectionPageRequest) {
        return experiencesService.findAllExpCollectionPage(experiencesCollectionPageRequest.getProject_id());
    }

    @GetMapping("/experience/search")
    @Operation(summary = "하나의 프로젝트 대상 경험을 필터링", description = "사용자가 설정한 조건을 갖춘 경험들을 보여줍니다.")
    public List<ExperiencesDTO.ExperienceSearchResponse> getSearchExperiences(@RequestBody ExperiencesDTO.ExperienceSearchRequest experienceSearchRequest) {
        return experiencesService.findExperiencesByFilter(experienceSearchRequest);
    }
}
