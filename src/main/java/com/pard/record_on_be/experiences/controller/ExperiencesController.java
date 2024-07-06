package com.pard.record_on_be.experiences.controller;


import com.pard.record_on_be.experiences.dto.ExperiencesDTO;
import com.pard.record_on_be.experiences.service.ExperiencesService;
import com.pard.record_on_be.user.dto.UserDTO;
import com.pard.record_on_be.util.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
public class ExperiencesController {
    private final ExperiencesService experiencesService;

    public ExperiencesController(ExperiencesService experiencesService) {
        this.experiencesService = experiencesService;
    }

    @PostMapping()
    @Operation(summary = "경험 생성", description = "경험 생성")
    public ResponseEntity<ResponseDTO> createExperience(@RequestBody ExperiencesDTO.ExperienceInfo experienceInfo) {
        ResponseDTO response = experiencesService.createExperience(experienceInfo);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "경험 상세보기", description = "경험 상세보기")
    public ResponseDTO getExperienceDetails(@PathVariable("id") Integer experience_id) {
        return experiencesService.getExperienceDetails(experience_id);
    }

    @PostMapping("/search")
    @Operation(summary = "하나의 프로젝트 대상 경험을 필터링", description = "사용자가 설정한 조건을 갖춘 경험들을 보여줍니다.")
    public List<ExperiencesDTO.ExperienceSearchResponse> getSearchExperiences(@RequestBody ExperiencesDTO.ExperienceSearchRequest experienceSearchRequest) {
        return experiencesService.findExperiencesByFilter(experienceSearchRequest);
    }

    @PostMapping("/project")
    @Operation(summary = "경험카드 모여있는 전체 페이지 뷰", description = "프로젝트를 처음 클릭했을 때 보여줄 경험들이 모여있는 페이지 입니다.")
    public Object getExperiences(@RequestBody ExperiencesDTO.ExperiencesCollectionPageRequest experiencesCollectionPageRequest) {
        return experiencesService.findAllExpCollectionPage(experiencesCollectionPageRequest.getProject_id(), experiencesCollectionPageRequest.getUser_id());
    }

    @PutMapping("/{id}")
    @Operation(summary = "경험 수정", description = "경험 수정")
    public ResponseEntity<ResponseDTO> updateExperience(@PathVariable Integer id, @RequestBody ExperiencesDTO.ExperienceInfo experienceInfo) {
        ResponseDTO response = experiencesService.updateExperience(id, experienceInfo);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "경험 삭제", description = "경험 삭제")
    public ResponseEntity<ResponseDTO> deleteExperience(@PathVariable Integer id, @RequestBody UserDTO.UserIdDTO userIdDTO) {
        ResponseDTO response = experiencesService.deleteExperience(id, userIdDTO.getUser_id());
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }
}