package com.pard.record_on_be.experiences.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.experiences.entity.Experiences;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class ExperiencesDTO {
    @Getter
    @Setter
    public static class Create {
        private Integer project_id;
        private String title;
        private Date exp_date;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read {
        private Integer id;
        private Integer project_id;
        private String title;
        private Date exp_date;

        public Read(Experiences experiences) {
            this.id = experiences.getId();
            this.project_id = experiences.getProject_id();
            this.title = experiences.getTitle();
            this.exp_date = experiences.getExp_date();
        }
    }
}
