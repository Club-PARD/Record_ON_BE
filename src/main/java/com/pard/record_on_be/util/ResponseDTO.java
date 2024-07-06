package com.pard.record_on_be.util;

import com.pard.record_on_be.experiences.dto.ExperiencesDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDTO {
    // Getters and Setters
    @Getter
    private boolean success;
    private String message;
    private Object experience;


    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseDTO(boolean success, String message, ExperiencesDTO.Read experience) {
        this.success = success;
        this.message = message;
        this.experience = experience;
    }

    public ResponseDTO(boolean success, String message, ExperiencesDTO.ExperienceDetailsResponse experienceDetailsResponse) {
        this.success = success;
        this.message = message;
        this.experience = experienceDetailsResponse;
    }
}
