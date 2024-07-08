package com.pard.record_on_be.s3.service;


import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.projects.repo.ProjectsRepo;
import com.pard.record_on_be.s3.uploader.S3Uploader;
import com.pard.record_on_be.util.ResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final ProjectsRepo projectsRepo;
    private final S3Uploader s3Uploader;


    // MultipartFile 을 받으면 업로더로 url 형태로 가공 후 전달
    public String uploadProfile(MultipartFile multipartFile) throws IOException {
        String url = s3Uploader.upload(multipartFile, "static/profile");
        System.out.println(url);
        return url;
    }


    // 프로젝트 아이디와 url을 받아와서 response로 보내줄 dto로 변환시켜서 return 시켜준
    @Transactional
    public ResponseDTO saveS3Url(Integer projectId, String url) {
        try {
            projectsRepo.findById(projectId).get().addPicture(url);
            return new ResponseDTO(true, "Project created successfully");
        } catch  (NoSuchElementException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while image Saving to S3 : " + e.getMessage());
        }
    }
}