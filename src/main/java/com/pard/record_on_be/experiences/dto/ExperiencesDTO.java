package com.pard.record_on_be.experiences.dto;

import com.pard.record_on_be.experiences.entity.Experiences;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ExperiencesDTO {

    @Getter
    @Setter
    public static class ExperienceInfo {
        private UUID user_id;
        private Integer projects_id;
        private String title;
        private Date exp_date;
        private String free_content;
        private String common_question_answer;
        private List<Integer> tag_ids;
        private List<Integer> question_ids;
        private List<String> question_answers;
        private List<String> reference_links;
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
    public static class ExperiencesCollectionPageRequest {
        private UUID user_id;
        private Integer project_id;
    }

    @Getter
    @Setter
    public static class ExperiencesCollectionPageResponse {
        private String project_name;
        private String project_image;
        private Integer is_finished;
        private Date start_date;
        private Date finish_date;
        private String description;
        private String part;
        private List<ExperienceSearchResponse> experiences;

        public ExperiencesCollectionPageResponse(String project_name, String project_image, Integer is_finished, Date start_date, Date finish_date, String description, String part, List<ExperienceSearchResponse> experiences) {
            this.project_name = project_name;
            this.project_image = project_image;
            this.is_finished = is_finished;
            this.start_date = start_date;
            this.finish_date = finish_date;
            this.description = description;
            this.part = part;
            this.experiences = experiences;
        }
    }

    @Getter
    @Setter
    public static class CreateExperience {
        private UUID user_id;
        private Integer projects_id;
        private String title;
        private Date exp_date;
        private String free_content;
        private String common_question_answer;
    }

    @Getter
    @Setter
    public static class Read {
        private Integer id;
        private UUID user_id;
        private Integer projects_id;
        private String title;
        private Date exp_date;
        private String free_content;
        private String common_question_answer;

        public Read(Experiences experiences) {
            this.id = experiences.getId();
            this.user_id = experiences.getUser_id();
            this.projects_id = experiences.getProjects_id();
            this.title = experiences.getTitle();
            this.exp_date = experiences.getExp_date();
            this.free_content = experiences.getFree_content();
            this.common_question_answer = experiences.getCommon_question_answer();
        }
    }
}
