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
        private Integer projects_id;
        private String title;
        private Date exp_date;
        private String free_content;
        private String common_question_answer;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read {
        private Integer id;
        private Integer projects_id;
        private String title;
        private Date exp_date;
        private String free_content;
        private String common_question_answer;

        public Read(Experiences experiences) {
            this.id = experiences.getId();
            this.projects_id = experiences.getProjects_id();
            this.title = experiences.getTitle();
            this.exp_date = experiences.getExp_date();
            this.free_content = experiences.getFree_content();
            this.common_question_answer = experiences.getCommon_question_answer();
        }
    }
}
