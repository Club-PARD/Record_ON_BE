package com.pard.record_on_be.s3.dto;

public class S3DTO {
    private String fileUrl;

    public S3DTO(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

}
