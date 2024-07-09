package com.pard.record_on_be.projects.controller;


import com.pard.record_on_be.projects.dto.ProjectsDTO;
import com.pard.record_on_be.projects.service.ProjectsService;
import com.pard.record_on_be.reference.dto.ReferenceDTO;
import com.pard.record_on_be.user.dto.UserDTO;
import com.pard.record_on_be.util.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectsController {
    private final ProjectsService projectsService;

    @PostMapping("")
    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성합니다.")
    public ResponseEntity<ResponseDTO> createProject(@RequestBody ProjectsDTO.Create projectCreateDTO) {
        ResponseDTO response = projectsService.createProject(projectCreateDTO);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list/{userId}")
    @Operation(summary = "프로젝트 카드들 보기", description = "사용자가 생성한 모든 프로젝트 카드들을 보여줍니다.")
    public ProjectsDTO.ReadAll getAllProjects(@PathVariable UUID userId) { return projectsService.findAll(userId); }

    @PostMapping("/{project_id}")
    @Operation(summary = "프로젝트 하나 읽어오기", description = "프로젝트 수정 시 필요한 정보들을 불러옵니다.")
    public ResponseDTO compactRead(@PathVariable Integer project_id, @RequestBody UserDTO.UserIdDTO userIdDTO) {
        return projectsService.compactReadById(project_id, userIdDTO);
    }

    @PostMapping("/search")
    @Operation(summary = "전체 프로젝트 대상 필터링", description = "사용자가 설정한 조건을 갖춘 프로젝트들을 보여줍니다.")
    public List<ProjectsDTO.ReadDefaultPage> getSearchProjects(@RequestBody ProjectsDTO.ProjectsSearchRequest projectsSearchRequest) {
        return projectsService.findProjectsByFilter(projectsSearchRequest);
    }

    @PutMapping("/finish/{project_id}")
    @Operation(summary = "프로젝트 완료", description = "프로젝트를 완료합니다.")
    public ResponseEntity<ResponseDTO> finishProject(@PathVariable Integer project_id, @RequestBody ProjectsDTO.Finish finishDTO) {
        ResponseDTO response = projectsService.finishProject(project_id, finishDTO);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }

    @PutMapping("/resume/{project_id}")
    @Operation(summary = "프로젝트 재개", description = "프로젝트를 재개합니다.")
    public ResponseEntity<ResponseDTO> resumeProject(@PathVariable("project_id") Integer projectId, @RequestBody UserDTO.UserIdDTO userIdDTO) {
        ResponseDTO response = projectsService.resumeProject(projectId, userIdDTO.getUser_id());
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "프로젝트 수정", description = "프로젝트를 수정합니다.")
    public ResponseEntity<ResponseDTO> updateProject(@PathVariable Integer projectId, @RequestBody ProjectsDTO.Create projectUpdateDTO) {
        ResponseDTO response = projectsService.updateProject(projectId, projectUpdateDTO, projectUpdateDTO.getUser_id());
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.")
    public ResponseEntity<ResponseDTO> deleteProject(@PathVariable Integer projectId, @RequestBody UserDTO.UserIdDTO userIdDTO) {
        ResponseDTO response = projectsService.deleteProject(projectId, userIdDTO.getUser_id());
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }

    @PostMapping("/reference")
    @Operation(summary = "프로젝트에 저장된 reference link를 전부 가져옵니다.", description = "경험 생성하며 저장한 관련 자료 링크들을 전부 모아 가져옵니다.")
    public List<ReferenceDTO.MetadataWithUrl> getReferences(@RequestBody ReferenceDTO.UrlCollectRequest request) {
        return projectsService.referenceResponse(request);
    }
}
