package com.pard.record_on_be.s3.controller;

import com.pard.record_on_be.s3.service.S3Service;
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
    @PostMapping("/s3/{projects_id}")
    public String uploadProfileImage(@PathVariable("projects_id") Integer projectsId, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        // projects_id와 image를 이용해 로직을 처리하는 부분
        String url = s3Service.uploadProfile(multipartFile);
        s3Service.saveS3Url(projectsId, url);
        return url;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public String handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        return ex.getMessage(); // or customize error message
    }
}