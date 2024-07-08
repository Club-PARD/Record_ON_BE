package com.pard.record_on_be.s3.controller;

import com.pard.record_on_be.projects.service.ProjectsService;
import com.pard.record_on_be.s3.service.S3Service;
import com.pard.record_on_be.util.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class S3Controller {
    private final S3Service s3Service;
    private final ProjectsService projectsService;


    @PostMapping("/s3/{projects_id}")
    @Operation(summary = "프로젝트 이미지 삽입하기", description = "이미지를 formdata로 보내주고, 올라간 이미지의 url을 받습니다.")
    public String uploadProfileImage(@PathVariable("projects_id") Integer projectsId, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        System.out.println("Received file: " + multipartFile.getOriginalFilename());
        System.out.println("File content type: " + multipartFile.getContentType());

        // projects_id와 image를 이용해 로직을 처리하는 부분
        String url = s3Service.uploadProfile(multipartFile);
        System.out.println("Url name : " + url);

        s3Service.saveS3Url(projectsId, url);
        return url;
    }

    @Operation(summary = "프로젝트 이미지 url 가져오기", description = "이미지를 가져오고자 하는 프로젝트의 id를 입력하면 해당 프로젝트의 이미지 url을 받습니다.")
    @GetMapping("/s3/{projects_id}")
    public ResponseDTO uploadProfileImage(@PathVariable("projects_id") Integer projectsId){
        return projectsService.getUrl(projectsId);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public String handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        return ex.getMessage(); // or customize error message
    }
}