package com.pard.record_on_be.experiences.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.experiences.entity.Experiences;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public static class ExperienceSearchRequest {
        private UUID user_id;
        private Integer project_id;
        private List<String> tag_name;
        private Date start_date;
        private Date finish_date;
        private String search_text;
    }

    @Getter
    @Setter
    public static class ExperienceSearchResponse {
        private Integer experience_id;
        private String experience_name;
        private List<Integer> tag_id;
        private List<String> tag_name;
        private Date exp_date;

        public ExperienceSearchResponse(Integer experience_id, String experience_name, List<Integer> tag_id, List<String> tag_name, Date exp_date) {
            this.experience_id = experience_id;
            this.experience_name = experience_name;
            this.tag_id = tag_id;
            this.tag_name = tag_name;
            this.exp_date = exp_date;
        }
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
