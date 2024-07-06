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
@RequestMapping("/api/experience")
public class ExperiencesController {
    private final ExperiencesService experiencesService;

    public ExperiencesController(ExperiencesService experiencesService) {
        this.experiencesService = experiencesService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createExperience(@RequestBody ExperiencesDTO.ExperienceInfo experienceInfo) {
        ResponseDTO response = experiencesService.createExperience(experienceInfo);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @PutMapping("/update/{experienceId}")
    public ResponseEntity<ResponseDTO> updateExperience(@PathVariable Integer experienceId, @RequestBody ExperiencesDTO.ExperienceInfo experienceInfo) {
        ResponseDTO response = experiencesService.updateExperience(experienceId, experienceInfo);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteExperience(@PathVariable Integer id, @RequestBody UserDTO.UserIdDTO userIdDTO) {
        ResponseDTO response = experiencesService.deleteExperience(id, userIdDTO.getUser_id());
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }

    @GetMapping()
    @Operation(summary = "경험카드 모여있는 전체 페이지 뷰", description = "프로젝트를 처음 클릭했을 때 보여줄 경험들이 모여있는 페이지 입니다.")
    public ExperiencesDTO.ExperiencesCollectionPageResponse getExperiences(@RequestBody ExperiencesDTO.ExperiencesCollectionPageRequest experiencesCollectionPageRequest) {
        return experiencesService.findAllExpCollectionPage(experiencesCollectionPageRequest.getProject_id());
    }

    @GetMapping("/search")
    @Operation(summary = "하나의 프로젝트 대상 경험을 필터링", description = "사용자가 설정한 조건을 갖춘 경험들을 보여줍니다.")
    public List<ExperiencesDTO.ExperienceSearchResponse> getSearchExperiences(@RequestBody ExperiencesDTO.ExperienceSearchRequest experienceSearchRequest) {
        return experiencesService.findExperiencesByFilter(experienceSearchRequest);
    }
}